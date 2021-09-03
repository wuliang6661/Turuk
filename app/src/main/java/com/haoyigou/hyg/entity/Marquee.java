package com.haoyigou.hyg.entity;
/** 
 * description: Marquee
 * author: xiaocao
 * date: 17/8/2 下午3:48
*/

public class Marquee {
    private String title;
    private String imgUrl;

    public Marquee(String title,String imgUrl){
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
