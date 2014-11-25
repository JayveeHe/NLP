package Test;

import TrainSet.WordIndex;

/**
 * Created by Jayvee on 2014/11/24.
 */
public class testSeqIndex {
    public static void main(String a[]) {
        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        int[] seq = wordIndex.Sentence2Index("我是一个大学生");
        for (int i : seq) {
            System.out.println(i);
        }
    }
}
