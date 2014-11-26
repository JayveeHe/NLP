package Tests;

import HMM.Utils.FileUtils;
import TrainSet.DataStructure.IndexResult;
import TrainSet.WordIndex;

/**
 * Created by Jayvee on 2014/11/24.
 */
public class testSeqIndex {
    public static void main(String a[]) {
        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        String text = FileUtils.File2str("D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile\\IT\\1000.txt", "gbk");
        IndexResult result = wordIndex.Sentence2Index(text);
        String[] words = result.getWord();
        int[] seq = result.getIndex();
        for (int i = 0; i < seq.length; i++) {
            System.out.println(words[i] + "\\" + seq[i]);
        }
    }
}
