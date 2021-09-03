package com.haoyigou.hyg.utils;

import android.util.Log;


import com.haoyigou.hyg.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by kristain on 16/1/21.
 */
public class FuncUtils {


    /**
     * 获取唯一标识uuid
     *
     * @return
     */
    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 功能描述：金额校验，必须大于0.01
     *
     * @param money
     * @return
     */
    public static boolean isMoney(String money) {
        if (StringUtils.isEmpty(money)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]{0,}[.]{0,1}[0-9]{0,2}$");
        if (!pattern.matcher(money).matches()) {
            return false;
        }
        return Double.parseDouble(money) >= 0.01;
    }

    public static boolean isMoneyNolimit(String money) {
        if (StringUtils.isEmpty(money)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]{0,}[.]{0,1}[0-9]{0,100}$");
        return pattern.matcher(money).matches();
    }


    /**
     * 根据产品名称获取钱包分割线色值
     * @param categoryId
     * @return
     */
    public static int toWalletColorByCategory(int categoryId){
        if(categoryId==11){
            return R.color.wallet_product_color;
        }else if (categoryId==9){
            return R.color.wallet_other_color;
        }else{
            return R.color.wallet_consumer_color;
        }
    }

    /**
     * 金额相减
     * @param amt_limit
     * @param amt 支付金额
     * @return
     */
    public static double amt_sub(String amt_limit, String amt){
        if (!isMoney(amt_limit) || !isMoney(amt_limit)) {
            return 0;
        }
        return  Double.parseDouble(amt_limit) - Double.parseDouble(amt);
    }
    /**
     * 金额相减
     * @param amt_limit
     * @param amt 支付金额
     * @return
     */
    public static String amt_sub1(String amt_limit, String amt){
        if (!isMoney(amt_limit) || !isMoney(amt_limit)) {
            return "0";
        }
       Double moneys = Double.parseDouble(amt_limit) - Double.parseDouble(amt);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        return  decimalFormat.format(moneys);
    }


    public static String formatMonth(String date){
        if(StringUtils.isEmpty(date)){
            return "";
        }
        String month = date.substring(date.indexOf("年") + 1);
        Log.e("TAG",month);
        if(month.length()==2){
            month="0"+month;
        }
        return date.substring(0,4)+""+month.replace("月","");
    }

    /**
     * 订单列表服务时间文案
     * 1970-01-18 15:00-15:30
     * @param starttime
     * @param endtime
     * @return
     */
    public static String getOrderServerTime(String starttime,String endtime){
        if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
            return "";
        }
        String server_starttime = DateTimeUtils.formatDateByMill(Long.parseLong(starttime));


        String server_endtime = DateTimeUtils.formatDateByMill(Long.parseLong(endtime));
          //这边本来不需要判断的但是要昨天，今天，明天必须获取当前的时间（明天：23：58，昨天，00：00）
        if(Long.parseLong(endtime)*1000>(DateTimeUtils.weeHours(new Date(),0).getTime())){
            if(!DateTimeUtils.getDateDetail(server_endtime).equals("")){
                server_starttime= server_starttime.replace(" "," ("+DateTimeUtils.getDateDetail(server_endtime)+")  ");
            }
        }else{
            if(!DateTimeUtils.getDateDetail(server_endtime).equals("")){
                server_starttime= server_starttime.replace(" "," ("+DateTimeUtils.getDateDetail1(server_endtime)+")  ");
            }
        }

