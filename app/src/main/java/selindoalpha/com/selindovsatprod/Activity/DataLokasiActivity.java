package selindoalpha.com.selindovsatprod.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class DataLokasiActivity extends AppCompatActivity {

    private static final String TAG = "DataLokasiActivity";

    String jsonStr,ResultWS;
    Boolean statusDL = false;
    String path,response, hubStr;
    String requestWsHub;
    SharedPrefManager sharedPrefManager;
    TextView mNamaPic, mTlpPic, mProvinsi, mKabupaten, mJarkom, mSatelite, mLatitude, mLongitude;
    TextView mNamaRemote, mAlamatInstall, mKanwil, mArea, mAlamatSekarang, mCatetan;

    SharedPrefDataTask sharedPrefDataTask;
    ProgressDialog progressDialog;
    String id, vid, noTask;
    Spinner mSpinerHub;
    Button mBtnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_lokasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("DETAIL DATA LOKASI");

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            id = in.getString(SharedPrefManager.SP_ID);
            vid = in.getString(SharedPrefManager.SP_VID);
            noTask = in.getString(SharedPrefDataTask.NoTask);
        }
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);
        progressDialog = new ProgressDialog(this);
        new AsyingDataLokasi().execute();

        mNamaPic = findViewById(R.id.namaPic);
        mTlpPic = findViewById(R.id.phonePic);
        mProvinsi = findViewById(R.id.provinsi);
        mKabupaten = findViewById(R.id.kabupaten);
        mJarkom = findViewById(R.id.jarkom);
        mSatelite = findViewById(R.id.satelite);
        mSpinerHub = findViewById(R.id.spinnerHub);
        mLatitude = findViewById(R.id.Latitude);
        mLongitude = findViewById(R.id.Longitude);
        mNamaRemote = findViewById(R.id.namaRemote);
        mAlamatInstall = findViewById(R.id.alamatInstall);
        mKanwil = findViewById(R.id.kanwil);
        mArea = findViewById(R.id.kancaInduk);
        mAlamatSekarang = findViewById(R.id.alamatSekarang);
        mCatetan = findViewById(R.id.catetan);
        mBtnSave = findViewById(R.id.btnUpdate);

        mNamaPic.setText(sharedPrefDataTask.getCustPIC());
        mTlpPic.setText(sharedPrefDataTask.getCustPIC_Phone());
        mProvinsi.setText(sharedPrefDataTask.getPROVINSI());
        mKabupaten.setText(sharedPrefDataTask.getKOTA());
        mJarkom.setText(sharedPrefDataTask.getIdJarkom());
        mSatelite.setText(sharedPrefDataTask.getIdSatelite());
        mLatitude.setText(sharedPrefDataTask.getLatitude());
        mLongitude.setText(sharedPrefDataTask.getLongitude());
        mNamaRemote.setText(sharedPrefDataTask.getNAMAREMOTE());
        mAlamatInstall.setText(sharedPrefDataTask.getAlamatInstall());
        mKanwil.setText(sharedPrefDataTask.getKANWIL());
        mArea.setText(sharedPrefDataTask.getKANCAINDUK());
        mAlamatSekarang.setText(sharedPrefDataTask.getAlamatSekarang());
        mCatetan.setText(sharedPrefDataTask.getCatatan());

        mSpinerHub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"Spinner String = "+mSpinerHub.getSelectedItem().toString());
                TextView getStr = (TextView) mSpinerHub.getSelectedView();
                getStr.setText(mSpinerHub.getSelectedItem().toString());
                hubStr = getStr.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class AsyingDataLokasi extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getData();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
