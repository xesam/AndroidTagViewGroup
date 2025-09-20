# Android TagViewGroup

## 简介
Android TagViewGroup 是一个自定义的 ViewGroup，用于实现标签流式布局。它支持自动换行、行数限制、间距控制等功能，适合用于显示标签云、搜索历史等场景。

## 功能特性
- 流式布局：自动将子视图按行排列，当一行空间不足时自动换行
- 行数限制：可以设置最大显示行数，超出部分会被截断
- 间距控制：支持设置水平和垂直间距
- 更多按钮：当内容被截断时，可以显示"更多"按钮
- 自定义属性：支持在 XML 中通过属性设置 maxLines、horizontalSpacing、verticalSpacing

## 集成方式

### Gradle 依赖
以 `gradle` 为例，在模块目录的 `build.gradle` 文件中添加：

```gradle
	dependencies {
        implementation 'io.github.xesam:android-taggroupview:0.0.1'
	}
```

## 使用方法

### 1. 在 XML 布局中使用

```xml
<com.github.xesam.android.views.tag.TagViewGroup
    android:id="@+id/tag_view_group"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:horizontalSpacing="10dp"
    app:maxLines="3"
    app:verticalSpacing="10dp" />
```

### 2. 在代码中设置数据

```java
TagViewGroup tagViewGroup = findViewById(R.id.tag_view_group);

// 创建数据源
List<String> dataList = new ArrayList<>();
for (int i = 0; i < 40; i++) {
    dataList.add("Tag " + (i + 1));
}

// 设置适配器
tagViewGroup.setAdapter(new TagAdapter<String>() {
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    @Override
    protected View getView(int position, ViewGroup parent) {
        TextView textView = new TextView(MainActivity.this);
        textView.setText(getItem(position));
        textView.setPadding(20, 10, 20, 10);
        textView.setBackgroundResource(R.drawable.tag_background);
        return textView;
    }

    @Override
    protected View getMoreView(ViewGroup parent) {
        TextView moreView = new TextView(MainActivity.this);
        moreView.setText("更多");
        moreView.setPadding(20, 10, 20, 10);
        moreView.setBackgroundResource(R.drawable.more_background);
        return moreView;
    }
});

// 设置标签点击事件
tagViewGroup.setOnTagClickListener(new OnTagClickListener() {
    @Override
    public void onTagClick(View view, int position) {
        Toast.makeText(MainActivity.this, "点击了标签: " + dataList.get(position), Toast.LENGTH_SHORT).show();
    }
});

// 设置更多按钮点击事件
tagViewGroup.setOnMoreClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(MainActivity.this, "点击了更多按钮", Toast.LENGTH_SHORT).show();
    }
});
```

### 3. 动态配置

```java
// 设置最大行数
tagViewGroup.setMaxLines(3);

// 设置水平间距
tagViewGroup.setHorizontalSpacing(10);

// 设置垂直间距
tagViewGroup.setVerticalSpacing(10);
```

## 自定义属性

| 属性名 | 格式 | 说明 |
| --- | --- | --- |
| maxLines | integer | 最大显示行数 |
| horizontalSpacing | dimension | 水平间距 |
| verticalSpacing | dimension | 竖直间距 |

## License

```
Copyright 2024 xesam

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```