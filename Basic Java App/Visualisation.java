import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfree.chart.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.DomainOrder;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.date.DateUtilities;
//credit to https://www.jfree.org/jfreechart/ for visualisation library


public class Visualisation extends DataProcessing {

    public Visualisation(List<StudentData> sdatabase, List<ModuleData> mdatabase){

        JFrame vis = new JFrame("Visualisation Menu") ;
        JPanel area = new JPanel() ;
        JPanel buttons = new JPanel() ;



        JButton b1 = new JButton("Show module piechart") ;
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PieChart_JFC pc = new PieChart_JFC("Module Piechart", mdatabase) ;
                pc.setSize(500,500);
                pc.setVisible(true);
            }
        });


        JButton b2 = new JButton("Create module mean graph") ;
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarChart_JFC bc = new BarChart_JFC("Module Barchart", mdatabase) ;
                bc.setSize(500,500);
                bc.setDefaultCloseOperation(3);
                bc.setVisible(true);
            }
        });

        JButton b3 = new JButton("View Module Info") ;
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame x = new JFrame("Select Module Code") ;
                JPanel p = new JPanel() ;
                JComboBox list = new JComboBox(moduleNames);
                JButton submit = new JButton("OK") ;
                submit.setSize(30,30);
                JLabel inpMessage = new JLabel();
                p.add(list);
                p.add(submit) ;
                p.setLayout(new FlowLayout());
                p.add(inpMessage);
                x.add(p) ;
                x.setSize(400,150);
                x.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                x.setVisible(true);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //----------------------------------------------------------------------------------------------
                        //Scatter Plot
                        String inp1 = (String) list.getItemAt(list.getSelectedIndex());
                        String selected = "You selected " + list.getItemAt(list.getSelectedIndex());
                        inpMessage.setText(selected);
                        //code takes the input from button and turns it into a string

                        XYSeriesCollection dataset = new XYSeriesCollection();
                        XYSeries s1 = new XYSeries("Students");

                        for (StudentData sd : sdatabase) {
                            for (String score : sd.scores) {
                                if (score.contains(inp1)) {
                                    String s = score.split(" ")[1]; //splits "CE101: 42" and returns "42"
                                    s1.add(sd.mean, Integer.parseInt(s));
                                }
                            }
                            //find student in course scores and make the student average grade y

                        }

                        ModuleData module = null;
                        for (ModuleData m : mdatabase) {
                            if (inp1 == m.name) {
                                module = m;
                            }
                        }

                        dataset.addSeries(s1);
                        JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                                inp1 + " - Difficulty: "+ module.difficulty,
                                "Grade in " + inp1,
                                "Student GPA",
                                dataset
                        );
// ----------------------------------------------------------------------------------------------------------------------
                        // Table
                        DefaultTableModel table = new DefaultTableModel();
                        JTable mod = new JTable();
                        String[] colnames = {"Student No.", "Grade", "GPA"};
                        table.setColumnIdentifiers(colnames);
                        mod.setModel(table);
                        JScrollPane scroll = new JScrollPane(mod);
                        JFrame present = new JFrame("Module Info");
                        for (StudentData s : sdatabase) {
                            for (String score : s.scores) {
                                if (score.contains(inp1)) {
                                    Object o[] = new Object[3];
                                    o[0] = s.regNum;
                                    String num = score.split(" ")[1];
                                    o[1] = Integer.parseInt(num);
                                    o[2] = s.mean;
                                    table.addRow(o);
                                }
                            }
                        }
                        mod.setPreferredSize(new Dimension(300, 400));
                        //-----------------------------------------------------------------------------------
                        // Box Plot
                        // Ideally I'd customise the XYDataset interface to make a horizontal box chart that doesn't require time but oh well

                        //BoxDataset boxdata = new BoxDataset(module);
                        DefaultBoxAndWhiskerXYDataset boxdata1 = new DefaultBoxAndWhiskerXYDataset("Score");
                        ArrayList<Double> datalist = new ArrayList<Double>();
                        Date date = DateUtilities.createDate(2023, 06, 26);
                        for(double s : module.scores) {
                            datalist.add((double) s);
                        }
                        boxdata1.add(date, BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(datalist));

                        JFreeChart box = ChartFactory.createBoxAndWhiskerChart(
                                "Mean: "+module.mean+"    Median: "+module.median,
                                "Time",
                                "Score",
                                boxdata1,
                                true);
                        box.setBackgroundPaint(new Color(255, 255, 255));
                        //---------------------------------------------------------------------------------------------
                        //Histogram
                        HistogramDataset histdata = new HistogramDataset();
                        histdata.setType(HistogramType.RELATIVE_FREQUENCY);
                        histdata.addSeries("Scores", module.scores, 4);
                        JFreeChart hist = ChartFactory.createHistogram(
                                module.name,
                                "Data",
                                "Frequency",
                                histdata,
                                PlotOrientation.VERTICAL,
                                false,
                                false,
                                false);


                        mod.setAutoCreateRowSorter(true);
                        present.setLayout(new FlowLayout());
                        ChartPanel scatter = new ChartPanel(scatterPlot);
                        scatter.setPreferredSize(new java.awt.Dimension(400, 300));
                        ChartPanel boxplot = new ChartPanel(box);
                        boxplot.setPreferredSize(new java.awt.Dimension(400,300));
                        ChartPanel histogram = new ChartPanel(hist);
                        histogram.setPreferredSize(new java.awt.Dimension(400,300));
                        present.add(scatter);
                        present.add(scroll);
                        present.add(boxplot);
                        present.add(histogram);
                        present.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        present.setSize(1200, 800);
                        present.setVisible(true);

                    }
                });
            }
        });

        /*JButton b4 = new JButton() ;
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

         */


        buttons.add(b1) ;
        buttons.add(b2) ;
        buttons.add(b3) ;
        //buttons.add(b4) ;
        area.add(buttons) ;


        vis.setSize(800, 100);
        vis.add(area, "Center");
        vis.setDefaultCloseOperation(3);
        vis.setVisible(true);

    }

}
