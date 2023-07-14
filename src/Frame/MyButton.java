package Frame;

import DataObject.DataObject;

import javax.swing.*;

class MyButton extends JButton {
    private DataObject dto;
    private int buttonState;
//    0:not pushed, 1:pushed, 2:not yet


    MyButton(String title, int x, int y) {
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

    DataObject getDto() {
        return dto;
    }

    void setDto(DataObject dto) {
        this.dto = dto;
    }
}
