package com.dyhdyh.helper.recyclerview.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dyhdyh.helper.recyclerview.checkable.CheckableAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    TextView tv_log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv);
        tv_log = findViewById(R.id.tv_log);
        rv.setLayoutManager(new LinearLayoutManager(this));
        clickMenuSingle(null);

        startActivity(new Intent(this, LoadMoreActivity.class));
    }

    public void clickMenuSingle(MenuItem menuItem) {
        final SingleExampleAdapter exampleAdapter = new SingleExampleAdapter(testData());
        exampleAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<ExampleModel>() {
            @Override
            public void onItemClick(BaseRecyclerAdapter recyclerAdapter, int position, ExampleModel item) {
                exampleAdapter.setCheckedPosition(position);
                updateCheckedLog();
            }
        });
        rv.setAdapter(exampleAdapter);
        updateCheckedLog();
    }

    public void clickMenuMultiple(MenuItem menuItem) {
        final MultipleExampleAdapter exampleAdapter = new MultipleExampleAdapter(testData());
        exampleAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<ExampleModel>() {
            @Override
            public void onItemClick(BaseRecyclerAdapter recyclerAdapter, int position, ExampleModel item) {
                ExampleModel model = exampleAdapter.getItem(position);
                exampleAdapter.setCheckedPositionArray(new int[]{position}, !model.isChecked());
                updateCheckedLog();
            }
        });
        rv.setAdapter(exampleAdapter);
        updateCheckedLog();
    }

    public void clickMenuClear(MenuItem menuItem) {
        RecyclerView.Adapter adapter = rv.getAdapter();
        if (adapter instanceof CheckableAdapter) {
            ((CheckableAdapter) adapter).clear();
        }
        updateCheckedLog();
    }

    private void updateCheckedLog() {
        StringBuffer sb = new StringBuffer();
        RecyclerView.Adapter adapter = rv.getAdapter();
        if (adapter instanceof SingleExampleAdapter) {
            sb.append("单选：");
            Integer checkedPosition = ((SingleExampleAdapter) adapter).getCheckedPosition();
            if (checkedPosition != null) {
                sb.append("Position = ");
                sb.append(checkedPosition);
                sb.append(" = ");
                ExampleModel item = ((SingleExampleAdapter) adapter).getData().get(checkedPosition);
                sb.append(item.getLabel());
            }
        } else if (adapter instanceof MultipleExampleAdapter) {
            sb.append("多选：\n");
            int[] positionArray = ((MultipleExampleAdapter) adapter).getCheckedPositionArray();
            for (int i = 0; i < positionArray.length; i++) {
                ExampleModel item = ((MultipleExampleAdapter) adapter).getItem(positionArray[i]);
                sb.append("Position = ");
                sb.append(positionArray[i]);
                sb.append(" = ");
                sb.append(item.getLabel());
                sb.append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        tv_log.setText(sb.toString());
    }

    private List<ExampleModel> testData() {
        List<ExampleModel> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(new ExampleModel("Item " + (i + 1)));
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
