package com.fantaike.tools.xml;

/**
 * Created by lenovo on 2017/7/20.
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XMLUtil {
    /**
     * TODO hejin 推荐直接使用 XmlUtil
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @Deprecated
    public static Map doXMLParse(String strxml) {
        /*strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = XMLUtil.getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;*/
        return new HashMap<>(16);
    }

    /**
     * TODO hejin 推荐直接使用 XmlUtil
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    @Deprecated
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        /*if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(XMLUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }*/

        return sb.toString();
    }

}
