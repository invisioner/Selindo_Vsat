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
import android.widget.AdapterView;
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
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class DataTeknisiActivity extends AppCompatActivity {

    private static final String TAG = "DataTeknisiActivity";

    EditText mHardware, mSqf, mEsno, mCtn, mXPoll, mCpi, mOpratorSatelite, mOpratorHelpDesk;
    EditText mOutPln, mAktifitasSolusi, mOutUps, mSuhuRuangan, mTypeMounting, mPanjangKabel;
    EditText mLetakAntena, mLetakModem, mLetakSatelit, mAnalisaProblem;
    Button mSaveDataTeknisi;
    Spinner mUpsForBackup;
    Boolean statusDT;

    String id, vid, noTask;
    String jsonStr,ResultWS, statusDataTeknisi,upsForBackup;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_teknisi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("DATA TEKNIS");

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            id = in.getString(SharedPrefManager.SP_ID);
            vid = in.getString(SharedPrefDataTask.VID);
            noTask = in.getString(SharedPrefDataTask.NoTask);
        }

        sharedPrefManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);

        mHardware = findViewById(R.id.hardware_rusak);
        mSqf = findViewById(R.id.sqf);
        mEsno = findViewById(R.id.enso);
        mCtn = findViewById(R.id.carrier);
        mXPoll = findViewById(R.id.hasil_xpoll);
        mCpi = findViewById(R.id.cpi);
        mOpratorSatelite = findViewById(R.id.oprator_satelit);
        mOpratorHelpDesk = findViewById(R.id.oprator_helpdesk);
        mOutPln = findViewById(R.id.out_pln);
        mAktifitasSolusi = findViewById(R.id.aktifitas_solusi);
        mOutUps = findViewById(R.id.out_ups);
        mSuhuRuangan = findViewById(R.id.suhu_ruangan);
        mTypeMounting = findViewById(R.id.type_mounting);
        mPanjangKabel = findViewById(R.id.panjang_kabel);
        mLetakAntena = findViewById(R.id.letak_antena);
        mLetakModem = findViewById(R.id.letak_modem);
        mLetakSatelit = findViewById(R.id.letak_satelit);
        mAnalisaProblem = findViewById(R.id.analisa_problem);
        mUpsForBackup = findViewById(R.id.ups_for_backup);
        mSaveDataTeknisi = findViewById(R.id.save_data_teknisi);

        new AsyingDataTeknisi().execute();

        mSaveDataTeknisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataLokasi();
            }
        });

        mUpsForBackup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getStr = (TextView) mUpsForBackup.getSelectedView();
                getStr.setText(mUpsForBackup.getSelectedItem().toString());
                upsForBackup = mUpsForBackup.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private class AsyingDataTeknisi extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dataDataTeknisi();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    public void dataDataTeknisi(){
        progressDialog.show();
        Log.i(TAG, "NoTask General Info: " + id);
        String url = BaseUrl.getPublicIp+ BaseUrl.detailTask+ id;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "dataGeneralInfo: " + url);

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

                                            String statusDataTeknisi = data.getString("IdStatusPerbaikan");
                                            String statusDT = data.getString("FlagDataTeknis");
                                            Log.i("DataTeknisi", "DataTeknisi: " + statusDataTeknisi);
                                            Log.i("statusDT", "statusDT: " + statusDT);

                                            if (statusDataTeknisi.equals("1")){
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("FAIL_HW"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("SQF"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("INITIAL_ESNO"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("CARRIER_TO_NOICE"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("HasilXPOLL"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("CPI"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("OperatorSatelite"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("OperatorHelpDesk"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("OutPLN"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("AktifitasSolusi"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("OutUPS"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("UPSforBackup"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("SuhuRuangan"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("TypeMounting"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("PanjangKabel"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("LetakAntena"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("LetakModem"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("KondisiBangungan"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("AnalisaProblem"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("UPSforBackup"));

                                                if (data.getString("FAIL_HW").equals("null")){
                                                    String hardware = "";
                                                    mHardware.setText(hardware);
                                                }else {
                                                    String hardware = data.getString("FAIL_HW");
                                                    mHardware.setText(hardware);
                                                }
                                                if (data.getString("SQF").equals("null")){
                                                    String sqf = "";
                                                    mSqf.setText(sqf);
                                                }else {
                                                    String sqf = data.getString("SQF");
                                                    mSqf.setText(sqf);
                                                }
                                                if (data.getString("INITIAL_ESNO").equals("null")){
                                                    String esno = "";
                                                    mEsno.setText(esno);
                                                }else {
                                                    String esno = data.getString("INITIAL_ESNO");
                                                    mEsno.setText(esno);
                                                }
                                                if (data.getString("CARRIER_TO_NOICE").equals("null")){
                                                    String ctn = "";
                                                    mCtn.setText(ctn);
                                                }else {
                                                    String ctn = data.getString("CARRIER_TO_NOICE");
                                                    mCtn.setText(ctn);
                                                }
                                                if (data.getString("HasilXPOLL").equals("null")){
                                                    String xpoll = "";
                                                    mXPoll.setText(xpoll);
                                                }else {
                                                    String xpoll = data.getString("HasilXPOLL");
                                                    mXPoll.setText(xpoll);
                                                }
                                                if (data.getString("CPI").equals("null")){
                                                    String ctn = "";
                                                    mCtn.setText(ctn);
                                                }else {
                                                    String ctn = data.getString("CARRIER_TO_NOICE");
                                                    mCtn.setText(ctn);
                                                }
                                                if (data.getString("OperatorSatelite").equals("null")){
                                                    String ctn = "";
                                                    mCtn.setText(ctn);
                                                }else {
                                                    String ctn = data.getString("CARRIER_TO_NOICE");
                                                    mCtn.setText(ctn);
                                                }
                                                if (data.getString("CPI").equals("null")){
                                                    String cpi = "";
                                                    mCpi.setText(cpi);
                                                }else {
                                                    String cpi = data.getString("CPI");
                                                    mCpi.setText(cpi);
                                                }
                                                if (data.getString("OperatorSatelite").equals("null")){
                                                    String opratorSatelite = "";
                                                    mOpratorSatelite.setText(opratorSatelite);
                                                }else {
                                                    String opratorSatelite = data.getString("OperatorSatelite");
                                                    mOpratorSatelite.setText(opratorSatelite);
                                                }
                                                if (data.getString("OperatorHelpDesk").equals("null")){
                                                    String helpDesk = "";
                                                    mOpratorHelpDesk.setText(helpDesk);
                                                }else {
                                                    String helpDesk = data.getString("OperatorHelpDesk");
                                                    mOpratorHelpDesk.setText(helpDesk);
                                                }
                                                if (data.getString("OutPLN").equals("null")){
                                                    String outPln = "";
                                                    mOutPln.setText(outPln);
                                                }else {
                                                    String outPln = data.getString("OutPLN");
                                                    mOutPln.setText(outPln);
                                                }
                                                if (data.getString("AktifitasSolusi").equals("null")){
                                                    String aktifitasSolusi = "";
                                                    mAktifitasSolusi.setText(aktifitasSolusi);
                                                }else {
                                                    String aktifitasSolusi = data.getString("AktifitasSolusi");
                                                    mAktifitasSolusi.setText(aktifitasSolusi);
                                                }
                                                if (data.getString("OutUPS").equals("null")){
                                                    String outUps = "";
                                                    mOutUps.setText(outUps);
                                                }else {
                                                    String outUps = data.getString("OutUPS");
                                                    mOutUps.setText(outUps);
                                                }
                                                if (data.getString("SuhuRuangan").equals("null")){
                                                    String suhuRuangan = "";
                                                    mSuhuRuangan.setText(suhuRuangan);
                                                }else {
                                                    String suhuRuangan = data.getString("SuhuRuangan");
                                                    mSuhuRuangan.setText(suhuRuangan);
                                                }
                                                if (data.getString("TypeMounting").equals("null")){
                                                    String typeMounting = "";
                                                    mPanjangKabel.setText(typeMounting);
                                                }else {
                                                    String typeMounting = data.getString("TypeMounting");
                                                    mPanjangKabel.setText(typeMounting);
                                                }
                                                if (data.getString("PanjangKabel").equals("null")){
                                                    String panjangKabel = "";
                                                    mTypeMounting.setText(panjangKabel);
                                                }else {
                                                    String panjangKabel = data.getString("PanjangKabel");
                                                    mTypeMounting.setText(panjangKabel);
                                                }
                                                if (data.getString("LetakAntena").equals("null")){
                                                    String letakAntena = "";
                                                    mLetakAntena.setText(letakAntena);
                                                }else {
                                                    String letakAntena = data.getString("LetakAntena");
                                                    mLetakAntena.setText(letakAntena);
                                                }
                                                if (data.getString("LetakModem").equals("null")){
                                                    String letakModem = "";
                                                    mLetakModem.setText(letakModem);
                                                }else {
                                                    String letakModem = data.getString("LetakModem");
                                                    mLetakModem.setText(letakModem);
                                                }
                                                if (data.getString("KondisiBangungan").equals("null")){
                                                    String kondisiBangungan = "";
                                                    mLetakSatelit.setText(kondisiBangungan);
                                                }else {
                                                    String kondisiBangungan = data.getString("KondisiBangungan");
                                                    mLetakSatelit.setText(kondisiBangungan);
                                                }
                                                if (data.getString("AnalisaProblem").equals("null")){
                                                    String analisaProblem = "";
                                                    mAnalisaProblem.setText(analisaProblem);
                                                }else {
                                                    String analisaProblem = data.getString("AnalisaProblem");
                                                    mAnalisaProblem.setText(analisaProblem);
                                                }
                                                if (data.getString("UPSforBackup").equals("null")){
                                                    TextView getSpiner = (TextView) mUpsForBackup.getSelectedView();
                                                    getSpiner.setText("");//changes the selected item text to this
                                                }else {
                                                    upsForBackup = data.getString("UPSforBackup");
                                                    TextView getSpiner = (TextView) mUpsForBackup.getSelectedView();
                                                    getSpiner.setText(upsForBackup);//changes the selected item text to this
                                                }
}
                                            if (statusDataTeknisi.equals("4")){
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("FAIL_HW"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("SQF"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("INITIAL_ESNO"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("CARRIER_TO_NOICE"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("HasilXPOLL"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("CPI"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("OperatorSatelite"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("OperatorHelpDesk"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("OutPLN"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("AktifitasSolusi"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("OutUPS"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("UPSforBackup"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("SuhuRuangan"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("TypeMounting"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("PanjangKabel"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("LetakAntena"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("LetakModem"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("KondisiBangungan"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("AnalisaProblem"));
                                                Log.i("DataTeknisi", "DataTeknisi: " + data.getString("UPSforBackup"));

                                                if (data.getString("FAIL_HW").equals("null")){
                                                    String hardware = "";
                                                    mHardware.setText(hardware);
                                                    mHardware.setEnabled(false);
                                                }else {
                                                    String hardware = data.getString("FAIL_HW");
                                                    mHardware.setText(hardware);
                                                    mHardware.setEnabled(false);
                                                }
                                                if (data.getString("SQF").equals("null")){
                                                    String sqf = "";
                                                    mSqf.setText(sqf);
                                                    mSqf.setEnabled(false);
                                                }else {
                                                    String sqf = data.getString("SQF");
                                                    mSqf.setText(sqf);
                                                    mSqf.setEnabled(false);
                                                }
                                                if (data.getString("INITIAL_ESNO").equals("null")){
                                                    String esno = "";
                                                    mEsno.setText(esno);
                                                    mEsno.setEnabled(false);
                                                }else {
                                                    String esno = data.getString("INITIAL_ESNO");
                                                    mEsno.setText(esno);
                                                    mEsno.setEnabled(false);
                                                }
                                                if (data.getString("CARRIER_TO_NOICE").equals("null")){
                                                    String ctn = "";
                                                    mCtn.setText(ctn);
                                                    mCtn.setEnabled(false);
                                                }else {
                                                    String ctn = data.getString("CARRIER_TO_NOICE");
                                                    mCtn.setText(ctn);
                                                    mCtn.setEnabled(false);
                                                }
                                                if (data.getString("HasilXPOLL").equals("null")){
                                                    String xpoll = "";
                                                    mXPoll.setText(xpoll);
                                                    mXPoll.setEnabled(false);
                                                }else {
                                                    String xpoll = data.getString("HasilXPOLL");
                                                    mXPoll.setText(xpoll);
                                                    mXPoll.setEnabled(false);
                                                }
                                                if (data.getString("CPI").equals("null")){
                                                    String ctn = "";
                                                    mCtn.setText(ctn);
                                                    mCtn.setEnabled(false);
                                                }else {
                                                    String ctn = data.getString("CARRIER_TO_NOICE");
                                                    mCtn.setText(ctn);
                                                    mCtn.setEnabled(false);
                                                }
                                                if (data.getString("OperatorSatelite").equals("null")){
                                                    String ctn = "";
                                                    mCtn.setText(ctn);
                                                    mCtn.setEnabled(false);
                                                }else {
                                                    String ctn = data.getString("CARRIER_TO_NOICE");
                                                    mCtn.setText(ctn);
                                                    mCtn.setEnabled(false);
                                                }
                                                if (data.getString("CPI").equals("null")){
                                                    String cpi = "";
                                                    mCpi.setText(cpi);
                                                    mCpi.setEnabled(false);
                                                }else {
                                                    String cpi = data.getString("CPI");
                                                    mCpi.setText(cpi);
                                                    mCpi.setEnabled(false);
                                                }
                                                if (data.getString("OperatorSatelite").equals("null")){
                                                    String opratorSatelite = "";
                                                    mOpratorSatelite.setText(opratorSatelite);
                                                    mOpratorSatelite.setEnabled(false);
                                                }else {
                                                    String opratorSatelite = data.getString("OperatorSatelite");
                                                    mOpratorSatelite.setText(opratorSatelite);
                                                    mOpratorSatelite.setEnabled(false);
                                                }
                                                if (data.getString("OperatorHelpDesk").equals("null")){
                                                    String helpDesk = "";
                                                    mOpratorHelpDesk.setText(helpDesk);
                                                    mOpratorHelpDesk.setEnabled(false);
                                                }else {
                                                    String helpDesk = data.getString("OperatorHelpDesk");
                                                    mOpratorHelpDesk.setText(helpDesk);
                                                    mOpratorHelpDesk.setEnabled(false);
                                                }
                                                if (data.getString("OutPLN").equals("null")){
                                                    String outPln = "";
                                                    mOutPln.setText(outPln);
                                                    mOutPln.setEnabled(false);
                                                }else {
                                                    String outPln = data.getString("OutPLN");
                                                    mOutPln.setText(outPln);
                                                    mOutPln.setEnabled(false);
                                                }
                                                if (data.getString("AktifitasSolusi").equals("null")){
                                                    String aktifitasSolusi = "";
                                                    mAktifitasSolusi.setText(aktifitasSolusi);
                                                    mAktifitasSolusi.setEnabled(false);
                                                }else {
                                                    String aktifitasSolusi = data.getString("AktifitasSolusi");
                                                    mAktifitasSolusi.setText(aktifitasSolusi);
                                                    mAktifitasSolusi.setEnabled(false);
                                                }
                                                if (data.getString("OutUPS").equals("null")){
                                                    String outUps = "";
                                                    mOutUps.setText(outUps);
                                                    mOutUps.setEnabled(false);
                                                }else {
                                                    String outUps = data.getString("OutUPS");
                                                    mOutUps.setText(outUps);
                                                    mOutUps.setEnabled(false);
                                                }
                                                if (data.getString("SuhuRuangan").equals("null")){
                                                    String suhuRuangan = "";
                                                    mSuhuRuangan.setText(suhuRuangan);
                                                    mSuhuRuangan.setEnabled(false);
                                                }else {
                                                    String suhuRuangan = data.getString("SuhuRuangan");
                                                    mSuhuRuangan.setText(suhuRuangan);
                                                    mSuhuRuangan.setEnabled(false);
                                                }
                                                if (data.getString("TypeMounting").equals("null")){
                                                    String typeMounting = "";
                                                    mPanjangKabel.setText(typeMounting);
                                                    mPanjangKabel.setEnabled(false);
                                                }else {
                                                    String typeMounting = data.getString("TypeMounting");
                                                    mPanjangKabel.setText(typeMounting);
                                                    mPanjangKabel.setEnabled(false);
                                                }
                                                if (data.getString("PanjangKabel").equals("null")){
                                                    String panjangKabel = "";
                                                    mTypeMounting.setText(panjangKabel);
                                                    mTypeMounting.setEnabled(false);
                                                }else {
                                                    String panjangKabel = data.getString("PanjangKabel");
                                                    mTypeMounting.setText(panjangKabel);
                                                    mTypeMounting.setEnabled(false);
                                                }
                                                if (data.getString("LetakAntena").equals("null")){
                                                    String letakAntena = "";
                                                    mLetakAntena.setText(letakAntena);
                                                    mLetakAntena.setEnabled(false);
                                                }else {
                                                    String letakAntena = data.getString("LetakAntena");
                                                    mLetakAntena.setText(letakAntena);
                                                    mLetakAntena.setEnabled(false);
                                                }
                                                if (data.getString("LetakModem").equals("null")){
                                                    String letakModem = "";
                                                    mLetakModem.setText(letakModem);
                                                    mLetakModem.setEnabled(false);
                                                }else {
                                                    String letakModem = data.getString("LetakModem");
                                                    mLetakModem.setText(letakModem);
                                                    mLetakModem.setEnabled(false);
                                                }
                                                if (data.getString("KondisiBangungan").equals("null")){
                                                    String kondisiBangungan = "";
                                                    mLetakSatelit.setText(kondisiBangungan);
                                                    mLetakSatelit.setEnabled(false);
                                                }else {
                                                    String kondisiBangungan = data.getString("KondisiBangungan");
                                                    mLetakSatelit.setText(kondisiBangungan);
                                                    mLetakSatelit.setEnabled(false);
                                                }
                                                if (data.getString("AnalisaProblem").equals("null")){
                                                    String analisaProblem = "";
                                                    mAnalisaProblem.setText(analisaProblem);
                                                    mAnalisaProblem.setEnabled(false);
                                                }else {
                                                    String analisaProblem = data.getString("AnalisaProblem");
                                                    mAnalisaProblem.setText(analisaProblem);
                                                    mAnalisaProblem.setEnabled(false);
                                                }
                                                if (data.getString("UPSforBackup").equals("null")){
                                                    TextView getSpiner = (TextView) mUpsForBackup.getSelectedView();
                                                    getSpiner.setText("");//changes the selected item text to this
                                                    getSpiner.setEnabled(false);
                                                }else {
                                                    upsForBackup = data.getString("UPSforBackup");
                                                    TextView getSpiner = (TextView) mUpsForBackup.getSelectedView();
                                                    getSpiner.setText(upsForBackup);//changes the selected item text to this
                                                    getSpiner.setEnabled(false);
                                                }

                                                mSaveDataTeknisi.setVisibility(View.GONE);

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
//                        progressDialog.dismiss();
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


    private void updateDataLokasi() {

        String url = BaseUrl.getPublicIp + BaseUrl.updateDataTeknisi;
        Log.i("url", "Data Lokasi: "+ url);
        String hardware = mHardware.getText().toString();
        String sqf = mSqf.getText().toString();
        String esno = mEsno.getText().toString();
        String ctn = mCtn.getText().toString();
        String xpoll = mXPoll.getText().toString();
        String cpi = mCpi.getText().toString();
        String opratorSatelite = mOpratorSatelite.getText().toString();
        String helpDesk = mOpratorHelpDesk.getText().toString();
        String outPln = mOutPln.getText().toString();
        String aktifitasSolusi = mAktifitasSolusi.getText().toString();
        String outUps = mOutUps.getText().toString();
        String suhuRuangan = mSuhuRuangan.getText().toString();
        String upsFormBackup = upsForBackup;
        String typeMounting = mTypeMounting.getText().toString();
        String panjangKabel = mPanjangKabel.getText().toString();
        String letakAntena = mLetakAntena.getText().toString();
        String letakModem = mLetakModem.getText().toString();
        Boolean statusDt = statusDT = true;
        String letakSatelite = mLetakSatelit.getText().toString();
        String analisaProblem = mAnalisaProblem.getText().toString();
        if (hardware.matches("")) {
            mHardware.setError("isi data ini");
        }
        else if (sqf.matches("")){
            mSqf.setError("isi data ini");
        }
        else if (esno.matches("")){
            mEsno.setError("isi data ini");
        }
        else if (ctn.matches("")){
            mCtn.setError("isi data ini");
        }
        else if (xpoll.matches("")){
            mXPoll.setError("isi data ini");
        }
        else if (cpi.matches("")){
            mCpi.setError("isi data ini");
        }
        else if (opratorSatelite.matches("")){
            mOpratorSatelite.setError("isi data ini");
        }
        else if (helpDesk.matches("")){
            mOpratorHelpDesk.setError("isi data ini");
        }
        else if (outPln.matches("")){
            mOutPln.setError("isi data ini");
        }
        else if (aktifitasSolusi.matches("")){
            mAktifitasSolusi.setError("isi data ini");
        }
        else if (outUps.matches("")){
            mOutUps.setError("isi data ini");
        }
        else if (suhuRuangan.matches("")){
            mSuhuRuangan.setError("isi data ini");
        }
        else if (upsFormBackup.matches("")){
            TextView errorText = (TextView) mUpsForBackup.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("isi data ini");//changes the selected item text to this
        }
        else if (typeMounting.matches("")){
            mTypeMounting.setError("isi data ini");
        }
        else if (panjangKabel.matches("")){
            mPanjangKabel.setError("isi data ini");
        }
        else if (letakAntena.matches("")){
            mLetakAntena.setError("isi data ini");
        }
        else if (letakModem.matches("")){
            mLetakModem.setError("isi data ini");
        }
        else if (letakSatelite.matches("")){
            mLetakSatelit.setError("isi data ini");
        }
        else if (analisaProblem.matches("")){
            mAnalisaProblem.setError("isi data ini");
        }
        else {
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
                                    Toasty.success(DataTeknisiActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                    Log.i("TAG","Ski data from server - ok" + response );
                                    Intent intent = new Intent();
                                    intent.putExtra(SharedPrefManager.SP_ID, id);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                                Log.i("TAG","Ski data from server - ok" + response );
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(DataTeknisiActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("TAG","Ski error connect - "+error);
                            Toasty.success(DataTeknisiActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
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
                    String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"NoListTask='"+id+"'\",\"FAIL_HW\": \""+hardware+"\",\"SQF\": \""+sqf+"\",\"INITIAL_ESNO\": \""+esno+"\",\"CARRIER_TO_NOICE\": \""+ctn+"\",\"HasilXPOLL\": \""+xpoll+"\",\"CPI\": \""+cpi+"\",\"OperatorSatelite\": \""+opratorSatelite+"\",\"OperatorHelpDesk\": \""+helpDesk+"\",\"OutPLN\": \""+outPln+"\",\"OutUPS\": \""+outUps+"\",\"UPSforBackup\": \""+upsFormBackup+"\",\"SuhuRuangan\": \""+suhuRuangan+"\",\"TypeMounting\": \""+typeMounting+"\",\"PanjangKabel\": \""+panjangKabel+"\",\"LetakAntena\": \""+letakAntena+"\",\"LetakModem\": \""+letakModem+"\",\"KondisiBangungan\": \""+letakSatelite+"\",\"AnalisaProblem\": \""+analisaProblem+"\",\"AktifitasSolusi\": \""+aktifitasSolusi+"\",\"FlagDataTeknis\": "+statusDt+"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
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


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
