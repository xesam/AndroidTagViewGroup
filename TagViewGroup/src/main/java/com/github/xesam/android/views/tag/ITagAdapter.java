package com.github.xesam.android.views.tag;

import android.view.View;
import android.view.ViewGroup;

public interface ITagAdapter {
    int getCount();

    // 创建子视图
     View getView(int position, ViewGroup parent);

    // 创建"更多"组件视图，可以返回 null 表示不显示"更多"组件
    View getMoreView(ViewGroup parent);
}
