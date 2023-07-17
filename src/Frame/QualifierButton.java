package Frame;

import DataObject.TimelineDataObject;

import javax.swing.*;
import java.awt.Window;
import java.awt.*;

class QualifierButton extends JButton {
    private final Container parent;

    QualifierButton(String title, TimelineDataObject dto) {
        setText(title);
        this.parent = getParent();
        addActionListener(a -> {
            dto.setActionQualifier(title);
            Component c = (Component) a.getSource();
            Window w = SwingUtilities.getWindowAncestor(c);
            w.dispose();
        });
    }
}
