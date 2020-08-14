package selindoalpha.com.selindovsatprod.Activity;

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
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class DataInstallasiActivity extends AppCompatActivity {

    private static final String TAG = "DataInstallasiActivity";

    private EditText mDiameterAntena, mPolarisasi, mElevasi, mAzimuth, mKva, mManagementIP, mSymboleRate, mPln1, mUps1, mGenset1, mPln2, mUps2, mGenset2, mPln3, mUps3, mGenset3, mSateliteLongitude, mIpLan1, mSubnetMask1, mIpLan2, mSubnetMask2;
    private EditText mAlamat1, mSuccess1, mLoss1, mKeterangan1, mAlamat2, mSuccess2, mLoss2, mKeterangan2, mAlamat3, mSuccess3, mLoss3, mKeterangan3;
    private Spinner mSpinnerSourceListrik, mSpinnerKabelRoll, mSpinnerUps , mSpinnerModulation;

    String sourceListrik,kabelRoll,ups,modulation;
    private Button mSaveBtn;
    Boolean statusIL;
    String ResultWS, id, vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_installasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Data Installasi");

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            id = in.getString(SharedPrefManager.SP_ID);
            vid = in.getString(SharedPrefManager.SP_VID);
        }

        mSpinnerSourceListrik = findViewById(R.id.source_listrik);
        mSpinnerKabelRoll = findViewById(R.id.kabel_roll);
        mSpinnerUps = findViewById(R.id.ups);
        mSpinnerModulation = findViewById(R.id.modulation);

        mDiameterAntena = findViewById(R.id.diameter_antena);
        mPolarisasi = findViewById(R.id.polarisasi);
        mElevasi = findViewById(R.id.elevasi);
        mAzimuth = findViewById(R.id.azimuth);
        mKva = findViewById(R.id.kva);
        mManagementIP = findViewById(R.id.ip_management);
        mSymboleRate = findViewById(R.id.receive_symbole);
        mPln1 = findViewById(R.id.pln1);
        mUps1 = findViewById(R.id.ups1);
        mGenset1 = findViewById(R.id.genset1);
        mPln2 = findViewById(R.id.pln2);
        mUps2 = findViewById(R.id.ups2);
        mGenset2 = findViewById(R.id.genset2);
        mPln3 = findViewById(R.id.pln3);
        mUps3 = findViewById(R.id.ups3);
        mGenset3 = findViewById(R.id.genset3);
        mSateliteLongitude = findViewById(R.id.satelite_longitude);
        mIpLan1 = findViewById(R.id.ip_lan);
        mSubnetMask1 = findViewById(R.id.subnet_mask);
        mIpLan2 = findViewById(R.id.ip_lan2);
        mSubnetMask2 = findViewById(R.id.subnet_mask2);
        mAlamat1 = findViewById(R.id.alamat1);
        mSuccess1 = findViewById(R.id.success1);
        mLoss1 = findViewById(R.id.loss1);
        mKeterangan1 = findViewById(R.id.keterangan1);
        mAlamat2 = findViewById(R.id.alamat2);
        mSuccess2 = findViewById(R.id.success2);
        mLoss2 = findViewById(R.id.loss2);
        mKeterangan2 = findViewById(R.id.keterangan2);
        mAlamat3 = findViewById(R.id.alamat3);
        mSuccess3 = findViewById(R.id.success3);
        mLoss3 = findViewById(R.id.loss3);
        mKeterangan3 = findViewById(R.id.keterangan3);
        mSaveBtn = findViewById(R.id.btnUpdateInstallasi);

        new AsyingDataInstallasi().execute();

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataInstallasi();
            }
        });

        mSpinnerSourceListrik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getStr = (TextView) mSpinnerSourceListrik.getSelectedView();
                getStr.setText(mSpinnerSourceListrik.getSelectedItem().toString());
                sourceListrik =  getStr.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerKabelRoll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getStr = (TextView) mSpinnerKabelRoll.getSelectedView();
                getStr.setText(mSpinnerKabelRoll.getSelectedItem().toString());
                kabelRoll =  getStr.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerUps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getStr = (TextView) mSpinnerUps.getSelectedView();
                getStr.setText(mSpinnerUps.getSelectedItem().toString());
                ups =  getStr.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerModulation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getStr = (TextView) mSpinnerModulation.getSelectedView();
                getStr.setText(mSpinnerModulation.getSelectedItem().toString());
                modulation =  getStr.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class AsyingDataInstallasi extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getDataInstallasi();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
