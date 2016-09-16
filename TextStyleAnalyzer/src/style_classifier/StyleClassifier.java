package style_classifier;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.RandomForest;
import weka.core.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by VladVin on 15.09.2016.
 */
public class StyleClassifier {

    private BagOfWords bagOfWords;
    private Classifier model;
    private ArrayList<Attribute> feature;
    private Instances trainingDataSet;

    public StyleClassifier() {
        this.bagOfWords = new BagOfWords();
        this.model = new RandomForest();
    }

    public void train(ArrayList<DocSamplePackage> trainSamples) throws Exception {
        ArrayList<ArrayList<Float>> docDescriptors =
                bagOfWords.parseTrainHistograms(trainSamples);

        Instances instances = fillDataSet(docDescriptors, trainSamples);
        model.buildClassifier(instances);
    }

    public double[] predict(
            HashMap<String, Float> docHistogram) throws Exception {
        ArrayList<Float> docDescriptor =
                bagOfWords.calcDocDescriptor(docHistogram);
        Instance predictInstance = convertInstance(docDescriptor);

        return model.distributionForInstance(predictInstance);
    }

    private void generateFeature(int numberOfWords) {
        feature = new ArrayList<>(numberOfWords + 1);
        for (int i = 0; i < numberOfWords; i++) {
            Attribute attribute = new Attribute(String.valueOf(i));
            feature.add(attribute);
        }

        Attribute classAttribute = new Attribute("class");
        feature.add(classAttribute);
    }

    private Instances fillDataSet(
            ArrayList<ArrayList<Float>> docDescriptors,
            ArrayList<DocSamplePackage> docPackages) throws Exception {
        if (docDescriptors == null || docDescriptors.size() == 0
                || docDescriptors.get(0) == null || docPackages == null
                || docPackages.size() == 0
                || docDescriptors.size() != docPackages.size()) {
            throw new Exception("Bad document descriptors of packages");
        }
        int countOfWordAttributes = docDescriptors.get(0).size();
        int samplesCount = docDescriptors.size();

        generateFeature(countOfWordAttributes);

        trainingDataSet = new Instances("train", feature, samplesCount);
        trainingDataSet.setClassIndex(feature.size() - 1);

        for (int i = 0; i < docDescriptors.size(); i++) {
            ArrayList<Float> docDescriptor = docDescriptors.get(i);
            DocSamplePackage docPackage = docPackages.get(i);
            Instance instance = new DenseInstance(feature.size());
            instance.setDataset(trainingDataSet);
            for (int j = 0; j < docDescriptor.size(); j++) {
                instance.setValue(j, docDescriptor.get(j));
            }

            instance.setClassValue(
                    docPackage.getLabel() == DocSamplePackage.Label.POSITIVE ? 1.0f : 0.0f);
            trainingDataSet.add(instance);
        }

        return trainingDataSet;
    }

    private Instance convertInstance(ArrayList<Float> docDescriptor) throws Exception {
        if (trainingDataSet == null) {
            throw new Exception("There is no training data set yet");
        }

        Instance instance = new DenseInstance(docDescriptor.size());
        instance.setDataset(trainingDataSet);

        for (int i = 0; i < docDescriptor.size(); i++) {
            instance.setValue(i, docDescriptor.get(i));
        }

        return instance;
    }
}
