package com.github.xesam.android.views.tag;

import android.view.View;
import android.view.ViewGroup;

public abstract class TagAdapter<T> implements ITagAdapter {

    // 获取指定位置的数据
    public abstract T getItem(int position);

    // 创建"更多"组件视图，可以返回 null 表示不显示"更多"组件
    public View getMoreView(ViewGroup parent) {
        return null;
    }
}