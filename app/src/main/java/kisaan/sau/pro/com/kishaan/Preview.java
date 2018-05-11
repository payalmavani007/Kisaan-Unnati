package kisaan.sau.pro.com.kishaan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kisaan.sau.pro.com.kishaan.ImageUploading.VolleyMultipartRequest;

public class Preview extends AppCompatActivity {
    TextView title_preview, gender_preview, dob_preview, nomidob_preview, district_preview, taluka_preview, fname_preview, lname_preview, pnum_preview, email_preview, villageid_preview,village_preview, zipcode_preview, nomifname_preview, nominepnum_preview, addr_preview, aadhar1_preview, aadhar2_preview, landpics_preview, empcode_preview;
    Button btn_submit, btn_edit;
    ImageView img1,img2,img3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        img1 = findViewById(R.id.preview_img1);
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("Aadharimg"))
                //.load(getString(Integer.parseInt("Aadharimg")))
                .into(img1);
        //img1.setText(getIntent().getStringExtra("Aadharimg"));

        img2 = findViewById(R.id.preview_img2);

        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("Aadharimg2"))
                //.load(getString(Integer.parseInt("Aadharimg")))
                .into(img2);

        //img2.setText(getIntent().getStringExtra("Aadharimg2"));

        img3 = findViewById(R.id.preview_img3);
        //img3.set(getIntent().getStringExtra("landpics"));

        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("landpics"))
                //.load(getString(Integer.parseInt("Aadharimg")))
                .into(img3);


        btn_edit = findViewById(R.id.preview_edit);
        btn_submit = findViewById(R.id.preview_Update);

        gender_preview = findViewById(R.id.gender_preview);
        gender_preview.setText(getIntent().getStringExtra("Gender"));

        dob_preview = findViewById(R.id.dbo_preview);
        dob_preview.setText(getIntent().getStringExtra("Date of birth"));

        district_preview = findViewById(R.id.district_preview);
        district_preview.setText(getIntent().getStringExtra("District"));

        taluka_preview = findViewById(R.id.taluka_preview);
        taluka_preview.setText(getIntent().getStringExtra("Taluka"));

        nomidob_preview = findViewById(R.id.nomidob_preview);
        nomidob_preview.setText(getIntent().getStringExtra("Nominee Birth date"));

        title_preview = findViewById(R.id.title_preview);
        title_preview.setText(getIntent().getStringExtra("title"));

        fname_preview = findViewById(R.id.fname_preview);
        fname_preview.setText(getIntent().getStringExtra("First Name"));

        lname_preview = findViewById(R.id.lname_preview);
        lname_preview.setText(getIntent().getStringExtra("Last Name"));


        pnum_preview = findViewById(R.id.pnum_preview);
        pnum_preview.setText(getIntent().getStringExtra("Phone Number"));

        email_preview = findViewById(R.id.email_preview);
        email_preview.setText(getIntent().getStringExtra("E-mail"));

        village_preview = findViewById(R.id.village_preview);
        villageid_preview = findViewById(R.id.villageid_preview);
        village_preview.setText(getIntent().getStringExtra("Village"));

        zipcode_preview = findViewById(R.id.zipcode_preview);
        zipcode_preview.setText(getIntent().getStringExtra("Zipcode"));

        nomifname_preview = findViewById(R.id.nomifnmae_preview);
        nomifname_preview.setText(getIntent().getStringExtra("Nominee Full Name"));

        nominepnum_preview = findViewById(R.id.nomipnum_preview);
        nominepnum_preview.setText(getIntent().getStringExtra("Nominee phone number"));

        addr_preview = findViewById(R.id.addr_preview);
        addr_preview.setText(getIntent().getStringExtra("Address"));

        aadhar1_preview = findViewById(R.id.aadhar1_preview);
        aadhar1_preview.setText(getIntent().getStringExtra("Aadhar1"));
