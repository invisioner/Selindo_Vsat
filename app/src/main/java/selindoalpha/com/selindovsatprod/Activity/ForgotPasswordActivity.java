package selindoalpha.com.selindovsatprod.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Api.Mysingleton;
import selindoalpha.com.selindovsatprod.R;

public class ForgotPasswordActivity extends AppCompatActivity {


    private EditText etEmail;
    private TextView tvBback, tvSubmit;
    private String ResultWS;
    //Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.registered_emailid);
        tvBback = findViewById(R.id.backToLoginBtn);
        tvSubmit = findViewById(R.id.forgot_button);

        tvBback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForgetPassword();
            }
        });
    }

    private void sendForgetPassword() {
        String getEmail = etEmail.getText().toString();


        // Pattern for email id validation
        Pattern p = Pattern.compile(regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmail);

        // First check if email id is not null else show error toast
        if (getEmail.equals("") || getEmail.length() == 0) {

            Toasty.warning(ForgotPasswordActivity.this, "Please enter your Email!", Toast.LENGTH_SHORT, true).show();
        }
            // Check if email id is valid or not
        else if (!m.find()) {
            Toasty.warning(ForgotPasswordActivity.this, "Your Email Id is Invalid!", Toast.LENGTH_SHORT, true).show();
        }

        else {

            String url = BaseUrl.getPublicIp + BaseUrl.forgetPassword + getEmail ;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                ResultWS = object.getString("Result");
                                Log.i("forgetPassword", "Result: " + ResultWS);
                                if (ResultWS.equals("False")) {

                                    Log.i("forgetPassword", "Data1: " + object.getString("Data1"));
                                    Toasty.success(ForgotPasswordActivity.this, "Check Your Email For New Password..", Toast.LENGTH_SHORT, true).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }
                                else {

                                    Log.i("forgetPassword", "Data1: " + object.getString("Data1"));

                                    Toasty.error(ForgotPasswordActivity.this, "Failure!", Toast.LENGTH_SHORT, true).show();
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

    }
}
