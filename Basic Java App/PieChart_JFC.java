import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
//credit to https://www.jfree.org/jfreechart/ for visualisation library
public class PieChart_JFC extends ApplicationFrame {

    public PieChart_JFC(String chartTitle, List<DataProcessing.ModuleData> mdatabase) {
        super(chartTitle);
        setContentPane(createPiePanel(mdatabase));
    }

    private static PieDataset createDataset(List<DataProcessing.ModuleData> mdatabase) {
        DefaultPieDataset pieDataset= new DefaultPieDataset() ;
        for (DataProcessing.ModuleData m : mdatabase) {
            pieDataset.setValue(m.name,new Double(m.scores.length) );
        }
        return pieDataset;
    }

    private static JFreeChart createChart( PieDataset dataset ) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Module data",
                dataset,
                true,
                true,
                false
        ) ;

        return chart;
    }

    public static JPanel createPiePanel(List<DataProcessing.ModuleData> mdatabase) {
        JFreeChart chart = createChart(createDataset(mdatabase) );
        return new ChartPanel(chart);
    }
}
