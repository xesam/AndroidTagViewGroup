package com.github.xesam.android.views.example;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.xesam.android.views.tag.OnTagClickListener;
import com.github.xesam.android.views.tag.TagAdapter;
import com.github.xesam.android.views.tag.TagViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 获取 TagViewGroup 实例
        TagViewGroup tagViewGroup = findViewById(R.id.tag_view_group);

        // 获取配置选项SeekBar
        SeekBar seekBarMaxLines = findViewById(R.id.seekBarMaxLines);
        SeekBar seekBarHorizontalSpacing = findViewById(R.id.seekBarHorizontalSpacing);
        SeekBar seekBarVerticalSpacing = findViewById(R.id.seekBarVerticalSpacing);

        // 获取显示数值的TextView
        TextView textViewMaxLinesValue = findViewById(R.id.textViewMaxLinesValue);
        TextView textViewHorizontalSpacingValue = findViewById(R.id.textViewHorizontalSpacingValue);
        TextView textViewVerticalSpacingValue = findViewById(R.id.textViewVerticalSpacingValue);

        // 获取控制MoreView显示的Switch
        Switch switchShowMore = findViewById(R.id.switchShowMore);

        // 创建示例数据
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            dataList.add("Tag " + (i + 1));
        }

        // 设置适配器
        setupTagAdapter(tagViewGroup, dataList, true);

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

        // 设置SeekBar监听器
        seekBarMaxLines.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 确保至少有1行
                int maxLines = progress == 0 ? 1 : progress;
                textViewMaxLinesValue.setText(String.valueOf(maxLines));
                tagViewGroup.setMaxLines(maxLines);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarHorizontalSpacing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewHorizontalSpacingValue.setText(String.valueOf(progress));
                tagViewGroup.setHorizontalSpacing(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarVerticalSpacing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewVerticalSpacingValue.setText(String.valueOf(progress));
                tagViewGroup.setVerticalSpacing(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // 设置Switch监听器
        switchShowMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupTagAdapter(tagViewGroup, dataList, isChecked);
            }
        });

        // 设置 RecyclerView
        setupRecyclerView();
    }

    private void setupTagAdapter(TagViewGroup tagViewGroup, List<String> dataList, final boolean showMore) {
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
                if (!showMore) {
                    return null;
                }
                TextView moreView = new TextView(MainActivity.this);
                moreView.setText("更多");
                moreView.setPadding(20, 10, 20, 10);
                moreView.setBackgroundResource(R.drawable.more_background);
                return moreView;
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 创建示例数据
        List<ListItem> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<String> tags = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                tags.add("标签" + (j + 1));
            }
            dataList.add(new ListItem("列表项 " + (i + 1), tags));
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }
}