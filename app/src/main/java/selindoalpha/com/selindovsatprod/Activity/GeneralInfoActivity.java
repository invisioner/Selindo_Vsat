package selindoalpha.com.selindovsatprod.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class GeneralInfoActivity extends AppCompatActivity {

    private static final String TAG = "GeneralInfoActivity";

    EditText mTask, mVid, mSid, mOrder,mIdAtm, mIpLan, mLaporan, mStatusCheck, mCatatan,mTglBerangkat,mTglSelesai, mTglPulang,mTglStatusPerbaikan;
    ImageView TglBerangkat,TglSelesai, TglPulang,TglStatusPerbaikan;
    Button mBtnSave;
    String jsonStr,ResultWS, statusGeneralInfo;
    Boolean statusGI = false;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;

    String id, noTask,strTglBerangkat,strTglSelesai, strTglPulang,strTglStatusPerbaikan;
    ProgressDialog progressDialog;

    DatePickerDialog DPD;
    Calendar myCalender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("GENERAL INFO");

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            id = in.getString(SharedPrefManager.SP_ID);
            noTask = in.getString(SharedPrefDataTask.NoTask);
        }

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);
        progressDialog = new ProgressDialog(this);
        mTask = findViewById(R.id.no_task);
        mTask.setEnabled(false);
        mVid = findViewById(R.id.no_vid);
        mVid.setEnabled(false);
        mSid = findViewById(R.id.no_sid);
        mSid.setEnabled(false);
        mLaporan = findViewById(R.id.laporan_pengaduan);
        mLaporan.setEnabled(false);
        mStatusCheck = findViewById(R.id.status_check_koordinator);
        mStatusCheck.setEnabled(false);
        mOrder = findViewById(R.id.order);
        mOrder.setEnabled(false);
        mIpLan = findViewById(R.id.ip_lan);
        mIdAtm = findViewById(R.id.id_atm);
        mTglBerangkat = findViewById(R.id.tgl_berangkat_ed);
        mTglSelesai = findViewById(R.id.tgl_selesai_ed);
        mTglPulang = findViewById(R.id.tgl_pulang_ed);
        mTglStatusPerbaikan = findViewById(R.id.tgl_status_perbaikan_ed);
        TglBerangkat = findViewById(R.id.berangkat_pick);
        TglSelesai = findViewById(R.id.selesai_pick);
        TglPulang = findViewById(R.id.pulang_pick);
        TglStatusPerbaikan = findViewById(R.id.perbaikan_pick);
        mCatatan = findViewById(R.id.catatan_koordinator);
        mCatatan.setEnabled(false);
        mBtnSave = findViewById(R.id.save_general_info);

        myCalender = Calendar.getInstance();
        TglBerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DPD = new DatePickerDialog(GeneralInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalender.set(Calendar.YEAR,year);
                        myCalender.set(Calendar.MONTH,month);
                        myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        String formatTanggal = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        String formatTanggaldb = "yyyy-MM-dd";
                        SimpleDateFormat sdfdb = new SimpleDateFormat(formatTanggaldb);
                        mTglBerangkat.setText(sdf.format(myCalender.getTime()));
                        strTglBerangkat = sdfdb.format(myCalender.getTime());
                    }
                },
                        myCalender.get(Calendar.YEAR),myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH));
                DPD.show();
            }
        });

        TglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DPD = new DatePickerDialog(GeneralInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalender.set(Calendar.YEAR,year);
                        myCalender.set(Calendar.MONTH,month);
                        myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        String formatTanggal = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        String formatTanggaldb = "yyyy-MM-dd";
                        SimpleDateFormat sdfdb = new SimpleDateFormat(formatTanggaldb);
                        mTglSelesai.setText(sdf.format(myCalender.getTime()));
                        strTglSelesai = sdfdb.format(myCalender.getTime());
                    }
                },
                        myCalender.get(Calendar.YEAR),myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH));
                DPD.show();
            }
        });

        TglPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DPD = new DatePickerDialog(GeneralInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalender.set(Calendar.YEAR,year);
                        myCalender.set(Calendar.MONTH,month);
                        myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        String formatTanggal = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        String formatTanggaldb = "yyyy-MM-dd";
                        SimpleDateFormat sdfdb = new SimpleDateFormat(formatTanggaldb);
                        mTglPulang.setText(sdf.format(myCalender.getTime()));
                        strTglPulang = sdfdb.format(myCalender.getTime());
                    }
                },
                        myCalender.get(Calendar.YEAR),myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH));
                DPD.show();
            }
        });

        TglStatusPerbaikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DPD = new DatePickerDialog(GeneralInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalender.set(Calendar.YEAR,year);
                        myCalender.set(Calendar.MONTH,month);
                        myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        String formatTanggal = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        String formatTanggaldb = "yyyy-MM-dd";
                        SimpleDateFormat sdfdb = new SimpleDateFormat(formatTanggaldb);
                        mTglStatusPerbaikan.setText(sdf.format(myCalender.getTime()));
                        strTglStatusPerbaikan = sdfdb.format(myCalender.getTime());
                    }
                },
                        myCalender.get(Calendar.YEAR),myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH));
                DPD.show();
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGeneralInfo();
            }
        });

        new AsyingGeneralInfo().execute();
