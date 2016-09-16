package style_classifier;

import java.util.*;

/**
 * Created by VladVin on 15.09.2016.
 */
public class BagOfWords {

    private static final int MAX_VOCABULARY_SIZE = 5000;
    private static final TfWeight TF_WEIGHT = TfWeight.RAW_FREQUENCY;

    private enum TfWeight {
        BINARY_FREQUENCY, RAW_FREQUENCY, LOG_NORMALIZATION }

    private ArrayList<String> vocabulary;

    public ArrayList<ArrayList<Float>> parseTrainHistograms(
            ArrayList<DocSamplePackage> trainSamples) {
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

    public ArrayList<Float> calcDocDescriptor(HashMap<String, Float> histogram) {
        if (vocabulary == null) {
            return null;
        }

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

    private void extractVocabulary(ArrayList<DocSamplePackage> samples) {
        // Determine the vocabulary through all documents
        HashMap<String, Float> allWords = new HashMap<>();
        for (DocSamplePackage sample : samples) {
            HashMap<String, Float> docHistogram = sample.getHistogram();
            for (String word : docHistogram.keySet()) {
                Float oldCount = allWords.get(word);
                if (oldCount != null) {
                    allWords.replace(word, oldCount + docHistogram.get(word));
                } else {
                    allWords.put(word, 0.0f);
                }
            }
        }

        // Extract vocabulary of most frequent words
        HashMap<String, Float> sortedVoc = sortHashMapByValues(allWords);
        Object[] sortedVocArr = sortedVoc.keySet().toArray();
        vocabulary = new ArrayList<>();
        int desiredVocSize = sortedVocArr.length >= MAX_VOCABULARY_SIZE ?
                MAX_VOCABULARY_SIZE : sortedVocArr.length;
        for (int i = desiredVocSize - 1; i >= 0; i--) {
            vocabulary.add((String)sortedVocArr[i]);
        }
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

    private HashMap<String, Float> sortHashMapByValues(
            HashMap<String, Float> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Float> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        HashMap<String, Float> sortedMap = new HashMap<>();

        Iterator<Float> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Float val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Float comp1 = passedMap.get(key);
                Float comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
