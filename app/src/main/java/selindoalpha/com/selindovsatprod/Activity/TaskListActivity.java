package selindoalpha.com.selindovsatprod.Activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import selindoalpha.com.selindovsatprod.Adapter.TaskListAdapter;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.DetailTaskModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class TaskListActivity extends AppCompatActivity {

    private static final String TAG = "TaskListActivity";

    List<DetailTaskModel> detailTaskModelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String ResultWS,statusTask;
    String getResultWS;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;

    TextView mStatusDetail;
    TextView mDataNull;
    Button mRerty;
    ProgressBar mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("TASK");

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);
        mStatusDetail = findViewById(R.id.tvStatusDetail);
        mDataNull = findViewById(R.id.tvDataKosong);
        mRerty = findViewById(R.id.btnRetry);

        mProgress = findViewById(R.id.progress_list_task);

        // set up the RecyclerView
        recyclerView = findViewById(R.id.rvCardTaskDetail);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new TaskListAdapter(getApplicationContext(), detailTaskModelList);
        recyclerView.setAdapter(adapter);


        getListData();
    }


    public void clear() {
        detailTaskModelList.clear();
        adapter.notifyDataSetChanged();
    }


    public void getListData(){
        mProgress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String id = sharedPrefManager.getId();
        Log.i("TAG", "getListData: " + id);
        String url = BaseUrl.getPublicIp + BaseUrl.detailTask+id;
        Log.i(TAG, "URL List Data: " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {

                               try {
                                   clear();
                                   JSONObject jsonObj = new JSONObject(response);
                                   getResultWS = jsonObj.getString("Result");

                                   Log.i("TaskList", "TaskList: "+getResultWS);
                                   if (getResultWS.equals("True")) {
                                       JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                       Log.d("onResponse", "onResponse: " + jsonArray);
                                       Log.i("TaskList", "TaskList: "+jsonArray);
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           JSONObject data = jsonArray.getJSONObject(i);
                                           statusTask = data.getString("IdStatusPerbaikan");
                                           Log.i("TAG", "status : "+statusTask);
                                           Log.i("TaskList", "TaskList: "+data);
                                           Log.i("TaskList", "id: " + data.getString("NoTask"));
                                           String alamatSekarang = data.getString("AlamatSekarang");
                                           String catetan = data.getString("Catatan");
                                           String hub = data.getString("Hub");
                                           DetailTaskModel lt_open = new DetailTaskModel(
                                                   data.getString("NoTask"),
                                                   data.getString("Provinsi"),
                                                   data.getString("StatusPekerjaan"),
                                                   data.getString("IdStatusPerbaikan")
                                           );

                                           Log.i("data_lokasi", "data_lokasi: "+ data.getString("NAMAREMOTE"));
                                           Log.i("data_lokasi", "data_lokasi: "+ data.getString("ALAMAT"));
                                           Log.i("data_lokasi", "data_lokasi: "+ data.getString("Provinsi"));
                                           String vid = data.getString("VID1");
                                           Log.i("TesVIDNext", "run: " + vid);
                                           sharedPrefManager.saveSPString(SharedPrefManager.SP_VID, vid);

                                           String namaRemote = data.getString("NAMAREMOTE");
                                           String alamatInstall = data.getString("ALAMAT");
                                           String kanwil = data.getString("KANWIL");
                                           String area = data.getString("KANCAINDUK");
                                           String namaPic = data.getString("NoHpPic");
                                           String noPic = data.getString("PIC");
                                           String provinsi = data.getString("Provinsi");
                                           String kota = data.getString("KOTA");
                                           String jarkom = data.getString("IdJarkom");
                                           String satelite = data.getString("IdSatelite");
                                           String latitude = data.getString("Latitude");
                                           String longtitude = data.getString("Longitude");
                                           String noTask = data.getString("NoTask");

                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.CustPIC, namaPic);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.CustPIC_Phone, noPic);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.PROVINSI, provinsi);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.KOTA, kota);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.KANWIL, kanwil);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.KANCAINDUK, area);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.NAMAREMOTE, namaRemote);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.AlamatInstall, alamatInstall);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.IdJarkom, jarkom);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.IdSatelite, satelite);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.Hub, hub);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.Latitude, latitude);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.Longitude, longtitude);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.AlamatSekarang, alamatSekarang);
                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.Catatan, catetan);

                                           sharedPrefDataTask.saveSPString(SharedPrefDataTask.NoTask, noTask);

                                           Log.i("TAG", "onResponse: " + data.getString("Provinsi"));
                                           Log.d("TaskList", "getData: " + data);

                                           detailTaskModelList.add(lt_open);


                                           String id = data.getString("Provinsi");
                                           Log.i("TaskList", "DataList: "+ id);
                                           adapter.notifyDataSetChanged();
                                           mProgress.setVisibility(View.GONE);
                                           recyclerView.setVisibility(View.VISIBLE);

                                       }



                                   }else {
                                       mProgress.setVisibility(View.GONE);
                                       recyclerView.setVisibility(View.GONE);
                                       mDataNull.setVisibility(View.VISIBLE);
                                       mRerty.setVisibility(View.VISIBLE);
                                       mDataNull.setText("Error Connection Kosong");
                                   }

                               } catch (JSONException e) {
                                   e.printStackTrace();
                                   mDataNull.setVisibility(View.VISIBLE);
                                   mRerty.setVisibility(View.VISIBLE);
                                   recyclerView.setVisibility(View.GONE);
                                   mDataNull.setText("Error Connection Catch");
                               }

                           }
                       },200);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        error.getMessage();
                        Log.i("TAG", "onErrorResponse: " + error.getMessage());
                        mDataNull.setVisibility(View.VISIBLE);
                        mRerty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        mDataNull.setText("Cek Jaringan anda");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Header", "Dota2");
                return headers;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
