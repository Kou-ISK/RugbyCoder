package Logic;

import DataObject.DataObject;
import javafx.util.Duration;

import java.io.*;

public class Logic {
    private String mediaName;
    private String path = "/Users/isakakou/Desktop/";
//    CSV形式で書き出し
    public void csvWriter(DataObject dataObject) {
        String timeCode = dataObject.getTimeCode();
        String action = dataObject.getActionName();
        String qualifier = dataObject.getActionQualifier();
FileWriter fw;
        try {
            File file = new File(path+mediaName + ".csv");
            // 書き込み権限の確認
            if (!file.canWrite()) {
                // 書き込み可能に変更
                file.setWritable(true);
            }

            fw = new FileWriter(file,true);
            // PrintWriterクラスのオブジェクトを生成
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print(timeCode);
            pw.print(",");
            pw.print(action);
            pw.println();
            pw.flush();
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setName(String mediaName){
        this.mediaName = mediaName;
        FileWriter fw;
        // 出力ファイルの作成
        try {
            fw = new FileWriter(path+mediaName + ".csv", true);
            // PrintWriterクラスのオブジェクトを生成
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("Timestamp");
            pw.print(",");
            pw.print("Action Name");
            pw.println(",");
            pw.close();
            System.out.println("setname");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getTimeStamp(Duration duration){
        int rawTime = (int) duration.toSeconds();
        int second = rawTime % 60;
        int minute = ((rawTime % 3600)/60);
        int hour = rawTime/3600;
        String time = String.format("%02d:%02d:%02d",
                hour, minute, second);
        return time;
    }
}
