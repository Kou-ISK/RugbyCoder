package Frame;


import DataObject.TimelineDataObject;
import Logic.Logic;

import javax.swing.*;
import java.util.ArrayList;

import static Frame.MakeFrames.filePath;

class MyMenuBar {
    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem export = new JMenuItem("Export");
    private final JMenu optionMenu = new JMenu("Options");
    private final JMenuItem analyze = new JMenuItem("Analyze");
    private JMenuBar menuBar;
    private Logic logic;

    MyMenuBar() {
        this.logic = new Logic();
        this.menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(optionMenu);
        export.addActionListener(l -> {
            exportAction();
        });
        analyze.addActionListener(l -> {
            analyze();
        });
        fileMenu.add(export);
        fileMenu.add(analyze);
    }

    JMenuBar getMenuBar() {
        return menuBar;
    }

    private void exportAction() {
        int rowNumber = CsvViewer.getTable().getSelectedRow();
        String[] rowData = CsvViewer.getRow(rowNumber);
        boolean exportSuccess = logic.ExportVideo(filePath, rowData[0], rowData[1], rowData[2]);
        showPopupOnExportAction(exportSuccess);
    }

    private void showPopupOnExportAction(boolean result) {
        JPopupMenu popupMenu = new JPopupMenu();
        if (result) {
            popupMenu.add("映像ファイル出力に成功しました");
        } else {
            popupMenu.add("映像ファイル出力に失敗しました");
        }
        popupMenu.show(this.menuBar.getComponent(), this.menuBar.getX(), this.menuBar.getY());
    }

    private void analyze() {
        int rowCount = CsvViewer.getTable().getRowCount();
        ArrayList<TimelineDataObject> dto = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            String[] rowData = CsvViewer.getRow(i);
            TimelineDataObject data = new TimelineDataObject(rowData[0], rowData[1], rowData[2], rowData[3]);
            dto.add(data);
        }
        logic.analyze(dto);
    }
}
