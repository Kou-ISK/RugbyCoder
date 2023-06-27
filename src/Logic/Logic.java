package Logic;

import DataObject.DataObject;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import javafx.scene.control.Slider;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static Frame.Main.directoryPath;

public class Logic {
    private String mediaName;
    private String filePath;
    private Slider timeSlider;
    private Label timeLabel;

    //    CSV形式で書き出し
    public void csvWriter(String directoryPath, DataObject dataObject) {
        String startTimeCode = dataObject.getStartTimeCode();
        String endTimeCode = dataObject.getEndTimeCode();
        String action = dataObject.getActionName();
        String qualifier = dataObject.getActionQualifier();
        FileWriter fw;
        try {
            File file = new File(directoryPath + mediaName + ".csv");
            // 書き込み権限の確認
            if (!file.canWrite()) {
                // 書き込み可能に変更
                file.setWritable(true);
            }

            fw = new FileWriter(file, true);
            // PrintWriterクラスのオブジェクトを生成
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print(startTimeCode);
            pw.print(",");
            pw.print(endTimeCode);
            pw.print(",");
            pw.print(action);
            pw.print(",");
            pw.print("");
            pw.println();
            pw.flush();
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMediaName() {
        return mediaName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setMediaName(String directoryPath, String mediaName) {
        this.mediaName = mediaName;
        FileWriter fw;
        File file = new File(directoryPath + mediaName + ".csv");
        if (!file.exists()) {
            // 出力ファイルの作成
            try {
                fw = new FileWriter(directoryPath + mediaName + ".csv", true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getTimeStamp(Duration duration) {
        int rawTime = (int) duration.toSeconds();
        int second = rawTime % 60;
        int minute = ((rawTime % 3600) / 60);
        int hour = rawTime / 3600;
        String time = String.format("%02d:%02d:%02d",
                hour, minute, second);
        return time;
    }

    //    csvを読み込んで映像とリンク
//    branch2 first commit
    public Duration getDataFromCsv(String time) {
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
    private String toStringMinSec(Duration dur) {
        int min = (int) Math.floor(dur.toMinutes());
        int sec = (int) (Math.ceil(dur.toSeconds() % 60));
        return String.format("%1$d:%2$02d", min, sec);
    }

    public void checkFileData(String p) {
        Path path = Paths.get(p);
        if (Files.exists(path)) {
            System.out.println("ファイルまたはディレクトリは存在します");
        } else {

        }
    }

    public boolean ExportVideo(String filePath, String startTime, String endTime, String actionName) {
        try {
            // 動画を読み込み
            Movie originalMovie = MovieCreator.build(filePath);

            // 分割
            Track track = originalMovie.getTracks().get(0);
            Movie movie = new Movie();

            Long parsedStartTime = parseToMilli(startTime);
            Long parsedEndTime = parseToMilli(endTime);
            movie.addTrack(new AppendTrack(new CroppedTrack(track, parsedStartTime, parsedEndTime)));

            // 出力
            Container out = new DefaultMp4Builder().build(movie);
            String outputFilePath = directoryPath + actionName + "_" + startTime.replace(":", "-") + "_output.mp4";
            FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Long parseToMilli(String time) {
        Long milli;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            Date parsed = sdf.parse(time);
            milli = Long.valueOf(parsed.getSeconds() * 1000);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return milli;
    }
}
