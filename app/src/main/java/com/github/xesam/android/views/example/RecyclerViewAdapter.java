package com.github.xesam.android.views.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.xesam.android.views.tag.OnTagClickListener;
import com.github.xesam.android.views.tag.TagAdapter;
import com.github.xesam.android.views.tag.TagViewGroup;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<ListItem> mDataList;

    public RecyclerViewAdapter(List<ListItem> dataList) {
        this.mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = mDataList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TagViewGroup tagViewGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.item_title);
            tagViewGroup = itemView.findViewById(R.id.item_tag_view_group);
        }

        public void bind(ListItem item) {
            titleText.setText(item.getTitle());
            
            // 设置 TagViewGroup 的适配器
            tagViewGroup.setAdapter(new TagAdapter<String>() {
                @Override
                public int getCount() {
                    return item.getTags().size();
                }

                @Override
                public String getItem(int position) {
                    return item.getTags().get(position);
                }

                @Override
                protected View getView(int position, ViewGroup parent) {
                    TextView textView = new TextView(parent.getContext());
                    textView.setText(getItem(position));
                    textView.setPadding(20, 10, 20, 10);
                    textView.setBackgroundResource(R.drawable.tag_background);
                    return textView;
                }

                @Override
                protected View getMoreView(ViewGroup parent) {
                    return null; // 在列表项中不显示更多按钮
                }
            });

            // 设置标签点击事件
            tagViewGroup.setOnTagClickListener(new OnTagClickListener() {
                @Override
                public void onTagClick(View view, int position) {
                    Toast.makeText(itemView.getContext(), "点击了标签: " + item.getTags().get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}