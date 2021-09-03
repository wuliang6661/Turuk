package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;

/**
 * Created by Witness on 2021/1/20
 * Describe: 支付结果
 */
public class AlipayMiniProgramCallbackActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent() != null){
            try {
                Uri uri = getIntent().getData();
                //完整路径
                String url = uri.toString();

                String errCode = uri.getQueryParameter("errCode");
                String errStr = uri.getQueryParameter("errStr");
                switch (errCode){
                    case "0000"://支付请求发送成功。商户订单是否成功支付应该以商户后台收到支付结果。
                        Intent intent = new Intent(AlipayMiniProgramCallbackActivity.this, WebActivity.class);
                        if (StateMessage.exchangeOrder){
                            Bundle bundle = new Bundle();
                            bundle.putString("exchangeUrl", HttpClient.HTTP_DOMAIN + "/exchange/record/index?distributorId=" + SharedPreferencesUtils.getInstance().getString("distributorId", ""));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            StateMessage.exchangeOrder = false;
                            finish();
                        }else {
                            Toast.makeText(AlipayMiniProgramCallbackActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();

                            if (StateMessage.isPinTuan) {//拼团
                                bundle.putString("orderNum", null);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                bundle.putString("orderNum", MApplication.ordernum);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                MApplication.ordernum = "";
                            }
                            finish();
                        }
                        break;
                    case "1000"://用户取消支付
                        payError("1000");
                        break;
                    case "1001"://参数错误
                        payError("1001");
                        break;
                    case "1002"://网络连接错误
                        payError("1002");
                        break;
                    case "1003"://支付客户端未安装
                        payError("1003");
                        break;
                    case "2001"://订单处理中，支付结果未知(有可能已经支付成功)，请通过后台接口查询订单状态"
                        payError("2001");
                        break;
                    case "2002"://订单号重复
                        payError("2002");
                        break;
                    case "2003"://订单支付失败
                        payError("2003");
                        break;
                    case "9999"://其他支付错误
                        payError("9999");
                        break;

                }
            }catch (Exception e){
                e.getStackTrace();

            }
        }else {
            payError("9999");
        }
    }


    private void payError(String errCode){
        if (StateMessage.exchangeOrder){
            Bundle bundle = new Bundle();
            bundle.putString("exchangeUrl", HttpClient.HTTP_DOMAIN + "/exchange/index");
            Intent intent = new Intent(AlipayMiniProgramCallbackActivity.this, WebActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            StateMessage.exchangeOrder = false;
            finish();
        }else {
            if (errCode.equals("2001")) {
//                        Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("orderNum", MApplication.ordernum);
                Intent intent = new Intent(AlipayMiniProgramCallbackActivity.this, OrderMessageAct.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(AlipayMiniProgramCallbackActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("orderNum", MApplication.ordernum);
                Intent intent = new Intent(AlipayMiniProgramCallbackActivity.this, OrderMessageAct.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            MApplication.ordernum = "";
            finish();
        }
    }
}
