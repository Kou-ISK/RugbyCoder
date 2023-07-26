package Frame;

import DataObject.ButtonState;
import DataObject.TimelineDataObject;

import javax.swing.*;

class MyButton extends JButton {
    private ButtonState buttonState;
    private TimelineDataObject dto;

    MyButton(String title, int x, int y) {
        setText(title);
        setSize(x, y);
        buttonState = ButtonState.PUSHED;
        setFocusable(false);
    }

    ButtonState getButtonState() {
        return this.buttonState;
    }

    void setButtonState(ButtonState buttonState) {
        this.buttonState = buttonState;
    }

    TimelineDataObject getDto() {
        return dto;
    }

    void setDto(TimelineDataObject dto) {
        this.dto = dto;
    }
}
