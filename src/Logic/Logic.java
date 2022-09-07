package Logic;

import DataObject.DataObject;
import javafx.scene.control.Slider;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Logic {
    private String mediaName;
    private String path = "/Users/isakakou/Desktop/";
    private Slider timeSlider;
    private Label timeLabel;

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

//    csvを読み込んで映像とリンク
//    branch2 first commit
    public Duration getDataFromCsv(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = sdf.parse("1970-01-01 " + time);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Duration.millis(date.getTime());
    }


    //Durationのフォーマッター。"MM:ss"形式
    private String toStringMinSec (Duration dur){
        int min = (int) Math.floor(dur.toMinutes());
        int sec = (int) (Math.ceil(dur.toSeconds() % 60));
        return String.format("%1$d:%2$02d", min, sec);
    }
}
