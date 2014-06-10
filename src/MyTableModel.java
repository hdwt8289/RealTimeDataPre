import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-12-26
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
public class MyTableModel extends AbstractTableModel {
    Object[][] data;
    private static final long serialVersionUID = 1L;

    public MyTableModel(Object[][] d){
        data=d;
    }
    //创建表示各个列类型的类型数组
    Class[] typeArray =
            {Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Color.class};
    //创建列标题字符串数组
    String[] head = {"选择", "名称", "块号", "最大值", "最小值", "单位", "颜色"};

    //重写getColumnCount方法
    public int getColumnCount() {
        return head.length;
    }

    //重写getRowCount方法
    public int getRowCount() {
        return data.length;
    }

    //重写getColumnName方法
    public String getColumnName(int col) {
        return head[col];
    }

    //重写getValueAt方法
    public Object getValueAt(int r, int c) {
        return data[r][c];
    }

    //重写getColumnClass方法
    public Class getColumnClass(int c) {
        return typeArray[c];
    }

    //重写isCellEditable方法
    public boolean isCellEditable(int r, int c) {
        if(c==2)
            return false;
        else
            return true;

    }

    //重写setValueAt方法
    public void setValueAt(Object value, int r, int c) {
        data[r][c] = value;
        //
        this.fireTableCellUpdated(r, c);
    }
}
