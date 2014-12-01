package Tests;

import HMM.BasicModel.HMModel;
import HMM.Viterbi.ViterbiDecoder;
import TrainSet.DataStructure.IndexResult;
import TrainSet.WordIndex;

import java.util.Arrays;

/**
 * Created by Jayvee on 2014/11/26.
 */
public class testPOStagging {
    public static void main(String a[]) {
        HMModel hmModel = new HMModel("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\hmmData");
        ViterbiDecoder vd = new ViterbiDecoder(hmModel);
        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        String source = "我现在以为，人与人之间的关系，最重要的不是怎么跟人打交道，而是跟自己打交道。";
        int[] seq;
        IndexResult indexResult = wordIndex.Sentence2Index(source);
        seq = indexResult.getIndex();
//        int[] result = vd.decode(seq, true).getPath();
        int[] path = vd.decode(seq, true).getPath();
        for (int i = 0; i < seq.length; i++) {
            System.out.println(indexResult.getWord()[i] + "\t" + seq[i]+"\t"+path[i]);}
//        System.out.println(Arrays.toString(vd.decode(seq, true).getPath()));
    }
}
