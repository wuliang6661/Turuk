package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/10.
 */
public class SavePersonalInfomationEntity implements Serializable {
    private String saveType;//保存数据类型saveType：值为1，2，3，4，5（1头像   2店铺名字  3 昵称  4 性别  5 生日）
    private String distributorId;//分销商id
    private String relative_ur;//保存头像时
    private String name;//保存店名时
    private String nickname;//保存昵称时
    private String sex;//保存性别时
    private String birthday;//生日
    private String localPath;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getRelative_ur() {
        return relative_ur;
    }

    public void setRelative_ur(String relative_ur) {
        this.relative_ur = relative_ur;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
