package HMM.BasicModel;

import Utils.JacobiMethod;

import java.util.ArrayList;

/**
 * Created by Jayvee on 2014/12/7.
 */
public class LogForwardVector {
    private HMModel hmModel;
    private double[][] logAMatrix;
    private int N;
    private int M;

    public LogForwardVector(HMModel hmModel) {
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


    /* 进行前向向量的对数运算*/

    /**
     * 计算对数形式的前向向量矩阵
     *
     * @param ObSequence 观察序列
     * @return
     */
    public double[][] calLogForwardMatrix(int[] ObSequence) {
        int T = ObSequence.length;
        double[][] logForwardMatrix = new double[T][hmModel.getN()];//前向向量矩阵，第一下标为时刻，第二下标为状态序号
        //首先计算初始化的值（即第一次迭代）
        double[] piVector = hmModel.getPiVector();
        double[][] bMatrix = hmModel.getBMatrix();
        for (int i = 0; i < N; i++) {
            logForwardMatrix[0][i] = Math.log(piVector[i]) + Math.log(bMatrix[i][ObSequence[0]]);
        }
        for (int t = 1; t < T; t++) {//外层大循环
            for (int j = 0; j < hmModel.getN(); j++) {
                ArrayList<Double> temp = new ArrayList<Double>();
                for (int i = 0; i < hmModel.getN(); i++) {
                    temp.add(logForwardMatrix[t - 1][i] + logAMatrix[i][j]);
                }
                logForwardMatrix[t][j] = JacobiMethod.JacobiRecursion(temp) + Math.log(bMatrix[j][ObSequence[t]]);
            }
        }//通过大循环，计算出了T时长内观测序列的所有前向向量，存在ListForwardVec中
        return logForwardMatrix;
    }


    public double calLogObSeqProb(int[] Obsequence) {
        int T = Obsequence.length;
        double[][] logForwardMatrix = calLogForwardMatrix(Obsequence);
        ArrayList<Double> temp = new ArrayList<Double>();
        for (int i = 0; i < N; i++) {
            temp.add(logForwardMatrix[T - 1][i]);
        }
        return JacobiMethod.JacobiRecursion(temp);
    }
}
