import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Vector;

public class Observation extends JPanel {
    public static JPanel p0;
    public static JPanel pChart = new JPanel();
    public static JPanel pChk = new JPanel();
    public static Vector V_colName = new Vector();
    public static Vector V_color = new Vector();
    public static JFrame frame;
    private static int timeLength = 0;
    public static void main(String[] args) {
        try {
            String systemInfo = System.getProperty("os.name").toLowerCase();
            if (systemInfo.equals("windows 7")) {
                //在Windows系统中，可以实现Swing界面跟Windows的GUI界面相同
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                frameShow();
            } else {
                frameShow();
            }
        } catch (Exception ex) {
        }

    }

    private static void frameShow() {
        frame = new JFrame("在线数据观察");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        addMenuBar addmenu = new addMenuBar();
        addmenu.setP0(p0);
        addmenu.setpChart(pChart);
        addmenu.setpChk(pChk);
        addmenu.setV_colName(V_colName);
        addmenu.setV_color(V_color);
        addmenu.setTimeLength(timeLength);
        frame.setJMenuBar(addmenu.addMenu());
        frame.add(pChart);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.validate();
    }
}
