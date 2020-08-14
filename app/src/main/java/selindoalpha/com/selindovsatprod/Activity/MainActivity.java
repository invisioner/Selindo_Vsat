package selindoalpha.com.selindovsatprod.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Fragment.FragmentAdapter;
import selindoalpha.com.selindovsatprod.Fragment.SpdFragment;
import selindoalpha.com.selindovsatprod.Fragment.TaskFragment;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.CustomUncaughtHandler;
import selindoalpha.com.selindovsatprod.Utils.HackyViewPager;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    ViewPager viewPager;
    HackyViewPager viewPager;
    int TAGMainActivity = 123;

    private SharedPrefManager sharedPrefManager;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    TaskFragment taskFragment;
    SpdFragment spdFragment;

    int counter = 0;
    boolean isSearch =false;
    Toolbar toolbar,toolbarSearch;
    private NavigationView navigationView;
    TextView navUsername,navEmail;


    private int[] tabIcons = {
            R.drawable.ic_folder_open_black_24dp,
            R.drawable.ic_local_atm_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbars);
        toolbarSearch = (Toolbar) findViewById(R.id.toolbarsearch);
        initTollbar(toolbar,"TASK");

        viewPager = new HackyViewPager(this);
        viewPager = findViewById(R.id.hackyviewpager);

        //eckosa
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            String spath= Environment.getExternalStorageDirectory().getPath();
            Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtHandler(spath));
        }


        if (viewPager!=null){
            setupViewPager(viewPager);
        }
        initComponen();

        statusCheck();

        sharedPrefManager = new SharedPrefManager(this);

        String username = sharedPrefManager.getSPUserName();
        String useremail = sharedPrefManager.getSpEmail();
        String NIK = sharedPrefManager.getSpNik();

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.user_name);
        navEmail = headerView.findViewById(R.id.user_email);
        navUsername.setText(username);
        navEmail.setText(NIK);

    }

    private void setupViewPager(HackyViewPager viewPager){
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        if (taskFragment ==  null){
            taskFragment = new TaskFragment();
        }
        if (spdFragment == null){
            spdFragment = new SpdFragment();
        }

        adapter.addFragment(taskFragment,"TASK");
        adapter.addFragment(spdFragment,"SPD");


        viewPager.setAdapter(adapter);
    }

    private void initComponen(){
        final TabLayout tabLayout = findViewById(R.id.tablytkaryawanadd);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                        initTollbar(toolbar,"TASK");
                        taskFragment.new AsyingTask().execute();
                        break;
                    case 1:
                        initTollbar(toolbar,"SPD");
                        spdFragment.new AsyingTaskSPD().execute();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setCurrentItem(0);
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert manager != null;

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> startActivity(new
                            Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            final AlertDialog alert = builder.create();
            alert.show();
        }

        else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void buildAlertMessageNoGps() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("GPS");
            if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
                permissionsNeeded.add("Location");
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("External Storage Read");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("External Storage Write");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            counter++;
            if(counter > 1){
                System.exit(3);
                finish();
            }else{
                Toasty.info(this, "Tekan sekali lagi untuk keluar!", Toast.LENGTH_SHORT, true).show();
            }
            final long DELAY_TIME = 2000L;
            new Thread(new Runnable() {
                public void run(){
                    try {
                        Thread.sleep(DELAY_TIME);
                        counter = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(isSearch ? R.menu.menu_search : R.menu.search, menu);

        if (isSearch) {
            final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setQueryHint("Search...");
            search.setIconified(false);
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    switch (viewPager.getCurrentItem()) {
                        case 0: {
                            taskFragment.mAdapterOpen.getFilter().filter(newText);
                            taskFragment.mAdapterFinish.getFilter().filter(newText);
                            break;
                        }
                        case 1: {
                            spdFragment.mAdapter.getFilter().filter(newText);
                            break;
                        }
                    }
                    return true;
                }
            });
            search.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    isSearch = false;
                    switch (viewPager.getCurrentItem()) {
                        case 0: {
                            initTollbar(toolbar,"TASK");
                            break;
                        }
                        case 1: {
                            initTollbar(toolbar,"SPD");
                            break;
                        }
                    }
                    toolbarSearch.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    supportInvalidateOptionsMenu();
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void initTollbar(Toolbar toolbar, String Title){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_search){
            isSearch = true;
            toolbarSearch.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            initTollbar(toolbarSearch,"");
            supportInvalidateOptionsMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_pass) {
            Intent next = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            startActivityForResult(next,TAGMainActivity);
        } else if (id == R.id.nav_logoutapp) {
            logout();
        } else if (id == R.id.nav_help) {
            Toasty.info(MainActivity.this, "Help and FAQ.", Toast.LENGTH_SHORT, true).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(){
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are You Sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_USER_SUDAH_LOGIN, false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
