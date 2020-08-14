package selindoalpha.com.selindovsatprod.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Adapter.AdapterBarangRusak;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.NamaBarang;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class AddBarangRusakActivity extends AppCompatActivity {

    private static final String TAG = "AddBarangRusakActivity";

    public static String encodedImage;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    private Bitmap bitmap;
    private EditText mBarang, mModel, mSerialNumber, mStatus, mCatatan, mFileName;
    private Button mSaveTerpasang;
    private ImageView mImageView;

    String ResultWS;
    Boolean StatusBT;
    SharedPrefManager sharedPrefManager;
    SharedPrefDataTask sharedPrefDataTask;

    public static String vid, id, noTask,NamabarangTerpasang;

    Spinner namaBarang;
    List<NamaBarang> NamaBarangArr = new ArrayList<NamaBarang>();
    AdapterBarangRusak adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barang_rusak);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("ADD BARANG RUSAK");

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            vid = in.getString(SharedPrefDataTask.VID);
            noTask = in.getString(SharedPrefDataTask.NoTask);
        }

        mBarang = findViewById(R.id.barang_rusak);
        mModel = findViewById(R.id.model_rusak);
        mSerialNumber = findViewById(R.id.serial_number_rusak);
        mStatus = findViewById(R.id.status_rusak);
        mCatatan = findViewById(R.id.catatan_rusak);
        mSaveTerpasang = findViewById(R.id.btn_simpan_rusak);
        mImageView = findViewById(R.id.imageViewBarangTerpasangRusak);
        mFileName = findViewById(R.id.etImagesNameRusak);
        namaBarang = findViewById(R.id.spinner_nama_barang_rusak);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);

        new AsyingTask().execute();

        mSaveTerpasang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        adapter = new AdapterBarangRusak(AddBarangRusakActivity.this,NamaBarangArr);
        namaBarang.setAdapter(adapter);
        namaBarang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NamabarangTerpasang = NamaBarangArr.get(position).getNamaBarang();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void insertData() {
        String url = BaseUrl.getPublicIp + BaseUrl.insetBarangRusak;
        Log.i(TAG, "insertData: " + url);
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        String barang = NamabarangTerpasang;
        String model = mModel.getText().toString();
        String sn = mSerialNumber.getText().toString();
        String nameFile = mFileName.getText().toString();
        String catatan = mStatus.getText().toString();
        Boolean statusBt = StatusBT = true;
        String DocNo = BuildDocNo();
        Boolean statusUp = true;
//        String vid = sharedPrefManager.getSpVid();
        Log.i(TAG, "insertData: " + vid);
        Log.i(TAG, "insertData: " + statusBt);
        Log.i("GetVID", "updateDataLokasi: " +vid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            String ResultWS = jsonResponse.getString("Result");

                            if (ResultWS.equals("True")){

                                String Data1 = jsonResponse.getString("Data1");
                                Log.i("TAG", "onResponse: " + Data1);
                                Toasty.success(AddBarangRusakActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(AddBarangRusakActivity.this, "Failed!", Toast.LENGTH_SHORT, true).show();
                        }
                        Log.i("TAG","Ski data from server - ok" + response );
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG","Ski error connect - "+error);
                        Toasty.error(AddBarangRusakActivity.this, "Failed!", Toast.LENGTH_SHORT, true).show();
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
                String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"VID\": \""+vid+"\",\"NamaBarang\": \""+barang+"\",\"Type\": \""+model+"\",\"SN\": \""+sn+"\",\"Status\": \"Rusak\",\"DateCreate\": \""+date+"\",\"UserCreate\": \""+sharedPrefDataTask.GetNamaTeknisi()+"\",\"DocNo\":\""+DocNo+"\"}],\"PARAM3\": [{\"WhereDatabaseinYou\": \"VID='"+vid+"' and NoTask = '"+noTask+"'\",\"FlagDataBarang\": "+statusBt+",\"FlagUploadPhoto\": "+statusUp+"}],\"PARAM2\": [{\"file_url\": \"UploadFoto/"+nameFile+"\",\"file_usercreate\": \""+sharedPrefDataTask.GetNamaTeknisi()+"\",\"file_datecreate\": \""+date+"\",\"VID\": \""+vid+"\",\"Description\": \""+catatan+"\",\"Keterangan\": \""+catatan+"\",\"YourImage64Name\": \""+nameFile+"\",\"YourImage64File\": \""+encodedImage+"\",\"DocNo\":\""+DocNo+"\"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                Log.i("str", "strRaw: " + str);
                return str.getBytes();
            }

            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }

        };

        Mysingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void setToImageView(Bitmap bitmap) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        byte[] byteFormat = bytes.toByteArray();
        // get the base 64 string
        final String imgString = new String(Base64.encode(byteFormat, Base64.DEFAULT));
        encodedImage = imgString.replaceAll("\\s+", "");
        Log.i("imgStr", "imgStr: " + encodedImage);
//        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
//        imageView.setImageBitmap(decoded);

//        Bitmap bm = BitmapFactory.decodeFile("/path/to/image.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos); //bm is the bitmap object
//        byte[] b = baos.toByteArray();

        mImageView.setImageBitmap(decodeBase64(encodedImage));

        Log.d("TAG", "setToImageView: "+encodedImage);

        Log.d("TAG", "setToImageViewDecode: " + decodeBase64(encodedImage));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            String fileName;
            Cursor returnCursor =
                    getContentResolver().query(filePath, null, null, null, null);
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                fileName = returnCursor.getString(nameIndex);
                Log.i("TAG", "neme file is : " +fileName);
                mFileName.setText(fileName);
                Log.i("TAG", "onActivityResult: " + bitmap);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void choseBarangRusak(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private class AsyingTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getListBarang();
        }

        @Override
        protected String doInBackground(Void ... arg0) {
            getListBarang();
            return "OK";
        }
    }



    public void getListBarang() {
        String url = BaseUrl.getPublicIp + BaseUrl.ListBarang;

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

                                            Log.i(TAG, "Type: " + data.getString("Barang"));

                                            NamaBarang nb = new NamaBarang();
                                            nb.setNamaBarang(data.getString("Barang"));

                                            NamaBarangArr.add(nb);

                                        }

                                        adapter.notifyDataSetChanged();

                                    }else {

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
        Mysingleton.getInstance(AddBarangRusakActivity.this).addToRequestQueue(request);
    }

    private String BuildDocNo() {
        String DocNo;

        Date current = new Date();
        SimpleDateFormat frmtYears = new SimpleDateFormat("yyyy");
        String DateYearsString = frmtYears.format(current);
        SimpleDateFormat frmtMounth = new SimpleDateFormat("MM");
        String DateMounthString = frmtMounth.format(current);
        SimpleDateFormat frmtDay = new SimpleDateFormat("dd");
        String DateDayString = frmtDay.format(current);

        SimpleDateFormat frmtTimeJam = new SimpleDateFormat("HH");
        String TimeJamString = frmtTimeJam.format(current);
        SimpleDateFormat frmtTimeMenit = new SimpleDateFormat("mm");
        String TimeMenitString = frmtTimeMenit.format(current);
        SimpleDateFormat frmtTimeDetik = new SimpleDateFormat("ss");
        String TimeDetikString = frmtTimeDetik.format(current);


        DocNo = "PTASK" + TimeJamString + DateYearsString + TimeMenitString + DateMounthString + DateDayString + TimeDetikString;

        return DocNo;
    }


}
