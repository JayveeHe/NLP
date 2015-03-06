package BasicStructure;

import edu.stanford.nlp.trees.TypedDependency;

import java.util.ArrayList;
import java.util.Map;

/**
 * 自定义的句子级结点
 * Created by ITTC-Jayvee on 2015/3/6.
 */
public class SentenceNode {
    String sentenceText;
    private Map<String, Map<String, ArrayList<TypedDependency>>> GovMap;//以支配词作为key的map

    private Map<String, Map<String, ArrayList<TypedDependency>>> DepdMap;//以被支配词作为key的map

    private Map<String, ArrayList<TypedDependency>> ParseMap;//以关系描述词作为key的map
    private ParserNode ParseTree;//该句子的解析树

    public SentenceNode(String text){
        this.sentenceText = text;
        
    }
}
