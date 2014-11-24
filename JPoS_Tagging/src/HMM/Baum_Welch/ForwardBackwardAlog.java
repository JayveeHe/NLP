package HMM.Baum_Welch;


import HMM.BasicModel.*;
import HMM.Utils.RandomUtils;

import java.util.ArrayList;

/**
 * Created by Jayvee on 2014/11/13.
 */
public class ForwardBackwardAlog {
    double[] piVector;
    double[][] aMatrix;
    double[][] bMatrix;
    HMModel hmModel;

    public ForwardBackwardAlog(HMModel hmModel) {
        this.hmModel = hmModel;
        this.piVector = hmModel.getPiVector();
        this.aMatrix = hmModel.getAMatrix();
        this.bMatrix = hmModel.getBMatrix();
    }


    /**
     * 使用多观测序列进行训练
     *
     * @param trainList 观测序列列表
     */
    public void TrainByMultiObseq(ArrayList<int[]> trainList, boolean isSmooth) {
        //TODO 多观测序列的训练
        double[] prob_O = calPK(trainList, hmModel);
        double[][] new_Amatrix = new double[hmModel.getN()][hmModel.getN()];
        double[][] new_Bmatrix = new double[hmModel.getN()][hmModel.getM()];
        double[] new_piVector = new double[hmModel.getN()];
        double probSum = 0;
        for (int q = 0; q < trainList.size(); q++) {
            probSum += prob_O[q];
        }

        for (int i = 0; i < hmModel.getN(); i++) {
            //大循环开始，分为三部分，第一部分为计算aMatrix，第二部分为计算bMatrix，第三部分计算piVector
            //由于他们的维度不同，所以分开计算
            //更新aMatrix
            for (int j = 0; j < hmModel.getN(); j++) {
                double a_numerator = 0;//新a矩阵的分子
                double a_denominator = 0;//新a的分母
                for (int k = 0; k < trainList.size(); k++) {
                    int[] individualSeq = trainList.get(k);
                    int Tk = individualSeq.length;
                    GammaVector gammaVector = new GammaVector(individualSeq, hmModel);
                    SigmaVector sigmaVector = new SigmaVector(hmModel, individualSeq);
                    double a_fenmuTemp = 0;
                    double a_fenziTemp = 0;
                    double[] gammaVec = new double[Tk];
                    for (int t = 0; t < Tk - 1; t++) {
                        gammaVec[i] = gammaVector.calGammaVector(t, i);
                        a_fenmuTemp += gammaVec[i];
//                    }
//                    for (int t = 0; t < Tk - 1; t++) {
                        a_fenziTemp += sigmaVector.calSigma(t, i, j);
                    }
                    a_numerator += a_fenziTemp / prob_O[k];
                    a_denominator += a_fenmuTemp / prob_O[k];

                }
                new_Amatrix[i][j] = a_numerator / a_denominator;

            }

            //更新bMatrix
            for (int p = 0; p < hmModel.getM(); p++) {//求bi(P)的新值
                double b_numerator = 0;//b的分子
                double b_denominator = 0;//b的分母
                for (int k = 0; k < trainList.size(); k++) {
                    int[] individualSeq = trainList.get(k);
                    int Tk = individualSeq.length;
                    GammaVector gammaVector = new GammaVector(individualSeq, hmModel);
                    double b_fenmuTemp = 0;
                    double b_fenziTemp = 0;
                    double[] gammaVec = new double[Tk];
                    for (int t = 0; t < Tk; t++) {//此处是iTk还是Tk-1，存疑
                        gammaVec[t] = gammaVector.calGammaVector(t, i);
                        b_fenmuTemp += gammaVec[t];
//                    }
//                    for (int t = 0; t < Tk ; t++) {
                        if (individualSeq[t] == p) {
                            b_fenziTemp += gammaVec[t];
                        }
                    }
                    b_numerator += b_fenziTemp / prob_O[k];
                    b_denominator += b_fenmuTemp / prob_O[k];
                }
                new_Bmatrix[i][p] = b_numerator / b_denominator;
            }
            //更新piVec
            for (int k = 0; k < trainList.size(); k++) {
                int[] individualSeq = trainList.get(k);
                GammaVector gammaVector = new GammaVector(individualSeq, hmModel);
                new_piVector[i] += (prob_O[k] / probSum) * gammaVector.calGammaVector(0, i);
            }
        }//大循环完毕，更新模型参数
        if (isSmooth) {
            for (int i = 0; i < hmModel.getN(); i++) {
                RandomUtils.LaplaceSmooth(new_Amatrix[i], 0.0001);
                RandomUtils.LaplaceSmooth(new_Bmatrix[i], 0.0001);
            }
            RandomUtils.LaplaceSmooth(new_piVector, 0.0001);
        }
        hmModel.setAMatrix(new_Amatrix);
        hmModel.setBMatrix(new_Bmatrix);
        hmModel.setPiVector(new_piVector);
        System.out.println("多观察序列训练完毕");
    }


