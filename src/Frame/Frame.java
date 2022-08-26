package Frame;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;


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

//        コードウィンドウ
        JFrame codeWindow = new JFrame("Code Window");
        codeWindow.setLayout(new FlowLayout());
        codeWindow.setTitle("Code Window");
        codeWindow.setVisible(true);
        codeWindow.setSize(500,600);
        codeWindow.setLocation(750,0);
        JButton startButton = new JButton("Start");
        JButton endButton = new JButton("End");
        JButton tackleButton = new JButton("Tackle");
        JButton scrumButton = new JButton("Scrum");
        JButton lineOutButton = new JButton("Line Out");
        startButton.setSize(400,100);
        endButton.setSize(400,100);
        tackleButton.setSize(400,200);
        scrumButton.setSize(400,200);
        lineOutButton.setSize(400,200);

        LocalTime startTime;
        LocalTime endTime;

        tackleButton.addActionListener(e -> {
            System.out.println("tackle");
//            System.out.println(startTime);
//            System.out.println(endTime);
        });

        scrumButton.addActionListener(e -> System.out.println("scrum"));

        Container cwContainer = codeWindow.getContentPane();
        cwContainer.add(startButton);
        cwContainer.add(endButton);
        cwContainer.add(tackleButton);
        cwContainer.add(scrumButton);
        cwContainer.add(lineOutButton);

        cwContainer.setVisible(true);

    }
}
