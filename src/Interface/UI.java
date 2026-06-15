package Interface;

import Utils.Utils;
import Utils.Utils.*;
import java.util.ArrayList;
import java.util.List;

public class UI implements Runnable {

    public UI() {
    }

    public void run() {
        printHeader();
        List<MenuItem> options = new ArrayList<MenuItem>();

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n--- MAIN MENU --------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

    public void printHeader(){
        String trademark = "- Rocheteau © 2026";
      System.out.printf("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   ⠀⢀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   ⢠⣾⣿⣿⣷⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀  ⢠⣿⣿⣿⣿⣿⣿⣿⣦⡀⠒⢶⣄⠀⠀⠀⠀⠀⠀⠀\n" +
              "⠀⢰⣶⣷⣶⣶⣤⣄⠀⣠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣾⣿⡆⠀⠀⠀⠀⠀⠀\n" +
              "⠀⢿⣿⣿⣿⣿⡟⢁⣄⠙⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀\n" +
              "⠀⠘⣿⣿⣿⣿⣧⡈⠻⢷⣦⣄⡉⠛⠿⢿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀\n" +
              "⠀⠀⠈⠻⣿⣿⣿⣿⣶⣄⡈⠙⠻⢷⣶⣤⣄⣈⡉⠛⠛⠛⠃⢠⣀⣀⡀⠀⠀⠀\n" +
              "⠀⠀⠀⠀⠈⠙⠻⢿⣿⣿⣿⣿⣶⣦⣤⣍⣉⠙⠛⠛⠛⠿⠃⢸⣿⣿⣿⣷⡀⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠻⠿⣿⣿⣿⣿⣿⣿⣿⣷⣶⣶⣾⣿⣿⣿⣿⣿⣧⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠙⠛⠻⠏⠀⠉⠻⢿⣿⣿⣿⣿⠿⠋⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
              "⠀⠀⠀⠀⠀⠀⠀%s⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀",trademark);
    }

}