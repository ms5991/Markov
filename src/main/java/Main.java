
public class Main 
{
    public static void main(String[] args) {
        int textLength = Integer.parseInt(args[0]);
        int order = Integer.parseInt(args[1]);
        int numFiles = Integer.parseInt(args[2]);

        String[] files = new String[numFiles];

        for (int i=0;i<numFiles;i++){
            files[i] = args[i + 3];
        }

        boolean dump = false;
        if (args.length == 3 + numFiles + 1) {
            dump = true;
        }

        MarkovChain chain = new MarkovChain(order, files);

        if (dump) {
            chain.dump();
        }

        System.out.println(chain.generate(textLength));
    }
}
