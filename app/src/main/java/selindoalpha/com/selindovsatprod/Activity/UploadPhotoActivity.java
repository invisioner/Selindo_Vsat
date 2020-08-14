package selindoalpha.com.selindovsatprod.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataTask;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class UploadPhotoActivity extends AppCompatActivity {

    private static final String TAG = "UploadPhotoActivity";

    public static String encodedImage;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    private Bitmap bitmap;
    private EditText mImagesName, mDescrip, mKeterangan;
    private ImageView mImageView;
    String ResultWS;
    Boolean statusUP = false;
    private SharedPrefManager sharedPrefManager;
    private SharedPrefDataTask sharedPrefDataTask;

    String id, vid, noTask, statusTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("UPLOAD PHOTO");

        Bundle in = getIntent().getExtras();
        if (in!=null) {
            id = in.getString(SharedPrefManager.SP_ID);
            noTask = in.getString(SharedPrefDataTask.NoTask);
            vid = in.getString(SharedPrefDataTask.VID);
            statusTask = in.getString(SharedPrefDataTask.idJenisTask);
        }

        Log.i(TAG, "id: " + id);
        Log.i(TAG, "vid: " + vid);
        Log.i(TAG, "statusTask: " + statusTask);
        Log.i(TAG, "noTask: " + noTask);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefDataTask = new SharedPrefDataTask(this);
        mImagesName = findViewById(R.id.etImagesName);
        mImageView = findViewById(R.id.imageViewFile);
        mDescrip = findViewById(R.id.etDescrip);
        mKeterangan = findViewById(R.id.etKeterangan);


    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        final String imgString = new String(Base64.encode(byteFormat, Base64.DEFAULT));
        encodedImage = imgString.replaceAll("\\s+", "");
        Log.i("imgStr", "imgStr: " + encodedImage);
        return encodedImage;
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
    public void saveUploadPhoto(View view) {
        UploadFoto();
    }

    private void UploadFoto() {
        String url = BaseUrl.getPublicIp + BaseUrl.insetFoto;
        Log.i(TAG, "UploadFoto Url: " + url);

        @SuppressLint("SimpleDateFormat")
        DateFormat dt = new SimpleDateFormat("HH:mm:ss");
        String time = dt.format(Calendar.getInstance().getTime());
        Log.i("Time", "Realtime: " + time);

//        String vid = sharedPrefDataTask.GetTaskVid();
//        String noTask = sharedPrefDataTask.GetTaskNoTask();
        String nameImage = mImagesName.getText().toString();
        String descrip = mDescrip.getText().toString();
        String keterangan = mKeterangan.getText().toString();
        Boolean statusUp = statusUP = true;
        if (nameImage.matches("")) {
            mImagesName.setError("isi data ini");
        }
        else if (descrip.matches("")){
            mDescrip.setError("isi data ini");
        }
        else if (keterangan.matches("")){
            mKeterangan.setError("isi data ini");
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
                                    Toasty.success(UploadPhotoActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                    Intent intent = new Intent();
                                    intent.putExtra(SharedPrefDataTask.VID, vid);
                                    intent.putExtra(SharedPrefDataTask.NoTask, noTask);
                                    intent.putExtra(SharedPrefDataTask.idJenisTask, statusTask);
                                    intent.putExtra(SharedPrefManager.SP_ID,id);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(UploadPhotoActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
                            }
                            Log.i("TAG","Ski data from server - ok" + response );
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("TAG","Ski error connect - "+error);
                            Toasty.error(UploadPhotoActivity.this, "Error!", Toast.LENGTH_SHORT, true).show();
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
                    String str = "{\"Result\": \"True\",\"Raw\": [{\"PARAM2\": [{\"WhereDatabaseinYou\": \"VID='"+vid+"' and NoTask = '"+noTask+"'\",\"FlagUploadPhoto\": "+statusUp+"}],\"PARAM1\": [{\"file_url\": \"UploadFoto/"+nameImage+"\",\"file_usercreate\": \"teknisi\",\"flagtime\":\""+time+"\",\"VID\": \""+vid+"\",\"Description\": \""+descrip+"\",\"Keterangan\": \""+keterangan+"\",\"YourImage64Name\": \""+nameImage+"\",\"YourImage64File\": \""+encodedImage+"\"}],\"Data1\": \"\",\"Data2\": \"\",\"Data3\": \"\",\"Data4\": \"\",\"Data5\": \"\",\"Data6\": \"\",\"Data7\": \"\",\"Data8\": \"\",\"Data9\": \"\",\"Data10\": \"\"}]}";
                    Log.i("sendRaw", "sendRaw: " + str);
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
                mImagesName.setText(fileName);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void choseImg(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


}
