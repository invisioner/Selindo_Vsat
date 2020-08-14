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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Adapter.ListInputSpdAdapter;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.Model.ListInputSpdModel;
import selindoalpha.com.selindovsatprod.R;

public class UploadDocumentSpdActivity extends AppCompatActivity {

    private List<ListInputSpdModel> uploadPhoto;
    private ListInputSpdAdapter listInputSpdAdapter;
    private EditText mNominal, mDeskripsi;
    private TextView mNameImages;
    private Spinner mCategory;
    private ImageView buttonChoose;
    private ImageView imageView;
    private Bitmap bitmap, decoded;
    String ResultWS;




    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100

    String encodedImage;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document_spd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("UPLOAD DATA SPD");


        mCategory = findViewById(R.id.spinnerCategory);
        mNominal = findViewById(R.id.etNominal);
        mDeskripsi = findViewById(R.id.etDescrip);
        mNameImages = findViewById(R.id.etImagesName);
        buttonChoose = findViewById(R.id.chooseImage);
        imageView = findViewById(R.id.imageView);
        uploadPhoto = new ArrayList<>();
        listInputSpdAdapter = new ListInputSpdAdapter(uploadPhoto);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        final String imgString = new String(Base64.encode(byteFormat, Base64.DEFAULT));
        String imgString2 = imgString.replaceAll("\\s+", "");
        Log.i("imgStr", "imgStr: " + imgString2);
        return imgString2;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
//        imageView.setImageBitmap(decoded);

//        Bitmap bm = BitmapFactory.decodeFile("/path/to/image.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos); //bm is the bitmap object
//        byte[] b = baos.toByteArray();

        encodedImage = encodeToBase64(bmp);


        imageView.setImageBitmap(decodeBase64(encodedImage));

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
                mNameImages.setText(fileName);
                Log.i("TAG", "onActivityResult: " + bitmap);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kosong() {
        imageView.setImageResource(0);
        mNameImages.setText(null);
    }

    public void saveUploadPhoto(View view) {

        try {
            postSpd();
        }catch (Exception e){

            kosong();
            e.getMessage();

        }


    }

    private void postSpd() {
        String url = "http://192.168.25.33:7003/vsat-api2/Service1.svc/Api-SPD/InsertSPD";

        String category = mCategory.getSelectedItem().toString();
        String nominal = mNominal.getText().toString();
        String images = mNameImages.getText().toString();
        String descrip = mDeskripsi.getText().toString();

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
                                Toasty.success(UploadDocumentSpdActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                                Intent intent = new Intent(UploadDocumentSpdActivity.this,SpdInputActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.warning(UploadDocumentSpdActivity.this, "Failure.!!.", Toast.LENGTH_SHORT, true).show();
                        }
                        Log.i("TAG","Ski data from server - ok" + response );
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG","Ski error connect - "+error);
                        Toasty.error(UploadDocumentSpdActivity.this, "Error.!!.", Toast.LENGTH_SHORT, true).show();
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
                String str = "{\"Result\":\"True\",\"Raw\":[{\"PARAM1\":[{\"NoTask\":\"2018101007371\",\"VID\":\"SCM201800010001001840\",\"NamaTeknisi\":\"admin\",\"JenisBiaya\":\""+category+"\",\"Nominal\":\""+nominal+"\",\"NamaRemote\":\"BRI%20KCP%20JKT_ROXY_CILEDUG\",\"IPLAN\":\"46.2.145.1\"}],\"PARAM2\":[{\"file_url\":\"UploadFoto/20190104035954LOGOPutih.png\",\"file_usercreate\":\"admin\",\"file_datecreate\":\"2019-01-04 15:59:54.120\",\"VID\":\"SCM201800010001001840\",\"Description\":\"Hotel\",\"Keterangan\":\""+descrip+"\",\"YourImage64Name\":\""+images+"\",\"YourImage64File\":\""+encodedImage+"\"}],\"Data1\":\"\",\"Data2\":\"\",\"Data3\":\"\",\"Data4\":\"\",\"Data5\":\"\",\"Data6\":\"\",\"Data7\":\"\",\"Data8\":\"\",\"Data9\":\"\",\"Data10\":\"\"}]}";
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

    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
