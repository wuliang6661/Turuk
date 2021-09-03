package com.haoyigou.hyg.config;

import com.haoyigou.hyg.entity.ExchangeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class StateMessage {

    /**
     * 判断是否登陆状态 （默认为未登录）
     */
    public static boolean IS_LOGIN = false;

    /**
     * 保存固定状态判断是登录页面是否需要返回键
     */
    public static boolean isLoginMethod = false;

    /***
     * 判断是否广告页进入
     */
    public static boolean isGDMessahe = false;

    /**
     * 是否有新消息(默认没有)
     */
    public static String haveMes = "0";

    /**
     * 新消息数目，用于角标显示
     */
    public static int badgeNum = 0;

    /**
     * 购物车是否显示返回
     */
    public static boolean isShopCarFinnish = false;

    /**
     * 购物车返回的商品页面
     */
    public static String url;

    /**
     * 判断是否是充值页面进入
     */
    public static int isVoucher = -1;

    /**
     * 从拼团进入的
     */
    public static boolean isPinTuan = false;
    public static String PinTuanUrl;
    public static String pinOrderNum;

    public static final String DOWNLOAD_PATH = "/data/data/com.haoyigou.hyg/download";


    public static String activityTitle = "";//是否有充话费送红包的活动
    public static String payOrderNum = "";//充值订单的订单号


    public static boolean exchangeOrder = false;//  1 是兑换商城订单

    public static List<ExchangeBean> exchangeProduct = new ArrayList<>();//加价购商品
    public static boolean canExchange = false;//是否满足加价购
}
