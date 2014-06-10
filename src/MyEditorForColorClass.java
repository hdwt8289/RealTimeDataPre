import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-12-26
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class MyEditorForColorClass extends AbstractCellEditor
        implements TableCellEditor, ActionListener {
    private static final long serialVersionUID = 1L;
    //定义Color变量
    Color c;
    //定义对话框变量
    JDialog jd;
    //创建一个按扭
    JButton jb = new JButton();
    //创建JColorChooser对象
    JColorChooser jcc = new JColorChooser();
    //声明一个常量
    public static final String EDIT = "edit";

    //定义构造器
    public MyEditorForColorClass() {
        //为按扭注册监听器
        jb.addActionListener(this);
        //设置此按扭的动作命令
        jb.setActionCommand(EDIT);
        //获取颜色选择器
        jd = JColorChooser.createDialog(jb, "选择颜色", true, jcc, this, null);
    }

    //实现actionPerformed方法
    public void actionPerformed(ActionEvent e) {
        //测试获得的动作命令是否等于EDIT常量
        if (e.getActionCommand().equals(EDIT)) {
            //设置按扭的背景颜色
            jb.setBackground(c);
            //设置前景色为背景色反色
            jb.setForeground(new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()));
            //设置按钮中显示RGB的值
            jb.setText("" + c.getRGB());

            //设置颜色选择器的颜色
            jcc.setColor(c);
            //设置颜色选择器可见
            jd.setVisible(true);
            //通知所有监听器，以延迟方式创建事件对象
            this.fireEditingStopped();
        } else {
            //获取颜色
            c = jcc.getColor();
//            if (V_color.size() > 0)
//                V_color.set(j10.getSelectedRow(), c);
        }
    }

    //定义getCellEditorValue方法返回颜色值
    public Object getCellEditorValue() {
        return c;
    }

    //重写getTableCellEditorComponent方法
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value, boolean isSelected, int row, int column) {
        c = (Color) value;
        return jb;
    }
}
