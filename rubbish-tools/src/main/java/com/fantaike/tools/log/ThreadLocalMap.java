package com.fantaike.tools.log;

import java.util.Hashtable;

/**
 *  * @ClassName: ThreadLocalMap
 *  * @Description: ThreadLocalMap
 *  * @Author: HeJin
 *  * @Date: 2019\12\10 0010 15:22
 *  * @Version: v1.0 文件初始创建
 */
final public class ThreadLocalMap extends InheritableThreadLocal {

    @Override
    public final Object childValue(Object parentValue) {
        Hashtable ht = (Hashtable) parentValue;
        if (ht != null) {
            return ht.clone();
        } else {
            return null;
        }
    }
}
