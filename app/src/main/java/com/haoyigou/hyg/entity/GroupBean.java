package com.haoyigou.hyg.entity;

/**
 * Created by Witness on 2/1/21
 * Describe:
 */
public class GroupBean {
    private String image;
    private String title;
    private String num;
    private String nowgroups;
    private String disprice;
    private Product product;
    private String id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNowgroups() {
        return nowgroups;
    }

    public void setNowgroups(String nowgroups) {
        this.nowgroups = nowgroups;
    }

    public String getDisprice() {
        return disprice;
    }

    public void setDisprice(String disprice) {
        this.disprice = disprice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Product {
        private String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
