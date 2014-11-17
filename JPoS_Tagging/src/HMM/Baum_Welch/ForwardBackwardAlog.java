package HMM.Baum_Welch;


import HMM.BasicModel.*;

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
    public void TrainByMultiObseq(ArrayList<int[]> trainList) {
        //TODO 多观测序列的训练
        double[] pk = calPK(trainList, hmModel);
        double a_numerator = 0;//新a矩阵的分子
        double a_denominator = 0;//新a的分母
        double b_numerator = 0;//b的分子
        double b_denominator = 0;//b的分母
        for (int k = 0; k < trainList.size(); k++) {
            int[] individualSeq = trainList.get(k);
            for (int t = 0; t < individualSeq.length; t++) {

            }
        }
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
