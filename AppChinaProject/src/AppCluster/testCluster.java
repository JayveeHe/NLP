package AppCluster;

import ClusterUtils.APCluster;
import ClusterUtils.BasicUtils;
import ClusterUtils.IClusterCalculable;

import java.io.IOException;

/**
 * Created by ITTC-Jayvee on 2015/4/9.
 */
public class testCluster {
    public static void main(String a[]) {
        AppNode[] appNodes = AppNode.getAppNodesByFile("AppChinaProject/data/appintro_Vec_50_data.txt",
                "AppChinaProject/data/appintro_Vec_50_name.txt");
        System.out.println(appNodes.length);
        APCluster apCluster = new APCluster(appNodes, 1, 0.6f, 100);
        APCluster[][] results = (APCluster[][]) apCluster.startCluster();
        try {
            BasicUtils.saveResults((IClusterCalculable[][]) results, "AppChinaProject/data/clusterResult" + System.currentTimeMillis() + ".csv", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
