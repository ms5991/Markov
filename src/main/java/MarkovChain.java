import java.util.*;
import java.io.*;

public class MarkovChain 
{
    private Hashtable<String, WordProbability> words;
    private int order;

    private static Random rand = new Random();

    public MarkovChain(int order, String[] files) {
        this.order = order;
        this.regenerateProbabilities(files);
    }

    // helper to print the probabilities
    public void dump() {
        Object[] keys = this.words.keySet().toArray();

        for (int i = 0;i<keys.length;i++) {
            this.words.get((String)keys[i]).dump((String)keys[i]);
        }
    }

    // relearns based on the input files
    public void regenerateProbabilities(String[] files) {
        this.words = new Hashtable<String, WordProbability>();

        int numFiles = files.length;

        for (int i = 0; i < numFiles; i++) {
            this.loadFile(files[i]);
        }
    }

    // Generates text based on the learned probabilities
    public String generate(int numWords) {
        // pick a random starting ngram
        String ngram = (String)this.words.keySet().toArray()[rand.nextInt(this.words.keySet().size())];

        StringBuilder strBuilder = new StringBuilder(); 

        // add the first ngram to the result
        strBuilder.append(ngram);

        // generate numwords
        for (int i = this.order - 1; i < numWords; i++) {
            // get the probabilty information for the current
            // ngram
            WordProbability prob = this.words.get(ngram);

            // get the word to add
            String nextWord = prob.getNextWord();

            try{
                // add a space only if the next token is not
                // a symbol
                if (nextWord.length() > 1 || Character.isLetterOrDigit(nextWord.charAt(0))) {
                    strBuilder.append(" ");
                }
            } catch (Throwable e) {
                System.out.printf("NextWord = [%s]\n", nextWord);
                throw e;
            }

            // add the word
            strBuilder.append(nextWord);

            // build the next ngram
            String[] splitNgram = ngram.split(" ");
            
            for (int j=0;j<this.order -1;j++) {
                splitNgram[j] = splitNgram[j+1];
            }
            splitNgram[this.order - 1] = nextWord;

            ngram = String.join(" ", splitNgram);
        }

        return strBuilder.toString();
    }

    // teaches the Markov chain using the text in the 
    // given file
    private void loadFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {

            // create an NGramParser using the buffered reader to the input fule
            NGramParser parser = new NGramParser(this.order, br);

            while (!parser.getEndOfFileFound()) {
                // advance the parser
                parser.advance();

                // get the current ngram and token
                String[] nGram = parser.getCurrentNGram();
                String nextWord = parser.getCurrentToken();

                // the key in the hashtable will be
                // the ngram tokens joined by a space. This is
                // safe since no space will be included in any
                // of the tokens
                String nGramKey = String.join(" ", nGram);

                // get the existing probability for the key
                // or create a new one if it doesn't exist
                WordProbability prob = this.words.containsKey(nGramKey) ? this.words.get(nGramKey) : new WordProbability();

                // add the transition from this ngram to 
                // the next token
                prob.addTransition(nextWord);

                // update the ngram in the hashtable
                this.words.put(nGramKey, prob);
            }

        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}