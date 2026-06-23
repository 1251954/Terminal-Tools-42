package Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class Utils {

    static public String readLineFromConsole(String prompt) {
        try {
            System.out.print("\n" + prompt);

            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);

            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public int readIntegerFromConsole(String prompt) {
        do {
            try {
                String input = readLineFromConsole(prompt);

                int value = Integer.parseInt(input);

                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number format.");
            }
        } while (true);
    }

    static public double readDoubleFromConsole(String prompt) {
        do {
            try {
                String input = readLineFromConsole(prompt);

                double value = Double.parseDouble(input);

                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number format.");
            }
        } while (true);
    }

    static public boolean confirm(String message) {
        String input;
        do {
            input = Utils.readLineFromConsole(message);
        } while (!Objects.requireNonNull(input).equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N"));

        return input.equalsIgnoreCase("Y");
    }

    static public Object showAndSelectOne(List list, String header) {
        showList(list, header);
        return selectsObject(list);
    }

    static public int showAndSelectIndex(List list, String header) {
        showList(list, header);
        return selectsIndex(list);
    }

    static public void showList(List list, String header) {
        System.out.println(header);

        int index = 0;
        for (Object o : list) {
            index++;

            System.out.println("  " + index + " - " + o.toString());
        }
        //System.out.println();
        System.out.println("  0 - Cancel");
    }

    static public Object selectsObject(List list) {
        String input;
        int value;
        do {
            input = Utils.readLineFromConsole("Type your option: ");
            value = Integer.parseInt(input);
        } while (value < 0 || value > list.size());

        if (value == 0) {
            return null;
        } else {
            return list.get(value - 1);
        }
    }

    static public int selectsIndex(List list) {
        String input;
        int value;
        do {
            input = Utils.readLineFromConsole("Type your option: ");
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                value = -1;
            }
        } while (value < 0 || value > list.size());

        return value - 1;
    }

    public static String truncate(String string, int max) {

        if (string == null) return ""; // null prevention.

        return string.length() <= max ? string : string.substring(0, max - 1) + "...";
    }

    //sort of a debugging tool, shows the elapsed time of a runnable.
    public static long timeFunction(Runnable task) {
        int timeReference = 1000000; // in this case to convert nano to ms.
        long start = System.nanoTime();
        task.run();
        return (System.nanoTime() - start) / timeReference;
    }

    public static void classify (long elapsedTime, boolean isComplex){

        String reset= "\u001B[0m";
        String green = "\u001B[32m";
        String red = "\u001B[31m";
        String yellow = "\u001B[33m";

        if(!isComplex){
            if(elapsedTime < 50){
                System.out.printf("Elapsed Time: %d. %sTest %s",elapsedTime,green,reset);
            }
        }
        else{
            System.out.printf("Elapsed Time: %d. Test%s",elapsedTime,red);
        }
    }
}