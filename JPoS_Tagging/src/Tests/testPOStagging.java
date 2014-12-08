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
        HMModel hmModel = new HMModel("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\hmmData-0");
        ViterbiDecoder vd = new ViterbiDecoder(hmModel);
        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        String source = "在协作分集系统中, 不同中继与目的端之间信号的时间延迟和频率偏移均不相同, 因而同时估计各中继与目的端之间的定时和频率偏移是协作分集系统同步中最关键、最具挑战性的部分之一.";
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
