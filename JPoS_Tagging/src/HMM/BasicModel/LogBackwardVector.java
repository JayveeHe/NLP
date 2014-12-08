package HMM.BasicModel;

import Utils.JacobiMethod;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by Jayvee on 2014/12/7.
 */
public class LogBackwardVector {
    private HMModel hmModel;
    private double[][] logAMatrix;
    private int N;
    private int M;

    public LogBackwardVector(HMModel hmModel) {
        this.hmModel = hmModel;
        this.N = hmModel.getN();
        this.M = hmModel.getM();
        double[][] aMatrix = hmModel.getAMatrix();
        this.logAMatrix = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                logAMatrix[i][j] = Math.log(aMatrix[i][j]);
            }
        }
    }

    /**
     * 计算对数形式的后向向量矩阵
     *
     * @param ObSequence 给定的观察序列
     * @return 后向向量矩阵
     */
    public double[][] calLogBackwardMatrix(int[] ObSequence) {
        int T = ObSequence.length;
        double[][] logBackwardMatrix = new double[T][hmModel.getN()];
        double[][] bMatrix = hmModel.getBMatrix();
        //初始化
        for (int i = 0; i < hmModel.getN(); i++) {
            logBackwardMatrix[T - 1][i] = 0;//取对数后log(1)=0
        }
        for (int t = T - 2; t > -1; t--) {
            for (int i = 0; i < hmModel.getN(); i++) {
                ArrayList<Double> temp = new ArrayList<Double>();
                for (int j = 0; j < hmModel.getN(); j++) {
                    temp.add(logAMatrix[i][j] + Math.log(bMatrix[j][ObSequence[t + 1]]) + logBackwardMatrix[t + 1][j]);
                }
                logBackwardMatrix[t][i] = JacobiMethod.JacobiRecursion(temp);
            }
        }
        return logBackwardMatrix;
    }
}
