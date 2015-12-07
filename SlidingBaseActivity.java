package com.example.slidingmenuapp;


import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Dilip Pandit on 05-11-2015.
 * HOME SCREEN
 */
public class SlidingBaseActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar homeToolbar;
    ImageView ivHomeMenu, ivHome, ivSearch;
    TextView tvTitle, tvNews, tvEat, tvSleep, tvInfo;
    SlidingBaseAdapter slidingBaseAdapter;
    String sideBarArr[] = new String[]{"IL PROGETTO", "DICONO DI NOI", "IL TERRITORIO", "I PERCORSI DEL GUSTO", "DOVE SOGGIORNARE", "DOVE MANAGIRE", "NEWS ED EVENTI", "CONTATTI", "CREDITS"};
    LinearLayout llLayout;
    EditText etSearch;
    ImageView ivSearchIcon;
    RelativeLayout rlSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingbase);
        _init();
        setActionBarTitle("CATEGORIES");
        setSidebarAdapter();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TestFragment()).commit();
    }

    private void setSidebarAdapter() {
        slidingBaseAdapter = new SlidingBaseAdapter(this, R.layout.text_item, sideBarArr);
        mDrawerList.setAdapter(slidingBaseAdapter);
        setItems();
    }

    private void setItems() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {


                switch (pos) {

                    case 0:

                        break;

                    case 1:

                        break;

                    case 2:
                        break;

                    case 3:
                        break;

                    case 4:
                        break;

                    case 5:
                        break;

                    case 6:
                        closeDrawer();
                        break;
                    case 7:
                        closeDrawer();
                        break;
                }


            }


        });
    }

    @Override
    public void onClick(View v) {

        //handling the navigation click
        if (v.getId() == R.id.ivHomeMenu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TestFragment()).commit();
        }
        else if(v.getId() == R.id.ivHome)
        {
            if (mDrawerLayout != null) {

                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    closeDrawer();
                } else {
                    openDrawer();
                }
            }
        }

    }

    /**
     * Side bar navigation close
     */
    public void closeDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    /**
     * Side bar navigation open
     */
    public void openDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(mDrawerList);
        }
    }

    public void setActionBarTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * declaration and definition of variables
     */
    private void _init() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.mipmap.ic_launcher, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            /**
             *
             * override this methode to make side bar right
             *
             */
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        mDrawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }
                return false;
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        homeToolbar = (Toolbar) findViewById(R.id.tb_sliding);
//        rlSearch = (RelativeLayout) homeToolbar.findViewById(R.id.rlSearchTab);
        ivSearchIcon = (ImageView) homeToolbar.findViewById(R.id.ivSearchIcon);
        ivSearchIcon.setOnClickListener(this);
        etSearch = (EditText) homeToolbar.findViewById(R.id.etSearch);
        ivHomeMenu = (ImageView) homeToolbar.findViewById(R.id.ivHomeMenu);
        ivHomeMenu.setOnClickListener(this);
        tvTitle = (TextView) homeToolbar.findViewById(R.id.tvTitle);
        ivHome = (ImageView) homeToolbar.findViewById(R.id.ivHome);
        ivHome.setOnClickListener(this);
        ivSearch = (ImageView) homeToolbar.findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(this);

        hideSearchIcon();
        hideSearchTab();
//        showIcons();
    }

    public void hideSearchIcon() {
        ivSearch.setVisibility(View.GONE);
    }

    public void hideIcons() {
        tvTitle.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
    }


    public void showIcons() {
        tvTitle.setVisibility(View.VISIBLE);
        ivHomeMenu.setVisibility(View.VISIBLE);
        ivHome.setVisibility(View.VISIBLE);
    }


    public void showSearchIcon() {
        ivSearch.setVisibility(View.VISIBLE);
    }

    public void showSearchTab() {
        etSearch.setVisibility(View.VISIBLE);
        ivSearchIcon.setVisibility(View.VISIBLE);
    }

    public void hideSearchTab() {
        etSearch.setVisibility(View.GONE);
        ivSearchIcon.setVisibility(View.GONE);
    }
}
