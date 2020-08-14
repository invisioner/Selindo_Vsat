package selindoalpha.com.selindovsatprod.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import selindoalpha.com.selindovsatprod.Activity.TaskDetailActivity;
import selindoalpha.com.selindovsatprod.Adapter.ListFinishRecyclerViewAdapter;
import selindoalpha.com.selindovsatprod.Adapter.TaskRecyclerViewAdapter;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.ListTaskFinishModel;
import selindoalpha.com.selindovsatprod.Model.ListTaskOpenModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;


public class TaskFragment extends Fragment {

    private static final String TAG = "TaskFragment";
    private static final int TAGOpenFragment = 111;

    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;

    TextView mStatusOpen, mCountOpen, mStatusFinish, mCountFinish;
    LinearLayout mOpen, mFinish,lytOpen,lytFinish;

    ConstraintLayout Open,Finish;

    String ResultWS;

    //Open
    public List<ListTaskOpenModel> listTaskOpenModels = new ArrayList<>();
    RecyclerView recyclerViewOpen;
    public TaskRecyclerViewAdapter mAdapterOpen;
    TextView mDataNullOpen;
    ProgressBar mProgressOpen;
    Button mRertyOpen;

    //Finish
    private static final int TAGFinishFragment = 666;

    public List<ListTaskFinishModel> listTaskFinishModels = new ArrayList<>();
    RecyclerView recyclerViewFinish;
    public ListFinishRecyclerViewAdapter mAdapterFinish;
    TextView mDataNullFinish;
    Button mRertyFinish;
    ProgressBar mProgressFinish;


    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_taks,container, false);

        mStatusOpen = view.findViewById(R.id.statusOpen);
        mCountOpen = view.findViewById(R.id.countOpen);
        mStatusFinish = view.findViewById(R.id.statusFinish);
        mCountFinish = view.findViewById(R.id.countFinish);
        mOpen = view.findViewById(R.id.open);
        mFinish = view.findViewById(R.id.finish);
        Open = view.findViewById(R.id.openlyt);
        Finish = view.findViewById(R.id.finishlyt);

        recyclerViewOpen = view.findViewById(R.id.rvCardListOpen);
        sharedPrefManager = new SharedPrefManager(getActivity());
        sharedPrefDataTask =  new SharedPrefDataTask(getActivity());

        mProgressOpen = view.findViewById(R.id.progress_open);
        mDataNullOpen = view.findViewById(R.id.tvDataKosongOpen);
        mRertyOpen = view.findViewById(R.id.btnRetryOpen);

        recyclerViewFinish = view.findViewById(R.id.rvCardListFinish);

        mProgressFinish = view.findViewById(R.id.progress_finish);
        mDataNullFinish = view.findViewById(R.id.tvDataKosongFinish);
        mRertyFinish = view.findViewById(R.id.btnRetryFinish);

        new AsyingTask().execute();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewOpen.setLayoutManager(layoutManager);
        recyclerViewOpen.setHasFixedSize(true);
        mAdapterOpen = new TaskRecyclerViewAdapter(getActivity(), listTaskOpenModels);

        mAdapterOpen.setOnItemClickListener(new TaskRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ListTaskOpenModel obj, int position) {
                Intent in = new Intent(getActivity(), TaskDetailActivity.class);
                in.putExtra(SharedPrefManager.SP_ID, obj.getId());
                in.putExtra(SharedPrefDataTask.VID, obj.getVid());
                in.putExtra(SharedPrefDataTask.NoTask, obj.getNoTask());
                in.putExtra(SharedPrefDataTask.NamaTeknisi, obj.getNamaTeknisi());
                sharedPrefDataTask.SetNamaTeknisi(obj.getNamaTeknisi());
                Log.i("Test","VID = "+obj.getVid());

                startActivityForResult(in,TAGOpenFragment);
            }
        });

        recyclerViewOpen.setAdapter(mAdapterOpen);


        final LinearLayoutManager layoutManagerFinish = new LinearLayoutManager(getActivity());
        recyclerViewFinish.setLayoutManager(layoutManagerFinish);
        recyclerViewFinish.setHasFixedSize(true);
        mAdapterFinish = new ListFinishRecyclerViewAdapter(getActivity(), listTaskFinishModels);

        mAdapterFinish.setOnItemClickListener(new ListFinishRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ListTaskFinishModel obj, int position) {
                Intent in = new Intent(getActivity(), TaskDetailActivity.class);
                in.putExtra(SharedPrefManager.SP_ID,obj.getId());
                in.putExtra(SharedPrefManager.SP_VID,obj.getVid());
                startActivityForResult(in,TAGFinishFragment);
            }
        });

        recyclerViewFinish.setAdapter(mAdapterFinish);


        mRertyOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatusOpen();
                getListData();
            }
        });
        mRertyFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatusFinish();
                getListDataFinish();
            }
        });

        mOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListData();
                Open.setVisibility(View.VISIBLE);
                Finish.setVisibility(View.GONE);
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListDataFinish();
                Open.setVisibility(View.GONE);
                Finish.setVisibility(View.VISIBLE);
            }
        });

        mStatusOpen.setVisibility(View.VISIBLE);
        mCountOpen.setVisibility(View.VISIBLE);
        mStatusFinish.setVisibility(View.VISIBLE);
        mCountFinish.setVisibility(View.VISIBLE);


        return view;
    }

    public void clearOpen(){
        listTaskOpenModels.clear();
        mAdapterOpen.notifyDataSetChanged();
    }
    public void clearFinish(){
        listTaskFinishModels.clear();
        mAdapterFinish.notifyDataSetChanged();
    }

    public class AsyingTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getStatusOpen();
            getStatusFinish();
            getListData();
            getListDataFinish();
        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    public void getStatusOpen() {
        String name = sharedPrefManager.getSPUserName();
        String nik = sharedPrefManager.getSpNik();
        Log.i("Name", "StatusOpen: " + nik);
        String url = BaseUrl.getPublicIp + BaseUrl.countOpenTask+nik+"/"+name;

        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "getStatusOpen URL: " +url);

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
                                    Log.i(TAG, "jsonObj StatusOpen : " +jsonObj);
                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d("TAG", "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);

                                            Log.i(TAG, "countOpen: " + data.getString("tot"));
                                            Log.i(TAG, "countOpen: " + data.getString("Status"));

                                            Log.d(TAG, "getData: " + data);


                                            final String countOpen = data.getString("tot");
                                            final String statusOpen = data.getString("Status");
                                            mStatusOpen.setText(statusOpen);
                                            mCountOpen.setText(countOpen);
                                        }
