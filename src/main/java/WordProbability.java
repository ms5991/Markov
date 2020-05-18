import java.util.*;

public class WordProbability {
    private static Random rand = new Random();

    private int totalCount;

    Hashtable<String, Integer> countMappings;

    public WordProbability() {
        this.totalCount = 0;
        this.countMappings = new Hashtable<String, Integer>();
    }

    public void dump(String word) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] -> \n", word));

        Enumeration<String> keys = this.countMappings.keys();
        while(keys.hasMoreElements()) {
            String target = keys.nextElement();
            double prob = (double)this.countMappings.get(target) / this.totalCount;
            sb.append(String.format("\t[%s] = [%f]\n", target, prob));
        }

        System.out.print(sb.toString());
    }

    public void addTransition(String targetWord) {
        int count = this.countMappings.containsKey(targetWord) ? this.countMappings.get(targetWord) : 0;
        this.countMappings.put(targetWord, count + 1);
        this.totalCount++;
    }

    public String getNextWord() {
        int random = rand.nextInt(this.totalCount);

        int sum = 0;

        String result = null;

        Enumeration<String> keys = this.countMappings.keys();
        while(keys.hasMoreElements()) { 
            String word = keys.nextElement();

            int val = this.countMappings.get(word);

            sum += val;

            if (random < sum) {
                result = word;
                break;
            }
        }

        return result;
    }
}