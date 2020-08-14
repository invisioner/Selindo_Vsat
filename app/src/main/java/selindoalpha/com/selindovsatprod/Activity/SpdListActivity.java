package selindoalpha.com.selindovsatprod.Activity;

import android.content.Intent;
import android.os.AsyncTask;
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

import selindoalpha.com.selindovsatprod.Adapter.SpdListAdapter;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.DetailSpdModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class SpdListActivity extends AppCompatActivity {

    private static final String TAG = "SpdListActivity";
    private static final int TAGSpdListActivity = 888;

    String nikUser,ResultWS;

    private List<DetailSpdModel> detailSpdModels = new ArrayList<>();
    RecyclerView recyclerView;
    SpdListAdapter adapter;
    SharedPrefDataSPD sharedPrefDataSPD;
    SharedPrefManager sharedPrefManager;

    TextView mDataKosong;
    Button mRetry;

    String noTask,NamaTeknisi;
    public static String vid;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spd_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Spd Per Vid");
        sharedPrefDataSPD = new SharedPrefDataSPD(this);
        sharedPrefManager = new SharedPrefManager(this);

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            noTask = in.getString(SharedPrefDataSPD.NoTask);
            NamaTeknisi = in.getString(SharedPrefDataSPD.NamaTeknisi);
        }

        Log.i(TAG, "NoTask: " + noTask);
        sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NoTask,noTask);

        nikUser = sharedPrefManager.getSpNik();

        new AsyingTaskSPD().execute();

        recyclerView = findViewById(R.id.rvCardSpdDetail);
        mProgress = findViewById(R.id.progress_ListSpd);
        mDataKosong = findViewById(R.id.tvDataKosongListSpd);
        mRetry = findViewById(R.id.btnRetryListSpd);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new SpdListAdapter(getApplicationContext(), detailSpdModels);
        adapter.setOnItemClickListener(new SpdListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DetailSpdModel obj, int position) {
                Intent intent = new Intent(SpdListActivity.this, SpdDetailActivity.class);
                intent.putExtra(SharedPrefDataSPD.VIDSAVE,obj.getVid());
                intent.putExtra(SharedPrefDataSPD.NOTASKSAVE,obj.getNoTask());
                intent.putExtra(SharedPrefDataSPD.NamaTeknisi, NamaTeknisi);
                startActivityForResult(intent,TAGSpdListActivity);
            }
        });

        recyclerView.setAdapter(adapter);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyingTaskSPD().execute();
            }
        });

    }

    public void clear() {
        detailSpdModels.clear();
        adapter.notifyDataSetChanged();
    }

    private class AsyingTaskSPD extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getListSPD();
        }
        @Override
        protected String doInBackground(Void ... arg0) {
            getListSPD();
            return "OK";
        }
    }

    public void getListSPD(){
        String url = BaseUrl.getPublicIp + BaseUrl.listSpdVid +noTask;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "Url SPD: "+url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    ResultWS = jsonObj.getString("Result");
                                    clear();
                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d(TAG, "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);

                                            vid = data.getString("VID");

                                            DetailSpdModel sp = new DetailSpdModel(
                                                    i,
                                                    data.getString("NoTask"),
                                                    data.getString("flagconfirm"),
                                                    data.getString("NAMAREMOTE"),
                                                    data.getString("VID"),
                                                    data.getString("TotalPengeluaran")
                                            );

                                            detailSpdModels.add(sp);

                                            mProgress.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);

                                        }

                                        adapter.notifyDataSetChanged();

                                    }else {
                                        mProgress.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                        String Responsecode = "Data SPD Tidak Ada";
                                        mDataKosong.setVisibility(View.VISIBLE);
                                        mRetry.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                        mDataKosong.setText(Responsecode);
                                        Log.i(TAG, "Response True: " + Responsecode);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mProgress.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    mDataKosong.setVisibility(View.VISIBLE);
                                    mRetry.setVisibility(View.VISIBLE);
                                    mDataKosong.setText("JSONException ERROR");
                                    Log.i(TAG, "JSONException: " + e.getMessage());
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
                        mDataKosong.setVisibility(View.VISIBLE);
                        mRetry.setVisibility(View.VISIBLE);
                        mDataKosong.setText(error.getMessage()    );
                        Log.i("TAG", "onErrorResponse: " + error.getMessage());
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
//            onBackPressed();;
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == TAGSpdListActivity ){
            if (resultCode == RESULT_OK){
                detailSpdModels.clear();
                noTask = data.getStringExtra(SharedPrefDataSPD.NoTask);
                new AsyingTaskSPD().execute();
            }
        }
    }

}
