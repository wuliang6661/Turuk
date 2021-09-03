package com.haoyigou.hyg.entity;

import java.util.List;

/**
 * Created by Witness on 2020/10/27
 * Describe:
 */
public class HuiProductBean {

    private String backImg;
    private String itemname;
    private String backImgLink;
    private List<ListBean> list;
    private PageSettingBean pageSetting;

    public String getBackImg() {
        return backImg;
    }

    public void setBackImg(String backImg) {
        this.backImg = backImg;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getBackImgLink() {
        return backImgLink;
    }

    public void setBackImgLink(String backImgLink) {
        this.backImgLink = backImgLink;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public PageSettingBean getPageSetting() {
        return pageSetting;
    }

    public void setPageSetting(PageSettingBean pageSetting) {
        this.pageSetting = pageSetting;
    }

    public static class ListBean {

        private String itemImg;
        private String itemImgLink;

        public String getItemImg() {
            return itemImg;
        }

        public void setItemImg(String itemImg) {
            this.itemImg = itemImg;
        }

        public String getItemImgLink() {
            return itemImgLink;
        }

        public void setItemImgLink(String itemImgLink) {
            this.itemImgLink = itemImgLink;
        }
    }

    public static class PageSettingBean{

        private DoubleElevenSettingBean doubelElevenSetting;

        public DoubleElevenSettingBean getDoubelElevenSetting() {
            return doubelElevenSetting;
        }

        public void setDoubelElevenSetting(DoubleElevenSettingBean doubelElevenSetting) {
            this.doubelElevenSetting = doubelElevenSetting;
        }

        public static class DoubleElevenSettingBean{

            private String backpic;
            private String color;


            public String getBackpic() {
                return backpic;
            }

            public void setBackpic(String backpic) {
                this.backpic = backpic;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }
    }

}
