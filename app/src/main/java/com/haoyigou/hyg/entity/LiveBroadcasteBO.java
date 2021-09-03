package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/30.
 */
public class LiveBroadcasteBO implements Serializable {


        private List<LiveBo> live;
        private List<PreviewBo> preview;
        private List<BackBo> back;

        public List<LiveBo> getLive() {
            return live;
        }

        public void setLive(List<LiveBo> live) {
            this.live = live;
        }

        public List<PreviewBo> getPreview() {
            return preview;
        }

        public void setPreview(List<PreviewBo> preview) {
            this.preview = preview;
        }

        public List<BackBo> getBack() {
            return back;
        }

        public void setBack(List<BackBo> back) {
            this.back = back;
        }

        public static class LiveBo {
            /**
             * addcount : 20
             * anchor : 新年
             * count : 0
             * headerurl : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201801/477NFtq.png
             * id : 69
             * lid : ysyon47xB8QD1Y6Q
             * livepic : 201801/7a560793cdecbda155bd67ba19d806a7.png
             * status : 1
             * title : 新年快乐
             * vid : 7GZW1HbyVnN4sVVb
             */

            private int addcount;
            private String anchor;
            private int count;
            private String headerurl;
            private int id;
            private String lid;
            private String livepic;
            private int status;
            private String title;
            private String vid;

            public int getAddcount() {
                return addcount;
            }

            public void setAddcount(int addcount) {
                this.addcount = addcount;
            }

            public String getAnchor() {
                return anchor;
            }

            public void setAnchor(String anchor) {
                this.anchor = anchor;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getHeaderurl() {
                return headerurl;
            }

            public void setHeaderurl(String headerurl) {
                this.headerurl = headerurl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLid() {
                return lid;
            }

            public void setLid(String lid) {
                this.lid = lid;
            }

            public String getLivepic() {
                return livepic;
            }

            public void setLivepic(String livepic) {
                this.livepic = livepic;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }
        }

        public static class PreviewBo {
            /**
             * addcount : 0
             * anchor : 22
             * count : 0
             * headerurl : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201801/343Bqsb.png
             * lid :
             * livepic : 201801/319308fe48c3d0c12aa6dd21a8c05170.png
             * status : 0
             * title : rtr
             * vid : 7AQQ5hKmaRvytEm4
             */

            private int addcount;
            private String anchor;
            private int count;
            private String headerurl;
            private String lid;
            private String livepic;
            private int status;
            private String title;
            private String vid;

            public int getAddcount() {
                return addcount;
            }

            public void setAddcount(int addcount) {
                this.addcount = addcount;
            }

            public String getAnchor() {
                return anchor;
            }

            public void setAnchor(String anchor) {
                this.anchor = anchor;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getHeaderurl() {
                return headerurl;
            }

            public void setHeaderurl(String headerurl) {
                this.headerurl = headerurl;
            }

            public String getLid() {
                return lid;
            }

            public void setLid(String lid) {
                this.lid = lid;
            }

            public String getLivepic() {
                return livepic;
            }

            public void setLivepic(String livepic) {
                this.livepic = livepic;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }
        }

        public static class BackBo {
            /**
             * addcount : 100
             * anchor : 东哥
             * channel :
             * count : 2
             * headerurl : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201712/534LuoN.png
             * lid : ysyon2N28jTi1uLK
             * livepic : 201712/cac6db24d8964817fe1af0d0e6004837.png
             * status : 3
             * title : 东哥直播
             * vid : Jq136UQVN146h4zJ
             */

            private int addcount;
            private String anchor;
            private String channel;
            private int count;
            private String headerurl;
            private String lid;
            private String livepic;
            private int status;
            private String title;
            private String vid;

            public int getAddcount() {
                return addcount;
            }

            public void setAddcount(int addcount) {
                this.addcount = addcount;
            }

            public String getAnchor() {
                return anchor;
            }

            public void setAnchor(String anchor) {
                this.anchor = anchor;
            }

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getHeaderurl() {
                return headerurl;
            }

            public void setHeaderurl(String headerurl) {
                this.headerurl = headerurl;
            }

            public String getLid() {
                return lid;
            }

            public void setLid(String lid) {
                this.lid = lid;
            }

            public String getLivepic() {
                return livepic;
            }

            public void setLivepic(String livepic) {
                this.livepic = livepic;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }
        }
    }