        if(!StringUtils.isEmpty(server_endtime)&&server_endtime.length()==16){
            server_endtime = server_endtime.substring(11,16);
        }
        return server_starttime+"-"+server_endtime;
    }

     public  static  void main(String[] str){  //1466047740   1465920000817
         System.out.println(getCircleNewsRate("6800", "1000") + "d");
     }
    /**
     * 格式化手机号码
     * 格式：135 8888 8888
     * @param phone
     * @return
     */
    public static String formatPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return "";
        }
        if(!isCellPhone(phone)){
            return phone;
        }
       // phone.substring(0, 3)+" "+phone.substring(3,7)+" "+phone.substring(7)
        return phone.substring(0,3) + " **** " + phone.substring(7, phone.length());
    }


    /**
     * 手机号码检查
     * @param str
     * @return
     */
    public static boolean isCellPhone(String str)
    {
        if (StringUtils.isEmpty(str))
            return false;
        return Pattern.matches("1[34578][0-9]{9}", str.trim());
    }

    /**
     * 获取指标完成率 action/target
     * @param target
     * @param action
     * @return
     */
    public static float getCircleRate(String target, String action){
        if(StringUtils.isEmpty(target) ||StringUtils.isEmpty(action)||!isMoney(target)||!isMoney(action)){
            return 0;
        }
        if(Float.parseFloat(target)==0 || Float.parseFloat(action)==0){
            return 0;
        }
        if(Float.parseFloat(action)-Float.parseFloat(target)>0){
            return 100;
        }
        return Float.parseFloat(action)*100/Float.parseFloat(target);
    }
    /**
     * 获取指标完成率 action/target(优化后的)
     * @param target
     * @param action
     * @return
     */
    public static String getCircleNewsRate(String target, String action){
        if(StringUtils.isEmpty(target) ||StringUtils.isEmpty(action)||!isMoney(target)||!isMoney(action)){
            return "0.00";
        }
        if(Float.parseFloat(target)==0 || Float.parseFloat(action)==0){
            return "0.00";
        }
        if(Float.parseFloat(action)-Float.parseFloat(target)>0){
            DecimalFormat decimalFormat=new DecimalFormat("0.0");
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setGroupingSize(0);
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            double rate= Float.parseFloat(action)*100/Float.parseFloat(target);
            return rate>999.9?"999.9":decimalFormat.format(rate);
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return decimalFormat.format(Float.parseFloat(action) * 100 / Float.parseFloat(target));
    }
    /**
     * 获得指标最大值百分率
     */
     public static float getCircleNewsReteMax(float target, float action, float sale){
         return Math.max(Math.max(target, action), sale);
     }
    /**
     * 获取门店完成率 action/target
     * @param target
     * @param action
     * @return
     */
    public static String getCircleStore(String target,String action){
        if(StringUtils.isEmpty(target)||StringUtils.isEmpty(action)){
            return "0.00";
        }
        if(Float.parseFloat(target)==0 || Float.parseFloat(action)==0){
            return "0.00";
        }
        if(Float.parseFloat(action)-Float.parseFloat(target)>0){
            return "100.00";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        double rate= Float.parseFloat(action)*100/Float.parseFloat(target);
        return decimalFormat.format(rate);
    }


    /**
     * 获取指标完成率
     * @param target
     * @param action
     * @return
     */
    public static String getPercentRate(String target, String action){
        if(StringUtils.isEmpty(target) ||StringUtils.isEmpty(action)){
            return "0";
        }
        if(Float.parseFloat(target)==0 || Float.parseFloat(action)==0){
            return "0";
        }
        target=formatWMoney2(target);
        action =formatWMoney2(action);
        double rate = (Float.parseFloat(action)/Float.parseFloat(target));
        if(!isMoneyNolimit(rate+"")){
           return "0";
        }
       // if(rate>1){
        //    return "100";
        //}
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return decimalFormat.format(rate * 100);
    }

    /**
     *  格式化double
     * @param
     * @param
     * @return
     */
    public  static String getDouble(String date){
        DecimalFormat formater = new DecimalFormat("0.00");
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
      //  formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(Float.parseFloat(date));
    }

    public static String getPercentRateInt(String target, String action){
        if(StringUtils.isEmpty(target) ||StringUtils.isEmpty(action)){
            return "0";
        }
        if(Float.parseFloat(target)==0 || Float.parseFloat(action)==0){
            return "0";
        }
        target=formatWMoney(target);
        action =formatWMoney(action);
        double rate = (Float.parseFloat(action)/Float.parseFloat(target));
        if(!isMoneyNolimit(rate+"")){
            return "0";
        }
        if(rate>1){
            return "100";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0");
        decimalFormat.setMaximumFractionDigits(0);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return decimalFormat.format(rate);
    }

    /**
     * 格式化金额
     * @param money
     * @return
     */
    public static String formatMoney(String money){
        if(StringUtils.isEmpty(money) || !StringUtils.isMoney(money)){
            return "0.0";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.0");
        decimalFormat.setMaximumFractionDigits(1);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(Double.parseDouble(money)/10000);
    }


    /**
     * 格式化金额 四舍五入
     * @param money
     * @return
     */
    public static String formatMoney4(String money){
      // ||!StringUtils.isMoney(money)
        if(StringUtils.isEmpty(money)){
            return "0.00";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(Double.parseDouble(money)/10000);
    }
    /**
     * 格式化金额 不四舍五入
     * @param money
     * @return
     */
    public static String formatMoney7(String money){
        // ||!StringUtils.isMoney(money)
        if(StringUtils.isEmpty(money)){
            return "0.00";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return decimalFormat.format(Double.parseDouble(money)/10000);
    }

    /**
     * 格式化金额 四舍五入
     * @param money
     * @return
     */
    public static String formatMoney5(String money){
        if(StringUtils.isEmpty(money)){
            return "0.0";
        }
           if(money.contains(".")){
              money=money.substring(0,money.indexOf("."));
               if(money.length()<5){
                   return money;
               }
           }else if(money.length()<5){
                 return money;
           }
              DecimalFormat decimalFormat=new DecimalFormat("0.00");
              return decimalFormat.format(Double.parseDouble(money)/10000)+"万";
          }
    /**
     * 格式化金额 不四舍五入
     * @param money
     * @return
     */
    public static String formatMoney8(String money){
        if(StringUtils.isEmpty(money)){
            return "0.00";
        }
        if(money.contains(".")){
           String money1=money.substring(0,money.indexOf("."));
            if(money1.length()<5){
                DecimalFormat decimalFormat=new DecimalFormat("0.00");
                decimalFormat.setMaximumFractionDigits(2);
                decimalFormat.setGroupingSize(0);
                decimalFormat.setRoundingMode(RoundingMode.FLOOR);
                return decimalFormat.format(Double.parseDouble(money));
            }
        }else if(money.length()<5){
            DecimalFormat decimalFormat=new DecimalFormat("0.00");
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setGroupingSize(0);
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            return decimalFormat.format(Double.parseDouble(money));
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return decimalFormat.format(Double.parseDouble(money)/10000)+"万";
    }
    /**
     * 格式化金额是（不四舍五入）
     * @param money
     * @return
     */
    public static String formatMoney6(String money){
        if(StringUtils.isEmpty(money)){
            return "0.00元";
        }
        if(Double.parseDouble(money)==0){
            return "0.00元";
        }
        if(money.contains(".")){
            money=money.substring(0,money.indexOf("."));
            if(money.length()<5){
                DecimalFormat decimalFormat=new DecimalFormat("0.00");
                decimalFormat.setMaximumFractionDigits(2);
                decimalFormat.setGroupingSize(0);
                decimalFormat.setRoundingMode(RoundingMode.FLOOR);
                return decimalFormat.format(Double.parseDouble(money))+"元";
            }
        }else if(money.length()<5){
            DecimalFormat decimalFormat=new DecimalFormat("0.00");
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setGroupingSize(0);
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            return decimalFormat.format(Double.parseDouble(money))+"元";
        }
      //  DecimalFormat decimalFormat=new DecimalFormat("0.00");
        DecimalFormat formater = new DecimalFormat("0.00");
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
       //或者简化版
       // DecimalFormat formater = new DecimalFormat("#0.##");
        return formater.format(Double.parseDouble(money)/10000)+"万";
    }
    /**
     * 格式化金额
     * @param money
     * @return
     */
    public static String formatMoney3(String money){
        if(StringUtils.isEmpty(money) || !StringUtils.isMoney(money)){
            return "0.0";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.0");
        decimalFormat.setMaximumFractionDigits(1);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(Double.parseDouble(money));
    }

    /**
     * 格式化金额
     * @param money
     * @return
     */
    public static String formatMoney2(String money){
        if(StringUtils.isEmpty(money) || !StringUtils.isMoney(money)){
            return "0.00";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(Double.parseDouble(money)/10000);
    }

    /**
     * 格式化金额(单位为万)
     * @param money
     * @return
     */
    public static String formatWMoney(String money){
        if(StringUtils.isEmpty(money) || !StringUtils.isMoney(money)){
            return "0.0";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.0");
        return decimalFormat.format(Double.parseDouble(money)/10000);
    }

    /**
     * 格式化金额(单位为万)
     * @param money
     * @return
     */
    public static String formatWMoney3(String money){
        if(StringUtils.isEmpty(money) || !StringUtils.isMoney(money)){
            return "0";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0");
        return decimalFormat.format(Double.parseDouble(money)*10000);
    }
    /**
     * 格式化百分率 保留2位小数
     * @param money
     * @return
     */
    public static String formatWLMoney(String money){
        if(StringUtils.isEmpty(money)){
            return "0.00";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(Double.parseDouble(money)*100);
    }

    /**
     * 格式化金额(单位为万) 保留2位小数
     * @param money
     * @return
     */
    public static String formatWMoney2(String money){
        if(StringUtils.isEmpty(money) || !StringUtils.isMoney(money)){
            return "0.0";
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.000");
        return decimalFormat.format(Double.parseDouble(money) / 10000);
    }

    /**
     * 获取下属角色
     * @param role
     * @return
     */


}
