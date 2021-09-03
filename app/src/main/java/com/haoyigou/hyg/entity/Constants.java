package com.haoyigou.hyg.entity;

import com.haoyigou.hyg.ui.JocellWebView;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Constants {
	public static final String domain= HttpClient.HTTP_DOMAIN;//好易购正式环境
	//public static String domain = "http://ruiduo.happydoit.com";
   //public static String domain = "http://192.168.1.50";
	public static String loginPath = "loginCheck?_supermember_remember=true";
	public static String categoryPath = "category/list1";
	public static String productListPath = "product/index";
	public static String lunboPath = "/lunbopicture/list1";
	public static String mid ="";
	public static String veriosn="";
//	public static String APP_ID="wx080d4ccaa7f3f9a0";
	public static String APP_ID="wx2a5538052969956e";
	public static String wxappbing_url=domain+"weixinlogin";
	public static String token_url=domain+"/super/tokendata";
	public static String getcodePath="business/getcode";
	public static String registerPath="distributor/registerok";
	public static String mePath="distributor/mememe";//个人信息
	public static String ssxxPath="distributor/taskcenter";
	public static String logoutPath="logout";
	public static String pinformationPath="distributor/pinformation";
	public static List<Map<String,Object>> typeList = new ArrayList<Map<String,Object>>();
	public static String updateXml = domain+"/app/meipingmi_android_version.xml";
	public static String appFileName="meipingmi.apk";
	public static String DOWNLOAD_PATH="/data/data/com.taobao.meipingmi/download";
	public static boolean needupdate =true;
	public static String appwxpaysign_url=domain+"/order/confirmsave";
	public static String appwxorderdetail=domain+"order/detail";
	//public static JocellWebView jocellWebView;
	public static String orderNum="";
	public static String feedback = "/distributor/feedback";
	public static JocellWebView jocellWebView;
	public static MainActivity mainActivity;
	public static long refreshTokenTime=0;
}
