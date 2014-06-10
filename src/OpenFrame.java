import com.mongodb.*;
import sun.plugin.dom.DOMObject;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.UnknownHostException;
import java.util.*;


public class OpenFrame extends JPanel {
    public void setJtable(JTable jtable) {
        this.jtable = jtable;
    }

    private JTable jtable;
    public Vector V_col;

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    private int timeLength;

    public void setPanelPop(JPanel panelPop) {
        this.panelPop = panelPop;
    }

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    private JPanel p0;

    public void setpChart(JPanel pChart) {
        this.pChart = pChart;
    }

    public void setpChk(JPanel pChk) {
        this.pChk = pChk;
    }

    private JPanel pChk;

    private JPanel pChart;

    private JPanel panelPop;
    private Vector V_dataRow;
    private JTable j1;

    final JTextField txt = new JTextField(10);

    public void setV_DbParam(Vector v_DbParam) {
        V_DbParam = v_DbParam;
    }

    private Vector V_DbParam;

    private Vector V_color = new Vector();
    private Vector V_colName = new Vector();
    private static Vector V_Max = new Vector();
    private static Vector V_Min = new Vector();
    private static JudgeContains judge;
    public static JTable j10;

    private static JScrollPane jsp;
    Object[][] data;
    String maxoldValue, maxnewValue, minoldValue, minnewValue;
    String oldName, newName;


