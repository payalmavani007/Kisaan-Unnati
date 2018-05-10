package kisaan.sau.pro.com.kishaan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Payal on 3/19/2018.
 */

public class  Registration extends AppCompatActivity {
    EditText edt_title, edt_fname, edt_lname, edt_email, edt_pnum, edt_village, edt_zipcode, edt_nomifname, edt_nomipnum, edt_address, edt_addhar1, edt_addhar2_, edt_landpics, edt_empcode;
    TextView txt_taluka, txt_id,txt_id1, txt_registration, datepiker1, txt_dist, datepiker2;
    ImageView imgcal1, imgcal2;
    LinearLayout linearLayout,linear_taluka;
    RadioGroup genderGroup;
    RadioButton male, female;
    Button btn_submit;
    private int mYear, mMonth, mDay;
    ArrayAdapter<CharSequence> adapter;
    ListView lv, lv1;
    String gender= "male";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        //txt_country = findViewById(R.id.txt_india);
        txt_id = findViewById(R.id.dist_id);
        txt_id1 = findViewById(R.id.taluka_id);
        txt_registration = findViewById(R.id.txt_registration);
        edt_title = findViewById(R.id.title);
        linearLayout = findViewById(R.id.lin_unid);
        edt_fname = findViewById(R.id.firstname);
        edt_lname = findViewById(R.id.lastname);
        edt_address = findViewById(R.id.address);
        edt_pnum = findViewById(R.id.phonenumber);
        edt_village = findViewById(R.id.village);
        edt_zipcode = findViewById(R.id.zipcode);
        genderGroup = findViewById(R.id.genderGroup);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    female.setChecked(true);
                gender="female";
                }else gender="male";
            }
        });

        edt_empcode = findViewById(R.id.empcode);
        edt_email = findViewById(R.id.email);
        edt_nomifname = findViewById(R.id.niominee_fullname);
        edt_nomipnum = findViewById(R.id.nominee_phonnum);
        edt_addhar1 = findViewById(R.id.addhar1);
        edt_addhar2_ = findViewById(R.id.addhar2);
        edt_landpics = findViewById(R.id.landpics);
        imgcal1 = findViewById(R.id.img_calender1);
        imgcal2 = findViewById(R.id.img_calender2);
        datepiker1 = findViewById(R.id.datepiker_txt1);
        datepiker2 = findViewById(R.id.datepiker_txt2);
        txt_dist = findViewById(R.id.district);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.list);
                final Dialog dialog = new Dialog(Registration.this);
                dialog.setContentView(R.layout.list);
                lv = dialog.findViewById(R.id.lv);
                dialog.setCancelable(true);

                String url = "  http://kisanunnati.com/kisan/getDistrictData";
                final KProgressHUD hud = KProgressHUD.create(Registration.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Dialog Responce", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("status", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();

                            if (resp.getInt("status") == 0) {
                                JSONArray data = resp.getJSONArray("data");



                                final String[] stringArray = new String[data.length()];
                                final int[] intArray = new int[data.length()];
                                for(int i = 0, count = data.length(); i< count; i++)
                                {
                                    try {
                                        JSONObject object = (JSONObject) data.get(i);
                                        String jsonString = object.getString("districts_name");
                                        stringArray[i] = jsonString.toString();


                                        int id= object.getInt("id");
                                        intArray[i] = id;
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_list_item_1, stringArray);
                                lv.setAdapter(adapter);
                                hud.dismiss();
                                dialog.show();
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView textView = (TextView) view;
                                        txt_dist.setText(stringArray[i]);
                                       txt_id.setText(String.valueOf(intArray[i]));
                                        dialog.dismiss();
                                    }
                                });

                            }
                            else {
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
                        return param;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

                /*
                final String names[] = {"Ahmedabad", "Surat", "Rajkot"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_list_item_1, names);
                lv.setAdapter(adapter);
                dialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView textView = (TextView) view;
                        txt_dist.setText(names[i]);
                        dialog.dismiss();
                    }
                });*/
            }

        });


