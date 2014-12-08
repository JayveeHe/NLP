package Tests;

import HMM.BasicModel.*;
import Utils.JacobiMethod;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jayvee on 2014/12/4.
 */
public class testOtherThing {
    public static void main(String aaaaa[]) {
//        HMModel hmModel = new HMModel(3, 4);
//        LogForwardVector lfv = new LogForwardVector(hmModel);
//        ForwardVector fv = new ForwardVector(hmModel);
//        int[] seq = {1, 0, 1, 2, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0};
//        double[][] logForwardMatrix = lfv.calLogForwardMatrix(seq);
//        double[][] forwardMatrix = fv.calForwardMatrix(seq, false);
//        System.out.println(Arrays.deepToString(forwardMatrix));
//        System.out.println(Arrays.deepToString(logForwardMatrix));
//        for (int i = 0; i < seq.length; i++) {
//            for (int j = 0; j < hmModel.getN(); j++) {
//                logForwardMatrix[i][j] = Math.pow(Math.E, logForwardMatrix[i][j]);
//            }
//        }
//        System.out.println(Arrays.deepToString(logForwardMatrix));
//
//        System.out.println(fv.calObSeqProb(seq, false));
//        System.out.println(Math.pow(Math.E, lfv.calLogObSeqProb(seq)));
//
//
//        LogBackwardVector lbv = new LogBackwardVector(hmModel);
//        BackwardVector bv = new BackwardVector(hmModel);
//        double[][] logBackwardMatrix = lbv.calLogBackwardMatrix(seq);
//        double[][] backwardMatrix = bv.calBackwardMatrix(seq, false);
//        System.out.println(Arrays.deepToString(backwardMatrix));
//        System.out.println(Arrays.deepToString(logBackwardMatrix));
//        for (int i = 0; i < seq.length; i++) {
//            for (int j = 0; j < hmModel.getN(); j++) {
//                logBackwardMatrix[i][j] = Math.pow(Math.E, logBackwardMatrix[i][j]);
//            }
//        }
//        System.out.println(Arrays.deepToString(logBackwardMatrix));


        double a = 0.022221111f;
        double b = 0.11122211114f;
        double c = 1.2222f;
        double d = 2.111155555f;
        ArrayList<Double> t = new ArrayList<Double>();
//        t.add(a);
//        t.add(b);
//        t.add(c);
//        t.add(d);
        double sum = 0;
        for (int i = 0; i < 1000000; i++) {
            double temp = Math.random();
            t.add(temp);
            sum += Math.pow(Math.E, temp);
        }
        System.out.println("log_sum="+ Math.log(sum));
//        double jacobiRecursion = JacobiMethod.JacobiRecursion((ArrayList<Double>) t.clone());
//        System.out.println(jacobiRecursion);
        double jacobiCalSum = JacobiMethod.JacobiCalSum(t);
        System.out.println(jacobiCalSum);

    }
}
