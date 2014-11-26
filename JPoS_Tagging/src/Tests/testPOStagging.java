package Tests;

import HMM.BasicModel.HMModel;
import HMM.Viterbi.ViterbiDecoder;
import TrainSet.WordIndex;

import java.util.Arrays;

/**
 * Created by Jayvee on 2014/11/26.
 */
public class testPOStagging {
    public static void main(String a[]){
        HMModel hmModel = new HMModel("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\hmmData");
        ViterbiDecoder vd = new ViterbiDecoder(hmModel);
        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        String source = "我是一个来自北京的大学生。";
        int[] seq = wordIndex.Sentence2Index(source).getIndex();
//        int[] result = vd.decode(seq, true).getPath();
        System.out.println(Arrays.toString(vd.decode(seq, true).getPath()));
    }
}
