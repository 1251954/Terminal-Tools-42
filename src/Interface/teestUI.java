package Interface;

import Utils.Utils;

public class teestUI implements Runnable {

    @Override
    public void run() {
        Utils.classify(Utils.timeFunction(() -> System.out.println("Hello World")),false);
    }
}