//            getListData();
            return "OK";
        }
    }

    public void getDataInstallasi(){
        String url = BaseUrl.getPublicIp + BaseUrl.detailTask+id;
        Log.i(TAG, "NoTask DataInstallasi: " + id);
        Log.i(TAG, "Url DataInstallasi: " + url);
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

                                            String statusDataInstallasi = data.getString("IdStatusPerbaikan");
                                            String statusInstalatsi = data.getString("FlagDataInstallasi");
                                            Log.i(TAG, "FlagDataInstallasi : "+ statusInstalatsi);

                                            Log.i("TAG", "status : "+statusDataInstallasi);
                                            if (statusDataInstallasi.equals("1")) {
                                                Log.i(TAG, "TaskList: "+data);
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("DiameterAntena"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("PolarisasiArahAntena"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("ElevasiArahAntena"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("AzimuthArahAntena"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("KVAUPS"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("IPManagement"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("ReceiveSymboleRate"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("PhaseNetralPLN"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("PhaseNetralUPS"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("PhaseNetralGenset"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("PhaseGroundPLN"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("PhaseGroundUPS"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("PhaseGroundGenset"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("NetralGroundPLN"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("NetralGroundUPS"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("NetralGroundGenset"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("SateliteLongitude"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("IPLAN1"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("subnetmask1"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("IPLAN2"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("subnetmask2"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("HasilTestAlamat1"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("SuccessTest1"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("LossTest1"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("KeteranganTest1"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("HasilTestAlamat2"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("SuccessTest2"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("LossTest2"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("KeteranganTest2"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("HasilTestAlamat3"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("SuccessTest3"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("LossTest3"));
                                                Log.i(TAG, "data_Installasi_Open: "+ data.getString("KeteranganTest3"));

                                                if (data.getString("DiameterAntena").equals("null") || data.getString("DiameterAntena").equals(null)|| data.getString("DiameterAntena").equals("")){
                                                    String diameterAntena = "";
                                                    mDiameterAntena.setText(diameterAntena);
                                                }else {
                                                    String diameterAntena = data.getString("DiameterAntena");
                                                    mDiameterAntena.setText(diameterAntena);
                                                }
                                                if (data.getString("PolarisasiArahAntena").equals("null") || data.getString("PolarisasiArahAntena").equals(null)|| data.getString("PolarisasiArahAntena").equals("")){
                                                    String polarisasiArahAntena = "";
                                                    mPolarisasi.setText(polarisasiArahAntena);
                                                }else {
                                                    String polarisasiArahAntena = data.getString("PolarisasiArahAntena");
                                                    mPolarisasi.setText(polarisasiArahAntena);
                                                }
                                                if (data.getString("ElevasiArahAntena").equals("null") || data.getString("ElevasiArahAntena").equals(null)|| data.getString("ElevasiArahAntena").equals("")){
                                                    String elevasiArahAntena = "";
                                                    mElevasi.setText(elevasiArahAntena);
                                                }else {
                                                    String elevasiArahAntena = data.getString("ElevasiArahAntena");
                                                    mElevasi.setText(elevasiArahAntena);
                                                }
                                                if (data.getString("AzimuthArahAntena").equals("null") || data.getString("AzimuthArahAntena").equals(null)|| data.getString("AzimuthArahAntena").equals("")){
                                                    String azimuthArahAntena = "";
                                                    mAzimuth.setText(azimuthArahAntena);
                                                }else {
                                                    String azimuthArahAntena = data.getString("AzimuthArahAntena");
                                                    mAzimuth.setText(azimuthArahAntena);
                                                }
                                                if (data.getString("KVAUPS").equals("null") || data.getString("KVAUPS").equals(null)|| data.getString("KVAUPS").equals("")){
                                                    String kva = "";
                                                    mKva.setText(kva);
                                                }else {
                                                    String kva = data.getString("KVAUPS");
                                                    mKva.setText(kva);
                                                }
                                                if (data.getString("IPManagement").equals("null") || data.getString("IPManagement").equals(null)|| data.getString("IPManagement").equals("")){
                                                    String ipManagement = "";
                                                    mManagementIP.setText(ipManagement);
                                                }else {
                                                    String ipManagement = data.getString("IPManagement");
                                                    mManagementIP.setText(ipManagement);
                                                }
                                                if (data.getString("ReceiveSymboleRate").equals("null") || data.getString("ReceiveSymboleRate").equals(null)|| data.getString("ReceiveSymboleRate").equals("")){
                                                    String azimuthArahAntena = "";
                                                    mAzimuth.setText(azimuthArahAntena);
                                                }else {
                                                    String receiveSymboleRate = data.getString("ReceiveSymboleRate");
                                                    mSymboleRate.setText(receiveSymboleRate);
                                                }
                                                if (data.getString("PhaseNetralPLN").equals("null") || data.getString("PhaseNetralPLN").equals(null)|| data.getString("PhaseNetralPLN").equals("")){
                                                    String phaseNetralPLN = "";
                                                    mPln1.setText(phaseNetralPLN);
                                                }else {
                                                    String phaseNetralPLN = data.getString("PhaseNetralPLN");
                                                    mPln1.setText(phaseNetralPLN);
                                                }
                                                if (data.getString("PhaseNetralUPS").equals("null") || data.getString("PhaseNetralUPS").equals(null)|| data.getString("PhaseNetralUPS").equals("")){
                                                    String phaseNetralUPS = "";
                                                    mUps1.setText(phaseNetralUPS);
                                                }else {
                                                    String phaseNetralUPS = data.getString("PhaseNetralUPS");
                                                    mUps1.setText(phaseNetralUPS);
                                                }
                                                if (data.getString("PhaseNetralGenset").equals("null") || data.getString("PhaseNetralGenset").equals(null)|| data.getString("PhaseNetralGenset").equals("")){
                                                    String phaseNetralGenset = "";
                                                    mGenset1.setText(phaseNetralGenset);
                                                }else {
                                                    String phaseNetralGenset = data.getString("PhaseNetralGenset");
                                                    mGenset1.setText(phaseNetralGenset);
                                                }
                                                if (data.getString("PhaseGroundUPS").equals("null") || data.getString("PhaseGroundUPS").equals(null)|| data.getString("PhaseGroundUPS").equals("")){
                                                    String phaseGroundUPS = "";
                                                    mUps2.setText(phaseGroundUPS);
                                                }else {
                                                    String phaseGroundUPS = data.getString("PhaseGroundUPS");
                                                    mUps2.setText(phaseGroundUPS);
                                                }
                                                if (data.getString("PhaseGroundGenset").equals("null") || data.getString("PhaseGroundGenset").equals(null)|| data.getString("PhaseGroundGenset").equals("")){
                                                    String phaseGroundGenset = "";
                                                    mGenset2.setText(phaseGroundGenset);
                                                }else {
                                                    String phaseGroundGenset = data.getString("PhaseGroundGenset");
                                                    mGenset2.setText(phaseGroundGenset);
                                                }
                                                if (data.getString("PhaseNetralGenset").equals("null") || data.getString("PhaseNetralGenset").equals(null)|| data.getString("PhaseNetralGenset").equals("")){
                                                    String phaseNetralGenset = "";
                                                    mGenset1.setText(phaseNetralGenset);
                                                }else {
                                                    String phaseNetralGenset = data.getString("PhaseNetralGenset");
                                                    mGenset1.setText(phaseNetralGenset);
                                                }
                                                if (data.getString("NetralGroundPLN").equals("null") || data.getString("NetralGroundPLN").equals(null)|| data.getString("NetralGroundPLN").equals("")){
                                                    String netralGroundPLN = "";
                                                    mPln3.setText(netralGroundPLN);
                                                }else {
                                                    String netralGroundPLN = data.getString("NetralGroundPLN");
                                                    mPln3.setText(netralGroundPLN);
                                                }
                                                if (data.getString("NetralGroundUPS").equals("null") || data.getString("NetralGroundUPS").equals(null)|| data.getString("NetralGroundUPS").equals("")){
                                                    String netralGroundUPS = "";
                                                    mUps3.setText(netralGroundUPS);
                                                }else {
                                                    String netralGroundUPS = data.getString("NetralGroundUPS");
                                                    mUps3.setText(netralGroundUPS);
                                                }
                                                if (data.getString("NetralGroundGenset").equals("null") || data.getString("NetralGroundGenset").equals(null)|| data.getString("NetralGroundGenset").equals("")){
                                                    String netralGroundGenset = "";
                                                    mGenset3.setText(netralGroundGenset);
                                                }else {
                                                    String netralGroundGenset = data.getString("NetralGroundGenset");
                                                    mGenset3.setText(netralGroundGenset);
                                                }
                                                if (data.getString("SateliteLongitude").equals("null") || data.getString("SateliteLongitude").equals(null)|| data.getString("SateliteLongitude").equals("")){
                                                    String sateliteLongitude = "";
                                                    mSateliteLongitude.setText(sateliteLongitude);
                                                }else {
                                                    String sateliteLongitude = data.getString("SateliteLongitude");
                                                    mSateliteLongitude.setText(sateliteLongitude);
                                                }
                                                if (data.getString("IPLAN1").equals("null") || data.getString("IPLAN1").equals(null)|| data.getString("IPLAN1").equals("")){
                                                    String iplan1 = "";
                                                    mIpLan1.setText(iplan1);
                                                }else {
                                                    String iplan1 = data.getString("IPLAN1");
                                                    mIpLan1.setText(iplan1);
                                                }
                                                if (data.getString("subnetmask1").equals("null") || data.getString("subnetmask1").equals(null)|| data.getString("subnetmask1").equals("")){
                                                    String subnetmask1 = "";
                                                    mSubnetMask1.setText(subnetmask1);
                                                }else {
                                                    String subnetmask1 = data.getString("subnetmask1");
                                                    mSubnetMask1.setText(subnetmask1);
                                                }
                                                if (data.getString("IPLAN2").equals("null") || data.getString("IPLAN2").equals(null)|| data.getString("IPLAN2").equals("")){
                                                    String iplan2 = "";
                                                    mIpLan2.setText(iplan2);
                                                }else {
                                                    String iplan2 = data.getString("IPLAN2");
                                                    mIpLan2.setText(iplan2);
                                                }
                                                if (data.getString("subnetmask2").equals("null") || data.getString("subnetmask2").equals(null)|| data.getString("subnetmask2").equals("")){
                                                    String subnetmask2 = "";
                                                    mSubnetMask2.setText(subnetmask2);
                                                }else {
                                                    String subnetmask2 = data.getString("subnetmask2");
                                                    mSubnetMask2.setText(subnetmask2);
                                                }
                                                if (data.getString("HasilTestAlamat1").equals("null") || data.getString("HasilTestAlamat1").equals(null)|| data.getString("HasilTestAlamat1").equals("")){
                                                    String hasilTestAlamat1 = "";
                                                    mAlamat1.setText(hasilTestAlamat1);
                                                }else {
                                                    String hasilTestAlamat1 = data.getString("HasilTestAlamat1");
                                                    mAlamat1.setText(hasilTestAlamat1);
                                                }
                                                if (data.getString("SuccessTest1").equals("null") || data.getString("SuccessTest1").equals(null)|| data.getString("SuccessTest1").equals("")){
                                                    String successTest1 = "";
                                                    mSuccess1.setText(successTest1);
                                                }else {
                                                    String successTest1 = data.getString("SuccessTest1");
                                                    mSuccess1.setText(successTest1);
                                                }
                                                if (data.getString("LossTest1").equals("null") || data.getString("LossTest1").equals(null)|| data.getString("LossTest1").equals("")){
                                                    String lossTest1 = "";
                                                    mLoss1.setText(lossTest1);
                                                }else {
                                                    String lossTest1 = data.getString("LossTest1");
                                                    mLoss1.setText(lossTest1);
                                                }
                                                if (data.getString("KeteranganTest1").equals("null") || data.getString("KeteranganTest1").equals(null)|| data.getString("KeteranganTest1").equals("")){
                                                    String keteranganTest1 = "";
                                                    mKeterangan1.setText(keteranganTest1);
                                                }else {
                                                    String keteranganTest1 = data.getString("KeteranganTest1");
                                                    mKeterangan1.setText(keteranganTest1);
                                                }
                                                if (data.getString("HasilTestAlamat2").equals("null") || data.getString("HasilTestAlamat2").equals(null)|| data.getString("HasilTestAlamat2").equals("")){
                                                    String hasilTestAlamat2 = "";
                                                    mAlamat2.setText(hasilTestAlamat2);
                                                }else {
                                                    String hasilTestAlamat2 = data.getString("HasilTestAlamat2");
                                                    mAlamat2.setText(hasilTestAlamat2);
                                                }
                                                if (data.getString("SuccessTest2").equals("null") || data.getString("SuccessTest2").equals(null)|| data.getString("SuccessTest2").equals("")){
                                                    String successTest2 = "";
                                                    mSuccess2.setText(successTest2);
                                                }else {
                                                    String successTest2 = data.getString("SuccessTest2");
                                                    mSuccess2.setText(successTest2);
                                                }
                                                if (data.getString("LossTest2").equals("null") || data.getString("LossTest2").equals(null)|| data.getString("LossTest2").equals("")){
                                                    String lossTest2 = "";
                                                    mLoss2.setText(lossTest2);
                                                }else {
                                                    String lossTest2 = data.getString("LossTest2");
                                                    mLoss2.setText(lossTest2);
                                                }
                                                if (data.getString("KeteranganTest2").equals("null") || data.getString("KeteranganTest2").equals(null)|| data.getString("KeteranganTest2").equals("")){
                                                    String keteranganTest2 = "";
                                                    mKeterangan2.setText(keteranganTest2);
                                                }else {
                                                    String keteranganTest2 = data.getString("KeteranganTest2");
                                                    mKeterangan2.setText(keteranganTest2);
                                                }
                                                if (data.getString("HasilTestAlamat3").equals("null") || data.getString("HasilTestAlamat3").equals(null)|| data.getString("HasilTestAlamat3").equals("")){
                                                    String hasilTestAlamat3 = "";
                                                    mAlamat3.setText(hasilTestAlamat3);
                                                }else {
                                                    String hasilTestAlamat3 = data.getString("HasilTestAlamat3");
                                                    mAlamat3.setText(hasilTestAlamat3);
                                                }
                                                if (data.getString("SuccessTest3").equals("null") || data.getString("SuccessTest3").equals(null)|| data.getString("SuccessTest3").equals("")){
                                                    String successTest3 = "";
                                                    mSuccess3.setText(successTest3);
                                                }else {
                                                    String successTest3 = data.getString("SuccessTest3");
                                                    mSuccess3.setText(successTest3);
                                                }
                                                if (data.getString("LossTest3").equals("null") || data.getString("LossTest3").equals(null)|| data.getString("LossTest3").equals("")){
                                                    String lossTest3 = "";
                                                    mLoss3.setText(lossTest3);
                                                }else {
                                                    String lossTest3 = data.getString("LossTest3");
                                                    mLoss3.setText(lossTest3);
                                                }
                                                if (data.getString("KeteranganTest3").equals("null") || data.getString("KeteranganTest3").equals(null)|| data.getString("KeteranganTest3").equals("")){
                                                    String keteranganTest3 = "";
                                                    mKeterangan3.setText(keteranganTest3);
                                                }else {
                                                    String keteranganTest3 = data.getString("KeteranganTest3");
                                                    mKeterangan3.setText(keteranganTest3);
                                                }
                                                if (data.getString("SourceListrik").equals("null") || data.getString("SourceListrik").equals(null)|| data.getString("SourceListrik").equals("")){
                                                    sourceListrik = "";
                                                    TextView getStrSourceListrik = (TextView) mSpinnerSourceListrik.getSelectedView();
                                                    getStrSourceListrik.setText(sourceListrik);//changes the selected item text to this
                                                }else {
                                                    sourceListrik = data.getString("SourceListrik");
                                                    TextView getStrSourceListrik = (TextView) mSpinnerSourceListrik.getSelectedView();
                                                    getStrSourceListrik.setText(sourceListrik);//changes the selected item text to this
                                                }
                                                if (data.getString("KabelRoll").equals("null") || data.getString("KabelRoll").equals(null)|| data.getString("KabelRoll").equals("")){
                                                    kabelRoll = "";
                                                    TextView getStrKabelRoll = (TextView) mSpinnerKabelRoll.getSelectedView();
                                                    getStrKabelRoll.setText(kabelRoll);//changes the selected item text to this
                                                }else {
                                                    kabelRoll = data.getString("KabelRoll");
                                                    TextView getStrKabelRoll = (TextView) mSpinnerKabelRoll.getSelectedView();
                                                    getStrKabelRoll.setText(kabelRoll);//changes the selected item text to this
                                                }
                                                if (data.getString("PerangkatkeUPS").equals("null") || data.getString("PerangkatkeUPS").equals(null)|| data.getString("PerangkatkeUPS").equals("")){
                                                    ups = "";
                                                    TextView getStrUps = (TextView) mSpinnerUps.getSelectedView();
                                                    getStrUps.setText(ups);//changes the selected item text to this
                                                }else {
                                                    ups = data.getString("PerangkatkeUPS");
                                                    TextView getStrUps = (TextView) mSpinnerUps.getSelectedView();
                                                    getStrUps.setText(ups);//changes the selected item text to this
                                                }
                                                if (data.getString("FrequencyBandModulation").equals("null") || data.getString("FrequencyBandModulation").equals(null)|| data.getString("FrequencyBandModulation").equals("")){
                                                    modulation = "";
                                                    TextView getStrModulation = (TextView) mSpinnerModulation.getSelectedView();
                                                    getStrModulation.setText(modulation);//changes the selected item text to this
                                                }else {
                                                    modulation = data.getString("FrequencyBandModulation");
                                                    TextView getStrModulation = (TextView) mSpinnerModulation.getSelectedView();
                                                    getStrModulation.setText(modulation);//changes the selected item text to this
                                                }
                                            }

                                            if (statusDataInstallasi.equals("4")) {
                                                Log.i(TAG, "TaskList: "+data);
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("DiameterAntena"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("PolarisasiArahAntena"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("ElevasiArahAntena"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("AzimuthArahAntena"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("KVAUPS"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("IPManagement"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("ReceiveSymboleRate"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("PhaseNetralPLN"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("PhaseNetralUPS"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("PhaseNetralGenset"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("PhaseGroundPLN"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("PhaseGroundUPS"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("PhaseGroundGenset"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("NetralGroundPLN"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("NetralGroundUPS"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("NetralGroundGenset"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("SateliteLongitude"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("IPLAN1"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("subnetmask1"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("IPLAN2"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("subnetmask2"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("HasilTestAlamat1"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("SuccessTest1"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("LossTest1"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("KeteranganTest1"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("HasilTestAlamat2"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("SuccessTest2"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("LossTest2"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("KeteranganTest2"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("HasilTestAlamat3"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("SuccessTest3"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("LossTest3"));
                                                Log.i(TAG, "data_Installasi_Finish: "+ data.getString("KeteranganTest3"));

                                                if (data.getString("DiameterAntena").equals("null") || data.getString("DiameterAntena").equals(null)|| data.getString("DiameterAntena").equals("")){
                                                    String diameterAntena = "";
                                                    mDiameterAntena.setText(diameterAntena);
                                                    mDiameterAntena.setEnabled(false);
                                                }else {
                                                    String diameterAntena = data.getString("DiameterAntena");
                                                    mDiameterAntena.setText(diameterAntena);
                                                    mDiameterAntena.setEnabled(false);
                                                }
                                                if (data.getString("PolarisasiArahAntena").equals("null") || data.getString("PolarisasiArahAntena").equals(null)|| data.getString("PolarisasiArahAntena").equals("")){
                                                    String polarisasiArahAntena = "";
                                                    mPolarisasi.setText(polarisasiArahAntena);
                                                    mPolarisasi.setEnabled(false);
                                                }else {
                                                    String polarisasiArahAntena = data.getString("PolarisasiArahAntena");
                                                    mPolarisasi.setText(polarisasiArahAntena);
                                                    mPolarisasi.setEnabled(false);
                                                }
                                                if (data.getString("ElevasiArahAntena").equals("null") || data.getString("ElevasiArahAntena").equals(null)|| data.getString("ElevasiArahAntena").equals("")){
                                                    String elevasiArahAntena = "";
                                                    mElevasi.setText(elevasiArahAntena);
                                                    mElevasi.setEnabled(false);
                                                }else {
                                                    String elevasiArahAntena = data.getString("ElevasiArahAntena");
                                                    mElevasi.setText(elevasiArahAntena);
                                                    mElevasi.setEnabled(false);
                                                }
                                                if (data.getString("AzimuthArahAntena").equals("null") || data.getString("AzimuthArahAntena").equals(null)|| data.getString("AzimuthArahAntena").equals("")){
                                                    String azimuthArahAntena = "";
                                                    mAzimuth.setText(azimuthArahAntena);
                                                    mAzimuth.setEnabled(false);
                                                }else {
                                                    String azimuthArahAntena = data.getString("AzimuthArahAntena");
                                                    mAzimuth.setText(azimuthArahAntena);
                                                    mAzimuth.setEnabled(false);
                                                }
                                                if (data.getString("KVAUPS").equals("null") || data.getString("KVAUPS").equals(null)|| data.getString("KVAUPS").equals("")){
                                                    String kva = "";
                                                    mKva.setText(kva);
                                                    mKva.setEnabled(false);
                                                }else {
                                                    String kva = data.getString("KVAUPS");
                                                    mKva.setText(kva);
                                                    mKva.setEnabled(false);
                                                }
                                                if (data.getString("IPManagement").equals("null") || data.getString("IPManagement").equals(null)|| data.getString("IPManagement").equals("")){
                                                    String ipManagement = "";
                                                    mManagementIP.setText(ipManagement);
                                                    mManagementIP.setEnabled(false);
                                                }else {
                                                    String ipManagement = data.getString("IPManagement");
                                                    mManagementIP.setText(ipManagement);
                                                    mManagementIP.setEnabled(false);
                                                }
                                                if (data.getString("ReceiveSymboleRate").equals("null") || data.getString("ReceiveSymboleRate").equals(null)|| data.getString("ReceiveSymboleRate").equals("")){
                                                    String azimuthArahAntena = "";
                                                    mAzimuth.setText(azimuthArahAntena);
                                                    mAzimuth.setEnabled(false);
                                                }else {
                                                    String receiveSymboleRate = data.getString("ReceiveSymboleRate");
                                                    mSymboleRate.setText(receiveSymboleRate);
                                                }
                                                if (data.getString("PhaseNetralPLN").equals("null") || data.getString("PhaseNetralPLN").equals(null)|| data.getString("PhaseNetralPLN").equals("")){
                                                    String phaseNetralPLN = "";
                                                    mPln1.setText(phaseNetralPLN);
                                                    mPln1.setEnabled(false);
                                                }else {
                                                    String phaseNetralPLN = data.getString("PhaseNetralPLN");
                                                    mPln1.setText(phaseNetralPLN);
                                                    mPln1.setEnabled(false);
                                                }
                                                if (data.getString("PhaseNetralUPS").equals("null") || data.getString("PhaseNetralUPS").equals(null)|| data.getString("PhaseNetralUPS").equals("")){
                                                    String phaseNetralUPS = "";
                                                    mUps1.setText(phaseNetralUPS);
                                                    mUps1.setEnabled(false);
                                                }else {
                                                    String phaseNetralUPS = data.getString("PhaseNetralUPS");
                                                    mUps1.setText(phaseNetralUPS);
                                                    mUps1.setEnabled(false);
                                                }
                                                if (data.getString("PhaseNetralGenset").equals("null") || data.getString("PhaseNetralGenset").equals(null)|| data.getString("PhaseNetralGenset").equals("")){
                                                    String phaseNetralGenset = "";
                                                    mGenset1.setText(phaseNetralGenset);
                                                    mGenset1.setEnabled(false);
                                                }else {
                                                    String phaseNetralGenset = data.getString("PhaseNetralGenset");
                                                    mGenset1.setText(phaseNetralGenset);
                                                    mGenset1.setEnabled(false);
                                                }
                                                if (data.getString("PhaseGroundUPS").equals("null") || data.getString("PhaseGroundUPS").equals(null)|| data.getString("PhaseGroundUPS").equals("")){
                                                    String phaseGroundUPS = "";
                                                    mUps2.setText(phaseGroundUPS);
                                                    mUps2.setEnabled(false);
                                                }else {
                                                    String phaseGroundUPS = data.getString("PhaseGroundUPS");
                                                    mUps2.setText(phaseGroundUPS);
                                                    mUps2.setEnabled(false);
                                                }
                                                if (data.getString("PhaseGroundGenset").equals("null") || data.getString("PhaseGroundGenset").equals(null)|| data.getString("PhaseGroundGenset").equals("")){
                                                    String phaseGroundGenset = "";
                                                    mGenset2.setText(phaseGroundGenset);
                                                    mGenset2.setEnabled(false);
                                                }else {
                                                    String phaseGroundGenset = data.getString("PhaseGroundGenset");
                                                    mGenset2.setText(phaseGroundGenset);
                                                    mGenset2.setEnabled(false);
                                                }
                                                if (data.getString("PhaseNetralGenset").equals("null") || data.getString("PhaseNetralGenset").equals(null)|| data.getString("PhaseNetralGenset").equals("")){
                                                    String phaseNetralGenset = "";
                                                    mGenset1.setText(phaseNetralGenset);
                                                    mGenset1.setEnabled(false);
                                                }else {
                                                    String phaseNetralGenset = data.getString("PhaseNetralGenset");
                                                    mGenset1.setText(phaseNetralGenset);
                                                    mGenset1.setEnabled(false);
                                                }
                                                if (data.getString("NetralGroundPLN").equals("null") || data.getString("NetralGroundPLN").equals(null)|| data.getString("NetralGroundPLN").equals("")){
                                                    String netralGroundPLN = "";
                                                    mPln3.setText(netralGroundPLN);
                                                    mPln3.setEnabled(false);
                                                }else {
                                                    String netralGroundPLN = data.getString("NetralGroundPLN");
                                                    mPln3.setText(netralGroundPLN);
                                                    mPln3.setEnabled(false);
                                                }
                                                if (data.getString("NetralGroundUPS").equals("null") || data.getString("NetralGroundUPS").equals(null)|| data.getString("NetralGroundUPS").equals("")){
                                                    String netralGroundUPS = "";
                                                    mUps3.setText(netralGroundUPS);
                                                    mUps3.setEnabled(false);
                                                }else {
                                                    String netralGroundUPS = data.getString("NetralGroundUPS");
                                                    mUps3.setText(netralGroundUPS);
                                                    mUps3.setEnabled(false);
                                                }
                                                if (data.getString("NetralGroundGenset").equals("null") || data.getString("NetralGroundGenset").equals(null)|| data.getString("NetralGroundGenset").equals("")){
                                                    String netralGroundGenset = "";
                                                    mGenset3.setText(netralGroundGenset);
                                                    mGenset3.setEnabled(false);
                                                }else {
                                                    String netralGroundGenset = data.getString("NetralGroundGenset");
                                                    mGenset3.setText(netralGroundGenset);
                                                    mGenset3.setEnabled(false);
                                                }
                                                if (data.getString("SateliteLongitude").equals("null") || data.getString("SateliteLongitude").equals(null)|| data.getString("SateliteLongitude").equals("")){
                                                    String sateliteLongitude = "";
                                                    mSateliteLongitude.setText(sateliteLongitude);
                                                    mSateliteLongitude.setEnabled(false);
                                                }else {
                                                    String sateliteLongitude = data.getString("SateliteLongitude");
                                                    mSateliteLongitude.setText(sateliteLongitude);
                                                    mSateliteLongitude.setEnabled(false);
                                                }
                                                if (data.getString("IPLAN1").equals("null") || data.getString("IPLAN1").equals(null)|| data.getString("IPLAN1").equals("")){
                                                    String iplan1 = "";
                                                    mIpLan1.setText(iplan1);
                                                    mIpLan1.setEnabled(false);
                                                }else {
                                                    String iplan1 = data.getString("IPLAN1");
                                                    mIpLan1.setText(iplan1);
                                                    mIpLan1.setEnabled(false);
                                                }
                                                if (data.getString("subnetmask1").equals("null") || data.getString("subnetmask1").equals(null)|| data.getString("subnetmask1").equals("")){
                                                    String subnetmask1 = "";
                                                    mSubnetMask1.setText(subnetmask1);
                                                    mSubnetMask1.setEnabled(false);
                                                }else {
                                                    String subnetmask1 = data.getString("subnetmask1");
                                                    mSubnetMask1.setText(subnetmask1);
                                                    mSubnetMask1.setEnabled(false);
                                                }
                                                if (data.getString("IPLAN2").equals("null") || data.getString("IPLAN2").equals(null)|| data.getString("IPLAN2").equals("")){
                                                    String iplan2 = "";
                                                    mIpLan2.setText(iplan2);
                                                    mIpLan2.setEnabled(false);
                                                }else {
                                                    String iplan2 = data.getString("IPLAN2");
                                                    mIpLan2.setText(iplan2);
                                                    mIpLan2.setEnabled(false);
                                                }
                                                if (data.getString("subnetmask2").equals("null") || data.getString("subnetmask2").equals(null)|| data.getString("subnetmask2").equals("")){
                                                    String subnetmask2 = "";
                                                    mSubnetMask2.setText(subnetmask2);
                                                    mSubnetMask2.setEnabled(false);
                                                }else {
                                                    String subnetmask2 = data.getString("subnetmask2");
                                                    mSubnetMask2.setText(subnetmask2);
                                                    mSubnetMask2.setEnabled(false);
                                                }
                                                if (data.getString("HasilTestAlamat1").equals("null") || data.getString("HasilTestAlamat1").equals(null)|| data.getString("HasilTestAlamat1").equals("")){
                                                    String hasilTestAlamat1 = "";
                                                    mAlamat1.setText(hasilTestAlamat1);
                                                    mAlamat1.setEnabled(false);
                                                }else {
                                                    String hasilTestAlamat1 = data.getString("HasilTestAlamat1");
                                                    mAlamat1.setText(hasilTestAlamat1);
                                                    mAlamat1.setEnabled(false);
                                                }
                                                if (data.getString("SuccessTest1").equals("null") || data.getString("SuccessTest1").equals(null)|| data.getString("SuccessTest1").equals("")){
                                                    String successTest1 = "";
                                                    mSuccess1.setText(successTest1);
                                                    mSuccess1.setEnabled(false);
                                                }else {
                                                    String successTest1 = data.getString("SuccessTest1");
                                                    mSuccess1.setText(successTest1);
                                                    mSuccess1.setEnabled(false);
                                                }
                                                if (data.getString("LossTest1").equals("null") || data.getString("LossTest1").equals(null)|| data.getString("LossTest1").equals("")){
                                                    String lossTest1 = "";
                                                    mLoss1.setText(lossTest1);
                                                    mLoss1.setEnabled(false);
                                                }else {
                                                    String lossTest1 = data.getString("LossTest1");
                                                    mLoss1.setText(lossTest1);
                                                    mLoss1.setEnabled(false);
                                                }
                                                if (data.getString("KeteranganTest1").equals("null") || data.getString("KeteranganTest1").equals(null)|| data.getString("KeteranganTest1").equals("")){
                                                    String keteranganTest1 = "";
                                                    mKeterangan1.setText(keteranganTest1);
                                                    mKeterangan1.setEnabled(false);
                                                }else {
                                                    String keteranganTest1 = data.getString("KeteranganTest1");
                                                    mKeterangan1.setText(keteranganTest1);
                                                    mKeterangan1.setEnabled(false);
                                                }
                                                if (data.getString("HasilTestAlamat2").equals("null") || data.getString("HasilTestAlamat2").equals(null)|| data.getString("HasilTestAlamat2").equals("")){
                                                    String hasilTestAlamat2 = "";
                                                    mAlamat2.setText(hasilTestAlamat2);
                                                    mAlamat2.setEnabled(false);
                                                }else {
                                                    String hasilTestAlamat2 = data.getString("HasilTestAlamat2");
                                                    mAlamat2.setText(hasilTestAlamat2);
                                                    mAlamat2.setEnabled(false);
                                                }
                                                if (data.getString("SuccessTest2").equals("null") || data.getString("SuccessTest2").equals(null)|| data.getString("SuccessTest2").equals("")){
                                                    String successTest2 = "";
                                                    mSuccess2.setText(successTest2);
                                                    mSuccess2.setEnabled(false);
                                                }else {
                                                    String successTest2 = data.getString("SuccessTest2");
                                                    mSuccess2.setText(successTest2);
                                                    mSuccess2.setEnabled(false);
                                                }
                                                if (data.getString("LossTest2").equals("null") || data.getString("LossTest2").equals(null)|| data.getString("LossTest2").equals("")){
                                                    String lossTest2 = "";
                                                    mLoss2.setText(lossTest2);
                                                    mLoss2.setEnabled(false);
                                                }else {
                                                    String lossTest2 = data.getString("LossTest2");
                                                    mLoss2.setText(lossTest2);
                                                    mLoss2.setEnabled(false);
                                                }
                                                if (data.getString("KeteranganTest2").equals("null") || data.getString("KeteranganTest2").equals(null)|| data.getString("KeteranganTest2").equals("")){
                                                    String keteranganTest2 = "";
                                                    mKeterangan2.setText(keteranganTest2);
                                                    mKeterangan2.setEnabled(false);
                                                }else {
                                                    String keteranganTest2 = data.getString("KeteranganTest2");
                                                    mKeterangan2.setText(keteranganTest2);
                                                    mKeterangan2.setEnabled(false);
                                                }
                                                if (data.getString("HasilTestAlamat3").equals("null") || data.getString("HasilTestAlamat3").equals(null)|| data.getString("HasilTestAlamat3").equals("")){
                                                    String hasilTestAlamat3 = "";
                                                    mAlamat3.setText(hasilTestAlamat3);
                                                    mAlamat3.setEnabled(false);
                                                }else {
                                                    String hasilTestAlamat3 = data.getString("HasilTestAlamat3");
                                                    mAlamat3.setText(hasilTestAlamat3);
                                                    mAlamat3.setEnabled(false);
                                                }
                                                if (data.getString("SuccessTest3").equals("null") || data.getString("SuccessTest3").equals(null)|| data.getString("SuccessTest3").equals("")){
                                                    String successTest3 = "";
                                                    mSuccess3.setText(successTest3);
                                                    mSuccess3.setEnabled(false);
                                                }else {
                                                    String successTest3 = data.getString("SuccessTest3");
                                                    mSuccess3.setText(successTest3);
                                                    mSuccess3.setEnabled(false);
                                                }
                                                if (data.getString("LossTest3").equals("null") || data.getString("LossTest3").equals(null)|| data.getString("LossTest3").equals("")){
                                                    String lossTest3 = "";
                                                    mLoss3.setText(lossTest3);
                                                    mLoss3.setEnabled(false);
                                                }else {
                                                    String lossTest3 = data.getString("LossTest3");
                                                    mLoss3.setText(lossTest3);
                                                    mLoss3.setEnabled(false);
                                                }
                                                if (data.getString("KeteranganTest3").equals("null") || data.getString("KeteranganTest3").equals(null)|| data.getString("KeteranganTest3").equals("")){
                                                    String keteranganTest3 = "";
                                                    mKeterangan3.setText(keteranganTest3);
                                                    mKeterangan3.setEnabled(false);
                                                }else {
                                                    String keteranganTest3 = data.getString("KeteranganTest3");
                                                    mKeterangan3.setText(keteranganTest3);
                                                    mKeterangan3.setEnabled(false);
                                                }
                                                if (data.getString("SourceListrik").equals("null") || data.getString("SourceListrik").equals(null)|| data.getString("SourceListrik").equals("")){
                                                    sourceListrik = "";
                                                    TextView getStrSourceListrik = (TextView) mSpinnerSourceListrik.getSelectedView();
                                                    getStrSourceListrik.setText(sourceListrik);//changes the selected item text to this
                                                    getStrSourceListrik.setEnabled(false);
                                                }else {
                                                    sourceListrik = data.getString("SourceListrik");
                                                    TextView getStrSourceListrik = (TextView) mSpinnerSourceListrik.getSelectedView();
                                                    getStrSourceListrik.setText(sourceListrik);//changes the selected item text to this
                                                    getStrSourceListrik.setEnabled(false);
                                                }
                                                if (data.getString("KabelRoll").equals("null") || data.getString("KabelRoll").equals(null)|| data.getString("KabelRoll").equals("")){
                                                    kabelRoll = "";
                                                    TextView getStrKabelRoll = (TextView) mSpinnerKabelRoll.getSelectedView();
                                                    getStrKabelRoll.setText(kabelRoll);//changes the selected item text to this
                                                    getStrKabelRoll.setEnabled(false);
                                                }else {
                                                    kabelRoll = data.getString("KabelRoll");
                                                    TextView getStrKabelRoll = (TextView) mSpinnerKabelRoll.getSelectedView();
                                                    getStrKabelRoll.setText(kabelRoll);//changes the selected item text to this
                                                    getStrKabelRoll.setEnabled(false);
                                                }
                                                if (data.getString("PerangkatkeUPS").equals("null") || data.getString("PerangkatkeUPS").equals(null)|| data.getString("PerangkatkeUPS").equals("")){
                                                    ups = "";
                                                    TextView getStrUps = (TextView) mSpinnerUps.getSelectedView();
                                                    getStrUps.setText(ups);//changes the selected item text to this
                                                    getStrUps.setEnabled(false);
                                                }else {
                                                    ups = data.getString("PerangkatkeUPS");
                                                    TextView getStrUps = (TextView) mSpinnerUps.getSelectedView();
                                                    getStrUps.setText(ups);//changes the selected item text to this
                                                    getStrUps.setEnabled(false);
                                                }
                                                if (data.getString("FrequencyBandModulation").equals("null") || data.getString("FrequencyBandModulation").equals(null)|| data.getString("FrequencyBandModulation").equals("")){
                                                    modulation = "";
                                                    TextView getStrModulation = (TextView) mSpinnerModulation.getSelectedView();
                                                    getStrModulation.setText(modulation);//changes the selected item text to this
                                                    getStrModulation.setEnabled(false);
                                                }else {
                                                    modulation = data.getString("FrequencyBandModulation");
                                                    TextView getStrModulation = (TextView) mSpinnerModulation.getSelectedView();
                                                    getStrModulation.setText(modulation);//changes the selected item text to this
                                                    getStrModulation.setEnabled(false);
                                                }
                                                mSaveBtn.setVisibility(View.GONE);
                                            }
                                        }

                                    }else {
                                        String Responsecode = jsonObj.getString("Data1");
                                        Log.i(TAG, "else Response True: " + Responsecode);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
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


    private void updateDataInstallasi() {
        String url = BaseUrl.getPublicIp + BaseUrl.updateDataInstalasi;
        Log.i("url", "DataInstalasi: " + url);
        Log.i("GetVID", "updateDataInstalasi: " + id);
        String diameterAntena = mDiameterAntena.getText().toString();
        String polarisasi = mPolarisasi.getText().toString();
        String elevasi = mElevasi.getText().toString();
        String azimuth = mAzimuth.getText().toString();
        String kva = mKva.getText().toString();
        String managementIP = mManagementIP.getText().toString();
        String symboleRate = mSymboleRate.getText().toString();
        String pln1 = mPln1.getText().toString();
        String ups1 = mUps1.getText().toString();
        String genset1 = mGenset1.getText().toString();
        String pln2 = mPln2.getText().toString();
        String ups2 = mUps2.getText().toString();
        String genset2 = mGenset2.getText().toString();
        String pln3 = mPln3.getText().toString();
        String ups3 = mUps3.getText().toString();
        String genset3 = mGenset3.getText().toString();
        String sateliteLongitude = mSateliteLongitude.getText().toString();
        String ipLan1 = mIpLan1.getText().toString();
        String subnetMask1 = mSubnetMask1.getText().toString();
        String ipLan2 = mIpLan2.getText().toString();
        String subnetMask2 = mSubnetMask2.getText().toString();
        String alamat1 = mAlamat1.getText().toString();
        String success1 = mSuccess1.getText().toString();
        String loss1 = mLoss1.getText().toString();
        String keterangan1 = mKeterangan1.getText().toString();
        String alamat2 = mAlamat2.getText().toString();
        String success2 = mSuccess2.getText().toString();
        String loss2 = mLoss2.getText().toString();
        String keterangan2 = mKeterangan2.getText().toString();
        String alamat3 = mAlamat3.getText().toString();
        String success3 = mSuccess3.getText().toString();
        String loss3 = mLoss3.getText().toString();
        String keterangan3 = mKeterangan3.getText().toString();
        String spinnerSourceListrik = sourceListrik;
        String spinnerKabelRoll = kabelRoll;
        String spinnerUps = ups;
        String spinnerModulation = modulation;
        Boolean statusIl = statusIL = true;
        if (diameterAntena.matches("")) {
            mDiameterAntena.setError("isi data ini");
        } else if (polarisasi.matches("")) {
            mPolarisasi.setError("isi data ini");
        } else if (elevasi.matches("")) {
            mElevasi.setError("isi data ini");
        } else if (azimuth.matches("")) {
            mAzimuth.setError("isi data ini");
        } else if (kva.matches("")) {
            mKva.setError("isi data ini");
        } else if (managementIP.matches("")) {
            mManagementIP.setError("isi data ini");
        } else if (symboleRate.matches("")) {
            mSymboleRate.setError("isi data ini");
        } else if (pln1.matches("")) {
            mPln1.setError("isi data ini");
        } else if (ups1.matches("")) {
            mUps1.setError("isi data ini");
        } else if (genset1.matches("")) {
            mGenset1.setError("isi data ini");
        } else if (pln2.matches("")) {
            mPln2.setError("isi data ini");
        } else if (ups2.matches("")) {
            mUps2.setError("isi data ini");
        } else if (genset2.matches("")) {
            mGenset2.setError("isi data ini");
        } else if (pln3.matches("")) {
            mPln3.setError("isi data ini");
        } else if (ups3.matches("")) {
            mUps3.setError("isi data ini");
        } else if (genset3.matches("")) {
            mGenset3.setError("isi data ini");
        } else if (sateliteLongitude.matches("")) {
            mSateliteLongitude.setError("isi data ini");
        } else if (ipLan1.matches("")) {
            mIpLan1.setError("isi data ini");
        } else if (subnetMask1.matches("")) {
            mSubnetMask1.setError("isi data ini");
        } else if (ipLan2.matches("")) {
            mIpLan2.setError("isi data ini");
        } else if (subnetMask2.matches("")) {
            mSubnetMask2.setError("isi data ini");
        } else if (alamat1.matches("")) {
            mAlamat1.setError("isi data ini");
        } else if (success1.matches("")) {
            mSuccess1.setError("isi data ini");
        } else if (loss1.matches("")) {
            mLoss1.setError("isi data ini");
        } else if (keterangan1.matches("")) {
            mKeterangan1.setError("isi data ini");
        } else if (alamat2.matches("")) {
            mAlamat2.setError("isi data ini");
        } else if (success2.matches("")) {
            mSuccess2.setError("isi data ini");
        } else if (loss2.matches("")) {
            mLoss2.setError("isi data ini");
        } else if (keterangan2.matches("")) {
            mKeterangan2.setError("isi data ini");
        } else if (alamat3.matches("")) {
            mAlamat3.setError("isi data ini");
        } else if (success3.matches("")) {
            mSuccess3.setError("isi data ini");
        } else if (loss3.matches("")) {
            mLoss3.setError("isi data ini");
        } else if (keterangan3.matches("")) {
            mKeterangan3.setError("isi data ini");
        } else if (spinnerSourceListrik.matches("")) {
            TextView errorText1 = (TextView) mSpinnerSourceListrik.getSelectedView();
            errorText1.setTextColor(Color.RED);//just to highlight that this is an error
            errorText1.setText("isi data ini");//changes the selected item text to this
        } else if (spinnerKabelRoll.matches("")) {
            TextView errorText2 = (TextView) mSpinnerKabelRoll.getSelectedView();
            errorText2.setTextColor(Color.RED);//just to highlight that this is an error
            errorText2.setText("isi data ini");//changes the selected item text to this
        } else if (spinnerUps.matches("")) {
            TextView errorText3 = (TextView) mSpinnerUps.getSelectedView();
            errorText3.setTextColor(Color.RED);//just to highlight that this is an error
            errorText3.setText("isi data ini");//changes the selected item text to this
        } else if (spinnerModulation.matches("")) {
            TextView errorText4 = (TextView) mSpinnerModulation.getSelectedView();
            errorText4.setTextColor(Color.RED);//just to highlight that this is an error
            errorText4.setText("isi data ini");//changes the selected item text to this
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
                                    Toasty.success(DataInstallasiActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                    Log.i("TAG", "Ski data from server - ok" + response);
                                    Intent intent = new Intent();
                                    intent.putExtra(SharedPrefManager.SP_ID, id);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                                Log.i("TAG", "Ski data from server - ok" + response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(DataInstallasiActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("TAG", "Ski error connect - " + error);
                            Toasty.error(DataInstallasiActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
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
//                    String str1 = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"NoListTask = '"+id+"'\",\"AlamatPengirimanSurvey\":\""+alamatPengiriman+"\",\"TempatPenyimpananSurvey\":\""+tempatPenyimpanan+"\",\"NamaPICSurvey\":\""+namaPic+"\",\"KontakPICSurvey\":\""+kontakPic+"\",\"PenempatanGroundingSurvey\":\""+grounding+"\",\"UkuranAntenaSurvey\":\""+ukuranAntena+"\",\"TempatAntenaSurvey\":\""+tempatAntena+"\",\"KekuatanRoofSurvey\":\""+kekuatanRoof+"\",\"JenisMountingSurvey\":\""+mountingAntena+"\",\"LatitudeSurvey\":\""+latitude+"\",\"LongitudeSurvey\": \""+longitude+"\",\"ListrikAwalSurvey\":\""+pengukuranListrik+"\",\"SarpenACIndoorSurvey\":\""+pendukungACIndoor+"\",\"SarpenUPSSurvey\":\""+pendukungUPS+"\",\"PanjangKabelSurvey\":\""+panjangKabel+"\",\"TypeKabelSurvey\":\""+spinnerTypeKabel+"\",\"ArahAntenaSurvey\":\""+arahAntena+"\",\"KeteranganSurvey\":\""+keterangan+"\",\"StatusHasilSurvey\":\""+hasilSurvey+"\",\"FlagDataSurvey\": "+statusSv+"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                    String str1 = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"WhereDatabaseinYou\": \"NoListTask = '"+id+"'\",\"DiameterAntena\":\""+diameterAntena+"\",\"PolarisasiArahAntena\":\""+polarisasi+"\",\"ElevasiArahAntena\":\""+elevasi+"\",\"AzimuthArahAntena\":\""+azimuth+"\",\"SourceListrik\":\""+spinnerSourceListrik+"\",\"KabelRoll\":\""+spinnerKabelRoll+"\",\"PerangkatkeUPS\":\""+spinnerUps+"\",\"KVAUPS\":\""+kva+"\",\"FrequencyBandModulation\":\""+spinnerModulation+"\",\"IPManagement\":\""+managementIP+"\",\"ReceiveSymboleRate\":\""+symboleRate+"\",\"PhaseNetralUPS\":\""+ups1+"\",\"PhaseNetralPLN\":\""+pln1+"\",\"PhaseNetralGenset\":\""+genset1+"\",\"PhaseGroundPLN\":\""+pln2+"\",\"PhaseGroundUPS\":\""+ups2+"\",\"PhaseGroundGenset\":\""+genset2+"\",\"NetralGroundPLN\":\""+pln3+"\",\"NetralGroundUPS\":\""+ups3+"\",\"NetralGroundGenset\":\""+genset3+"\",\"SateliteLongitude\":\""+sateliteLongitude+"\",\"IPLAN1\":\""+ipLan1+"\",\"subnetmask1\":\""+subnetMask1+"\",\"IPLAN2\":\""+ipLan2+"\",\"subnetmask2\":\""+subnetMask2+"\",\"HasilTestAlamat1\":\""+alamat1+"\",\"HasilTestAlamat2\":\""+alamat2+"\",\"HasilTestAlamat3\":\""+alamat3+"\",\"SuccessTest1\":\""+success1+"\",\"SuccessTest2\":\""+success2+"\",\"SuccessTest3\":\""+success3+"\",\"LossTest1\":\""+loss1+"\",\"LossTest2\":\""+loss1+"\",\"LossTest3\":\""+loss1+"\",\"KeteranganTest1\":\""+keterangan1+"\",\"KeteranganTest2\":\""+keterangan2+"\",\"KeteranganTest3\":\""+keterangan3+"\",\"FlagDataInstallasi\": "+statusIl+"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
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
