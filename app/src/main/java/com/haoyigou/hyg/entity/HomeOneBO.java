package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2017/10/18.
 * <p>
 * 首页第一部分bean
 */

public class HomeOneBO implements Serializable {


    /**
     * lunbopics : [{"address":"http://ruiduo.happydoit.com:8080/mycoupon/getdis?discode=1252261035&source=1","id":41,"pictureroot":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201610/14SiYJ.jpg"},{"address":"http://ruiduo.happydoit.com:8080/mycoupon/getdis?discode=1042951033&source=0","id":37,"pictureroot":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201609/545NKnu.jpg"},{"address":"http://ruiduo.happydoit.com:8080/mycoupon/getdis?discode=0539341032&source=1","id":44,"pictureroot":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201610/518seZE.jpg"}]
     * lunbotime : 5
     * video : []
     * ad : []
     * icons : []
     * havemes : 0
     */

    private int lunbotime;
    private int havemes;
    private List<BannerBO> lunbopics;
    private VideoEntry video;
    private AdBo ad;
    private List<MenuBO> icons;
    private String grouptip;
    private PageSettingBean pageSetting;

    public int getLunbotime() {
        return lunbotime;
    }

    public void setLunbotime(int lunbotime) {
        this.lunbotime = lunbotime;
    }

    public int getHavemes() {
        return havemes;
    }

    public void setHavemes(int havemes) {
        this.havemes = havemes;
    }

    public List<BannerBO> getLunbopics() {
        return lunbopics;
    }

    public void setLunbopics(List<BannerBO> lunbopics) {
        this.lunbopics = lunbopics;
    }

    public VideoEntry getVideo() {
        return video;
    }

    public void setVideo(VideoEntry video) {
        this.video = video;
    }

    public AdBo getAd() {
        return ad;
    }

    public void setAd(AdBo ad) {
        this.ad = ad;
    }

    public List<MenuBO> getIcons() {
        return icons;
    }

    public void setIcons(List<MenuBO> icons) {
        this.icons = icons;
    }


    public static class AdBo implements Serializable {


        /**
         * id : 8
         * img : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201710/863RsjR.png
         * title : 广告牌
         * url : https://www.baidu.com/
         */

        private int id;
        private String img;
        private String title;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public void setGrouptip(String grouptip){
        this.grouptip = grouptip;
    }

    public String getGrouptip(){
        return grouptip;
    }

    public PageSettingBean getPageSetting() {
        return pageSetting;
    }

    public void setPageSetting(PageSettingBean pageSetting) {
        this.pageSetting = pageSetting;
    }

    public static class PageSettingBean{

        private BillboardSettingBean billboardSetting;
        private IconSettingBean iconSetting;
        private BannerSettingBean bannerSetting;

        public BillboardSettingBean getBillboardSetting() {
            return billboardSetting;
        }

        public void setBillboardSetting(BillboardSettingBean billboardSetting) {
            this.billboardSetting = billboardSetting;
        }

        public IconSettingBean getIconSetting() {
            return iconSetting;
        }

        public void setIconSetting(IconSettingBean iconSetting) {
            this.iconSetting = iconSetting;
        }

        public BannerSettingBean getBannerSetting() {
            return bannerSetting;
        }

        public void setBannerSetting(BannerSettingBean bannerSetting) {
            this.bannerSetting = bannerSetting;
        }

        public static class BillboardSettingBean{

            private String backpic;
            private String color;
            private String pic;

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

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }

        public static class IconSettingBean{
            private String backpic;
            private String color;
            private String pic;

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

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }

        public static class BannerSettingBean{
            private String backpic;
            private String color;
            private String pic;

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

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }
}
