package com.example.homework04;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter{
    Context ctx;
    List<MyData> data;

    public MyAdapter(Context ctx, List<MyData> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(ctx, R.layout.row, null); // inflate를 했으니까, 필드를 가져올 수 있음
        }

        TextView text1 = view.findViewById(R.id.text1);
        TextView text2 = view.findViewById(R.id.text2);
        TextView text3 = view.findViewById(R.id.text3);
        text1.setText(data.get(i).getName());
        text2.setText(data.get(i).getAlias());
        text3.setText(data.get(i).getPhone());

        return view;
    }
}
