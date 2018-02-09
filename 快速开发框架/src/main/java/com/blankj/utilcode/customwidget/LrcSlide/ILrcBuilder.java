package com.blankj.utilcode.customwidget.LrcSlide;


import com.blankj.utilcode.customwidget.LrcSlide.impl.LrcRow;

import java.util.List;

/**
 * 解析歌词，得到LrcRow的集合
 */
public interface ILrcBuilder {
    List<LrcRow> getLrcRows(String rawLrc);
}
