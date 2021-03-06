package Word2VecUtils;

/**
 * Created by Jayvee on 2015/6/1.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import ClusterUtils.BasicUtils;
import ClusterUtils.IClusterCalculable;
import ClusterUtils.KMeansCluster;
import Utils.FileUtils;
import Word2VecUtils.vec.Learn;
import Word2VecUtils.vec.Word2VEC;
import Word2VecUtils.vec.domain.WordEntry;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.json.JSONException;
import org.json.JSONObject;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;


public class demo {
//    private static final File sportCorpusFile = new File("D:\\CS\\Git\\NLP\\AppChinaProject\\data\\word2vecData\\word2vec_resultSanguo.txt");

    public static void main(String[] args) throws IOException {
//        File dirfile = new File("D:\\CS\\Git\\NLP\\AppChinaProject\\data\\sanguo");
//        File[] files = dirfile.listFiles();
//
//        构建语料
//        try (FileOutputStream fos = new FileOutputStream(sportCorpusFile)) {
//            int i = 0;
//            for (File classfile : files) {
//                for (File file : classfile.listFiles()) {
//                    System.out.println(i++);
//                    if (file.canRead() && file.getName().endsWith(".txt")) {
////                    parserFile(fos, file);
//                        String str = FileUtils.File2str(file.getPath(), "gbk");
//                        List<Term> terms = NlpAnalysis.parse(str);
//                        StringBuilder sb = new StringBuilder();
//                        for (Term term : terms) {
//                            sb.append(term.getName() + " ");
//                        }
//                        fos.write(sb.toString().getBytes("gbk"));
//                    }
//                }
//            }
//        }
//
//        try (FileOutputStream fos = new FileOutputStream(sportCorpusFile)) {
//            int i = 0;
////            for (File classfile : files) {
//                for (File file : files) {
//                    System.out.println(i++);
//                    if (file.canRead() && file.getName().endsWith(".txt")) {
////                    parserFile(fos, file);
//                        String str = FileUtils.File2str(file.getPath(), "utf-8");
//                        List<Term> terms = NlpAnalysis.parse(str);
//                        StringBuilder sb = new StringBuilder();
//                        for (Term term : terms) {
//                            sb.append(term.getName() + " ");
//                        }
//                        fos.write(sb.toString().getBytes("gbk"));
//                    }
////                }
//            }
//        }


////
////        //进行分词训练
////
//        Learn lean = new Learn() ;
//
//        lean.learnFile(sportCorpusFile) ;
//
//        lean.saveModel(new File("D:\\CS\\Git\\NLP\\AppChinaProject\\data\\word2vecData\\vectorSanguo.mod")) ;


        //加载测试

        Word2VEC w2v = new Word2VEC();
        w2v.loadJavaModel("D:\\CS\\Git\\NLP\\AppChinaProject\\data\\word2vecData\\vectorSanguo.mod");

//        float[] vector = w2v.getWordVector("CBA");
//        System.out.println(w2v.analogy("球队","投篮","CBA"));
//        System.out.println(Arrays.toString(vector));
//        String qury = "喜欢";
//        FileOutputStream fos = new FileOutputStream(
//                new File("D:\\CS\\Git\\NLP\\AppChinaProject\\data\\word2vecData\\" + qury + ".csv"));
////        fos.write("\\xEF\\xBB\\xBF".getBytes());
//        fos.write("词汇,距离\n".getBytes());
//        for (WordEntry we : w2v.distance(qury)) {
//            System.out.println(we.name + "\t" + we.score);
//            fos.write((we.name + "," + we.score + "\n").getBytes());
//        }
//        String sent1 = "我喜欢吃西瓜";
//        String sent2 = "他比较讨厌吃西瓜";
//        double cosDist = BasicUtils.calCosDist(sent2vec(w2v, sent1), sent2vec(w2v, sent2));
//        System.out.println(cosDist);
//        String quryword= "喜欢";
//        Set<WordEntry> wordEntries = w2v.distance(quryword);
//        System.out.println(quryword+"===============");
//        for (WordEntry we : wordEntries) {
//            System.out.println(we.name + "\t" + we.score);
//        }
//
//        quryword= "讨厌";
//        wordEntries = w2v.distance(quryword);
//        System.out.println(quryword+"==============");
//        for (WordEntry we : wordEntries) {
//            System.out.println(we.name + "\t" + we.score);
//        }

        wordCluster(w2v,20,20);

    }

    public static void wordCluster(Word2VEC w2v, int iterNum, int clusterNum) {
        KMeansCluster kmc = new KMeansCluster(clusterNum, iterNum);
        HashMap<String, float[]> wordMap = w2v.getWordMap();
        //build word cluster nodes
        int wordCount = wordMap.size();
        System.out.println(wordCount);
        WordClusterNode[] dataset = new WordClusterNode[wordCount];
        int i = 0;
        for (String key : wordMap.keySet()) {
            float[] vec = wordMap.get(key);
            dataset[i] = new WordClusterNode(key, i, vec);
            i++;
        }
        IClusterCalculable[][] result = kmc.kmeans(dataset, true);
        try {
            BasicUtils.saveResults(result, "kmeansResult.csv", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static float[] sent2vec(Word2VEC w2v, String sent) {
        List<Term> parse = NlpAnalysis.parse(sent);
        float[] result = new float[w2v.getWordVector("苏宁").length];
        for (Term t : parse) {
            float[] wordVector = w2v.getWordVector(t.getName());
            if (wordVector != null) {
                for (int i = 0; i < wordVector.length; i++) {
                    float val = wordVector[i];
                    result[i] += val;
                }
            }
        }
        return result;
    }


    private static void parseStr(FileOutputStream fos, String title) throws IOException {
        List<Term> parse2 = ToAnalysis.parse(title);
        StringBuilder sb = new StringBuilder();
        for (Term term : parse2) {
            sb.append(term.getName());
            sb.append(" ");
        }
        fos.write(sb.toString().getBytes());
        fos.write("\n".getBytes());
    }
}

