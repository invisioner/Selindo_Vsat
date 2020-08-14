package selindoalpha.com.selindovsatprod.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import selindoalpha.com.selindovsatprod.Activity.SpdListActivity;
import selindoalpha.com.selindovsatprod.Adapter.SpdRecyclerViewAdapter;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.ListSpdModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;


public class SpdFragment extends Fragment {

    private static final String TAG = "SpdFragment";
    private static final int TAGSpdFragment = 777;

    List<ListSpdModel> listSpdModels = new ArrayList<>();
    RecyclerView recyclerView;
    public SpdRecyclerViewAdapter mAdapter;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;
    TextView mDiBerikan, mPresetujuan, mDiGunakan, mSisa;

    LinearLayout mLinearLayout;


    TextView mDataNull;
    ProgressBar mProgress;
    Button mRerty;

    String nikUser,diBerikan,presetujuan,diGunakan;

    DecimalFormat kursIdr = (DecimalFormat) DecimalFormat.getCurrencyInstance();
    DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

    String sisa,noTask;

    NumberFormat format ;

    public SpdFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spd,container, false);
        mDiBerikan = view.findViewById(R.id.tvDiBerikan);
        mPresetujuan = view.findViewById(R.id.tvPresetujuan);
        mDiGunakan = view.findViewById(R.id.tvDiGunakan);
        mSisa = view.findViewById(R.id.tvSisa);
        recyclerView = view.findViewById(R.id.rvCardVertical);
        sharedPrefManager = new SharedPrefManager(getActivity());
        sharedPrefDataTask = new SharedPrefDataTask(getActivity());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new AsyingTaskSPD().execute();
        recyclerView.setHasFixedSize(true);
        mAdapter = new SpdRecyclerViewAdapter(getActivity(),listSpdModels );

        mAdapter.setOnItemClickListener(new SpdRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ListSpdModel obj, int position) {
                Log.i("Masuk","Masuk 2");
                Intent in = new Intent(getActivity(), SpdListActivity.class);
                in.putExtra(SharedPrefDataSPD.NoTask, obj.getNoTask());
                in.putExtra(SharedPrefDataSPD.NamaTeknisi, obj.getNamaTeknisi());
                startActivityForResult(in,TAGSpdFragment);
            }
        });

        recyclerView.setAdapter(mAdapter);
        mLinearLayout = view.findViewById(R.id.llHorizontalScroll);
        mProgress = view.findViewById(R.id.progress_spd);

        nikUser = sharedPrefManager.getSpNik();

        mDataNull = view.findViewById(R.id.tvDataKosongSpd);
        mRerty = view.findViewById(R.id.btnRetrySpd);

        mRerty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTaskSPD();
            }
        });


        return view;
    }

    public void clear(){
        listSpdModels.clear();
        mAdapter.notifyDataSetChanged();
    }



    public class AsyingTaskSPD extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getTaskSPD();
            getListData();
        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    public void getTaskSPD(){
        mProgress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        String url =BaseUrl.getPublicIp + BaseUrl.listSpd+nikUser;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "Url SPD: "+url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            Log.i(TAG, "jsonObj TaskSPD: " +jsonObj);
                            String ResultWS = jsonObj.getString("Result");
                            clear();
                            if (ResultWS.equals("True")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                Log.d(TAG, "onResponse: " + jsonArray);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    ListSpdModel lt = new ListSpdModel(
                                            i,
                                            data.getString("Notask"),
                                            data.getString("NamaTeknisi"),
                                            data.getString("IdTeknisi"),
                                            data.getString("jumlahtrf"),
                                            data.getString("ApprovalNominal"),
                                            data.getString("TanggalTask"),
                                            data.getString("Penggunaan"),
                                            data.getString("sisa"),
                                            data.getString("NamaTask")

                                    );

                                    listSpdModels.add(lt);
                                    mProgress.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }



                                mAdapter.notifyDataSetChanged();

                            }else {
                                mProgress.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                String Responsecode = "Data SPD Tidak Ada";
                                mDataNull.setVisibility(View.VISIBLE);
                                mRerty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                mDataNull.setText(Responsecode);
                                Log.i(TAG, "Response True: " + Responsecode);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgress.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            mDataNull.setVisibility(View.VISIBLE);
                            mRerty.setVisibility(View.VISIBLE);
                            mDataNull.setText("Cek Jaringan anda");
                            Log.i(TAG, "JSONException: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        error.getMessage();
                        mDataNull.setVisibility(View.VISIBLE);
                        mRerty.setVisibility(View.VISIBLE);
                        mDataNull.setText("Cek Jaringan anda");
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
        Mysingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void getListData(){
        String url = BaseUrl.getPublicIp+BaseUrl.NominalSPD+nikUser;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG,"URL = "+url);
        StringRequest stringRequest =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String ResultWS = jsonObj.getString("Result");

                            Log.i(TAG, "ResultWS = " + ResultWS);

                            if (ResultWS.equals("True")) {
                                String Raw = jsonObj.getString("Raw");
                                Raw = Raw.replace("[", "");
                                Raw = Raw.replace("]", "");
                                JSONObject obj = new JSONObject(Raw);
                                diBerikan = obj.getString("jumlahtrf");
                                presetujuan =obj.getString("ApprovalNominal");
                                diGunakan = obj.getString("Penggunaan");
                                sisa = obj.getString("sisa");
                                if (diBerikan.equals("null")){
                                    diBerikan ="0";
                                }if (presetujuan.equals("null")){
                                    presetujuan ="0";
                                }if (diGunakan.equals("null")){
                                    diGunakan ="0";
                                }if (sisa.equals("null")){
                                    sisa ="0";
                                }
                                SetTotal(diBerikan,presetujuan,diGunakan,sisa);


                            }else {
                                String Responsecode = "Data Karyawan Tidak Ada";
                                Log.i(TAG, "ResponseCode = " + Responsecode);
                            }

                        }catch (JSONException e){
                            Log.i(TAG, "onErrorResponse: " + e.toString());
                        }

                    }
                },200);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        Mysingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void SetTotal(String strdiBerikan,String strpresetujuan,String strdiGunakan,String strsisa){

        format = new DecimalFormat();

        String getApprove = String.valueOf(format.format(Double.valueOf(strdiBerikan)));
        String getpresetujuan = String.valueOf(format.format(Double.valueOf(strpresetujuan)));
        String getdiGunakan = String.valueOf(format.format(Double.valueOf(strdiGunakan)));
        String getsisa = String.valueOf(format.format(Double.valueOf(strsisa)));

        mDiBerikan.setText(getApprove);
        mPresetujuan.setText(getpresetujuan);
        mDiGunakan.setText(getdiGunakan);
        mSisa.setText(getsisa);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == TAGSpdFragment ){
            if (resultCode == Activity.RESULT_OK){
                listSpdModels.clear();
                new AsyingTaskSPD().execute();
            }
        }
    }

}
