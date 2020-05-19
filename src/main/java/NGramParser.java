import java.io.BufferedReader;
import java.io.IOException;

public class NGramParser
{
    // differentiate from EOF (-1)
    private static final int EMPTY_NEXT_CHAR = -2;

    // the "N" in ngram
    private int order;

    // reader pointing to the file
    private BufferedReader reader;

    // the current Ngram, where each
    // element is a token
    private String[] currentNGram;

    // the current token that follows
    // the current Ngram in the input
    private String currentToken;

    // indicates whether th eend of the file
    // has been found
    private boolean endOfFileFound;

    // stores the nextChar from the input
    // if the parser encounters a situation
    // where it speculatively reads one
    // too many chars from the file
    private int nextChar;

    public NGramParser(int order, BufferedReader reader) throws IOException {
        this.order = order;
        this.reader = reader;
        this.nextChar = 0;
        this.endOfFileFound = false;
        this.currentNGram = new String[this.order];

        this.Init();
    }

    // initializes this parser by populating 
    // the currentNGram from the file
    private void Init() throws IOException {
        // advance the parser this.order -1 times
        // to populate the entire ngram array except the last
        // element. It is expected that the caller must call
        // advance() before reading the first token
        for (int i = 0; i < this.order - 1; i++) {
            this.advance();
        }
    }

    // advances the parser by getting the next
    // word from the BufferedReader
    public void advance() throws IOException {

        // if the end of the file has already been found,
        // return directly without doing anything
        if (this.endOfFileFound) {
            return;
        }

        // advance the ngram first such that
        // the values of the ngram shift forward
        // in the array and the current currentToken
        // is added at the back of the array
        for (int i=0;i<this.order;i++) {
            // shift all of the elements in 
            // the array to the front
            if (i < this.order - 1){
                this.currentNGram[i] = this.currentNGram[i + 1];
            } else {
                // on the last iteration, set the last
                // entry equal to the current token and
                // set the current token to null
                this.currentNGram[i] = currentToken;
                currentToken = null;
            }
        }

        try {
            boolean shouldContinue = true;
            StringBuilder sb = new StringBuilder();

            // if nextChar is not empty, it means
            // there is a char that needs to be appended first
            if (this.nextChar != EMPTY_NEXT_CHAR) {
                sb.append((char)this.nextChar);
                this.nextChar = EMPTY_NEXT_CHAR;
            }

            while (shouldContinue) {
                // read the next character from the input
                int val = this.reader.read();

                // cast to char
                char ch = (char)val;

                // end of file found, stop processing
                if (val == -1) {
                    this.endOfFileFound = true;
                    shouldContinue = false;
                // eliminate all whitespace
                } else if (Character.isWhitespace(ch)) {
                    // iterate until a non-whitespace character
                    // or EOF is found
                    while (shouldContinue) {
                        // read the next char to determine if it
                        // is whitespace or not
                        val =this.reader.read();
                        ch = (char)val;

                        // end of file
                        if (val == -1) {
                            this.endOfFileFound = true;
                            shouldContinue = false;
                        // the first non-whitespace character
                        // is saved for the next call to advance()
                        } else if (!Character.isWhitespace(ch)) {
                            this.nextChar = ch;
                            shouldContinue = false;
                        }
                    }
                // non-letter/digit is a symbol, which
                // should be treated as its own token.
                // The apostrophe token is slightly different
                // and is therefore treated as a part of
                // the word it appears with
                } else if (!Character.isLetterOrDigit(ch) && ch != '\'') {
                    this.nextChar = ch;
                    shouldContinue = false;
                // digit or letter
                } else {
                    // most chars get appended to the result directly
                    sb.append(ch);
                }
            }

            // set the current token
            this.currentToken = sb.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // gets the current NGram
    public String[] getCurrentNGram() {
        return this.currentNGram;
    }

    // gets the current token
    public String getCurrentToken() {
        return this.currentToken;
    }

    // Indicates whether the parser has 
    // encountered the end of the file
    public boolean getEndOfFileFound() {
        return this.endOfFileFound;
    }
}