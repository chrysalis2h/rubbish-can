package com.fantaike.tools.utils;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

public class JaxbBinder {
	//多线程安全的Context.
	private JAXBContext jaxbContext;

	/**
	 * @param types 所有需要序列化的Root对象的类型.
	 */
	public JaxbBinder(Class<?>... types) {
		try {
			jaxbContext = JAXBContext.newInstance(types);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java Object->Xml.
	 *
	 * @param obj        java Object
	 * @param encoding   encoding 字符集编码(可为Null,默认GBK)
	 * @param formatFlag 是否格式化XML输出
	 * @return
	 */
	public String toXml(Object obj, String encoding, boolean formatFlag) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller(encoding, formatFlag).marshal(obj, writer);
			return writer.toString().trim();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java Object->Xml, 特别支持对Root Element是Collection的情形.
	 */
	@SuppressWarnings("rawtypes")
	public String toXml(Collection root, String rootName, String encoding, boolean formatFlag) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;
			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
					CollectionWrapper.class, wrapper);
			StringWriter writer = new StringWriter();
			createMarshaller(encoding, formatFlag).marshal(wrapperElement, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Xml->Java Object.
	 *
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller().unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建Marshaller,
	 *
	 * @param encoding   字符集编码(可为Null,默认GBK)
	 * @param FORMATFLAG 是否格式化输出
	 * @return
	 */
	public Marshaller createMarshaller(String encoding, boolean FORMATFLAG) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			//是否格式化输出
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, FORMATFLAG);
			if ("".equals(encoding) || encoding == null) {
				//encoding = IntfQueryConstant.ENCODE_GBK;
			}
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			return marshaller;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建UnMarshaller.
	 */
	public Unmarshaller createUnmarshaller() {
		try {
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 封装Root Element 是 Collection的情况.
	 */
	public static class CollectionWrapper {
		@SuppressWarnings("rawtypes")
		@XmlAnyElement
		protected Collection collection;
	}

}
