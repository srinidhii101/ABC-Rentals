package com.example.abcrentals;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    public FirebaseAuth fbAuth;
    public  static FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        fbAuth = FirebaseAuth.getInstance();
        fab = findViewById(R.id.fab);
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Create Navigation drawer and inflate layout
        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(),add_record.class);
                startActivity(intent);
            }
        });
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        if(menuItem.getItemId() == R.id.profile){
                            Intent intent = new Intent(getApplication(), Logout.class);
                            startActivity(intent);
                        }
                        else if(menuItem.getItemId() ==R.id.Home)
                        {
                            Intent intent = new Intent(getApplication(),SearchPage.class);
                            startActivity(intent);
                        }
                        else if(menuItem.getItemId() ==R.id.AddInterests)
                        {
                            Intent intent = new Intent(getApplication(),add_record.class);
                            startActivity(intent);
                        }

                        menuItem.setChecked(true);

                        // TODO: handle navigation
                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });



    }
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        SearchPage.Adapter adapter = new SearchPage.Adapter(getSupportFragmentManager());
        adapter.addFragment(new CardContentFragment(), "Search Listings");
        adapter.addFragment(new ListContentFragment(), "My Interests");
        viewPager.setAdapter(adapter);

        final FloatingActionButton fabObj = findViewById(R.id.fab);
        fabObj.hide();
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){



                                              @Override
                                              public void onPageSelected(int position) {
                                                  if(position==0)
                                                  {
                                                      fabObj.hide();
                                                  }
                                                  else
                                                  {
                                                      fabObj.show();
                                                  }
                                              }
                                          }


        );


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fbAuth!=null){
            FirebaseUser user = fbAuth.getCurrentUser();
            if(user==null)
            {
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }




        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.string.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    }

