package Tests;

import HMM.BasicModel.HMModel;
import HMM.Baum_Welch.ForwardBackwardAlog;
import HMM.Utils.FileUtils;
import TrainSet.WordIndex;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ITTC-Jayvee on 2014/11/21.
 */
public class testTrainByFiles {
    public static void main(String a[]) {
        //读取训练序列
        File dirRoot = new File("D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile");
        ArrayList<int[]> trainList = new ArrayList<int[]>();
        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        for (File classFile : dirRoot.listFiles()) {
//            for (File txtFile : classFile.listFiles()) {
            File[] files = classFile.listFiles();
            for (int i = 0; i < 10; i++) {
                File txtFile = files[i];
                String txt = FileUtils.File2str(txtFile.getPath(), "utf-8");
                int[] index = wordIndex.Sentence2Index(txt).getIndex();
                trainList.add(index);
            }
        }
        System.out.println("完成训练序列的读取");
        int M = wordIndex.indexSum;
        //进行多观察序列的模型训练
        HMModel hmModel = new HMModel(12, M);
        System.out.println("完成模型初始化");
        ForwardBackwardAlog fba = new ForwardBackwardAlog(hmModel);
        for (int i = 0; i < 10; i++) {
            System.out.println("进行第" + i + "次训练");
            fba.TrainByMultiObseq(trainList, false);
            hmModel.saveModel("hmmData");
        }

    }
}