/*
        aadhar2_preview = findViewById(R.id.aadhar2_preview);
        aadhar2_preview.setText(getIntent().getStringExtra("Aadhar2"));*/
/*
        landpics_preview = findViewById(R.id.landpic_preview);
        landpics_preview.setText(getIntent().getStringExtra("Landpics"));*/

        empcode_preview = findViewById(R.id.empcode_preview);
        empcode_preview.setText(getIntent().getStringExtra("Emp Code"));

//        ImageView Aadhar1_pic=findViewById(R.id.preview_aadhar1_img);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String url = "  http://kisanunnati.com/kisan/form_register?" +
//                    String url = "http://192.168.1.200/kisaan2/form_register?" +
                        "&title=" + title_preview.getText().toString().replace(" ", "%20") +
                        "&first_name=" + fname_preview.getText().toString().replace(" ", "%20") +
                        "&last_name=" + lname_preview.getText().toString().replace(" ", "%20") +
                        "&phone_number=" + pnum_preview.getText().toString().replace(" ", "%20") +
                        "&email=" + email_preview.getText().toString().replace(" ", "%20") +
                        "&gender=" + gender_preview.getText().toString().replace(" ", "%20") +
                        "&date_of_birth=" + dob_preview.getText().toString().replace(" ", "%20") +
                        "&address=" + addr_preview.getText().toString().replace(" ", "%20") +
                        "&country=" + "india".replace(" ", "%20") +
                        "&state=" + "Gujarat".replace(" ", "%20") +
                        "&district=" + district_preview.getText().toString().replace(" ", "%20") +
                        "&taluka=" + taluka_preview.getText().toString().replace(" ", "%20") +
                        "&village=" + village_preview.getText().toString().replace(" ", "%20") +
                        "&village_id=" + villageid_preview.getText().toString().replace(" ", "%20") +
                        "&zipcode=" + zipcode_preview.getText().toString().replace(" ", "%20") +
                        "&nominee_full_name=" + nomifname_preview.getText().toString().replace(" ", "%20") +
                        "&nominee_phone=" + nominepnum_preview.getText().toString().replace(" ", "%20") +
                        "&nominee_birthdate=" + nomidob_preview.getText().toString().replace(" ", "%20") +
                        "&nominee_aadhar=123123123"+
                        "&farmers_aadhar_number="  + aadhar1_preview.getText().toString().replace(" ", "%20") /*+ aadhar2_preview.getText().toString().replace(" ", "%20") */+
                        "&empcode=" + empcode_preview.getText().toString().replace(" ", "%20") +
                        "&date=" + df +
                        "&district_id=" + getIntent().getStringExtra("dist_id") +
                        "&taluka_id=" + getIntent().getStringExtra("taluka_id")+
                        "&aadhar1="+getIntent().getStringExtra("Aadharimg")+
                        "&aadhar2="+getIntent().getStringExtra("Aadharimg2")+
                        "&lands_pics="+getIntent().getStringExtra("landpics");

                final KProgressHUD hud = KProgressHUD.create(Preview.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();


                Log.e("form_url", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Form Responce", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("status", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();

                            if (resp.getInt("status") == 0) {
                                JSONArray data = resp.getJSONArray("data");
                                JSONObject object = (JSONObject) data.get(0);
                                hud.dismiss();


                             /*   if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 1) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminProfileFragment()).commit();
                                }
                                if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 2) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                                }
                                if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 3) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TalukaProfileFragment()).commit();
                                }
                                if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 4) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EmployeeProfileFragment(Integer.parseInt(preferences.getString("id", "")))).commit();
                                }*/

                                Intent intent = new Intent(getApplicationContext(), ThankYou.class);
//                                    intent.putExtra("title", edt_title.getText().toString());
                                startActivity(intent);
                            } else {
                                hud.dismiss();
                                Toast.makeText(getApplicationContext(), "Invalid Registration.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }
        });
    }
}