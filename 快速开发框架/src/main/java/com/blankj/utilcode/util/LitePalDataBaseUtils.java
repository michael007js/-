package com.blankj.utilcode.util;

import android.content.ContentValues;
import android.database.Cursor;

import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.crud.DataSupport.where;


/**
 * LitePal数据库的常用封装  参考http://blog.csdn.net/guolin_blog/article/details/40153833
 * Created by leilei on 2017/5/18.
 */

public class LitePalDataBaseUtils {
//************************************************************************↓↓储存↓↓************************************************************************

    /**
     * 储存,返回储存结果
     *
     * @param table 继承于DataSupport的表模型
     * @return 成功或失败
     */
    public static boolean save(DataSupport table) {
        return table.save();
    }

    /**
     * 储存,如果出现错误,则抛异常
     *
     * @param table 继承于DataSupport的表模型对象
     */
    public static void saveThrows(DataSupport table) {
        table.saveThrows();
    }

    /**
     * 储存集合数据
     *
     * @param l 继承于DataSupport的表模型对象集合
     * @return
     */
    public static void saveAll(List<DataSupport> l) {
        DataSupport.saveAll(l);
    }


//************************************************************************↑↑储存↑↑************************************************************************
//************************************************************************↓↓修改↓↓************************************************************************

    /**
     * 更新指定下标的值
     *
     * @param table 继承于DataSupport的表模型对象
     * @param index 下标
     */
    public static void updata(DataSupport table, long index) {
        table.update(index);
    }

    /**
     * 更新所有指定字段的值
     *
     * @param table     继承于DataSupport的表模型对象
     * @param targetKay 要修改的目标字段数组(与modify一一对应)
     * @param modify    要修改的值数组(与targetKay一一对应)
     * @return
     */
    public static int updateAll(DataSupport table, String[] targetKay, String[] modify) {
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        for (int i = 0; i < targetKay.length; i++) {
            s1.append(targetKay[i]);
            s1.append(" = ?");
            if (i < targetKay.length) {
                s1.append(" and ");
            }
        }
        for (int i = 0; i < modify.length; i++) {
            s2.append(targetKay[i]);
            s2.append(" = ?");
            if (i < targetKay.length) {
                s2.append(" and ");
            }
        }
        return table.updateAll(s1.toString() , s2.toString());
    }


//************************************************************************↑↑修改↑↑************************************************************************
//************************************************************************↓↓删除↓↓************************************************************************

    /**
     * @param modelClass 继承于DataSupport的表模型对象类名
     * @param index      下标
     * @return
     */
    public static int delete(Class<DataSupport> modelClass, int index) {
        return DataSupport.delete(modelClass, index);
    }

    /**
     * 按指定字段来删除
     *
     * @param table     继承于DataSupport的表模型对象
     * @param targetKay 要删除的目标字段数组(与modify一一对应)
     * @param modify    要删除的值数组(与targetKay一一对应)
     * @return
     */
    public static int deleteAll(DataSupport table, String[] targetKay, String[] modify) {
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        for (int i = 0; i < targetKay.length; i++) {
            s1.append(targetKay[i]);
            s1.append(" = ?");
            if (i < targetKay.length) {
                s1.append(" and ");
            }
        }
        for (int i = 0; i < modify.length; i++) {
            s2.append(targetKay[i]);
            s2.append(" = ?");
            if (i < targetKay.length) {
                s2.append(" and ");
            }
        }
        return table.deleteAll(s1.toString() , s2.toString());
    }


//************************************************************************↑↑删除↑↑************************************************************************
//************************************************************************简单↓↓查询************************************************************************

    /**
     * 按下标查询
     *
     * @param modelClass 继承于DataSupport的表模型对象类名
     * @param index
     * @return
     */
    public static DataSupport find(Class<DataSupport> modelClass, long index) {
        return DataSupport.find(modelClass, index);
    }


    /**
     * 查询第一个
     *
     * @param modelClass 继承于DataSupport的表模型对象类名
     * @return
     */
    public static DataSupport findFirst(Class<DataSupport> modelClass) {
        return DataSupport.findFirst(modelClass);
    }


