package HMM.BasicModel;

/**
 * Gamma向量，给定模型和观察序列，t时刻处于隐藏状态i的概率
 * Created by Jayvee on 2014/11/12.
 */
public class GammaVector {
    double[][] forwardMatrix;
    double[][] backwardMatrix;
    HMModel hmModel;

    public GammaVector(int[] ObSequence, HMModel hmModel) {
        this.hmModel = hmModel;
        ForwardVector forwardVector = new ForwardVector(hmModel);
        this.forwardMatrix = forwardVector.calForwardMatrix(ObSequence,true);
        BackwardVector backwardVector = new BackwardVector(hmModel);
        this.backwardMatrix = backwardVector.calBackwardMatrix(ObSequence,true);
    }

    public double calGammaVector(int t, int i) {
        double ProbSum = 0;
        for (int k = 0; k < hmModel.getN(); k++) {
            ProbSum += forwardMatrix[t][k] * backwardMatrix[t][k];
        }
        double gamma_ti = forwardMatrix[t][i] * backwardMatrix[t][i] / ProbSum;
        return gamma_ti;

    }
}
