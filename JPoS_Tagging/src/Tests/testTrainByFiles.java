package Tests;

import HMM.BasicModel.HMModel;
import HMM.BasicModel.SigmaVector;
import HMM.Baum_Welch.ForwardBackwardAlog;
import Utils.FileUtils;
import TrainSet.WordIndex;
import org.nlpcn.commons.lang.standardization.SentencesUtil;

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
        SentencesUtil sentencesUtil = new SentencesUtil();
        for (File classFile : dirRoot.listFiles()) {
//            for (File txtFile : classFile.listFiles()) {
            File[] files = classFile.listFiles();
            for (int i = 0; i < 5; i++) {//每一类读取多少个文件
                File txtFile = files[i];
                String txt = FileUtils.File2str(txtFile.getPath(), "gbk");
                for (String s : sentencesUtil.toSentenceList(txt)) {
                    int[] index = wordIndex.Sentence2Index(s).getIndex();
                    trainList.add(index);
                }
            }
        }
        System.out.println("完成训练序列的读取");
        int M = wordIndex.indexSum;
        //进行多观察序列的模型训练
        HMModel hmModel = new HMModel(12, M);
        System.out.println("完成模型初始化");
        System.out.println("训练句子总数："+trainList.size());
        SigmaVector[] sigmaVectors = new SigmaVector[trainList.size()];
        for(int k = 0;k<trainList.size();k++){
            sigmaVectors[k] = new SigmaVector(hmModel,trainList.get(k),true);
        }
        ForwardBackwardAlog fba = new ForwardBackwardAlog(hmModel);
        for (int i = 0; i < 50; i++) {
            System.out.println("进行第" + i + "次训练");
//            fba.TrainByMultiObseq(trainList,false ,sigmaVectors);
            fba.TrainByMultiObseq(trainList, true);
            hmModel.saveModel("hmmData-"+i);
        }
//        fba.Train(trainList, true, 10, true);

    }
}