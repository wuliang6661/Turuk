package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2017/10/20.
 * <p>
 * 专题精选
 */

public class ZhuanTiBO implements Serializable {


    /**
     * cullpro : [{"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201701/945znYl.png","id":62,"name":"【测试】11112.升特五彩缤纷有氧运动走步weii"},{"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201709/970ZIfb.jpg","id":97,"name":"20170209测试用"}]
     * cull : {"title":"第一次","id":196,"url":"","shortDesc":"11","minPrice":0,"pic":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201710/626kjuG.jpg"}
     */

    private CullBo cull;
    private List<MonthNewsEntry> cullpro;

    public CullBo getCull() {
        return cull;
    }

    public void setCull(CullBo cull) {
        this.cull = cull;
    }

    public List<MonthNewsEntry> getCullpro() {
        return cullpro;
    }

    public void setCullpro(List<MonthNewsEntry> cullpro) {
        this.cullpro = cullpro;
    }

    public static class CullBo {
        /**
         * title : 第一次
         * id : 196
         * url :
         * shortDesc : 11
         * minPrice : 0.0
         * pic : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201710/626kjuG.jpg
         */

        private String title;
        private int id;
        private String url;
        private String shortDesc;
        private double minPrice;
        private String pic;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getShortDesc() {
            return shortDesc;
        }

        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }

        public double getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(double minPrice) {
            this.minPrice = minPrice;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }


}
