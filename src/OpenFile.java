import javax.swing.*;
import java.io.File;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-1-2
 * Time: 下午10:38
 * To change this template use File | Settings | File Templates.
 */
public class OpenFile {
    public void setpChk(JPanel pChk) {
        this.pChk = pChk;
    }

    private JPanel pChk;

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    private int timeLength;

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    private JPanel p0;

    public JTable getJtable() {
        return jtable;
    }

    public void setpChart(JPanel pChart) {
        this.pChart = pChart;
    }

    private JPanel pChart;
    public JTable jtable;
    public static JPanel panelPop = new JPanel();

    public void setV_DbParam(Vector v_DbParam) {
        V_DbParam = v_DbParam;
    }

    private Vector V_DbParam;

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser("C:\\Java");
        fileChooser.addChoosableFileFilter(new DataFilter("csv"));
        int result = fileChooser.showOpenDialog(Observation.frame);
        File file;
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            String sType = file.getAbsolutePath();
            DataImport di = new DataImport();
            ///csv文件
            if (sType.toLowerCase().endsWith(".csv")) {
                jtable = di.ReadCSV(file);
            }
            jtable.setFillsViewportHeight(true);

            OpenFrame openFrame = new OpenFrame();
            openFrame.setTimeLength(timeLength);
            openFrame.setP0(p0);
            openFrame.setpChk(pChk);
            openFrame.setpChart(pChart);
            openFrame.setJtable(jtable);
            openFrame.setPanelPop(panelPop);
            openFrame.setV_DbParam(V_DbParam);
            openFrame.popFrame();
        } else {
            addMenuBar.iOpenfile = 0;
        }
    }
}
