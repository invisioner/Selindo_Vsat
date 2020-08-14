package selindoalpha.com.selindovsatprod.Activity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import selindoalpha.com.selindovsatprod.Fragment.BarangTerpasangFragment;
import selindoalpha.com.selindovsatprod.Fragment.BarangRusakFragment;
import selindoalpha.com.selindovsatprod.Fragment.FragmentAdapter;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class DataBarangActivity extends AppCompatActivity {

    private static final String TAG = "DataBarangActivity";
    private static final int TAGDataBarangActivity = 222;

    private ViewPager mViewPager;
    private TabLayout tabLayout;

    BarangTerpasangFragment barangTerpasangFragment;
    BarangRusakFragment barangRusakFragment;

    String id, vid, noTask;

    SharedPrefDataTask sharedPrefDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_barang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("DATA BARANG");

        sharedPrefDataTask = new SharedPrefDataTask(this);

        id = sharedPrefDataTask.GetTaskid();
        vid = sharedPrefDataTask.GetTaskVid();
        noTask = sharedPrefDataTask.GetTaskNoTask();
        Log.i(TAG, "id: " +id);
        Log.i(TAG, "vid: "+vid);
        Log.i(TAG, "notask: "+noTask);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        barangTerpasangFragment.new AsyingBarangTerpasang().execute();
                        break;
                    case 1:
                        barangRusakFragment.new AsyingBarangRusak().execute();
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
    }

    private void setupViewPager(ViewPager viewPager) {

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        if (barangTerpasangFragment ==  null){
            barangTerpasangFragment = new BarangTerpasangFragment();
        }
        if (barangRusakFragment == null){
            barangRusakFragment = new BarangRusakFragment();
        }

        adapter.addFragment(barangTerpasangFragment,"TERPASANG");
        adapter.addFragment(barangRusakFragment,"RUSAK");


        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
//            onBackPressed();
            Intent intent = new Intent();
            intent.putExtra(SharedPrefManager.SP_ID, id);
            setResult(RESULT_OK,intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra(SharedPrefManager.SP_ID, id);
        setResult(RESULT_OK,intent);
        finish();
    }

}
