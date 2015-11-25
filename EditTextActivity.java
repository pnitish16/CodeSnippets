package com.example.android.newsreader;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nitish.Pradhan on 24-11-2015.
 */
public class EditTextActivity extends Activity implements onChangeValue {

    ListView lvItems;
    ArrayList<ItemData> alItems;
    ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ediittext);

        lvItems = (ListView) findViewById(R.id.lvItems);

        alItems = new ArrayList<ItemData>();
        alItems.add(new ItemData("sample", 1, 200, 200));
        alItems.add(new ItemData("sample1", 1, 400, 400));

        adapter = new ItemListAdapter(EditTextActivity.this, R.layout.listitem_sample, alItems);
        adapter.setListener(EditTextActivity.this);
        lvItems.setAdapter(adapter);
    }

    public void viewTotal(View view) {
        int total = 0;
//        for (int i = 0; i < alItems.size(); i++) {
//            total = total + alItems.get(i).getTotal();
//        }
//
//        Log.d("Total", "" + total);
        if(adapter != null)
        {
            for(int i=0;i<adapter.getCount();i++)
            {
                View childview = lvItems.getChildAt(i);
                EditText et = (EditText) childview.findViewById(R.id.etQty);
                int qty = Integer.parseInt(et.getText().toString().trim());
                total = total + ( qty * alItems.get(i).getTotal());
            }
        }
        Log.d("Total", "" + total);
    }

    @Override
    public void onChange(int position, int value, int total) {
//        alItems.get(position).setQty(value);
//        alItems.get(position).setTotal(total);
//        adapter.notifyDataSetChanged();
        alItems.set(position , new ItemData(alItems.get(position).getName(),value,alItems.get(position).getAmount(),total));
    }

    public class ItemData {
        String name;
        int qty, amount, total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public ItemData(String name, int qty, int amount, int total) {
            this.name = name;
            this.qty = qty;
            this.amount = amount;
            this.total = total;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    public class ItemListAdapter extends ArrayAdapter<ItemData> {
        private Context context;
        private ArrayList<ItemData> data;
        LayoutInflater inflater;
        private onChangeValue listener;

        public ItemListAdapter(Context context, int textViewResourceId, ArrayList<ItemData> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            data = objects;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            View row = convertView;

            if (row == null) {

                viewHolder = new ViewHolder();
                row = LayoutInflater.from(getContext()).inflate(
                        R.layout.listitem_sample, parent, false);

                viewHolder.tvName = (TextView) row.findViewById(R.id.tvName);
                viewHolder.tvAmount = (TextView) row.findViewById(R.id.tvAmount);
                viewHolder.etQty = (EditText) row.findViewById(R.id.etQty);

                row.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) row.getTag();
            }

            final ItemData item = getItem(position);
            viewHolder.tvName.setText(item.getName());
            viewHolder.etQty.setText("" + item.getQty());
            viewHolder.tvAmount.setText("" + item.getAmount());
            viewHolder.etQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > 0) {
                        viewHolder.tvAmount.setText("" + (Integer.parseInt(s.toString()) * item.getAmount()));
//                        alItems.get(position).setTotal((Integer.parseInt(s.toString()) * item.getAmount()));
//                        if (listener != null) {
//                            int value = Integer.parseInt(s.toString());
//                            listener.onChange(position, value, (value * item.getAmount()));
//                        }

                    }
                }
            });

            return row;
        }

        @Override
        public ItemData getItem(int position) {
            return super.getItem(position);
        }

        public void setListener(onChangeValue listener) {
            this.listener = listener;
        }
    }

    public class ViewHolder {
        TextView tvName, tvAmount;
        EditText etQty;
    }
}
