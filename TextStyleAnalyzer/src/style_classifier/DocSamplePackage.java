package style_classifier;

import java.util.HashMap;

/**
 * Created by VladVin on 16.09.2016.
 */
public class DocSamplePackage {
    private HashMap<String, Float> histogram;
    private Label label;

    public enum Label { POSITIVE, NEGATIVE }

    public DocSamplePackage(HashMap<String, Float> histogram, Label label) {
        this.histogram = histogram;
        this.label = label;
    }

    public HashMap<String, Float> getHistogram() {
        return histogram;
    }

    public Label getLabel() {
        return label;
    }
}
