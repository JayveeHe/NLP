package Tests;

import HMM.BasicModel.HMModel;
import Utils.SeqGenerator;
import HMM.Viterbi.ViterbiDecoder;

/**
 * Created by ITTC-Jayvee on 2014/11/21.
 */
public class testSeqGenerator {
    public static void main(String a[]) {
//        double[] probs = {0.05, 0.1, 0.2, 0.65};
//        if (!RandomUtils.isStochastic(probs)) {
//            return;
//        }
//        int[] count = new int[probs.length];
//        for (int i = 0; i < 100000; i++) {
//            int t = pickRandomIndex(probs);
//            count[t]++;
////            System.out.println(t);
//        }
//        for (int k = 0; k < probs.length; k++) {
//            System.out.println(count[k] / 100000f);
//        }
//        HMModel hmModel = new HMModel(500, 1000);

        HMModel hmModel = new HMModel("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\seqGenModel");
//        hmModel.saveModel("seqGenModel");
        SeqGenerator sg = new SeqGenerator(hmModel);
        int[] seq = sg.genHiddenSeq(10000, 0.7);
        int[] obseq = sg.genObSeq(seq, 1);
//        String txt = "原始序列：\n";
//        for (int i : seq) {
//            txt = txt + i + "\t";
////            System.out.println(i);
//        }
//        System.out.println(txt);
//        txt = "观察序列：\n";
//        for (int i : obseq) {
//            txt = txt + i + "\t";
////            System.out.println(i);
//        }
//        System.out.println(txt);
        ViterbiDecoder vd = new ViterbiDecoder(hmModel);
        ViterbiDecoder.DecodeResult decodeResult = vd.decode(obseq, true);
        int[] decodeSeq = decodeResult.getPath();
        int errCount = 0;
//        txt = "译码序列：\n";
//        for (int i = 0; i < decodeSeq.length; i++) {
//            if (decodeSeq[i] != seq[i]) {
//                errCount++;
//                txt = txt + "[" + decodeSeq[i] + "]" + "\t";
//            } else {
//                txt = txt + decodeSeq[i] + "\t";
////            System.out.println(i);
//            }
//        }
//        System.out.println(txt);
//        System.out.println("译码路径概率：" + decodeResult.getPathProb());
        //对比原始序列

//        for (int i = 0; i < decodeSeq.length; i++) {
//            if (int i = 0; i < decodeSeq.length; i++) {
//                errCount++;
//            }
//        }
        System.out.println("译码错误率：" + (double) errCount / decodeSeq.length + "(" + errCount + "/" + decodeSeq.length + ")");

//        ForwardBackwardAlog fba = new ForwardBackwardAlog(hmModel);
//        for (int i = 0; i < 50; i++) {
//            fba.TrainBySingleObseq(obseq);
//        }
//        vd = new ViterbiDecoder(hmModel);
//        decodeResult = vd.decode(obseq, true);
//        decodeSeq = decodeResult.getPath();
//        System.out.println("译码可信度：" + decodeResult.getPathProb());

        //对比原始序列
//        errCount = 0;
//        for (int i = 0; i < decodeSeq.length; i++) {
//            if (decodeSeq[i] != seq[i]) {
//                errCount++;
//            }
//        }
//        System.out.println((double) errCount / decodeSeq.length);
    }


}
