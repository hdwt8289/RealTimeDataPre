
import com.csvreader.CsvWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Vector;
import java.util.Iterator;

public class DataExp {
    /*导出为csv数据*/
    public void DataExportCsv(String fileName, JTable jTable) {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        try {
            CsvWriter writer = new CsvWriter(fileName, ',', Charset.forName("UTF-8"));
            int cols = model.getColumnCount();
            for (int i = 0; i < cols; i++) {
                writer.write(model.getColumnName(i));
            }
            writer.endRecord();
            Vector rows = model.getDataVector();
            for (Iterator it = rows.iterator(); it.hasNext(); ) {
                Vector v = (Vector) it.next();
                for (int j = 0; j < cols; j++) {
                    writer.write(v.get(j).toString());
                }
                writer.endRecord();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
