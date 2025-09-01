package com.github.xesam.android.views.tag;

import android.view.View;
import android.view.ViewGroup;

public abstract class TagAdapter<T> {

    // 获取子视图数量
    public abstract int getCount();

    // 获取指定位置的数据
    public abstract T getItem(int position);

    // 创建子视图
    protected abstract View getView(int position, ViewGroup parent);

    // 创建"更多"组件视图，可以返回 null 表示不显示"更多"组件
    protected View getMoreView(ViewGroup parent) {
        return null;
    }
}