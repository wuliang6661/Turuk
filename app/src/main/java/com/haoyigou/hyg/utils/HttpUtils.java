package com.haoyigou.hyg.utils;


import com.haoyigou.hyg.entity.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class HttpUtils {
	// private static HttpParams httpParams;
	private static DefaultHttpClient httpClient;
	public static String JSESSIONID; //
	public static String _supermember_token=null;
	public static Boolean islogin=false;
	public static DefaultHttpClient getHttpClient() throws Exception {
		if (httpClient != null) {
//			return httpClient;
			httpClient = null;
		}

		HttpParams httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		HttpClientParams.setRedirecting(httpParams, true);

		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

	public static String send(String url, Map<String, Object> paramsMap)
			throws Exception {
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("access-type", "app");
		if(null != SharedPreferencesUtils.getInstance().getString("jsessionid",null)){
            httpPost.setHeader("Cookie", "JSESSIONID=" + SharedPreferencesUtils.getInstance().getString("jsessionid",null));
        } 
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (paramsMap != null) {
			for (Map.Entry<String, Object> param : paramsMap.entrySet()) {
				params.add(new BasicNameValuePair(param.getKey(), ""+param
						.getValue()));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		DefaultHttpClient httpclient = getHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpPost.getParams(), 20000);
		HttpConnectionParams.setSoTimeout(httpPost.getParams(), 20000);
		HttpResponse httpResponse = httpclient.execute(httpPost);
		StringBuilder builder = new StringBuilder();
		BufferedReader bufferedReader2 = new BufferedReader(
				new InputStreamReader(httpResponse.getEntity().getContent()));
		for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
				.readLine()) {
			builder.append(s);
		}
		return builder.toString();
	}
	
	public static Boolean getNewToken(String oldtoken)
			throws Exception {
		HttpPost httpPost = new HttpPost(Constants.token_url);
		httpPost.addHeader("access-type", "app");
		httpPost.setHeader("Cookie", "_supermember_token=" + oldtoken);
//		DefaultHttpClient httpClient = getHttpClient();
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		HttpClientParams.setRedirecting(httpParams, true);

		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		//strResult = EntityUtils.toString(httpResponse.getEntity());
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			CookieStore cookieStore = httpClient.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				System.out.println(cookies.get(i).getName()+"="+cookies.get(i).getValue());
				if ("JSESSIONID".equals(cookies.get(i).getName())) {
					JSESSIONID = cookies.get(i).getValue();
					if(_supermember_token!=null){
						break;
					}else{
						continue;
					}
				}
				if("_supermember_token".equals(cookies.get(i).getName())){
					_supermember_token=cookies.get(i).getValue();
					Constants.refreshTokenTime = System.currentTimeMillis();
					if(JSESSIONID!=null){
						break;
					}else{
						continue;
					}
				}
			}
			return true;
		}
		return false;
	}


	
		
}
