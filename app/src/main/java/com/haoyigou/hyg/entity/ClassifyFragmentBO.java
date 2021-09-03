package com.haoyigou.hyg.entity;

import java.util.List;

/**
 * Created by witness on 2018/9/12.
 *  分类页面数据，轮播图和标签
 */

public class ClassifyFragmentBO {


    private List<SecondBean> second;
    private List<SowingBean> sowing;

    public List<SecondBean> getSecond() {
        return second;
    }

    public void setSecond(List<SecondBean> second) {
        this.second = second;
    }

    public List<SowingBean> getSowing() {
        return sowing;
    }

    public void setSowing(List<SowingBean> sowing) {
        this.sowing = sowing;
    }

    public static class SecondBean {
        /**
         * id : 45
         * name : 食品
         */

        private int id;
        private String name;

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
    }

    public static class SowingBean {
        /**
         * id : 69
         * address :
         * pictureroot :
         */

        private int id;
        private String address;
        private String pictureroot;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPictureroot() {
            return pictureroot;
        }

        public void setPictureroot(String pictureroot) {
            this.pictureroot = pictureroot;
        }
    }
}
