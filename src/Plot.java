import com.mongodb.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;


import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.*;


public class Plot implements ChartMouseListener {

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setP0(JPanel p0) {
        this.p0 = p0;
    }

    public void setpChk(JPanel pChk) {
        this.pChk = pChk;
    }

    public void setpChart(JPanel pChart) {
        this.pChart = pChart;
    }

    public void setV_color(Vector v_color) {
        V_color = v_color;
    }

    public void setV_colName(Vector v_colName) {
        V_colName = v_colName;
    }
    public void setV_Max(Vector v_Max) {
        V_Max = v_Max;
    }

    public void setV_Min(Vector v_Min) {
        V_Min = v_Min;
    }

    public void setJ1(JTable j1) {
        this.j1 = j1;
    }

    private JTable j1;
    public int height;
    public int width;
    private JPanel p0;
    private JPanel pChk;
    private JPanel pChart;
    public static Vector V_color;
    public static Vector V_colName;
    public static Vector V_Max;
    public static Vector V_Min;
    private JFreeChart chart;
    private static ChartPanel chartPanel;
    private static int colNum;
    static JPanel p10 = new JPanel();

    public void setmValue(Map mValue) {
        this.mValue = mValue;
    }

    private Map mValue;
    private Map mapKvalue=new HashMap();///记录名称即K值
    private XYPlot plot=null;


    ////定时数据
    private static TimeSeriesCollection[] dataset;
    private static TimeSeries[] timeSeries;
    private static Mongo m;
    private static DB db;
    private static DBCollection coll;
    private static DBCollection collValue;
    private static Label[] jl;
    private static int count = 0;
    private static NumberAxis[] localNumberAxis1;
    private static StandardXYItemRenderer[] localStandardXYItemRenderer1;