//        dataGeneralInfo();
    }

    private class AsyingGeneralInfo extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dataGeneralInfo();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    public void dataGeneralInfo(){
        progressDialog.show();
        Log.i(TAG, "NoTask General Info: " + id);
        String url = BaseUrl.getPublicIp + BaseUrl.detailTask+id;
        Log.i(TAG, "dataGeneralInfo: " + url);
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new Handler().postDelayed(new Runnable() {
                            @SuppressLint("SimpleDateFormat")
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

                                            String statusGeneralInfo = data.getString("IdStatusPerbaikan");
                                            String statusGI = data.getString("FlagGeneralInfo");
                                            Log.i("TAG", "run: "+ statusGI);

                                            Log.i("TAG", "status : "+statusGeneralInfo);
                                            if (statusGeneralInfo.equals("1")) {
                                                Log.i(TAG, "GeneralInfo: " + data.getString("NoTask"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("VID1"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("SID"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IPLAN"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IdATM"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("LaporanPengaduan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IdStatusKoordinator"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("idJenisTask1"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglBerangkat"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglSelesaiKerjaan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglPulang"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglStatusPerbaikan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("CatatanKoordinator"));

                                                if (data.getString("NoTask").equals("null") || data.getString("NoTask").equals(null)){
                                                    String noTask = "";
                                                    mTask.setText(noTask);
                                                }else {
                                                    String noTask = data.getString("NoTask");
                                                    mTask.setText(noTask);
                                                }
                                                if (data.getString("VID1").equals("null") || data.getString("VID1").equals(null)){
                                                    String vid = "";
                                                    mVid.setText(vid);
                                                }else {
                                                    String vid = data.getString("VID1");
                                                    mVid.setText(vid);
                                                }
                                                if (data.getString("SID").equals("null") || data.getString("SID").equals(null)){
                                                    String noSid = "";
                                                    mSid.setText(noSid);
                                                }else {
                                                    String noSid = data.getString("SID");
                                                    mSid.setText(noSid);
                                                }
                                                if (data.getString("IPLAN").equals("null") || data.getString("IPLAN").equals(null)){
                                                    String ipLan = "";
                                                    mIpLan.setText(ipLan);
                                                }else {
                                                    String ipLan = data.getString("IPLAN");
                                                    mIpLan.setText(ipLan);
                                                }
                                                if (data.getString("IdATM").equals("null") || data.getString("IdATM").equals(null)){
                                                    String idAtm = "";
                                                    mIdAtm.setText(idAtm);
                                                }else {
                                                    String idAtm = data.getString("IdATM");
                                                    mIdAtm.setText(idAtm);
                                                }
                                                if (data.getString("LaporanPengaduan").equals("null") || data.getString("LaporanPengaduan").equals(null)){
                                                    String LaporanPengaduan = "";
                                                    mLaporan.setText(LaporanPengaduan);
                                                }else {
                                                    String LaporanPengaduan = data.getString("LaporanPengaduan");
                                                    mLaporan.setText(LaporanPengaduan);
                                                }
                                                if (data.getString("IdStatusKoordinator").equals("null") || data.getString("IdStatusKoordinator").equals(null)){
                                                    String StatusCheck = "";
                                                    mStatusCheck.setText(StatusCheck);
                                                }else {
                                                    String StatusCheck = data.getString("IdStatusKoordinator");
                                                    mStatusCheck.setText(StatusCheck);
                                                }
                                                if (data.getString("idJenisTask1").equals("null") || data.getString("idJenisTask1").equals(null)){
                                                    String order = "";
                                                    mOrder.setText(order);
                                                }else {
                                                    String order = data.getString("idJenisTask1");
                                                    mOrder.setText(order);
                                                }
                                                if (data.getString("CatatanKoordinator").equals("null") || data.getString("CatatanKoordinator").equals(null)){
                                                    String CatatanKoordinator = "";
                                                    mCatatan.setText(CatatanKoordinator);
                                                }else {
                                                    String CatatanKoordinator = data.getString("CatatanKoordinator");
                                                    mCatatan.setText(CatatanKoordinator);
                                                }
                                                String tglBerangkat = data.getString("TglBerangkat");
                                                String tglSelesai = data.getString("TglSelesaiKerjaan");
                                                String tglPulang = data.getString("TglPulang");
                                                String tglStatusPerbaikan = data.getString("TglStatusPerbaikan");
                                                if (tglBerangkat.equals("null") && tglSelesai.equals("null") && tglPulang.equals("null") && tglStatusPerbaikan.equals("null") || tglBerangkat.equals(null) && tglSelesai.equals(null) && tglPulang.equals(null) && tglStatusPerbaikan.equals(null)){
                                                    mTglBerangkat.setText("");
                                                    strTglBerangkat="";
                                                    mTglSelesai.setText("");
                                                    strTglSelesai = "";
                                                    mTglPulang.setText("");
                                                    strTglPulang = "";
                                                    mTglStatusPerbaikan.setText("");
                                                    strTglStatusPerbaikan = "";
                                                } else {
                                                    Date date2 = null;
                                                    Date date3 = null;
                                                    Date date4 = null;
                                                    Date date5 = null;
                                                    try {
                                                        date2 = new SimpleDateFormat("dd-MM-yyyy").parse(tglBerangkat);
                                                        date3 = new SimpleDateFormat("dd-MM-yyyy").parse(tglSelesai);
                                                        date4 = new SimpleDateFormat("dd-MM-yyyy").parse(tglPulang);
                                                        date5 = new SimpleDateFormat("dd-MM-yyyy").parse(tglStatusPerbaikan);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                        Toasty.success(GeneralInfoActivity.this, "Data Tanggal Berangkat/Selesai/Pulang/StatusPerbaikan tidak boleh KOSONG.", Toast.LENGTH_SHORT, true).show();
                                                        break;
                                                    }
                                                    String dateString1 = new SimpleDateFormat("dd-MM-yyyy").format(date2);
                                                    String dateString2 = new SimpleDateFormat("dd-MM-yyyy").format(date3);
                                                    String dateString3 = new SimpleDateFormat("dd-MM-yyyy").format(date4);
                                                    String dateString4 = new SimpleDateFormat("dd-MM-yyyy").format(date5);

                                                    mTglBerangkat.setText(dateString1);
                                                    strTglBerangkat = dateString1;
                                                    mTglSelesai.setText(dateString2);
                                                    strTglSelesai = dateString2;
                                                    mTglPulang.setText(dateString3);
                                                    strTglPulang = dateString3;
                                                    mTglStatusPerbaikan.setText(dateString4);
                                                    strTglStatusPerbaikan = dateString4;
                                                }
                                            }

                                            if (statusGeneralInfo.equals("4")) {
                                                Log.i(TAG, "GeneralInfo: " + data.getString("NoTask"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("VID1"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("SID"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IPLAN"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IdATM"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("LaporanPengaduan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("IdStatusKoordinator"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("idJenisTask1"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglBerangkat"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglSelesaiKerjaan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglPulang"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("TglStatusPerbaikan"));
                                                Log.i(TAG, "GeneralInfo: " + data.getString("CatatanKoordinator"));

                                                if (data.getString("NoTask").equals("null") || data.getString("NoTask").equals(null)){
                                                    String noTask = "";
                                                    mTask.setText(noTask);
                                                    mTask.setEnabled(false);
                                                }else {
                                                    String noTask = data.getString("NoTask");
                                                    mTask.setText(noTask);
                                                    mTask.setEnabled(false);
                                                }
                                                if (data.getString("VID1").equals("null") || data.getString("VID1").equals(null)){
                                                    String vid = "";
                                                    mVid.setText(vid);
                                                    mVid.setEnabled(false);
                                                }else {
                                                    String vid = data.getString("VID1");
                                                    mVid.setText(vid);
                                                    mVid.setEnabled(false);
                                                }
                                                if (data.getString("SID").equals("null") || data.getString("SID").equals(null)){
                                                    String noSid = "";
                                                    mSid.setText(noSid);
                                                    mSid.setEnabled(false);
                                                }else {
                                                    String noSid = data.getString("SID");
                                                    mSid.setText(noSid);
                                                    mSid.setEnabled(false);
                                                }
                                                if (data.getString("IPLAN").equals("null") || data.getString("IPLAN").equals(null)){
                                                    String ipLan = "";
                                                    mIpLan.setText(ipLan);
                                                    mIpLan.setEnabled(false);
                                                }else {
                                                    String ipLan = data.getString("IPLAN");
                                                    mIpLan.setText(ipLan);
                                                    mIpLan.setEnabled(false);
                                                }
                                                if (data.getString("IdATM").equals("null") || data.getString("IdATM").equals(null)){
                                                    String idAtm = "";
                                                    mIdAtm.setText(idAtm);
                                                    mIdAtm.setEnabled(false);
                                                }else {
                                                    String idAtm = data.getString("IdATM");
                                                    mIdAtm.setText(idAtm);
                                                    mIdAtm.setEnabled(false);
                                                }
                                                if (data.getString("LaporanPengaduan").equals("null") || data.getString("LaporanPengaduan").equals(null)){
                                                    String LaporanPengaduan = "";
                                                    mLaporan.setText(LaporanPengaduan);
                                                    mLaporan.setEnabled(false);
                                                }else {
                                                    String LaporanPengaduan = data.getString("LaporanPengaduan");
                                                    mLaporan.setText(LaporanPengaduan);
                                                    mLaporan.setEnabled(false);
                                                }
                                                if (data.getString("IdStatusKoordinator").equals("null") || data.getString("IdStatusKoordinator").equals(null)){
                                                    String StatusCheck = "";
                                                    mStatusCheck.setText(StatusCheck);
                                                    mStatusCheck.setEnabled(false);
                                                }else {
                                                    String StatusCheck = data.getString("IdStatusKoordinator");
                                                    mStatusCheck.setText(StatusCheck);
                                                    mStatusCheck.setEnabled(false);
                                                }
                                                if (data.getString("idJenisTask1").equals("null") || data.getString("idJenisTask1").equals(null)){
                                                    String order = "";
                                                    mOrder.setText(order);
                                                    mOrder.setEnabled(false);
                                                }else {
                                                    String order = data.getString("idJenisTask1");
                                                    mOrder.setText(order);
                                                    mOrder.setEnabled(false);
                                                }
                                                if (data.getString("CatatanKoordinator").equals("null") || data.getString("CatatanKoordinator").equals(null)){
                                                    String CatatanKoordinator = "";
                                                    mCatatan.setText(CatatanKoordinator);
                                                    mCatatan.setEnabled(false);
                                                }else {
                                                    String CatatanKoordinator = data.getString("CatatanKoordinator");
                                                    mCatatan.setText(CatatanKoordinator);
                                                    mCatatan.setEnabled(false);
                                                }
                                                String tglBerangkat = data.getString("TglBerangkat");
                                                String tglSelesai = data.getString("TglSelesaiKerjaan");
                                                String tglPulang = data.getString("TglPulang");
                                                String tglStatusPerbaikan = data.getString("TglStatusPerbaikan");

                                                if (tglBerangkat.equals("null") && tglSelesai.equals("null") && tglPulang.equals("null") && tglStatusPerbaikan.equals("null")){
                                                    mTglBerangkat.setText(tglBerangkat);
                                                    strTglBerangkat = tglBerangkat;
                                                    mTglSelesai.setText(tglSelesai);
                                                    strTglSelesai = tglSelesai;
                                                    mTglPulang.setText(tglPulang);
                                                    strTglPulang = tglPulang;
                                                    mTglStatusPerbaikan.setText(tglStatusPerbaikan);
                                                    strTglStatusPerbaikan = tglStatusPerbaikan;
                                                } else {
                                                    Date date6 = null;
                                                    Date date7 = null;
                                                    Date date8 = null;
                                                    Date date9 = null;
                                                    try {
                                                        date6 = new SimpleDateFormat("dd-MM-yyyy").parse(tglBerangkat);
                                                        date7 = new SimpleDateFormat("dd-MM-yyyy").parse(tglSelesai);
                                                        date8 = new SimpleDateFormat("dd-MM-yyyy").parse(tglPulang);
                                                        date9 = new SimpleDateFormat("dd-MM-yyyy").parse(tglStatusPerbaikan);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    String dateString1 = new SimpleDateFormat("yyyy-MM-dd").format(date6);
                                                    String dateString2 = new SimpleDateFormat("yyyy-MM-dd").format(date7);
                                                    String dateString3 = new SimpleDateFormat("yyyy-MM-dd").format(date8);
                                                    String dateString4 = new SimpleDateFormat("yyyy-MM-dd").format(date9);

                                                    mTglBerangkat.setText(dateString1);
                                                    strTglBerangkat = dateString1;
                                                    mTglBerangkat.setEnabled(false);
                                                    mTglSelesai.setText(dateString2);
                                                    strTglSelesai = dateString2;
                                                    mTglSelesai.setEnabled(false);
                                                    mTglPulang.setText(dateString3);
                                                    strTglPulang = dateString3;
                                                    mTglPulang.setEnabled(false);
                                                    mTglStatusPerbaikan.setText(dateString4);
                                                    strTglStatusPerbaikan = dateString4;
                                                    mTglStatusPerbaikan.setEnabled(false);
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

    private void updateGeneralInfo() {
        String url = BaseUrl.getPublicIp+BaseUrl.updateGeneralInfo;
        Log.i("url", "General info: "+ url);

        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        Log.i("Date", "Realtime Date: " + date);
        String nameTeknisi = sharedPrefManager.getSPUserName();
        String vid = mVid.getText().toString();
        String ipLan = mIpLan.getText().toString();
        String idAtm = mIdAtm.getText().toString();
        String tglBerangkat = strTglBerangkat;
        String tglSelesai = strTglSelesai;
        String tglPulang = strTglPulang;
        String tglStatusPerbaikan = strTglStatusPerbaikan;
        Boolean statusGi = statusGI = true;

        if (ipLan.matches("")) {
            mIpLan.setError("isi data ini");
        }
        else if (idAtm.matches("")){
            mIdAtm.setError("isi data ini");
        }
        else if (tglBerangkat.matches("")){
            mTglBerangkat.setError("isi data ini");
        }
        else if (tglSelesai.matches("")){
            mTglSelesai.setError("isi data ini");
        }
        else if (tglPulang.matches("")){
            mTglPulang.setError("isi data ini");
        }
        else if (tglStatusPerbaikan.matches("")){
            mTglStatusPerbaikan.setError("isi data ini");
        }
        else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                ResultWS = jsonResponse.getString("Result");
                                if (ResultWS.equals("True")){
                                    String Data1 = jsonResponse.getString("Data1");
                                    Toasty.success(GeneralInfoActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                    Log.i("TAG", "onResponse: " + Data1);

                                    Log.i("TAG","Ski data from server - ok" + response );

                                    Intent intent = new Intent();
                                    intent.putExtra(SharedPrefManager.SP_ID, id);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                                Log.i("TAG","Ski data from server - ok" + response );

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(GeneralInfoActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("TAG","Ski error connect - " + error);
                            Toasty.error(GeneralInfoActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
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
//                    String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"NoListTask = '"+id+"'\",\"IdATM\": \""+idAtm+"\",\"IPLAN\": \""+ipLan+"\",\"TglBerangkat\": \""+tglBerangkat+"\",\"TglSelesaiKerjaan\": \""+tglSelesai+"\",\"TglPulang\": \""+tglPulang+"\",\"TglStatusPerbaikan\": \""+tglStatusPerbaikan+"\",\"UserUpdate\": \"ADMIN\",\"DateUpdate\": \"2018-09-28\"}],\"PARAM2\": [{\"WhereDatabaseinYou\": \"VID = '"+vid+"'\",\"IPLAN\": \""+ipLan+"\"}],\"PARAM3\": [{\"WhereDatabaseinYou\": \"VID='"+vid+"' and NoTask = '"+noTask+"'\",\"FlagGeneralInfo\": "+statusGi+"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                    String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"NoListTask = '"+id+"'\",\"IdATM\": \""+idAtm+"\",\"IPLAN\": \""+ipLan+"\",\"TglBerangkat\": \""+tglBerangkat+"\",\"TglSelesaiKerjaan\": \""+tglSelesai+"\",\"TglPulang\": \""+tglPulang+"\",\"TglStatusPerbaikan\": \""+tglStatusPerbaikan+"\",\"UserUpdate\": \"ADMIN\",\"DateUpdate\": \"2018-09-28\",\"FlagGeneralInfo\": "+statusGi+"}],\"PARAM2\": [{\"WhereDatabaseinYou\": \"VID = '"+vid+"'\",\"IPLAN\": \""+ipLan+"\"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
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
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
