package tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    final static int WORD_SIZE = 3;
    final static int LEFT_GAP = 3;
    final static int RIGHT_GAP = 3;
    final static int NUMBER_OF_WORDS = 10000;

    public static HashMap<String, Float> tokenize (String doc){

        doc = doc.toLowerCase();
        HashMap <String, Float>  hm = new HashMap<>();
        ArrayList<String> keys= new ArrayList<>();
        String currentWord;
        String regexp = "\\p{L}{"+ WORD_SIZE +",}";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(doc);
        int wordCounter = 0;

        float portion;
        float value;
        String key;

        while (m.find() && (wordCounter < NUMBER_OF_WORDS)) {
            if (!(LEFT_GAP == 0 && RIGHT_GAP == 0)) {
                currentWord = m.group(0);
                for (HashMap.Entry<String, Float> entry : hm.entrySet()) {
                    key = findSubstring(entry.getKey(), currentWord);
                    if (key != null) {
                        keys.add(entry.getKey());
                    }
                }

                if (!keys.isEmpty()) {
                    portion = 1 / (float) keys.size();

                    while (keys.size() > 0) {

                        key = findSubstring(currentWord, keys.get(0));
                        value = hm.get(keys.get(0)) + portion;
                        if (hm.containsKey(key) && (!key.equals(keys.get(0)))) {
                            value += hm.get(key);
                        }
                        if (!key.equals(keys.get(0))) {
                            hm.remove(keys.get(0));
                        }
                        hm.put(key, value);
                        keys.remove(0);
                    }
                } else {
                    hm.put(currentWord, (float) 1);
                }

            }
            else {
                currentWord = m.group(0);
                if (hm.containsKey(currentWord)){
                    hm.put(currentWord, hm.get(currentWord)+1);
                } else hm.put(currentWord, (float)1);
            }
        }
        wordCounter++;
        return hm;

    }

    private static String findSubstring (String a, String b) {
        if (a == null || b == null || a.length() == 0 || b.length() == 0) {
            return null;
        }

        if (a.equals(b)) {
            return a;
        }

        int[][] matrix = new int[a.length()][];

        int maxLength = 0;
        int maxI = 0;
        int maxJ = 0;

        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new int[b.length()];
            for (int j = 0; j < matrix[i].length; j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    if (i != 0 && j != 0) {
                        matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    } else {
                        matrix[i][j] = 1;
                    }
                    if (matrix[i][j] > maxLength) {
                        maxLength = matrix[i][j];
                        maxI = i;
                        maxJ = j;
                    }
                }
            }
        }
        if (!(maxLength < WORD_SIZE || maxI - maxLength > LEFT_GAP - 1 || maxJ - maxLength > LEFT_GAP -1 || a.length() - (maxI+1) > RIGHT_GAP || b.length() - (maxJ+1) > RIGHT_GAP)) {
            return a.substring(maxI - maxLength + 1, maxI + 1);
        }
        else {
            return null;
        }
    }
}