package Frame;

import javax.swing.*;

class window extends JFrame {
    window(String title, int x, int y) {
        setTitle(title);
        setSize(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    window(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
