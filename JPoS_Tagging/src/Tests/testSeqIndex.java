package Tests;

import Utils.FileUtils;
import TrainSet.DataStructure.IndexResult;
import TrainSet.WordIndex;

/**
 * Created by Jayvee on 2014/11/24.
 */
public class testSeqIndex {
    public static void main(String a[]) {
        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
//        String text = FileUtils.File2str("D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile\\IT\\1000.txt", "gbk");
        String text = "每个人的记忆就像飘浮在宇宙中的一颗颗星球，相互独立，却又彼此存在。";
        IndexResult result = wordIndex.Sentence2Index(text);
        String[] words = result.getWord();
        int[] seq = result.getIndex();
        String out = "";
        for (int i = 0; i < seq.length; i++) {
            out = out +words[i] + "\\" + seq[i]+" ";
        }
        System.out.println(out);
    }
}
