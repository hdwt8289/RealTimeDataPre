/*import jxl.*;
import jxl.biff.EmptyCell;
import jxl.read.biff.BiffException;*/


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 12-9-5
 * Time: 上午9:12
 * To change this template use File | Settings | File Templates.
 */
public class DataImport {
    /*读取csv文件*/
    public JTable ReadCSV(File file) {
        TableModel dataInfo = getFileStats(file);
        JTable jt = new JTable(dataInfo);
        jt.setPreferredScrollableViewportSize(new Dimension(400, 80));
        return jt;
    }

    private TableModel getFileStats(File a) {
        String data;
        Object[] object = null;
        int columnCount = 0;//文件中最大行的列数
        DefaultTableModel dt = new DefaultTableModel();
        try {
            BufferedReader br = new BufferedReader(new FileReader(a));
            //不是文件尾一直读
            while ((data = br.readLine()) != null) {
                object = data.split(",");
                //如果这行的列数大于最大的，那么再增加一列
                if (object.length > columnCount) {
                    for (int i = 0; i < object.length - columnCount; i++) {
                        dt.addColumn("column".concat(String.valueOf(i)));
                    }
                    columnCount = object.length;
                }
                //添加一行
                dt.addRow(object);
            }
            ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dt;
    }

}
