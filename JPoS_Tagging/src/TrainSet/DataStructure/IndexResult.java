package TrainSet.DataStructure;

/**
 * Created by Jayvee on 2014/11/26.
 */
public class IndexResult {
    private String[] word;
    private int[] index;

    public IndexResult(String[] word, int[] index) {
        this.word = word;
        this.index = index;
    }

    public String[] getWord() {
        return word;
    }

    public int[] getIndex() {
        return index;
    }
}