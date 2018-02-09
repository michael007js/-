package com.blankj.utilcode.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/5/20.
 */
public class ListUtils {


    /** 默认加入分隔符 **/
    public static final String DEFAULT_JOIN_SEPARATOR = ",";

    private ListUtils() {
        throw new AssertionError();
    }

    /**
     * 获取列表的大小
     *
     * <pre>
     * getSize(null)   =   0;
     * getSize({})     =   0;
     * getSize({1})    =   1;
     * </pre>
     *
     * @param <V>
     * @param sourceList
     * @return 如果是null或空列表,返回0, else return {@link List#size()}.
     */
    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null ? 0 : sourceList.size();
    }

    /**
     * 判断列表是null或它的大小是0
     *
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
     * </pre>
     *
     * @param <V>
     * @param sourceList
     * @return if list is null or its size is 0, return true, else return false.
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    /**
     * 比较两个列表是否相同
     *
     * <pre>
     * isEquals(null, null) = true;
     * isEquals(new ArrayList&lt;String&gt;(), null) = false;
     * isEquals(null, new ArrayList&lt;String&gt;()) = false;
     * isEquals(new ArrayList&lt;String&gt;(), new ArrayList&lt;String&gt;()) = true;
     * </pre>
     *
     * @param <V>
     * @param actual
     * @param expected
     * @return
     */
    public static <V> boolean isEquals(ArrayList<V> actual, ArrayList<V> expected) {
        if (actual == null) {
            return expected == null;
        }
        if (expected == null) {
            return false;
        }
        if (actual.size() != expected.size()) {
            return false;
        }

        for (int i = 0; i < actual.size(); i++) {
            if (!ObjectUtils.isEquals(actual.get(i), expected.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 加入列表字符串分隔符 ","
     *
     * <pre>
     * join(null)      =   "";
     * join({})        =   "";
     * join({a,b})     =   "a,b";
     * </pre>
     *
     * @param list
     * @return 加入列表字符串分隔符 ",". 如果列表为空, return ""
     */
    public static String join(List<String> list) {
        return join(list, DEFAULT_JOIN_SEPARATOR);
    }

    /**
     * 加入列表字符串
     *
     * <pre>
     * join(null, '#')     =   "";
     * join({}, '#')       =   "";
     * join({a,b,c}, ' ')  =   "abc";
     * join({a,b,c}, '#')  =   "a#b#c";
     * </pre>
     *
     * @param list
     * @param separator
     * @return 加入列表字符串。如果列表为空,返回 ""
     */
    public static String join(List<String> list, char separator) {
        return join(list, new String(new char[] {separator}));
    }

    /**
     * 加入列表字符串。如果分隔符为空,使用 {@link #DEFAULT_JOIN_SEPARATOR}
     *
     * <pre>
     * join(null, "#")     =   "";
     * join({}, "#$")      =   "";
     * join({a,b,c}, null) =   "a,b,c";
     * join({a,b,c}, "")   =   "abc";
     * join({a,b,c}, "#")  =   "a#b#c";
     * join({a,b,c}, "#$") =   "a#$b#$c";
     * </pre>
     *
     * @param list
     * @param separator
     * @return 加入列表分隔符字符串。如果列表为空,返回 ""
     */
    public static String join(List<String> list, String separator) {
        return list == null ? "" : TextUtils.join(separator, list);
    }

    /**
     * 不同的条目添加到列表中
     *
     * @param <V>
     * @param sourceList
     * @param entry
     * @return 如果在sourceList条目已经存在,返回false,否则添加,返回true.
     */
    public static <V> boolean addDistinctEntry(List<V> sourceList, V entry) {
        return (sourceList != null && !sourceList.contains(entry)) ? sourceList.add(entry) : false;
    }

    /**
     *从list2向list1添加不同的条目
     *
     * @param <V>
     * @param sourceList
     * @param entryList
     * @return 被添加的条目数量
     */
    public static <V> int addDistinctList(List<V> sourceList, List<V> entryList) {
        if (sourceList == null || isEmpty(entryList)) {
            return 0;
        }

        int sourceCount = sourceList.size();
        for (V entry : entryList) {
            if (!sourceList.contains(entry)) {
                sourceList.add(entry);
            }
        }
        return sourceList.size() - sourceCount;
    }

    /**
     * 删除重复的条目列表
     *
     * @param <V>
     * @param sourceList
     * @return 被删除的条目数量
     */
    public static <V> int distinctList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return 0;
        }

        int sourceCount = sourceList.size();
        int sourceListSize = sourceList.size();
        for (int i = 0; i < sourceListSize; i++) {
            for (int j = (i + 1); j < sourceListSize; j++) {
                if (sourceList.get(i).equals(sourceList.get(j))) {
                    sourceList.remove(j);
                    sourceListSize = sourceList.size();
                    j--;
                }
            }
        }
        return sourceCount - sourceList.size();
    }

    /**
     * 非空条目添加到列表中
     *
     * @param sourceList
     * @param value
     * @return <ul>
     *         <li>如果sourceList为空,返回false</li>
     *         <li>如果值为空,返回false</li>
     *         <li>return {@link List#add(Object)}</li>
     *         </ul>
     */
    public static <V> boolean addListNotNullValue(List<V> sourceList, V value) {
        return (sourceList != null && value != null) ? sourceList.add(value) : false;
    }

//    /**
//     * @see {@link ArrayUtils#getLast(Object[], Object, Object, boolean)} defaultValue is null, isCircle is true
//     */
//    @SuppressWarnings("unchecked")
//    public static <V> V getLast(List<V> sourceList, V value) {
//        return (sourceList == null) ? null : (V) ArrayUtils.getLast(sourceList.toArray(), value, true);
//    }

//    /**
//     * @see {@link ArrayUtils#getNext(Object[], Object, Object, boolean)} defaultValue is null, isCircle is true
//     */
//    @SuppressWarnings("unchecked")
//    public static <V> V getNext(List<V> sourceList, V value) {
//        return (sourceList == null) ? null : (V) ArrayUtils.getNext(sourceList.toArray(), value, true);
//    }

    /**
     * 反转列表
     *
     * @param <V>
     * @param sourceList
     * @return
     */
    public static <V> List<V> invertList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }

        List<V> invertList = new ArrayList<V>(sourceList.size());
        for (int i = sourceList.size() - 1; i >= 0; i--) {
            invertList.add(sourceList.get(i));
        }
        return invertList;
    }
}
