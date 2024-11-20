import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
}

class Trie {
    TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return false;
            }
        }
        return node.isEndOfWord;
    }
}

class EditDoc implements Cloneable {
    private String[] line;

    public EditDoc(int lineCount, String sourceFile) {
        line = new String[lineCount];
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            int i = 0;
            String currenrLine;
            while ((currenrLine = br.readLine()) != null) {
                line[i] = currenrLine;
                ++i;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeNonEngAlphabet() {
        for (int i = 0; i < line.length; ++i) {
            // replace all non-alphabet characters and replace them with whitespaces
            line[i] = line[i].replaceAll("[^a-zA-Z]", " ").toLowerCase().trim().replaceAll("\\s+", " ");

            // line[i] = line[i].toLowerCase();
            // line[i] = line[i].replaceAll("\\s+", " ");
            // line[i] = line[i].replaceFirst(" ", "");
        }

    }

    public String[] clone() {
        String[] line = this.line.clone();
        return line;
    }
}

class dataAnalysis {

    public double tf(HashMap<String, Integer> doc, String term) {
        // long tf1 = System.currentTimeMillis();
        int wordCount = 0;
        for (Integer t : doc.values()) {
            wordCount += t;
        }

        // long tf2 = System.currentTimeMillis();
        // System.out.println("TF: " + (tf2 - tf1));

        int ftd = doc.getOrDefault(term, 0);
        // int wordCount = doc.values().stream().mapToInt(Integer::intValue).sum();

        return ((double) ftd / (double) wordCount);
    }

    public double idf(HashMap<String, Integer> occurTimes, String term, int docsNum) {
        // long idf1 = System.currentTimeMillis();
        int number_doc_contain_term = occurTimes.get(term);

        return Math.log((double) docsNum / (double) number_doc_contain_term);
    }

    public double tfIdfCalculate(HashMap<String, Integer> doc, HashMap<String, Integer> occurTimes, String term,
            int docsNum) {
        return tf(doc, term) * idf(occurTimes, term, docsNum);
    }

    // public double idf(HashMap<Integer, HashMap<String, Integer>> docs, String
    // term) {
    // int number_doc_contain_term = 0;
    // for (int i = 0; i < docs.size(); i++) {
    // if (docs.get(i).containsKey(term)) {
    // number_doc_contain_term++;
    // }
    // }
    // return Math.log((double) docs.size() / (double) number_doc_contain_term);
    // }

    // public double tfIdfCalculate(HashMap<Integer, HashMap<String, Integer>> docs,
    // HashMap<String, Integer> doc,
    // String term) {
    // return tf(doc, term) * idf(docs, term);
    // }

}

public class TFIDFCalculator {
    public static void main(String[] args) {
        long sTime = System.currentTimeMillis();
        int lineCount = 0;
        int rootNumber = 0;

        // read how many lines are in the doc
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {

            while (br.readLine() != null)
                lineCount++;
            rootNumber = lineCount / 5;
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long time1 = System.currentTimeMillis();
        // refine the document
        EditDoc ed = new EditDoc(lineCount, args[0]);
        ed.removeNonEngAlphabet();
        String[] line = ed.clone();

        // build up the Trie and Hashmap
        HashMap<Integer, HashMap<String, Integer>> docs = new HashMap<Integer, HashMap<String, Integer>>();
        HashMap<String, Integer> occurTimes = new HashMap<String, Integer>();
        Trie[] t = new Trie[rootNumber];
        for (int i = 0; i < rootNumber; ++i) {
            HashMap<String, Integer> doc = new HashMap<String, Integer>();
            t[i] = new Trie();
            for (int j = 0; j < 5; j++) {
                Scanner sc = new Scanner(line[i * 5 + j]);
                sc.useDelimiter(" ");
                while (sc.hasNext()) {
                    String vocab = sc.next();
                    t[i].insert(vocab);
                    if (doc.containsKey(vocab)) {
                        int repeat = doc.get(vocab) + 1;
                        doc.remove(vocab);
                        doc.put(vocab, repeat);
                    } else {
                        doc.put(vocab, 1);
                        // count how many docs contain the specific words
                        // add 1 only if it is the first its appearence in the doc
                        if (occurTimes.containsKey(vocab)) {
                            int occur = occurTimes.get(vocab);
                            occurTimes.remove(vocab);
                            occurTimes.put(vocab, occur + 1);
                        } else {
                            occurTimes.put(vocab, 1);
                        }

                    }
                }
            }
            docs.put(i, doc);
        }
        long time2 = System.currentTimeMillis();
        System.out.println("Build: " + (time2 - time1) / 1000);

        long time3 = System.currentTimeMillis();
        // read input
        int numOfTarget = 0;
        String[] targetVocab = null;
        int[] targetDoc = null;
        try (BufferedReader bf = new BufferedReader(new FileReader(args[1]))) {
            String currentLine = bf.readLine();
            while (numOfTarget == 0) {
                String firstLine = currentLine;
                targetVocab = firstLine.split(" ");
                String secondLine = bf.readLine();
                String[] targetDocStr = secondLine.split(" ");
                numOfTarget = targetDocStr.length;
                targetDoc = new int[numOfTarget];
                for (int i = 0; i < targetDocStr.length; ++i) {
                    targetDoc[i] = Integer.parseInt(targetDocStr[i]);
                }
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long time4 = System.currentTimeMillis();
        System.out.println("read input: " + (time4 - time3) / 1000);

        long time5 = System.currentTimeMillis();

        // calculate tf and idf
        dataAnalysis da = new dataAnalysis();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            String tfidfString = new String();
            for (int i = 0; i < numOfTarget; ++i) {
                // long tfidf1 = System.currentTimeMillis();

                if (t[targetDoc[i]].search(targetVocab[i])) {
                    double tfidf = da.tfIdfCalculate(docs.get(targetDoc[i]), occurTimes, targetVocab[i], t.length);
                    // double tfidf = da.tfIdfCalculate(docs, docs.get(targetDoc[i]),
                    // targetVocab[i]);

                    // tfidfString += da.tfIdfCalculate(docs.get(targetDoc[i]), t, targetVocab[i]);
                    // tfidfString += (String.format("%.5f", tfidf) + " ");
                    bw.write(String.format("%.5f", tfidf) + " ");
                    // System.out.println(tfidf);
                } else {
                    // double tfidf = 0.d;
                    // tfidfString += ("0.00000 ");
                    bw.write(String.format("%.5f", 0) + " ");
                    // System.out.println(tfidf);
                }
                // long tfidf2 = System.currentTimeMillis();
                // System.out.println("tfidf(ms): " + (tfidf2 - tfidf1));
            }
            bw.write(tfidfString);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long eTime = System.currentTimeMillis();
        System.out.println("TFIDF: " + (eTime - time5) / 1000);
        System.out.println("Duration: " + (eTime - sTime) / 1000);

    }
}