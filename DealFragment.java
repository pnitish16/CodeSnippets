package com.teamtreehouse.oslist.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.teamtreehouse.oslist.R;

/**
 * Created by Nitish_Pradhan on 12/17/2015.
 */
public class DealFragment extends Fragment implements View.OnClickListener{

    TextView tvCategory,tvSubCategory;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deal, container, false);
        context = getActivity();
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {

        tvCategory = (TextView) rootView.findViewById(R.id.tvCategory);
        tvSubCategory = (TextView) rootView.findViewById(R.id.tvSubCategory);

        tvCategory.setOnClickListener(this);
        tvSubCategory.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvCategory:

                PopupMenu popup = new PopupMenu(context, tvCategory);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.category_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.one:
                                Toast.makeText(getActivity(), "One", Toast.LENGTH_SHORT).show();
                                tvSubCategory.setVisibility(View.VISIBLE);
                                tvSubCategory.setText(item.getTitle());
                                return true;
                            case R.id.two:
                                Toast.makeText(getActivity(), "Two", Toast.LENGTH_SHORT).show();
                                tvSubCategory.setVisibility(View.VISIBLE);
                                tvSubCategory.setText(item.getTitle());
                                return true;
                        }
                        return false;
                    }
                });

                popup.show();//showing popup menu
                break;

            case R.id.tvSubCategory:
                PopupMenu popup1 = new PopupMenu(context, tvSubCategory);
                //Inflating the Popup using xml file
                popup1.getMenuInflater().inflate(R.menu.category_menu, popup1.getMenu());

                //registering popup with OnMenuItemClickListener
                popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.one:
                                Toast.makeText(getActivity(), "One", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.two:
                                Toast.makeText(getActivity(), "Two", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return false;
                    }
                });

                popup1.show();//showing popup menu
                break;
        }

    }
}
