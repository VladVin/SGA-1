package text_style_analyzer;

import style_classifier.DocSamplePackage;
import style_classifier.StyleClassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by VladVin on 15.09.2016.
 */
public class TextStyleAnalyzer {
    public static void main(String[] args) {
        StyleClassifier styleClassifier = new StyleClassifier();

        ArrayList<DocSamplePackage> trainSamples = new ArrayList<>();
        HashMap<String, Float> hist1 = new HashMap<>();
        hist1.put("приехать", 14.0f);
        hist1.put("выиграть", 7.0f);
        hist1.put("пирамида", 10.0f);
        trainSamples.add(new DocSamplePackage(hist1, DocSamplePackage.Label.POSITIVE));
        HashMap<String, Float> hist2 = new HashMap<>();
        hist2.put("находка", 35.0f);
        hist2.put("шкаф", 5.0f);
        hist2.put("пирамида", 4.0f);
        trainSamples.add(new DocSamplePackage(hist2, DocSamplePackage.Label.NEGATIVE));

        // Train stage
        try {
            styleClassifier.train(trainSamples);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Predict stage
        try {
            double[] result = styleClassifier.predict(hist1);
            System.out.println(
                    result[0] >= 0.5f ?
                        ("The author is the same. Confidence: " + result[0]) :
                        "The author isn't the same. Confidence: " + (1.0f - result[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
