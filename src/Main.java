import Interface.UI;

public class Main {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run();

        try {
            UI console = new UI();
            console.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}