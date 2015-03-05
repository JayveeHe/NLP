package BasicStructure;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Jayvee on 2015/2/28.
 */
public class ParserNode {
    String type;
    String word;
    ParserNode parent;
    ArrayList<ParserNode> childs = new ArrayList<ParserNode>(0);
    boolean isLeaf;

    public ParserNode(String type, String word, ParserNode parent) {
        this.type = type;
        this.word = word;
        this.parent = parent;
        this.isLeaf = true;

    }

    public ParserNode(String type, ParserNode parent) {
        this.type = type;

        this.parent = parent;
        this.word = null;
        this.isLeaf = false;
    }

    public ParserNode() {
        this.type = "ROOT";
        this.word = null;
        this.isLeaf = false;
        this.parent = null;
    }

    public void addChild(ParserNode childNode) {
        childs.add(childNode);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ParserNode getParent() {
        return parent;
    }


    public ArrayList<ParserNode> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ParserNode> childs) {
        this.childs = childs;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

}
