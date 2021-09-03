package com.haoyigou.hyg.utils;

import com.haoyigou.hyg.entity.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class JsonAnalyze {

	/**
	 *
	 * @param jsonString
	 * @return
	 */
	public static List analyzeTypeJson(String jsonString){
		List<Map<String,Object>> typeList = new ArrayList<Map<String,Object>>();
		try{
			JSONObject jsondata = new JSONObject(jsonString );
		    JSONArray typeJson = jsondata.getJSONArray("data");
			for(int i=0;i<typeJson.length();i++){
				Map<String,Object> map = new HashMap<String,Object>();
				JSONObject jsonObject = (JSONObject) typeJson.get(i);
				map.put("id", jsonObject.getInt("id"));
				map.put("name", jsonObject.getString("name"));
				map.put("pic", Constants.domain+jsonObject.getString("root"));
				JSONArray childJson = jsonObject.getJSONArray("children");
				List childrenList = new ArrayList();
				for(int j=0;j<childJson.length();j++){
					JSONObject chlid = (JSONObject)childJson.get(j);
					Map childMap = new HashMap();
					childMap.put("id", chlid.getInt("id"));
					childMap.put("name", chlid.getString("name"));
					childMap.put("pic", Constants.domain+chlid.getString("root"));
					childrenList.add(childMap);
				}
				map.put("children", childrenList);
				typeList.add(map);
			}
		}catch(Exception e){
			
		}
		return typeList;
	}
	
	/**
	 *
	 * @param jsonString
	 * @return
	 */
	public static List analyzeProductJson(String jsonString){
		List<Map<String,Object>> productList = new ArrayList<Map<String,Object>>();
		try {
			JSONObject jsondata = new JSONObject(jsonString);
			JSONArray typeJson = jsondata.getJSONArray("result");
			for(int i=0;i<typeJson.length();i++){
				Map<String,Object> map = new HashMap<String,Object>();
				JSONObject jsonObject = (JSONObject) typeJson.get(i);
				map.put("id", jsonObject.getInt("id"));
				map.put("pic", Constants.domain+jsonObject.getString("piclogo"));
				productList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return productList;
	}
	
	/**
	 *
	 * @param jsonString
	 * @return
	 */
	public static List analyzeLunboJson(String jsonString){
		List<Map<String,Object>> picList = new ArrayList<Map<String,Object>>();
		try {
			JSONObject jsondata = new JSONObject(jsonString);
			JSONArray typeJson = jsondata.getJSONArray("data");
			for(int i=0;i<typeJson.length();i++){
				Map<String,Object> map = new HashMap<String,Object>();
				JSONObject jsonObject = (JSONObject) typeJson.get(i);
				map.put("address", jsonObject.getString("address"));
				map.put("pictureroot", jsonObject.getString("pictureroot"));
				picList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return picList;
	}
	
	/**
	 *
	 * @param jsonString
	 * @return
	 */
	public static Map analyzeVcodeJson(String jsonString){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			JSONObject jsondata = new JSONObject(jsonString);
			map.put("status", jsondata.getString("status"));
			map.put("code", jsondata.getString("code"));
			map.put("message", jsondata.getString("message"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 *
	 * @param jsonString
	 * @return
	 */
	public static Map analyzeRegisterJson(String jsonString){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			JSONObject jsondata = new JSONObject(jsonString);
			map.put("status", jsondata.getString("status"));
			map.put("message", jsondata.getString("message"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 *
	 * @param jsonString
	 * @return
	 */
	public static Map analyzeMeJson(String jsonString){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			JSONObject jdata = new JSONObject(jsonString);
			JSONObject jsondata= jdata.getJSONObject("data");
			map.put("attention", jsondata.getInt("attention"));
			map.put("retrans", jsondata.getInt("retrans"));
			map.put("salesamount", jsondata.getDouble("salesamount"));
			map.put("salesvolume", jsondata.getDouble("salesvolume"));
			map.put("totleamount", jsondata.getDouble("totleamount"));
			map.put("pic", jsondata.getString("pic"));
			map.put("sign", jsondata.getString("sign"));
			map.put("name", jsondata.getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

}
