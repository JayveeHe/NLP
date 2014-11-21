package HMM.BasicModel;


import HMM.Utils.RandomUtils;

/**
 * Created by Jayvee on 2014/11/6.
 */
public class HMModel {

    /**
     * 模型的隐藏状态数
     */
    private int N;
    /**
     * 对于每个隐藏状态，可观察的符号数
     */
    private int M;
    /**
     * 状态转移概率矩阵
     */
    private double[][] AMatrix;
    /**
     * 对于每个隐藏状态，可观测到的各符号的概率。
     * 即混淆矩阵。第一个下标为状态序号，第二个下标为观测序号
     */
    private double[][] BMatrix;


    /**
     * 初始概率分布向量
     */
    private double[] piVector;

    /**
     * 指定状态数和符号数，进行随机初始化HMM模型
     *
     * @param N 状态数
     * @param M 符号数
     */
    public HMModel(int N, int M) {
        this.N = N;
        this.M = M;
        this.AMatrix = new double[N][N];
        this.BMatrix = new double[N][M];
        this.piVector = new double[N];
        for (int i = 0; i < N; i++) {
            RandomUtils.randomInitProb(AMatrix[i]);
            RandomUtils.randomInitProb(BMatrix[i]);
        }
        RandomUtils.randomInitProb(piVector);
    }


    /**
     * 根据给定参数进行
     *
     * @param N
     * @param M
     * @param AMatrix
     * @param BMatrix
     * @param piVector
     */
    public HMModel(int N, int M, double[][] AMatrix, double[][] BMatrix, double[] piVector) {
        this.N = N;
        this.M = M;
        this.AMatrix = AMatrix;
        this.BMatrix = BMatrix;
        this.piVector = piVector;
    }

    /**
     * @return 模型的隐藏状态数
     */
    public int getN() {
        return N;
    }

    /**
     * @return 对于每个隐藏状态，可观察的符号数
     */
    public int getM() {
        return M;
    }

    /**
     * @return 状态转移概率矩阵
     */
    public double[][] getAMatrix() {
        return AMatrix;
    }

    /**
     * @return 混淆矩阵
     */
    public double[][] getBMatrix() {
        return BMatrix;
    }

    /**
     * @return 初始概率分布向量
     */
    public double[] getPiVector() {
        return piVector;
    }

    public void setAMatrix(double[][] AMatrix) {
        this.AMatrix = AMatrix;
    }

    public void setBMatrix(double[][] BMatrix) {
        this.BMatrix = BMatrix;
    }

    public void setPiVector(double[] piVector) {
        this.piVector = piVector;
    }

    @Override
    public String toString() {
        String text = "";
        //打印参数
        text = text + "N=" + N + "\tM=" + M + "\npiVector:\n";
        //打印pi向量
        for (int i = 0; i < piVector.length; i++) {
            text = text + piVector[i] + "\t";
        }
        text = text + "\nA:\n";
        for (int i = 0; i < AMatrix.length; i++) {
            for (int j = 0; j < AMatrix[i].length; j++) {
                text = text + AMatrix[i][j] + "\t";
            }
            text = text + "\n";
        }
        text = text + "\nB:\n";
        for (int i = 0; i < BMatrix.length; i++) {
            for (int j = 0; j < BMatrix[i].length; j++) {
                text = text + BMatrix[i][j] + "\t";
            }
            text = text + "\n";
        }
        return text;
    }
}
