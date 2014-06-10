import com.mongodb.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.*;

public class ValueFrame extends JFrame {
    public void setV_No(Vector v_No) {
        V_No = v_No;
    }

    public void setmKvalue(Map mKvalue) {
        this.mKvalue = mKvalue;
    }

    private Map mKvalue;

    public void setdTime(double dTime) {
        this.dTime = dTime;
    }

    public void setColl(DBCollection coll) {
        this.coll = coll;
    }

    public void setJ1(JTable j1) {
        this.j1 = j1;
    }

    private JTable j1;
    private Vector V_No;
    private double dTime;
    private DBCollection coll;
    public static boolean isShow = false;

    public void setmValue(Map mValue) {
        this.mValue = mValue;
    }

    private Map mValue;


    public void frameShow() {
        int num = V_No.size();
        JFrame f = new JFrame("曲线值查看");
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
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        f.setSize(300, num * 80);
        f.setResizable(false);

        Map map1 = new HashMap();
        Date dt00 = new Date((long) dTime);
        Date dt01 = new Date((long) dTime - 1000);
        BasicDBObject b2 = new BasicDBObject();
        b2.put("$gte", dt01);
        b2.put("$lte", dt00);

        DBCursor cursor = coll.find(new BasicDBObject("_id", b2)).sort(new BasicDBObject("_id", -1)).limit(1);
        if (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            for (int i = 0; i < num; i++) {
                String sNo = V_No.get(i).toString();
                String sValue = dbo.get(sNo).toString();
                if (sValue.equals("true")) {
                    sValue = "" + 1;
                } else if (sValue.equals("false")) {
                    sValue = "" + 0;
                }
                double dvalue=Double.parseDouble(sValue);
                if(mKvalue.containsKey(sNo)){
                    double valuek=Double.parseDouble(mKvalue.get(sNo).toString());
                    dvalue=valuek*dvalue;
                }
                String sName = sNo;
                map1.put(sName, ""+dvalue);
            }

            isShow = true;

            JPanel p1 = new JPanel();
            final Vector v0 = new Vector();
            v0.add("名称");
            v0.add("块号");
            v0.add("当前值");
            v0.add("最大值");
            v0.add("最小值");
            Vector v1 = new Vector();

            int j = 0;
            NumberFormat formate = java.text.NumberFormat.getNumberInstance();
            formate.setMaximumFractionDigits(4);//设定小数最大为数   ，那么显示的最后会四舍五入的
            Set set1 = map1.entrySet();
            Iterator it1 = set1.iterator();
            while (it1.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it1.next();
                String sname = entry.getKey().toString();
                double dd = Double.parseDouble(entry.getValue().toString());
                Vector v2 = new Vector();
                String showName = mValue.get(sname).toString();
                v2.add(showName);
                v2.add(sname);
                v2.add(dd);

                double max = 0;
                double min = 0;

                for (int i = 0; i < j1.getRowCount(); i++) {
                    String sname1 = j1.getModel().getValueAt(i, 2).toString();
                    if (sname1.equals(sname)) {
                        max = Double.parseDouble(j1.getModel().getValueAt(i, 3).toString());
                        min = Double.parseDouble(j1.getModel().getValueAt(i, 4).toString());
                        v2.add(max);
                        v2.add(min);
                    }
                }
                v1.add(v2);
                j++;
            }

            final JTable jtable = new JTable(v1, v0);
            jtable.setEnabled(false);
            p1.removeAll();
            p1.add(new JScrollPane(jtable));
            p1.validate();
            f.add(p1);
            f.pack();
            f.setVisible(true);
            f.validate();
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    isShow = false;
//                    for (int i = 0; i < j1.getRowCount(); i++) {
//                        String sname = j1.getModel().getValueAt(i, 1).toString();
//                        for (int j = 0; j < jtable.getRowCount(); j++) {
//                            String sname1 = jtable.getModel().getValueAt(j, 1).toString();
//                            if (sname.equals(sname1)) {
//                                j1.setValueAt(jtable.getValueAt(j, 3).toString(), i, 3);
//                                j1.setValueAt(jtable.getValueAt(j, 4).toString(), i, 4);
//                            }
//                        }
//                    }
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "本时刻没有数据");
            f.dispose();
        }


    }
}
