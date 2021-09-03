package com.haoyigou.hyg.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/9/20.
 */

public class MadrushListBean {

    /**
     * grabData : [{"product":[{"disprice":289,"id":167,"jumpAdress":"http://192.168.1.65/pix?distributorId=1060&source=1&productid=167","name":"千岛湖啤酒330ml","overplus":100,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201808/02zOqojz.jpg","price":299,"store":1073}],"time":1554171578000},{"product":[{"disprice":989,"id":168,"jumpAdress":"http://192.168.1.65/pix?distributorId=1060&source=1&productid=168","name":"促销润田矿泉水","overplus":100,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201809/20mKUygI.jpg","price":999,"store":378}],"time":1554171642000},{"product":[{"disprice":977,"id":166,"jumpAdress":"http://192.168.1.65/pix?distributorId=1060&source=1&productid=166","name":"雪津啤酒550ml","overplus":50,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201808/02kXGnds.jpg","price":999,"store":888}],"time":1554171679000}]
     * tvData : {}
     */

    private List<TvDataBean> tvData;
    private List<GrabDataBean> grabData;
    private List<GroupBuy> groupBuy;
    private List<DayNewProductListBean> dayNewProductList;//每日上新
    private PageSettingBean pageSetting;

    public List<TvDataBean> getTvData() {
        return tvData;
    }

    public void setTvData(List<TvDataBean> tvData) {
        this.tvData = tvData;
    }

    public List<GrabDataBean> getGrabData() {
        return grabData;
    }

    public void setGrabData(List<GrabDataBean> grabData) {
        this.grabData = grabData;
    }

    public List<GroupBuy> getGroupBuy() {
        return groupBuy;
    }

    public void setGroupBuy(List<GroupBuy> groupBuy) {
        this.groupBuy = groupBuy;
    }

    public List<DayNewProductListBean> getDayNewProductList() {
        return dayNewProductList;
    }

    public void setDayNewProductList(List<DayNewProductListBean> dayNewProductList) {
        this.dayNewProductList = dayNewProductList;
    }

    public PageSettingBean getPageSetting() {
        return pageSetting;
    }

    public void setPageSetting(PageSettingBean pageSetting) {
        this.pageSetting = pageSetting;
    }

    public static class TvDataBean {
        private String pic;
        private String url;
        private String name;
        private String price;
        private String disprice;
        private String memo;
        private String isPro;
        private String jumpUrl;
        private String startTime;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDisprice() {
            return disprice;
        }

        public void setDisprice(String disprice) {
            this.disprice = disprice;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getIsPro() {
            return isPro;
        }

        public void setIsPro(String isPro) {
            this.isPro = isPro;
        }

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
    }

    public static class GroupBuy {

        private String pic;
        private String name;
        private String price;
        private String disprice;
        private String differ;
        private String memo;
        private String coaststore;
        private String jumpUrl;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDisprice() {
            return disprice;
        }

        public void setDisprice(String disprice) {
            this.disprice = disprice;
        }

        public String getDiffer() {
            return differ;
        }

        public void setDiffer(String differ) {
            this.differ = differ;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getCoaststore() {
            return coaststore;
        }

        public void setCoaststore(String coaststore) {
            this.coaststore = coaststore;
        }

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
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

            private int disprice;
            private int id;
            private String jumpAdress;
            private String name;
            private int overplus;
            private String piclogo;
            private int price;
            private int store;

            public int getDisprice() {
                return disprice;
            }

            public void setDisprice(int disprice) {
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

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
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

    public static class DayNewProductListBean{

        private String disprice;
        private String jumpAdress;
        private String name;
        private String piclogo;
        private String price;

        public String getDisprice() {
            return disprice;
        }

        public void setDisprice(String disprice) {
            this.disprice = disprice;
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
    }

    public static class PageSettingBean{

        private TVSettingBean tvSetting;
        private GroupBuySettingBean groupBuySetting;
        private DayNewSetting dayNewSetting;

        public TVSettingBean getTvSetting() {
            return tvSetting;
        }

        public void setTvSetting(TVSettingBean tvSetting) {
            this.tvSetting = tvSetting;
        }

        public GroupBuySettingBean getGroupBuySetting() {
            return groupBuySetting;
        }

        public void setGroupBuySetting(GroupBuySettingBean groupBuySetting) {
            this.groupBuySetting = groupBuySetting;
        }

        public DayNewSetting getDayNewSetting() {
            return dayNewSetting;
        }

        public void setDayNewSetting(DayNewSetting dayNewSetting) {
            this.dayNewSetting = dayNewSetting;
        }

        public static class TVSettingBean{
            private String color;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }

        public static class GroupBuySettingBean{
            private String color;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }

        public static class DayNewSetting{
            private String color;


            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

        }
    }
}
