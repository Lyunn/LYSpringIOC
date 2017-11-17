package com.ly.myspring;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class ClassPathXml implements ApplicationContext {

	private String configPath;

	public ClassPathXml() {
		configPath = "applicationContext.xml";
	}

	public ClassPathXml(String configPath) {
		super();
		this.configPath = configPath;
	}

	/*
	 * beans -->Map<String,Map<String,Object>> bean --> Map<String,Object>
	 * property --> Map<String,String>
	 * 
	 */
	// 读取配置文件 并生成map集合返回
	public Map<String, Map<String, Object>> readConfig() throws Exception {
		Map<String, Map<String, Object>> beans = null;
		Map<String, Object> bean = null;
		Map<String, Map<String, String>> property = null;
		Map<String, String> propObject = null;
		String name = null;
		String value = null;
		String ref = null;
		XmlPullParser xmlPull = XmlPullParserFactory.newInstance().newPullParser();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(configPath);
		// System.out.println("in" + in);
		xmlPull.setInput(in, "utf-8");
		int eventType = xmlPull.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				beans = new HashMap<String, Map<String, Object>>();
				break;
			case XmlPullParser.START_TAG:
				if ("bean".equals(xmlPull.getName())) {
					bean = new HashMap<String, Object>();
					String id = xmlPull.getAttributeValue(null, "id");
					String className = xmlPull.getAttributeValue(null, "class");
					bean.put("id", id);
					bean.put("class", className);
				} else if ("property".equals(xmlPull.getName())) {
					
					if (property == null) {
						property = new HashMap<String, Map<String, String>>();
					}
					propObject = new HashMap<String, String>();
					name = xmlPull.getAttributeValue(null, "name");
					value = xmlPull.getAttributeValue(null, "value");
					ref = xmlPull.getAttributeValue(null, "ref");
					if (value != null) {
						propObject.put("value", value);
					}else if(ref != null){
						propObject.put("ref", ref);
					}
					property.put(name, propObject);
				}
				break;
			case XmlPullParser.END_TAG:
				if ("bean".equals(xmlPull.getName())) {
					bean.put("property", property);
					beans.put((String) bean.get("id"), bean);
					property = null;
					bean = null;
				}else if("property".equals(xmlPull.getName())){
					propObject = null;
					value = null;
					ref = null;
					name = null;
				}
				break;
			}
			eventType = xmlPull.next();
		}
		return beans;
	}

	// 通过反射生成对象
	@Override
	public Object getBean(String id) {
		Object obj = null;
		Map<String, Map<String, Object>> beans = null;
		Map<String, Object> bean = null;
		Map<String, Map<String, String>> property = null;
		Map<String, String> propObject = null;
		try {
			beans = readConfig();
			bean = beans.get(id);
			if (bean == null) {
				System.out.println("配置文件中无此对象");
			} else {
				String beanClass = (String) bean.get("class");
				Class cls = Class.forName(beanClass);
				obj = cls.newInstance();
				property = (Map<String, Map<String, String>>) bean.get("property");
				if (property != null && property.size() > 0) {
					for (Map.Entry<String, Map<String, String>> prop : property.entrySet()) {
						String propName = prop.getKey();
						propObject = prop.getValue();
						//遍历属性propperty
						for (Map.Entry<String, String> propObj : propObject.entrySet()) {
							String propObjectName = propObj.getKey();
							String propValue = propObj.getValue();
							String methodName = new StringBuilder().append("set")
									.append(propName.substring(0, 1).toUpperCase()).append(propName.substring(1))
									.toString();
							Object propRealValue = null;
							Class propType = cls.getDeclaredField(propName).getType();
							//判断property是value 还是 ref
							if ("value".equals(propObjectName)) {
								
								switch (propType.getName()) {
								case "int":
									propRealValue = Integer.parseInt(propValue);
									break;
								case "double":
									propRealValue = Double.parseDouble(propValue);
									break;
								default:
									propRealValue = propValue;
									break;
								}
							}else if("ref".equals(propObjectName)){
								propRealValue = Class.forName((String)readConfig().get(propName).get("class")).newInstance();
								propRealValue = getBean(propName);
								
							}
							Method setMethod = cls.getMethod(methodName, new Class[] { propType });
							setMethod.invoke(obj, propRealValue);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

}
