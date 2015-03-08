package BasicStructure;

import LexicalParser.StanfordUtils;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseGrammaticalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的句子级结点
 * Created by ITTC-Jayvee on 2015/3/6.
 */
public class SentenceNode {
    String sentenceText;
    public Map<String, Map<String, ArrayList<TypedDependency>>> GovMap;//以支配词作为key的map

    public Map<String, Map<String, ArrayList<TypedDependency>>> DepdMap;//以被支配词作为key的map

    public Map<String, ArrayList<TypedDependency>> ParseMap;//以关系描述词作为key的map
    public ParserNode ParseTree;//该句子的解析树

    public SentenceNode(String text) {
        this.sentenceText = text;
        this.GovMap = new HashMap<String, Map<String, ArrayList<TypedDependency>>>(0);
        this.DepdMap = new HashMap<String, Map<String, ArrayList<TypedDependency>>>(0);
        Tree tree = StanfordUtils.parseChinese(text);
        this.ParseTree = ParserManager.buildParseTree(tree.toString());
        ChineseGrammaticalStructure gs = new ChineseGrammaticalStructure(tree);
        Collection<TypedDependency> tds = gs.typedDependenciesCollapsed();
        this.ParseMap = ParserManager.buildDependencyMap(tds, GovMap, DepdMap);
    }
}
