package Tests;

import TrainSet.WordIndex;

/**
 * Created by Jayvee on 2014/11/24.
 */
public class testSeqIndex {
    public static void main(String a[]) {
        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        WordIndex.IndexResult result = wordIndex.Sentence2Index("我是一个来自北京的大学生。");
        String[] words = result.getWord();
        int[] seq = result.getIndex();
        for (int i = 0;i<seq.length;i++) {

            System.out.println(words[i] + "\\" + seq[i]);
        }
    }
}
