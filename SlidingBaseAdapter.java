package com.example.slidingmenuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Dilip on 06-11-2015.
 */
public class SlidingBaseAdapter extends ArrayAdapter<String> {
    String[] strArr;

    public SlidingBaseAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        strArr = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.init(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(strArr[position]);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;

        public void init(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.tvName);
        }
    }
}
