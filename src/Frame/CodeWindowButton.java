package Frame;

import DataObject.TimelineDataObject;
import Logic.Logic;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;

import static Frame.MakeFrames.directoryPath;
import static Frame.MakeFrames.kl;

class CodeWindowButton extends JButton {
    private TimelineDataObject dto;
    private int buttonState;
//    0:not pushed, 1:pushed, 2:not yet

    CodeWindowButton(String title, Logic logic, MediaPlayer player, CsvViewer csvViewer, int x, int y, boolean needQualifier) {
        setText(title);
        setSize(x, y);
        setFocusable(false);
        setOpaque(true);
        setRequestFocusEnabled(false);
        addKeyListener(kl);
        buttonState = 2;
        addActionListener(a -> {
            int state = this.getButtonState();
            if (state == 0 || state == 2) {
                TimelineDataObject dto = new TimelineDataObject(logic.getTimeStamp(player.getCurrentTime()), title);
                this.setBorderPainted(false);
                this.setDto(dto);
                this.setForeground(Color.red);
                this.setButtonState(1);
                if (needQualifier == true) {
                    QualifierWindow qw = new QualifierWindow(dto);
                    qw.setVisible(true);
                }
            }
            if (state == 1) {
                this.setBorderPainted(true);
                TimelineDataObject dto = this.getDto();
                if (dto != null) {
                    dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                    logic.csvWriter(directoryPath, dto);
                    this.setForeground(Color.black);
                    csvViewer.addRow(dto);
                }
                this.setButtonState(0);
            }
        });
    }

    private int getButtonState() {
        return this.buttonState;
    }

    private void setButtonState(int buttonState) {
        this.buttonState = buttonState;
    }

    private TimelineDataObject getDto() {
        return dto;
    }

    private void setDto(TimelineDataObject dto) {
        this.dto = dto;
    }
}