//                                        mAdapter.notifyDataSetChanged();
                                        mStatusOpen.setVisibility(View.VISIBLE);
                                        mCountOpen.setVisibility(View.VISIBLE);
                                        mStatusFinish.setVisibility(View.VISIBLE);
                                        mCountFinish.setVisibility(View.VISIBLE);

                                    }else {
                                        mCountOpen.setText("0");
                                        mStatusOpen.setVisibility(View.VISIBLE);
                                        mCountOpen.setVisibility(View.VISIBLE);
                                        mStatusFinish.setVisibility(View.VISIBLE);
                                        mCountFinish.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }

                            }
                        }, 20);

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
        Mysingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void getStatusFinish() {
        String name = sharedPrefManager.getSPUserName();
        String nik = sharedPrefManager.getSpNik();
        Log.i("TAG", "getListData: " + name);
        String url = BaseUrl.getPublicIp + BaseUrl.countFinishTask+nik+"/"+name;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "getStatusFinish URL: " +url);

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
                                    Log.i(TAG, "jsonObj StatusFinish : " +jsonObj);
                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d("TAG", "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);

                                            Log.i(TAG, "countFinish: " + data.getString("tot"));
                                            Log.i(TAG, "countFinish: " + data.getString("Status"));

                                            Log.d(TAG, "getData: " + data);


                                            final String countFinish = data.getString("tot");
                                            final String statusFinish = data.getString("Status");
                                            mStatusFinish.setText(statusFinish);
                                            mCountFinish.setText(countFinish);
                                        }
