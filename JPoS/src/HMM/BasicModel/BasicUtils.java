package HMM.BasicModel;

/**
 * Created by Jayvee on 2014/11/14.
 */
public class BasicUtils {
    /**
     * 给定一个double数组，为其随机分配符合概率约束的0到1之间的值
     *
     * @param toInitValues 给定数组，可空可不空
     * @return
     */
    public static void randomInitProb(double[] toInitValues) {
        int N = toInitValues.length;
        double probSum = 0;//已分配的概率和
        for (int i = 0; i < N - 1; i++) {
            toInitValues[i] = Math.random() * (1 - probSum);
            probSum += toInitValues[i];
        }
        toInitValues[N - 1] = 1 - probSum;
    }

    /**
     * 测试给定的值数组是否符合概率约束
     *
     * @param testProbs 给定值数组
     * @return true or false
     */
    public static boolean isStochastic(double[] testProbs) {
        double sum = 0;
        for (int i = 0; i < testProbs.length; i++) {
            sum += testProbs[i];
        }
        return sum == 1;
    }

}
