package com.fantaike.tools.utils;

import cn.hutool.log.Log;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName(类名) : JacksonUtil
 * @Description(描述) : Jackson工具类---json和对象的互转
 * @author(作者) ：Cola
 * @date (开发日期)      ：2017-8-8 下午4:23:11
 */
public class JacksonUtil {
    private static final Log logger = Log.get();
    public static ObjectMapper objectMapper;


    /**
     * @param jsonStr
     * @param valueType
     * @return T
     * @Description(功能描述) :  将JSON字符串转换为JavaBean
     * @author(作者) ：Cola
     * @date (开发日期)      ：2017-8-8 下午4:23:11
     */
    public static <T> T jsonToObject(String jsonStr, Class<T> valueType) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            logger.error("异常：", e);
        }
        return null;
    }


    /**
     * @param object
     * @return String
     * @Description(功能描述) :  JavaBean转Json
     * @author(作者) ：Cola
     * @date (开发日期)      ：2017-8-8 下午4:23:11
     */
    public static String ObjectToJson(Object object) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("异常：", e);
        }
        return null;
    }

    /**
     * @param json
     * @return Map<String, Object>
     * @throws :
     * @Description(功能描述) :  json转map
     * @author(作者) ：Cola
     * @date (开发日期)      ：2017-8-8 下午4:23:11
     */
    public static Map<String, Object> jsonToMap(String json) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            logger.error("异常：", e);
        }
        return null;
    }

    /**
     * @param map
     * @return String
     * @throws :
     * @Description(功能描述) :  map转json
     * @author(作者) ：Cola
     * @date (开发日期)      ：2017-8-8 下午4:23:11
     */
    public static String mapToJson(Map<String, Object> map) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            logger.error("异常：", e);
        }
        return null;
    }

    /**
     * @param json
     * @param collectionClass
     * @param elementClasses
     * @return List<?>
     * @throws :
     * @Description(功能描述) :  json转list
     * @author(作者) ：Cola
     * @date (开发日期)      ：2017-8-8 下午4:23:11
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToCollection(String json, Class<?> collectionClass, Class<T> elementClasses) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        JavaType javaType = getCollectionType(collectionClass, elementClasses);
        List<T> list = null;
        try {
            list = objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            logger.error("异常：", e);
        }
        return list;
    }

    /**
     * @param collectionClass
     * @param elementClasses
     * @return JavaType
     * @throws :
     * @Description(功能描述) :  获取泛型的Collection Type
     * @author(作者) ：Cola
     * @date (开发日期)        ：2017-8-8 下午4:23:11
     */
    @SuppressWarnings("deprecation")
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    /**
     * @param type 实体类class对象类型
     * @param map  要转化的map
     * @return Object 强转成想要的实体类型
     */
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        // 获取类属性
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        // 创建 JavaBean 对象
        Object obj = type.newInstance();

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            //获取属性名
            String propertyName = descriptor.getName();
            //获取属性类型
            String propertyType = descriptor.getPropertyType().getTypeName();
            if (map.containsKey(propertyName)) {
                System.out.println("你好，我是：" + propertyName + "! 我长这个样：" + map.get(propertyName));
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object[] args = new Object[1];
                //如果数据库类型是BigDecimal，传来不可以是非BigDecimal类型或者为空和null，会产生参数类型不匹配异常，
                //这里将需要类型为BigDecimal的前台数据做类型转换
                if ("java.math.BigDecimal".equals(propertyType)) {
                    if (map.get(propertyName) == null || "".equals(map.get(propertyName))) {
                        continue;
                    }
                    BigDecimal value = new BigDecimal(map.get(propertyName).toString());
                    args[0] = value;
                } else {
                    Object value = map.get(propertyName);
                    args[0] = value != null ? value.toString() : "";
                }
                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }
}
