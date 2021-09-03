package com.haoyigou.hyg.entity;

/**
 * Created by Witness on 2020-04-20
 * Describe:
 */
public class CommunBean {
    private String comment;
    private String header;
    private String id;
    private String name;
    private String pictures;
    private ProductBean product;
    private String text;
    private String tips;
    private String videoUrl;
    private String watchTimes;
    private String isTip;
    private String videoImg;
    private String createTime;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getWatchTimes() {
        return watchTimes;
    }

    public void setWatchTimes(String watchTimes) {
        this.watchTimes = watchTimes;
    }

    public String getIsTip() {
        return isTip;
    }

    public void setIsTip(String isTip) {
        this.isTip = isTip;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static class ProductBean {
        /**
         * id : 131
         * name : 纯生经典 330mlaa
         */

        private int id;
        private String name;
        private String jumpAdress;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getJumpAdress() {
            return jumpAdress;
        }

        public void setJumpAdress(String jumpAdress) {
            this.jumpAdress = jumpAdress;
        }
    }
}
