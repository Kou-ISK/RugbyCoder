package Frame;

import DataObject.teamDatas;
import Logic.Logic;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {
    private static String path;
    private static String mediaName;
    private static String directoryPath;

    public static void main(String[] args) throws Exception {
//        TODO パッケージにまとめる
        Logic logic = new Logic();
        mainView mv = new mainView("Main", 500, 500);
        mv.setVisible(true);
        mv.getNameButton.addActionListener(e -> {
            String path = mv.pathField.getText();
            File file = new File(path);
            directoryPath = file.getParentFile() + "/";
            System.out.println(file.getName().split("."));
            int point = file.getName().lastIndexOf(".");
            if (point != -1) {
                mediaName = file.getName().substring(0, point);
            } else {
                mediaName = file.getName();
            }
            mv.mediaNameField.setText(mediaName);
        });
        mv.button.addActionListener(e -> {
            System.out.println(mv.pathField.getText());
            path = mv.pathField.getText();
            mediaName = mv.mediaNameField.getText();
            String Ateam = mv.AteamField.getText();
            String Bteam = mv.BteamField.getText();
            teamDatas td = new teamDatas(Ateam, Bteam);
            logic.setFilePath(mv.pathField.getText());
            logic.setMediaName(directoryPath, mv.mediaNameField.getText());
            MakeFrames mf = new MakeFrames();
            try {
                mf.makeFrames(directoryPath, path, mediaName, td);
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
        Button getNameButton;
        JTextField pathField;
        JTextField mediaNameField;
        JTextField AteamField;
        JTextField BteamField;
        private final JScrollPane jScrollPane = new JScrollPane();

        mainView(String title, int x, int y) {
            setTitle(title);
            setSize(x, y);
            button = new Button("Confirm");
            button.setBackground(Color.blue);
            getNameButton = new Button("Get Media Name");
            getNameButton.setPreferredSize(new Dimension(400, 100));
            getNameButton.setBackground(Color.lightGray);
            button.setPreferredSize(new Dimension(400, 100));
            pathField = new JTextField("Path Field");
            pathField.setPreferredSize(new Dimension(400, 50));
            mediaNameField = new JTextField("Media Name");
            mediaNameField.setPreferredSize(new Dimension(400, 50));
            AteamField = new JTextField("A TEAM");
            AteamField.setPreferredSize(new Dimension(200, 30));
            BteamField = new JTextField("B TEAM");
            BteamField.setPreferredSize(new Dimension(200, 30));
            setLayout(new FlowLayout(FlowLayout.CENTER));
            add(pathField);
            add(getNameButton);
            add(mediaNameField);
            add(AteamField);
            add(BteamField);
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