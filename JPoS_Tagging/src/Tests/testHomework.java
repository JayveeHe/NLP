package Tests;

import HMM.BasicModel.BackwardVector;
import HMM.BasicModel.ForwardVector;
import HMM.BasicModel.HMModel;
import HMM.BasicModel.SigmaVector;
import HMM.Viterbi.ViterbiDecoder;

import java.util.Arrays;

/**
 * Created by Jayvee on 2014/12/5.
 */
public class testHomework {
    public static void main(String a[]) {
        //例题数据
        double[][] aMatrix = {{0.5, 0.2, 0.3}, {0.3, 0.5, 0.2}, {0.2, 0.3, 0.5}};
        double[][] bMatrix = {{0.5, 0.5}, {0.4, 0.6}, {0.7, 0.3}};
        double[] piVector = {0.2, 0.4, 0.4};
        int[] testSeq = new int[]{0, 1, 0};
        HMModel hmModel = new HMModel(3,2,aMatrix,bMatrix,piVector);
//        HMModel hmModel = new HMModel(3, 2, aMatrix, bMatrix, piVector);
        ViterbiDecoder vd = new ViterbiDecoder(hmModel);
        ViterbiDecoder.DecodeResult decodeResult = vd.decode(testSeq, false);
        SigmaVector sigmaVector = new SigmaVector(hmModel, testSeq, false);
//        System.out.println("gamma=" + sigmaVector.calGammaVector(3, 2));
//        System.out.println(Math.pow(Math.E,decodeResult.getPathProb()));
        BackwardVector backwardVector = new BackwardVector(hmModel);
        double[][] backwardMatrix = backwardVector.calBackwardMatrix(testSeq, false);
        System.out.println(Arrays.deepToString(backwardMatrix));
        ForwardVector forwardVector = new ForwardVector(hmModel);
        double obSeqProb = forwardVector.calObSeqProb(testSeq, false);
        System.out.println("obSeqProb="+obSeqProb);
        System.out.println("obProb=" + Math.pow(Math.E,forwardVector.calObSeqProb(testSeq,true)));
        System.out.println("路径概率=" + decodeResult.getPathProb());
        System.out.println(Arrays.toString(decodeResult.getPath()));
    }
}