    public void popFrame() {
        final JFrame f = new JFrame("打开趋势");
        f.setSize(300, 400);
        f.setResizable(false);
        //获取屏幕分辨率的工具集
        Toolkit tool = Toolkit.getDefaultToolkit();
        //利用工具集获取屏幕的分辨率
        Dimension dim = tool.getScreenSize();
        //获取屏幕分辨率的高度
        int height = (int) dim.getHeight();
        //获取屏幕分辨率的宽度
        int width = (int) dim.getWidth();

        LimitedDocument ld = new LimitedDocument(8);//参数为能输入的最大长度
        ld.setAllowChar("0123456789.");//只能输入的字符
        txt.setDocument(ld);
        //设置位置
        f.setLocation((width - 300) / 2, (height - 400) / 2);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addMenuBar.iOpenfile = 0;
            }
        });

        DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
        int colNum = dtm.getColumnCount();
        int rowNum = dtm.getRowCount();
        V_col = new Vector();
        for (int i = 1; i < rowNum; i++) {
            V_dataRow = new Vector();
            V_dataRow.add("false");
            for (int j = 1; j < colNum; j++) {
                V_dataRow.add(dtm.getValueAt(i, j).toString());
            }
            V_col.add(V_dataRow);
        }

        final Vector v0 = new Vector();
        v0.add("选择");
        v0.add("名称");
        v0.add("块号");
        v0.add("最大值");
        v0.add("最小值");
        v0.add("单位");
        v0.add("颜色");

        j1 = new JTable(V_col, v0);
        //j1.setPreferredSize(new Dimension(100,500));
        TableColumn col = j1.getColumn("选择");
        JCheckBox chk = new JCheckBox();
        col.setCellEditor(new DefaultCellEditor(chk));
        col.setCellRenderer(new MyCheckBoxRenderer());

        panelPop.removeAll();
        panelPop.add(new JScrollPane(j1));
        panelPop.validate();
        JPanel panelPop2 = new JPanel();

        JButton btnOk = new JButton("确 定");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txt.getText().isEmpty()) {
                    judge = new JudgeContains(V_DbParam);
                    timeLength = Integer.parseInt(txt.getText().toString());
                    Vector vShow = new Vector();
                    Vector vShow0 = new Vector();
                    final Vector vtest = new Vector();
                    int rowCount = j1.getRowCount();
                    JTable js=j1;
                    for (int j0 = 0; j0 < rowCount; j0++) {
                        String ss = js.getModel().getValueAt(j0, 0).toString();
                        String paramName = js.getModel().getValueAt(j0, 1).toString();
                        String paramNo = js.getModel().getValueAt(j0, 2).toString();
                        String maxValue = js.getModel().getValueAt(j0, 3).toString();
                        String minValue = js.getModel().getValueAt(j0, 4).toString();
                        String paramUnit = js.getModel().getValueAt(j0, 5).toString();
                        String scolor=js.getModel().getValueAt(j0, 6).toString();
                        Color colcolor =new Color((int)Double.parseDouble(scolor));
                        int intColor=(int)Double.parseDouble(scolor);
                        if (ss.equals("true")) {
                            Vector v2 = new Vector();
                            int abc = j0 + 1;
                            v2.add(abc);
                            v2.add(paramName);
                            v2.add(paramNo);
                            v2.add(maxValue);
                            v2.add(minValue);
                            v2.add(paramUnit);
                            v2.add(colcolor);
                            vShow.add(v2);
                            vtest.add(colcolor);
                            /////传递导入的数据
                            Vector v3 = new Vector();
                            v3.add(abc);
                            v3.add(paramName);
                            v3.add(paramNo);
                            v3.add(maxValue);
                            v3.add(minValue);
                            v3.add(paramUnit);
                            v3.add(intColor);
                            vShow0.add(v3);
                        }
                    }
                    PopFrame.v0 = vShow0;
                    ///绘制实时曲线
                    //获取屏幕分辨率的工具集
                    Toolkit tool = Toolkit.getDefaultToolkit();
                    //利用工具集获取屏幕的分辨率
                    Dimension dim = tool.getScreenSize();
                    //获取屏幕分辨率的高度
                    int height = (int) dim.getHeight();
                    int width = (int) dim.getWidth();
                    ///实时绘制曲线窗体
                    p0 = new JPanel();
                    final Plot plot = new Plot(V_DbParam);

                    plot.setpChart(pChart);
                    plot.setP0(p0);
                    plot.setpChk(pChk);
                    plot.setHeight(height * 87 / 100);
                    plot.setWidth(width * 90 / 100);


                    pChk.removeAll();
                    pChk.validate();
                    pChk.setLayout(new GridLayout(1, 1));

                    int numColor = vShow.size();
                    String[] arrcorlor = new String[numColor];
                    for (int i = 0; i < numColor; i++) {
                        arrcorlor[i] = "" + i;
                    }

                    int a1 = vShow.size();
                    data = new Object[a1][7];
                    for (int ii = 0; ii < vShow.size(); ii++) {
                        Vector v = (Vector) vShow.get(ii);
                        data[ii] = v.toArray();
                    }
                    MyTableModel dtm = new MyTableModel(data);
                    j10 = new JTable(dtm);
                    jsp = new JScrollPane(j10);
                    ///单元格改变事件
                    j10.getModel().addTableModelListener(new TableModelListener() {
                        @Override
                        public void tableChanged(TableModelEvent e) {
                            if (e.getType() == TableModelEvent.UPDATE) {
                                int row = j10.getEditingRow();
                                if (e.getColumn() == 3) {
                                    maxnewValue = j10.getValueAt(row, e.getColumn()).toString();
                                    if (!maxnewValue.equals(maxoldValue)) {
                                        V_Max.set(row, maxnewValue);
                                        plot.setV_Max(V_Max);
                                    }
                                }
                                if (e.getColumn() == 4) {
                                    minnewValue = j10.getValueAt(row, e.getColumn()).toString();
                                    if (!minnewValue.equals(minoldValue)) {
                                        V_Min.set(row, minnewValue);
                                        plot.setV_Min(V_Min);
                                    }
                                }
                                if (e.getColumn() == 2) {
                                    newName = j10.getValueAt(row, e.getColumn()).toString();
                                    if (!newName.equals(oldName)) {
                                        V_colName.set(row, newName);
                                        oldName = newName;
                                    }
                                }
                            }
                        }
                    });


                    //设置表格每行的高度为30个像素
                    //j10.setRowHeight(30);
                    //创建自定义的表格绘制器
                    MyCellRendererForColorClass mcr = new MyCellRendererForColorClass();
                    //向表格注册指定类型数据的绘制器
                    j10.setDefaultRenderer(Color.class, mcr);
                    //创建自定义的表格编辑器
                    MyEditorForColorClass mefcc = new MyEditorForColorClass();
                    MyEditorForIconClass mefic = new MyEditorForIconClass();
                    //向表格注册指定类型数据的编辑器
                    j10.setDefaultEditor(Color.class, mefcc);
                    j10.setDefaultEditor(Icon.class, mefic);
                    j10.setAutoCreateRowSorter(false);
                    j10.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int clickTimes = e.getClickCount();//获得点击次数
                            if (clickTimes == 2) {  //判断是否为双击
                                int rowNum = j10.getSelectedRow();
                                Vector v1 = new Vector();
                                String s1 = j10.getModel().getValueAt(rowNum, 1).toString();
                                String s2 = j10.getModel().getValueAt(rowNum, 2).toString();
                                String s3 = j10.getModel().getValueAt(rowNum, 3).toString();
                                String s4 = j10.getModel().getValueAt(rowNum, 4).toString();
                                String s5 = j10.getModel().getValueAt(rowNum, 5).toString();
                                String s6 = j10.getModel().getValueAt(rowNum, 6).toString();
                                v1.add(s1);
                                v1.add(s2);
                                v1.add(s3);
                                v1.add(s4);
                                v1.add(s5);
                                v1.add(s6);
                                j10.setValueAt(false, rowNum, 0);

                                UpdateFrame uFrame = new UpdateFrame();
                                uFrame.setV1(v1);
                                uFrame.setOpen(true);
                                uFrame.setRowNum(rowNum);
                                uFrame.setV_Color(vtest);
                                uFrame.setFrame();
                                j10.validate();

                            }
                        }
                    });
                    TableColumn col = j10.getColumn("选择");
                    JCheckBox chk = new JCheckBox();
                    chk.setSelected(false);
                    chk.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            V_color.clear();
                            V_colName.clear();
                            Map mValue=new HashMap();
                            V_Max.clear();
                            V_Min.clear();
                            for (int j0 = 0; j0 < j10.getRowCount(); j0++) {
                                String ss = j10.getModel().getValueAt(j0, 0).toString();
                                if (ss.equals("true")) {
                                    String s1 = j10.getModel().getValueAt(j0, 1).toString();
                                    String s2 = j10.getModel().getValueAt(j0, 2).toString();
                                    String s3 = j10.getModel().getValueAt(j0, 3).toString();
                                    String s4 = j10.getModel().getValueAt(j0, 4).toString();
                                    Color s5 = (Color) j10.getModel().getValueAt(j0, 6);
                                    boolean isin = judge.isContains(s2);
                                    if (isin) {
                                        mValue.put(s2,s1);
                                        V_colName.add(s2);
                                        V_Max.add(s3);
                                        V_Min.add(s4);
                                        V_color.add(s5);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "字段" + s2 + "不存在");
                                        j10.setValueAt(false, j0, 0);
                                    }
                                }
                            }
                            if (V_colName.size() > 0) {
                                Plot.V_colName=V_colName;
                                Plot.V_color=V_color;
                                Plot.V_Max=V_Max;
                                Plot.V_Min=V_Min;
                                plot.setmValue(mValue);
                                plot.setJ1(j10);
                                plot.plot(timeLength);
                                plot.startDG().start();
                            } else {
                                plot.startDG().stop();
                                Plot.dg=null;
                            }
                        }
                    });
                    col.setCellEditor(new DefaultCellEditor(chk));
                    col.setCellRenderer(new MyCheckBoxRenderer());
                    pChk.add(jsp);
                    pChk.validate();
                    plot.addPanel();

                    f.dispose();
                    addMenuBar.iOpenfile = 0;
                } else {
                    JOptionPane.showConfirmDialog(null, "请输入显示时间长度");
                }

            }
        });

        JButton btnSavewAll = new JButton("全选");
        btnSavewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < j1.getRowCount(); i++) {
                    j1.getModel().setValueAt(true, i, 0);
                }
            }
        });
        JLabel lbl = new JLabel("显示时长(S)：");
        panelPop2.add(lbl);
        panelPop2.add(txt);

        JButton btnDel = new JButton("删除");
        ///删除选中的数据
        btnDel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable js=j1;
                DefaultTableModel dtm=(DefaultTableModel)js.getModel();
                int rowCount = j1.getRowCount();
                for (int j0 = 0; j0 < rowCount; j0++) {
                    String ss = js.getModel().getValueAt(j0, 0).toString();
                    if (ss.equals("true")) {
                        int selectRow=js.getSelectedRow();
                        dtm.removeRow(selectRow);
                        j0--;
                        rowCount--;
                    }
                }
                j1=js;
                j1.validate();
                panelPop.removeAll();
                TableColumn col = j1.getColumn("选择");
                JCheckBox chk = new JCheckBox();
                col.setCellEditor(new DefaultCellEditor(chk));
                col.setCellRenderer(new MyCheckBoxRenderer());
                chk.setSelected(true);
                panelPop.add(new JScrollPane(j1));
                panelPop.validate();

            }
        });
        panelPop2.add(btnDel);
        panelPop2.add(btnSavewAll);
        panelPop2.add(btnOk);


        JSplitPane splpop = new JSplitPane();
        splpop.setOneTouchExpandable(false);
        splpop.setContinuousLayout(true);
        splpop.setOrientation(0);
        splpop.setTopComponent(panelPop);
        splpop.setBottomComponent(panelPop2);
        splpop.setDividerSize(10);
        splpop.setDividerLocation(380);

        JPanel panelPop3 = new JPanel();
        panelPop3.add(splpop);

        f.add(panelPop3);
        f.pack();
        f.setVisible(true);
    }
}
