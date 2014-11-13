package HMM.BasicModel;

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
    public SigmaVector(HMModel hmModel, int[] obSequence) {
        this.hmModel = hmModel;
        this.obSequence = obSequence;
        ForwardVector forwardVector = new ForwardVector(hmModel);
        forwardMatrix = forwardVector.calForwardMatrix(obSequence);
        BackwardVector backwardVector = new BackwardVector(hmModel);
        backwardMatrix = backwardVector.calBackwardMatrix(obSequence);

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
        double fenzi = calTemp(t, i, j);
        //计算P(O|lamda)，即分母
        double fenmu = 0;
        for (int n = 0; n < hmModel.getN(); n++) {
            for (int m = 0; m < hmModel.getN(); m++) {
                fenmu += calTemp(t, n, m);
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
    public double calTemp(int t, int i, int j) {
        double[][] aMatrix = hmModel.getAMatrix();
        double[][] bMatrix = hmModel.getBMatrix();
        return forwardMatrix[t][i] * aMatrix[i][j]
                * bMatrix[j][obSequence[t + 1]] * backwardMatrix[t + 1][j];
    }
}
