package Frame;

import DataObject.DataObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.regex.Pattern;

class csvViewer extends JFrame {
    private final String fileName;
    private final String[] header = {"startTime", "endTime", "Action", "Qualifier"};
    private final DefaultTableModel tableModel = new DefaultTableModel(null, header);
    private final JTable table = new JTable(tableModel);
    private final JScrollPane jScrollPane = new JScrollPane(table);

    csvViewer(String directoryPath, String filename) throws IOException {
        super(filename + " - TimeLine");
        fileName = directoryPath + "/" + filename + ".csv";

        readIn();
        getContentPane().add(jScrollPane);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        pack();
        show();
    }

    String getFileName() {
        return fileName;
    }

    void addRow(DataObject dto) {
        Object[] dataList = {dto.getStartTimeCode(), dto.getEndTimeCode(), dto.getActionName(), dto.getActionQualifier()};
        tableModel.addRow(dataList);
    }

    void saveCsvFile() {
        File file = new File(fileName);
        if (!file.canWrite()) {
            // 書き込み可能に変更
            file.setWritable(true);
        }

        try (FileWriter fw = new FileWriter(file, false)) {
            // PrintWriterクラスのオブジェクトを生成
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < table.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < table.getColumnCount(); j++) {
                    sb.append(table.getValueAt(i, j)).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                bw.write(sb.toString());
                bw.newLine();
                bw.flush();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("あかんわ");
            throw new RuntimeException(e);
        }
        System.out.println("OK");
    }


    JTable getTable() {
        return table;
    }


    private void readIn() {
        Pattern pattern = Pattern.compile(",");
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            for (int row = 0; (line = reader.readLine()) != null; row++) {
                String[] items = pattern.split(line);
                model.setRowCount(row + 1);
                for (int column = 0; column < items.length; column++) {
                    if (model.getColumnCount() <= column) {
                        model.setColumnCount(column + 1);
                    }
                    table.setValueAt(items[column], row, column);
                }
            }
            table.setAutoCreateRowSorter(true);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
