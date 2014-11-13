package HMM.BasicModel;

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

    public HMModel(int N, int M) {
        this.N = N;
        this.M = M;
        this.AMatrix = new double[N][N];
        this.BMatrix = new double[N][M];
        this.piVector = new double[N];
    }

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

}
