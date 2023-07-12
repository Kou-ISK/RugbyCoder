package Frame;

import DataObject.DataObject;

import javax.swing.*;
import java.awt.*;
import java.awt.Window;

class QButton extends JButton {
    private final Container parent;

    QButton(String title, DataObject dto) {
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
