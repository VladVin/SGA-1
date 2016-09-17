package text_style_analyzer;

import command_line_parser.CommandLineParser;
import documents_reader.DocumentsReader;
import style_classifier.BagOfWords;
import style_classifier.DocSamplePackage;
import style_classifier.StyleClassifier;
import tokenizer.Tokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by VladVin on 15.09.2016.
 */
public class TextStyleAnalyzer {
    public static void main(String[] args) {
        CommandLineParser clp = new CommandLineParser();
        clp.parse(args);

        switch (clp.getLaunchType()) {
            case TRAIN:
                train(clp.getPosDirs(), clp.getNegDirs());
                break;
            case TEST:
                test(clp.getPredictDocs());
                break;
            case NONE:
                System.out.println("Nothing to do");
                break;
        }
    }

    private static void train(ArrayList<String> posDirs, ArrayList<String> negDirs) {
        ArrayList<String> posDocs;
        ArrayList<String> negDocs;
        try {
            posDocs = DocumentsReader.readDocsFromDirectories(posDirs);
            negDocs = DocumentsReader.readDocsFromDirectories(negDirs);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ArrayList<DocSamplePackage> trainSamples = new ArrayList<>();
        for (String posDoc : posDocs) {
            HashMap<String, Float> docHistogram = Tokenizer.tokenize(posDoc);
            DocSamplePackage docSamplePackage = new DocSamplePackage(
                    docHistogram, DocSamplePackage.Label.POSITIVE
            );
            trainSamples.add(docSamplePackage);
        }
        for (String negDoc : negDocs) {
            HashMap<String, Float> docHistogram = Tokenizer.tokenize(negDoc);
            DocSamplePackage docSamplePackage = new DocSamplePackage(
                    docHistogram, DocSamplePackage.Label.NEGATIVE
            );
            trainSamples.add(docSamplePackage);
        }

        try {
            StyleClassifier styleClassifier = new StyleClassifier();
            styleClassifier.train(trainSamples);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test(ArrayList<String> testDirs) {
        StyleClassifier styleClassifier;
        try {
            styleClassifier = new StyleClassifier();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ArrayList<String> testDocs;
        try {
            testDocs = DocumentsReader.readDocsFromDirectories(testDirs);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ArrayList<HashMap<String, Float>> testSamples = new ArrayList<>();
        for (String testDoc : testDocs) {
            HashMap<String, Float> docHistogram = Tokenizer.tokenize(testDoc);
            testSamples.add(docHistogram);
        }

        try {
            for (int i = 0; i < testSamples.size(); i++) {
                double[] result = styleClassifier.predict(testSamples.get(i));
                System.out.println(
                        "Book " + String.valueOf(i + 1) + ". " +
                                (result[0] >= 0.5f ?
                                ("The author IS THE SAME. Confidence: " + result[0]) :
                                "The author IS'NOT THE SAME. Confidence: " + (1.0f - result[0])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
