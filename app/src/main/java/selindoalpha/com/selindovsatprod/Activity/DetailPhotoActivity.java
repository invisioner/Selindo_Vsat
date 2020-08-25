package selindoalpha.com.selindovsatprod.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.R;

public class DetailPhotoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView mDescripsi;
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Foto");

        imageView = findViewById(R.id.imageViewDetail);
        mDescripsi = findViewById(R.id.tvDescription);
        mDelete = findViewById(R.id.btnDelete);

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePicture();
            }
        });

        Intent i=getIntent();
        String imageurl=i.getStringExtra("Image");
        String desc=i.getStringExtra("Desc");

        Log.i("TAG", "onCreate: "+ imageurl);
        Log.i("TAG", "onCreate: "+ desc);
        Picasso.with(this)
                .load(imageurl)
                .into(imageView);
        mDescripsi.setText(desc);
    }

    public void deletePicture(){
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are You Sure Delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=getIntent();
                        String imageurl=i.getStringExtra("Image").replace("http://182.16.164.170:7020/vsatmonitoring/UploadFoto/", "");
                        String image = "UploadFoto/" + imageurl;
                        imageurl = "D:\\\\vsatPRO_umar\\\\UploadFoto\\\\" + imageurl;
                        Log.i("path", "picture: "+ imageurl);
                        delete(image,imageurl);
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

    public void delete(String File,String Path){
        String url = BaseUrl.getPublicIp + BaseUrl.deletePicture;
        Log.i("url", "Delete picture: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            String ResultWS = jsonResponse.getString("Result");
                            if (ResultWS.equals("True")){
                                String Data1 = jsonResponse.getString("Data1");
                                Toasty.success(DetailPhotoActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                Log.i("TAG", "onResponse:" + Data1);

                                Log.i("TAG","Ski data from server - ok" + response );

                                Intent intent = new Intent();
                                intent.putExtra("Data", Path);
                                intent.putExtra("File", File);
                                Log.i("TAG", "File " + File);
                                Log.i("TAG", "Path " + Path);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            Log.i("TAG","Ski data from server - ok" + response );
                        } catch (JSONException e){
                            e.printStackTrace();
                            Toasty.error(DetailPhotoActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG","Ski error connect - " + error);
                        Toasty.error(DetailPhotoActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
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

                String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM1\": [{\"Data\": \""+Path+"\",\"File\": \""+File+"\"}]}]}";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri filePath = data.getData();
            String fileName;
            Cursor returnCursor =
                    getContentResolver().query(filePath, null, null, null, null);
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
