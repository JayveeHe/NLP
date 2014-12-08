package Utils;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Jayvee on 2014/11/14.
 */
public class RandomUtils {
    /**
     * 给定一个double数组，为其随机分配符合概率约束的0到1之间的值
     *
     * @param toInitValues 给定数组，可空可不空
     * @return
     */
    public static void randomInitProb(double[] toInitValues) {
        int N = toInitValues.length;
        double probSum = 0;//已分配的概率和
        Random r = new Random(System.currentTimeMillis());
//        BigDecimal probSum_big = BigDecimal.valueOf(0);
//        probSum_big =probSum_big.setScale(30);
        for (int i = 0; i < N - 1; i++) {
            double temp = r.nextDouble() * (1 - probSum);
            toInitValues[((int) (r.nextDouble() * N))] += temp;
//            toInitValues[i] = temp;
            probSum += temp;
//            probSum_big =probSum_big.add(BigDecimal.valueOf(temp).setScale(30));
//            if (probSum == 1) {
//                break;
//            }
        }
        toInitValues[((int) (Math.random() * N))] += 1 - probSum;
//        toInitValues[((int) (r.nextDouble() * N))] += 1 - probSum_big.doubleValue();
//        toInitValues[N - 1] = 1 - probSum;
//        System.out.println(isStochastic(toInitValues));
    }


    /**
     * 平均分配概率
     *
     * @param toInitValues
     */
    public static void meansInitProb(double[] toInitValues) {
        int N = toInitValues.length;
        for (int i = 0; i < N; i++) {
            toInitValues[i] = (double) 1.0f / N;

        }
    }

    /**
     * 测试给定的值数组是否符合概率约束
     *
     * @param testProbs 给定值数组
     * @return true or false
     */
    public static boolean isStochastic(double[] testProbs) {
        double sum = 0;
        BigDecimal sum_big = BigDecimal.valueOf(0);
        for (int i = 0; i < testProbs.length; i++) {
//            if (testProbs[i] != 0) {
//            sum += testProbs[i];
            sum_big = sum_big.add(BigDecimal.valueOf(testProbs[i]));
//            }
        }
        double temp = sum_big.doubleValue() - 1;
        if (temp == 0) {
            return true;
        } else {
            temp = Math.log10(temp * temp);
            return temp < -29;
        }
//        return sum_big.doubleValue() == 1.0f;
    }

    /**
     * 对一组概率值进行拉普拉斯平滑
     *
     * @param prob  概率数组
     * @param lamda 平滑参数，介于0到1之间
     */
    public static void LaplaceSmooth(double[] prob, double lamda) {
        double K = prob.length * lamda;
        for (int i = 0; i < prob.length; i++) {
            prob[i] = (prob[i] + lamda) / (1 + K);
        }
    }

}
