package com.walker.ui.main;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.walker.R;
import com.walker.ui.common.BaseActivity;
import com.walker.ui.order.HomeFragment;
import com.walker.ui.original.calendar.CalendarActivity;

import java.util.HashMap;

import butterknife.InjectView;

public class MainActivity extends BaseActivity{

    private static HashMap<Integer, Class[]> sNavigationMap;
    //init and fragment manager
    FragmentManager fragmentManager = getSupportFragmentManager();
    static {
        Class[] originalActivities = {
                CalendarActivity.class
        };
        /*Class[] libraryActivities = {
                ImageLoaderActivity.class,
                XListViewActivity.class,
                OttoActivity.class,
                RxJavaActivity.class,
                RealmActivity.class
        };
        Class[] componentActivities = {
                ServiceActivity.class,
                FragmentManageActivity.class,
                NotificationActivity.class,
                SendDataOneActivity.class,
                SendDataTwoActivity.class,
                IntentsActivity.class
        };
        Class[] uiActivities = {
                TouchEventActivity.class,
                RecyclerViewActivity.class,
                ViewPagerActivity.class,
                SwipeRefreshLayoutActivity.class,
                FragmentTabHostActivity.class,
                DialogActivity.class,
                PopupWindowActivity.class,
                DrawableStateActivity.class
        };
        Class[] textActivities = {
                TextSizeActivity.class,
                EditTextActivity.class,
                AutoCompleteActivity.class,
                TextViewSpanActivity.class,
                TextAdvanceActivity.class
        };
        Class[] animationActivities = {
                AnimationActivity.class,
                MarkAnimationActivity.class
        };
        Class[] sensorActivities = {
                GeocoderActivity.class
        };
        Class[] storageActivities = {
                ContentProviderActivity.class,
                BitmapSaveLocalActivity.class
        };
        Class[] otherActivities = {
                WebViewActivity.class,
                PackageManageActivity.class,
                ScreenshotActivity.class,
                ImageSelectorActivity.class,
                Md5Activity.class
        };*/
        sNavigationMap = new HashMap<>();
        sNavigationMap.put(R.id.nav_book, originalActivities);
        /*sNavigationMap.put(R.id.navigation_library, libraryActivities);
        sNavigationMap.put(R.id.navigation_component, componentActivities);
        sNavigationMap.put(R.id.navigation_ui, uiActivities);
        sNavigationMap.put(R.id.navigation_text, textActivities);
        sNavigationMap.put(R.id.navigation_animation, animationActivities);
        sNavigationMap.put(R.id.navigation_sensor, sensorActivities);
        sNavigationMap.put(R.id.navigation_storage, storageActivities);
        sNavigationMap.put(R.id.navigation_other, otherActivities);*/
    }

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.navigation)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;
    private int currentNavigationId;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPageView() {
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getActionBarToolbar(), R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    protected void initPageViewListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (sNavigationMap.containsKey(menuItem.getItemId())) {
                    menuItem.setChecked(true);
                    setTitle(menuItem.getTitle());
                    currentNavigationId = menuItem.getItemId();
                    MainContentFragment contentFragment = (MainContentFragment) getSupportFragmentManager().findFragmentById(R.id.content_layout);
                    if (contentFragment != null) {
                        contentFragment.updateContentList(sNavigationMap.get(currentNavigationId));
                    }
                    mDrawerLayout.closeDrawers();
                    return true;
                } else if (menuItem.getItemId() == R.id.navigation_settings) {
                    // todo
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    return true;
                } else {
                    setTitle("首页");
                    fragmentManager.beginTransaction().replace(R.id.content_layout, new HomeFragment()).commit();
                    mDrawerLayout.closeDrawer(mNavigationView);
                    return false;
                }
            }
        });
    }

    @Override
    protected void process(Bundle savedInstanceState) {
        currentNavigationId = R.id.nav_book;
        MainContentFragment fragment = new MainContentFragment();
        fragment.updateContentList(sNavigationMap.get(currentNavigationId));
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, fragment).commit();
    }
}
