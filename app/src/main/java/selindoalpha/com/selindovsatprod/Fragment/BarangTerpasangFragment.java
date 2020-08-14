package selindoalpha.com.selindovsatprod.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Activity.AddBarangTerpasangActivity;
import selindoalpha.com.selindovsatprod.Adapter.BarangTerpasangAdapter;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.DataTerpasangModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;


public class BarangTerpasangFragment extends Fragment {

    private static final String TAG = "BarangTerpasangFragment";
    private static final int TAGBarangTerpasangFragment = 333;

    List<DataTerpasangModel> terpasangModelList = new ArrayList<>();
    private FloatingActionButton fab;
    RecyclerView recyclerView;
    BarangTerpasangAdapter mAdapter;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;
    String jsonStr,ResultWS;

    TextView mDataNull;
    ProgressBar mProgress;
    Button mRerty;

    String vid,id,noTask;

    public BarangTerpasangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_barang_terpasang,container, false);

        recyclerView = view.findViewById(R.id.rvCardListDataTerpasang);

        sharedPrefManager = new SharedPrefManager(getActivity());
        sharedPrefDataTask = new SharedPrefDataTask(getActivity());

        mDataNull = view.findViewById(R.id.tvDataTerpasangKosong);
        mRerty = view.findViewById(R.id.btnRetryDataTerpasang);
        mProgress = view.findViewById(R.id.progress_terpasang);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new BarangTerpasangAdapter(getActivity(), terpasangModelList);
        mAdapter.setOnItemClickListener(new BarangTerpasangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DataTerpasangModel obj, int position) {
//                Toast.makeText(getActivity(),obj.getVid(),Toast.LENGTH_SHORT).show();
                DialogDelete(obj.getSn());
            }
        });
        recyclerView.setAdapter(mAdapter);

        id = sharedPrefDataTask.GetTaskid();
        vid = sharedPrefDataTask.GetTaskVid();
        noTask = sharedPrefDataTask.GetTaskNoTask();
        Log.i(TAG, "BarangTerpasang: " + noTask);
        Log.i(TAG, "BarangTerpasang: " + id);
        Log.i(TAG, "BarangTerpasang: " + vid);

        fab = view.findViewById(R.id.fabAddTerpasang);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddBarangTerpasangActivity.class);
                intent.putExtra(SharedPrefManager.SP_VID,vid);
                intent.putExtra(SharedPrefManager.SP_ID,id);
                intent.putExtra(SharedPrefDataTask.NoTask,noTask);
                startActivityForResult(intent,TAGBarangTerpasangFragment);
            }
        });

        mRerty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyingBarangTerpasang().execute();
            }
        });

        new AsyingBarangTerpasang().execute();

        return view;


    }

    public void clear(){
        terpasangModelList.clear();
        mAdapter.notifyDataSetChanged();
    }

    public class AsyingBarangTerpasang extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getBarangTerpasang();

        }

        @Override
        protected String doInBackground(Void ... arg0) {
            return "OK";
        }
    }

    public void getBarangTerpasang(){
        mProgress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        String vid = sharedPrefDataTask.GetTaskVid();
        Log.i(TAG, "BarangTerpasang: " + vid);
        String url = BaseUrl.getPublicIp + BaseUrl.listBarangTerpasang + vid;
        if (url.contains(" ")){
            url = url.replace(" ","%20");
        }
        Log.i(TAG, "BarangTerpasang: " + url);
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
                                    clear();
                                    if (ResultWS.equals("True")) {
                                        JSONArray jsonArray = jsonObj.getJSONArray("Raw");
                                        Log.d("TAG", "onResponse: " + jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);

                                            DataTerpasangModel lt = new DataTerpasangModel(
                                                    data.getString("VID"),
                                                    data.getString("NamaBarang"),
                                                    data.getString("Type"),
                                                    data.getString("SN"),
                                                    data.getString("ESNMODEM"),
                                                    data.getString("Status"),
                                                    data.getString("file_url"),
                                                    data.getString("Description")
                                            );
                                            Log.d("TAG", "getDataTerpasang: " + data);

                                            terpasangModelList.add(lt);
                                        }
                                        mAdapter.notifyDataSetChanged();
                                        mProgress.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        mDataNull.setVisibility(View.GONE);

                                    }else {
                                        String Responsecode = "Data Barang Terpasang Belom Di Buat";
                                        Log.i(TAG, "else Response True: " + Responsecode);
                                        mDataNull.setVisibility(View.VISIBLE);
                                        mRerty.setVisibility(View.GONE);
                                        mDataNull.setText(Responsecode);
                                        mProgress.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i(TAG, "JSONException : " + e.getMessage());
                                    mDataNull.setVisibility(View.VISIBLE);
                                    mRerty.setVisibility(View.VISIBLE);
                                    mDataNull.setText(e.getMessage());
                                    mProgress.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
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
                        mDataNull.setVisibility(View.VISIBLE);
                        mRerty.setVisibility(View.VISIBLE);
                        mDataNull.setText(error.getMessage());
                        mProgress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Header", "Dota2");
                return headers;
            }
        };
        Mysingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){

        if (requestCode==TAGBarangTerpasangFragment){
            if (resultCode== Activity.RESULT_OK){
                terpasangModelList.clear();
                new AsyingBarangTerpasang().execute();
            }
        }

    }

    private void DeleteImage(String SN){
        String url = BaseUrl.getPublicIp + BaseUrl.updateDataInstalasi;
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
                                Toasty.success(getActivity(), "Success!", Toast.LENGTH_SHORT, true).show();
                                Log.i("TAG", "Ski data from server - ok" + response);


                            }
                            Log.i("TAG", "Ski data from server - ok" + response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(getActivity(), "Failure!", Toast.LENGTH_SHORT, true).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", "Ski error connect - " + error);
                        Toasty.error(getActivity(), "Error!", Toast.LENGTH_SHORT, true).show();
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
                String str1 = "{\"Result\":\"True\",\"Raw\":[{\"PARAM3\":[{\"WhereDatabaseinYou\":\"SN='"+SN+"'\"}],\"Data1\":\"\",\"Data2\":\"\",\"Data3\":\"\",\"Data4\":\"\",\"Data5\":\"\",\"Data6\":\"\",\"Data7\":\"\",\"Data8\":\"\",\"Data9\":\"\",\"Data10\":\"\"}]}";
                Log.i("sendBody", "sendBody: " + str1);
                return str1.getBytes();
            }

            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }
        };

        Mysingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public void DialogDelete(String SN){
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setMessage("Are You Sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteImage(SN);
                        new AsyingBarangTerpasang().execute();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}







