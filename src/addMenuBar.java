import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class addMenuBar {
    public void setV_colName(Vector v_colName) {
        V_colName = v_colName;
    }

    public Vector V_colName;

    public void setV_color(Vector v_color) {
        V_color = v_color;
    }

    private Vector V_color;

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    private int timeLength;


    public void setpChart(JPanel pChart) {
        this.pChart = pChart;
    }

    private JPanel pChart;

    public void setpChk(JPanel pChk) {
        this.pChk = pChk;
    }

    private JPanel pChk;

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    private JPanel p0;

    public void setPanelPop(JPanel panelPop) {
        this.panelPop = panelPop;
    }

    private JPanel panelPop;
    public static JTable jtable;

    ///数据库
    public static int iSetting = 0;
    public static int iPopFrame = 0;
    public static int iOpenfile = 0;
    private static Vector V_DbParam = new Vector();
    public static JMenuItem item0;

    public JMenuBar addMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("趋势定义");
        menu.setMnemonic('F');
        menubar.add(menu);

        item0 = new JMenuItem("连接");
        menu.add(item0);
        menu.addSeparator();
        JMenuItem item1 = new JMenuItem("新建");
        menu.add(item1);
        menu.addSeparator();
        JMenuItem item2 = new JMenuItem("打开");
        menu.add(item2);

        //配置数据源
        item0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iSetting++;
                if (iSetting == 1) {
                    Setting set = new Setting();
                    set.setV_DbParam(V_DbParam);
                    set.setFrame();
                }

            }
        });
        //新建趋势
        item1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iPopFrame++;
                if (!V_DbParam.isEmpty()) {
                    if (iPopFrame == 1) {
                        if (Plot.dg != null)
                            Plot.dg.stop();
                        PopFrame popFrame = new PopFrame();
                        popFrame.setP0(p0);
                        popFrame.setpChk(pChk);
                        popFrame.setpChart(pChart);
                        popFrame.setTimeLength(timeLength);
                        popFrame.setV_DbParam(V_DbParam);
                        popFrame.PopFrame();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请配置数据源", "提示", JOptionPane.ERROR_MESSAGE);
                    iPopFrame = 0;
                }
            }
        });
        //打开趋势
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iOpenfile++;
                if (!V_DbParam.isEmpty()) {
                    if (iOpenfile == 1 && iSetting == 0) {
                        if (Plot.dg != null)
                            Plot.dg.stop();
                        OpenFile openfile = new OpenFile();
                        openfile.setP0(p0);
                        openfile.setpChk(pChk);
                        openfile.setpChart(pChart);
                        openfile.setTimeLength(timeLength);
                        openfile.setV_DbParam(V_DbParam);
                        openfile.openFile();
                        jtable = openfile.getJtable();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请配置数据源", "提示", JOptionPane.ERROR_MESSAGE);
                    iOpenfile = 0;
                }
            }
        });

        return menubar;
    }
}
