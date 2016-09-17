package style_classifier;

import java.io.*;
import java.util.*;

/**
 * Created by VladVin on 15.09.2016.
 */
public class BagOfWords {

    private static final int MAX_VOCABULARY_SIZE = 5000;
    private static final TfWeight TF_WEIGHT = TfWeight.RAW_FREQUENCY;
    private static final String VOCABULARY_PATH = "vocabulary.txt";

    private enum TfWeight {
        BINARY_FREQUENCY, RAW_FREQUENCY, LOG_NORMALIZATION }

    private ArrayList<String> vocabulary;

    public ArrayList<ArrayList<Float>> parseTrainHistograms(
            ArrayList<DocSamplePackage> trainSamples) throws Exception {
        if (trainSamples == null || trainSamples.size() == 0) {
            return null;
        }

        extractVocabulary(trainSamples);

        ArrayList<ArrayList<Float>> docDescriptors = new ArrayList<>();
        for (DocSamplePackage sample : trainSamples) {
            HashMap<String, Float> histogram = sample.getHistogram();
            ArrayList<Float> docDescriptor = calcDocDescriptor(histogram);
            docDescriptors.add(docDescriptor);
        }

        return docDescriptors;
    }
    public ArrayList<Float> calcDocDescriptor(
            HashMap<String, Float> histogram) throws Exception {
        vocabulary = loadVocabulary();

        float countOfTerms = 0.0f;
        for (float count : histogram.values()) {
            countOfTerms += count;
        }
        ArrayList<Float> docTf = new ArrayList<>();
        for (String word : vocabulary) {
            Float termCount = histogram.get(word);
            if (termCount != null) {
                float rawTermFrequency = termCount / countOfTerms;
                float tf = calcTf(rawTermFrequency);
                docTf.add(tf);
            } else {
                docTf.add(0.0f);
            }
        }

        return docTf;
    }

    private void extractVocabulary(
            ArrayList<DocSamplePackage> samples) throws Exception {
        // Determine the vocabulary through all documents
        HashMap<String, Float> allWords = new HashMap<>();
        for (DocSamplePackage sample : samples) {
            HashMap<String, Float> docHistogram = sample.getHistogram();
            for (String word : docHistogram.keySet()) {
                Float oldCount = allWords.get(word);
                Float curCount = docHistogram.get(word);
                if (oldCount != null) {
                    allWords.replace(word, oldCount + curCount);
                } else {
                    allWords.put(word, curCount);
                }
            }
        }

        // Extract vocabulary of most frequent words
        ArrayList<Map.Entry<String, Float>> sortedVoc = getEntriesBySortingMapByValues(allWords);
        vocabulary = new ArrayList<>();
        int desiredVocSize = sortedVoc.size() >= MAX_VOCABULARY_SIZE ?
                MAX_VOCABULARY_SIZE : sortedVoc.size();
        for (int i = desiredVocSize - 1; i >= 0; i--) {
            vocabulary.add(sortedVoc.get(i).getKey());
        }

        saveVocabulary(vocabulary);
    }

    private String mergeDocDescriptors(ArrayList<String> docDescriptors) {
        String mergedDescriptors = "";
        for (int i = 0; i < docDescriptors.size() - 1; i++) {
            mergedDescriptors += docDescriptors.get(i) + "\n";
        }
        if (docDescriptors.size() != 0) {
            mergedDescriptors += docDescriptors.get(docDescriptors.size() - 1);
        }
        return mergedDescriptors;
    }

    private float calcTf(float rawTermFrequency) {
        // Choose the way of calculating term frequency weight
        switch (TF_WEIGHT) {
            case BINARY_FREQUENCY:
                if (rawTermFrequency > 0.0f) {
                    return 1.0f;
                } else {
                    return 0.0f;
                }
            case RAW_FREQUENCY:
                return rawTermFrequency;
            case LOG_NORMALIZATION:
                return 1.0f + (float)Math.log(rawTermFrequency);
            default:
                return rawTermFrequency;
        }
    }

    private ArrayList<Map.Entry<String, Float>> getEntriesBySortingMapByValues(
            HashMap<String, Float> passedMap) {

        ArrayList<Map.Entry<String, Float>> entryList =
                new ArrayList<>(passedMap.entrySet());

        entryList.sort(new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        return entryList;
    }

    private void saveVocabulary(ArrayList<String> vocabulary) throws Exception {
        if (vocabulary == null) {
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(VOCABULARY_PATH);
            BufferedWriter bw = new BufferedWriter(fileWriter);

            for (String vocRecord : vocabulary) {
                bw.write(vocRecord);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            throw new Exception("Cannot save vocabulary");
        }
    }

    private ArrayList<String> loadVocabulary() throws Exception {
        try {
            ArrayList<String> vocabulary = new ArrayList<>();

            FileReader fileReader = new FileReader(VOCABULARY_PATH);
            BufferedReader br = new BufferedReader(fileReader);

            String line;
            while ((line = br.readLine()) != null) {
                vocabulary.add(line);
            }

            br.close();

            return vocabulary;
        } catch (FileNotFoundException e) {
            throw new Exception("Cannot load vocabulary");
        }
    }
}
