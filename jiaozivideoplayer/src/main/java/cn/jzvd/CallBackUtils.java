package cn.jzvd;

/**
 * Created by Witness on 2020/3/5
 * Describe:
 */
public class CallBackUtils {
    private static BuyCallBack mCallBack;

    public static void setCallBack(BuyCallBack callBack) {
        mCallBack = callBack;
    }

    public static void doCallBackMethod(String url){
        mCallBack.goToBuy(url);
    }

}
