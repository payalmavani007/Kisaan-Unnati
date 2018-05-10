package kisaan.sau.pro.com.kishaan;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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

/**
 * Created by hp-pc on 21-12-2017.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class SplashscreenActivity extends AppCompatActivity implements Animation.AnimationListener
{
        ImageView img_logo;

    SharedPreferences.Editor editor;
    TextView txt_kisaan;
    ImageView powerby;

    private Handler mHandler = new Handler();
    private static int SPLASH_TIME_OUT = 800;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
           // Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    Animation animFadein;
    Animation blink;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        img_logo = findViewById(R.id.img_logo);
       // txt_kisaan =findViewById(R.id.txtview_kisaan);

       /* Typeface tf = Typeface.createFromAsset(getAssets(),
                "res/segoeui.ttf");
        txt_kisaan.setTypeface(tf);*/

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        editor = permissionStatus.edit();

//        checkPermission();



        SharedPreferences sp = getApplicationContext().getSharedPreferences("monthly", Context.MODE_PRIVATE);
        //final SharedPreferences.Editor editor = sp.edit();
        sp.edit().remove("month").commit();




        animFadein = AnimationUtils.loadAnimation(SplashscreenActivity.this,
                R.anim.fadein);
/*
        blink = AnimationUtils.loadAnimation(SplashscreenActivity.this,
                R.anim.blink);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
       animFadein.setAnimationListener(SplashscreenActivity.this);
       // blink.setAnimationListener(SplashscreenActivity.this);
        img_logo.setAnimation(animFadein);
       // img_logo.setAnimation(blink);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        SharedPreferences preferences = SplashscreenActivity.this.getSharedPreferences("status", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.contains("email") && preferences.contains("password")){


            String url = "  http://kisanunnati.com/kisan/user_login?" + "email=" +preferences.getString("email","")+"&password="+ preferences.getString("password","");
            final KProgressHUD hud = KProgressHUD.create(SplashscreenActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            Log.e("Splash_login_url",url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Login Responce", response);
                    try {
                        JSONObject resp = new JSONObject(response);

                        if (resp.getInt("status") == 0) {
                            JSONArray data = resp.getJSONArray("data");
                            JSONObject object = (JSONObject) data.get(0);

                            SharedPreferences preferences = SplashscreenActivity.this.getSharedPreferences("status", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", object.getString("firstname")+" "+object.getString("lastname"));
                            editor.putString("id", object.getString("id"));
                            editor.putString("email", object.getString("email"));
                            editor.putString("empcode", object.getString("empcode"));
                            editor.putString("rolemanagement_id", object.getString("rolemanagement_id"));
                            if (object.has("talukas_id"))
                                    editor.putString("talukas_id", object.getString("talukas_id"));
                            editor.putString("districts_id", object.getString("districts_id")).apply();

                            hud.dismiss();


                            Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(SplashscreenActivity.this, Login.class);
                            startActivity(intent);
                            hud.dismiss();
                            Toast.makeText(SplashscreenActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) /*{
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("email", edt_email.getText().toString());
                    param.put("password", edt_pwd.getText().toString());
                    return param;
                }
            }*/;
            Volley.newRequestQueue(SplashscreenActivity.this).add(stringRequest);

        }else {
            mHandler.postDelayed(new Runnable() {


                @Override
                public void run() {
                    Intent intent = new Intent(SplashscreenActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
    
    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    
    
    
    
    ///runtime permission


    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (ActivityCompat.checkSelfPermission(SplashscreenActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    //ActivityCompat.checkSelfPermission(SplashscreenActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                   // ActivityCompat.checkSelfPermission(SplashscreenActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(SplashscreenActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashscreenActivity.this, android.Manifest.permission.CAMERA)
                       // || ActivityCompat.shouldShowRequestPermissionRationale(SplashscreenActivity.this, android.Manifest.permission.RECORD_AUDIO)
                       // || ActivityCompat.shouldShowRequestPermissionRationale(SplashscreenActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(SplashscreenActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SplashscreenActivity.this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(SplashscreenActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SplashscreenActivity.this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            sentToSettings = true;
                          /*  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", SplashscreenActivity.this.getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);*/
//                            Toast.makeText(SplashscreenActivity.this, "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(SplashscreenActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }


                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                editor.apply();


            } else {
                proceedAfterPermission();
            }
        } else {
        }
    }
    private void proceedAfterPermission() {
        img_logo.startAnimation(animFadein);
    }



/*
    private void proceedAfterPermission() {
        img_logo.startAnimation(blink);
    }
*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(SplashscreenActivity.this, permissionsRequired[0])
                 //   || ActivityCompat.shouldShowRequestPermissionRationale(SplashscreenActivity.this, permissionsRequired[1])
                   // || ActivityCompat.shouldShowRequestPermissionRationale(SplashscreenActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashscreenActivity.this, permissionsRequired[1])) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SplashscreenActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashscreenActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        proceedAfterPermission();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(SplashscreenActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


   /* @Override
    protected void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(SplashscreenActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();

            }
        }
        else if (ActivityCompat.checkSelfPermission(SplashscreenActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
             //   ActivityCompat.checkSelfPermission(SplashscreenActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
               //ActivityCompat.checkSelfPermission(SplashscreenActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(SplashscreenActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            checkPermission();

        }
   
   
    }*/
}
