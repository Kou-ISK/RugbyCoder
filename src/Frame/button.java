package Frame;

import DataObject.DataObject;

import javax.swing.*;

class button extends JButton {
    private DataObject dto;
    private int buttonState;
//    0:not pushed, 1:pushed, 2:not yet


    button(String title, int x, int y) {
        setText(title);
        setSize(x, y);
        buttonState = 2;
        setFocusable(false);
    }

    int getButtonState() {
        return this.buttonState;
    }

    void setButtonState(int buttonState) {
        this.buttonState = buttonState;
    }

    void setDto(DataObject dto) {
        this.dto = dto;
    }

    DataObject getDto() {
        return dto;
    }
}
