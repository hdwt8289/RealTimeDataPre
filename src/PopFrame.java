import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.text.TableView;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class PopFrame extends JPanel {

    public static JPanel panelPop = new JPanel();
    public static Vector v0 = new Vector();
    public static JTable j1=new JTable();
    private static Vector V_Color = new Vector();

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    private JPanel p0;

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

    public void setV_DbParam(Vector v_DbParam) {
        V_DbParam = v_DbParam;
    }

    private Vector V_DbParam;

    private static Vector V_color = new Vector();
    private static Vector V_No = new Vector();
    private static Vector V_Max = new Vector();
    private static Vector V_Min = new Vector();
    public static JTable j10;

    private static JudgeContains judge;

    private static JScrollPane jsp;
    Object[][] data;
    String maxoldValue, maxnewValue, minoldValue, minnewValue;
    String oldName, newName;

    public void PopFrame() {
        final JFrame f = new JFrame("新建趋势");

        f.setSize(300, 400);
        f.setResizable(false);
        // f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //获取屏幕分辨率的工具集
        Toolkit tool = Toolkit.getDefaultToolkit();
        //利用工具集获取屏幕的分辨率
        Dimension dim = tool.getScreenSize();
        //获取屏幕分辨率的高度
        int height = (int) dim.getHeight();
        //获取屏幕分辨率的宽度
        int width = (int) dim.getWidth();
        //设置位置
        f.setLocation((width - 300) / 2, (height - 400) / 2);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addMenuBar.iPopFrame = 0;
            }
        });
        JPanel panelPop2 = new JPanel();
        JLabel lbl = new JLabel("显示时长(S)：");
        final JTextField txt = new JTextField(20);
        //f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LimitedDocument ld = new LimitedDocument(8);//参数为能输入的最大长度
        ld.setAllowChar("0123456789.");//只能输入的字符
        txt.setDocument(ld);

        JButton btnAdd = new JButton("新增");

        panelPop.setPreferredSize(new Dimension(600, 400));
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelPop.removeAll();
                SetFrame setFrame = new SetFrame(V_DbParam);
                setFrame.setPanel(panelPop);
                setFrame.setV1(v0);
                setFrame.setV_Color(V_Color);
                setFrame.setFrame();
            }
        });
        JButton btnSave = new JButton("保存");
        ///保存趋势
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (j1 != null) {
                    JFileChooser fileChooser = new JFileChooser("C:\\Java");
                    fileChooser.addChoosableFileFilter(new DataFilter("csv"));
                    fileChooser.setDialogType(1);
                    int result = fileChooser.showSaveDialog(Observation.frame);
                    File file;
                    if (result == 0) {
                        file = fileChooser.getSelectedFile();
                        ///判断文件名中是否包含文件后缀，如果没有，则需要自动添加
                        if (!file.toString().contains(".")) {
                            String tail = fileChooser.getFileFilter().getDescription();
                            int s1 = tail.indexOf("(");
                            int e1 = tail.indexOf(")");

                            String sa = tail.substring(s1 + 2, e1);
                            file = new File(file.toString() + sa);
                        }

                        String sType = file.getAbsolutePath();
                        DataExp de = new DataExp();
                        if (sType.toLowerCase().endsWith(".csv")) {
                            de.DataExportCsv(sType, j1);
                        }
                    }
                    f.dispose();
                    addMenuBar.iPopFrame = 0;
                }
            }
        });
        JButton btnSaveAll = new JButton("全选");
        btnSaveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < j1.getRowCount(); i++) {
                    j1.getModel().setValueAt(true, i, 0);
                }
            }
        });

        JButton btnOk = new JButton("确定");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txt.getText().isEmpty()) {
                    judge = new JudgeContains(V_DbParam);
                    JTable js=j1;
                    timeLength = Integer.parseInt(txt.getText().toString());
                    Vector vShow = new Vector();
                    Vector vtest = new Vector();
                    int rowCount = js.getRowCount();
                    for (int j0 = 0; j0 < rowCount; j0++) {
                        String ss = js.getModel().getValueAt(j0, 0).toString();
                        String paramName = js.getModel().getValueAt(j0, 1).toString();
                        String paramNo = js.getModel().getValueAt(j0, 2).toString();
                        String maxValue = js.getModel().getValueAt(j0, 3).toString();
                        String minValue = js.getModel().getValueAt(j0, 4).toString();
                        String paramUnit = js.getModel().getValueAt(j0, 5).toString();
                        String colcolor = js.getModel().getValueAt(j0, 6).toString();
                        Color c1 = new Color((int)Double.parseDouble(colcolor));
                        if (ss.equals("true")) {
                            Vector v2 = new Vector();
                            int abc = j0 + 1;
                            v2.add(abc);
                            v2.add(paramName);
                            v2.add(paramNo);
                            v2.add(maxValue);
                            v2.add(minValue);
                            v2.add(paramUnit);
                            v2.add(c1);
                            vShow.add(v2);

                            vtest.add(c1);
                        }
                    }

                    Vector vTitle = new Vector();
                    vTitle.add("选择");
                    vTitle.add("名称");
                    vTitle.add("块号");
                    vTitle.add("最大值");
                    vTitle.add("最小值");
                    vTitle.add("单位");
                    vTitle.add("颜色");

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
                    final Plot plot= new Plot(V_DbParam);
                    plot.setpChart(pChart);
                    plot.setP0(p0);
                    plot.setpChk(pChk);
                    plot.setHeight(height * 87 / 100);
                    plot.setWidth(width * 90 / 100);

                    pChk.removeAll();
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

                    //设置表格每行的高度为30个像素
                    // j10.setRowHeight(30);
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
                    //j10.setRowSelectionAllowed(true);
                    j10.getModel().addTableModelListener(new TableModelListener() {
                        @Override
                        public void tableChanged(TableModelEvent e) {
                            if (e.getType() == TableModelEvent.UPDATE) {
                                int row = j10.getEditingRow();
                                if (e.getColumn() == 3) {
                                    ////修改极值只更新一条的原因在这里e.getLastRow()
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
                                        V_No.set(row, newName);
                                        oldName = newName;
                                    }
                                }
                            }
                        }
                    });


                    TableColumn col0 = j10.getColumn("选择");
                    JCheckBox chk0 = new JCheckBox();
                    chk0.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            V_color.clear();
                            V_No.clear();
                            ////V_name.clear();
                            Map  mValue=new HashMap();
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
                                        V_No.add(s2);
                                        V_Max.add(s3);
                                        V_Min.add(s4);
                                        V_color.add(s5);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "字段" + s2 + "不存在");
                                        j10.setValueAt(false, j0, 0);
                                    }
                                }
                            }
                            if (V_No.size() > 0) {
                                plot.setV_colName(V_No);
                                plot.setV_color(V_color);
                                plot.setmValue(mValue);
                                plot.setV_Max(V_Max);
                                plot.setV_Min(V_Min);
                                plot.setJ1(j10);
                                plot.plot(timeLength);
                                plot.startDG().start();
                            } else {
                                plot.startDG().stop();
                            }
                        }
                    });
                    col0.setCellEditor(new DefaultCellEditor(chk0));
                    col0.setCellRenderer(new MyCheckBoxRenderer());
                    pChk.add(jsp);
                    pChk.validate();
                    plot.addPanel();
                    addMenuBar.iPopFrame = 0;
                    f.dispose();
                } else {
                    JOptionPane.showConfirmDialog(null, "请输入显示时间长度");
                }
            }
        });
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
        panelPop2.add(lbl);
        panelPop2.add(txt);
        panelPop2.add(btnAdd);
        panelPop2.add(btnDel);
        panelPop2.add(btnSave);
        panelPop2.add(btnSaveAll);
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
        panelPop3.setLayout(new GridLayout(1, 1));
        panelPop3.add(splpop);
        f.add(panelPop3);
        f.pack();
        f.setVisible(true);
    }
}

