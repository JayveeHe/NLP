package Tests;

import HMM.BasicModel.HMModel;
import HMM.Viterbi.ViterbiDecoder;
import Utils.SeqGenerator;

import java.util.Arrays;

/**
 * Created by Jayvee on 2014/12/2.
 */
public class testErrorRate {
    public static void main(String tt[]) {
        HMModel hmModel = new HMModel("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\seqGenModel");
//        hmModel.saveModel("seqGenModel");
        SeqGenerator sg = new SeqGenerator(hmModel);
        ViterbiDecoder vd = new ViterbiDecoder(hmModel);
        double[][] err = new double[10][10];
        for (double a = 0f; a < 1; a += 0.1f) {
            for (double b = 0f; b < 1; b += 0.1f) {
                double errRate = calErrRate(sg, vd, a, b);
                err[((int) (a * 10))][((int) (b * 10))] = errRate;
            }
        }
        System.out.println(Arrays.deepToString(err));
    }

    public static double calErrRate(SeqGenerator sg, ViterbiDecoder vd, double a, double b) {
        int[] seq = sg.genHiddenSeq(1000, a);
        int[] obseq = sg.genObSeq(seq, b);
        ViterbiDecoder.DecodeResult decodeResult = vd.decode(obseq, true);
        int[] decodeSeq = decodeResult.getPath();
        int errCount = 0;
        System.out.println("译码错误率：" + (double) errCount / decodeSeq.length + "(" + errCount + "/" + decodeSeq.length + ")");
        return (double) errCount / decodeSeq.length;
    }
}
