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
import android.widget.Button;
import android.widget.EditText;
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
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class DataSurveyActivity extends AppCompatActivity {

    private static final String TAG = "DataSurveyActivity";

    private EditText mAlamatPengiriman, mTempatPenyimpanan, mNamaPic, mKontakPic, mGrounding, mUkuranAntena, mTempatAntena, mKekuatanRoof, mMountingAntena, mLatitude, mLongitude, mPengukuranListrik, mPendukungACIndoor, mPendukungUPS, mPanjangKabel, mArahAntena, mHasilSurvey, mKeterangan;
    private Spinner mSpinnerTypeKabel;
    private Button mSaveBtn;
    ProgressDialog progressDialog;

    Boolean statusSV;
    String ResultWS, id, vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_survey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Data Survey");

        progressDialog = new ProgressDialog(this);
        Bundle in = getIntent().getExtras();
        if (in!=null) {
            id = in.getString(SharedPrefManager.SP_ID);
            vid = in.getString(SharedPrefManager.SP_VID);
        }

        mSpinnerTypeKabel = findViewById(R.id.type_kabel);
        mAlamatPengiriman = findViewById(R.id.alamat_pengiriman);
        mTempatPenyimpanan = findViewById(R.id.tempat_penyimpanan);
        mNamaPic = findViewById(R.id.nama_pic);
        mKontakPic = findViewById(R.id.kontak_pic);
        mGrounding = findViewById(R.id.penempatan_grounding);
        mUkuranAntena = findViewById(R.id.ukuran_antena);
        mTempatAntena = findViewById(R.id.tempat_antena);
        mKekuatanRoof = findViewById(R.id.kekuatan_roof);
        mMountingAntena = findViewById(R.id.jenis_mounting_antena);
        mLatitude = findViewById(R.id.latitude);
        mLongitude = findViewById(R.id.longitude);
        mPengukuranListrik = findViewById(R.id.pengukuran_listrik_awal);
        mPendukungACIndoor = findViewById(R.id.sarana_pendukung_ac_indoor);
        mPendukungUPS = findViewById(R.id.sarana_pendukung_ups);
        mPanjangKabel = findViewById(R.id.panjang_kabel);
        mArahAntena = findViewById(R.id.arah_antena);
        mHasilSurvey = findViewById(R.id.hasil_survey);
        mKeterangan = findViewById(R.id.keterangan);
        mSaveBtn = findViewById(R.id.btnUpdateSurvey);

        new AsyingDataSurvey().execute();

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataSurvey();
            }
        });
    }


    private class AsyingDataSurvey extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getDataSurvey();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    public void getDataSurvey(){
        progressDialog.show();
        String url = BaseUrl.getPublicIp + BaseUrl.detailTask+id;
        Log.i(TAG, "NoTask DataSurvey: " + id);
        Log.i(TAG, "Url DataSurvey: " + url);
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "getDataSurvey Url: " + url);
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

                                            String statusDataSurvey = data.getString("IdStatusPerbaikan");
                                            String statusSurvey = data.getString("FlagDataSurvey");
                                            Log.i("TAG", "run: "+ statusSurvey);

                                            Log.i("TAG", "status : "+statusDataSurvey);
                                            if (statusDataSurvey.equals("1")) {
                                                Log.i(TAG, "TaskList: "+data);
                                                Log.i(TAG, "data_Survey_Open: " + data.getString("AlamatPengirimanSurvey"));
                                                Log.i(TAG, "data_Survey_Open: " + data.getString("TempatPenyimpananSurvey"));
                                                Log.i(TAG, "data_Survey_Open: " + data.getString("NamaPICSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("KontakPICSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("PenempatanGroundingSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("UkuranAntenaSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("TempatAntenaSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("KekuatanRoofSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("JenisMountingSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("LatitudeSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("LongitudeSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("ListrikAwalSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("SarpenACIndoorSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("SarpenUPSSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("PanjangKabelSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("TypeKabelSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("ArahAntenaSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("StatusHasilSurvey"));
                                                Log.i(TAG, "data_Survey_Open: "+ data.getString("KeteranganSurvey"));

                                                if (data.getString("AlamatPengirimanSurvey").equals("null") || data.getString("AlamatPengirimanSurvey").equals(null)|| data.getString("AlamatPengirimanSurvey").equals("")){
                                                    String alamatPengiriman = "";
                                                    mAlamatPengiriman.setText(alamatPengiriman);
                                                }else {
                                                    String alamatPengiriman = data.getString("AlamatPengirimanSurvey");
                                                    mAlamatPengiriman.setText(alamatPengiriman);
                                                }
                                                if (data.getString("TempatPenyimpananSurvey").equals("null") || data.getString("TempatPenyimpananSurvey").equals(null)|| data.getString("TempatPenyimpananSurvey").equals("")){
                                                    String tempatPenyimpanan = "";
                                                    mTempatPenyimpanan.setText(tempatPenyimpanan);
                                                }else {
                                                    String tempatPenyimpanan = data.getString("TempatPenyimpananSurvey");
                                                    mTempatPenyimpanan.setText(tempatPenyimpanan);
                                                }
                                                if (data.getString("NamaPICSurvey").equals("null") || data.getString("NamaPICSurvey").equals(null)|| data.getString("NamaPICSurvey").equals("")){
                                                    String namaPIC = "";
                                                    mNamaPic.setText(namaPIC);
                                                }else {
                                                    String namaPIC = data.getString("NamaPICSurvey");
                                                    mNamaPic.setText(namaPIC);
                                                }
                                                if (data.getString("KontakPICSurvey").equals("null") || data.getString("KontakPICSurvey").equals(null)|| data.getString("KontakPICSurvey").equals("")){
                                                    String kontakPIC = "";
                                                    mKontakPic.setText(kontakPIC);
                                                }else {
                                                    String kontakPIC = data.getString("KontakPICSurvey");
                                                    mKontakPic.setText(kontakPIC);
                                                }
                                                if (data.getString("PenempatanGroundingSurvey").equals("null") || data.getString("PenempatanGroundingSurvey").equals(null)|| data.getString("PenempatanGroundingSurvey").equals("")){
                                                    String penempatanGrounding = "";
                                                    mGrounding.setText(penempatanGrounding);
                                                }else {
                                                    String penempatanGrounding = data.getString("PenempatanGroundingSurvey");
                                                    mGrounding.setText(penempatanGrounding);
                                                }
                                                if (data.getString("UkuranAntenaSurvey").equals("null") || data.getString("UkuranAntenaSurvey").equals(null)|| data.getString("UkuranAntenaSurvey").equals("")){
                                                    String ukuranAntena = "";
                                                    mUkuranAntena.setText(ukuranAntena);
                                                }else {
                                                    String ukuranAntena = data.getString("UkuranAntenaSurvey");
                                                    mUkuranAntena.setText(ukuranAntena);
                                                }
                                                if (data.getString("TempatAntenaSurvey").equals("null") || data.getString("TempatAntenaSurvey").equals(null)|| data.getString("TempatAntenaSurvey").equals("")){
                                                    String ukuranAntena = "";
                                                    mTempatAntena.setText(ukuranAntena);
                                                }else {
                                                    String ukuranAntena = data.getString("TempatAntenaSurvey");
                                                    mTempatAntena.setText(ukuranAntena);
                                                }
                                                if (data.getString("KekuatanRoofSurvey").equals("null") || data.getString("KekuatanRoofSurvey").equals(null)|| data.getString("KekuatanRoofSurvey").equals("")){
                                                    String kekuatanRoof = "";
                                                    mKekuatanRoof.setText(kekuatanRoof);
                                                }else {
                                                    String kekuatanRoof = data.getString("KekuatanRoofSurvey");
                                                    mKekuatanRoof.setText(kekuatanRoof);
                                                }
                                                if (data.getString("JenisMountingSurvey").equals("null") || data.getString("JenisMountingSurvey").equals(null)|| data.getString("JenisMountingSurvey").equals("")){
                                                    String jenisMounting = "";
                                                    mMountingAntena.setText(jenisMounting);
                                                }else {
                                                    String jenisMounting = data.getString("JenisMountingSurvey");
                                                    mMountingAntena.setText(jenisMounting);
                                                }
                                                if (data.getString("LatitudeSurvey").equals("null") || data.getString("LatitudeSurvey").equals(null)|| data.getString("LatitudeSurvey").equals("")){
                                                    String latitude = "";
                                                    mLatitude.setText(latitude);
                                                }else {
                                                    String latitude = data.getString("LatitudeSurvey");
                                                    mLatitude.setText(latitude);
                                                }
                                                if (data.getString("LongitudeSurvey").equals("null") || data.getString("LongitudeSurvey").equals(null)|| data.getString("LongitudeSurvey").equals("")){
                                                    String longtitude = "";
                                                    mLongitude.setText(longtitude);
                                                }else {
                                                    String longtitude = data.getString("LongitudeSurvey");
                                                    mLongitude.setText(longtitude);
                                                }
                                                if (data.getString("ListrikAwalSurvey").equals("null") || data.getString("ListrikAwalSurvey").equals(null)|| data.getString("ListrikAwalSurvey").equals("")){
                                                    String listrikAwal = "";
                                                    mPengukuranListrik.setText(listrikAwal);
                                                }else {
                                                    String listrikAwal = data.getString("ListrikAwalSurvey");
                                                    mPengukuranListrik.setText(listrikAwal);
                                                }
                                                if (data.getString("SarpenACIndoorSurvey").equals("null") || data.getString("SarpenACIndoorSurvey").equals(null)|| data.getString("SarpenACIndoorSurvey").equals("")){
                                                    String sarpenACIndoor = "";
                                                    mPendukungACIndoor.setText(sarpenACIndoor);
                                                }else {
                                                    String sarpenACIndoor = data.getString("SarpenACIndoorSurvey");
                                                    mPendukungACIndoor.setText(sarpenACIndoor);
                                                }
                                                if (data.getString("SarpenUPSSurvey").equals("null") || data.getString("SarpenUPSSurvey").equals(null)|| data.getString("SarpenUPSSurvey").equals("")){
                                                    String sarpenUPS = "";
                                                    mPendukungUPS.setText(sarpenUPS);
                                                }else {
                                                    String sarpenUPS = data.getString("SarpenUPSSurvey");
                                                    mPendukungUPS.setText(sarpenUPS);
                                                }
                                                if (data.getString("PanjangKabelSurvey").equals("null") || data.getString("PanjangKabelSurvey").equals(null)|| data.getString("PanjangKabelSurvey").equals("")){
                                                    String panjangKabel = "";
                                                    mPanjangKabel.setText(panjangKabel);
                                                }else {
                                                    String panjangKabel = data.getString("PanjangKabelSurvey");
                                                    mPanjangKabel.setText(panjangKabel);
                                                }
                                                if (data.getString("ArahAntenaSurvey").equals("null") || data.getString("ArahAntenaSurvey").equals(null)|| data.getString("ArahAntenaSurvey").equals("")){
                                                    String arahAntena = "";
                                                    mArahAntena.setText(arahAntena);
                                                }else {
                                                    String arahAntena = data.getString("ArahAntenaSurvey");
                                                    mArahAntena.setText(arahAntena);
                                                }
                                                if (data.getString("StatusHasilSurvey").equals("null") || data.getString("StatusHasilSurvey").equals(null)|| data.getString("StatusHasilSurvey").equals("")){
                                                    String statusHasil = "";
                                                    mHasilSurvey.setText(statusHasil);
                                                }else {
                                                    String statusHasil = data.getString("StatusHasilSurvey");
                                                    mHasilSurvey.setText(statusHasil);
                                                }
                                                if (data.getString("KeteranganSurvey").equals("null") || data.getString("KeteranganSurvey").equals(null)|| data.getString("KeteranganSurvey").equals("")){
                                                    String keterangan = "";
                                                    mKeterangan.setText(keterangan);
                                                }else {
                                                    String keterangan = data.getString("KeteranganSurvey");
                                                    mKeterangan.setText(keterangan);
                                                }
                                                if (data.getString("TypeKabelSurvey").equals("null") || data.getString("TypeKabelSurvey").equals(null)|| data.getString("TypeKabelSurvey").equals("")){
                                                    String typeKabel = "";
                                                    TextView getStr = (TextView) mSpinnerTypeKabel.getSelectedView();
                                                    getStr.setText(typeKabel);//changes the selected item text to this
                                                }else {
                                                    String typeKabel = data.getString("TypeKabelSurvey");
                                                    TextView getStr = (TextView) mSpinnerTypeKabel.getSelectedView();
                                                    getStr.setText(typeKabel);//changes the selected item text to this
                                                }
                                            }

                                            if (statusDataSurvey.equals("4")) {
                                                Log.i(TAG, "TaskList: "+data);
                                                Log.i(TAG, "data_Survey_Finish: " + data.getString("AlamatPengirimanSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: " + data.getString("TempatPenyimpananSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: " + data.getString("NamaPICSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("KontakPICSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("PenempatanGroundingSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("UkuranAntenaSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("TempatAntenaSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("KekuatanRoofSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("JenisMountingSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("LatitudeSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("LongitudeSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("ListrikAwalSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("SarpenACIndoorSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("SarpenUPSSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("PanjangKabelSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("TypeKabelSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("ArahAntenaSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("StatusHasilSurvey"));
                                                Log.i(TAG, "data_Survey_Finish: "+ data.getString("KeteranganSurvey"));

                                                if (data.getString("NAMAREMOTE").equals("null") || data.getString("NAMAREMOTE").equals(null)|| data.getString("NAMAREMOTE").equals("")){
                                                    String alamatPengiriman = "";
                                                    mAlamatPengiriman.setText(alamatPengiriman);
                                                    mAlamatPengiriman.setEnabled(false);
                                                }else {
                                                    String alamatPengiriman = data.getString("AlamatPengirimanSurvey");
                                                    mAlamatPengiriman.setText(alamatPengiriman);
                                                    mAlamatPengiriman.setEnabled(false);
                                                }
                                                if (data.getString("TempatPenyimpananSurvey").equals("null") || data.getString("TempatPenyimpananSurvey").equals(null)|| data.getString("TempatPenyimpananSurvey").equals("")){
                                                    String tempatPenyimpanan = "";
                                                    mTempatPenyimpanan.setText(tempatPenyimpanan);
                                                    mTempatPenyimpanan.setEnabled(false);
                                                }else {
                                                    String tempatPenyimpanan = data.getString("TempatPenyimpananSurvey");
                                                    mTempatPenyimpanan.setText(tempatPenyimpanan);
                                                    mTempatPenyimpanan.setEnabled(false);
                                                }
                                                if (data.getString("NamaPICSurvey").equals("null") || data.getString("NamaPICSurvey").equals(null)|| data.getString("NamaPICSurvey").equals("")){
                                                    String namaPIC = "";
                                                    mNamaPic.setText(namaPIC);
                                                    mNamaPic.setEnabled(false);
                                                }else {
                                                    String namaPIC = data.getString("NamaPICSurvey");
                                                    mNamaPic.setText(namaPIC);
                                                    mNamaPic.setEnabled(false);
                                                }
                                                if (data.getString("KontakPICSurvey").equals("null") || data.getString("KontakPICSurvey").equals(null)|| data.getString("KontakPICSurvey").equals("")){
                                                    String kontakPIC = "";
                                                    mKontakPic.setText(kontakPIC);
                                                    mKontakPic.setEnabled(false);
                                                }else {
                                                    String kontakPIC = data.getString("KontakPICSurvey");
                                                    mKontakPic.setText(kontakPIC);
                                                    mKontakPic.setEnabled(false);
                                                }
                                                if (data.getString("PenempatanGroundingSurvey").equals("null") || data.getString("PenempatanGroundingSurvey").equals(null)|| data.getString("PenempatanGroundingSurvey").equals("")){
                                                    String penempatanGrounding = "";
                                                    mGrounding.setText(penempatanGrounding);
                                                    mGrounding.setEnabled(false);
                                                }else {
                                                    String penempatanGrounding = data.getString("PenempatanGroundingSurvey");
                                                    mGrounding.setText(penempatanGrounding);
                                                    mGrounding.setEnabled(false);
                                                }
                                                if (data.getString("UkuranAntenaSurvey").equals("null") || data.getString("UkuranAntenaSurvey").equals(null)|| data.getString("UkuranAntenaSurvey").equals("")){
                                                    String ukuranAntena = "";
                                                    mUkuranAntena.setText(ukuranAntena);
                                                    mUkuranAntena.setEnabled(false);
                                                }else {
                                                    String ukuranAntena = data.getString("UkuranAntenaSurvey");
                                                    mUkuranAntena.setText(ukuranAntena);
                                                    mUkuranAntena.setEnabled(false);
                                                }
                                                if (data.getString("TempatAntenaSurvey").equals("null") || data.getString("TempatAntenaSurvey").equals(null)|| data.getString("TempatAntenaSurvey").equals("")){
                                                    String ukuranAntena = "";
                                                    mTempatAntena.setText(ukuranAntena);
                                                    mTempatAntena.setEnabled(false);
                                                }else {
                                                    String ukuranAntena = data.getString("TempatAntenaSurvey");
                                                    mTempatAntena.setText(ukuranAntena);
                                                    mTempatAntena.setEnabled(false);
                                                }
                                                if (data.getString("KekuatanRoofSurvey").equals("null") || data.getString("KekuatanRoofSurvey").equals(null)|| data.getString("KekuatanRoofSurvey").equals("")){
                                                    String kekuatanRoof = "";
                                                    mKekuatanRoof.setText(kekuatanRoof);
                                                    mKekuatanRoof.setEnabled(false);
                                                }else {
                                                    String kekuatanRoof = data.getString("KekuatanRoofSurvey");
                                                    mKekuatanRoof.setText(kekuatanRoof);
                                                    mKekuatanRoof.setEnabled(false);
                                                }
                                                if (data.getString("JenisMountingSurvey").equals("null") || data.getString("JenisMountingSurvey").equals(null)|| data.getString("JenisMountingSurvey").equals("")){
                                                    String jenisMounting = "";
                                                    mMountingAntena.setText(jenisMounting);
                                                    mMountingAntena.setEnabled(false);
                                                }else {
                                                    String jenisMounting = data.getString("JenisMountingSurvey");
                                                    mMountingAntena.setText(jenisMounting);
                                                    mMountingAntena.setEnabled(false);
                                                }
                                                if (data.getString("LatitudeSurvey").equals("null") || data.getString("LatitudeSurvey").equals(null)|| data.getString("LatitudeSurvey").equals("")){
                                                    String latitude = "";
                                                    mLatitude.setText(latitude);
                                                    mLatitude.setEnabled(false);
                                                }else {
                                                    String latitude = data.getString("LatitudeSurvey");
                                                    mLatitude.setText(latitude);
                                                    mLatitude.setEnabled(false);
                                                }
                                                if (data.getString("LongitudeSurvey").equals("null") || data.getString("LongitudeSurvey").equals(null)|| data.getString("LongitudeSurvey").equals("")){
                                                    String longtitude = "";
                                                    mLongitude.setText(longtitude);
                                                    mLongitude.setEnabled(false);
                                                }else {
                                                    String longtitude = data.getString("LongitudeSurvey");
                                                    mLongitude.setText(longtitude);
                                                    mLongitude.setEnabled(false);
                                                }
                                                if (data.getString("ListrikAwalSurvey").equals("null") || data.getString("ListrikAwalSurvey").equals(null)|| data.getString("ListrikAwalSurvey").equals("")){
                                                    String listrikAwal = "";
                                                    mPengukuranListrik.setText(listrikAwal);
                                                    mPengukuranListrik.setEnabled(false);
                                                }else {
                                                    String listrikAwal = data.getString("ListrikAwalSurvey");
                                                    mPengukuranListrik.setText(listrikAwal);
                                                    mPengukuranListrik.setEnabled(false);
                                                }
                                                if (data.getString("SarpenACIndoorSurvey").equals("null") || data.getString("SarpenACIndoorSurvey").equals(null)|| data.getString("SarpenACIndoorSurvey").equals("")){
                                                    String sarpenACIndoor = "";
                                                    mPendukungACIndoor.setText(sarpenACIndoor);
                                                    mPendukungACIndoor.setEnabled(false);
                                                }else {
                                                    String sarpenACIndoor = data.getString("SarpenACIndoorSurvey");
                                                    mPendukungACIndoor.setText(sarpenACIndoor);
                                                    mPendukungACIndoor.setEnabled(false);
                                                }
                                                if (data.getString("SarpenUPSSurvey").equals("null") || data.getString("SarpenUPSSurvey").equals(null)|| data.getString("SarpenUPSSurvey").equals("")){
                                                    String sarpenUPS = "";
                                                    mPendukungUPS.setText(sarpenUPS);
                                                    mPendukungUPS.setEnabled(false);
                                                }else {
                                                    String sarpenUPS = data.getString("SarpenUPSSurvey");
                                                    mPendukungUPS.setText(sarpenUPS);
                                                    mPendukungUPS.setEnabled(false);
                                                }
                                                if (data.getString("PanjangKabelSurvey").equals("null") || data.getString("PanjangKabelSurvey").equals(null)|| data.getString("PanjangKabelSurvey").equals("")){
                                                    String panjangKabel = "";
                                                    mPanjangKabel.setText(panjangKabel);
                                                    mPanjangKabel.setEnabled(false);
                                                }else {
                                                    String panjangKabel = data.getString("PanjangKabelSurvey");
                                                    mPanjangKabel.setText(panjangKabel);
                                                    mPanjangKabel.setEnabled(false);
                                                }
                                                if (data.getString("ArahAntenaSurvey").equals("null") || data.getString("ArahAntenaSurvey").equals(null)|| data.getString("ArahAntenaSurvey").equals("")){
                                                    String arahAntena = "";
                                                    mArahAntena.setText(arahAntena);
                                                    mArahAntena.setEnabled(false);
                                                }else {
                                                    String arahAntena = data.getString("ArahAntenaSurvey");
                                                    mArahAntena.setText(arahAntena);
                                                    mArahAntena.setEnabled(false);
                                                }
                                                if (data.getString("StatusHasilSurvey").equals("null") || data.getString("StatusHasilSurvey").equals(null)|| data.getString("StatusHasilSurvey").equals("")){
                                                    String statusHasil = "";
                                                    mHasilSurvey.setText(statusHasil);
                                                    mHasilSurvey.setEnabled(false);
                                                }else {
                                                    String statusHasil = data.getString("StatusHasilSurvey");
                                                    mHasilSurvey.setText(statusHasil);
                                                    mHasilSurvey.setEnabled(false);
                                                }
                                                if (data.getString("KeteranganSurvey").equals("null") || data.getString("KeteranganSurvey").equals(null)|| data.getString("KeteranganSurvey").equals("")){
                                                    String keterangan = "";
                                                    mKeterangan.setText(keterangan);
                                                    mKeterangan.setEnabled(false);
                                                }else {
                                                    String keterangan = data.getString("KeteranganSurvey");
                                                    mKeterangan.setText(keterangan);
                                                    mKeterangan.setEnabled(false);
                                                }
                                                if (data.getString("TypeKabelSurvey").equals("null") || data.getString("TypeKabelSurvey").equals(null)|| data.getString("TypeKabelSurvey").equals("")){
                                                    String typeKabel = "";
                                                    TextView getStr = (TextView) mSpinnerTypeKabel.getSelectedView();
                                                    getStr.setText(typeKabel);//changes the selected item text to this
                                                    getStr.setEnabled(false);
                                                }else {
                                                    String typeKabel = data.getString("TypeKabelSurvey");
                                                    TextView getStr = (TextView) mSpinnerTypeKabel.getSelectedView();
                                                    getStr.setText(typeKabel);//changes the selected item text to this
                                                    getStr.setEnabled(false);
                                                }

                                                mSaveBtn.setVisibility(View.GONE);

                                            }
                                            progressDialog.dismiss();
                                        }

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

    private void updateDataSurvey() {
        String url = BaseUrl.getPublicIp + BaseUrl.updateDataSurvey;
        Log.i("url", "Data Lokasi: " + url);
        Log.i("GetVID", "updateDataLokasi: " + id);
        String alamatPengiriman = mAlamatPengiriman.getText().toString();
        String tempatPenyimpanan = mTempatPenyimpanan.getText().toString();
        String namaPic = mNamaPic.getText().toString();
        String kontakPic = mKontakPic.getText().toString();
        String grounding = mGrounding.getText().toString();
        String ukuranAntena = mUkuranAntena.getText().toString();
        String tempatAntena = mTempatAntena.getText().toString();
        String kekuatanRoof = mKekuatanRoof.getText().toString();
        String mountingAntena = mMountingAntena.getText().toString();
        String latitude = mLatitude.getText().toString();
        String longitude = mLongitude.getText().toString();
        String pengukuranListrik = mPengukuranListrik.getText().toString();
        String pendukungACIndoor = mPendukungACIndoor.getText().toString();
        String pendukungUPS = mPendukungUPS.getText().toString();
        String panjangKabel = mPanjangKabel.getText().toString();
        String arahAntena = mArahAntena.getText().toString();
        String hasilSurvey = mHasilSurvey.getText().toString();
        String keterangan = mKeterangan.getText().toString();
        String spinnerTypeKabel = mSpinnerTypeKabel.getSelectedItem().toString();
        Boolean statusSv = statusSV = true;
        if (alamatPengiriman.matches("")) {
            mAlamatPengiriman.setError("isi data ini");
        } else if (tempatPenyimpanan.matches("")) {
            mTempatPenyimpanan.setError("isi data ini");
        } else if (namaPic.matches("")) {
            mNamaPic.setError("isi data ini");
        } else if (kontakPic.matches("")) {
            mKontakPic.setError("isi data ini");
        } else if (grounding.matches("")) {
            mGrounding.setError("isi data ini");
        } else if (ukuranAntena.matches("")) {
            mUkuranAntena.setError("isi data ini");
        } else if (tempatAntena.matches("")) {
            mTempatAntena.setError("isi data ini");
        } else if (kekuatanRoof.matches("")) {
            mKekuatanRoof.setError("isi data ini");
        } else if (mountingAntena.matches("")) {
            mMountingAntena.setError("isi data ini");
        } else if (latitude.matches("")) {
            mLatitude.setError("isi data ini");
        } else if (spinnerTypeKabel.matches("")) {
            TextView errorText = (TextView) mSpinnerTypeKabel.getSelectedView();
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("isi data ini");//changes the selected item text to this
        } else if (longitude.matches("")) {
            mLongitude.setError("isi data ini");
        } else if (pengukuranListrik.matches("")) {
            mPengukuranListrik.setError("isi data ini");
        } else if (pendukungACIndoor.matches("")) {
            mPendukungACIndoor.setError("isi data ini");
        } else if (pendukungUPS.matches("")) {
            mPendukungUPS.setError("isi data ini");
        } else if (panjangKabel.matches("")) {
            mPanjangKabel.setError("isi data ini");
        } else if (arahAntena.matches("")) {
            mArahAntena.setError("isi data ini");
        } else if (hasilSurvey.matches("")) {
            mHasilSurvey.setError("isi data ini");
        } else if (keterangan.matches("")) {
            mKeterangan.setError("isi data ini");
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
                                    Toasty.success(DataSurveyActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                    Log.i("TAG", "Ski data from server - ok" + response);
                                    Intent intent = new Intent();
                                    intent.putExtra(SharedPrefManager.SP_ID, id);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                                Log.i("TAG", "Ski data from server - ok" + response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(DataSurveyActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("TAG", "Ski error connect - " + error);
                            Toasty.error(DataSurveyActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
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
                    String str1 = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"NoListTask = '"+id+"'\",\"AlamatPengirimanSurvey\":\""+alamatPengiriman+"\",\"TempatPenyimpananSurvey\":\""+tempatPenyimpanan+"\",\"NamaPICSurvey\":\""+namaPic+"\",\"KontakPICSurvey\":\""+kontakPic+"\",\"PenempatanGroundingSurvey\":\""+grounding+"\",\"UkuranAntenaSurvey\":\""+ukuranAntena+"\",\"TempatAntenaSurvey\":\""+tempatAntena+"\",\"KekuatanRoofSurvey\":\""+kekuatanRoof+"\",\"JenisMountingSurvey\":\""+mountingAntena+"\",\"LatitudeSurvey\":\""+latitude+"\",\"LongitudeSurvey\": \""+longitude+"\",\"ListrikAwalSurvey\":\""+pengukuranListrik+"\",\"SarpenACIndoorSurvey\":\""+pendukungACIndoor+"\",\"SarpenUPSSurvey\":\""+pendukungUPS+"\",\"PanjangKabelSurvey\":\""+panjangKabel+"\",\"TypeKabelSurvey\":\""+spinnerTypeKabel+"\",\"ArahAntenaSurvey\":\""+arahAntena+"\",\"KeteranganSurvey\":\""+keterangan+"\",\"StatusHasilSurvey\":\""+hasilSurvey+"\",\"FlagDataSurvey\": "+statusSv+"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
