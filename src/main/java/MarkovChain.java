import java.util.*;
import java.io.*;

public class MarkovChain 
{
    private Hashtable<String, WordProbability> words;
    private int order;

    private static Random rand = new Random();

    public void regenerateProbabilities(String[] files) {
        this.words = new Hashtable<String, WordProbability>();

        int numFiles = files.length;

        for (int i = 0; i < numFiles; i++) {
            this.loadFile(files[i]);
        }
    }

    public MarkovChain(int order, String[] files) {
        this.order = order;
        this.regenerateProbabilities(files);
    }

    public void dump() {
        Object[] keys = this.words.keySet().toArray();

        for (int i = 0;i<keys.length;i++) {
            this.words.get((String)keys[i]).dump((String)keys[i]);
        }
    }

    public String generate(int numWords) {
        String ngram = (String)this.words.keySet().toArray()[rand.nextInt(this.words.keySet().size())];

        StringBuilder strBuilder = new StringBuilder(); 

        strBuilder.append(ngram);

        for (int i=this.order - 1;i<numWords;i++) {
            WordProbability prob = this.words.get(ngram);

            String nextWord = prob.getNextWord();

            if (nextWord.length() > 1 || Character.isLetterOrDigit(nextWord.charAt(0))) {
                strBuilder.append(" ");
            }

            strBuilder.append(nextWord);

            String[] splitNgram = ngram.split(" ");
            
            for (int j=0;j<this.order -1;j++) {
                splitNgram[j] = splitNgram[j+1];
            }
            splitNgram[this.order - 1] = nextWord;

            ngram = String.join(" ", splitNgram);
        }

        return strBuilder.toString();
    }

    private void loadFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
            NGramParser parser = new NGramParser(this.order, br);

            while (!parser.getEndOfFileFound()) {

                parser.advance();

                if (!parser.getEndOfFileFound()) {
                    String[] nGram = parser.getCurrentNGram();
                    String nextWord = parser.getNextToken();

                    String nGramKey = String.join(" ", nGram);

                    WordProbability prob = this.words.containsKey(nGramKey) ? this.words.get(nGramKey) : new WordProbability();

                    prob.addTransition(nextWord);

                    this.words.put(nGramKey, prob);
                }
            }

        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}