package BasicStructure;

import edu.stanford.nlp.trees.TypedDependency;

import java.util.*;

/**
 * Created by Jayvee on 2015/3/5.
 */
public class ParserManager {

    public Map<String, Map<String, ArrayList<TypedDependency>>> GovMap;//以支配词作为key的map
    public Map<String, Map<String, ArrayList<TypedDependency>>> DepdMap;//以被支配词作为key的map


    /**
     * 根据依赖关系列表创建Map
     *
     * @param tds
     */
    public Map<String, ArrayList<TypedDependency>> buildParserMapByTDs(Collection<TypedDependency> tds) {
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
            //govmap的填充
            String govword = td.gov().value();
            if (GovMap.get(govword) != null) {
//                Map<String >
//                GovMap.put(govword,)
            }
        }
        return rlMap;
    }

    /**
     * 根据stanford句法分析给出的字符串结果建立一个树
     *
     * @param text
     * @return
     */
    public static ParserNode buildTree(String text) {
        Stack<ParserNode> taskStack = new Stack<ParserNode>();
        ParserNode ROOT = null;
        int i = 0;
        char[] temp = new char[0];
        int count = 0;
        while (i < text.length()) {
            char ch = text.charAt(i);
            switch (ch) {
                case '(': {
                    if (text.charAt(i + 1) >= 'A' && text.charAt(i + 1) <= 'Z') {
                        temp = new char[10];
                        count = 0;
                        char[] newch = new char[10];
                        int j = 0;
                        char chtemp = text.charAt(++i);
                        while (chtemp != ' ') {//提取出的语素描述词
                            newch[j++] = chtemp;
                            chtemp = text.charAt(++i);
                        }
                        String dscr = String.valueOf(newch, 0, j);
                        if (taskStack.size() > 0) {
                            ParserNode newnode = new ParserNode(dscr, taskStack.peek());//待填充的node
                            taskStack.peek().addChild(newnode);
                            taskStack.push(newnode);//将最新的node压入栈中                  }
                        } else {
                            taskStack.push(new ParserNode());
                            ROOT = taskStack.peek();
                        }
                    } else {
                        //否则就认为这个‘(’是一个word
//                        taskStack.peek().setWord(String.valueOf(text.charAt(++i)));
                        temp[count++] = '(';
                        taskStack.peek().setLeaf(true);
                    }
                    i++;
                }
                break;
                case ')': {
                    ParserNode peek = taskStack.peek();
                    if (!peek.isLeaf() && peek.getChilds().size() == 0) {
                        //则说明这个"）"是一个word
//                        peek.setWord(String.valueOf(')'));
                        temp[count++] = ')';
                        peek.setLeaf(true);
                    } else {//正常结束
                        ParserNode pop = taskStack.pop();
                        if (pop.isLeaf()) {
                            pop.setWord(String.valueOf(temp, 0, count));
                        }
//                        pop.setLeaf(true);
                    }
                    i++;
                }
                break;
                case ' ':
                    i++;
                    break;
                default: {//默认情况下就是获取一个节点的word
                    temp[count] = ch;
                    count++;
                    i++;
                    taskStack.peek().setLeaf(true);
                }
            }
        }
        return ROOT;
    }
}
