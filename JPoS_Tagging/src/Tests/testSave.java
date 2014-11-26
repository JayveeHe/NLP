package Tests;

import HMM.BasicModel.HMModel;

/**
 * Created by Jayvee on 2014/11/24.
 */
public class testSave {
    public static void main(String[] a) {
        HMModel hmModel = new HMModel(40, 50000);
        hmModel.saveModel("Tests");
//        System.out.println(hmModel);
        HMModel hmModel1 = new HMModel("D:\\CS\\Git\\NLP\\JPoS_Tagging\\data\\Tests");
//        System.out.println(hmModel1);
    }
}
