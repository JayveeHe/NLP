package HMM.BasicModel;


import Utils.FileUtils;
import Utils.RandomUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

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
            RandomUtils.LaplaceSmooth(AMatrix[i],0.0000001f);
            RandomUtils.randomInitProb(BMatrix[i]);
            RandomUtils.LaplaceSmooth(BMatrix[i],0.0000001f);
//            RandomUtils.meansInitProb(AMatrix[i]);
//            RandomUtils.meansInitProb(BMatrix[i]);
        }
        RandomUtils.meansInitProb(piVector);
    }


    /**
     * 根据给定参数进行模型构造
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
     * 从文件中读取模型参数，并返回模型实例
     *
     * @param filepath 文件的路径
     */
    public HMModel(String filepath) {
        String text = FileUtils.File2str(filepath, "utf-8");
        JSONTokener tokener = new JSONTokener(text);
        try {
            JSONObject root = (JSONObject) tokener.nextValue();
            this.N = root.getInt("N");
            this.M = root.getInt("M");
            this.AMatrix = new double[N][N];
            this.BMatrix = new double[N][M];
            this.piVector = new double[N];
            JSONArray aMatrixArry = root.getJSONArray("AMatrix");
            JSONArray bMatrixArry = root.getJSONArray("BMatrix");
            JSONArray piVectorArry = root.getJSONArray("piVector");
            for (int i = 0; i < N; i++) {
                JSONArray aMatrixVec = aMatrixArry.getJSONArray(i);
                JSONArray bMatrixVec = bMatrixArry.getJSONArray(i);
                if (N > M) {
                    for (int j = 0; j < N; j++) {
                        this.AMatrix[i][j] = aMatrixVec.getDouble(j);
                        if (j < M) {
                            this.BMatrix[i][j] = bMatrixVec.getDouble(j);
                        }
                    }
                } else {
                    for (int j = 0; j < M; j++) {
                        if (j < N) {
                            this.AMatrix[i][j] = aMatrixVec.getDouble(j);
                        }
                        this.BMatrix[i][j] = bMatrixVec.getDouble(j);
                    }
                }
                this.piVector[i] = piVectorArry.getDouble(i);
            }
            System.out.println("从文件读取模型成功");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    /**
     * 将模型参数以JSON格式保存到文件，输出路径为工程文件夹下的data文件夹
     *
     * @param filename 文件名
     */
    public void saveModel(String filename) {
        JSONObject root = new JSONObject();
        try {
            root.put("N", N);
            root.put("M", M);
            JSONArray aMatrixArry = new JSONArray();
            JSONArray bMatrixArry = new JSONArray();
            JSONArray piVectorArry = new JSONArray();
            for (int i = 0; i < N; i++) {
                JSONArray aMatrixVec = new JSONArray();
                JSONArray bMatrixVec = new JSONArray();
                if (N > M) {
                    for (int j = 0; j < N; j++) {
                        aMatrixVec.put(AMatrix[i][j]);
                        if (j < M) {
                            bMatrixVec.put(BMatrix[i][j]);
                        }
                    }
                } else {
                    for (int j = 0; j < M; j++) {
                        if (j < N) {
                            aMatrixVec.put(AMatrix[i][j]);
                        }
                        bMatrixVec.put(BMatrix[i][j]);
                    }
                }
                aMatrixArry.put(aMatrixVec);
                bMatrixArry.put(bMatrixVec);
                piVectorArry.put(piVector[i]);
            }
            root.put("AMatrix", aMatrixArry);
            root.put("BMatrix", bMatrixArry);
            root.put("piVector", piVectorArry);

            //写入文件
//            File dir = new File("");
            String dirpath = new File("").getAbsolutePath();
            File file = new File(dirpath + "/JPoS_Tagging/data/" + filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(root.toString().getBytes("utf-8"));
            System.out.println("模型保存完毕！路径：" + file.getAbsolutePath());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