    /**
     * 查询最后一个
     *
     * @param modelClass 继承于DataSupport的表模型对象类名
     * @return
     */
    public static DataSupport findLast(Class<DataSupport> modelClass) {
        return DataSupport.findLast(modelClass);
    }

    /**
     * 查询所有
     *
     * @param modelClass 继承于DataSupport的表模型对象类名
     * @param indexs     要查询的下标集合
     * @return
     */
    public static List<DataSupport> findAll(Class<DataSupport> modelClass, long[] indexs) {
        return DataSupport.findAll(modelClass, indexs);
    }

    /**
     * 查询所有
     *
     * @param modelClass 继承于DataSupport的表模型对象类名
     * @return
     */
    public static List<DataSupport> findAll(Class<DataSupport> modelClass) {
        return DataSupport.findAll(modelClass);
    }
//************************************************************************简单↑↑查询************************************************************************
//************************************************************************连缀↓↓查询************************************************************************

    /**
     * 按条件查询 (此方法会将表中所有的列都查询出来)
     * 效果等同于select * from table_name where column > 0;
     *
     * @param conditionKey   条件--指定被比较的Key
     * @param condition      条件--指定查询条件 可以是大于,小于,等于...
     * @param conditionValue 条件--指定被比较的值
     * @param modelClass     继承于DataSupport的表模型对象类名
     * @return
     */
    public static List<DataSupport> findByCondition(String conditionKey, String condition, String conditionValue, Class<DataSupport> modelClass) {
        return where(conditionKey + condition + "?", conditionValue).find(modelClass);
    }

    /**
     * 按条件查询(带指定目标列)
     * 效果等同于select column_1,column_2,column_3 from table_name where column_4 > 0;
     *
     * @param target         指定要查询的目标列
     * @param conditionKey   条件--指定被比较的Key
     * @param condition      条件--指定查询条件 可以是大于,小于,等于...
     * @param conditionValue 条件--指定被比较的值
     * @param modelClass     继承于DataSupport的表模型对象类名
     * @return
     */
    public static List<DataSupport> findByCondition(String[] target, String conditionKey, String condition, String conditionValue, Class<DataSupport> modelClass) {
        return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
    }

    /**
     * 按条件查询(带指定目标列,排序)
     * 效果等同于select column_1,column_2,column_3 from table_name where column_4 > 0 order by column_5 desc;
     *
     * @param target         指定要查询的目标列
     * @param sortKey        将此字段指定为排序字段
     * @param sequence       输出顺序,asc表示正序排序，desc表示倒序排序
     * @param conditionKey   条件--指定被比较的Key
     * @param condition      条件--指定查询条件 可以是大于,小于,等于...
     * @param conditionValue 条件--指定被比较的值
     * @param modelClass     继承于DataSupport的表模型对象类名
     * @return
     */
    public static List<DataSupport> findByCondition(String[] target, String sortKey, String sequence, String conditionKey, String condition, String conditionValue, Class<DataSupport> modelClass) {

        if (sortKey == null) {
            return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
        } else {
            if (!StringUtils.isEmpty(sequence)) {
                if ("asc".equals(sequence) || "desc".equals(sequence)) {
                    return DataSupport.select(target).order(sortKey + " " + sequence).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                } else {
                    return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                }
            } else {
                return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
            }
        }

    }

