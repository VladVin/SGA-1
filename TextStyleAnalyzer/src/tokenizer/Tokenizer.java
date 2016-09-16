package tokenizer;

import java.util.ArrayList;
import java.util.HashMap;

//tokenizer
public class Tokenizer {
    public static HashMap<String, Float> tokenize (String doc){

        HashMap <String, Float>  hm = new HashMap<>();
        ArrayList<String> keys= new ArrayList<>();

        String[] parts = doc.split("(\\P{L})+");
        float portion;
        float value;
        String key;
        for (int i = 0; i < parts.length; i++){
            if (parts[i].length()>=3){
                for (HashMap.Entry<String, Float> entry :hm.entrySet()) {
                    key = findSubstring(entry.getKey(), parts[i]);
                    if (key != null) {
                        keys.add(entry.getKey());
                    }
                }

                if (!keys.isEmpty()) {
                    portion = 1 / (float)keys.size();

                    while (keys.size() > 0) {

                        key = findSubstring(parts[i], keys.get(0));
                        value = hm.get(keys.get(0)) + portion;
                        if (hm.containsKey(key) && (!key.equals(keys.get(0)))) {
                            value += hm.get(key);
                        }
                        if (!key.equals(keys.get(0))){
                            hm.remove(keys.get(0));
                        }
                        hm.put(key, value);
                        keys.remove(0);
                    }
                } else {
                    hm.put(parts[i].toLowerCase(), (float)1);
                }

            }

        }

        return hm;
    }

    private static String findSubstring (String a, String b) {
        if (a == null || b == null || a.length() == 0 || b.length() == 0) {
            return null;
        }
        a = a.toLowerCase();
        b = b.toLowerCase();

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
        if (!(maxLength < 3 || maxI - maxLength > 2 || maxJ - maxLength > 2 || a.length() - (maxI+1) > 3 || b.length() - (maxJ+1) > 3)) {
            return a.substring(maxI - maxLength + 1, maxI + 1);
        }
        else {
            return null;
        }
    }
}