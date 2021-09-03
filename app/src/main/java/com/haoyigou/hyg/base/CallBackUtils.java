package com.haoyigou.hyg.base;

/**
 * Created by Witness on 2020/3/5
 * Describe:
 */
public class CallBackUtils {

    private static CallBack mCallBack;


    public static void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public static void doCallBackMethod(){
        String info = "";
        mCallBack.goToBuy(info);
    }

}
