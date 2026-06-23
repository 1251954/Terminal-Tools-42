package Interface;

import java.util.*;
import java.io.*;
import java.nio.file.Files; // Faster file reading
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.awt.Toolkit;
import java.util.stream.Collectors;

public class Improve {

    //TODO: IMPROVE COHESION.
    //TODO: INTEGRATE INTO THE OPTION MENU.
    //TODO: DELEGATE INITIALIZATION RESPONSABILITY TO BOOTSTRAP (COLOR AND LOG FILE LOCATION)
    //TODO: UTILIZE UTILS TO HANDLE USER ERROR AND MISSINPUT EXCEPTIONS.
    //TODO: PREVENT USER TO DEFORMAT THE TASK PROGRESS BAR.
    //TODO: SPACE OUT THE OUTPUT FORMATTING SO EVERYTHING ISN'T "GLUED TOGETHER".

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_HIDE_CURSOR = "\u001B[?25l";
    private static final String ANSI_SHOW_CURSOR = "\u001B[?25h";
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + "/activity_log.txt";
    private static final Random rand = new Random();

    enum TaskType { PRODUCTIVITY, LEISURE }

    static class Task {
        String name;
        TaskType type;

        Task(String name, TaskType type) {
            this.name = name;
            this.type = type;
        }
    }

