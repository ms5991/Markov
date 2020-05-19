
public class Main 
{
    private static final String ERROR_STR = "Usage: \"Java Main [dump|generate] [number of words to generate] [nGram order] [number of input files] [input file path 0] ... [input file path N]\"";
    
    private static final String MODE_DUMP = "dump";
    private static final String MODE_GENERATE = "generate";

    public static void main(String[] args) {

        if (args.length < 5){
            System.err.println(ERROR_STR);
            System.exit(-1);
        }

        String mode = args[0];

        // make sure mode is valid
        if (!mode.equalsIgnoreCase(MODE_DUMP) && !mode.equalsIgnoreCase(MODE_GENERATE)) {
            System.err.println(ERROR_STR);
            System.exit(-1);
        }

        int textLength = 0;
        int order = 0;
        int numFiles = 0;

        // parse the first information from the args
        try {
            textLength = Integer.parseInt(args[1]);
            order = Integer.parseInt(args[2]);
            numFiles = Integer.parseInt(args[3]);
        } catch (Exception e) {
            System.err.println(ERROR_STR);
            System.exit(-1);
        }

        // numFiles does not match the actual number of input paths
        if (args.length != 4 + numFiles) {
            System.err.println(ERROR_STR);
            System.exit(-1);
        }

        // create an array of size numFiles
        String[] files = new String[numFiles];

        for (int i=0;i<numFiles;i++){
            files[i] = args[i + 4];
        }

        // create the Markov chain
        MarkovChain chain = new MarkovChain(order, files);

        if (mode.equals(MODE_DUMP)) {
            // dump
            chain.dump();
        } else {
            // generate the text
            System.out.println(chain.generate(textLength));
        }

        System.exit(0);
    }
}
