package style_classifier;

import java.util.*;

/**
 * Created by VladVin on 15.09.2016.
 */
public class BagOfWords {

    private static final int MAX_VOCABULARY_SIZE = 5000;

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

//        String docDescriptor = "";
//        for (int i = 0; i < docTf.size() - 1; i++) {
//            docDescriptor += docTf.get(i) + ", ";
//        }
//        if (docTf.size() != 0) {
//            docDescriptor += docTf.get(docTf.size() - 1);
//        }
//
//        return docDescriptor;
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
        // The simplest way of calculating term frequency
        return rawTermFrequency;
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
