package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/8.
 */
public class ActivityListEntity implements Serializable{
    private String distributorId;
    private String id;
    private String pic;
    private String type;

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
