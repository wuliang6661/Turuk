package com.haoyigou.hyg.entity;

import java.util.List;

/**
 * Created by Witness on 2019/3/28
 * Describe:
 */
public class NewProdectBO {

    /**
     * categoryId : 45
     * categoryName : 大众食品
     * categoryIndexPic : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201903/27fRfVxy.png
     * products : [{"canUseCoupon":0,"discontent":"","disprice":999,"id":168,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=168","name":"促销润田矿泉水","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201809/20mKUygI.jpg","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":0.01,"id":164,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=164","name":"就百威啤酒（铝装）550ml","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201807/12JQvasO.jpg","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":2999,"id":131,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=131","name":"纯生经典 330ml","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201807/04OJYiCa.png","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":1698,"id":130,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=130","name":"崂山啤酒1000ml","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201711/184GShf.jpg","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":1900,"id":98,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=98","name":"乌克兰矿工啤酒1.25L","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201809/20MLpIid.jpg","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":600,"id":97,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=97","name":"买玫瑰花送百合","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201809/20NyfKJi.jpg","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":10,"id":94,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=94","name":" 徐刚达芙妮28周年纪念休闲运动鞋佟丽娅卡空2","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201809/20AzcZVT.jpg","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":2980,"id":74,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=74","name":"【朱彬彬】18.松下超视距大容量进口摄像机8","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201611/409FJmm.jpg","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":120,"id":171,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=171","name":"小米耳机","otherDiscontent":"","pic5":"","piclogo":"","price":0,"productCase":"","secondTitle":""},{"canUseCoupon":0,"discontent":"","disprice":105.6,"id":170,"jumpAdress":"http://192.168.1.65/pix?distributorId=1&source=1&productid=170","name":"跨境保税-澳洲Nulax乐康膏果蔬润肠养颜2","otherDiscontent":"","pic5":"","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201902/27uAyOvh.jpg","price":0,"productCase":"","secondTitle":""}]
     */

    private int categoryId;
    private String categoryName;
    private String categoryUrl;
    private String categoryIndexPic;
    private List<ProductsBean> products;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIndexPic() {
        return categoryIndexPic;
    }

    public void setCategoryIndexPic(String categoryIndexPic) {
        this.categoryIndexPic = categoryIndexPic;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public static class ProductsBean {
        /**
         * canUseCoupon : 0
         * discontent :
         * disprice : 999
         * id : 168
         * jumpAdress : http://192.168.1.65/pix?distributorId=1&source=1&productid=168
         * name : 促销润田矿泉水
         * otherDiscontent :
         * pic5 :
         * piclogo : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201809/20mKUygI.jpg
         * price : 0
         * productCase :
         * secondTitle :
         */

        private int canUseCoupon;
        private String discontent;
        private String disprice;
        private int id;
        private String jumpAdress;
        private String name;
        private String otherDiscontent;
        private String pic5;
        private String piclogo;
        private String price;
        private String productCase;
        private String secondTitle;

        public int getCanUseCoupon() {
            return canUseCoupon;
        }

        public void setCanUseCoupon(int canUseCoupon) {
            this.canUseCoupon = canUseCoupon;
        }

        public String getDiscontent() {
            return discontent;
        }

        public void setDiscontent(String discontent) {
            this.discontent = discontent;
        }

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

        public String getOtherDiscontent() {
            return otherDiscontent;
        }

        public void setOtherDiscontent(String otherDiscontent) {
            this.otherDiscontent = otherDiscontent;
        }

        public String getPic5() {
            return pic5;
        }

        public void setPic5(String pic5) {
            this.pic5 = pic5;
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

        public String getProductCase() {
            return productCase;
        }

        public void setProductCase(String productCase) {
            this.productCase = productCase;
        }

        public String getSecondTitle() {
            return secondTitle;
        }

        public void setSecondTitle(String secondTitle) {
            this.secondTitle = secondTitle;
        }
    }
}
