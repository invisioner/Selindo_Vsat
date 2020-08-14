package selindoalpha.com.selindovsatprod.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.ListTaskFinishModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class FinishFragment extends Fragment {

    private static final String TAG = "FinishFragment";
    private static final int TAGFinishFragment = 666;

    public List<ListTaskFinishModel> listTaskFinishModels = new ArrayList<>();
    ListTaskFinishModel ls;
    public RecyclerView recyclerViewFinish;
    public ListFinishRecyclerViewAdapter mAdapter;
    SharedPrefManager sharedPrefManager;
    TextView mDataNull;
    Button mRerty;
//    ProgressDialog progressDialog;
    ProgressBar mProgress;
    String jsonStr,ResultWS;

    boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finish,container, false);

        recyclerViewFinish = view.findViewById(R.id.rvCardListFinish);
        sharedPrefManager = new SharedPrefManager(getActivity());
//        progressDialog = new ProgressDialog(getActivity());
        mDataNull = view.findViewById(R.id.tvDataKosongFinish);
        mRerty = view.findViewById(R.id.btnRetryFinish);
        mProgress = view.findViewById(R.id.progress_finish);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewFinish.setLayoutManager(layoutManager);
        new AsyingTaskList().execute();
//        populateData();
        initScrollListener();

        recyclerViewFinish.setHasFixedSize(true);
        mAdapter = new ListFinishRecyclerViewAdapter(getActivity(), listTaskFinishModels);

        mAdapter.setOnItemClickListener(new ListFinishRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ListTaskFinishModel obj, int position) {
                Intent in = new Intent(getActivity(), TaskDetailActivity.class);
                in.putExtra(SharedPrefManager.SP_ID,obj.getId());
                in.putExtra(SharedPrefManager.SP_VID,obj.getVid());
                startActivityForResult(in,TAGFinishFragment);
            }
        });

        recyclerViewFinish.setAdapter(mAdapter);

//        progressDialog.setMessage("Loading");
//        progressDialog.setCancelable(false);

        mRerty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListData();
                clear();
            }
        });

        return view;
    }

    private void populateData() {
        int i = 0;
        while (i < 10) {
            listTaskFinishModels.add(i,ls);
            i++;
        }
    }

    private void initScrollListener() {
        recyclerViewFinish.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == listTaskFinishModels.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        listTaskFinishModels.add(null);
        mAdapter.notifyItemInserted(listTaskFinishModels.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listTaskFinishModels.remove(listTaskFinishModels.size() - 1);
                int scrollPosition = listTaskFinishModels.size();
                mAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    listTaskFinishModels.add(currentSize, ls);
                    currentSize++;
                }

                mAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }


    public void clear(){
        listTaskFinishModels.clear();
        mAdapter.notifyDataSetChanged();
    }
    private class AsyingTaskList extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getListData();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            populateData();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
//            getListData();
            return "OK";
        }
    }

    public void getListData(){
        mProgress.setProgress(200);
        mProgress.setVisibility(View.VISIBLE);
        mRerty.setVisibility(View.GONE);
        mDataNull.setVisibility(View.GONE);
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
                                    clear();
                                    JSONObject jsonObj = new JSONObject(response);
                                    ResultWS = jsonObj.getString("Result");

                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d("TAG", "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);
                                            ls = new ListTaskFinishModel(
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
                                            mProgress.setVisibility(View.GONE);
                                            recyclerViewFinish.setVisibility(View.VISIBLE);
                                        }
                                        mAdapter.notifyDataSetChanged();

                                    } else {
//                                        String Responsecode = jsonObj.getString("Data1");
                                        String Responsecode = "Data Task Tidak Ada";
                                        mDataNull.setVisibility(View.VISIBLE);
                                        mRerty.setVisibility(View.GONE);
                                        recyclerViewFinish.setVisibility(View.GONE);
                                        mDataNull.setText(Responsecode);
                                        mProgress.setVisibility(View.GONE);
                                        recyclerViewFinish.setVisibility(View.GONE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mProgress.setVisibility(View.GONE);
                                    recyclerViewFinish.setVisibility(View.GONE);
                                    mDataNull.setVisibility(View.VISIBLE);
                                    mRerty.setVisibility(View.VISIBLE);
                                    recyclerViewFinish.setVisibility(View.GONE);
                                    mDataNull.setText("Error Connection Catch");
                                }

                            }
                        }, 500);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.setVisibility(View.VISIBLE);
                        recyclerViewFinish.setVisibility(View.GONE);
                        error.getMessage();
                        Log.i("TAG", "onErrorResponse: " + error.getMessage());
                        mDataNull.setVisibility(View.VISIBLE);
                        mRerty.setVisibility(View.VISIBLE);
                        recyclerViewFinish.setVisibility(View.GONE);
                        mDataNull.setText("Cek Jaringan anda");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == TAGFinishFragment ){
            if (resultCode == Activity.RESULT_OK){
                listTaskFinishModels.clear();
                new AsyingTaskList().execute();
            }
        }
    }

//    private class getData extends AsyncTask<Void, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Response();
//
//        }
//
//        @Override
//        protected String doInBackground(Void ... arg0)
//        {
//            HttpHandlerGet sh = new HttpHandlerGet();
//            // Making a request to url and getting response
//            String name = sharedPrefManager.getSPUserName();
//            String nik = sharedPrefManager.getId();
//            String url = BaseUrl.getPublicIp + BaseUrl.listFinishTask+nik+"/"+name;
//
//
//            if (url.contains(" ")){
//                url = url.replace(" ","%20");
//            }
//            Log.i("Trans","url = " + url);
//            jsonStr = sh.makeServiceCall(url);
//            Log.i("Trans","Json = " + jsonStr);
//            return "OK";
//        }
//    }
//
//    private void Response() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (jsonStr != null && jsonStr.length() > 10) {
//                    try {
//                        clear();
//                        JSONObject jsonObj = new JSONObject(jsonStr);
//                        ResultWS = jsonObj.getString("Result");
//                        mProgress.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                        if (ResultWS.equals("True")) {
//                            JSONArray jsonArray = jsonObj.getJSONArray("Raw");
//                            Log.d("TAG", "onResponse: " + jsonArray);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject data = jsonArray.getJSONObject(i);
//
//                                ListTaskFinishModel lt = new ListTaskFinishModel(
//                                        data.getString("NoTask"),
//                                        data.getString("VID"),
//                                        data.getString("ID"),
//                                        data.getString("TanggalTask"),
//                                        data.getString("PROVINSI"),
//                                        data.getString("idJenisTask"),
//                                        data.getString("NamaKoordinator"),
//                                        data.getString("NamaTeknisi"),
//                                        data.getString("StatusTask")
//                                );
//                                Log.d("TAG", "getData: " + data);
//
//                                listTaskFinishModels.add(lt);
//
//                                String vid = data.getString("VID");
//
//                                Log.i("TesVID", "run: " + vid);
//                                String id = data.getString("ID");
//                                sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, id);
//                                sharedPrefManager.saveSPString(SharedPrefManager.SP_VID, vid);
//                                mProgress.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.VISIBLE);
//                            }
//                            mAdapter.notifyDataSetChanged();
//                            mProgress.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//
//                        }else {
//                            String Responsecode = jsonObj.getString("Data1");
//                            mDataNull.setVisibility(View.VISIBLE);
//                            mRerty.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//                            mDataNull.setText(Responsecode);
//                            mProgress.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//                        }
//
//
//                    } catch (final JSONException e) {
//
//
//                    }
//                } else {
//
//                }
//            }
//        }, 200);
//    }


}
