
import org.jfree.base.modules.Module;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import java.util.ArrayList;
import java.util.List;
//credit to https://www.jfree.org/jfreechart/ for visualisation library

public class BarChart_JFC extends ApplicationFrame {

    public BarChart_JFC(String chartTitle, List<DataProcessing.ModuleData> mdatabase) {

        super(chartTitle);
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Module",
                "Mean",
                createDataset(mdatabase),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset(List<DataProcessing.ModuleData> mdatabase) {

        DefaultCategoryDataset data = new DefaultCategoryDataset() ;
        for (DataProcessing.ModuleData m : mdatabase) {
            data.addValue(m.mean, "mean",m.name);
        }
        return data;
    }


}
