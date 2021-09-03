package com.haoyigou.hyg.utils;

import com.alibaba.fastjson.JSON;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.entity.UpdateBean;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.listener.IUpdateParseCallback;
import com.xuexiang.xupdate.proxy.IUpdateParser;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Witness on 2020/7/14
 * Describe:
 */
public class CustomUpdateParser implements IUpdateParser {

    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        VersionXMLHandler handler = new VersionXMLHandler();
        parser.parse(new ByteArrayInputStream(json.getBytes("UTF-8")) , handler);
        Map<String, String> versionMap = handler.getVersionMap();
        if (null != versionMap) {
            UpdateEntity updateEntity = new UpdateEntity();
            if (Integer.parseInt(Objects.requireNonNull(versionMap.get("versionCode"))) > GlobalApplication.currentVersionCode) {//更新
                updateEntity.setHasUpdate(true);
                if (Integer.parseInt(Objects.requireNonNull(versionMap.get("minVersionCode"))) > GlobalApplication.currentVersionCode) {//强制更新
                    updateEntity.setForce(true);
                }else {
                    updateEntity.setForce(false);
                }
                updateEntity.setIsIgnorable(false);
                updateEntity.setVersionCode(Integer.parseInt(Objects.requireNonNull(versionMap.get("versionCode"))));
                updateEntity.setVersionName(versionMap.get("versionName"));
                updateEntity.setUpdateContent(versionMap.get("updateMessage"));
                updateEntity.setDownloadUrl(versionMap.get("url"));
                updateEntity.setSize(0);
            }else {
                updateEntity.setHasUpdate(false);
            }
            return updateEntity;
        }
        return null;
    }

    @Override
    public void parseJson(String json, IUpdateParseCallback callback) throws Exception {

    }

    @Override
    public boolean isAsyncParser() {
        return false;
    }

}