    ///循环搜集数据
    protected class DataGenerator extends Timer implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Vector v1 = V_colName;
            Vector v2 = V_color;
            Vector vmax = V_Max;
            Vector vmin = V_Min;
            chartPanel.validate();
            int count1 = v1.size();
            Calendar calener = Calendar.getInstance();
            Date d1 = calener.getTime();
            Date d2 = new Date(calener.getTime().getTime() - 30000);
            BasicDBObject b2 = new BasicDBObject();
            b2.put("$gte", d2);
            b2.put("$lte", d1);
            DBCursor cursor = coll.find(new BasicDBObject("_id", b2)).sort(new BasicDBObject("_id", -1)).limit(1);
            while (cursor.hasNext()) {
                DBObject dbo = cursor.next();
                for (int i = 0; i < count1; i++) {
                    String sName = v1.get(i).toString();
                    Date ds = (Date) dbo.get("_id");
                    RegularTimePeriod rtp = new Millisecond(ds);
                    int num = timeSeries[i].getItemCount();
                    TimeSeriesDataItem tsd = timeSeries[i].getDataItem(num - 1);
                    RegularTimePeriod rtp1 = tsd.getPeriod();
                    if (rtp.getFirstMillisecond() > rtp1.getFirstMillisecond()) {
                        String sValue = dbo.get(sName).toString();
                        if (sValue.equals("true")) {
                            sValue = "" + 1;
                        } else if (sValue.equals("false")) {
                            sValue = "" + 0;
                        }
                        double dvalue=Double.parseDouble(sValue);
                        if(mapKvalue.containsKey(sName)){
                            double valuek=Double.parseDouble(mapKvalue.get(sName).toString());
                            dvalue=valuek*dvalue;
                        }
                        timeSeries[i].add(new Millisecond(ds), dvalue);
                        double dd = dvalue;
                        NumberFormat formate = java.text.NumberFormat.getNumberInstance();
                        formate.setMaximumFractionDigits(4);//设定小数最大为数   ，那么显示的最后会四舍五入的
                        String m = formate.format(dd);
                        jl[i].setText(m);
                        jl[i].setBackground((Color) v2.get(i));
                        jl[i].validate();
                        localNumberAxis1[i].setLowerBound(Double.parseDouble(vmin.get(i).toString()));
                        localNumberAxis1[i].setUpperBound(Double.parseDouble(vmax.get(i).toString()));
                        localNumberAxis1[i].setAutoRange(false);
                        localStandardXYItemRenderer1[i].setSeriesPaint(0, (Color) v2.get(i));
                        chartPanel.validate();

                        p10.validate();
                        RegularTimePeriod times = timeSeries[i].getDataItem(0).getPeriod();
                        long cmp = rtp.getFirstMillisecond() - times.getFirstMillisecond();
                        if (cmp > count * 1000) {
                            timeSeries[i].delete(0, 1);
                        }
                    }
                }
            }
        }

        DataGenerator(int interval) {
            super(interval, null);
            addActionListener(this);
        }


    }

    public static DataGenerator dg;

    public DataGenerator startDG() {
        if (dg == null) {
            dg = new DataGenerator(200);
        }
        return dg;
    }

    public void plot(int maxAge) {

        p0.removeAll();
        count = maxAge;
        p10.removeAll();
        p0.setLayout(new BorderLayout());
        colNum = V_colName.size();
        localNumberAxis1 = new NumberAxis[colNum];
        localStandardXYItemRenderer1 = new StandardXYItemRenderer[colNum];

        ///绘制实时曲线
        //获取屏幕分辨率的工具集
        Toolkit tool = Toolkit.getDefaultToolkit();
        //利用工具集获取屏幕的分辨率
        Dimension dim = tool.getScreenSize();
        //获取屏幕分辨率的高度
        int height = (int) dim.getHeight();

        dataset = new TimeSeriesCollection[colNum];
        timeSeries = new TimeSeries[colNum];
        jl = new Label[colNum];
        p10.removeAll();
        p10.setLayout(new GridLayout(height / 25, 1));
        for (int i = 0; i < colNum; i++) {
            dataset[i] = new TimeSeriesCollection();
            timeSeries[i] = new TimeSeries("");
            jl[i] = new Label();
            jl[i].setBackground((Color) V_color.get(i));
            p10.add(jl[i]);
        }
        p10.validate();

        Calendar calener = Calendar.getInstance();
        Date d1 = calener.getTime();
        Date d2 = new Date(calener.getTime().getTime() - 180000);
        BasicDBObject b2 = new BasicDBObject();
        b2.put("$gte", d2);
        b2.put("$lte", d1);
        DBCursor cursor = coll.find(new BasicDBObject("_id", b2)).sort(new BasicDBObject("_id", -1)).limit(1);
        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            for (int i = 0; i < V_colName.size(); i++) {
                String sName = V_colName.get(i).toString();
                Date arrDate = (Date) dbo.get("_id");
                String sValue = dbo.get(sName).toString();
                if (sValue.equals("true")) {
                    sValue = "" + 1;
                } else if (sValue.equals("false")) {
                    sValue = "" + 0;
                }
                double dvalue=Double.parseDouble(sValue);
                if(mapKvalue.containsKey(sName)){
                    double valuek=Double.parseDouble(mapKvalue.get(sName).toString());
                    dvalue=valuek*dvalue;
                }
                timeSeries[i].add(new Millisecond(arrDate), dvalue);
            }
        }

        for (int i = 0; i < V_colName.size(); i++) {
            dataset[i].addSeries(timeSeries[i]);
        }
        DateAxis domain = new DateAxis("");
        NumberAxis range = new NumberAxis("");
        domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        range.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        ////数据曲线绘图
        StandardXYItemRenderer renderer3
                = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES);

        plot = new XYPlot(dataset[0], domain, range, renderer3);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        DateAxis domain1 = new DateAxis("");
        NumberAxis range1 = new NumberAxis("");
        domain1.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        range1.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        domain1.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        range1.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.red);
        renderer.setSeriesPaint(1, Color.green);
        renderer.setBaseStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

        ////绘制预测曲线
        XYPlot plotPre = new XYPlot(dataset[0], domain, range, renderer);
        plotPre.setBackgroundPaint(Color.lightGray);
        plotPre.setDomainGridlinePaint(Color.white);
        plotPre.setRangeGridlinePaint(Color.white);
        plotPre.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plotPre.setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT);

        ValueAxis rangeAxis = plot.getRangeAxis();
        //设置最高的一个 Item 与图片顶端的距离
        rangeAxis.setUpperMargin(0.35);
        //设置最低的一个 Item 与图片底端的距离
        rangeAxis.setLowerMargin(0.45);

        NumberAxis rangeAxi1 = new NumberAxis("Value");
        rangeAxi1.setAutoRangeIncludesZero(false);
        CombinedRangeXYPlot xyPlot=new CombinedRangeXYPlot(rangeAxi1);
        xyPlot.setRenderer(renderer3);
        xyPlot.getRangeAxis().setVisible(false);
        xyPlot.setGap(0);
        xyPlot.add(plot, 3);
        xyPlot.add(plotPre,1);
        xyPlot.setOrientation(PlotOrientation.VERTICAL);

        domain.setAutoRange(true);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);
        domain.setTickLabelsVisible(true);
        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart = new JFreeChart("", new Font("SansSerif", Font.BOLD, 24), xyPlot, false);
        chart.setBackgroundPaint(Color.white);
        for (int i = 0; i < V_colName.size(); i++) {
            localNumberAxis1[i] = new NumberAxis("");
            localNumberAxis1[i].setLowerBound(Double.parseDouble(V_Min.get(i).toString()));
            localNumberAxis1[i].setUpperBound(Double.parseDouble(V_Max.get(i).toString()));
            plot.setRangeAxis(i, localNumberAxis1[i]);
            ///实验预测曲线量程设置
            plotPre.setRangeAxis(i, localNumberAxis1[i]);

            XYDataset localXYDataset2 = dataset[i];
            plot.setDataset(i, localXYDataset2);
            plot.mapDatasetToRangeAxis(i, i);
            localStandardXYItemRenderer1[i] = new StandardXYItemRenderer();
            plot.setRenderer(i, localStandardXYItemRenderer1[i]);
            localNumberAxis1[i].setLabelPaint((Color) V_color.get(i));
            localNumberAxis1[i].setTickLabelPaint((Color) V_color.get(i));
            localStandardXYItemRenderer1[i].setSeriesPaint(0, (Color) V_color.get(i));
            //控制小数点
            NumberFormat numformatter = NumberFormat.getInstance(); // 创建一个数字格式格式对象
            numformatter.setMaximumFractionDigits(1);   // 设置数值小数点后最多2位
            numformatter.setMinimumFractionDigits(1);   // 设置数值小数点后最少2位
            localNumberAxis1[i].setNumberFormatOverride(numformatter);    // 设置为Y轴显示数据间隔为10
            localNumberAxis1[i].setVisible(false);
        }
        chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createLineBorder(Color.black)));
        chartPanel.addChartMouseListener(this);
        chartPanel.validate();
        p0.add(chartPanel);
        p0.validate();

    }

    public void addPanel() {
        pChart.removeAll();

        JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setOrientation(1);
        splitPane1.setLeftComponent(p0);
        splitPane1.setRightComponent(p10);
        splitPane1.setDividerSize(0);
        splitPane1.setEnabled(false);
        splitPane1.setResizeWeight(0.98);

        JSplitPane splMain = new JSplitPane();
        splMain.setOneTouchExpandable(true);
        splMain.setOrientation(1);
        splMain.setLeftComponent(pChk);
        splMain.setRightComponent(splitPane1);
        splMain.setDividerSize(10);
        splMain.setDividerLocation(240);


        pChart.setLayout(new GridLayout(1, 1));
        pChart.add(splMain);
        pChart.validate();
    }

    private String paramIp = null;
    private int paramPort = 0;
    private String paramDb = null;
    private String paramColl = null;

    public Plot(Vector V_DbParam) {
        paramIp = V_DbParam.get(0).toString();
        paramPort = Integer.parseInt(V_DbParam.get(1).toString());
        paramDb = V_DbParam.get(2).toString();
        paramColl = V_DbParam.get(3).toString();
        DBCollection meta=null;
        try {
            m = new Mongo(paramIp, paramPort);
            db = m.getDB(paramDb);
            coll = db.getCollection(paramColl);
            collValue = db.getCollection(paramColl);
            meta=db.getCollection("META");
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        /////获取字段名及k值
        DBCursor cursor = meta.find();
        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            double kValue = Double.parseDouble(dbo.get("kvalue").toString());
            if(kValue>0){
                String sValue = dbo.get("_id").toString();
                mapKvalue.put(sValue,kValue);
            }
        }
        cursor.close();
    }

    ValueFrame frame = new ValueFrame();

    public void chartMouseClicked(ChartMouseEvent paramChartMouseEvent) {
        int xPos = paramChartMouseEvent.getTrigger().getX();
        int yPos = paramChartMouseEvent.getTrigger().getY();
        //System.out.println("x = " + xPos + ", y = " + yPos);
        Point2D point2D = this.chartPanel.translateScreenToJava2D(new Point(xPos, yPos));
        ///XYPlot xyPlot = (XYPlot) this.chart.getPlot();
        XYPlot xyPlot = (XYPlot)((CombinedRangeXYPlot) this.chart.getXYPlot()).getSubplots().get(0);
        ChartRenderingInfo chartRenderingInfo = this.chartPanel.getChartRenderingInfo();
        Rectangle2D rectangle2D = chartRenderingInfo.getPlotInfo().getDataArea();
        ValueAxis valueAxis1 = xyPlot.getDomainAxis();
        //ValueAxis valueAxis1 = plot.getDomainAxis();
        //ValueAxis valueAxis1 = xyPlot.getDomainAxis(0);
        RectangleEdge rectangleEdge1 = xyPlot.getDomainAxisEdge();
        ValueAxis valueAxis2 = xyPlot.getRangeAxis();
        RectangleEdge rectangleEdge2 = xyPlot.getRangeAxisEdge();
        double d1 = valueAxis1.java2DToValue(point2D.getX(), rectangle2D, rectangleEdge1);
        ///double d2 = valueAxis2.java2DToValue(point2D.getY(), rectangle2D, rectangleEdge2);
        frame.setmValue(mValue);
        frame.setV_No(V_colName);
        frame.setColl(collValue);
        frame.setJ1(j1);
        frame.setmKvalue(mapKvalue);
        frame.setdTime(d1);
        if (!ValueFrame.isShow) {
            frame.frameShow();
        }

        pChk.validate();

    }

    public void chartMouseMoved(ChartMouseEvent paramChartMouseEvent) {

    }

}
