package style_classifier;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.core.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by VladVin on 15.09.2016.
 */
public class StyleClassifier {

    private static final String MODEL_NAME = "first_version.model";
    private static final String PATH_TO_MODELS = "model/";
    private static final String TRAINING_DATESET_NAME = "train_dataset";
    private static final String PATH_TO_TRAINING_DATASET = "datasets/";

    private BagOfWords bagOfWords;
    private Classifier model;
    private ArrayList<Attribute> feature;
    private Instances trainingDataSet;

    public StyleClassifier() throws IOException {
        this.bagOfWords = new BagOfWords();
        this.model = new LinearRegression();

        createDirIfNotExist(PATH_TO_MODELS);
        createDirIfNotExist(PATH_TO_TRAINING_DATASET);
    }

    public void train(ArrayList<DocSamplePackage> trainSamples) throws Exception {
        ArrayList<ArrayList<Float>> docDescriptors =
                bagOfWords.parseTrainHistograms(trainSamples);

        Instances instances = fillDataSet(docDescriptors, trainSamples);

        model.buildClassifier(instances);

        // Save trained model
        SerializationHelper.write(PATH_TO_MODELS + MODEL_NAME, model);
    }

    public double[] predict(
            HashMap<String, Float> docHistogram) throws Exception {
        ArrayList<Float> docDescriptor =
                bagOfWords.calcDocDescriptor(docHistogram);
        Instance predictInstance = convertInstance(docDescriptor);

        // Load trained model
        model = (Classifier) SerializationHelper.read(PATH_TO_MODELS + MODEL_NAME);

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

        // Save training dataset
        SerializationHelper.write(PATH_TO_TRAINING_DATASET + TRAINING_DATESET_NAME,
                trainingDataSet);

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
        // Load training dataset
        trainingDataSet = (Instances)
                SerializationHelper.read(
                        PATH_TO_TRAINING_DATASET + TRAINING_DATESET_NAME);

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

    private void createDirIfNotExist(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            Path p = Paths.get(path);
            Files.createDirectory(p);
        }
    }
}
