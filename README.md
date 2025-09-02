# Android TagViewGroup

一个灵活的标签流式布局组件，支持在RecyclerView中使用。

## 功能特性

- 流式标签布局，自动换行
- 支持最大行数限制
- 支持"更多"按钮显示
- 完全兼容RecyclerView
- 支持标签点击事件
- 自定义样式支持

## 修复内容

### 修复了RecyclerView中的高度更新问题

在之前的版本中，TagViewGroup在RecyclerView中设置adapter后无法正确更新高度，导致无法显示。本次修复包含以下内容：

1. **改进了测量逻辑**
   - 优化了`onMeasure`方法中的宽度计算
   - 正确处理了AT_MOST测量模式
   - 确保在无数据时也有最小高度

2. **添加了布局更新机制**
   - 在`setAdapter`方法中添加了`requestLayout()`调用
   - 使用`post()`方法确保父布局也得到更新通知
   - 新增了`notifyDataSetChanged()`方法用于手动触发更新

3. **增强了RecyclerView兼容性**
   - 确保在ViewHolder中设置adapter后能正确测量
   - 支持动态数据更新

## 使用方法

### 在RecyclerView中使用

```java
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        TagViewGroup tagViewGroup;
        
        public ViewHolder(View itemView) {
            super(itemView);
            tagViewGroup = itemView.findViewById(R.id.tag_view_group);
        }
        
        public void bind(ItemData data) {
            tagViewGroup.setAdapter(new TagAdapter<String>() {
                @Override
                public int getCount() {
                    return data.getTags().size();
                }

                @Override
                public String getItem(int position) {
                    return data.getTags().get(position);
                }

                @Override
                protected View getView(int position, ViewGroup parent) {
                    TextView textView = new TextView(parent.getContext());
                    textView.setText(getItem(position));
                    return textView;
                }
            });
        }
    }
}
```

### XML布局

```xml
<com.github.xesam.android.views.tag.TagViewGroup
    android:id="@+id/tag_view_group"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:maxLines="3"
    app:horizontalSpacing="8dp"
    app:verticalSpacing="4dp" />
```

### 动态更新数据

```java
// 设置新数据后调用notifyDataSetChanged()刷新布局
tagViewGroup.setAdapter(newAdapter);
tagViewGroup.notifyDataSetChanged();
```

## 构建和运行

```bash
# 构建项目
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

## 示例应用

项目中包含两个示例：
- `MainActivity`: 展示基本功能和交互控制
- `TestActivity`: 专门测试RecyclerView中的使用

修复后的版本确保在RecyclerView中能正确显示和更新标签布局。