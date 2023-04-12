package Frame;

import DataObject.DataObject;

import javax.swing.*;
import java.awt.*;

class qualifierWindow extends JFrame {
    private final DataObject dto;
    private boolean close;

    qualifierWindow(DataObject dto) {
        setTitle("Qualifier");
        this.dto = dto;
        setSize(350, 100);
        setLocation(900, 100);
        setLayout(new GridLayout(1, 5));
        add(new QButton("Won", dto));
        add(new QButton("Stolen", dto));
        add(new QButton("Success", dto));
        add(new QButton("Miss", dto));
        add(new QButton("Again", dto));
        toFront();
    }
}