    static void main(String[] args) {
        // Restore terminal even if user force-quits (Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            setTerminalRaw(false);
            System.out.print(ANSI_SHOW_CURSOR);
        }));

        prepareEnvironment();        
        List<Task> allTasks = new ArrayList<>(Arrays.asList(
            new Task("Do push-ups", TaskType.PRODUCTIVITY),
            new Task("Practice Typing", TaskType.PRODUCTIVITY),
            new Task("Study Programming", TaskType.PRODUCTIVITY),
            new Task("Study Math", TaskType.PRODUCTIVITY),
            new Task("Relax", TaskType.LEISURE),
            new Task("Reflect", TaskType.LEISURE)
        ));

        int streak = calculateStreak();
        String greeting = getGreeting();
        String streakMsg = (streak > 0) ? ANSI_GREEN + " [Current Streak: " + streak + " days]" + ANSI_RESET : ""; 
        showLoadingBar(greeting + " MR.ROCHETEAU" + streakMsg);

        Scanner configScanner = new Scanner(System.in);

        System.out.print("\nView History? [Y/N]: ");
        if (configScanner.next().equalsIgnoreCase("Y")) {
            printHistory();
        }

        System.out.println("\n--- CONFIGURATION MENU ---");
        System.out.print("Add new tasks [Y/N]? ");
        if (configScanner.next().equalsIgnoreCase("Y")) {
            configScanner.nextLine(); // consume newline
            while (true) {
                System.out.print("Name (or 'done'): ");
                String name = configScanner.nextLine();
                if (name.equalsIgnoreCase("done")) break;
                System.out.print("Type (P for Productivity, L for Leisure): ");
                String typeInput = configScanner.nextLine();
                TaskType type = typeInput.equalsIgnoreCase("L") ? TaskType.LEISURE : TaskType.PRODUCTIVITY;
                if (!name.trim().isEmpty()) allTasks.add(new Task(name, type));
            }
        }

        System.out.print("Productivity probability (0-100%): ");
        int prodProb = configScanner.nextInt();

        System.out.print("Tasks per achievement: ");
        int achievementThreshold = configScanner.nextInt();

        System.out.print("Total tasks today: ");
        int totalTasks = configScanner.nextInt();

        System.out.print("Minutes per task: ");
        int minutesPerTask = configScanner.nextInt();

        setTerminalRaw(true);
        System.out.print(ANSI_HIDE_CURSOR);

        long sessionStart = System.currentTimeMillis();
        int completedThisSession = 0;

        try {
            for (int i = 1; i <= totalTasks; i++) {
                Task currentTask = getTaskBasedOnProbability(allTasks, prodProb);
                runTaskTimer(i, totalTasks, currentTask, minutesPerTask);
                completedThisSession++;
                Toolkit.getDefaultToolkit().beep();
                logTask(currentTask, minutesPerTask);
                if (completedThisSession % achievementThreshold == 0) {
                    System.out.print("\n" + ANSI_YELLOW + ">> Congratulations! You've completed " + completedThisSession + " tasks this session!!!" + ANSI_RESET + "\n");
                } else {
                    System.out.println();
                }
            }
        } finally {
            setTerminalRaw(false);
            System.out.print(ANSI_SHOW_CURSOR);
        }

        long sessionEnd = System.currentTimeMillis();
        printSummary(totalTasks, sessionEnd - sessionStart);
    }

    private static Task getTaskBasedOnProbability(List<Task> tasks, int prodPercent) {
        int roll = rand.nextInt(101);
        TaskType targetType = (roll <= prodPercent) ? TaskType.PRODUCTIVITY : TaskType.LEISURE;
        List<Task> filtered = tasks.stream()
            .filter(t -> t.type == targetType)
            .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            return tasks.get(rand.nextInt(tasks.size()));
        }
        return filtered.get(rand.nextInt(filtered.size()));
    }
    private static int calculateStreak() {
        File f = new File(LOG_FILE);
        if (!f.exists()) return 0;

        try {
            // Read all lines at once (Much faster than Scanner line-by-line)
            List<String> lines = Files.readAllLines(f.toPath());
            Set<String> uniqueDates = new TreeSet<>(); // Automatically sorts dates

            for (String line : lines) {
                if (line.length() > 11 && line.startsWith("[")) {
                    uniqueDates.add(line.substring(1, 11)); // Extract "yyyy-MM-dd"
                }
            }

            if (uniqueDates.isEmpty()) return 0;

            List<LocalDate> dates = new ArrayList<>();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (String d : uniqueDates) {
                try { dates.add(LocalDate.parse(d, dtf)); } catch (Exception e) {}
            }
            return calculateSimpleStreak(dates);

        } catch (IOException e) {
            return 0;
        }
    }

    private static int calculateSimpleStreak(List<LocalDate> dates) {
        if (dates.isEmpty()) return 0;
        int streak = 0;
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1); 
        LocalDate lastLog = dates.get(dates.size() - 1);
        if (!lastLog.equals(today) && !lastLog.equals(yesterday)) {
            return 0;
        }

        LocalDate currentTarget = lastLog;
        for (int i = dates.size() - 1; i >= 0; i--) {
            if (dates.get(i).equals(currentTarget)) {
                streak++;
                currentTarget = currentTarget.minusDays(1);
            }
        }
        return streak;
    }

    private static void printHistory() {
        System.out.println(ANSI_CYAN + "\n--- LAST 5 ACTIVITIES ---" + ANSI_RESET);
        File f = new File(LOG_FILE);
        if (!f.exists()) {
            System.out.println("No history found.");
            return;
        }
        LinkedList<String> lines = new LinkedList<>();
        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                lines.add(s.nextLine());
                if (lines.size() > 5) lines.removeFirst();
            }
        } catch (FileNotFoundException e) {}

        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println("-------------------------");
    }

    private static void runTaskTimer(int current, int total, Task task, int mins) {
        long totalSecs = mins * 60L;
        long remainingSecs = totalSecs;
        boolean paused = false;

        String label = (task.type == TaskType.PRODUCTIVITY) ? "[PROD]" : "[LEISURE]";

        while (remainingSecs >= 0) {
            try {
                if (System.in.available() > 0) {
                    int input = System.in.read();
                    if (input == 'p' || input == 'P') paused = !paused;
                }

                if (paused) {
                    System.out.print("\rTask " + current + "/" + total + ": " + task.name + " " + label + ANSI_YELLOW + " [PAUSED] (P)" + ANSI_RESET + "              ");
                    Thread.sleep(200);
                } else {
                    long m = remainingSecs / 60;
                    long s = remainingSecs % 60;
                    double progressDone = (double) (totalSecs - remainingSecs) / totalSecs;
                    int percent = (int) (progressDone * 100);
                    String bar = getProgressBar(percent, 10);
                    String color = (remainingSecs <= 30) ? ANSI_RED : ANSI_CYAN;
                    System.out.printf("\rTask %d/%d: %s %s %s [%02d:%02d : %s] %d%% %s", current, total, task.name, label, color, m, s, bar, percent, ANSI_RESET);
                    Thread.sleep(1000);
                    remainingSecs--;
                }
            } catch (IOException | InterruptedException e) {
                break;
            }
        }
        System.out.print("\rTask " + current + "/" + total + ": " + task.name + " " + label + " [COMPLETED]                                     ");
    }

    private static String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) return "Good Morning";
        if (hour >= 12 && hour < 18) return "Good Afternoon";
        return "Good Night";
    }

    private static void prepareEnvironment() {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) dir.mkdir();
    }

    private static void setTerminalRaw(boolean raw) {
        try {
            String mode = raw ? "-echo -icanon" : "echo icanon";
            String[] cmd = {"/bin/sh", "-c", "stty " + mode + " < /dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception ignored) {}
    }

    private static String getProgressBar(int percent, int totalBlocks) {
        int filled = (int) Math.round((percent / 100.0) * totalBlocks);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalBlocks; i++) {
            sb.append(i < filled ? "|" : " ");
        }
        return sb.toString();
    }

    private static void showLoadingBar(String msg) {
        System.out.println(msg);
        for (int i = 0; i <= 100; i += 5) {
            StringBuilder b = new StringBuilder("[");
            int filled = i / 10;
            for (int j = 0; j < 10; j++) b.append(j < filled ? "|" : " ");
            System.out.print("\rInitializing improvement " + b + "] " + i + "%");
            try { Thread.sleep(100); } catch (Exception ignored) {}
        }
        System.out.println();
    }

    private static void logTask(Task t, int m) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true); PrintWriter pw = new PrintWriter(fw)) {
            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.printf("[%s] Task: %s [%s]. Time: [%02d: %02d: 00]%n", ts, t.name, t.type, m/60, m%60);
        } catch (IOException ignored) {}
    }

    private static void printSummary(int n, long ms) {
        long totalSeconds = ms / 1000;
        long h = totalSeconds / 3600;
        long m = (totalSeconds % 3600) / 60;
        long s = totalSeconds % 60;
        System.out.println("\n--- SESSION SUMMARY ---");
        System.out.printf("Tasks Done: %d | Elapsed: [%02d hours: %02d minutes: %02d seconds]\n", n, h, m, s);
    }
}