linear_taluka = findViewById(R.id.lin_unid2);
        txt_taluka = findViewById(R.id.taluka);
        linear_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.list);
                final Dialog dialog = new Dialog(Registration.this);
                dialog.setContentView(R.layout.listtaluka);
                lv1 = dialog.findViewById(R.id.lv1);
                dialog.setCancelable(true);

                String url = "  http://kisanunnati.com/kisan/getTalukaData?districts_id="+txt_id.getText().toString();
                final KProgressHUD hud = KProgressHUD.create(Registration.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Dialog Taluka Responce", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("status", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();

                            if (resp.getInt("status") == 0) {
                                JSONArray data = resp.getJSONArray("talukas");


                                final int[] intArray = new int[data.length()];
                                final String[] stringArray = new String[data.length()];
                                for(int i = 0, count = data.length(); i< count; i++)
                                {
                                    try {
                                        JSONObject object = (JSONObject) data.get(i);
                                        String jsonString = object.getString("talukas_name");
                                        stringArray[i] = jsonString.toString();

                                        int id= object.getInt("id");
                                        intArray[i] = id;
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_list_item_1, stringArray);
                                lv1.setAdapter(adapter);
                                hud.dismiss();
                                dialog.show();
                                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView textView = (TextView) view;
                                        txt_taluka.setText(stringArray[i]);
                                        txt_id1.setText(String.valueOf(intArray[i]));
                                        dialog.dismiss();
                                    }
                                });

                            }
                            else {
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
                        return param;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

/*
                final String names[] = {"sgdfhsd", "sagfg", "sfgdghdt"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_list_item_1, names);
                lv1.setAdapter(adapter);
                dialog.show();
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView textView = (TextView) view;
                        txt_taluka.setText(names[i]);
                        dialog.dismiss();
                    }
                });*/
            }
        });
        btn_submit = findViewById(R.id.reg_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if (edt_title.getText().toString().equals("")) {
//                    edt_title.setError("Title Required.");
                    ((TextInputLayout)findViewById(R.id.titleInputLayout)).setError("Title Required.");
                }*//* else if (edt_fname.getText().toString().equals("")) {
                    edt_fname.setError("Firstname Required.");
                } else if (edt_lname.getText().toString().equals("")) {
                    edt_lname.setError("Lastname Required.");
                } else if (edt_email.getText().toString().equals("")) {
                    edt_email.setError("Invalid Email");
                } else if (edt_pnum.getText().toString().equals("")) {
                    edt_pnum.setError("Phonenumber Required.");
                } else if (edt_village.getText().toString().equals("")) {
                    edt_village.setError("Village Required.");
                } else if (edt_zipcode.getText().toString().equals("")) {
                    edt_zipcode.setError("Zipcode Required.");
                } else if (edt_nomifname.getText().toString().equals("")) {
                    edt_nomifname.setError("Nominee name Required.");
                } else if (edt_address.getText().toString().equals("")) {
                    edt_address.setError("Address Required.");
                } else if (edt_nomipnum.getText().toString().equals("")) {
                    edt_nomipnum.setError("Number Required.");
                } else if (edt_addhar1.getText().toString().equals("")) {
                    edt_addhar1.setError("Aadharnum1 Required.");
                } else if (edt_addhar2_.getText().toString().equals("")) {
                    edt_addhar2_.setError("Aadharnum2 Required.");
                } else if (edt_landpics.getText().toString().equals("")) {
                    edt_landpics.setError("Landpics Required.");
                } else if (edt_empcode.getText().toString().equals("")) {
                    edt_empcode.setError("Empcode Required.");
                } */

            /*    if(edt_title.getText().toString().equals("") || edt_fname.getText().toString().equals("") || edt_lname.getText().toString().equals("") *//* edt_email.getText().toString().equals("")*//*
                        || edt_pnum.getText().toString().equals("") || edt_village.getText().toString().equals("") || edt_zipcode.getText().toString().equals("") || edt_nomifname.getText().toString().equals("")
                        || edt_nomipnum.getText().toString().equals("") || edt_address.getText().toString().equals("") || edt_addhar1.getText().toString().equals("") || edt_addhar2_.getText().toString().equals("") ||
                *//*edt_landpics.getText().toString().equals("") ||*//* edt_empcode.getText().toString().equals("")
                        ){
                    Toast.makeText(Registration.this, "Enter all fields.", Toast.LENGTH_SHORT).show();
                }
                else {*/

                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(c);
                    Toast.makeText(Registration.this, formattedDate, Toast.LENGTH_SHORT).show();

                    String url = "  http://kisanunnati.com/kisan/form_register?" +
                            "&title=" + edt_title.getText().toString().replace(" ","%20") +
                            "&first_name=" + edt_fname.getText().toString().replace(" ","%20") +
                            "&last_name=" + edt_lname.getText().toString().replace(" ","%20") +
                            "&phone_number=" + edt_pnum.getText().toString().replace(" ","%20") +
                            "&email=" + edt_email.getText().toString().replace(" ","%20") +
                            "&gender=" + gender.replace(" ","%20") +
                            "&date_of_birth=" + datepiker1.getText().toString().replace(" ","%20") +
                            "&address=" + edt_address.getText().toString().replace(" ","%20") +
                            "&country=" + "india" .replace(" ","%20")+
                            "&state=" + "Gujarat".replace(" ","%20") +
                            "&district=" + txt_dist.getText().toString().replace(" ","%20") +
                            "&taluka=" + txt_taluka.getText().toString() .replace(" ","%20")+
                            "&village=" + edt_village.getText().toString() .replace(" ","%20")+
                            "&zipcode=" + edt_zipcode.getText().toString() .replace(" ","%20")+
                            "&nominee_full_name=" + edt_nomifname.getText().toString() .replace(" ","%20")+
                            "&nominee_phone=" + edt_nomipnum.getText().toString() .replace(" ","%20")+
                            "&nominee_birthdate=" + datepiker2.getText().toString() .replace(" ","%20")+
                            "&nominee_aadhar=" + edt_addhar1.getText().toString() .replace(" ","%20")+
                            "&farmers_aadhar_number=" + edt_addhar2_.getText().toString() .replace(" ","%20")+
                            "&empcode=" + edt_empcode.getText().toString().replace(" ","%20")+
                            "&date="+formattedDate+
                            "district_id="+txt_id.getText().toString()+
                            "taluka_id="+txt_id1.getText().toString();

                    final KProgressHUD hud = KProgressHUD.create(Registration.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Form Responce", response);
                            try {
                                JSONObject resp = new JSONObject(response);
                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("status", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                if (resp.getInt("status") == 0) {
                                    JSONArray data = resp.getJSONArray("data");
                                    JSONObject object = (JSONObject) data.get(0);
                                    hud.dismiss();
                                    Intent intent = new Intent(Registration.this, ThankYou.class);
                                    startActivity(intent);
                                } else {
                                    hud.dismiss();
                                    Toast.makeText(Registration.this, "Invalid Registration.", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) ;
                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
//                }
            }
        });


        imgcal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Registration.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datepiker1.setText( dayOfMonth + "-" + (monthOfYear + 1) + "-" + year );

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });
        imgcal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Registration.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datepiker2.setText( dayOfMonth + "-" + (monthOfYear + 1) + "-" + year );

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
    }

}

