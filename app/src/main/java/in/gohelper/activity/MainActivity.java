package in.gohelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.gohelper.R;
import in.gohelper.fragment.HelpFragment;
import in.gohelper.fragment.MainFragment;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView = null;
    Toolbar toolbar;
    DrawerLayout drawer;
    TextView textCartItemCount;
    int mCartItemCount = 00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
//        toggle.setDrawerIndicatorEnabled(false);
//        toggle.setHomeAsUpIndicator(R.drawable.logo100);
//        toolbar.setLogo(R.drawable.logo100);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        setupNavigationHeader();

        addFragments(new MainFragment(), false, "hyperlocal");

      /*  NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_not_login, navigationView, false);
        navigationView.addHeaderView(view);
*/
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            toolbar.setTitle("hyperLocal");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //return super.onCreateOptionsMenu(menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    //To show Badge on cart icon
    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, MyCartActivity.class);
            this.startActivity(intent);
            return true;
        }

      /* if (id == R.id.action_notifications) {
            Intent intent = new Intent(this, BadgeActivity.class);
            this.startActivity(intent);
            return true;
        }*/
        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        drawer.closeDrawers();
        drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedItem(item.getItemId());
            }
        }, 300);

        return true;
    }

    public void setupNavigationHeader() {
        if (navigationView != null) {
            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_not_login);
            ImageView navProfile = (ImageView) headerLayout.findViewById(R.id.btnLogin);
            TextView tvName = (TextView) headerLayout.findViewById(R.id.nav_name);
            if (PreferencesManager.getPrefBool(Constants.MOBILE_VERIFIED)) {
                tvName.setText(PreferencesManager.getPrefString(Constants.FIRST_NAME) + " " + PreferencesManager.getPrefString(Constants.LAST_NAME));
                navProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                tvName.setText("Log in to view your profile");
                navProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MobileRegistrationActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void selectedItem(int id) {
        switch (id) {
            case R.id.nav_home:
                addFragments(new MainFragment(), false, "hyperLocal");
                break;
            case R.id.nav_my_orders:
                if (!PreferencesManager.getPrefBool(Constants.MOBILE_VERIFIED)) {
                    Intent intent = new Intent(MainActivity.this, MobileRegistrationActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                    startActivity(intent);
                }
                // addFragments(new MyOrderFragment(), true, "My Orders");
                break;
            case R.id.nav_my_address:
                if (!PreferencesManager.getPrefBool(Constants.MOBILE_VERIFIED)) {
                    Intent intentMyAddress = new Intent(MainActivity.this, MobileRegistrationActivity.class);
                    startActivity(intentMyAddress);
                } else {
                    Intent intentMyAddress = new Intent(MainActivity.this, ManageAddressActivity.class);
                    startActivity(intentMyAddress);
                }
                break;
            case R.id.nav_share:
//                addFragments(new ReferEarnFragment(), true, "Share");

                //Share application link
                Intent intentSend = new Intent(Intent.ACTION_SEND);
                intentSend.setType("text/plain");
                String shareBody = "Please download the hyperLocal app here";
                String shareSub = "Your hyperLocal service link is: https://www.hyperLocal.in";
                intentSend.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                intentSend.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(intentSend, "Share Via"));
                break;
            case R.id.nav_help:
                addFragments(new HelpFragment(), true, "Help");
                break;
        }
    }

    public void addFragments(android.support.v4.app.Fragment fragment,
                             boolean addToBackStack, String tag) {
        toolbar.setTitle(tag);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (manager.getBackStackEntryCount() > 0) {
            addToBackStack = false;
        }
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }
}
