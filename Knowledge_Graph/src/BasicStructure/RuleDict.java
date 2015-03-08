package BasicStructure;


import FudanNLP.FudanUtils;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.fnlp.nlp.parser.dep.DependencyTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jayvee on 2015/2/28.
 */
public class RuleDict {
    private static final int ITER_MAX = 10;
    public Map<String, WordMark> wordDict = new HashMap<String, WordMark>(0);
    public List<Rule> ruleList = new ArrayList<Rule>(0);

    public void addWord(String word, String mark) {
        WordMark wordMark = new WordMark(word, mark);
        wordDict.put(word, wordMark);
    }

    /**
     * 添加规则
     *
     * @param coreWord 触发词
     * @param relation 触发词所连接的关系
     * @param isGov    触发词是否为支配词
     */
    public void addRule(String coreWord, String relation, String targetNature, boolean isGov, String mark) {
        ruleList.add(new Rule(coreWord, relation, targetNature, isGov, mark));
    }

    public void analysis(DataManager dataManager) {
//        for (int iter = 0; iter < ITER_MAX; iter++) {
        int dictCount = ruleList.size();
        do {//每一次迭代对ruledict的所有规则进行查询
            for (int i = 0; i < dictCount; i++) {
                Rule rule = ruleList.get(i);
                String coreWord = rule.getCoreWord();
                List<String> sentences = dataManager.qury(coreWord);
                if (sentences != null) {
                    for (String singleSentence : sentences) {
                        DependencyTree dt = FudanUtils.parseText(singleSentence);
//                        for()
                        List<Term> terms = NlpAnalysis.parse(singleSentence);
                        ArrayList<List<String>> lists = dt.toList();
                        if (rule.isGov()) {
                            //如果触发词是支配词，则可以直接查找
                            for (int j = 0; j < lists.size(); j++) {
                                List<String> info = lists.get(j);
                                if (info.get(0).equals(coreWord)
                                        && info.get(3).equals(rule.getRelation())
                                        && terms.get(j).getNatureStr().equals(rule.getTargetNature())
                                        ) {
                                    int toIndex = Integer.valueOf(info.get(2));
                                    WordMark wordMark = new WordMark(lists.get(toIndex).get(0),
                                            rule.getMark());
                                    wordDict.put(lists.get(toIndex).get(0), wordMark);
                                }
                            }
                        } else {
                            //如果不是支配词，则遍历查找所有指向该点的词
                            for (int j = 0; j < lists.size(); j++) {
                                List<String> info = lists.get(j);
                                if (
                                        terms.get(j).getNatureStr().equals(rule.getTargetNature()) &&
                                                info.get(3).equals(rule.getRelation())) {
                                    String target = lists.get(Integer.valueOf(info.get(2))).get(0);
                                    if (target.equals(rule.getCoreWord())) {
                                        WordMark wordMark = new WordMark(info.get(0), rule.getMark());
                                        wordDict.put(info.get(0), wordMark);
                                    }

                                }
                            }
                        }
                    }
                }
//                System.out.println("第" + i + "个句子处理完毕");
            }
        } while (dictCount < ruleList.size());
//        }
        System.out.println(wordDict.values());
    }


    public void printDict() {
        for (WordMark wm : wordDict.values()) {

        }
    }


    private class WordMark {
        private final String word;
        private final String mark;

        public WordMark(String word, String mark) {

            this.word = word;
            this.mark = mark;
        }

        public String getWord() {
            return word;
        }

        public String getMark() {
            return mark;
        }

        @Override
        public String toString() {
            return "实体:" + word + "\t类别：" + mark;
        }
    }

    private class Rule {
        private final String coreWord;
        private final String relation;
        private final String targetNature;
        private final boolean isGov;
        private final String mark;

        public Rule(String coreWord, String relation, String targetNature, boolean isGov, String mark) {

            this.coreWord = coreWord;
            this.relation = relation;
            this.targetNature = targetNature;
            this.isGov = isGov;
            this.mark = mark;
        }

        public String getRelation() {
            return relation;
        }

        public String getCoreWord() {
            return coreWord;
        }

        public boolean isGov() {
            return isGov;
        }

        public String getMark() {
            return mark;
        }

        public String getTargetNature() {
            return targetNature;
        }
    }
}



