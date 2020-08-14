package selindoalpha.com.selindovsatprod.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;

public class SpdDetailActivity extends AppCompatActivity {

    private static final String TAG = "SpdDetailActivity";
    private static final int TAGSpdDetailActivity = 999;

    public static String flagUpload;

    String vid, noTask,NamaTeknisi;

    TextView mNamaRemote, mNoTask, mVid, mNamaTask, mIpLan, mlokasi, mNote, statusSpd,tvJenisBiaya;
    CardView info, formList;
    Button mKonfirm;

    SharedPrefDataSPD sharedPrefDataSPD;
    ProgressDialog progressDialog;

    Boolean statusKonfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spd_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Form Penggunaan Uang");

        progressDialog = new ProgressDialog(this);

        sharedPrefDataSPD = new SharedPrefDataSPD(this);

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            vid = in.getString(SharedPrefDataSPD.VIDSAVE);
            noTask = in.getString(SharedPrefDataSPD.NOTASKSAVE);
            NamaTeknisi = in.getString(SharedPrefDataSPD.NamaTeknisi);
        }

        mNamaRemote = findViewById(R.id.tvNamaRemoteSpd);
        mNoTask = findViewById(R.id.tvNoTask);
        mVid = findViewById(R.id.tvNoVid);
        mNamaTask = findViewById(R.id.tvNamaTask);
        mIpLan = findViewById(R.id.tvIpLanSpd);
        mlokasi = findViewById(R.id.tvLokasiSpd);
        info = findViewById(R.id.cvLayout);
        formList = findViewById(R.id.listForm);
        mKonfirm = findViewById(R.id.btnKonfirmasiSpd);
        mNote = findViewById(R.id.noteSpdKonfrim);
        statusSpd = findViewById(R.id.status_upload_spd);
        tvJenisBiaya = findViewById(R.id.tvJenisBiayaSpd);

        Log.i(TAG, "onCreate: VID" + vid);
        Log.i(TAG, "onCreate: NoTask" + noTask);

        getDataSpdVid();

        @SuppressLint("SimpleDateFormat")
        DateFormat dt = new SimpleDateFormat("HH:mm:ss");
        String time = dt.format(Calendar.getInstance().getTime());
        Log.i("Time", "Realtime: " + time);


    }

    public void getDataSpdVid(){
        progressDialog.show();
        mKonfirm.setVisibility(View.GONE);
        mNote.setVisibility(View.GONE);
        String url = BaseUrl.getPublicIp + BaseUrl.getSpdVid+noTask+"/"+vid;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "getDataSpdVid Detail: " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new Handler().postDelayed(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    String ResultWS = jsonObj.getString("Result");
                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d(TAG, "onResponse: " + jsonArray);
                                        JSONObject data = jsonArray.getJSONObject(0);
                                            Log.i(TAG, "Raw : " + data);
                                            String statusConfirm = data.getString("flagconfirm");
                                            Log.i(TAG, "run: "+ statusConfirm);
                                            Log.i(TAG, "status : "+statusConfirm);
                                            Log.i(TAG, "GetSpdVid : NoTask = " + data.getString("NoTask"));
                                        Log.i(TAG, "GetSpdVid : VID = " + data.getString("VID"));

                                        flagUpload = data.getString("flagupload");

                                        if (flagUpload.equals("null")){
                                            statusSpd.setBackgroundResource(R.color.super_hot);
                                        }
                                        if (flagUpload.equals("true")){
                                            statusSpd.setBackgroundResource(R.color.colorGreen500);
                                        }
                                        if (flagUpload.equals("false")){
                                            statusSpd.setBackgroundResource(R.color.super_hot);
                                        }
                                        if (statusConfirm.equals("null")) {

                                            Log.i(TAG, "GetSpdVid : NamaTask = " + data.getString("NamaTask"));
                                            Log.i(TAG, "GetSpdVid : NoTask = " + data.getString("NoTask"));
                                            Log.i(TAG, "GetSpdVid : NAMAREMOTE = " + data.getString("NAMAREMOTE"));
                                            Log.i(TAG, "GetSpdVid : VID = " + data.getString("flagconfirm"));
                                            Log.i(TAG, "GetSpdVid : IPLAN = " + data.getString("VID"));
                                            Log.i(TAG, "GetSpdVid : ALAMAT = " + data.getString("IPLAN"));
                                            Log.i(TAG, "GetSpdVid : ALAMAT = " + data.getString("ALAMAT"));

                                            String namaTask = data.getString("NamaTask");
                                            String noTask = data.getString("NoTask");
                                            String vid = data.getString("VID");
                                            String namaRemote = data.getString("NAMAREMOTE");
                                            String ipLan = data.getString("IPLAN");
                                            String alamat = data.getString("ALAMAT");
                                            String JenisBiaya = data.getString("Keterangan");

                                            if (JenisBiaya.equals("null")){
                                                JenisBiaya = "N/A";
                                            }

                                            mNamaRemote.setText(namaRemote);
                                            mNoTask.setText(noTask);
                                            mVid.setText(vid);
                                            mNamaTask.setText(namaTask);
                                            mIpLan.setText(ipLan);
                                            mlokasi.setText(alamat);
                                            tvJenisBiaya.setText(JenisBiaya);

                                            mKonfirm.setVisibility(View.VISIBLE);
                                            mNote.setVisibility(View.VISIBLE);

                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NOTASKSAVE,noTask);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.VIDSAVE,vid);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NAMATASKSAVE,namaTask);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NAMAREMOTESAVE,namaRemote);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.IPLANSAVE,ipLan);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.LOKASISAVE,alamat);

                                        }
                                        if (statusConfirm.equals("true")) {

                                            Log.i(TAG, "GetSpdVid : NamaTask = " + data.getString("NamaTask"));
                                            Log.i(TAG, "GetSpdVid : NoTask = " + data.getString("NoTask"));
                                            Log.i(TAG, "GetSpdVid : NAMAREMOTE = " + data.getString("NAMAREMOTE"));
                                            Log.i(TAG, "GetSpdVid : VID = " + data.getString("flagconfirm"));
                                            Log.i(TAG, "GetSpdVid : IPLAN = " + data.getString("VID"));
                                            Log.i(TAG, "GetSpdVid : ALAMAT = " + data.getString("IPLAN"));
                                            Log.i(TAG, "GetSpdVid : ALAMAT = " + data.getString("ALAMAT"));

                                            String namaTask = data.getString("NamaTask");
                                            String noTask = data.getString("NoTask");
                                            String vid = data.getString("VID");
                                            String namaRemote = data.getString("NAMAREMOTE");
                                            String ipLan = data.getString("IPLAN");
                                            String alamat = data.getString("ALAMAT");
                                            String JenisBiaya = data.getString("Keterangan");

                                            if (JenisBiaya.equals("null")){
                                                JenisBiaya = "N/A";
                                            }

                                            mNamaRemote.setText(namaRemote);
                                            mNoTask.setText(noTask);
                                            mVid.setText(vid);
                                            mNamaTask.setText(namaTask);
                                            mIpLan.setText(ipLan);
                                            mlokasi.setText(alamat);
                                            tvJenisBiaya.setText(JenisBiaya);

                                            mKonfirm.setVisibility(View.GONE);
                                            mNote.setVisibility(View.GONE);

                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NOTASKSAVE,noTask);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.VIDSAVE,vid);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NAMATASKSAVE,namaTask);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NAMAREMOTESAVE,namaRemote);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.IPLANSAVE,ipLan);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.LOKASISAVE,alamat);

                                        }
                                        if (statusConfirm.equals("false")) {

                                            Log.i(TAG, "GetSpdVid : NamaTask = " + data.getString("NamaTask"));
                                            Log.i(TAG, "GetSpdVid : NoTask = " + data.getString("NoTask"));
                                            Log.i(TAG, "GetSpdVid : NAMAREMOTE = " + data.getString("NAMAREMOTE"));
                                            Log.i(TAG, "GetSpdVid : VID = " + data.getString("flagconfirm"));
                                            Log.i(TAG, "GetSpdVid : IPLAN = " + data.getString("VID"));
                                            Log.i(TAG, "GetSpdVid : ALAMAT = " + data.getString("IPLAN"));
                                            Log.i(TAG, "GetSpdVid : ALAMAT = " + data.getString("ALAMAT"));

                                            String namaTask = data.getString("NamaTask");
                                            String noTask = data.getString("NoTask");
                                            String vid = data.getString("VID");
                                            String namaRemote = data.getString("NAMAREMOTE");
                                            String ipLan = data.getString("IPLAN");
                                            String alamat = data.getString("ALAMAT");
                                            String JenisBiaya = data.getString("Keterangan");

                                            if (JenisBiaya.equals("null")){
                                                JenisBiaya = "N/A";
                                            }

                                            mNamaRemote.setText(namaRemote);
                                            mNoTask.setText(noTask);
                                            mVid.setText(vid);
                                            mNamaTask.setText(namaTask);
                                            mIpLan.setText(ipLan);
                                            mlokasi.setText(alamat);
                                            tvJenisBiaya.setText(JenisBiaya);

                                            mKonfirm.setVisibility(View.VISIBLE);
                                            mNote.setVisibility(View.VISIBLE);

                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NOTASKSAVE,noTask);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.VIDSAVE,vid);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NAMATASKSAVE,namaTask);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.NAMAREMOTESAVE,namaRemote);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.IPLANSAVE,ipLan);
                                            sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.LOKASISAVE,alamat);

                                        }

                                        progressDialog.dismiss();
                                    }else {
                                        progressDialog.dismiss();
                                        String Responsecode = jsonObj.getString("Data Tidak ada");
                                        Log.i(TAG, "else Response True: " + Responsecode);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Log.i(TAG, "JSONException : " + e.getMessage());

                                }

                            }
                        },200);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                        Log.i(TAG, "onErrorResponse: " + error.getMessage());

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
//            onBackPressed();
            Intent intent = new Intent();
            intent.putExtra(SharedPrefDataSPD.NoTask, noTask);
            intent.putExtra(SharedPrefDataSPD.NamaTeknisi,NamaTeknisi);
            setResult(RESULT_OK,intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra(SharedPrefDataSPD.NoTask, noTask);
        intent.putExtra(SharedPrefDataSPD.NamaTeknisi,NamaTeknisi);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void detailSpd(View view) {

    }

    public void inputSpd(View view) {
        Intent intent = new Intent(SpdDetailActivity.this,ListSpdGambarActivity.class);
        intent.putExtra(SharedPrefDataSPD.VIDSAVE, vid);
        intent.putExtra(SharedPrefDataSPD.NOTASKSAVE, noTask);
        intent.putExtra(SharedPrefDataSPD.NamaTeknisi, NamaTeknisi);
        startActivityForResult(intent,TAGSpdDetailActivity);

    }

    public void konfrimUang(View view) {

        if (flagUpload.equals("true")){
            Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
            updateKonfrim();
        }else {
            Toasty.warning(this, "Lengkapi Data Yang masih Merah!", Toast.LENGTH_SHORT, true).show();
        }


    }

    public void updateKonfrim(){
        String url = BaseUrl.getPublicIp+BaseUrl.insetSpdVid;
        Boolean statusKonf = statusKonfirm =  true;
        Log.i("url", "SPD vid: "+ url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String ResultWS = jsonResponse.getString("Result");
                            if (ResultWS.equals("True")){
                                String Data1 = jsonResponse.getString("Data1");
                                Toasty.success(SpdDetailActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                Log.i("TAG", "onResponse: " + Data1);

                                Log.i("TAG","Ski data from server - ok" + response );

                                Intent intent = new Intent();
                                intent.putExtra(SharedPrefDataSPD.NoTask, noTask);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                            Log.i("TAG","Ski data from server - ok" + response );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG","Ski error connect - " + error);
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

//                String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"ID = '"+idPenguna+"'\"}],\"PARAM2\": [{\"WhereDatabaseinYou\": \"file_id='"+idFile+"'\"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM3\": [{\"WhereDatabaseinYou\":\"VID='"+vid+"' and NoTask = '"+noTask+"'\",\"flagconfirm\": "+statusKonf+"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == TAGSpdDetailActivity ){
            if (resultCode == RESULT_OK){
//                mKonfirm.setVisibility(View.VISIBLE);
//                mNote.setVisibility(View.VISIBLE);
                noTask = data.getStringExtra(SharedPrefDataSPD.NoTask);
                vid = data.getStringExtra(SharedPrefDataSPD.VID);
                NamaTeknisi = data.getStringExtra(SharedPrefDataSPD.NamaTeknisi);
                getDataSpdVid();
            }
        }
    }

}
