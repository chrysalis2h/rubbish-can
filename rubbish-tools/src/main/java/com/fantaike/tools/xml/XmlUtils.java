/**
 *
 */
package com.fantaike.tools.xml;

import cn.hutool.log.Log;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * XML工具类
 *
 * @author wangqi
 *
 */
public final class XmlUtils {
    private static final Log logger = Log.get();
    private static XStream xstream;

    static {
        setXstream(new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_"))));
        getXstream().autodetectAnnotations(true);
    }

    public static String toXML(Object obj) {
        getXstream().aliasSystemAttribute(null, "class");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            getXstream().toXML(obj, writer);
            String xml = outputStream.toString("UTF-8");
            return xml;
        } catch (UnsupportedEncodingException e) {
            logger.error("异常：", e);
        } catch (IOException e) {
            logger.error("异常：", e);
        }
        return null;
    }

    public static Object fromXML(String xml, Object... alias) {
        XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
        xstream.autodetectAnnotations(true);
        for (int i = 0; i < alias.length; i = i + 2) {
            xstream.alias(alias[i].toString(), (Class) alias[i + 1]);
        }
        return xstream.fromXML(xml);
    }

    public static String toXML(Object obj, Object... alias) {
        for (int i = 0; i < alias.length; i = i + 2) {
            getXstream().alias(alias[i].toString(), (Class) alias[i + 1]);
        }
        return getXstream().toXML(obj);
    }

    public static XStream getXstream() {
        return xstream;
    }

    public static void setXstream(XStream xstream) {
        XmlUtils.xstream = xstream;
    }
}
