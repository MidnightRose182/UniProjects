import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import org.jfree.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class StudentStats extends DataProcessing {
    public StudentStats (List<StudentData> sdatabase) {

        JFrame stats = new JFrame("Student and Course Data");
        JPanel area = new JPanel();
        JPanel buttons = new JPanel();

        JButton b1 = new JButton("Show all student data");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel tablemodel = new DefaultTableModel();
                JTable stu = new JTable();
                String[] cnames = {"Course", "Student No.", "Average", "Median", "Standard Deviation", "Ability Score"};
                JFrame dataframe = new JFrame("Data");
                tablemodel.setColumnIdentifiers(cnames);
                stu.setModel(tablemodel);
                JScrollPane scroll = new JScrollPane(stu);
                try {
                    dataframe.setVisible(true);
                    dataframe.add(scroll);
                    dataframe.setSize(700, 400);
                    for (StudentData s : sdatabase) {
                        Object o[] = new Object[6];
                        o[0] = s.course;
                        o[1] = s.regNum;
                        o[2] = s.mean;
                        o[3] = s.median;
                        double sd = Math.round(s.sd * 100);
                        o[4] = sd / 100;
                        double sas = Math.round(s.sas * 100);
                        o[5] = sas / 100;
                        tablemodel.addRow(o);
                    }
                    stu.setAutoCreateRowSorter(true);
                } catch (NullPointerException npe) {
                    JOptionPane.showMessageDialog(dataframe, "You must first import a dataset");
                } catch (IndexOutOfBoundsException oob) {
                    JOptionPane.showMessageDialog(dataframe, "Imported dataset in wrong format");
                }
            }
        });

        JButton b2 = new JButton("Course Data");
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame x = new JFrame("Select Course Name");
                JPanel p = new JPanel();
                JComboBox list = new JComboBox(courseNames);
                JButton submit = new JButton("OK");
                submit.setSize(30, 30);
                JLabel inpMessage = new JLabel();
                p.add(list);
                p.add(submit);
                p.setLayout(new FlowLayout());
                p.add(inpMessage);
                x.add(p);
                x.setSize(400, 150);
                x.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                x.setVisible(true);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String inp1 = (String) list.getItemAt(list.getSelectedIndex());
                        String selected = "You selected " + list.getItemAt(list.getSelectedIndex());
                        inpMessage.setText(selected);
                        //code takes the input from button and turns it into a string

                        XYSeriesCollection dataset = new XYSeriesCollection();
                        XYSeries s1 = new XYSeries("Students");
                    }
                });
            }
        });


        buttons.add(b1) ;
        buttons.add(b2);
        area.add(buttons) ;

        stats.setSize(800, 100);
        stats.add(area, "Center");
        stats.setDefaultCloseOperation(3);
        stats.setVisible(true);
    }
}

