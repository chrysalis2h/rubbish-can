package com.fantaike.tools.log;

import java.util.Hashtable;

/**
 *  * @ClassName: TraceInfoObject
 *  * @Description: TraceInfoObject
 *  * @Author: HeJin
 *  * @Date: 2019\12\10 0010 15:21
 *  * @Version: v1.0 文件初始创建
 */
public class TraceInfoObject {
    final static TraceInfoObject mdc = new TraceInfoObject();

    static final int HT_SIZE = 7;

    boolean java1;

    Object tlm;

    private TraceInfoObject() {
        tlm = new ThreadLocalMap();
    }

    /**
     * Put a context value (the <code>o</code> parameter) as identified
     * with the <code>key</code> parameter into the current thread's
     * context map.
     *
     * <p>If the current thread does not have a context map it is
     * created as a side effect.
     */
    static
    public void put(String key, Object o) {
        if (mdc != null) {
            mdc.put0(key, o);
        }
    }

    /**
     * Get the context identified by the <code>key</code> parameter.
     *
     * <p>This method has no side effects.
     */
    static
    public Object get(String key) {
        if (mdc != null) {
            return mdc.get0(key);
        }
        return null;
    }

    /**
     * Remove the the context identified by the <code>key</code>
     * parameter.
     */
    static
    public void remove(String key) {
        if (mdc != null) {
            mdc.remove0(key);
        }
    }


    /**
     * Get the current thread's MDC as a hashtable. This method is
     * intended to be used internally.
     */
    public static Hashtable getContext() {
        if (mdc != null) {
            return mdc.getContext0();
        } else {
            return null;
        }
    }

    /**
     * Remove all values from the MDC.
     *
     * @since 1.2.16
     */
    public static void clear() {
        if (mdc != null) {
            mdc.clear0();
        }
    }


    private void put0(String key, Object o) {
        if (java1 || tlm == null) {
            return;
        } else {
            Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
            if (ht == null) {
                ht = new Hashtable(HT_SIZE);
                ((ThreadLocalMap) tlm).set(ht);
            }
            ht.put(key, o);
        }
    }

    private Object get0(String key) {
        if (java1 || tlm == null) {
            return null;
        } else {
            Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
            if (ht != null && key != null) {
                return ht.get(key);
            } else {
                return null;
            }
        }
    }

    private void remove0(String key) {
        if (!java1 && tlm != null) {
            Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
            if (ht != null) {
                ht.remove(key);
            }
        }
    }


    private Hashtable getContext0() {
        if (java1 || tlm == null) {
            return null;
        } else {
            return (Hashtable) ((ThreadLocalMap) tlm).get();
        }
    }

    private void clear0() {
        if (!java1 && tlm != null) {
            Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
            if (ht != null) {
                ht.clear();
            }
        }
    }
}
