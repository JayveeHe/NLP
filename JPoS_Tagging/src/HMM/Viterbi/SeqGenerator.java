package HMM.Viterbi;

import HMM.BasicModel.BasicUtils;
import HMM.BasicModel.HMModel;
import HMM.Baum_Welch.ForwardBackwardAlog;

/**
 * Created by Jayvee on 2014/11/19.
 */
public class SeqGenerator {
    HMModel hmModel;

    public SeqGenerator(HMModel hmModel) {
        this.hmModel = hmModel;
    }

    /**
     * 根据已有的模型随机生成n长的状态序列
     *
     * @param n
     * @param noiseRate 噪声概率
     * @return
     */
    public int[] genHiddenSeq(int n, double noiseRate) {
        double[][] aMatrix = hmModel.getAMatrix();
        int[] result = new int[n];
        int randCount = 0;
        if (Math.random() > noiseRate) {
            result[0] = pickMaxIndex(aMatrix[0]);
        } else {
            result[0] = pickRandomIndex(aMatrix[0]);
            randCount++;
        }

        for (int i = 1; i < n; i++) {
            if (Math.random() > noiseRate) {
                result[i] = pickMaxIndex(aMatrix[result[i - 1]]);
            } else {
                result[i] = pickRandomIndex(aMatrix[result[i - 1]]);
                randCount++;
            }
        }
        double temp = (double) randCount / n;
        System.out.println("生成随机概率:" + temp);
//        result[0] = pickRandomIndex(aMatrix[0]);
//        for (int i = 1; i < n; i++) {
//            result[i] = pickRandomIndex(aMatrix[result[i - 1]]);
//        }
        return result;
    }

    /**
     * 根据已有模型和状态序列随机生成符号序列
     *
     * @param hiddenSeq
     * @param noiseRate
     * @return
     */
    public int[] genObSeq(int[] hiddenSeq, double noiseRate) {
        double[][] bMatrix = hmModel.getBMatrix();
        int[] result = new int[hiddenSeq.length];
        int randCount = 0;
        if (Math.random() > noiseRate) {
            result[0] = pickMaxIndex(bMatrix[hiddenSeq[0]]);
        } else {
            result[0] = pickRandomIndex(bMatrix[hiddenSeq[0]]);
            randCount++;
        }

        for (int i = 1; i < hiddenSeq.length; i++) {
            if (Math.random() > noiseRate) {
                result[i] = pickMaxIndex(bMatrix[hiddenSeq[i]]);
            } else {
                result[i] = pickRandomIndex(bMatrix[hiddenSeq[i]]);
                randCount++;
            }
        }
        double temp = (double) randCount / hiddenSeq.length;
        System.out.println("生成随机概率:" + temp);
        return result;
    }

    /**
     * 根据概率数组随机选取一个序号
     *
     * @param probs 概率数组
     * @return 当概率数组符合概率约束时，返回所选序号；否则返回-1
     */
    public static int pickRandomIndex(double[] probs) {
        if (!BasicUtils.isStochastic(probs)) {
            return -1;
        }
        double prob = Math.random();
        int i = 0;
        for (i = 0; i < probs.length; i++) {
            prob -= probs[i];
            if (prob <= 0) break;
        }
        return i;
    }

    /**
     * 根据概率数组选取最大的概率序号
     *
     * @param probs 概率数组
     * @return 当概率数组符合概率约束时，返回所选序号；否则返回-1
     */
    public static int pickMaxIndex(double[] probs) {
        if (!BasicUtils.isStochastic(probs)) {
            return -1;
        }
        int i = 0;
        int result = 0;
        double max = 0;
        for (i = 0; i < probs.length; i++) {
            if (probs[i] > max) {
                max = probs[i];
                result = i;
            }
        }
        return result;
    }

    public static void main(String a[]) {
//        double[] probs = {0.05, 0.1, 0.2, 0.65};
//        if (!BasicUtils.isStochastic(probs)) {
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
        HMModel hmModel = new HMModel(10, 100);
        SeqGenerator sg = new SeqGenerator(hmModel);
        int[] seq = sg.genHiddenSeq(10, 0.4);
        int[] obseq = sg.genObSeq(seq, 0.3);
        String txt = "原始序列：\n";
        for (int i : seq) {
            txt = txt + i + "\t";
//            System.out.println(i);
        }
        System.out.println(txt);
        txt = "观察序列：\n";
        for (int i : obseq) {
            txt = txt + i + "\t";
//            System.out.println(i);
        }
        System.out.println(txt);
        ViterbiDecoder vd = new ViterbiDecoder(hmModel);
        ViterbiDecoder.DecodeResult decodeResult = vd.decode(obseq, true);
        int[] decodeSeq = decodeResult.getPath();
        txt = "译码序列：\n";
        for (int i : decodeSeq) {
            txt = txt + i + "\t";
//            System.out.println(i);
        }
        System.out.println(txt);
        System.out.println("译码路径概率：" + decodeResult.getPathProb());
        //对比原始序列
        int errCount = 0;
        for (int i = 0; i < decodeSeq.length; i++) {
            if (decodeSeq[i] != seq[i]) {
                errCount++;
            }
        }
        System.out.println(Math.log((double) errCount / decodeSeq.length));

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