    /**
     * 使用单个观测序列进行训练
     *
     * @param trainObseq 观测序列
     */
    public void TrainBySingleObseq(int[] trainObseq) {
        int T = trainObseq.length;
        double[][] new_Amatrix = new double[hmModel.getN()][hmModel.getN()];
        double[][] new_Bmatrix = new double[hmModel.getN()][hmModel.getM()];
        double[] new_piVector = new double[hmModel.getN()];
        GammaVector gammaVector = new GammaVector(trainObseq, hmModel);
        for (int i = 0; i < hmModel.getN(); i++) {
            //更新pi
            new_piVector[i] = gammaVector.calGammaVector(0, i);

            //更新aMatrix
            SigmaVector sigmaVector = new SigmaVector(hmModel, trainObseq);
            double[] gammaVec = new double[T];
            double denominator_aMatrix = 0;//a矩阵迭代的分母项
            for (int t = 0; t < T - 1; t++) {
                gammaVec[t] = gammaVector.calGammaVector(t, i);
                denominator_aMatrix += gammaVec[t];
            }
            for (int j = 0; j < hmModel.getN(); j++) {
                double sigmaSum = 0;
                for (int t = 0; t < T - 1; t++) {
                    sigmaSum += sigmaVector.calSigma(t, i, j);
                }
                new_Amatrix[i][j] = sigmaSum / denominator_aMatrix;
            }
            //更新bMatrix
            gammaVec[T - 1] = gammaVector.calGammaVector(T - 1, i);
            for (int k = 0; k < hmModel.getM(); k++) {
                double numerator_bMatrix = 0;//b矩阵迭代的分子项
                double denominator_bMatrix = 0;//b矩阵迭代的分母项
                for (int t = 0; t < T; t++) {
                    if (trainObseq[t] == k) {
                        numerator_bMatrix += gammaVec[t];
                    }
                    denominator_bMatrix += gammaVec[t];
                }
                new_Bmatrix[i][k] = numerator_bMatrix / denominator_bMatrix;
            }
            //大循环完毕
        }
        hmModel.setAMatrix(new_Amatrix);
        hmModel.setBMatrix(new_Bmatrix);
        hmModel.setPiVector(new_piVector);
        System.out.println("参数重估完毕");
    }


    private void initHMM(int N, int M) {
        HMModel hmModel = new HMModel(N, M);
    }

    /**
     * TODO 收敛判定
     */
    private void isConverged() {

    }

    /**
     * 计算各观测序列的概率值
     *
     * @param listTrain
     * @param hmModel
     * @return
     */
    private double[] calPK(ArrayList<int[]> listTrain, HMModel hmModel) {
        int K = listTrain.size();
        ForwardVector forwardVector = new ForwardVector(hmModel);
        //计算出各pk值
        double[] p_o_lamda = new double[K];
        for (int k = 0; k < K; k++) {
            p_o_lamda[k] = forwardVector.calObSeqProb(listTrain.get(k), true);
        }
        return p_o_lamda;
    }
}
