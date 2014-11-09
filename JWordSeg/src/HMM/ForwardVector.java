package HMM;

/**
 * Created by Jayvee on 2014/11/6.
 */
public class ForwardVector {
    //保存的变量
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
    public double calObSeqProb(int[] ObSequence) {
        double[][] forwardMatrix = calForwardMatrix(ObSequence);
        double prob = 0;
        for (int i = 0; i < hmModel.getN(); i++) {
            prob += forwardMatrix[ObSequence.length - 1][i];
        }
        return prob;
    }

    /**
     * 输入观测序列（映射序号形式），计算前向向量矩阵（即所有时刻的前向向量）
     *
     * @param ObSequence 观测序列（映射序号形式）
     * @return 观测序列对应的前向向量矩阵
     */
    public double[][] calForwardMatrix(int[] ObSequence) {
        int T = ObSequence.length;
        double[][] forwardMatrix = new double[T][hmModel.getN()];//前向向量矩阵，第一下标为时刻，第二下标为状态序号
        //首先计算初始化的值（即第一次迭代）
        double[] piVector = hmModel.getPiVector();
        double[][] bMatrix = hmModel.getBMatrix();
        for (int i = 0; i < this.hmModel.getN(); i++) {
            forwardMatrix[0][i] = piVector[i] * bMatrix[i][ObSequence[0]];
        }
        for (int t = 1; t < T; t++) {//外层大循环
            for (int j = 0; j < hmModel.getN(); j++) {
                forwardMatrix[t][j] = forwardInduction(j, ObSequence[t], forwardMatrix[t - 1]);
            }
        }//通过大循环，计算出了T时长内观测序列的所有前向向量，存在ListForwardVec中
        return forwardMatrix;
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

}
