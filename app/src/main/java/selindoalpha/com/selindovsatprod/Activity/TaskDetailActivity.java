package selindoalpha.com.selindovsatprod.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "TaskDetailActivity";
    private static final int TAGDataLokas = 1;
    private static final int TAGGeneralInfo = 2;
    private static final int TAGDataTeknisi = 3;
    private static final int TAGDataBarang = 4;
    private static final int TAGUploadFoto = 5;
    private static final int TAGInstalasi = 6;
    private static final int TAGSurvay = 7;
    String jsonStr,ResultWS, statusTask;
//    Boolean statusDL, statusGI, statusDT, statusDB, statusUP, statusIL, statusSV;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;
    TextView mNamaRemote, mSid, mTlpRemote, mNamaPic, mTlpPic, mAlamat;
    TextView statusDataLokasi, statusGeneralInfo, statusDataTeknisi, statusDataBarang, statusUploadPhoto, statusSurvey, statusInstalasi;

    CardView mCardView1, mCardView2;
    RelativeLayout mGeneralinfo, mDataTeknisi, mDataBarang, mDataSurvey, mDataInstalasi, mUploadPhoto;
    Button mKonfirmasiFinish;
    ProgressBar mProgress;

    View viewDataBarang, viewDataInstalasi, viewDataSurvey, viewDataTeknisi, viewUploadPhoto;
    String id, vid, noTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("DETAIL TASK");
        Bundle in = getIntent().getExtras();
        if (in!=null) {
            id = in.getString(SharedPrefManager.SP_ID);
            vid = in.getString(SharedPrefDataTask.VID);
            noTask = in.getString(SharedPrefDataTask.NoTask);
        }
        Log.i(TAG, "id: "+id);
        Log.i(TAG, "vid: "+vid);
        Log.i(TAG, "noTask: "+noTask);

        mNamaRemote = findViewById(R.id.tvNamaRemote);
        mSid = findViewById(R.id.tvSid);
        mTlpRemote = findViewById(R.id.tvNoTlpRemote);
        mNamaPic = findViewById(R.id.tvNamaPic);
        mTlpPic = findViewById(R.id.tvNoPic);
        mAlamat = findViewById(R.id.tvAlamat);
        statusDataLokasi = findViewById(R.id.status_data_lokasi);
        statusGeneralInfo = findViewById(R.id.status_general_info);
        statusDataTeknisi = findViewById(R.id.status_data_teknisi);
        statusDataBarang = findViewById(R.id.status_data_barang);
        statusUploadPhoto = findViewById(R.id.status_upload_photo);
        statusSurvey = findViewById(R.id.status_survey);
        statusInstalasi = findViewById(R.id.status_installasi);
        mProgress = findViewById(R.id.progress_detail_task);
        mCardView1 = findViewById(R.id.cvLayout);
        mCardView2 = findViewById(R.id.list_item);

        mGeneralinfo = findViewById(R.id.rvGeneralInfo);
        mDataTeknisi = findViewById(R.id.rvDataTeknis);
        mDataBarang = findViewById(R.id.rvDataTerpasang);
        mDataSurvey = findViewById(R.id.rvSurvey);
        mDataInstalasi = findViewById(R.id.rvInstallasi);
        mUploadPhoto = findViewById(R.id.rvUploadPhoto);

        viewDataSurvey = findViewById(R.id.viewDataSurvey);
        viewDataInstalasi = findViewById(R.id.viewDataInstall);
        viewDataBarang = findViewById(R.id.viewDataBarang);
        viewDataTeknisi = findViewById(R.id.viewDataTeknisi);
        viewUploadPhoto = findViewById(R.id.viewUploadPhoto);

        mKonfirmasiFinish = findViewById(R.id.btnKonfirmasi);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);

        mKonfirmasiFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadein = new AlphaAnimation(0,1);
                fadein.setDuration(50);
                Button konfirm = findViewById(R.id.btnKonfirmasi);
                konfirm.startAnimation(fadein);
                verifikasiTask();
            }
        });

        new AsyingDetaikTask().execute();


    }



    private class AsyingDetaikTask extends AsyncTask<Void, Void, String> {

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
            return "OK";
        }
    }

    public void getData(){
        mProgress.setVisibility(View.VISIBLE);
        mCardView1.setVisibility(View.GONE);
        mCardView2.setVisibility(View.GONE);
        mKonfirmasiFinish.setVisibility(View.GONE);

        Log.i(TAG, "idTest: " + id);
//        String id = sharedPrefManager.getId();
        String url = BaseUrl.getPublicIp + BaseUrl.detailTask+id;
        Log.i(TAG, "Url Detail Task: " + url);
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

                                            statusTask = data.getString("IdStatusPerbaikan");
                                            Log.i(TAG, "IdStatusPerbaikan : " +statusTask);
                                            String statusDL = data.getString("FlagDataLokasi");
                                            String statusGI = data.getString("FlagGeneralInfo");
                                            String statusDT = data.getString("FlagDataTeknis");
                                            String statusDB = data.getString("FlagDataBarang");
                                            String statusUP = data.getString("FlagUploadPhoto");
                                            String statusSV = data.getString("FlagDataSurvey");
                                            String statusIL = data.getString("FlagDataInstallasi");

                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.statusDataLokasi, statusDL);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.statusGeneralInfo, statusGI);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.statusDataTeknis, statusDT);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.statusDataBarang, statusDB);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.statusUploadPhoto, statusUP);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.statusDataSurvey, statusSV);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.statusDataInstallasi, statusIL);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.StatusTask, statusTask);

                                            Log.i(TAG, "saveSPBoolean DataLokasi: " + sharedPrefDataTask.getStatusDataLokasi());
                                            Log.i(TAG, "saveSPBoolean GeneralInfo: " + sharedPrefDataTask.getStatusGeneralInfo());
                                            Log.i(TAG, "saveSPBoolean DataBarang: " + sharedPrefDataTask.getStatusDataBarang());
                                            Log.i(TAG, "saveSPBoolean DataTeknisi: " + sharedPrefDataTask.getStatusDataTeknis());
                                            Log.i(TAG, "saveSPBoolean StatusUpload: " + sharedPrefDataTask.getStatusUploadPhoto());
                                            Log.i(TAG, "saveSPBoolean DataSurvey: " + sharedPrefDataTask.getStatusDataSurvey());
                                            Log.i(TAG, "saveSPBoolean DataInstall: " + sharedPrefDataTask.getStatusDataInstallasi());
                                            Log.i(TAG, "saveSPBoolean StatusTaskOPEN/FINISH: " + sharedPrefDataTask.getStatusTask());

                                            String statusJenisTask = data.getString("idJenisTask1");
                                            String alamatSekarang = data.getString("ALAMAT");


                                            String vid = data.getString("VID1");
                                            String remote = data.getString("NAMAREMOTE");
                                            String alamatInstall = data.getString("ALAMAT");
                                            String kanwil = data.getString("KANWIL");
                                            String area = data.getString("KANCAINDUK");
                                            String pic = data.getString("NoHpPic");
                                            String noPic = data.getString("PIC");
                                            String provinsi = data.getString("Provinsi");
                                            String kota = data.getString("KOTA");
                                            String jarkom = data.getString("IdJarkom");
                                            String satelite = data.getString("IdSatelite");
                                            String latitude = data.getString("Latitude");
                                            String longtitude = data.getString("Longitude");
                                            String noTask = data.getString("NoTask");
                                            String catetan = data.getString("Catatan");
                                            String hub = data.getString("Hub");

                                            sharedPrefManager.saveSPString(SharedPrefManager.SP_VID, vid);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.CustPIC, pic);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.CustPIC_Phone, noPic);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.PROVINSI, provinsi);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.KOTA, kota);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.KANWIL, kanwil);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.KANCAINDUK, area);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.NAMAREMOTE, remote);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.AlamatInstall, alamatInstall);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.IdJarkom, jarkom);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.IdSatelite, satelite);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.Hub, hub);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.Latitude, latitude);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.Longitude, longtitude);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.AlamatSekarang, alamatSekarang);
                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.Catatan, catetan);


                                            sharedPrefDataTask.saveSPString(SharedPrefDataTask.NoTask, noTask);

                                            Log.i(TAG, "FlagDataLokasi: " + statusDL);
                                            Log.i(TAG, "FlagGeneralInfo: " + statusGI);
                                            Log.i(TAG, "FlagDataTeknis: " + statusDT);
                                            Log.i(TAG, "FlagDataBarang: " + statusDB);
                                            Log.i(TAG, "FlagUploadPhoto: " + statusUP);
                                            Log.i(TAG, "idJenisTask: " + statusJenisTask);


                                            if (statusJenisTask.equals("Installation")){
                                                if (statusDL.equals("false") || statusDL.equals("null") || statusDL==null){
                                                    statusDataLokasi.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusDataLokasi.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusGI.equals("false") || statusGI.equals("null") || statusGI==null){
                                                    statusGeneralInfo.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusGeneralInfo.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusDT.equals("false") || statusDT.equals("null") || statusDT==null){
                                                    statusDataTeknisi.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusDataTeknisi.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusDB.equals("false") || statusDB.equals("null") || statusDB==null){
                                                    statusDataBarang.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusDataBarang.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusUP.equals("false")  || statusUP.equals("null") || statusUP==null){
                                                    statusUploadPhoto.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusUploadPhoto.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusIL.equals("false") || statusIL.equals("null") || statusIL==null){
                                                    statusInstalasi.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusInstalasi.setBackgroundResource(R.color.colorGreen500);
                                                }

                                                if (statusTask.equals("1")) {
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("NAMAREMOTE"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("ALAMAT"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("Provinsi"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("SID"));
                                                    String namaRemote = data.getString("NAMAREMOTE");
                                                    String id = data.getString("SID");
                                                    String namaPic = data.getString("PIC");
                                                    String tlpPic = data.getString("NoHpPic");
                                                    String alamat = data.getString("ALAMAT");
                                                    mNamaRemote.setText(namaRemote);
                                                    mSid.setText(id);
                                                    mNamaPic.setText(namaPic);
                                                    mTlpPic.setText(tlpPic);
                                                    mAlamat.setText(alamat);
                                                    mGeneralinfo.setVisibility(View.VISIBLE);
                                                    mDataTeknisi.setVisibility(View.VISIBLE);
                                                    mDataBarang.setVisibility(View.VISIBLE);
                                                    mDataInstalasi.setVisibility(View.VISIBLE);
                                                    viewDataSurvey.setVisibility(View.GONE);
                                                    mDataSurvey.setVisibility(View.GONE);
                                                    mUploadPhoto.setVisibility(View.VISIBLE);
                                                }
                                                if (statusTask.equals("4")) {
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("NAMAREMOTE"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("ALAMAT"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("Provinsi"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("SID"));

                                                    String namaRemote = data.getString("NAMAREMOTE");
                                                    String id = data.getString("SID");
                                                    String namaPic = data.getString("PIC");
                                                    String tlpPic = data.getString("NoHpPic");
                                                    String alamat = data.getString("ALAMAT");

                                                    statusInstalasi.setBackgroundResource(R.color.colorGreen500);
                                                    statusUploadPhoto.setBackgroundResource(R.color.colorGreen500);
                                                    statusDataBarang.setBackgroundResource(R.color.colorGreen500);
                                                    statusDataTeknisi.setBackgroundResource(R.color.colorGreen500);
                                                    statusDataLokasi.setBackgroundResource(R.color.colorGreen500);
                                                    statusGeneralInfo.setBackgroundResource(R.color.colorGreen500);



                                                    mNamaRemote.setText(namaRemote);
                                                    mSid.setText(id);
                                                    mNamaPic.setText(namaPic);
                                                    mTlpPic.setText(tlpPic);
                                                    mAlamat.setText(alamat);
                                                    mGeneralinfo.setVisibility(View.VISIBLE);
                                                    mDataTeknisi.setVisibility(View.VISIBLE);
                                                    mDataBarang.setVisibility(View.VISIBLE);
                                                    mDataInstalasi.setVisibility(View.VISIBLE);
                                                    viewDataSurvey.setVisibility(View.GONE);
                                                    mDataSurvey.setVisibility(View.GONE);
                                                    mUploadPhoto.setVisibility(View.VISIBLE);
                                                }



                                                mProgress.setVisibility(View.GONE);
                                                mCardView1.setVisibility(View.VISIBLE);
                                                mCardView2.setVisibility(View.VISIBLE);
                                                mKonfirmasiFinish.setVisibility(View.VISIBLE);
                                            }else if (statusJenisTask.equals("SiteSurvey")){
                                                if (statusGI.equals("false") || statusGI.equals("null") || statusGI==null){
                                                    statusGeneralInfo.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusGeneralInfo.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusDL.equals("false") || statusDL.equals("null") || statusDL==null){
                                                    statusDataLokasi.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusDataLokasi.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusUP.equals("false") || statusUP.equals("null") || statusUP==null){
                                                    statusUploadPhoto.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusUploadPhoto.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusSV.equals("false") || statusSV.equals("null") || statusSV==null){
                                                    statusSurvey.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusSurvey.setBackgroundResource(R.color.colorGreen500);
                                                }

                                                if (statusTask.equals("1")) {
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("NAMAREMOTE"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("ALAMAT"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("Provinsi"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("SID"));
                                                    String namaRemote = data.getString("NAMAREMOTE");
                                                    String id = data.getString("SID");
                                                    String namaPic = data.getString("PIC");
                                                    String tlpPic = data.getString("NoHpPic");
                                                    String alamat = data.getString("ALAMAT");
                                                    mNamaRemote.setText(namaRemote);
                                                    mSid.setText(id);
                                                    mNamaPic.setText(namaPic);
                                                    mTlpPic.setText(tlpPic);
                                                    mAlamat.setText(alamat);
                                                    mGeneralinfo.setVisibility(View.VISIBLE);
                                                    viewDataTeknisi.setVisibility(View.VISIBLE);
                                                    mDataTeknisi.setVisibility(View.GONE);
                                                    viewDataBarang.setVisibility(View.GONE);
                                                    mDataBarang.setVisibility(View.GONE);
                                                    viewDataInstalasi.setVisibility(View.GONE);
                                                    mDataInstalasi.setVisibility(View.GONE);
                                                    viewDataSurvey.setVisibility(View.GONE);
                                                    mDataSurvey.setVisibility(View.VISIBLE);
                                                    mUploadPhoto.setVisibility(View.VISIBLE);
                                                }
                                                if (statusTask.equals("4")) {
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("NAMAREMOTE"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("ALAMAT"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("Provinsi"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("SID"));

                                                    String namaRemote = data.getString("NAMAREMOTE");
                                                    String id = data.getString("SID");
                                                    String namaPic = data.getString("PIC");
                                                    String tlpPic = data.getString("NoHpPic");
                                                    String alamat = data.getString("ALAMAT");

                                                    statusDataLokasi.setBackgroundResource(R.color.colorGreen500);
                                                    statusUploadPhoto.setBackgroundResource(R.color.colorGreen500);
                                                    statusSurvey.setBackgroundResource(R.color.colorGreen500);

                                                    mNamaRemote.setText(namaRemote);
                                                    mSid.setText(id);
                                                    mNamaPic.setText(namaPic);
                                                    mTlpPic.setText(tlpPic);
                                                    mAlamat.setText(alamat);
                                                    mGeneralinfo.setVisibility(View.GONE);
                                                    viewDataTeknisi.setVisibility(View.GONE);
                                                    mDataTeknisi.setVisibility(View.GONE);
                                                    viewDataBarang.setVisibility(View.GONE);
                                                    mDataBarang.setVisibility(View.GONE);
                                                    viewDataInstalasi.setVisibility(View.GONE);
                                                    mDataInstalasi.setVisibility(View.GONE);
                                                    mDataSurvey.setVisibility(View.VISIBLE);
                                                    mUploadPhoto.setVisibility(View.VISIBLE);

                                                }

                                                mProgress.setVisibility(View.GONE);
                                                mCardView1.setVisibility(View.VISIBLE);
                                                mCardView2.setVisibility(View.VISIBLE);
                                                mKonfirmasiFinish.setVisibility(View.VISIBLE);
                                            }else {
                                                if (statusDL.equals("false") || statusDL.equals("null") || statusDL==null){
                                                    statusDataLokasi.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusDataLokasi.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusGI.equals("false") || statusGI.equals("null") || statusGI==null){
                                                    statusGeneralInfo.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusGeneralInfo.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusDT.equals("false") || statusDT.equals("null") || statusDT==null){
                                                    statusDataTeknisi.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusDataTeknisi.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusDB.equals("false") || statusDB.equals("null") || statusDB==null){
                                                    statusDataBarang.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusDataBarang.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusUP.equals("false") || statusUP.equals("null") || statusUP==null){
                                                    statusUploadPhoto.setBackgroundResource(R.color.super_hot);
                                                }else {
                                                    statusUploadPhoto.setBackgroundResource(R.color.colorGreen500);
                                                }
                                                if (statusTask.equals("1")) {
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("NAMAREMOTE"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("ALAMAT"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("Provinsi"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("SID"));
                                                    String namaRemote = data.getString("NAMAREMOTE");
                                                    String id = data.getString("SID");
                                                    String namaPic = data.getString("PIC");
                                                    String tlpPic = data.getString("NoHpPic");
                                                    String alamat = data.getString("ALAMAT");
                                                    mNamaRemote.setText(namaRemote);
                                                    mSid.setText(id);
                                                    mNamaPic.setText(namaPic);
                                                    mTlpPic.setText(tlpPic);
                                                    mAlamat.setText(alamat);

                                                    mGeneralinfo.setVisibility(View.VISIBLE);
                                                    mDataTeknisi.setVisibility(View.VISIBLE);
                                                    mDataBarang.setVisibility(View.VISIBLE);
                                                    viewDataInstalasi.setVisibility(View.GONE);
                                                    mDataInstalasi.setVisibility(View.GONE);
                                                    viewDataSurvey.setVisibility(View.GONE);
                                                    mDataSurvey.setVisibility(View.GONE);
                                                    mUploadPhoto.setVisibility(View.VISIBLE);
                                                }
                                                if (statusTask.equals("4")) {
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("NAMAREMOTE"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("ALAMAT"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("Provinsi"));
                                                    Log.i("data_lokasi", "data_lokasi: "+ data.getString("SID"));

                                                    String namaRemote = data.getString("NAMAREMOTE");
                                                    String id = data.getString("SID");
                                                    String namaPic = data.getString("PIC");
                                                    String tlpPic = data.getString("NoHpPic");
                                                    String alamat = data.getString("ALAMAT");

                                                    statusDataLokasi.setBackgroundResource(R.color.colorGreen500);
                                                    statusGeneralInfo.setBackgroundResource(R.color.colorGreen500);
                                                    statusDataTeknisi.setBackgroundResource(R.color.colorGreen500);
                                                    statusDataBarang.setBackgroundResource(R.color.colorGreen500);
                                                    statusUploadPhoto.setBackgroundResource(R.color.colorGreen500);

                                                    mNamaRemote.setText(namaRemote);
                                                    mSid.setText(id);
                                                    mNamaPic.setText(namaPic);
                                                    mTlpPic.setText(tlpPic);
                                                    mAlamat.setText(alamat);

                                                    mGeneralinfo.setVisibility(View.VISIBLE);
                                                    mDataTeknisi.setVisibility(View.VISIBLE);
                                                    mDataBarang.setVisibility(View.VISIBLE);
                                                    viewDataInstalasi.setVisibility(View.GONE);
                                                    mDataInstalasi.setVisibility(View.GONE);
                                                    viewDataSurvey.setVisibility(View.GONE);
                                                    mDataSurvey.setVisibility(View.GONE);
                                                    mUploadPhoto.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            mProgress.setVisibility(View.GONE);
                                            mCardView1.setVisibility(View.VISIBLE);
                                            mCardView2.setVisibility(View.VISIBLE);
                                            mKonfirmasiFinish.setVisibility(View.VISIBLE);

                                        }


                                    }else {
                                        mProgress.setVisibility(View.GONE);
                                        String Responsecode = jsonObj.getString("Data1");
                                        Log.i(TAG, "else True: " + Responsecode);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mProgress.setVisibility(View.GONE);
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


    private void verifikasiTask() {
        String StatusTask = sharedPrefDataTask.getStatusTask();
        String statusDataLokasi = sharedPrefDataTask.getStatusDataLokasi();
        String statusGeneralInfo = sharedPrefDataTask.getStatusGeneralInfo();
        String statusDataTeknis = sharedPrefDataTask.getStatusDataTeknis();
        String statusDataBarang = sharedPrefDataTask.getStatusDataBarang();
        String StatusDataSurvey = sharedPrefDataTask.getStatusDataSurvey();
        String statusInstallasi = sharedPrefDataTask.getStatusDataInstallasi();
        String statusUploadPhoto = sharedPrefDataTask.getStatusUploadPhoto();

        if (StatusTask.equals("1")){

            if (statusDataLokasi.equals("true") && statusGeneralInfo.equals("true") && statusDataTeknis.equals("true")
                    && statusDataBarang.equals("true") && statusUploadPhoto.equals("true")){
                Toasty.success(this, "Data Sudah Lengkap", Toast.LENGTH_SHORT, true).show();
                updateTask();
            }else {
                if (statusDataLokasi.equals("true") && StatusDataSurvey.equals("true") && statusUploadPhoto.equals("true")){
                    Toasty.success(this, "Data Sudah Lengkap", Toast.LENGTH_SHORT, true).show();
                    updateTask();
                }else {
                    if (statusDataLokasi.equals("true") && statusGeneralInfo.equals("true") && statusDataTeknis.equals("true")
                            && statusDataBarang.equals("true") && statusInstallasi.equals("true") && statusUploadPhoto.equals("true")){
//                        Toasty.success(this, "Data Sudah Lengkap", Toast.LENGTH_SHORT, true).show();
                        updateTask();
                    }else {
                        Toasty.warning(this, "Lengkapi Data Yang masih Merah!", Toast.LENGTH_SHORT, true).show();
                    }
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
//            onBackPressed();
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

    public void dataLokasi(View view) {
        Intent next = new Intent(this, DataLokasiActivity.class);
                next.putExtra(SharedPrefManager.SP_ID, id);
                next.putExtra(SharedPrefManager.SP_VID, vid);
                next.putExtra(SharedPrefDataTask.NoTask, noTask);
        startActivityForResult(next,TAGDataLokas);
    }

    public void generalInfo(View view) {
        Intent next = new Intent(this, GeneralInfoActivity.class);
                next.putExtra(SharedPrefManager.SP_ID, id);
                next.putExtra(SharedPrefManager.SP_VID, vid);
                next.putExtra(SharedPrefDataTask.NoTask, noTask);
        startActivityForResult(next,TAGGeneralInfo);

    }

    public void dataTeknisi(View view) {
        Intent next = new Intent(this, DataTeknisiActivity.class);
                next.putExtra(SharedPrefManager.SP_ID, id);
                next.putExtra(SharedPrefDataTask.VID, vid);
                next.putExtra(SharedPrefDataTask.NoTask, noTask);
        startActivityForResult(next,TAGDataTeknisi);
    }


    public void dataBarang(View view) {
        Intent next = new Intent(this, DataBarangActivity.class);
        sharedPrefDataTask.SetTaskVid(vid);
        sharedPrefDataTask.SetTaskid(id);
        sharedPrefDataTask.SetTaskNoTask(noTask);
        startActivityForResult(next,TAGDataBarang);
    }

    public void uploadPhoto(View view) {
        Intent next = new Intent(this, GalleryPhotoActivity.class);
                next.putExtra(SharedPrefDataTask.VID, vid);
                next.putExtra(SharedPrefManager.SP_ID, id);
                next.putExtra(SharedPrefDataTask.idJenisTask, statusTask);
                next.putExtra(SharedPrefDataTask.NoTask, noTask);
        startActivityForResult(next,TAGUploadFoto);
    }


    public void instalasi(View view) {
        Intent next = new Intent(this, DataInstallasiActivity.class);
                next.putExtra(SharedPrefManager.SP_ID, id);
                next.putExtra(SharedPrefManager.SP_VID, vid);
                next.putExtra(SharedPrefDataTask.NoTask, noTask);
        startActivityForResult(next,TAGInstalasi);
    }


    public void survey(View view) {
        Intent next = new Intent(this, DataSurveyActivity.class);
                next.putExtra(SharedPrefManager.SP_ID, id);
                next.putExtra(SharedPrefManager.SP_VID, vid);
        startActivityForResult(next,TAGSurvay);
    }

    private void updateTask() {

        String url = BaseUrl.getPublicIp + BaseUrl.updateDataTask;
        Log.i("url", "Data Lokasi: "+ url);
        String statusTask = "4";
        Log.i("GetVID", "updateDataLokasi: " + id);
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                ResultWS = jsonResponse.getString("Result");

                                if (ResultWS.equals("True")){
                                    String Data1 = jsonResponse.getString("Data1");
                                    Log.i("TAG", "onResponse: " + Data1);
                                    Toasty.success(TaskDetailActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                    Log.i("TAG","Ski data from server - ok" + response );
                                    Intent next = new Intent();
                                    setResult(RESULT_OK);
                                    finish();
                                }

                                Log.i("TAG","Ski data from server - ok"+ response );
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(TaskDetailActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("TAG","Ski error connect - "+error);
                            Toasty.error(TaskDetailActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
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
//                    String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\":[{\"WhereDatabaseinYou\":\"NoListTask=\'"+id+"\'\",\"IdStatusPerbaikan\":\""+statisTask+"\"}],\"Data1\":\"\",\"Data2\":\"\",\"Data3\":\"\",\"Data4\":\"\",\"Data5\":\"\",\"Data6\":\"\",\"Data7\":\"\",\"Data8\":\"\",\"Data9\":\"\",\"Data10\":\"\"}]}";
                    String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"NoListTask = '"+id+"'\",\"VID\":\""+vid+"\",\"IdStatusPerbaikan\": \""+statusTask+"\",\"DateUpdate\": \"2018-09-28\",\"UserUpdate\": \"MARTADINATA\"}],\"PARAM2\": [{\"WhereDatabaseinYou\": \"NoTask = '"+noTask+"'\",\"IdStatusTask\": \""+statusTask+"\",\"DateStamp\": \"2018-09-28\"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                    Log.i("sendBody", "sendBody: " + str);
                    return str.getBytes();
                }

                public String getBodyContentType()
                {
                    return "application/x-www-form-urlencoded; charset=utf-8";
                }
            };

            Mysingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode==TAGDataLokas && data!=null){
            if (resultCode == RESULT_OK){
                id = data.getStringExtra(SharedPrefManager.SP_ID);
                new AsyingDetaikTask().execute();
            }
        }
        if (requestCode==TAGGeneralInfo && data!=null){
            if (resultCode == RESULT_OK){
                id = data.getStringExtra(SharedPrefManager.SP_ID);
                new AsyingDetaikTask().execute();
            }
        }
        if (requestCode==TAGDataTeknisi && data!=null){
            if (resultCode == RESULT_OK){
                id = data.getStringExtra(SharedPrefManager.SP_ID);
                new AsyingDetaikTask().execute();
            }
        }
        if (requestCode==TAGDataBarang){
            if (resultCode==RESULT_OK){
                id = sharedPrefDataTask.GetTaskid();
                new AsyingDetaikTask().execute();
            }
        }
        if (requestCode==TAGUploadFoto && data!=null){
            if (resultCode == RESULT_OK){
                id = data.getStringExtra(SharedPrefManager.SP_ID);
                Log.i("CEK", "ID = " + id);
                new AsyingDetaikTask().execute();
            }
        }
        if (requestCode==TAGInstalasi && data!=null){
            if (resultCode == RESULT_OK){
                id = data.getStringExtra(SharedPrefManager.SP_ID);
                new AsyingDetaikTask().execute();
            }
        }
        if (requestCode==TAGSurvay && data!=null){
            if (resultCode == RESULT_OK){
                id = data.getStringExtra(SharedPrefManager.SP_ID);
                new AsyingDetaikTask().execute();
            }
        }
    }

}
