package selindoalpha.com.selindovsatprod.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import selindoalpha.com.selindovsatprod.Adapter.TaskRecyclerViewAdapter;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.ListTaskOpenModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class OpenFragment extends Fragment {

    private static final String TAG = "OpenFragment";
    private static final int TAGOpenFragment = 111;

    public List<ListTaskOpenModel> listTaskOpenModels = new ArrayList<>();

    public RecyclerView recyclerViewOpen;
    public TaskRecyclerViewAdapter mAdapterOpen;
    SharedPrefManager sharedPrefManager;
    String jsonStr,ResultWS;

    TextView mDataNull;
    ProgressBar mProgress;
    Button mRerty;


    FrameLayout frameLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_open,container, false);



        recyclerViewOpen = view.findViewById(R.id.rvCardListOpen);
        sharedPrefManager = new SharedPrefManager(getActivity());

        mProgress = view.findViewById(R.id.progress_open);
        mDataNull = view.findViewById(R.id.tvDataKosongOpen);
        mRerty = view.findViewById(R.id.btnRetryOpen);

        new AsyingTaskList().execute();

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

                Log.i("Test","VID = "+obj.getVid());

                startActivityForResult(in,TAGOpenFragment);
            }
        });

        recyclerViewOpen.setAdapter(mAdapterOpen);

        mRerty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListData();
            }
        });

        return view;

    }

    private class AsyingTaskList extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getListData();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    public void clear(){
        listTaskOpenModels.clear();
        mAdapterOpen.notifyDataSetChanged();
    }

    public void getListData(){
        mProgress.setVisibility(View.VISIBLE);
        mRerty.setVisibility(View.GONE);
        mDataNull.setVisibility(View.GONE);
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
                                    clear();
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
                                            mProgress.setVisibility(View.GONE);
                                            recyclerViewOpen.setVisibility(View.VISIBLE);
                                        }
                                        mAdapterOpen.notifyDataSetChanged();


                                    }else {
                                        mProgress.setVisibility(View.GONE);
                                        recyclerViewOpen.setVisibility(View.GONE);
//                                        String Responsecode = jsonObj.getString("Data1");
                                        String Responsecode = "Data Task Tidak Ada";
                                        mDataNull.setVisibility(View.VISIBLE);
                                        mRerty.setVisibility(View.GONE);
                                        mDataNull.setText(Responsecode);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mProgress.setVisibility(View.GONE);
                                    recyclerViewOpen.setVisibility(View.GONE);
                                    mDataNull.setVisibility(View.VISIBLE);
                                    mRerty.setVisibility(View.VISIBLE);
                                    mDataNull.setText("Error Connection Catch");
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
                        mDataNull.setVisibility(View.VISIBLE);
                        mRerty.setVisibility(View.VISIBLE);
                        recyclerViewOpen.setVisibility(View.GONE);
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
        if (requestCode == TAGOpenFragment ){
            if (resultCode == Activity.RESULT_OK){
                listTaskOpenModels.clear();
                new AsyingTaskList().execute();
            }
        }
    }

}
