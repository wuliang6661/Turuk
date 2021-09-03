package com.haoyigou.hyg.entity;

import java.util.List;

/**
 * Created by Witness on 2019/4/8
 * Describe:
 */
public class MadrushBO {
    /**
     * product : [{"disprice":48,"id":20654,"jumpAdress":"https://m.best1.com/pix?distributorId=1&source=1&productid=20654","name":"维达抽纸超韧3层S码130抽24包v2339-B","overplus":132,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201903/26ahSyxm.jpg","price":59.9,"store":213},{"disprice":39,"id":20092,"jumpAdress":"https://m.best1.com/pix?distributorId=1&source=1&productid=20092","name":"九河滩盘锦蟹田大米5kg","overplus":164,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201902/28pyzCAS.jpg","price":55,"store":190},{"disprice":89,"id":17672,"jumpAdress":"https://m.best1.com/pix?distributorId=1&source=1&productid=17672","name":"FERACE D3飞锐思彩屏防水智能运动血压手环","overplus":49,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201810/22wLxUAb.jpg","price":99,"store":229},{"disprice":69,"id":17700,"jumpAdress":"https://m.best1.com/pix?distributorId=1&source=1&productid=17700","name":"跨境保税泰国安娜贝拉深海矿物补水面膜20片","overplus":194,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201810/19AHtFCi.jpg","price":80,"store":44},{"disprice":179,"id":7734,"jumpAdress":"https://m.best1.com/pix?distributorId=1&source=1&productid=7734","name":"小熊Bear迷你榨汁机随身杯LLJ-D06A1","overplus":48,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201712/251jDqU.jpg","price":198,"store":93}]
     * time : 1554688799000
     */

    private long time;
    private List<ProductBean> product;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<ProductBean> getProduct() {
        return product;
    }

    public void setProduct(List<ProductBean> product) {
        this.product = product;
    }

    public static class ProductBean {
        /**
         * disprice : 48
         * id : 20654
         * jumpAdress : https://m.best1.com/pix?distributorId=1&source=1&productid=20654
         * name : 维达抽纸超韧3层S码130抽24包v2339-B
         * overplus : 132
         * piclogo : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201903/26ahSyxm.jpg
         * price : 59.9
         * store : 213
         */

        private String disprice;
        private int id;
        private String jumpAdress;
        private String name;
        private int overplus;
        private String piclogo;
        private String price;
        private String store;

        public String getDisprice() {
            return disprice;
        }

        public void setDisprice(String disprice) {
            this.disprice = disprice;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJumpAdress() {
            return jumpAdress;
        }

        public void setJumpAdress(String jumpAdress) {
            this.jumpAdress = jumpAdress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOverplus() {
            return overplus;
        }

        public void setOverplus(int overplus) {
            this.overplus = overplus;
        }

        public String getPiclogo() {
            return piclogo;
        }

        public void setPiclogo(String piclogo) {
            this.piclogo = piclogo;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }
    }
}
