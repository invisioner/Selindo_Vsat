package selindoalpha.com.selindovsatprod.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;

public class ViewDetailPhotoActivity extends AppCompatActivity {

    private static final String TAG = "ViewDetailPhotoActivity";

    ImageView imageView;
    String imageurl,vid,noTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Foto Spd");

        Bundle i=getIntent().getExtras();
        if (i!=null) {
            imageurl = i.getString("ImageSpd");
            vid = i.getString(SharedPrefDataSPD.VIDSAVE);
            noTask = i.getString(SharedPrefDataSPD.NOTASKSAVE);
        }
        Log.i(TAG, "onCreate: " + imageurl);

        imageView = findViewById(R.id.imageViewDetailSpd);

        Picasso.with(this)
                .load(imageurl)
                .into(imageView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent();
            intent.putExtra(SharedPrefDataSPD.VID, vid);
            intent.putExtra(SharedPrefDataSPD.NoTask, vid);
            setResult(RESULT_OK,intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra(SharedPrefDataSPD.VID, vid);
        intent.putExtra(SharedPrefDataSPD.NoTask, vid);
        setResult(RESULT_OK,intent);
        finish();
    }
}
