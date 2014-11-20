package HMM.Viterbi;

import HMM.BasicModel.HMModel;

/**
 * Created by Jayvee on 2014/11/12.
 */
public class ViterbiDecoder {
    HMModel hmModel;
    double[][] log_aMatrix;
    double[] piVector;
    double[][] bMatrix;
    double[][] aMatrix;

    public ViterbiDecoder(HMModel hmModel) {
        this.hmModel = hmModel;
        this.piVector = hmModel.getPiVector();
        this.aMatrix = hmModel.getAMatrix();
        this.bMatrix = hmModel.getBMatrix();
        this.log_aMatrix = new double[hmModel.getN()][hmModel.getN()];
        for (int i = 0; i < hmModel.getN(); i++) {
            for (int j = 0; j < hmModel.getN(); j++) {
                log_aMatrix[i][j] = Math.log(hmModel.getAMatrix()[i][j]);
            }
        }
    }

    /**
     * 用于存储解码结果的类，包含了解码的最佳路径和该路径的概率
     */
    public class DecodeResult {
        int[] path;
        double pathProb;

        public DecodeResult(int[] path, double pathProb) {
            this.path = path;
            this.pathProb = pathProb;
        }

        public int[] getPath() {
            return path;
        }

        public double getPathProb() {
            return pathProb;
        }
    }

    /**
     * 给定一个观察序列，进行维特比译码
     *
     * @param ObSequence 观察序列
     * @return DecodeResult 结果类
     */
    public DecodeResult decode(int[] ObSequence, boolean isScaled) {
        int T = ObSequence.length;
        double[][] deltaMatrix = new double[T][hmModel.getN()];
        int[][] faiMatrix = new int[T][hmModel.getN()];

        //init
        for (int i = 0; i < hmModel.getN(); i++) {
            if (isScaled) {
                deltaMatrix[0][i] = Math.log(piVector[i]) + Math.log(bMatrix[i][ObSequence[0]]);
            } else {
                deltaMatrix[0][i] = piVector[i] * bMatrix[i][ObSequence[0]];
            }
            faiMatrix[0][i] = 0;
        }
        //recursion
        for (int t = 1; t < T; t++) {
            for (int j = 0; j < hmModel.getN(); j++) {
                double[] result = ViterbiRecursion(j, ObSequence[t], deltaMatrix[t - 1], isScaled);
                deltaMatrix[t][j] = result[0];
                faiMatrix[t][j] = (int) result[1];

            }
        }
        //termination
        double maxProb = 0;
        if (isScaled) {
            maxProb = Double.NEGATIVE_INFINITY;
        }
        int bestFai = 0;
        for (int i = 0; i < hmModel.getN(); i++) {
            if (maxProb < deltaMatrix[T - 1][i]) {
                maxProb = deltaMatrix[T - 1][i];
                bestFai = i;
            }
        }
        //Path
        int[] path = new int[T];
        path[T - 1] = bestFai;
        for (int t = T - 2; t >= 0; t--) {
            path[t] = faiMatrix[t + 1][path[t + 1]];
        }
        return new DecodeResult(path, maxProb);
    }

    /**
     * 维特比译码的迭代项
     *
     * @param j              给定的隐藏状态序号
     * @param ObIndex        指定时刻的观测序号
     * @param formerDeltaVec 上一时刻的delta向量
     * @return 一个二维double数组，第0项为delta值，第1项为fai值
     */
    public double[] ViterbiRecursion(int j, int ObIndex, double[] formerDeltaVec, boolean isScaled) {
        double max = 0;
        int fai = 0;
        if (isScaled) {
            max = Double.NEGATIVE_INFINITY;
        }
        double[][] aMatrix = hmModel.getAMatrix();
        for (int i = 0; i < hmModel.getN(); i++) {
            if (isScaled) {
                if (formerDeltaVec[i] == Double.NEGATIVE_INFINITY || log_aMatrix[i][j] == Double.NEGATIVE_INFINITY) {
                    continue;
                }
                double temp = formerDeltaVec[i] + log_aMatrix[i][j];
                if (temp > max) {
                    max = temp;
                    fai = i;
                }
            } else {
                double temp = formerDeltaVec[i] * aMatrix[i][j];
                if (temp > max) {
                    max = temp;
                    fai = i;
                }
            }
        }
        if (isScaled) {
            double[] result = {max + Math.log(hmModel.getBMatrix()[j][ObIndex]), fai};
            return result;
        } else {
            double[] result = {max * hmModel.getBMatrix()[j][ObIndex], fai};
            return result;

        }
    }
}
