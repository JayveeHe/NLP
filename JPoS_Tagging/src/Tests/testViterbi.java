package Tests;

import HMM.BasicModel.HMModel;
import HMM.Viterbi.ViterbiDecoder;

import java.util.Arrays;

/**
 * Created by Jayvee on 2014/11/24.
 */
public class testViterbi {
    public static void main(String[] a) {
        HMModel hmModel1 = new HMModel("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\1417431744986");
        HMModel hmModel2 = new HMModel("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\1417431690325");
//        HMModel hmModel1 = new HMModel(12,1000);
//        HMModel hmModel2 = new HMModel(12,100);
        int[] seq1 = {0, 1, 1, 7, 1,7, 2, 3, 4, 1, 1, 2, 2, 3, 4};
        int[] seq2 = {2, 1, 1, 3, 1, 2, 2, 1, 1, 3, 2, 5, 1, 2, 6};
//        int[] seq3 = {0, 4, 7, 8, 8, 5, 4, 6, 2, 2, 3, 3, 4, 1, 1};
        ViterbiDecoder vd1 = new ViterbiDecoder(hmModel1);
        ViterbiDecoder vd2 = new ViterbiDecoder(hmModel2);
        System.out.println("模型1：\n" + Arrays.toString(vd1.decode(seq1, true).getPath()));
        System.out.println(Arrays.toString(vd1.decode(seq2, true).getPath()));
//        System.out.println(Arrays.toString(vd1.decode(seq3, true).getPath()));
        System.out.println("模型2：\n" + Arrays.toString(vd2.decode(seq1, true).getPath()));
        System.out.println(Arrays.toString(vd2.decode(seq2, true).getPath()));
//        System.out.println(Arrays.toString(vd2.decode(seq3, true).getPath()));
    }
}
