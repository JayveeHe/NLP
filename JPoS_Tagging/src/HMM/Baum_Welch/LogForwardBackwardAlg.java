package HMM.Baum_Welch;

import HMM.BasicModel.HMModel;
import HMM.BasicModel.LogBackwardVector;
import HMM.BasicModel.LogForwardVector;
import HMM.BasicModel.SigmaVector;
import Utils.JacobiMethod;
import Utils.RandomUtils;
import Utils.org.nevec.rjm.BigDecimalMath;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jayvee on 2014/12/7.
 */
public class LogForwardBackwardAlg {
    private double[] piVector;
    private double[][] aMatrix;
    private double[][] bMatrix;
    private HMModel hmModel;
    private double[][] logAMatrix;
    private double[][] logBMatrix;
    private double[] logPiVector;
    private int N;
    private int M;
    private boolean isN;//比较NM的值，N大为true，否则false

    public LogForwardBackwardAlg(HMModel hmModel) {
        this.hmModel = hmModel;
        this.piVector = hmModel.getPiVector();
        this.aMatrix = hmModel.getAMatrix();
        this.bMatrix = hmModel.getBMatrix();
        this.N = hmModel.getN();
        this.M = hmModel.getM();
        this.logAMatrix = new double[N][N];
        this.logBMatrix = new double[N][M];
        this.logPiVector = new double[N];
        calLogMatrix();
    }


