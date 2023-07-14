package Frame;

import DataObject.TeamData;
import Logic.Logic;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static String directoryPath;
    private static String path;
    private static String mediaName;
    private static Logic logic = new Logic();

    public static void main(String[] args) throws Exception {
//        TODO パッケージをクリックで実行できるようにしたい
        mainView mv = new mainView("Open File", 350, 300);
        mv.setVisible(true);
        mv.getNameButton.addActionListener(e -> {
            path = mv.pathField.getText();
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
            // 新規パッケージ生成する場合
            System.out.println(file.getParentFile().getName());
            String jsonPath = directoryPath + mediaName + ".json";
            System.out.println(mediaName);
            if (!file.getParentFile().getName().equals(mediaName)) {
                rugbyCoderPkg.mkdir();
                try {
                    Files.move(Path.of(file.getPath()), Path.of(directoryPath + mediaName + "/" + mediaName + ".mp4"));
                    path = directoryPath + mediaName + "/" + mediaName + ".mp4";
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                path = directoryPath + "/" + mediaName + ".mp4";
                File jsonFile = new File(jsonPath);
                if (jsonFile.exists()) {
                    // JSONファイルからの読み込み
                    try (JsonReader reader =
                                 new JsonReader(new BufferedReader(new FileReader(jsonPath)))) {
                        // JSONからUserオブジェクトへの変換
                        Gson gson = new Gson();
                        TeamData td = gson.fromJson(reader, TeamData.class);
                        MakeFrames mf = new MakeFrames();
                        mf.makeFrames(directoryPath, path, mediaName, td);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    mv.setVisible(false);
                }
            }
            mv.mediaNameField.setText(mediaName);
        });
        mv.button.addActionListener(e -> {
            path = mv.pathField.getText();
            mediaName = mv.mediaNameField.getText();
            String Ateam = mv.AteamField.getText();
            String Bteam = mv.BteamField.getText();
            TeamData td = new TeamData(Ateam, Bteam);
            logic.setFilePath(mv.pathField.getText());
            logic.setMediaName(directoryPath, mv.mediaNameField.getText());
            MakeFrames mf = new MakeFrames();
            try {
                // jsonファイル生成
                String jsonPath = directoryPath + mediaName + "/" + mediaName + ".json";
                path = directoryPath + mediaName + "/" + mediaName + ".mp4";
                directoryPath = directoryPath + mediaName + "/";
                try (JsonWriter writer =
                             new JsonWriter(new BufferedWriter(new FileWriter(jsonPath)))) {
                    Gson gson = new Gson();
                    gson.toJson(td, TeamData.class, writer);
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

        private final JScrollPane jScrollPane = new JScrollPane();
        private String filePath;
        private String mediaName;
        private Button button;
        private Button getNameButton;
        private JTextField pathField;
        private JTextField mediaNameField;
        private JTextField AteamField;
        private JTextField BteamField;

        mainView(String title, int x, int y) {
            setTitle(title);
            setSize(x, y);
            setLocation(600, 200);
            button = new Button("Confirm");
            button.setBackground(Color.darkGray);
            getNameButton = new Button("Get Media Name");
            getNameButton.setPreferredSize(new Dimension(300, 50));
            getNameButton.setBackground(Color.lightGray);
            button.setPreferredSize(new Dimension(300, 50));
            pathField = new JTextField("Path Field");
            pathField.setPreferredSize(new Dimension(300, 30));
            pathField.setLocation(0, 30);
            mediaNameField = new JTextField("Media Name");
            mediaNameField.setPreferredSize(new Dimension(300, 30));
            AteamField = new JTextField("A TEAM");
            AteamField.setPreferredSize(new Dimension(100, 30));
            BteamField = new JTextField("B TEAM");
            BteamField.setPreferredSize(new Dimension(100, 30));
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