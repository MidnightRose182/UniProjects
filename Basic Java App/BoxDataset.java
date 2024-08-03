import org.jfree.data.DomainOrder;
import org.jfree.data.Range;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.statistics.BoxAndWhiskerXYDataset;

import java.util.List;

public class BoxDataset extends AbstractDataset implements BoxAndWhiskerXYDataset {
    private double[] scores;
    private Comparable seriesKey;
    private Number mean;
    private Number median;
    private Number Q1;
    private Number Q3;
    private Number minimumRangeValue;
    private Number maximumRangeValue;
    private Range rangeBounds;
    private double outlierCoefficient = 1.5;
    private double faroutCoefficient = 2.0;

    public BoxDataset(DataProcessing.ModuleData m) {
        this.scores = m.scores;
        this.seriesKey = "Scores";
        this.mean = m.mean;
        this.median = m.median;
        this.Q1 = m.Q1;
        this.Q3 = m.Q3;
        this.minimumRangeValue = null;
        this.maximumRangeValue = null;
        this.rangeBounds = null;
    }

    @Override
    public Number getMeanValue(int i, int i1) {
        return null;
    }

    @Override
    public Number getMedianValue(int i, int i1) {
        return null;
    }

    @Override
    public Number getQ1Value(int i, int i1) {
        return null;
    }

    @Override
    public Number getQ3Value(int i, int i1) {
        return null;
    }

    @Override
    public Number getMinRegularValue(int i, int i1) {
        return null;
    }

    @Override
    public Number getMaxRegularValue(int i, int i1) {
        return null;
    }

    @Override
    public Number getMinOutlier(int i, int i1) {
        return null;
    }

    @Override
    public Number getMaxOutlier(int i, int i1) {
        return null;
    }

    @Override
    public List getOutliers(int i, int i1) {
        return null;
    }

    @Override
    public double getOutlierCoefficient() {
        return 0;
    }

    @Override
    public double getFaroutCoefficient() {
        return 0;
    }

    @Override
    public DomainOrder getDomainOrder() {
        return null;
    }

    @Override
    public int getItemCount(int i) {
        return 0;
    }

    @Override
    public Number getX(int i, int i1) {
        return null;
    }

    @Override
    public double getXValue(int i, int i1) {
        return 0;
    }

    @Override
    public Number getY(int i, int i1) {
        return null;
    }

    @Override
    public double getYValue(int i, int i1) {
        return 0;
    }

    @Override
    public int getSeriesCount() {
        return 0;
    }

    @Override
    public Comparable getSeriesKey(int i) {
        return null;
    }

    @Override
    public int indexOf(Comparable comparable) {
        return 0;
    }
}
