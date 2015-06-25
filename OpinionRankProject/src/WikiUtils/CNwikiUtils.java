package WikiUtils;

import Utils.FileUtils;
import org.nlpcn.commons.lang.jianfan.JianFan;

import java.io.File;

/**
 * Created by Jayvee on 2015/6/25.
 */
public class CNwikiUtils {
    public static void loadCNwikiData() {

        File root = new File("D:\\CS\\NLPtrainset\\wikidata");
        for (File txt :root.listFiles()){
            String str = FileUtils.File2str(txt.getPath(),"utf-8");
            String jstr = JianFan.f2J(str);
            System.out.println(jstr);
        }
    }
    public static void main(String a[]){
        loadCNwikiData();
    }
}
