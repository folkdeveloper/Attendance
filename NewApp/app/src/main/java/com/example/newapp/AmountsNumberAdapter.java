package com.example.newapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AmountsNumberAdapter extends BaseAdapter {
    private final ArrayList mData;

    public AmountsNumberAdapter(TreeMap<String, Integer> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public TreeMap.Entry<String, AmountsNumber> getItem(int position) {
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_adapter_item, parent, false);
        } else {
            result = convertView;
        }

        TreeMap.Entry<String, AmountsNumber> item = getItem(position);

        // TODO replace findViewById by ViewHolder

        ((TextView) result.findViewById(android.R.id.text1)).setText(item.getKey());
        ((TextView) result.findViewById(android.R.id.text2)).setText(String.valueOf(item.getValue()));

//        for (int i=0; i<item.getKey().length(); i++) {
//            if (item.getValue().getNumber() == 0) {
//                continue;
//            } else {
//                ((TextView) result.findViewById(android.R.id.text1)).setText(item.getKey());
//                ((TextView) result.findViewById(android.R.id.text2)).setText(String.valueOf(item.getValue().getNumber()));
//            }
//        }

        return result;
    }
}