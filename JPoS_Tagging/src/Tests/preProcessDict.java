package Tests;

import Utils.FileUtils;
import TrainSet.DataStructure.TrieTree;
import TrainSet.DataStructure.WordNode;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayvee on 2014/11/26.
 */
public class preProcessDict {
    public static void main(String[] a) {
        File dirRoot = new File("D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile");
        TrieTree wordTree = new TrieTree();
        String NEWLINE = System.getProperty("line.separator");
//        ArrayList<int[]> trainList = new ArrayList<int[]>();
//        WordIndex wordIndex = new WordIndex(WordIndex.getIDTree());
        for (File classFile : dirRoot.listFiles()) {
//            for (File txtFile : classFile.listFiles()) {
            File[] files = classFile.listFiles();
            for (int i = 0; i < 5; i++) {
                File txtFile = files[i];
                String txt = FileUtils.File2str(txtFile.getPath(), "gbk");
                List<Term> terms = ToAnalysis.parse(txt);
                new NatureRecognition(terms).recognition();
                for (Term term : terms) {
                    wordTree.addWord(term.getName(), term.getNatureStr());
                }
            }
        }
//        new
        ArrayList<WordNode> sortedList = wordTree.getSortedList(TrieTree.downSortor);
        String dirpath = new File("").getAbsolutePath();
        File file = new File(dirpath + "/JPoS_Tagging/data/" + "myDict");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (WordNode wn : sortedList) {
            try {
                if (fos != null) {
                    String nature = wn.getNature();
                    if (!nature.equals("null") && !nature.equals("m") && !nature.equals("nw")) {
                        fos.write((wn.getWord() + "\t" + wn.getNature() + NEWLINE).getBytes("utf-8"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("完成词典读取，路径：" + file.getAbsolutePath());
    }
}
