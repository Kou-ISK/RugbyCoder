package Logic;

import DataObject.TimelineDataObject;
import Frame.Window;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static Frame.Main.directoryPath;

public class Logic {
    private String mediaName;
    private String filePath;

    //    CSV形式で書き出し
    public void csvWriter(String directoryPath, TimelineDataObject timelineDataObject) {
        String startTimeCode = timelineDataObject.getStartTimeCode();
        String endTimeCode = timelineDataObject.getEndTimeCode();
        String action = timelineDataObject.getActionName();
        String qualifier = timelineDataObject.getActionQualifier();
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
            if (qualifier == null || qualifier.isBlank()) {
                pw.print("");
            } else {
                pw.print(qualifier);
            }
            pw.println();
            pw.flush();
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMediaName() {
        return mediaName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    // mp4parser参考: https://dev.classmethod.jp/articles/mp4parser/
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
            File exportFolder = new File(directoryPath + "export");
            if (!exportFolder.exists()) exportFolder.mkdir();
            String outputFilePath = directoryPath + "export/" + actionName + "_" + startTime.replace(":", "-") + "_output.mp4";
            FileOutputStream fos = new FileOutputStream(outputFilePath);
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
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date parsed = sdf.parse(time);
            milli = Long.valueOf(parsed.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return milli;
    }

    // TODO 分析用メソッドを追加
    public void analyze(ArrayList<TimelineDataObject> dto) {
        var data = dto.stream().map(TimelineDataObject::getActionName).distinct();
        Map<String, Map> analysisDatas = new HashMap<>();
        data.forEach(it -> {
            Map analysisData = new HashMap<>();
            List<TimelineDataObject> dataOfIt = dto.stream().filter(dataObject -> dataObject.getActionName().equals(it)).toList();
            analysisData.put("Count", dataOfIt.size());
            analysisData.put("Total Second", getTotalMillis(dataOfIt) / 1000);
            int averageSecond = (int) ((getTotalMillis(dataOfIt) / 1000) / dataOfIt.size());
            analysisData.put("Average Second", averageSecond);
            analysisData.put("Qualifiers", countQualifiers(dataOfIt));
            analysisDatas.put(it, analysisData);
        });
        String[] header = {"Instance Name", "Count", "Total Second", "Average Second"};
        showAnalysisWindow(header, analysisDatas);
    }

    private Long getTotalMillis(List<TimelineDataObject> dto) {
        return dto.stream().mapToLong(dataObject ->
                parseToMilli(dataObject.getEndTimeCode()) - parseToMilli(dataObject.getStartTimeCode())
        ).sum();
    }

    private HashMap countQualifiers(List<TimelineDataObject> dto) {
        HashMap<String, Integer> qualifierCount = new HashMap<>();
        dto.stream().map(data -> data.getActionQualifier()).distinct().forEach(it -> {
            qualifierCount.put(it, (int) dto.stream().map(dataObject -> dataObject.getActionQualifier()).filter(s -> s.equals(it)).count());
        });
        return qualifierCount;
    }

    private Long getTotalMillisByQualifier(List<TimelineDataObject> dto, String qualifierName) {
        return dto.stream().filter(s -> s.getActionQualifier().equals(qualifierName)).mapToLong(dataObject -> parseToMilli(dataObject.getEndTimeCode()) - parseToMilli(dataObject.getStartTimeCode())).sum();
    }

    private void showAnalysisWindow(String[] header, Map<String, Map> analysisDatas) {
        Window analysisWindow = new Window("Analysis", 1400, 400);
        DefaultTableModel tableModel = new DefaultTableModel(header, 0);
        JTable table = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(table);
        analysisDatas.entrySet().forEach(it -> {
            var data = it.getValue();
            tableModel.addRow(new String[]{it.getKey(), data.get("Count").toString(), data.get("Total Second").toString(), data.get("Average Second").toString()});
        });
        analysisWindow.add(jScrollPane);
        analysisWindow.show();
    }
}
