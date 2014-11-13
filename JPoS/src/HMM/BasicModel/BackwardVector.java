package HMM.BasicModel;

/**
 * Created by Jayvee on 2014/11/10.
 */
public class BackwardVector {
    private HMModel hmModel;

    public BackwardVector(HMModel hmModel) {
        this.hmModel = hmModel;
    }

    /**
     * 计算后向向量矩阵
     *
     * @param ObSequence 给定的观察序列
     * @return 后向向量矩阵
     */
    public double[][] calBackwardMatrix(int[] ObSequence) {
        int T = ObSequence.length;
        double[][] backwardMatrix = new double[T][hmModel.getN()];
        //初始化
        for (int i = 0; i < hmModel.getN(); i++) {
            backwardMatrix[T - 1][i] = 1;
        }
        int t = 0;
        for (t = T - 1; t > 0; t--) {
            for (int i = 0; i < hmModel.getN(); i++) {
                backwardMatrix[t - 1][i] = backInduction(i, ObSequence[t], backwardMatrix[t]);
            }
        }
        return backwardMatrix;
    }

    /**
     * 后向向量计算迭代项
     *
     * @param i              状态序号
     * @param ObIndex        观察序列序号
     * @param forBackwardVec 后一时刻的后向向量（已计算）
     * @return 指定时刻处于指定状态的后向向量
     */
    public double backInduction(int i, int ObIndex, double[] forBackwardVec) {
        double sum = 0;
        double[][] aMatrix = hmModel.getAMatrix();
        double[][] bMatrix = hmModel.getBMatrix();
        for (int j = 0; j < hmModel.getN(); j++) {
            sum += aMatrix[i][j] * bMatrix[j][ObIndex] * forBackwardVec[j];
        }
        return sum;
    }

}