    /**
     * 按条件查询(带指定目标列,排序,截断)
     * 效果等同于select column_1,column_2,column_3 from table_name where column_4 > 0 order by column_5 desc limit 10;
     *
     * @param target         指定要查询的目标列
     * @param sortKey        将此字段指定为排序字段
     * @param sequence       输出顺序,asc表示正序排序，desc表示倒序排序
     * @param conditionKey   条件--指定被比较的Key
     * @param condition      条件--指定查询条件 可以是大于,小于,等于...
     * @param conditionValue 条件--指定被比较的值
     * @param limitCount     要截取的条数
     * @param modelClass     继承于DataSupport的表模型对象类名
     * @return
     */
    public static List<DataSupport> findByConditionLimit(String[] target, String sortKey, String sequence, String conditionKey, String condition, String conditionValue, int limitCount, Class<DataSupport> modelClass) {

        if (sortKey == null) {
            if (limitCount > 0) {
                return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).find(modelClass);
            } else {
                return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
            }

        } else {
            if (!StringUtils.isEmpty(sequence)) {
                if ("asc".equals(sequence) || "desc".equals(sequence)) {
                    if (limitCount > 0) {
                        return DataSupport.select(target).order(sortKey + " " + sequence).where(conditionKey + condition + "?", conditionValue).limit(limitCount).find(modelClass);
                    } else {
                        return DataSupport.select(target).order(sortKey + " " + sequence).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                    }
                } else {
                    if (limitCount > 0) {
                        return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).find(modelClass);
                    } else {
                        return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                    }
                }
            } else {
                if (limitCount > 0) {
                    return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).find(modelClass);
                } else {
                    return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                }
            }
        }

    }


    /**
     * 按条件查询(带指定目标列,排序,截断,偏移量)
     * 效果等同于 select column_1,column_2,column_3 from table_name where column_4 > 0 order by column_5 desc limit 10;
     *
     * @param target         指定要查询的目标列
     * @param sortKey        将此字段指定为排序字段
     * @param sequence       输出顺序,asc表示正序排序，desc表示倒序排序
     * @param conditionKey   条件--指定被比较的Key
     * @param condition      条件--指定查询条件 可以是大于,小于,等于...
     * @param conditionValue 条件--指定被比较的值
     * @param limitCount     要截取的条数
     * @param offsetCount    偏移量,一般用于分页
     * @param modelClass     继承于DataSupport的表模型对象类名
     * @return
     */
    public static List<DataSupport> findByConditionLimitOffset(String[] target, String sortKey, String sequence, String conditionKey, String condition, String conditionValue, int limitCount, int offsetCount, Class<DataSupport> modelClass) {

        if (sortKey == null) {
            if (limitCount > 0) {
                if (offsetCount > 0) {
                    return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).offset(offsetCount).find(modelClass);
                } else {
                    return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).find(modelClass);
                }

            } else {
                if (offsetCount > 0) {
                    return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).offset(offsetCount).find(modelClass);
                } else {
                    return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                }
            }
        } else {
            if (!StringUtils.isEmpty(sequence)) {
                if ("asc".equals(sequence) || "desc".equals(sequence)) {
                    if (limitCount > 0) {
                        if (offsetCount > 0) {
                            return DataSupport.select(target).order(sortKey + " " + sequence).where(conditionKey + condition + "?", conditionValue).limit(limitCount).offset(offsetCount).find(modelClass);
                        } else {
                            return DataSupport.select(target).order(sortKey + " " + sequence).where(conditionKey + condition + "?", conditionValue).limit(limitCount).find(modelClass);
                        }
                    } else {
                        if (offsetCount > 0) {
                            return DataSupport.select(target).order(sortKey + " " + sequence).where(conditionKey + condition + "?", conditionValue).offset(offsetCount).find(modelClass);
                        } else {
                            return DataSupport.select(target).order(sortKey + " " + sequence).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                        }
                    }
                } else {
                    if (limitCount > 0) {
                        if (offsetCount > 0) {
                            return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).offset(offsetCount).find(modelClass);
                        } else {
                            return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).find(modelClass);
                        }
                    } else {
                        if (offsetCount > 0) {
                            return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).offset(offsetCount).find(modelClass);
                        } else {
                            return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                        }
                    }
                }
            } else {
                if (limitCount > 0) {
                    if (offsetCount > 0) {
                        return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).offset(offsetCount).find(modelClass);
                    } else {
                        return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).limit(limitCount).find(modelClass);
                    }
                } else {
                    if (offsetCount > 0) {
                        return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).offset(offsetCount).find(modelClass);
                    } else {
                        return DataSupport.select(target).where(conditionKey + condition + "?", conditionValue).find(modelClass);
                    }
                }
            }
        }

    }


    /**
     * 自由发挥
     *
     * @param sql sql语句
     * @return 数据库内的游标
     */
    public static Cursor findBySQL(String sql) {
        return DataSupport.findBySQL(sql);
    }
//************************************************************************连缀↑↑查询************************************************************************

}
