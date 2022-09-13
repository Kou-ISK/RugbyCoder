package Frame;

import Logic.Logic;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static String path;
    private static String mediaName;

    public static void main(String[] args) throws Exception {
//        TODO パッケージにまとめる
//        TODO 絶対パスの取得
        Logic logic = new Logic();
        mainView mv = new mainView("Main", 500, 500);
        mv.setVisible(true);

        mv.button.addActionListener(e -> {
            System.out.println(mv.pathField.getText());
            path = mv.pathField.getText();
            mediaName = mv.mediaNameField.getText();
            logic.setFilePath(mv.pathField.getText());
            logic.setMediaName(mv.mediaNameField.getText());
            MakeFrames mf = new MakeFrames();
            try {
                mf.makeFrames(path, mediaName);
                mv.setVisible(false);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


    }

    static class mainView extends JFrame {

        private String filePath;
        private String mediaName;
        Button button;
        JTextField pathField;
        JTextField mediaNameField;
        private final JScrollPane jScrollPane = new JScrollPane();

        mainView(String title, int x, int y) {
            setTitle(title);
            setSize(x, y);
            button = new Button("Confirm");
            button.setPreferredSize(new Dimension(400, 100));
            pathField = new JTextField("Path Field");
            pathField.setPreferredSize(new Dimension(400, 100));
            mediaNameField = new JTextField("Media Name");
            mediaNameField.setPreferredSize(new Dimension(400, 100));
            setLayout(new FlowLayout(FlowLayout.CENTER));
            add(pathField);
            add(mediaNameField);
            add(button);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        String getFilePath() {
            return filePath;
        }

        String getMediaName() {
            return mediaName;
        }
    }
}