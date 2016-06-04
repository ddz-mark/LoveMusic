package com.example.dudaizhong.lovemusic.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.dudaizhong.lovemusic.R;
import com.example.dudaizhong.lovemusic.common.PLog;
import com.example.dudaizhong.lovemusic.fragment.BooksFragment;
import com.example.dudaizhong.lovemusic.fragment.HomeFragment;
import com.example.dudaizhong.lovemusic.fragment.MusicFragment;
import com.example.dudaizhong.lovemusic.fragment.SettingFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    private BooksFragment booksFragment;
    private HomeFragment homeFragment;
    private MusicFragment musicFragment;
    private SettingFragment settingFragment;
    private long exitTime = 0;//记录第一次点击的时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar); //标题栏的初始化
        //侧滑栏的初始化
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //侧滑栏事件的初始化
        navigationView.setNavigationItemSelectedListener(this);
        //初始化Fragment
        booksFragment = new BooksFragment();
        homeFragment = new HomeFragment();
        musicFragment = new MusicFragment();
        settingFragment = new SettingFragment();

        setDefaultFragment();//设置默认的显示Fragment
        //BottomBar的设置
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_camera, "Home")).setActiveColor(R.color.black_bg)
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_gallery, "Books")).setActiveColor(R.color.black_bg)
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_manage, "Music")).setActiveColor(R.color.black_bg)
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_send, "Setting")).setActiveColor(R.color.black_bg)
                .initialise();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
//        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                switch (position) {
                    case 0:
                        transaction.replace(R.id.fragment_container, homeFragment);
                        break;
                    case 1:
                        transaction.replace(R.id.fragment_container, booksFragment);
                        break;
                    case 2:
                        transaction.replace(R.id.fragment_container, musicFragment);
                        break;
                    case 3:
                        transaction.replace(R.id.fragment_container, settingFragment);
                        break;
                    default:
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transition = fm.beginTransaction();
        transition.replace(R.id.fragment_container, homeFragment);
        transition.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        return true;
    }
}