//            getListData();
            return "OK";
        }
    }

    public void getData(){
        progressDialog.show();
        String url = BaseUrl.getPublicIp + BaseUrl.detailTask+id;
        Log.i(TAG, "NoTask DataLokasi: " + id);
        Log.i(TAG, "Url DataLokasi: " + url);
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }

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

                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d("TAG", "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);

                                            String statusDataLokasi = data.getString("IdStatusPerbaikan");
                                            String statusDL = data.getString("FlagDataLokasi");
                                            Log.i("TAG", "run: "+ statusDL);

                                            Log.i("TAG", "status : "+statusDataLokasi);
                                            if (statusDataLokasi.equals("1")) {
                                                Log.i(TAG, "TaskList: "+data);
                                                Log.i(TAG, "data_lokasi_Open: " + data.getString("NAMAREMOTE"));
                                                Log.i(TAG, "data_lokasi_Open: " + data.getString("ALAMAT"));
                                                Log.i(TAG, "data_lokasi_Open: " + data.getString("PROVINSI1"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("Provinsi"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("KOTA"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("KANWIL"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("KANCAINDUK"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("PIC"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("NoHpPic"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("IdJarkom"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("IdSatelite"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("Hub"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("Latitude"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("Longitude"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("AlamatSekarang"));
                                                Log.i(TAG, "data_lokasi_Open: "+ data.getString("Catatan"));

                                                if (data.getString("NAMAREMOTE").equals("null") || data.getString("NAMAREMOTE") == null || data.getString("NAMAREMOTE").equals("")){
                                                    String namaRemote = "";
                                                    mNamaRemote.setText(namaRemote);
                                                }else {
                                                    String namaRemote = data.getString("NAMAREMOTE");
                                                    mNamaRemote.setText(namaRemote);
                                                }
                                                if (data.getString("ALAMAT").equals("null") || data.getString("ALAMAT") == null || data.getString("ALAMAT").equals("")){
                                                    String alamatInstall = "";
                                                    mAlamatInstall.setText(alamatInstall);
                                                }else {
                                                    String alamatInstall = data.getString("ALAMAT");
                                                    mAlamatInstall.setText(alamatInstall);
                                                }
                                                if (data.getString("KANWIL").equals("null") || data.getString("KANWIL") == null || data.getString("KANWIL").equals("")){
                                                    String kanwil = "";
                                                    mKanwil.setText(kanwil);
                                                }else {
                                                    String kanwil = data.getString("KANWIL");
                                                    mKanwil.setText(kanwil);
                                                }
                                                if (data.getString("KANCAINDUK").equals("null") || data.getString("KANCAINDUK").equals(null) || data.getString("KANCAINDUK").equals("")){
                                                    String area = "";
                                                    mArea.setText(area);
                                                }else {
                                                    String area = data.getString("KANCAINDUK");
                                                    mArea.setText(area);
                                                }
                                                if (data.getString("PIC").equals("null") || data.getString("PIC").equals(null) || data.getString("PIC").equals("")){
                                                    String namaPic = "";
                                                    mNamaPic.setText(namaPic);
                                                }else {
                                                    String namaPic = data.getString("PIC");
                                                    mNamaPic.setText(namaPic);
                                                }
                                                if (data.getString("NoHpPic").equals("null") || data.getString("NoHpPic").equals(null) || data.getString("NoHpPic").equals("")){
                                                    String noPic = "";
                                                    mTlpPic.setText(noPic);
                                                }else {
                                                    String noPic = data.getString("NoHpPic");
                                                    mTlpPic.setText(noPic);
                                                }
                                                if (data.getString("Provinsi").equals("null") || data.getString("Provinsi").equals(null) || data.getString("Provinsi").equals("")){
                                                    String provinsi = "";
                                                    mProvinsi.setText(provinsi);
                                                }else {
                                                    String provinsi = data.getString("Provinsi");
                                                    mProvinsi.setText(provinsi);
                                                }
                                                if (data.getString("KOTA").equals("null") || data.getString("KOTA").equals(null) || data.getString("KOTA").equals("")){
                                                    String kota = "";
                                                    mKabupaten.setText(kota);
                                                }else {
                                                    String kota = data.getString("KOTA");
                                                    mKabupaten.setText(kota);
                                                }
                                                if (data.getString("IdJarkom").equals("null") || data.getString("IdJarkom").equals(null)  || data.getString("IdJarkom").equals("")){
                                                    String jarkom = "";
                                                    mJarkom.setText(jarkom);
                                                }else {
                                                    String jarkom = data.getString("IdJarkom");
                                                    mJarkom.setText(jarkom);
                                                }
                                                if (data.getString("IdSatelite").equals("null") || data.getString("IdSatelite").equals(null) || data.getString("IdSatelite").equals("")){
                                                    String satelite = "";
                                                    mSatelite.setText(satelite);
                                                }else {
                                                    String satelite = data.getString("IdSatelite");
                                                    mSatelite.setText(satelite);
                                                }
                                                if (data.getString("Latitude").equals("null") || data.getString("Latitude").equals(null) || data.getString("Latitude").equals("")){
                                                    String latitude = "";
                                                    mLatitude.setText(latitude);
                                                }else {
                                                    String latitude = data.getString("Latitude");
                                                    mLatitude.setText(latitude);
                                                }
                                                if (data.getString("Longitude").equals("null") || data.getString("Longitude").equals(null) || data.getString("Longitude").equals("")){
                                                    String longtitude = "";
                                                    mLongitude.setText(longtitude);
                                                }else {
                                                    String longtitude = data.getString("Longitude");
                                                    mLongitude.setText(longtitude);
                                                }
                                                if (data.getString("AlamatSekarang").equals("null") || data.getString("AlamatSekarang").equals(null) || data.getString("AlamatSekarang").equals("null")){
                                                    String alamatSekarang = "";
                                                    mAlamatSekarang.setText(alamatSekarang);
                                                }else {
                                                    String alamatSekarang = data.getString("AlamatSekarang");
                                                    mAlamatSekarang.setText(alamatSekarang);
                                                }
                                                if (data.getString("Catatan").equals("null") || data.getString("Catatan").equals(null) || data.getString("Catatan").equals("")){
                                                    String catetan = "";
                                                    mCatetan.setText(catetan);
                                                }else {
                                                    String catetan = data.getString("Catatan");
                                                    mCatetan.setText(catetan);
                                                }
                                                if (data.getString("Hub").equals("null") || data.getString("Hub").equals(null) || data.getString("Hub").equals("")){
                                                    hubStr = "";
                                                    TextView getStr = (TextView) mSpinerHub.getSelectedView();
                                                    getStr.setText(hubStr);//changes the selected item text to this
                                                }else {
                                                    hubStr = data.getString("Hub");
                                                    TextView getStr = (TextView) mSpinerHub.getSelectedView();
                                                    getStr.setText(hubStr);//changes the selected item text to this
                                                }
                                            }

                                            if (statusDataLokasi.equals("4")) {
                                                Log.i(TAG, "TaskList: "+data);
                                                Log.i(TAG, "data_lokasi_Finish: " + data.getString("NAMAREMOTE"));
                                                Log.i(TAG, "data_lokasi_Finish: " + data.getString("ALAMAT"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("Provinsi"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("KOTA"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("KANWIL"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("KANCAINDUK"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("PIC"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("NoHpPic"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("IdJarkom"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("IdSatelite"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("Hub"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("Latitude"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("Longitude"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("AlamatSekarang"));
                                                Log.i(TAG, "data_lokasi_Finish: "+ data.getString("Catatan"));


                                                if (data.getString("NAMAREMOTE").equals("null") || data.getString("NAMAREMOTE").equals(null)|| data.getString("NAMAREMOTE").equals("")){
                                                    String namaRemote = "";
                                                    mNamaRemote.setText(namaRemote);
                                                    mNamaRemote.setEnabled(false);
                                                }else {
                                                    String namaRemote = data.getString("NAMAREMOTE");
                                                    mNamaRemote.setText(namaRemote);
                                                    mNamaRemote.setEnabled(false);
                                                }
                                                if (data.getString("ALAMAT").equals("null") || data.getString("ALAMAT").equals(null) || data.getString("ALAMAT").equals("")){
                                                    String alamatInstall = "";
                                                    mAlamatInstall.setText(alamatInstall);
                                                    mAlamatInstall.setEnabled(false);
                                                }else {
                                                    String alamatInstall = data.getString("ALAMAT");
                                                    mAlamatInstall.setText(alamatInstall);
                                                    mAlamatInstall.setEnabled(false);
                                                }
                                                if (data.getString("KANWIL").equals("null") || data.getString("KANWIL").equals(null)  || data.getString("KANWIL").equals("")){
                                                    String kanwil = "";
                                                    mKanwil.setText(kanwil);
                                                    mKanwil.setEnabled(false);
                                                }else {
                                                    String kanwil = data.getString("KANWIL");
                                                    mKanwil.setText(kanwil);
                                                    mKanwil.setEnabled(false);
                                                }
                                                if (data.getString("KANCAINDUK").equals("null") || data.getString("KANCAINDUK").equals(null) || data.getString("KANCAINDUK").equals("")){
                                                    String area = "";
                                                    mArea.setText(area);
                                                    mArea.setEnabled(false);
                                                }else {
                                                    String area = data.getString("KANCAINDUK");
                                                    mArea.setText(area);
                                                    mArea.setEnabled(false);
                                                }
                                                if (data.getString("PIC").equals("null") || data.getString("PIC").equals(null) || data.getString("PIC").equals("")){
                                                    String namaPic = "";
                                                    mNamaPic.setText(namaPic);
                                                    mNamaPic.setEnabled(false);
                                                }else {
                                                    String namaPic = data.getString("PIC");
                                                    mNamaPic.setText(namaPic);
                                                    mNamaPic.setEnabled(false);
                                                }
                                                if (data.getString("NoHpPic").equals("null") || data.getString("NoHpPic").equals(null) || data.getString("NoHpPic").equals("") ){
                                                    String noPic = "";
                                                    mTlpPic.setText(noPic);
                                                    mTlpPic.setEnabled(false);
                                                }else {
                                                    String noPic = data.getString("NoHpPic");
                                                    mTlpPic.setText(noPic);
                                                    mTlpPic.setEnabled(false);
                                                }
                                                if (data.getString("Provinsi").equals("null") || data.getString("Provinsi").equals(null) || data.getString("Provinsi").equals("")){
                                                    String provinsi = "";
                                                    mProvinsi.setText(provinsi);
                                                    mProvinsi.setEnabled(false);
                                                }else {
                                                    String provinsi = data.getString("Provinsi");
                                                    mProvinsi.setText(provinsi);
                                                    mProvinsi.setEnabled(false);
                                                }
                                                if (data.getString("KOTA").equals("null") || data.getString("KOTA").equals(null) || data.getString("KOTA").equals("")){
                                                    String kota = "";
                                                    mKabupaten.setText(kota);
                                                    mKabupaten.setEnabled(false);
                                                }else {
                                                    String kota = data.getString("KOTA");
                                                    mKabupaten.setText(kota);
                                                    mKabupaten.setEnabled(false);
                                                }
                                                if (data.getString("IdJarkom").equals("null") || data.getString("IdJarkom").equals(null) || data.getString("IdJarkom").equals("")){
                                                    String jarkom = "";
                                                    mJarkom.setText(jarkom);
                                                    mJarkom.setEnabled(false);
                                                }else {
                                                    String jarkom = data.getString("IdJarkom");
                                                    mJarkom.setText(jarkom);
                                                    mJarkom.setEnabled(false);
                                                }
                                                if (data.getString("IdSatelite").equals("null") || data.getString("IdSatelite").equals(null) || data.getString("IdSatelite").equals("")){
                                                    String satelite = "";
                                                    mSatelite.setText(satelite);
                                                    mSatelite.setEnabled(false);
                                                }else {
                                                    String satelite = data.getString("IdSatelite");
                                                    mSatelite.setText(satelite);
                                                    mSatelite.setEnabled(false);
                                                }
                                                if (data.getString("Latitude").equals("null") || data.getString("Latitude").equals(null)  || data.getString("Latitude").equals("")){
                                                    String latitude = "";
                                                    mLatitude.setText(latitude);
                                                    mLatitude.setEnabled(false);
                                                }else {
                                                    String latitude = data.getString("Latitude");
                                                    mLatitude.setText(latitude);
                                                    mLatitude.setEnabled(false);
                                                }
                                                if (data.getString("Longitude").equals("null") || data.getString("Longitude").equals(null) || data.getString("Longitude").equals("")){
                                                    String longtitude = "";
                                                    mLongitude.setText(longtitude);
                                                    mLongitude.setEnabled(false);
                                                }else {
                                                    String longtitude = data.getString("Longitude");
                                                    mLongitude.setText(longtitude);
                                                    mLongitude.setEnabled(false);
                                                }
                                                if (data.getString("AlamatSekarang").equals("null") || data.getString("AlamatSekarang").equals(null) || data.getString("AlamatSekarang").equals("null")){
                                                    String alamatSekarang = "";
                                                    mAlamatSekarang.setText(alamatSekarang);
                                                    mAlamatSekarang.setEnabled(false);
                                                }else {
                                                    String alamatSekarang = data.getString("AlamatSekarang");
                                                    mAlamatSekarang.setText(alamatSekarang);
                                                    mAlamatSekarang.setEnabled(false);
                                                }
                                                if (data.getString("Catatan").equals("null") || data.getString("Catatan").equals(null) || data.getString("Catatan").equals("")){
                                                    String catetan = "";
                                                    mCatetan.setText(catetan);
                                                    mCatetan.setEnabled(false);
                                                }else {
                                                    String catetan = data.getString("Catatan");
                                                    mCatetan.setText(catetan);
                                                    mCatetan.setEnabled(false);
                                                }
                                                if (data.getString("Hub").equals("null") || data.getString("Hub").equals(null) || data.getString("Hub").equals("")){
                                                    hubStr = "";
                                                    TextView getStr = (TextView) mSpinerHub.getSelectedView();
                                                    getStr.setText(hubStr);//changes the selected item text to this
                                                    getStr.setEnabled(false);
                                                }else {
                                                    hubStr = data.getString("Hub");
                                                    TextView getStr = (TextView) mSpinerHub.getSelectedView();
                                                    getStr.setText(hubStr);//changes the selected item text to this
                                                    getStr.setEnabled(false);
                                                }

                                                mBtnSave.setVisibility(View.GONE);

                                            }
                                        }
                                        progressDialog.dismiss();

                                    }else {
                                        progressDialog.dismiss();
                                        String Responsecode = jsonObj.getString("Data1");
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
                        progressDialog.dismiss();
                        error.getMessage();
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
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(View view) {
        Animation fadein = new AlphaAnimation(0,1);
        fadein.setDuration(50);
        mBtnSave.startAnimation(fadein);
        updateDataLokasi();

    }


    private void updateDataLokasi() {
        String url = BaseUrl.getPublicIp + BaseUrl.updateDataLokasi;
        Log.i("url", "Data Lokasi: " + url);
        Log.i("GetVID", "updateDataLokasi: " + id);
        String namaRrmote = mNamaRemote.getText().toString();
        String alamatInstall = mAlamatInstall.getText().toString();
        String kanwil = mKanwil.getText().toString();
        String area = mArea.getText().toString();
        String alamatSekarang = mAlamatSekarang.getText().toString();
        String catetan = mCatetan.getText().toString();
        String namaPic = mNamaPic.getText().toString();
        String tlpPic = mTlpPic.getText().toString();
        String provinsi = mProvinsi.getText().toString();
        String kabupaten = mKabupaten.getText().toString();
        String jarkom = mJarkom.getText().toString();
        String satelit = mSatelite.getText().toString();
        String hub = hubStr;
        String latitude = mLatitude.getText().toString();
        String longtitude = mLongitude.getText().toString();
        Boolean statusDl = statusDL = true;
        if (namaRrmote.matches("")) {
            mNamaRemote.setError("isi data ini");
        } else if (alamatInstall.matches("")) {
            mAlamatInstall.setError("isi data ini");
        } else if (kanwil.matches("")) {
            mKanwil.setError("isi data ini");
        } else if (area.matches("")) {
            mArea.setError("isi data ini");
        } else if (alamatSekarang.matches("")) {
            mAlamatSekarang.setError("isi data ini");
        } else if (catetan.matches("")) {
            mCatetan.setError("isi data ini");
        } else if (namaPic.matches("")) {
            mNamaPic.setError("isi data ini");
        } else if (tlpPic.matches("")) {
            mTlpPic.setError("isi data ini");
        } else if (provinsi.matches("")) {
            mProvinsi.setError("isi data ini");
        } else if (kabupaten.matches("")) {
            mKabupaten.setError("isi data ini");
        } else if (hub.matches("")) {
            TextView errorText = (TextView) mSpinerHub.getSelectedView();
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("isi data ini");//changes the selected item text to this
        } else if (jarkom.matches("")) {
            mJarkom.setError("isi data ini");
        } else if (satelit.matches("")) {
            mSatelite.setError("isi data ini");
        } else if (latitude.matches("")) {
            mLatitude.setError("isi data ini");
        } else if (longtitude.matches("")) {
            mLongitude.setError("isi data ini");
        } else {
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                ResultWS = jsonResponse.getString("Result");

                                if (ResultWS.equals("True")) {

                                    String Data1 = jsonResponse.getString("Data1");
                                    Log.i("TAG", "onResponse: " + Data1);
                                    Toasty.success(DataLokasiActivity.this, "Success", Toast.LENGTH_SHORT, true).show();
                                    Log.i("TAG", "Ski data from server - ok" + response);
                                    Intent intent = new Intent();
                                    intent.putExtra(SharedPrefManager.SP_ID, id);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                                Log.i("TAG", "Ski data from server - ok" + response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(DataLokasiActivity.this, "Failed!!!", Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("TAG", "Ski error connect - " + error);
                            Toasty.error(DataLokasiActivity.this, "Failed!!!", Toast.LENGTH_SHORT, true).show();
                        }
                    }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Header", "Dota2");
                    return headers;
                }

                @Override
                public byte[] getBody() {
//                    String str1 = "{\"Result\":\"True\",\"Raw\":[{\"PARAM1\": [{\"WhereDatabaseinYou\":\"NoListTask=\'" + id + "\'\",\"KANWIL\":\"" + kanwil + "\",\"KANCAINDUK\":\"" + area + "\",\"NAMAREMOTE\":\"" + namaRrmote + "\",\"ALAMAT\":\"" + alamatInstall + "\",\"Provinsi\":\"" + provinsi + "\",\"KOTA\":\"" + kabupaten + "\",\"IdJarkom\":\"" + jarkom + "\",\"IdSatelite\":\"" + satelit + "\",\"PIC\":\"" + namaPic + "\",\"NoHpPic\":\"" + tlpPic + "\",\"Hub\":\"" + hub + "\",\"Latitude\":\"" + latitude + "\",\"Longitude\":\"" + longtitude + "\",\"AlamatSekarang\":\"" + alamatSekarang + "\",\"Catatan\":\"" + catetan + "\",\"FlagDataLokasi\":\"" + statusDl + "\"}],\"PARAM2\": [{\"WhereDatabaseinYou\":\"VID=\'" + vid + "\'\",\"KANWIL\":\"" + kanwil + "\",\"KANCAINDUK\":\"" + area + "\",\"NAMAREMOTE\":\"" + namaRrmote + "\",\"AlamatInstall\":\"" + alamatInstall + "\",\"PROVINSI\":\"" + provinsi + "\",\"KOTA\":\"" + kabupaten + "\",\"IdJarkom\":\"" + jarkom + "\",\"IdSatelite\":\"" + satelit + "\",\"CustPIC\":\"" + namaPic + "\",\"CustPIC_Phone\":\"" + tlpPic + "\",\"Hub\":\"" + hub + "\",\"Latitude\":\"" + latitude + "\",\"Longitude\":\"" + longtitude + "\"}],\"Data1\":\"\",\"Data2\":\"\",\"Data3\":\"\",\"Data4\":\"\",\"Data5\":\"\",\"Data6\":\"\",\"Data7\":\"\",\"Data8\":\"\",\"Data9\":\"\",\"Data10\":\"\"}]}";
                    String str1 = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [ {\"WhereDatabaseinYou\": \"NoListTask = '"+id+"'\",\"KANWIL\": \""+kanwil+"\",\"KANCAINDUK\":\"" + area + "\",\"ALAMAT\": \""+alamatInstall+"\",\"PROVINSI\": \""+provinsi+"\",\"KOTA\": \""+kabupaten+"\",\"IdJarkom\": \""+jarkom+"\",\"IdSatelite\": \""+satelit+"\",\"PIC\": \""+namaPic+"\",\"NoHpPic\": \""+tlpPic+"\",\"Hub\": \""+hub+"\",\"Latitude\": \""+latitude+"\",\"Longitude\": \""+longtitude+"\",\"AlamatSekarang\": \""+alamatSekarang+"\",\"Catatan\": \""+catetan+"\",\"FlagDataLokasi\": "+statusDl+"}],\"PARAM2\": [{\"WhereDatabaseinYou\": \"VID = '"+vid+"'\",\"KANWIL\": \""+kanwil+"\",\"KANCAINDUK\": \""+area+"\",\"NAMAREMOTE\": \""+namaRrmote+"\",\"AlamatInstall\": \""+alamatInstall+"\",\"PROVINSI\": \""+provinsi+"\",\"KOTA\": \""+kabupaten+"\",\"IdJarkom\": \""+jarkom+"\",\"IdSatelite\": \""+satelit+"\",\"PIC\": \""+namaPic+"\",\"CustPIC\": \""+namaPic+"\",\"CustPIC_Phone\": \""+tlpPic+"\",\"Hub\": \""+hub+"\",\"Latitude\": \""+latitude+"\",\"Longitude\": \""+longtitude+"\"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                    Log.i("sendBody", "sendBody: " + str1);
                    return str1.getBytes();
                }

                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=utf-8";
                }
            };

            Mysingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

        }


        }

}
