package selindoalpha.com.selindovsatprod.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Adapter.ListSpdGambarAdapter;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.ListSpdGambarModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;

public class ListSpdGambarActivity extends AppCompatActivity {

    private static final String TAG = "ListSpdGambarActivity";
    private static final int TAGListSpdGambarActivity = 1010;

    private List<ListSpdGambarModel> spdGambarModels = new ArrayList<>();
    private FloatingActionButton fab;
    RecyclerView recyclerView;
    ListSpdGambarAdapter adapter;

    TextView mDataKosong;
    Button mRetry;

    String vid, noTask, noTasks, ResultWS,NamaTeknisi;
    ProgressBar mProgress;

    SharedPrefDataSPD sharedPrefDataSPD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_spd_gambar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("SPD");

        sharedPrefDataSPD = new SharedPrefDataSPD(this);

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            vid = in.getString(SharedPrefDataSPD.VIDSAVE);
            noTask = in.getString(SharedPrefDataSPD.NOTASKSAVE);
            NamaTeknisi = in.getString(SharedPrefDataSPD.NamaTeknisi);
        }

        new AsyingTaskSPD().execute();

        recyclerView = findViewById(R.id.rvCardSpdGbr);
        mProgress = findViewById(R.id.progress_list_spd_gbr);
        mDataKosong = findViewById(R.id.tvDataKosongSpdGbr);
        mRetry = findViewById(R.id.btnRetrySpdGbr);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ListSpdGambarAdapter(getApplicationContext(), spdGambarModels);

        adapter.setOnMoreButtonClickListener(new ListSpdGambarAdapter.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, ListSpdGambarModel obj, int position) {
                String replaceAll = obj.getFile_url();
                replaceAll = replaceAll.replace(" ","");
                Intent mIntent = new Intent(ListSpdGambarActivity.this, ViewDetailPhotoActivity.class);
                mIntent.putExtra("ImageSpd", BaseUrl.getPublicIp + "vsatmonitoring/" + replaceAll);
                mIntent.putExtra(SharedPrefDataSPD.VIDSAVE, obj.getVID());
                mIntent.putExtra(SharedPrefDataSPD.NOTASKSAVE, obj.getNoTask());
                startActivityForResult(mIntent,TAGListSpdGambarActivity);
            }
        });

        adapter.setOnItemClickListener(new ListSpdGambarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ListSpdGambarModel obj, int position) {
                String replaceAll = obj.getFile_url();
                replaceAll = replaceAll.replace(" ","");
                Intent in = new Intent(ListSpdGambarActivity.this, InputSpdGambarActivity.class);
                in.putExtra(SharedPrefDataSPD.VIDSAVE, obj.getVID());
                in.putExtra(SharedPrefDataSPD.NOTASKSAVE, obj.getNoTask());
                in.putExtra(SharedPrefDataSPD.JenisBiaya, obj.getJenisBiaya());
                in.putExtra(SharedPrefDataSPD.Nominal, obj.getNominal());
                in.putExtra(SharedPrefDataSPD.TglInputBiaya, obj.getTglInputBiaya());
                in.putExtra("note", obj.getCatatanTransaksi());
                in.putExtra("idPenguna", obj.getId());
                in.putExtra("idFile", obj.getFile_id());
                String urlfoto = BaseUrl.getPublicIp + "vsatmonitoring/" + replaceAll;
                String nameFoto = urlfoto.replaceAll("http://182.16.164.170:7020/vsatmonitoring/UploadFoto/",  "");
                in.putExtra("nameFoto", nameFoto);
                in.putExtra("urlFoto",urlfoto);
                in.putExtra("Task","Edit");
                startActivityForResult(in,TAGListSpdGambarActivity);
            }
        });

        adapter.setOnItemLongClickListener(new ListSpdGambarAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, ListSpdGambarModel obj, int position) {
                DialogDelete(obj.getId(),obj.getFile_id());
            }
        });

        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fabAddSpdGambar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListSpdGambarActivity.this, InputSpdGambarActivity.class);
                intent.putExtra(SharedPrefDataSPD.VIDSAVE, vid);
                intent.putExtra(SharedPrefDataSPD.NOTASKSAVE, noTasks);
                intent.putExtra("Task","Simpan");
                intent.putExtra(SharedPrefDataSPD.NamaTeknisi, NamaTeknisi);
                startActivityForResult(intent,TAGListSpdGambarActivity);
            }
        });

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyingTaskSPD().execute();
            }
        });


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
            return "OK";
        }
    }

    public void clear() {
        spdGambarModels.clear();
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    public void getListSPD(){
        mProgress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        String url = BaseUrl.getPublicIp + BaseUrl.listSpdVidGambar+vid;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "Url SPD: "+url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new Handler().postDelayed(new Runnable() {
                            @SuppressLint("RestrictedApi")
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

                                            String flag = data.getString("flagconfirm");
                                            Log.i(TAG, "Status flag: " + flag);

                                            if (flag.equals("null")){

                                                ListSpdGambarModel md = new ListSpdGambarModel(
                                                        i,
                                                        data.getString("ID"),
                                                        data.getString("file_url"),
                                                        data.getString("Description"),
                                                        data.getString("VID"),
                                                        data.getString("NoTask"),
                                                        data.getString("CatatanTransaksi"),
                                                        data.getString("JenisBiaya"),
                                                        data.getString("Nominal"),
                                                        data.getString("TglInputBiaya"),
                                                        data.getString("file_id"),
                                                        data.getString("flagconfirm")
                                                );

                                                spdGambarModels.add(md);

                                                mProgress.setVisibility(View.GONE);
                                                recyclerView.setVisibility(View.VISIBLE);
                                                fab.setVisibility(View.VISIBLE);
                                                mDataKosong.setVisibility(View.GONE);
                                            }
                                            else if (flag.equals("true")){
                                                ListSpdGambarModel md = new ListSpdGambarModel(
                                                        i,
                                                        data.getString("ID"),
                                                        data.getString("file_url"),
                                                        data.getString("Description"),
                                                        data.getString("VID"),
                                                        data.getString("NoTask"),
                                                        data.getString("CatatanTransaksi"),
                                                        data.getString("JenisBiaya"),
                                                        data.getString("Nominal"),
                                                        data.getString("TglInputBiaya"),
                                                        data.getString("file_id"),
                                                        data.getString("flagconfirm")
                                                );
                                                spdGambarModels.add(md);
                                                mProgress.setVisibility(View.GONE);
                                                recyclerView.setVisibility(View.VISIBLE);
                                                fab.setVisibility(View.GONE);
                                                mDataKosong.setVisibility(View.GONE);
                                            }
                                            else if (flag.equals("false")){
                                                ListSpdGambarModel md = new ListSpdGambarModel(
                                                        i,
                                                        data.getString("ID"),
                                                        data.getString("file_url"),
                                                        data.getString("Description"),
                                                        data.getString("VID"),
                                                        data.getString("NoTask"),
                                                        data.getString("CatatanTransaksi"),
                                                        data.getString("JenisBiaya"),
                                                        data.getString("Nominal"),
                                                        data.getString("TglInputBiaya"),
                                                        data.getString("file_id"),
                                                        data.getString("flagconfirm")
                                                );
                                                spdGambarModels.add(md);
                                                mProgress.setVisibility(View.GONE);
                                                recyclerView.setVisibility(View.VISIBLE);
                                                fab.setVisibility(View.VISIBLE);
                                                mDataKosong.setVisibility(View.GONE);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        mProgress.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                        String Responsecode = "Pengeluaran SPD Tidak Ada";
                                        mDataKosong.setVisibility(View.VISIBLE);
                                        mRetry.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                        mDataKosong.setText(Responsecode);
                                        Log.i(TAG, "Response True: " + Responsecode);
                                        fab.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mProgress.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    mDataKosong.setVisibility(View.VISIBLE);
                                    mRetry.setVisibility(View.VISIBLE);
                                    mDataKosong.setText("Cek Jaringan Anda");
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

    public void DialogDelete(String IdPengguna,String FileId){
        new AlertDialog.Builder(ListSpdGambarActivity.this)
                .setTitle("Delete")
                .setMessage("Are You Sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(IdPengguna,FileId);
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

    public void delete(String IdPengguna,String FileId){
        String url = BaseUrl.getPublicIp+BaseUrl.deleteSpdVid;
        Log.i("url", "General info: "+ url);
        String vid = sharedPrefDataSPD.getVID();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String ResultWS = jsonResponse.getString("Result");
                            if (ResultWS.equals("True")){
                                String Data1 = jsonResponse.getString("Data1");
                                Toasty.success(ListSpdGambarActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                Log.i("TAG", "onResponse: " + Data1);

                                Log.i("TAG","Ski data from server - ok" + response );

                                Intent intent = new Intent();
                                intent.putExtra(SharedPrefDataSPD.VID, vid);
                                intent.putExtra(SharedPrefDataSPD.NoTask, noTask);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                            Log.i("TAG","Ski data from server - ok" + response );

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(ListSpdGambarActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG","Ski error connect - " + error);
                        Toasty.error(ListSpdGambarActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
                    }
                }){

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Header", "Dota2");
                return headers;
            }

            @Override
            public byte[] getBody() {

                String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"ID = '"+IdPengguna+"'\"}],\"PARAM2\": [{\"WhereDatabaseinYou\": \"file_id='"+FileId+"'\"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                Log.i("sendBody", "sendBody: " + str);
                return str.getBytes();
            }

            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }

        };

        Mysingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Intent in = new Intent();
            in.putExtra(SharedPrefDataSPD.NoTask,noTask);
            in.putExtra(SharedPrefDataSPD.VID,vid);
            in.putExtra(SharedPrefDataSPD.NamaTeknisi,NamaTeknisi);
            setResult(RESULT_OK,in);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent in = new Intent();
        in.putExtra(SharedPrefDataSPD.NoTask,noTask);
        in.putExtra(SharedPrefDataSPD.VID,vid);
        in.putExtra(SharedPrefDataSPD.NamaTeknisi,NamaTeknisi);
        setResult(RESULT_OK,in);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == TAGListSpdGambarActivity ){
            if (resultCode == RESULT_OK){
                noTask = data.getStringExtra(SharedPrefDataSPD.NoTask);
                vid = data.getStringExtra(SharedPrefDataSPD.VID);
                NamaTeknisi = data.getStringExtra(SharedPrefDataSPD.NamaTeknisi);
                new AsyingTaskSPD().execute();
            }
        }
    }
}
