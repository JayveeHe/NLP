package TrainSet;

import HMM.Utils.FileUtils;
import TrainSet.DataStructure.IndexResult;
import TrainSet.DataStructure.TrieTree;
import TrainSet.DataStructure.WordNode;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayvee on 2014/11/24.
 */
public class WordIndex {
    private TrieTree indexTree;
    public int indexSum = 0;

    public WordIndex(TrieTree indexTree) {
        this.indexTree = indexTree;
        this.indexSum = indexTree.word_list.size()+1;
    }


    /**
     * 获取包含索引的词汇树
     *
     * @return 生成以ID+1为freq的字典树，因为freq=0时程序会认为该结点不能组成词语
     * 加上未登陆词，总共的ID编号范围为0~wordlist.size()之间的闭区间
     */
    public static TrieTree getIDTree() {
        String NEWLINE = System.getProperty("line.separator");
        String str = FileUtils.File2str("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\myDict", "utf-8");
        String[] split = str.split(NEWLINE);
        TrieTree trieTree = new TrieTree();
        //统计词汇并自动去重
        for (String text : split) {
            String[] words = text.split("\t");
//            Matcher lineMatcher = Pattern.compile(" ").matcher(text);
//            if (lineMatcher.find()) {
            String word = words[0];
            trieTree.addWord(word);
//            System.out.println(word);
        }
        ArrayList<WordNode> wordlist = trieTree.getSortedList(TrieTree.downSortor);
        //生成以ID+1为freq的字典树，因为freq=0时程序会认为该结点不能组成词语
        //加上未登陆词，总共的ID编号范围为0~wordlist.size()之间的闭区间
        TrieTree idTree = new TrieTree();
        for (int i = 0; i < wordlist.size(); i++) {
            idTree.addWord(wordlist.get(i).getWord(), i + 1);
        }
        return idTree;
    }


    /**
     * 由句子转换为序号序列
     *
     * @param sentence
     * @return
     */
    public IndexResult Sentence2Index(String sentence) {
        //首先进行句子的分词处理
        List<Term> terms = ToAnalysis.parse(sentence);
        int T = terms.size();
//        IndexResult[] infos = new IndexResult[T];
        int[] seq = new int[T];
        String[] words = new String[T];
        //编号处理
        for (int i = 0; i < T; i++) {
            words[i] = terms.get(i).getName();
            WordNode wordNode = indexTree.getWordNode(terms.get(i).getName());
            if (null != wordNode) {
//                infos[i] = new IndexResult(wordNode.getWord(),wordNode.getFreq());
                seq[i] = wordNode.getFreq();
            } else {
                seq[i] = 0;//把0序号留给未发现词
//                infos[i] = new IndexResult(terms.get(i).getName(),0);
            }
        }
        return new IndexResult(words, seq);
    }


}