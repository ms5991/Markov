import java.io.BufferedReader;
import java.io.IOException;

public class NGramParser
{
    private int order;

    private BufferedReader reader;

    private String[] currentNGram;

    private String nextToken;

    private boolean endOfFileFound;

    private char nextChar;

    public NGramParser(int order, BufferedReader reader) throws IOException {
        this.order = order;
        this.reader = reader;
        this.nextChar = 0;
        this.endOfFileFound = false;
        this.currentNGram = new String[this.order];

        this.Init();
    }

    private void Init() throws IOException {
        boolean init = false;

        while (!init) {
            this.advance();
            for (int i = 0; i < this.order; i++) {
                if (this.currentNGram[i] == null) {
                    break;
                }
            }
            init = true;
        }
    }

    public void advance() throws IOException {
        // advance the ngram first such that
        // the values of the ngram shift forward
        // in the array and the current nextToken
        // is added at the back of the array
        for (int i=0;i<this.order;i++) {
            if (i < this.order - 1){
                this.currentNGram[i] = this.currentNGram[i + 1];
            } else {
                this.currentNGram[i] = nextToken;
                nextToken = null;
            }
        }

        try {
            boolean shouldContinue = true;

            StringBuilder sb = new StringBuilder();

            if (this.nextChar != 0) {
                sb.append(this.nextChar);
                this.nextChar = 0;
            }

            int val = 0;
            while (shouldContinue) {

                // read the next character from the input
                val = this.reader.read();
                char ch = (char)val;
                // end of file found
                if (val == -1) {
                    this.endOfFileFound = true;
                    shouldContinue = false;
                // eliminate all whitespace
                } else if (Character.isWhitespace(ch)) {
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
                } else if (!Character.isLetterOrDigit(ch) && ch != '\'') {
                    this.nextChar = ch;
                    shouldContinue = false;
                // digit or letter
                } else {
                    sb.append(ch);
                }
            }

            this.nextToken = sb.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String[] getCurrentNGram() {
        return this.currentNGram;
    }

    public String getNextToken() {
        return this.nextToken;
    }

    public boolean getEndOfFileFound() {
        return this.endOfFileFound;
    }
}