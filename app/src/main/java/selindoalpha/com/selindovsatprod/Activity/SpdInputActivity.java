package selindoalpha.com.selindovsatprod.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import selindoalpha.com.selindovsatprod.Adapter.ListInputSpdAdapter;
import selindoalpha.com.selindovsatprod.Model.ListInputSpdModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.DataHelperSpdInput;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class SpdInputActivity extends AppCompatActivity {


    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    protected SpdInputActivity.LayoutManagerType mCurrentLayoutManagerType;

    private FloatingActionButton fab;
    private List<ListInputSpdModel> listSpdModel;
    private ListInputSpdAdapter listInputSpdAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    protected Cursor cursor;
    private SQLiteDatabase tampil, tulis;
    private DataHelperSpdInput dataHelperSpdInput;
    private AlertDialog.Builder alertBuilder;
    private AlertDialog alertDialog;

    private SharedPrefManager sharedPrefManager;

    private TextView mVid;
    String jsonStr,ResultWS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spd_input);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("INPUT SPD");

        sharedPrefManager = new SharedPrefManager(this);

        mVid = findViewById(R.id.vidSpd);



        dataHelperSpdInput =  new DataHelperSpdInput(this);
        tampil      = dataHelperSpdInput.getReadableDatabase();
        tulis       = dataHelperSpdInput.getWritableDatabase();

        recyclerView = findViewById(R.id.rvCardSpdUpload);
        listSpdModel = new ArrayList<>();
        listInputSpdAdapter = new ListInputSpdAdapter(listSpdModel);

        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType   = SpdInputActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (SpdInputActivity.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listInputSpdAdapter);


        fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpdInputActivity.this, UploadDocumentSpdActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

//        dataList();
    }




    public void setRecyclerViewLayoutManager(SpdInputActivity.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = SpdInputActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clear(){
        listSpdModel.clear();
        mAdapter.notifyDataSetChanged();
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
//
//            HttpHandlerGet sh = new HttpHandlerGet();
//            // Making a request to url and getting response
//
//
//
//            String vid = sharedPrefManager.getSpVid();
//            Log.i("TAG", "nameLog: " +vid);
//            String url = BaseUrl.getPublicIp + BaseUrl.listFoto + vid;
//            Log.i("Trans","url = " + url);
//            jsonStr = sh.makeServiceCall(url);
//            Log.i("Trans","Json = " + jsonStr);
//
//
//
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
//
//                        clear();
//                        JSONObject jsonObj = new JSONObject(jsonStr);
//                        ResultWS = jsonObj.getString("Result");
//
//                        if (ResultWS.equals("True")) {
//                            JSONArray jsonArray = jsonObj.getJSONArray("Raw");
//                            Log.d("TAG", "onResponse: " + jsonArray);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject data = jsonArray.getJSONObject(i);
//
//                                Log.i("TAG", "id: " + data.getString("ID"));
////                                ListTaskOpenModel objc = realms.createObject(ListTaskOpenModel.class);
//                                ListInputSpdModel objc = new ListInputSpdModel();
//
////                                objc.getImage(data.getBoolean(""));
////                                objc.getImage(data.getBoolean(""));
////                                objc.getId(data.getString("file_id"));
////                                objc.getId(data.getString(""));
////                                objc.getId(data.getString(""));
//
//                                Log.d("TAG", "getData: " + data);
//
////
//
//                                listSpdModel.add(objc);
//
////                                String vid = data.getString("VID");
////                                Log.i("TesVID", "run: " + vid);
//                                String id = data.getString("ID");
//                                sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, id);
////                                sharedPrefManager.saveSPString(SharedPrefManager.SP_VID, vid);
//
//                            }
//                            mAdapter.notifyDataSetChanged();
//
//                        }else {
//                            String Responsecode = jsonObj.getString("Data1");
//
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
//        }, 500);
//    }

}
