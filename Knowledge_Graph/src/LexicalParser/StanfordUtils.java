package LexicalParser;

import BasicStructure.ParserNode;
import BasicStructure.ParserTree;
import Utils.FileUtils;
import Utils.IDFCaculator;
import Utils.WordNode;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseGrammaticalStructure;
import edu.stanford.nlp.trees.international.pennchinese.ChineseUtils;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import edu.stanford.nlp.trees.*;
import org.nlpcn.commons.lang.standardization.SentencesUtil;

import java.util.*;

/**
 * Created by Jayvee on 2015/2/12.
 */
public class StanfordUtils {
    static String[] options = {"-MAX_ITEMS", "200000000"};
    static LexicalizedParser lp;

    static {
        String grammar = "Knowledge_Graph/data/chineseFactored.ser.gz";
        String[] options = {"-maxLength", "50", "-MAX_ITEMS", "500000"};
        lp = LexicalizedParser.loadModel(grammar, options);
    }

    public static Tree parseChinese(String text) {
        List<Term> terms = NlpAnalysis.parse(text);
        List<String> lss = new ArrayList<String>();
        for (Term term : terms) {
            lss.add(term.getName());
        }
        Tree parseTree = lp.parseStrings(lss);
        return parseTree;
//        System.out.println(parseTree);
//        ChineseTreebankLanguagePack tlp = new ChineseTreebankLanguagePack();
//        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
//        ChineseGrammaticalStructure gs = new ChineseGrammaticalStructure(parseTree);
//        Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();
//        System.out.println(parseTree.yield());
//        parseTree.pennPrint();
//        Collection<TypedDependency> typedDependencies = gs.typedDependenciesCollapsedTree();
//        for (TypedDependency td : tdl) {
//            System.out.println(td.reln().getLongName()+td);
//        }

//        gs.getGrammaticalRelation(ChineseGrammaticalRelations.NOUN_MODIFIER, )
//        GrammaticalStructure structure = gsf.newGrammaticalStructure(parseTree);
//        System.out.println(typedDependencies);
//        return tdl;
    }

    public static void main(String a[]) {
//        String text = "何太冲是崆峒派的掌门人。";\
        SentencesUtil su = new SentencesUtil();
        String text = FileUtils.File2str("Knowledge_Graph/data/倚天屠龙记.txt", "utf-8");
        List<String> sentenceList = su.toSentenceList(text);
        Map<String, ArrayList<TypedDependency>> totalMap = new HashMap<String, ArrayList<TypedDependency>>(1);
        IDFCaculator idfCaculator = new IDFCaculator("Knowledge_Graph/data/IDF值.txt");
        ArrayList<WordNode> wordNodes = idfCaculator.CalTFIDF(text);
        for (int i = 0; i < (1000<wordNodes.size()?1000:wordNodes.size()); i++) {
            WordNode wn = wordNodes.get(i);
            if (wn.getNature().contains("n")) {
//            if (wn.getNature().equals("n")||wn.getNature().equals("nw")) {
//            WordNode wn = wordNodes.get(i);
            System.out.println(wn.getWord() + "\ttfidf=" + wn.tfidf + "\tnature=" + wn.getNature());
            }
        }
        ArrayList<ParserNode> totalROOT = new ArrayList<ParserNode>(0);
        for (int i = 0; i < (100 < sentenceList.size() ? 100 : sentenceList.size()); i++) {
            String sentence = sentenceList.get(i);
            System.out.println("正在处理第" + i + "个句子\n" + sentence);
            Tree parseTree = parseChinese(sentence);
            ParserNode root = ParserNode.buildTree(parseTree.toString());
            totalROOT.add(root);
            ChineseGrammaticalStructure gs = new ChineseGrammaticalStructure(parseTree);
            Collection<TypedDependency> tds = gs.typedDependenciesCollapsed();
            Map<String, ArrayList<TypedDependency>> parserMapByTDs = buildParserMapByTDs(tds);
            //进行关系map的合并
            for (String rl : parserMapByTDs.keySet()) {
                if (totalMap.get(rl) != null) {
                    for (TypedDependency td : parserMapByTDs.get(rl)) {
                        totalMap.get(rl).add(td);
                    }
                } else {
                    totalMap.put(rl, parserMapByTDs.get(rl));
                }
//                totalMap.putAll(parserMapByTDs);

            }
//            if (parserMapByTDs.get("nn") != null) {
//                System.out.println(parserMapByTDs.get("nn").get(0).gov().pennString());
//            }
        }
        System.out.println(totalMap.size()+"\t"+totalROOT.size());

//        System.out.println(parserMapByTDs);
//        System.out.println(parserMapByTDs.get("root").get(0).gov().pennString());
    }

    /**
     * 根据依赖关系列表创建树
     *
     * @param tds
     */
    public static Map<String, ArrayList<TypedDependency>> buildParserMapByTDs(Collection<TypedDependency> tds) {
        Map<String, ArrayList<TypedDependency>> rlMap = new HashMap<String, ArrayList<TypedDependency>>(1);

        for (TypedDependency td : tds) {
            String longname = td.reln().getLongName();
            String shortname = td.reln().getShortName();
            if (rlMap.get(shortname) != null) {
                rlMap.get(shortname).add(td);
            } else {
                ArrayList<TypedDependency> arrayList = new ArrayList<TypedDependency>(1);
                arrayList.add(td);
                rlMap.put(shortname, arrayList);
            }
        }
        return rlMap;
    }

}


