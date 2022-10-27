package Frame;

import DataObject.teamDatas;
import Logic.Logic;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

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
            File rugbyCoderPkg = new File(directoryPath + mediaName);
            // 新規ディレクトリを作成
            String jsonPath = directoryPath + mediaName + ".json";
            if (!rugbyCoderPkg.exists()) {
                rugbyCoderPkg.mkdir();
                System.out.println(directoryPath + mediaName);
            } else {
                File json = new File(jsonPath);
                if (json.exists()) {
                    // TODO jsonを読み込む。チーム名を代入し、makeFramesを実行
                    Gson gson = new Gson();
                    // JSONから配列への変換
                    String[] teams = gson.fromJson(String.valueOf(json), String[].class);
                }

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
                // jsonファイル生成
                String jsonPath = directoryPath + mediaName + "/" + mediaName + ".json";
                try (PrintWriter out = new PrintWriter(new FileWriter(jsonPath))) {
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(td);
                    out.write(jsonString);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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