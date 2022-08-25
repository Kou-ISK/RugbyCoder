package Frame;

import javax.swing.JFrame;


public class Frame{
    public static void main(String[] args) {
//        ビデオ再生用ウィンドウ
        JFrame videoFrame = new JFrame("Rugby Coder");
        videoFrame.setTitle("Rugby Coder");
        videoFrame.setVisible(true);
        videoFrame.setSize(700,450);

//        タイムライン用ウィンドウ
        JFrame tlFrame = new JFrame("Timeline");
        tlFrame.setTitle("TimeLine");
        tlFrame.setVisible(true);
        tlFrame.setSize(1400, 400);
        tlFrame.setLocation(0,490);
    }
}
