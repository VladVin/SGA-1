package style_classifier;

import weka.classifiers.Classifier;
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

    private void generateFeature(int numberOfWords) {
        feature = new ArrayList<>(numberOfWords + 1);
        for (int i = 0; i < numberOfWords; i++) {
            Attribute attribute = new Attribute(String.valueOf(i));
            feature.add(attribute);
        }

        ArrayList<String> fvClassValues = new ArrayList<>(2);
        fvClassValues.add("positive");
        fvClassValues.add("negative");
        Attribute classAttribute = new Attribute("class", fvClassValues);

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

        generateFeature(countOfWordAttributes);

        Instances instances = new Instances("train", feature, countOfWordAttributes + 1);
        instances.setClassIndex(countOfWordAttributes);

        for (int i = 0; i < docDescriptors.size(); i++) {
            ArrayList<Float> docDescriptor = docDescriptors.get(i);
            DocSamplePackage docPackage = docPackages.get(i);
            Instance instance = new DenseInstance(countOfWordAttributes + 1);
            instance.setDataset(instances);
            for (int j = 0; j < docDescriptor.size(); j++) {
                instance.setValue(j, docDescriptor.get(j));
            }
            instance.setValue(countOfWordAttributes,
                    docPackage.getLabel() == DocSamplePackage.Label.POSITIVE ? "positive" : "negative");
            instances.add(instance);
        }

        return instances;
    }
}
