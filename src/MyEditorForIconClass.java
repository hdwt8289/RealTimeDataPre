import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-12-26
 * Time: 下午5:03
 * To change this template use File | Settings | File Templates.
 */
public class MyEditorForIconClass extends AbstractCellEditor
        implements TableCellEditor, ActionListener {
    private static final long serialVersionUID = 1L;
    //定义Icon变量
    Icon icon;
    //创建一个按扭
    JButton jb = new JButton();
    //创建JColorChooser对象
    JFileChooser jfc = new JFileChooser();
    //声明一个常量
    public static final String EDIT = "edit";

    //定义构造器
    public MyEditorForIconClass() {
        //为按扭注册监听器
        jb.addActionListener(this);
        //设置此按扭的动作命令
        jb.setActionCommand(EDIT);
    }

    //实现actionPerformed方法
    public void actionPerformed(ActionEvent e) {
        //测试获得的动作命令是否等于EDIT常量
        if (e.getActionCommand().equals(EDIT)) {
            //设置按扭的图标
            jb.setIcon(icon);
            //显示文件选择器对话框
            jfc.showOpenDialog(jb);
            //获取新图片
            if (jfc.getSelectedFile() != null) {
                icon = new ImageIcon(jfc.getSelectedFile().getAbsolutePath());
            }
            //通知所有监听器，以延迟方式创建事件对象
            this.fireEditingStopped();
        }
    }

    //定义getCellEditorValue方法返回图标
    public Object getCellEditorValue() {
        return icon;
    }

    //重写getTableCellEditorComponent方法
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value, boolean isSelected, int row, int column) {
        icon = (Icon) value;
        return jb;
    }
}
