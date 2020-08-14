package selindoalpha.com.selindovsatprod.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private ShowHidePasswordEditText mNewPassword,confirmPassword;
    private Button mbtnSave;
    private String ResultWS;

    //Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Change Password");

        confirmPassword = findViewById(R.id.confirm_change_password);
        mNewPassword = findViewById(R.id.new_password_change_password);
        mbtnSave = findViewById(R.id.btnSavePassword);

        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasi();
            }
        });


    }

    public void validasi(){


        String pass = mNewPassword.getText().toString();
        String conf = confirmPassword.getText().toString();

        if (!conf.equals(pass) || conf.length() == 0) {

            Toasty.warning(this, "Confirm Password is False!", Toast.LENGTH_SHORT, true).show();
        }
        else if (pass.matches("") || pass.length() == 0) {
            mNewPassword.setError("Your New Password Empty");
        }
        else {
            changePassword();
        }
    }

    public void changePassword(){
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        String conf = confirmPassword.getText().toString();
        String pass = mNewPassword.getText().toString();

        String url = BaseUrl.getPublicIp + BaseUrl.changePassword +"/"+sharedPrefManager.getSpNik()+"/"+pass+"";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            ResultWS = object.getString("Result");
                            Log.i("forgetPassword", "Result: " + ResultWS);
                            if (ResultWS.equals("True")) {

                                Log.i("forgetPassword", "Data1: " + object.getString("Data1"));

                                Toasty.success(ChangePasswordActivity.this, "successful..", Toast.LENGTH_SHORT, true).show();
                                setResult(RESULT_OK);
                                finish();
                            }else {
                                Log.i("forgetPassword", "Data1: " + object.getString("Data1"));

                                Toasty.error(ChangePasswordActivity.this, "Failed!!", Toast.LENGTH_SHORT, true).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("forgetPassword", "Error: " + error.getMessage());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Header", "Dota2");
                return headers;
            }

        };

        Mysingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
