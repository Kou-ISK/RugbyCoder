package Frame;

import javax.swing.*;

public class Window extends JFrame {
    public Window(String title, int x, int y) {
        setTitle(title);
        setSize(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Window(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
