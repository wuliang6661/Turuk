package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2017/10/20.
 * <p>
 * 人气推荐bean
 */

public class RenQiBO implements Serializable {


    /**
     * moudule_id : 1
     * moduleElement : [{"product_id":76,"title":"单品","product_name":"4.cotton floss内裤","product_piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201611/765vUvZ.jpg","urladdress":"","product_price":298,"product_disprice":198,"imgurl":"","sequence":""},{"product_id":76,"title":"单品","product_name":"4.cotton floss内裤","product_piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201611/765vUvZ.jpg","urladdress":"","product_price":298,"product_disprice":198,"imgurl":"","sequence":""}]
     */

    private int moudule_id;
    private List<ModuleElementBo> moduleElement;

    public int getMoudule_id() {
        return moudule_id;
    }

    public void setMoudule_id(int moudule_id) {
        this.moudule_id = moudule_id;
    }

    public List<ModuleElementBo> getModuleElement() {
        return moduleElement;
    }

    public void setModuleElement(List<ModuleElementBo> moduleElement) {
        this.moduleElement = moduleElement;
    }

    public static class ModuleElementBo {
        /**
         * product_id : 76
         * title : 单品
         * product_name : 4.cotton floss内裤
         * product_piclogo : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201611/765vUvZ.jpg
         * urladdress :
         * product_price : 298.0
         * product_disprice : 198.0
         * imgurl :
         * sequence :
         */
        private int jumptype;
        private int product_id;
        private String title;
        private String product_name;
        private String product_piclogo;
        private String urladdress;
        private double product_price;
        private double product_disprice;
        private double product_oldprice;
        private String imgurl;
        private String sequence;
        private String idparam;

        public int getJumptype() {
            return jumptype;
        }

        public void setJumptype(int jumptype) {
            this.jumptype = jumptype;
        }


        public String getIdparam() {
            return idparam;
        }

        public void setIdparam(String idparam) {
            this.idparam = idparam;
        }

        public double getProduct_oldprice() {
            return product_oldprice;
        }

        public void setProduct_oldprice(double product_oldprice) {
            this.product_oldprice = product_oldprice;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_piclogo() {
            return product_piclogo;
        }

        public void setProduct_piclogo(String product_piclogo) {
            this.product_piclogo = product_piclogo;
        }

        public String getUrladdress() {
            return urladdress;
        }

        public void setUrladdress(String urladdress) {
            this.urladdress = urladdress;
        }

        public double getProduct_price() {
            return product_price;
        }

        public void setProduct_price(double product_price) {
            this.product_price = product_price;
        }

        public double getProduct_disprice() {
            return product_disprice;
        }

        public void setProduct_disprice(double product_disprice) {
            this.product_disprice = product_disprice;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }
    }
}
