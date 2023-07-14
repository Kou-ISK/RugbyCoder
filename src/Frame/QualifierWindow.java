package Frame;

import DataObject.DataObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

class QualifierWindow extends JFrame {
    private final DataObject dto;
    private ArrayList<String> qualifiers = new ArrayList<>(Arrays.asList("Won", "Stolen", "Success", "Miss", "Again"));

    QualifierWindow(DataObject dto) {
        setTitle("Qualifier");
        this.dto = dto;
        setSize(350, 100);
        setLocation(900, 100);
        setLayout(new GridLayout(1, 5));
        // qualifiersをもとにボタンを生成
        qualifiers.forEach(q -> add(new QualifierButton(q, dto)));
        toFront();
    }
}
