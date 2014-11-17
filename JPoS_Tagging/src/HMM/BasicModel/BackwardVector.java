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
    public double[][] calBackwardMatrix(int[] ObSequence, boolean isScaled) {
        int T = ObSequence.length;
        double[][] backwardMatrix = new double[T][hmModel.getN()];
        //初始化
        for (int i = 0; i < hmModel.getN(); i++) {
            backwardMatrix[T - 1][i] = 1;
        }
        for (int t = T - 2; t > -1; t--) {
            for (int i = 0; i < hmModel.getN(); i++) {
                backwardMatrix[t][i] = backInduction(i, ObSequence[t + 1], backwardMatrix[t + 1]);
            }
            if (isScaled) {
                //进行缩放，并将缩放后的后向向量重新赋值
                double dt = calDt(backwardMatrix[t]);
                for (int i = 0; i < hmModel.getN(); i++) {
                    backwardMatrix[t][i] = dt * backwardMatrix[t][i];
                }
            }
        }
//        if (isScaled) {
//            return calScaledBackwardMatrix(ObSequence, backwardMatrix);
//        } else {
        return backwardMatrix;
//        }
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

    private double[][] calScaledBackwardMatrix(int[] ObSequence, double[][] backwardMatrix) {
        int T = ObSequence.length;
        double[][] scaledBackwardMatrix = new double[backwardMatrix.length][backwardMatrix[0].length];
        for (int t = 0; t < T; t++) {
            double Dt = 0;
            for (int k = 0; k < backwardMatrix[0].length; k++) {
                Dt += backwardMatrix[t][k];
            }
            for (int i = 0; i < backwardMatrix[0].length; i++) {
                scaledBackwardMatrix[t][i] = backwardMatrix[t][i] / Dt;
            }
        }
        return scaledBackwardMatrix;
    }

    private double calDt(double[] backwardVec) {
        double Dtemp = 0;
        for (int i = 0; i < backwardVec.length; i++) {
            Dtemp += backwardVec[i];
        }
        return 1 / Dtemp;
    }

}
