package com.example.newapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeMap;

public class JapaActivityAdapter extends BaseAdapter {
    private final ArrayList mData;

    public JapaActivityAdapter(TreeMap<String, JapaClass> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public TreeMap.Entry<String, JapaClass> getItem(int position) {
        return (TreeMap.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.japa_item, parent, false);
        } else {
            result = convertView;
        }

        TreeMap.Entry<String, JapaClass> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.text1)).setText(item.getKey());
        ((TextView) result.findViewById(R.id.text2)).setText(String.valueOf(item.getValue().getStudents()));
        ((TextView) result.findViewById(R.id.text3)).setText(String.valueOf(item.getValue().getPercentage()));
//        ((TextView) result.findViewById(R.id.text_amount)).setText(String.valueOf(item.getValue().getAmount()));
        return result;
    }
}