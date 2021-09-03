package com.haoyigou.hyg.entity;

/**
 * Created by Witness on 5/28/21
 * Describe:
 */
public class ExchangeBean {

    private String price;
    private String id;
    private String originPrice;
    private ProductBean product;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public static class ProductBean{
        private String name;
        private String piclogo;
        private String id;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
