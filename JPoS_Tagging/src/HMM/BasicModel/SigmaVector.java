package HMM.BasicModel;

import java.math.BigDecimal;

/**
 * 用于表示，已知模型和观察序列，t时刻处于隐藏状态i，t+1时刻处于隐藏状态j的概率
 * Created by Jayvee on 2014/11/13.
 */
public class SigmaVector {
    HMModel hmModel;
    double[][] forwardMatrix;
    double[][] backwardMatrix;
    double polamda = 0;
    int[] obSequence;

    /**
     * 构造函数。一个实例是否指定唯一的观测序列？此点存疑。
     *
     * @param hmModel    模型
     * @param obSequence 观测序列
     */
    public SigmaVector(HMModel hmModel, int[] obSequence, boolean isScaled) {
        this.hmModel = hmModel;
        this.obSequence = obSequence;
        ForwardVector forwardVector = new ForwardVector(hmModel);
        forwardMatrix = forwardVector.calForwardMatrix(obSequence, isScaled);
        BackwardVector backwardVector = new BackwardVector(hmModel);
        backwardMatrix = backwardVector.calBackwardMatrix(obSequence, isScaled);

    }


    /**
     * 计算sigma_tij
     *
     * @param t 时刻
     * @param i t时状态序号
     * @param j t+1时状态序号
     * @return
     */
    public double calSigma(int t, int i, int j) {
        //分子
        double fenzi = calSigmaTemp(t, i, j);
        //计算P(O|lamda)，即分母
        double fenmu = 0;
        for (int n = 0; n < hmModel.getN(); n++) {
            for (int m = 0; m < hmModel.getN(); m++) {
                fenmu += calSigmaTemp(t, n, m);
            }
        }
        return fenzi / fenmu;
    }

    /**
     * 计算sigma的中间项
     *
     * @param t 时刻
     * @param i t时刻状态序号
     * @param j t+1时刻状态序号
     */
    public double calSigmaTemp(int t, int i, int j) {
        double[][] aMatrix = hmModel.getAMatrix();
        double[][] bMatrix = hmModel.getBMatrix();
        return forwardMatrix[t][i] * aMatrix[i][j]
                * bMatrix[j][obSequence[t + 1]] * backwardMatrix[t + 1][j];
    }

    /**
     * 计算Gamma值
     *
     * @param t
     * @param i
     * @return
     */
    public double calGammaVector(int t, int i) {
        double ProbSum = 0;
        BigDecimal probSum_big = BigDecimal.valueOf(0);
        for (int k = 0; k < hmModel.getN(); k++) {
            probSum_big = probSum_big.add(
                    BigDecimal.valueOf(
                            forwardMatrix[t][k]).multiply(
                            BigDecimal.valueOf(backwardMatrix[t][k])));
            ProbSum += forwardMatrix[t][k] * backwardMatrix[t][k];
        }
//        double gamma_ti = forwardMatrix[t][i] * backwardMatrix[t][i] / ProbSum;
        if(probSum_big.doubleValue()==0){
            return 0;
        }
        double gamma_ti = ((BigDecimal.valueOf(
                forwardMatrix[t][i]).multiply(
                BigDecimal.valueOf(backwardMatrix[t][i]))).divide(probSum_big,100,BigDecimal.ROUND_DOWN)).doubleValue();
        return gamma_ti;

    }

    /**
     * 计算Gamma的中间项
     *
     * @param t
     * @param i
     * @return
     */
    public double calGammaTemp(int t, int i) {
        return forwardMatrix[t][i] * backwardMatrix[t][i];
    }
}
