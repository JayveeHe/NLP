package HMM;

import HMM.BasicModel.ForwardVector;
import HMM.BasicModel.GammaVector;
import HMM.BasicModel.HMModel;
import HMM.BasicModel.SigmaVector;
import HMM.Baum_Welch.ForwardBackwardAlog;
import HMM.Viterbi.ViterbiDecoder;
import javafx.scene.text.FontWeight;

/**
 * Created by Jayvee on 2014/11/12.
 */
public class testHMM {
    public static void main(String[] a) {


        int[] testSeq = new int[]{1, 1,7, 6, 1,1,0,2,1,1,3};
        double[][] aMatrix = {{0.5, 0.25, 0.25}, {0.6, 0.1, 0.3}, {0.3, 0.5, 0.2}};
        double[][] bMatrix = {{0.5, 0.5}, {0.2, 0.8}, {0.4, 0.6}};
        double[] piVector = {0.5, 0.4, 0.1};

//        HMModel hmModel = new HMModel(3, 2, aMatrix, bMatrix, piVector);
        HMModel hmModel = new HMModel(10,100);
        System.out.println(hmModel);
        ForwardVector forwardVector = new ForwardVector(hmModel);
        double obSeqProb = forwardVector.calObSeqProb(testSeq);
        System.out.println(obSeqProb);
        ForwardBackwardAlog fba = new ForwardBackwardAlog(hmModel);
        for (int i = 0; i < 100; i++) {
            fba.TrainBySingleObseq(testSeq);
            System.out.println(hmModel);
            forwardVector = new ForwardVector(hmModel);
            ViterbiDecoder vd = new ViterbiDecoder(hmModel);
            ViterbiDecoder.DecodeResult decodeResult = vd.decode(testSeq);
            int[] tt = decodeResult.getPath();
            String text = "译码序列：";
            for (int k : tt) {
                text = text + "\t" + k;
            }
            text = text + "概率：" + decodeResult.getPathProb();
            System.out.println(text);
            obSeqProb = forwardVector.calObSeqProb(testSeq);
            System.out.println(obSeqProb);
        }
        System.out.println(hmModel);

//        SigmaVector sigmaVector = new SigmaVector(hmModel, testSeq);
//        GammaVector gammaVector = new GammaVector(testSeq, hmModel);
//        double sum = 0;
//        for (int j = 0; j < hmModel.getN(); j++) {
//            sum += sigmaVector.calSigma(2, 1, j);
//        }
//        System.out.println(sum);
//        System.out.println(gammaVector.calGammaVector(2, 1));
    }
}
