import Interface.UI;

void main() {
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.run();

    try {
        UI console = new UI();
        console.run();
    } catch (Exception e) {
        e.printStackTrace();
    }
}