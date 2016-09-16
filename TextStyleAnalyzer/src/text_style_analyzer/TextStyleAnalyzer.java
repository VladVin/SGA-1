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
        HashMap<String, Float> hist = new HashMap<>();
        hist.put("приехать", 14.0f);
        hist.put("выиграть", 7.0f);
        trainSamples.add(new DocSamplePackage(hist, DocSamplePackage.Label.POSITIVE));
        hist = new HashMap<>();
        hist.put("находка", 35.0f);
        hist.put("шкаф", 5.0f);
        trainSamples.add(new DocSamplePackage(hist, DocSamplePackage.Label.NEGATIVE));

        try {
            styleClassifier.train(trainSamples);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
