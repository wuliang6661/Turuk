package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Witness on 2020/7/14
 * Describe:
 */
public class UpdateBean implements Serializable {

    /**
     * status : 0
     * data : {"android":{"versionCode":12,"versionName":"1.1.2","updateMessage":"1.充值优惠。","minVersionCode":11,"minVersionName":"1.1.1","url":"https://shapp.oss-cn-hangzhou.aliyuncs.com/app/shCinema1.1.2.apk"},"updateMessage":"1.版本更新。","apple":"1.2.0","show":1,"isForce":1,"drawShow":1}
     * message : null
     * code : 1
     */

    private int status;
    private DataBean data;
    private String message;
    private int code;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {

        private AndroidBean android;
        private String updateMessage;
        private String apple;
        private int show;
        private int isForce;
        private int drawShow;

        public AndroidBean getAndroid() {
            return android;
        }

        public void setAndroid(AndroidBean android) {
            this.android = android;
        }

        public String getUpdateMessage() {
            return updateMessage;
        }

        public void setUpdateMessage(String updateMessage) {
            this.updateMessage = updateMessage;
        }

        public String getApple() {
            return apple;
        }

        public void setApple(String apple) {
            this.apple = apple;
        }

        public int getShow() {
            return show;
        }

        public void setShow(int show) {
            this.show = show;
        }

        public int getIsForce() {
            return isForce;
        }

        public void setIsForce(int isForce) {
            this.isForce = isForce;
        }

        public int getDrawShow() {
            return drawShow;
        }

        public void setDrawShow(int drawShow) {
            this.drawShow = drawShow;
        }

        public static class AndroidBean {
            /**
             * versionCode : 12
             * versionName : 1.1.2
             * updateMessage : 1.充值优惠。
             * minVersionCode : 11
             * minVersionName : 1.1.1
             * url : https://shapp.oss-cn-hangzhou.aliyuncs.com/app/shCinema1.1.2.apk
             */

            private int versionCode;
            private String versionName;
            private String updateMessage;
            private int minVersionCode;
            private String minVersionName;
            private String url;

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public String getUpdateMessage() {
                return updateMessage;
            }

            public void setUpdateMessage(String updateMessage) {
                this.updateMessage = updateMessage;
            }

            public int getMinVersionCode() {
                return minVersionCode;
            }

            public void setMinVersionCode(int minVersionCode) {
                this.minVersionCode = minVersionCode;
            }

            public String getMinVersionName() {
                return minVersionName;
            }

            public void setMinVersionName(String minVersionName) {
                this.minVersionName = minVersionName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
