package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/2/13.
 * <p>
 * 标签bean
 */

public class TabBean implements Serializable {

    //标签图片
    private String tabpic;

    //标签描述
    private String tabdesc;

    //标签标题
    private String tabtitle;

    public String getSupertitle() {
        return supertitle;
    }

    public void setSupertitle(String supertitle) {
        this.supertitle = supertitle;
    }

    private String supertitle;

    public String getTabpic() {
        return tabpic;
    }

    public void setTabpic(String tabpic) {
        this.tabpic = tabpic;
    }

    public String getTabdesc() {
        return tabdesc;
    }

    public void setTabdesc(String tabdesc) {
        this.tabdesc = tabdesc;
    }

    public String getTabtitle() {
        return tabtitle;
    }

    public void setTabtitle(String tabtitle) {
        this.tabtitle = tabtitle;
    }
}
