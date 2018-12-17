package com;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext {

    Map<String, Object> map = new HashMap<String, Object>();

    public ClassPathXmlApplicationContext() throws Exception {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(path);
        String filePath = path + "applicationContext.xml";
        File file = new File(filePath);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);

        //得到根标签beans标签
        Element root = document.getRootElement();
        //得到beans标签下bean标签的集合
        List<Element> list = root.elements();
        for (Element element : list) {
            //根据属性名得到一个属性的对象
            //获取id字符串
            Attribute attrId = element.attribute("id");
            String id = attrId.getValue();
            //System.out.println(id);

            //获取class字符串
            Attribute attrClass = element.attribute("class");
            String cls = attrClass.getValue();
            //System.out.println(cls);
            //根据class字符串得到Class类的对象
            Class targetClass = Class.forName(cls);

            //初始化bean里这个“class”目标对象为空
            Object targetObject = null;

            //得到bean标签下子标签的集合
            List<Element> eleChildList = element.elements();
            for (Element eleChild : eleChildList) {
                //获取子标签里的ref对象，即注入对象（有依赖）
                Attribute eleChildRefObject = eleChild.attribute("ref");
                String objectName = eleChildRefObject.getValue();
                Object injectObject = map.get(objectName);
                //通过target这个class对象得到一个要注入的injectObject对象类型参数的构造方法
                Constructor constructor = targetClass.getConstructor(injectObject.getClass());
                //通过上面这样一个构造器最终得到了bean标签里“class”目标对象
                targetObject = constructor.newInstance(injectObject);
            }

            //如果bean标签里没有子标签，直接通过class类的对象得到目标class对象
            if(targetObject == null) {
                targetObject = targetClass.newInstance();
            }

            map.put(id, targetObject);
        }

    }

    public Object getBean(String name) throws Exception {
        return map.get(name);
    }

}