//                                        mAdapter.notifyDataSetChanged();
                                    }
                                    else {
                                        mCountFinish.setText("0");
                                    }

                                } catch (final JSONException e) {

                                }

                            }
                        }, 20);

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
        Mysingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void getListData(){
        mProgressOpen.setVisibility(View.VISIBLE);
        mRertyOpen.setVisibility(View.GONE);
        mDataNullOpen.setVisibility(View.GONE);
        recyclerViewOpen.setVisibility(View.GONE);
        String name = sharedPrefManager.getSPUserName();
        String nik = sharedPrefManager.getSpNik();
        String url = BaseUrl.getPublicIp + BaseUrl.listOpenTask+nik+"/"+name;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "getListData Url: " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    clearOpen();
                                    JSONObject jsonObj = new JSONObject(response);
                                    String ResultWS = jsonObj.getString("Result");

                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d("TAG", "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);

                                            Log.i("TAG", "id: " + data.getString("ID"));
                                            ListTaskOpenModel lt = new ListTaskOpenModel(
                                                    i,
                                                    data.getString("NAMAREMOTE"),
                                                    data.getString("ALAMAT"),
                                                    data.getString("NoTask"),
                                                    data.getString("VID"),
                                                    data.getString("ID"),
                                                    data.getString("TanggalTask"),
                                                    data.getString("PROVINSI"),
                                                    data.getString("idJenisTask"),
                                                    data.getString("NamaKoordinator"),
                                                    data.getString("NamaTeknisi"),
                                                    data.getString("StatusTask")
                                            );

                                            Log.d("TAG", "getData: " + data);

                                            listTaskOpenModels.add(lt);
                                            mProgressOpen.setVisibility(View.GONE);
                                            recyclerViewOpen.setVisibility(View.VISIBLE);
                                        }
                                        mAdapterOpen.notifyDataSetChanged();


                                    }else {
                                        mProgressOpen.setVisibility(View.GONE);
                                        recyclerViewOpen.setVisibility(View.GONE);
//                                        String Responsecode = jsonObj.getString("Data1");
                                        String Responsecode = "Data Task Open Tidak Ada";
                                        mDataNullOpen.setVisibility(View.VISIBLE);
                                        mRertyOpen.setVisibility(View.GONE);
                                        mDataNullOpen.setText(Responsecode);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mProgressOpen.setVisibility(View.GONE);
                                    recyclerViewOpen.setVisibility(View.GONE);
                                    mDataNullOpen.setVisibility(View.VISIBLE);
                                    mRertyOpen.setVisibility(View.VISIBLE);
                                    mDataNullOpen.setText("Error Connection Catch");
                                }

                            }
                        },200);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        mProgress.setVisibility(View.GONE);
                        recyclerViewOpen.setVisibility(View.GONE);
                        error.getMessage();
                        Log.i("TAG", "onErrorResponse: " + error.getMessage());
                        mDataNullOpen.setVisibility(View.VISIBLE);
                        mRertyOpen.setVisibility(View.VISIBLE);
                        recyclerViewOpen.setVisibility(View.GONE);
                        mDataNullOpen.setText("Cek Jaringan anda");
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

    public void getListDataFinish(){
        mProgressFinish.setProgress(200);
        mProgressFinish.setVisibility(View.VISIBLE);
        mRertyFinish.setVisibility(View.GONE);
        mDataNullFinish.setVisibility(View.GONE);
        String name = sharedPrefManager.getSPUserName();
        String nik = sharedPrefManager.getSpNik();
        Log.i("TAG", "getListData: " + name);
        String url = BaseUrl.getPublicIp + BaseUrl.listFinishTask+nik+"/"+name;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "getListData Url: " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    clearFinish();
                                    JSONObject jsonObj = new JSONObject(response);
                                    ResultWS = jsonObj.getString("Result");

                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d("TAG", "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);
                                            ListTaskFinishModel ls = new ListTaskFinishModel(
                                                    i,
                                                    data.getString("NAMAREMOTE"),
                                                    data.getString("ALAMAT"),
                                                    data.getString("NoTask"),
                                                    data.getString("VID"),
                                                    data.getString("ID"),
                                                    data.getString("TanggalTask"),
                                                    data.getString("PROVINSI"),
                                                    data.getString("idJenisTask"),
                                                    data.getString("NamaKoordinator"),
                                                    data.getString("NamaTeknisi"),
                                                    data.getString("StatusTask")
                                            );
                                            Log.d("TAG", "getData: " + data);

                                            listTaskFinishModels.add(ls);

                                            String vid = data.getString("VID");

                                            Log.d("TesVID", "run: " + vid.length());
                                            String id = data.getString("ID");
                                            sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, id);
                                            sharedPrefManager.saveSPString(SharedPrefManager.SP_VID, vid);
                                            mProgressFinish.setVisibility(View.GONE);
                                            recyclerViewFinish.setVisibility(View.VISIBLE);
                                        }
                                        mAdapterFinish.notifyDataSetChanged();

                                    } else {
//                                        String Responsecode = jsonObj.getString("Data1");
                                        String Responsecode = "Data Task Finish Tidak Ada";
                                        mDataNullFinish.setVisibility(View.VISIBLE);
                                        mRertyFinish.setVisibility(View.GONE);
                                        recyclerViewFinish.setVisibility(View.GONE);
                                        mDataNullFinish.setText(Responsecode);
                                        mProgressFinish.setVisibility(View.GONE);
                                        recyclerViewFinish.setVisibility(View.GONE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mProgressFinish.setVisibility(View.GONE);
                                    recyclerViewFinish.setVisibility(View.GONE);
                                    mDataNullFinish.setVisibility(View.VISIBLE);
                                    mRertyFinish.setVisibility(View.VISIBLE);
                                    recyclerViewFinish.setVisibility(View.GONE);
                                    mDataNullFinish.setText("Error Connection Catch");
                                }

                            }
                        }, 500);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressFinish.setVisibility(View.VISIBLE);
                        recyclerViewFinish.setVisibility(View.GONE);
                        error.getMessage();
                        Log.i("TAG", "onErrorResponse: " + error.getMessage());
                        mDataNullFinish.setVisibility(View.VISIBLE);
                        mRertyFinish.setVisibility(View.VISIBLE);
                        recyclerViewFinish.setVisibility(View.GONE);
                        mDataNullFinish.setText("Cek Jaringan anda");
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

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){

        if (requestCode==TAGFinishFragment||requestCode==TAGOpenFragment){
            if (resultCode== Activity.RESULT_OK){
                new AsyingTask().execute();
            }
        }


    }

}
