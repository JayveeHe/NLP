package Test;

import HMM.BasicModel.ForwardVector;
import HMM.BasicModel.HMModel;
import HMM.Baum_Welch.ForwardBackwardAlog;

import java.util.ArrayList;

/**
 * Created by Jayvee on 2014/11/24.
 */
public class testTrain {
    public static void main(String a[]) {
        HMModel hmModel = new HMModel(10, 100000);
        ForwardBackwardAlog fba = new ForwardBackwardAlog(hmModel);
        int[] seq1 = {0, 1, 1, 1, 1, 1, 2, 3, 4, 1, 1, 1, 2, 3, 4};
        int[] seq2 = {2, 1, 1, 2, 2, 2, 2, 3, 4, 3, 2, 5, 1, 2, 6};
        int[] seq3 = {0, 4, 7, 8, 8, 5, 4, 6, 2, 2, 3, 3, 4, 1, 1};
        ArrayList<int[]> trainList = new ArrayList<int[]>();
        trainList.add(seq1);
        trainList.add(seq2);
        trainList.add(seq3);
        ForwardVector forwardVector = new ForwardVector(hmModel);
        System.out.println("学习前概率：\n" + forwardVector.calObSeqProb(seq1, true));
        System.out.println(forwardVector.calObSeqProb(seq2, true));
        System.out.println(forwardVector.calObSeqProb(seq3, true));
//        System.out.println(hmModel);
        for (int i = 0; i < 5; i++) {
            fba.TrainByMultiObseq(trainList,false);
            forwardVector = new ForwardVector(hmModel);
            System.out.println("学习后概率：\n" + forwardVector.calObSeqProb(seq1, true));
            System.out.println(forwardVector.calObSeqProb(seq2, true));
            System.out.println(forwardVector.calObSeqProb(seq3, true));
//            System.out.println(hmModel);
        }
        fba.TrainByMultiObseq(trainList,true);
        forwardVector = new ForwardVector(hmModel);
        System.out.println("学习后概率：\n" + forwardVector.calObSeqProb(seq1, true));
        System.out.println(forwardVector.calObSeqProb(seq2, true));
        System.out.println(forwardVector.calObSeqProb(seq3, true));
        hmModel.saveModel(System.currentTimeMillis() + "");
    }
}
