package HMM.BasicModel;


import java.util.ArrayList;

/**
 * Created by Jayvee on 2014/11/6.
 */
public class ForwardVector {
    private HMModel hmModel;

    public ForwardVector(HMModel hmModel) {
        this.hmModel = hmModel;
    }

    /**
     * 根据给定的观测序列，和已知的HMM，计算模型产生该观测序列的概率
     *
     * @param ObSequence 观测序列
     * @return 模型产生该观测序列的概率
     */
    public double calObSeqProb(int[] ObSequence, boolean isScaled) {
        int T = ObSequence.length;
        if (!isScaled) {
//     未缩放前概率计算
            double[][] forwardMatrix = calForwardMatrix(ObSequence, false);
            double prob = 0;
            for (int i = 0; i < hmModel.getN(); i++) {
                prob += forwardMatrix[ObSequence.length - 1][i];
            }
            return prob;
        } else {
            //首先计算初始化的值（即第一次迭代）
            double[] forwardVec = new double[hmModel.getN()];
            double[] piVector = hmModel.getPiVector();
            double[][] bMatrix = hmModel.getBMatrix();
            double prob_log = 0;
            //t=0时的初始化
            for (int i = 0; i < this.hmModel.getN(); i++) {
                forwardVec[i] = piVector[i] * bMatrix[i][ObSequence[0]];

            }
            double ct_init = calCtByRaw(forwardVec);
            prob_log += -Math.log(ct_init);
            //t>0后的迭代
            for (int t = 1; t < T; t++) {//外层大循环
                double[] rawAlphas = new double[hmModel.getN()];
                for (int j = 0; j < hmModel.getN(); j++) {
                    rawAlphas[j] = forwardInduction(j, ObSequence[t], forwardVec);//通过缩放后的前一时刻向量计算出来的当前时刻向量j
                }
                //缩放后的概率计算
                double ct = calCtByRaw(rawAlphas);
                //缩放后的赋值
                for (int k = 0; k < hmModel.getN(); k++) {
                    forwardVec[k] = rawAlphas[k] * ct;
                }
                prob_log += -Math.log(ct);
            }
//            return Math.pow(Math.E, prob_log);
            return prob_log;
        }

    }

    /**
     * 输入观测序列（映射序号形式），计算前向向量矩阵（即所有时刻的前向向量）
     *
     * @param ObSequence 观测序列（映射序号形式）
     * @return 观测序列对应的前向向量矩阵
     */
    public double[][] calForwardMatrix(int[] ObSequence, boolean isScaled) {
        int T = ObSequence.length;
        double[][] forwardMatrix = new double[T][hmModel.getN()];//前向向量矩阵，第一下标为时刻，第二下标为状态序号
        //首先计算初始化的值（即第一次迭代）
        double[] piVector = hmModel.getPiVector();
        double[][] bMatrix = hmModel.getBMatrix();
        for (int i = 0; i < this.hmModel.getN(); i++) {
            forwardMatrix[0][i] = piVector[i] * bMatrix[i][ObSequence[0]];
            if (isScaled) {
                //进行缩放,将缩放后的前向向量赋值给当前存储的前向向量
                double ct = calCtByRaw(forwardMatrix[0]);
                for (int k = 0; k < hmModel.getN(); k++) {
                    forwardMatrix[0][k] = ct * forwardMatrix[0][k];
                }
            }
        }
        for (int t = 1; t < T; t++) {//外层大循环
            for (int j = 0; j < hmModel.getN(); j++) {
                forwardMatrix[t][j] = forwardInduction(j, ObSequence[t], forwardMatrix[t - 1]);
            }
            if (isScaled) {
                //进行缩放,将缩放后的前向向量赋值给当前存储的前向向量
                double ct = calCtByRaw(forwardMatrix[t]);
                for (int k = 0; k < hmModel.getN(); k++) {
                    forwardMatrix[t][k] = ct * forwardMatrix[t][k];
                }
            }
        }//通过大循环，计算出了T时长内观测序列的所有前向向量，存在ListForwardVec中
//        if (isScaled) {
//            return calScaledForwardMatrix(ObSequence, forwardMatrix);
//        } else {
        return forwardMatrix;
//    }

    }

    /**
     * 用于前向向量计算过程中的迭代项
     *
     * @param j             指定的状态序号
     * @param obIndex       此时观察到的序列序号
     * @param preForwardVec 上一时刻的前向向量数组
     */
    public double forwardInduction(int j, int obIndex, double[] preForwardVec) {
        double beta_j = hmModel.getBMatrix()[j][obIndex];
        double sum = 0;
        double[][] aMatrix = hmModel.getAMatrix();
        for (int i = 0; i < hmModel.getN(); i++) {
            sum += preForwardVec[i] * aMatrix[i][j];
        }
        return sum * beta_j;
    }


    /**
     * 根据已算好的未缩放前向向量矩阵， 计算缩放后的前向向量矩阵
     *
     * @param ObSequence
     * @param forwardMatrix
     */
    private double[][] calScaledForwardMatrix(int[] ObSequence, double[][] forwardMatrix) {
        int T = ObSequence.length;
        double[][] scaledForwardMatrix = new double[forwardMatrix.length][forwardMatrix[0].length];
        for (int t = 0; t < T; t++) {
            double Ct = 0;
            Ct = calCtByRaw(forwardMatrix[t]);
            for (int i = 0; i < forwardMatrix[0].length; i++) {
                scaledForwardMatrix[t][i] = forwardMatrix[t][i] / Ct;
            }
        }
        return scaledForwardMatrix;
    }

    /**
     * 根据未缩放的t-1时刻的前向向量组计算缩放因子Ct
     *
     * @param rawForwardVec
     * @return
     */
    private double calCtByRaw(double[] rawForwardVec) {
        double ctemp = 0;
        for (int i = 0; i < rawForwardVec.length; i++) {
            ctemp += rawForwardVec[i];
        }
        return 1 / ctemp;
    }



}
