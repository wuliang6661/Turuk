package com.haoyigou.hyg.entity;

/**
 * Created by Witness on 5/28/21
 * Describe:
 */
public class SKU {

    private ColorBean color;
    private String disprice;
    private String id;
    private String picture;
    private String price;
    private SizeBean size;
    private String store;

    public ColorBean getColor() {
        return color;
    }

    public void setColor(ColorBean color) {
        this.color = color;
    }

    public String getDisprice() {
        return disprice;
    }

    public void setDisprice(String disprice) {
        this.disprice = disprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public SizeBean getSize() {
        return size;
    }

    public void setSize(SizeBean size) {
        this.size = size;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public static class ColorBean {
        /**
         * code : 5073
         * name : 卡其
         */

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SizeBean {
        /**
         * code : 5352
         * name : M
         */

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
