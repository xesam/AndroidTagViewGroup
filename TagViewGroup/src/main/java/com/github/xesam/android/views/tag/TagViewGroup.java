package com.github.xesam.android.views.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class TagViewGroup extends ViewGroup {
    private int mMaxLines = Integer.MAX_VALUE;
    private int mHorizontalSpacing = 0;
    private int mVerticalSpacing = 0;
    private TagAdapter<?> mAdapter;
    private OnTagClickListener mOnTagClickListener;
    private OnClickListener mOnMoreClickListener;

    private void refreshChildViews() {
        if (mAdapter == null) {
            removeAllViews();
            return;
        }

        removeAllViews();

        // 添加普通标签
        int childCount = mAdapter.getCount();
        for (int i = 0; i < childCount; i++) {
            View child = mAdapter.getView(i, this);
            addView(child);
        }

        // 添加MoreView
        View moreView = mAdapter.getMoreView(this);
        if (moreView != null) {
            addView(moreView);
        }

        requestLayout();
    }

    public TagViewGroup(Context context) {
        this(context, null);
    }

    public TagViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Xesam_TagViewGroup);
            mMaxLines = a.getInt(R.styleable.Xesam_TagViewGroup_maxLines, Integer.MAX_VALUE);
            mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.Xesam_TagViewGroup_horizontalSpacing, 0);
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.Xesam_TagViewGroup_verticalSpacing, 0);
            a.recycle();
        }
    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
        refreshChildViews();
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    public void setHorizontalSpacing(int spacing) {
        mHorizontalSpacing = spacing;
        refreshChildViews();
    }

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    public void setVerticalSpacing(int spacing) {
        mVerticalSpacing = spacing;
        refreshChildViews();
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    public void setOnTagClickListener(OnTagClickListener listener) {
        mOnTagClickListener = listener;
    }

    public void setOnMoreClickListener(OnClickListener listener) {
        mOnMoreClickListener = listener;
    }

    public void setAdapter(TagAdapter<?> adapter) {
        mAdapter = adapter;
        refreshChildViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAdapter == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 修复RecyclerView中高度计算问题
        int maxWidth = widthMode == MeasureSpec.EXACTLY ? widthSize :
                (widthMode == MeasureSpec.AT_MOST ? widthSize : Integer.MAX_VALUE);

        // 如果宽度未确定，使用父容器提供的最大宽度
        if (maxWidth == 0 || maxWidth == Integer.MAX_VALUE) {
            maxWidth = getSuggestedMinimumWidth();
            if (maxWidth <= 0) {
                maxWidth = getResources().getDisplayMetrics().widthPixels;
            }
        }

        int totalHeight = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int lineCount = 1;
        int childCount = mAdapter.getCount();

        // 先移除所有现有的子视图
        removeAllViews();

        if (childCount == 0) {
            // 没有数据时设置最小高度
            setMeasuredDimension(
                    widthMode == MeasureSpec.EXACTLY ? widthSize : 0,
                    heightMode == MeasureSpec.EXACTLY ? heightSize : 0
            );
            return;
        }

        // 添加普通标签
        boolean needMoreView = false;
        for (int i = 0; i < childCount; i++) {
            View child = mAdapter.getView(i, this);
            addView(child);

            // 测量子视图
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 判断是否需要换行
            if (lineWidth + childWidth > maxWidth && lineWidth > 0) {
                // 换行
                if (lineCount >= mMaxLines) {
                    // 超过最大行数，移除当前视图
                    removeViewAt(getChildCount() - 1);
                    needMoreView = true;
                    break;
                }

                totalHeight += lineHeight + mVerticalSpacing;
                lineWidth = 0;
                lineHeight = 0;
                lineCount++;
            }

            // 更新当前行的宽度和高度
            lineWidth += childWidth + (lineWidth > 0 ? mHorizontalSpacing : 0);
            lineHeight = Math.max(lineHeight, childHeight);
        }

        // 添加MoreView
        View moreView = mAdapter.getMoreView(this);
        if (moreView != null && needMoreView) {
            addView(moreView);
            measureChild(moreView, widthMeasureSpec, heightMeasureSpec);
            int moreWidth = moreView.getMeasuredWidth();
            int moreHeight = moreView.getMeasuredHeight();

            // 检查MoreView是否能放在当前行
            if (lineWidth + (lineWidth > 0 ? mHorizontalSpacing : 0) + moreWidth > maxWidth) {
                // 当前行放不下，需要换行
                if (lineCount < mMaxLines) {
                    totalHeight += lineHeight + mVerticalSpacing;
                    lineWidth = moreWidth;
                    lineHeight = moreHeight;
                    lineCount++;
                } else {
                    // 已达到最大行数，移除最后一个普通标签，为MoreView腾出空间
                    if (getChildCount() > 1) {
                        View lastChild = getChildAt(getChildCount() - 2);
                        removeViewAt(getChildCount() - 2);
                        lineWidth -= lastChild.getMeasuredWidth() + mHorizontalSpacing;
                        // 重新检查MoreView是否能放下
                        if (lineWidth + (lineWidth > 0 ? mHorizontalSpacing : 0) + moreWidth <= maxWidth) {
                            lineWidth += (lineWidth > 0 ? mHorizontalSpacing : 0) + moreWidth;
                            lineHeight = Math.max(lineHeight, moreHeight);
                        } else {
                            // 还是放不下，移除MoreView
                            removeViewAt(getChildCount() - 1);
                        }
                    } else {
                        // 没有普通标签可移除，移除MoreView
                        removeViewAt(getChildCount() - 1);
                    }
                }
            } else {
                // 当前行可以放下MoreView
                lineWidth += (lineWidth > 0 ? mHorizontalSpacing : 0) + moreWidth;
                lineHeight = Math.max(lineHeight, moreHeight);
            }
        }

        totalHeight += lineHeight;

        // 确保有最小高度
        if (totalHeight == 0 && childCount > 0) {
            totalHeight = lineHeight;
        }

        // 设置最终尺寸
        int finalWidth = widthMode == MeasureSpec.EXACTLY ? widthSize :
                (widthMode == MeasureSpec.AT_MOST ? Math.min(lineWidth, widthSize) : lineWidth);
        int finalHeight = heightMode == MeasureSpec.EXACTLY ? heightSize :
                (heightMode == MeasureSpec.AT_MOST ? Math.min(totalHeight, heightSize) : totalHeight);

        // 计算最终高度
        finalHeight = Math.max(finalHeight, getSuggestedMinimumHeight());

        setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mAdapter == null || getChildCount() == 0) {
            return;
        }

        int width = r - l;
        int totalHeight = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int lineCount = 1;
        int childCount = getChildCount();

        // 用于记录每行的子视图
        java.util.List<java.util.List<View>> lines = new java.util.ArrayList<>();
        java.util.List<View> currentLine = new java.util.ArrayList<>();
        lines.add(currentLine);

        // 第一次遍历，确定每行的子视图
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 判断是否需要换行
            if (lineWidth + childWidth > width && lineWidth > 0) {
                // 换行
                totalHeight += lineHeight + mVerticalSpacing;
                lineWidth = 0;
                lineHeight = 0;
                lineCount++;

                // 创建新行
                currentLine = new java.util.ArrayList<>();
                lines.add(currentLine);
            }

            // 添加到当前行
            currentLine.add(child);

            // 更新当前行的宽度和高度
            lineWidth += childWidth + (lineWidth > 0 ? mHorizontalSpacing : 0);
            lineHeight = Math.max(lineHeight, childHeight);
        }

        totalHeight += lineHeight;

        // 第二次遍历，进行布局
        int currentTop = 0;
        for (java.util.List<View> line : lines) {
            lineHeight = 0;
            // 先计算行高
            for (View child : line) {
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight());
            }

            int currentLeft = 0;
            for (View child : line) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                // 垂直居中对齐
                int top = currentTop + (lineHeight - childHeight) / 2;
                int bottom = top + childHeight;

                child.layout(currentLeft, top, currentLeft + childWidth, bottom);

                // 设置标签点击事件
                final int childIndex = indexOfChild(child);
                if (childIndex < mAdapter.getCount()) {
                    child.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnTagClickListener != null) {
                                mOnTagClickListener.onTagClick(v, childIndex);
                            }
                        }
                    });
                } else {
                    // 这是MoreView
                    child.setOnClickListener(mOnMoreClickListener);
                }

                currentLeft += childWidth + mHorizontalSpacing;
            }

            currentTop += lineHeight + mVerticalSpacing;
        }
    }
}
