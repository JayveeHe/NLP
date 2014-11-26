package Tests;

import HMM.Utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jayvee on 2014/11/26.
 */
public class preProcessDict {
    public static void main(String []a){
        //读取训练序列
        File dirRoot = new File("D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile");
        ArrayList<int[]> trainList = new ArrayList<int[]>();
//        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        for (File classFile : dirRoot.listFiles()) {
//            for (File txtFile : classFile.listFiles()) {
            File[] files = classFile.listFiles();
            for (int i = 0; i < 1; i++) {
                File txtFile = files[i];
                String txt = FileUtils.File2str(txtFile.getPath(), "utf-8");
            }
        }
        System.out.println("完成训练序列的读取");
    }
}
