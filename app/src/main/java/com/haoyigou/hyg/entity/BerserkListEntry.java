package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2016/11/23.
 * <p>
 * 正在疯抢的集合bean
 */

public class BerserkListEntry implements Serializable {

    /**
     * grabData : [{"product":[{"disprice":289,"id":167,"jumpAdress":"http://192.168.1.65/pix?distributorId=1060&source=1&productid=167","name":"千岛湖啤酒330ml","overplus":100,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201808/02zOqojz.jpg","price":299,"store":1073}],"time":1554171578000},{"product":[{"disprice":989,"id":168,"jumpAdress":"http://192.168.1.65/pix?distributorId=1060&source=1&productid=168","name":"促销润田矿泉水","overplus":100,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201809/20mKUygI.jpg","price":999,"store":378}],"time":1554171642000},{"product":[{"disprice":977,"id":166,"jumpAdress":"http://192.168.1.65/pix?distributorId=1060&source=1&productid=166","name":"雪津啤酒550ml","overplus":50,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201808/02kXGnds.jpg","price":999,"store":888}],"time":1554171679000}]
     * tvData : {}
     */

    private TvDataBean tvData;
    private List<GrabDataBean> grabData;

    public TvDataBean getTvData() {
        return tvData;
    }

    public void setTvData(TvDataBean tvData) {
        this.tvData = tvData;
    }

    public List<GrabDataBean> getGrabData() {
        return grabData;
    }

    public void setGrabData(List<GrabDataBean> grabData) {
        this.grabData = grabData;
    }

    public static class TvDataBean {
        private String pic;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }

    public static class GrabDataBean {
        /**
         * product : [{"disprice":289,"id":167,"jumpAdress":"http://192.168.1.65/pix?distributorId=1060&source=1&productid=167","name":"千岛湖啤酒330ml","overplus":100,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201808/02zOqojz.jpg","price":299,"store":1073}]
         * time : 1554171578000
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
             * disprice : 289
             * id : 167
             * jumpAdress : http://192.168.1.65/pix?distributorId=1060&source=1&productid=167
             * name : 千岛湖啤酒330ml
             * overplus : 100
             * piclogo : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201808/02zOqojz.jpg
             * price : 299
             * store : 1073
             */

            private String disprice;
            private int id;
            private String jumpAdress;
            private String name;
            private int overplus;
            private String piclogo;
            private String price;
            private int store;

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

            public int getStore() {
                return store;
            }

            public void setStore(int store) {
                this.store = store;
            }
        }
    }
}
