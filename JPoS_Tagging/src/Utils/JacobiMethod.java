package Utils;

import java.util.ArrayList;

/**
 * Created by Jayvee on 2014/12/4.
 */
public class JacobiMethod {
    /**
     * 求雅克比近似
     * 所求的是log（e^a+e^b）的值
     *
     * @param a
     * @param b
     * @return
     */
    public static double JacobiApprox(double a, double b) {
        double max = a > b ? a : b;
        double abs = Math.abs(a - b);
        double log = Math.log((1 + Math.pow(Math.E, -abs)));
        return max + log;
    }

    /**
     * 通过雅可比公式近似计算log求和
     *
     * @param plusItems 求和项的对数值，如e^a+e^b+e^c，则输入包含a,b,c的数组
     * @return
     */
    public static double JacobiRecursion(ArrayList<Double> plusItems) {
        double firstValue;
        if (plusItems.size() > 2) {
            firstValue = plusItems.remove(0);
            return JacobiApprox(firstValue, JacobiRecursion(plusItems));
        } else if (plusItems.size() == 2) {
            return JacobiApprox(plusItems.get(0), plusItems.get(1));
        } else if (plusItems.size() == 1) {
            return Math.log(plusItems.get(0));
        } else {
            return Double.NEGATIVE_INFINITY;

        }
    }

    public static double JacobiCalSum(ArrayList<Double> plusItems) {
        int LEN = plusItems.size();
        if (LEN > 2) {
            double[] temp = new double[LEN - 1];
            temp[0] = JacobiApprox(plusItems.get(0), plusItems.get(1));
            for (int i = 1; i < temp.length; i++) {
                temp[i] = JacobiApprox(temp[i - 1], plusItems.get(i + 1));
            }
            return temp[temp.length - 1];
        } else if (LEN == 2) {
            return JacobiApprox(plusItems.get(0), plusItems.get(1));
        } else if (LEN == 1) {
            return Math.log(plusItems.get(0));
        } else {
            return Double.NEGATIVE_INFINITY;
        }
    }

}