    private void calLogMatrix() {
        int MAX = N > M ? N : M;
        this.isN = N > M;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < MAX; j++) {
                if (isN) {
                    logAMatrix[i][j] = Math.log(aMatrix[i][j]);
                    if (j < M) {
                        logBMatrix[i][j] = Math.log(bMatrix[i][j]);
                    }
                } else {
                    if (j < N) {
                        logAMatrix[i][j] = Math.log(aMatrix[i][j]);
                    }
                    logBMatrix[i][j] = Math.log(bMatrix[i][j]);
                }
            }
            logPiVector[i] = Math.log(piVector[i]);
        }
    }

    /**
     * 使用多观测序列进行训练
     *
     * @param trainList 观测序列列表
     * @param isSmooth  是否对参数进行拉普拉斯平滑
     */
    public void TrainByMultiObseq(ArrayList<int[]> trainList, boolean isSmooth) {
        calLogMatrix();
        LogForwardVector logForwardVector = new LogForwardVector(hmModel);
        LogBackwardVector logBackwardVector = new LogBackwardVector(hmModel);
        double[] logPk = new double[trainList.size()];
        double[][] newLogAMatrix = new double[N][N];
        double[][] newLogBMatrix = new double[N][M];
        double[] newLogPiVector = new double[N];
        int K = trainList.size();
        //首先计算各序列的概率对数值，并存储可以复用的前向、后向矩阵
        double[][] logAlphaMatrix = new double[N][N];
        double[][] logBetaMatrix = new double[N][M];
//        for (int k = 0; k < K; k++) {
//            int[] individualSeq = trainList.get(k);
//            logPk[k] = logForwardVector.calLogObSeqProb(individualSeq);
//            logAlphaMatrix[k] = logForwardVector.calLogForwardMatrix(individualSeq);
//            logBetaMatrix[k] = logBackwardVector.calLogBackwardMatrix(individualSeq);
//        }
        int MAX = N > M ? N : M;
        for (int i = 0; i < N; i++) {//外层大循环，统一计算a、b、pi矩阵的迭代
            System.out.println("进行第"+i+"个状态的计算");
            //由于ab矩阵的分母项相同，所以首先进行计算并在之后不再重复计算
            double log_denominator = 0;
            ArrayList<Double> DenominatorTemp = new ArrayList<Double>();
            //首先进行分母的计算
            for (int k = 0; k < K; k++) {
                int Tk = trainList.get(k).length;
                int[] individualSeq = trainList.get(k);
                logPk[k] = logForwardVector.calLogObSeqProb(individualSeq);
                logAlphaMatrix = logForwardVector.calLogForwardMatrix(individualSeq);
                logBetaMatrix = logBackwardVector.calLogBackwardMatrix(individualSeq);
                for (int t = 0; t < Tk - 1; t++) {
                    DenominatorTemp.add(logAlphaMatrix[t][i] + logBetaMatrix[t][i] - logPk[k]);
                }
            }
            log_denominator = JacobiMethod.JacobiCalSum(DenominatorTemp);
            for (int j = 0; j < MAX; j++) {
                ArrayList<Double> AtempArr = new ArrayList<Double>();
                ArrayList<Double> BtempArr = new ArrayList<Double>();
                double atemp = 0;
                double btemp = 0;
//                if (i == 0) {

//                }
                System.out.println("\t进行第"+j+"个符号的计算");
                if (isN) {//N比M大
                    if (j < M) {//N比M大且j小于M时，ab矩阵同时进行更新
                        for (int k = 0; k < K; k++) {
                            int[] seq = trainList.get(k);
                            int Tk = seq.length;
                            logPk[k] = logForwardVector.calLogObSeqProb(seq);
                            logAlphaMatrix = logForwardVector.calLogForwardMatrix(seq);
                            logBetaMatrix = logBackwardVector.calLogBackwardMatrix(seq);
                            for (int t = 0; t < Tk - 1; t++) {
                                atemp = logAlphaMatrix[t][i] + logAMatrix[i][j]
                                        + logBMatrix[j][seq[t + 1]]
                                        + logBetaMatrix[t + 1][j] - logPk[k];
                                AtempArr.add(atemp);
                                if (seq[t] == j) {//B矩阵约束条件的判断
                                    btemp = logAlphaMatrix[t][i]
                                            + logBetaMatrix[t][i] - logPk[k];
                                    BtempArr.add(btemp);
                                }
                            }
                        }
                        newLogAMatrix[i][j] = JacobiMethod.JacobiCalSum(AtempArr) - log_denominator;
                        newLogBMatrix[i][j] = JacobiMethod.JacobiCalSum(BtempArr) - log_denominator;
                    } else {//N比M大且j大于等于M时，只更新a矩阵
                        for (int k = 0; k < K; k++) {
                            int[] seq = trainList.get(k);
                            int Tk = seq.length;
                            logPk[k] = logForwardVector.calLogObSeqProb(seq);
                            logAlphaMatrix = logForwardVector.calLogForwardMatrix(seq);
                            logBetaMatrix = logBackwardVector.calLogBackwardMatrix(seq);
                            for (int t = 0; t < Tk - 1; t++) {
                                atemp = logAlphaMatrix[t][i] + logAMatrix[i][j]
                                        + logBMatrix[j][seq[t + 1]]
                                        + logBetaMatrix[t + 1][j] - logPk[k];
                                AtempArr.add(atemp);
                            }
                        }
                        newLogAMatrix[i][j] = JacobiMethod.JacobiCalSum(AtempArr) - log_denominator;
                    }
                } else {//N比M小
                    if (j < N) {//N比M小且j小于N时，ab矩阵同时进行更新
                        for (int k = 0; k < K; k++) {
                            int[] seq = trainList.get(k);
                            int Tk = seq.length;
                            logPk[k] = logForwardVector.calLogObSeqProb(seq);
                            logAlphaMatrix = logForwardVector.calLogForwardMatrix(seq);
                            logBetaMatrix = logBackwardVector.calLogBackwardMatrix(seq);
                            for (int t = 0; t < Tk - 1; t++) {
                                atemp = logAlphaMatrix[t][i] + logAMatrix[i][j]
                                        + logBMatrix[j][seq[t + 1]]
                                        + logBetaMatrix[t + 1][j] - logPk[k];
                                AtempArr.add(atemp);
                                if (seq[t] == j) {//B矩阵约束条件的判断
                                    btemp = logAlphaMatrix[t][i]
                                            + logBetaMatrix[t][i] - logPk[k];
                                    BtempArr.add(btemp);
                                }
                            }
                        }
                        double temp = JacobiMethod.JacobiCalSum(AtempArr);
                        double tttt = JacobiMethod.JacobiCalSum(BtempArr);
                        newLogAMatrix[i][j] = temp - log_denominator;
                        newLogBMatrix[i][j] = tttt - log_denominator;
                    } else {//N比M小且j大于等于N时，只更新b矩阵
                        for (int k = 0; k < K; k++) {
                            int[] seq = trainList.get(k);
                            int Tk = seq.length;
                            logPk[k] = logForwardVector.calLogObSeqProb(seq);
                            logAlphaMatrix = logForwardVector.calLogForwardMatrix(seq);
                            logBetaMatrix = logBackwardVector.calLogBackwardMatrix(seq);
                            for (int t = 0; t < Tk - 1; t++) {
                                if (seq[t] == j) {//B矩阵约束条件的判断
                                    btemp = logAlphaMatrix[t][i]
                                            + logBetaMatrix[t][i] - logPk[k];
                                    BtempArr.add(btemp);
                                }
                            }
                        }
                        newLogBMatrix[i][j] = JacobiMethod.JacobiCalSum(BtempArr) - log_denominator;
                    }
                }
            }
            //进行pi的更新
            ArrayList<Double> piArr = new ArrayList<Double>();
            for (int k = 0; k < K; k++) {
                int[] seq = trainList.get(k);
                int Tk = seq.length;
                logPk[k] = logForwardVector.calLogObSeqProb(seq);
                logAlphaMatrix = logForwardVector.calLogForwardMatrix(seq);
                logBetaMatrix = logBackwardVector.calLogBackwardMatrix(seq);
                piArr.add(logAlphaMatrix[0][i] + logBetaMatrix[0][i] - logPk[k]);
            }
            newLogPiVector[i] = JacobiMethod.JacobiCalSum(piArr) - Math.log(K);
        }
        //更新完毕，进行整体的变量替换
        this.logAMatrix = newLogAMatrix;
        this.logBMatrix = newLogBMatrix;
        this.logPiVector = newLogPiVector;
        updateHMM();
        if (isSmooth) {
            smoothHMM(0.0000000000001f);
        }
        System.out.println("训练完毕");
    }


    public HMModel getHMModel() {
        int MAX = N > M ? N : M;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < MAX; j++) {
                if (isN) {
                    this.aMatrix[i][j] = Math.pow(Math.E, logAMatrix[i][j]);
                    if (j < M) {
                        this.bMatrix[i][j] = Math.pow(Math.E, logBMatrix[i][j]);
                    }
                } else {
                    if (j < N) {
                        this.aMatrix[i][j] = Math.pow(Math.E, logAMatrix[i][j]);
                    }
                    this.bMatrix[i][j] = Math.pow(Math.E, logBMatrix[i][j]);
                }
            }
            piVector[i] = Math.pow(Math.E, logPiVector[i]);
        }
        return hmModel;//由于是值引用，所以不用new一个了
    }

    public void updateHMM() {
        int MAX = N > M ? N : M;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < MAX; j++) {
                if (isN) {
                    this.aMatrix[i][j] = Math.pow(Math.E, logAMatrix[i][j]);
                    if (j < M) {
                        this.bMatrix[i][j] = Math.pow(Math.E, logBMatrix[i][j]);
                    }
                } else {
                    if (j < N) {
                        this.aMatrix[i][j] = Math.pow(Math.E, logAMatrix[i][j]);
                    }
                    this.bMatrix[i][j] = Math.pow(Math.E, logBMatrix[i][j]);
                }
            }
            piVector[i] = Math.pow(Math.E, logPiVector[i]);
        }
        System.out.println("update done!");
    }

    private void smoothHMM(double lamda) {
        for (int i = 0; i < N; i++) {
            RandomUtils.LaplaceSmooth(aMatrix[i], lamda);
            RandomUtils.LaplaceSmooth(bMatrix[i], lamda);
        }
        RandomUtils.LaplaceSmooth(piVector, lamda);
        System.out.println("smooth done!");
    }
}
