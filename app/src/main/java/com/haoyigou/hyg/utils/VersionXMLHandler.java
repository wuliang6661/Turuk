package com.haoyigou.hyg.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class VersionXMLHandler extends DefaultHandler{
	private Map<String,String> versionMap;
	private StringBuffer buffer =new StringBuffer();
	

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch,start,length);
		super.characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(localName.equals("name")){
			versionMap.put("name", buffer.toString().trim());
		}else if(localName.equals("versionCode")){
			versionMap.put("versionCode", buffer.toString().trim());
		}else if(localName.equals("versionName")){
			versionMap.put("versionName", buffer.toString().trim());
		}else if(localName.equals("minVersionCode")){
			versionMap.put("minVersionCode", buffer.toString().trim());
		}else if(localName.equals("minVersionName")){
			versionMap.put("minVersionName", buffer.toString().trim());
		}else if(localName.equals("updateMessage")){
			versionMap.put("updateMessage", buffer.toString().trim());
		}else if(localName.equals("url")){
			versionMap.put("url", buffer.toString().trim());
		}else if(localName.equals("market")){
			versionMap.put("market", buffer.toString().trim());
		}
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startDocument() throws SAXException {
		versionMap = new HashMap<String,String>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		buffer.setLength(0);
		super.startElement(uri, localName, qName, attributes);
	}

	public Map<String, String> getVersionMap() {
		return versionMap;
	}

	public void setVersionMap(Map<String, String> versionMap) {
		this.versionMap = versionMap;
	}

}
