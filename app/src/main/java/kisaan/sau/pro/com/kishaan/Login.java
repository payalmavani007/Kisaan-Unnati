package kisaan.sau.pro.com.kishaan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity  {
    EditText edt_email, edt_pwd;
    Button btn_submit;
    TextView txtview_kishaan;
    CheckBox checkBox;
    private static final String TAG = "Login";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      //  txtview_kishaan = findViewById(R.id.txtview_kisaan);
        edt_email = findViewById(R.id.user_email);
        edt_pwd = findViewById(R.id.user_pwd);
        btn_submit = findViewById(R.id.login_submit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        btn_submit.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (edt_pwd.length() < 6 ) {
                   //edt_pwd.setError("Password ");
                    Toast.makeText(getApplicationContext(), "Password length must be greater than 6 digits.", Toast.LENGTH_SHORT).show();
                }
                 else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches()){
                     Toast.makeText(getApplicationContext(), "Invalid Email.", Toast.LENGTH_SHORT).show();


                }else {
                    String url = "  http://kisanunnati.com/kisan/user_login?" + "email=" +edt_email.getText().toString()+"&password="+ edt_pwd.getText().toString();
                    final KProgressHUD hud = KProgressHUD.create(Login.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    Log.e("splash_login_url",url);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Login Responce", response);
                            try {
                                JSONObject resp = new JSONObject(response);


                                if (resp.getInt("status") == 0) {
                                    JSONArray data = resp.getJSONArray("data");
                                    JSONObject object = (JSONObject) data.get(0);
                                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("status", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("username", object.getString("firstname")+" "+object.getString("lastname"));
                                    editor.putString("email", edt_email.getText().toString());
                                    editor.putString("id", object.getString("id"));
                                    editor.putString("empcode", object.getString("empcode"));
                                    editor.putString("password", edt_pwd.getText().toString());
                                    editor.putString("rolemanagement_id", object.getString("rolemanagement_id"));
                                    if (object.has("talukas_id"))
                                        editor.putString("talukas_id", object.getString("talukas_id"));
                                    editor.putString("districts_id", object.getString("districts_id")).apply();
                                    hud.dismiss();

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else {
                                    hud.dismiss();
                                    Toast.makeText(Login.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<>();
                            param.put("email", edt_email.getText().toString());
                            param.put("password", edt_pwd.getText().toString());
                            return param;
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
                }
            }
        });
    }

}