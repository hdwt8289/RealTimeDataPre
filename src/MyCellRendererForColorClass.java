import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-12-26
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class MyCellRendererForColorClass  extends JLabel implements TableCellRenderer {
    private static final long serialVersionUID = 1L;

    //定义构造器
    public MyCellRendererForColorClass() {
        //设置标签为不透明状态
        this.setOpaque(true);
        //设置标签的文本对齐方式为居中
        this.setHorizontalAlignment(JLabel.CENTER);
    }

    //实现获取呈现控件的getTableCellRendererComponent方法
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //获取要呈现的颜色
        Color c = (Color) value;
        //根据参数value设置背景色
        this.setBackground(c);
        //设置前景色为背景色反色
        // this.setForeground(new Color(255-c.getRed() ,255-c.getGreen(),255-c.getBlue()));
        //设置标签中显示RGB的值
        this.setText("" + c.getRGB());
        //将自己返回
        return this;
    }
}
