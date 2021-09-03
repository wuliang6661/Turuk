package com.haoyigou.hyg.entity;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

/**
 * Created by Witness on 2020-04-20
 * Describe:
 */
public class SendCircleBean {

    private List<LocalMedia> fileupload;
    private String type;// 1 图片  2 视频
    private String productId;
    private String text;
    private String sendType;//发布类型  1  推荐发布   2  买家秀发布
    private File videoImg;

    public List<LocalMedia> getFileupload() {
        return fileupload;
    }

    public void setFileupload(List<LocalMedia> fileupload) {
        this.fileupload = fileupload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public File getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(File videoImg) {
        this.videoImg = videoImg;
    }
}
