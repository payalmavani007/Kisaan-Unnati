package kisaan.sau.pro.com.kishaan;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import kisaan.sau.pro.com.kishaan.ImageUploading.Config;
import kisaan.sau.pro.com.kishaan.ImageUploading.VolleyMultipartRequest;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends Fragment {
    EditText edt_fname, edt_lname, edt_email, edt_pnum,  edt_zipcode, edt_nomifname, edt_nomipnum, edt_address, edt_addhar1, edt_landpics, edt_empcode;
    TextView txt_taluka,txt_village, edt_title,edt_village, txt_id, txt_id1,txt_villageid,txt_id4, txt_registration, datepiker_date_txt1, datepiker1, txt_dist, datepiker_date_txt2, datepiker2;
    ImageView imgcal1, imgcal2;
    LinearLayout linearLayout, linear_taluka;
    RadioGroup genderGroup;
    RadioButton male, female;
    Button btn_submit, btn_addimg1, btn_addimg2, btn_addimg3;
    private int mYear, mMonth, mDay;
    ArrayAdapter<CharSequence> adapter;
    ListView lv, lv1;
    String gender = "male";
    DatePickerDialog datePickerDialog;
    int CAMERA_REQUEST = 1001;
    int GALLERY_REQUEST = 1002;
    int CROP_REQUEST = 1003;
    final int PIC_CROP = 1;
    String userChoosenTask;
    AlertDialog dialog;
    TextView addimg_uid, addimg_uid2, addimg_uid3;
    ScrollView scroll;
    SharedPreferences preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);

        btn_addimg1 = view.findViewById(R.id.add_image1);
        btn_addimg2 = view.findViewById(R.id.add_image2);
        btn_addimg3 = view.findViewById(R.id.add_image3);
        //scroll.fullScroll(View.generateViewId(R.id.empcode));

/*
scroll.postDelayed(new Runnable() {
    @Override
    public void run() {
        scroll.fullScroll(ScrollView.FOCUS_DOWN);
    }
},1000);
*/
      /*  btn_addimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Button click 1", Toast.LENGTH_SHORT).show();
            }
        });
*/
        addimg_uid = view.findViewById(R.id.addimg_uid);
        addimg_uid2 = view.findViewById(R.id.addimg_uid2);
        addimg_uid3 = view.findViewById(R.id.addimg_uid3);
     /* LinearLayout addImageLayout1=view.findViewById(R.id.addImageLayout1);
        btn_addimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Button 1 clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

        //txt_country = findViewById(R.id.txt_india);
        txt_id = view.findViewById(R.id.dist_id);
        txt_id1 = view.findViewById(R.id.taluka_id);

        txt_village= view.findViewById(R.id.formvillage_txt);
        txt_villageid = view.findViewById(R.id.village_id);
        txt_registration = view.findViewById(R.id.txt_registration);


        edt_title = view.findViewById(R.id.title);
        edt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name[] = {"Mr.", "Mrs.", "Miss"};

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.list);
                lv = dialog.findViewById(R.id.lv);
                dialog.setCancelable(true);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, name);
                lv.setAdapter(adapter);
                dialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView textView = (TextView) view;
                        edt_title.setText(name[i]);
                        dialog.dismiss();
                    }
                });

            }

        });


        linearLayout = view.findViewById(R.id.lin_unid);
        edt_fname = view.findViewById(R.id.firstname);
        edt_lname = view.findViewById(R.id.lastname);
        edt_address = view.findViewById(R.id.address);
        edt_pnum = view.findViewById(R.id.phonenumber);
        edt_village = view.findViewById(R.id.village);
        edt_zipcode = view.findViewById(R.id.zipcode);
        genderGroup = view.findViewById(R.id.genderGroup);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    female.setChecked(true);
                    gender = "male";
                } else gender = "female";
            }
        });

        edt_empcode = view.findViewById(R.id.empcode);
        edt_email = view.findViewById(R.id.email);
        edt_nomifname = view.findViewById(R.id.niominee_fullname);
        edt_nomipnum = view.findViewById(R.id.nominee_phonnum);
        edt_addhar1 = view.findViewById(R.id.addhar1);

        //edt_addhar2_ = view.findViewById(R.id.addhar2);
        edt_landpics = view.findViewById(R.id.landpics);
        imgcal1 = view.findViewById(R.id.img_calender1);
        imgcal2 = view.findViewById(R.id.img_calender2);
        datepiker1 = view.findViewById(R.id.datepiker_txt1);
        datepiker_date_txt1 = view.findViewById(R.id.datepiker_date_txt1);
        datepiker2 = view.findViewById(R.id.datepiker_txt2);
        datepiker_date_txt2 = view.findViewById(R.id.datepiker_date_txt2);
        txt_dist = view.findViewById(R.id.district);

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.listtaluka);
        final ListView lv1 = dialog.findViewById(R.id.lv1);
        dialog.setCancelable(true);


        if (preferences.getString("districts_id","").equals("0")){
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final KProgressHUD hud = KProgressHUD.create(getContext())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(true)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.list);
                    lv = dialog.findViewById(R.id.lv);
                    dialog.setCancelable(true);
                    String url = " http://kisanunnati.com/kisan/getDistrictData";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Dialog Responce", response);
                            try {
                                JSONObject resp = new JSONObject(response);
                                SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                if (resp.getInt("status") == 0) {
                                    JSONArray data = resp.getJSONArray("data");

                                hud.dismiss();
                                    final String[] stringArray = new String[data.length()];
                                    final int[] intArray = new int[data.length()];
                                    for (int i = 0, count = data.length(); i < count; i++) {
                                        try {
                                            JSONObject object = (JSONObject) data.get(i);
                                            String jsonString = object.getString("districts_name");
                                            stringArray[i] = jsonString;
                                            int id = object.getInt("id");
                                            intArray[i] = id;

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
                                    lv.setAdapter(adapter);
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

                                } else {
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
                            return new HashMap<>();
                        }
                    };
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                }});
        }else {
            txt_id.setText(preferences.getString("districts_id",""));
            final KProgressHUD hud = KProgressHUD.create(getContext())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

            final String url = " http://kisanunnati.com/kisan/districts_talukas_list?districts_id=" + txt_id.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Dialog Responce", response);
                    Log.e("taluka url",url);
                    try {                        JSONObject resp = new JSONObject(response);
hud.dismiss();
                        txt_dist.setText(resp.getString("District_name"));
                        txt_dist.setClickable(false);

                        if (resp.getInt("status") == 0) {
                            JSONArray data = resp.getJSONArray("data");


                            final String[] stringArray = new String[data.length()];
                            final int[] intArray = new int[data.length()];
                            for (int i = 0, count = data.length(); i < count; i++) {
                                try {
                                    JSONObject object = (JSONObject) data.get(i);
                                    String jsonString = object.getString("districts_name");
                                    stringArray[i] = jsonString;
                                    int id = object.getInt("id");
                                    intArray[i] = id;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
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
                return new HashMap<>();
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
        }



       // if (preferences.getString("talukas_id","").equals("0")){
           LinearLayout linear_taluka = view.findViewById(R.id.lin_unid2);

        linear_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final KProgressHUD hud = KProgressHUD.create(getContext())
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.listtaluka);
                final ListView lv1 = dialog.findViewById(R.id.lv1);
                dialog.setCancelable(true);

//                String url = " http://kisanunnati.com/kisan/fetch_talukas_name";
                        //?talukas_id=" + txt_id1.getText().toString()
                final String url = " http://kisanunnati.com/kisan/districts_talukas_list?districts_id=" + txt_id.getText().toString();

               // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Dialog Responce", response);
                        try {
                            JSONObject resp = new JSONObject(response);
hud.dismiss();
                            if (resp.getInt("status") == 0) {
                                JSONArray data = resp.getJSONArray("talukas");


                                final String[] stringArray = new String[data.length()];
                                final int[] intArray = new int[data.length()];
                                for (int i = 0, count = data.length(); i < count; i++) {
                                    try {
                                        JSONObject object = (JSONObject) data.get(i);
                                        String jsonString = object.getString("talukas_name");
                                        stringArray[i] = jsonString;
                                        int id = object.getInt("id");
                                        intArray[i] = id;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
                                lv1.setAdapter(adapter);

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

                            } else {
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
                        return new HashMap<>();
                    }
                };
                Volley.newRequestQueue(getContext()).add(stringRequest);



            }
                                });

        LinearLayout linear_taluka4 = view.findViewById(R.id.village_layout);
        txt_id4= view.findViewById(R.id.formvillage_txt);


        txt_id4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final KProgressHUD hud = KProgressHUD.create(getContext())
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.listtaluka);
               final ListView lv1 = dialog.findViewById(R.id.lv1);
                dialog.setCancelable(true);

//                String url = " http://kisanunnati.com/kisan/fetch_talukas_name";
                //?talukas_id=" + txt_id1.getText().toString()
                final String url = " http://kisanunnati.com/kisan/talukas_village_list?talukas_id=" + txt_id1.getText().toString();

                // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Dialog Responce", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            hud.dismiss();
                            if (resp.getInt("status") == 0) {
                                JSONArray data = resp.getJSONArray("village");


                                final String[] stringArray = new String[data.length()];
                                final int[] intArray = new int[data.length()];
                                for (int i = 0, count = data.length(); i < count; i++) {
                                    try {
                                        JSONObject object = (JSONObject) data.get(i);
                                        String jsonString = object.getString("village_name");
                                        stringArray[i] = jsonString;
                                        int id = object.getInt("id");
                                        intArray[i] = id;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
                                lv1.setAdapter(adapter);
                                dialog.show();
                                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView textView = (TextView) view;

                                        txt_village.setText(stringArray[i]);
                                        txt_villageid.setText(String.valueOf(intArray[i]));

                                        dialog.dismiss();


                                    }
                                });

                            } else {
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
                        return new HashMap<>();
                    }
                };
                Volley.newRequestQueue(getContext()).add(stringRequest);

            }
        });



        /*linear_taluka4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Village layout clicked", Toast.LENGTH_SHORT).show();
                final KProgressHUD hud = KProgressHUD.create(getContext())
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.listtaluka);
                lv1 = dialog.findViewById(R.id.lv1);
                dialog.setCancelable(true);
                //final String url = " http://kisanunnati.com/kisan/talukas_village_list?talukas_id=" + txt_id.getText().toString();
                final String url = "http://kisanunnati.com/kisan/fetch_village_name?village_id"
                        + txt_id4.getText().toString();
              //  http://192.168.1.200/kishaan2/user_village?user_id=8&talukas_id=2&date1=2018-03-30&date2&month_flag=0
              //  http://kisanunnati.com/kisan/fetch_village_name?village_id=1
                // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Dialog Responce", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            hud.dismiss();
                            if (resp.getInt("status") == 0) {
                                JSONArray data = resp.getJSONArray("villages");
                                Log.e("Village response array",data.toString());

                                final String[] stringArray = new String[data.length()];
                                final int[] intArray = new int[data.length()];
                                for (int i = 0, count = data.length(); i < count; i++) {
                                    try {
                                        JSONObject object = (JSONObject) data.get(i);
                                        String jsonString = object.getString("village_name");
                                        stringArray[i] = jsonString;
                                        int id = object.getInt("id");
                                        intArray[i] = id;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
                                lv1.setAdapter(adapter);
                                dialog.show();
                                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView textView = (TextView) view;

                                        edt_village.setText(stringArray[i]);
                                        txt_id4.setText(String.valueOf(intArray[i]));

                                        dialog.dismiss();


                                    }
                                });

                            } else {
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
                        return new HashMap<>();
                    }
                };
                Volley.newRequestQueue(getContext()).add(stringRequest);



    }
        });*/








         /*                   else {
            txt_id1.setText(preferences.getString("talukas_id",""));

            final String url = " http://kisanunnati.com/kisan/fetch_talukas_name?talukas_id=" + txt_id1.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Dialog Responce", response);
                    Log.e("taluka url",url);
                    try {

                        JSONObject resp = new JSONObject(response);
                        SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                       // Toast.makeText(getContext(), "dgdshgdhdshgdfhdshhfgd", Toast.LENGTH_SHORT).show();

                        //txt_dist.setClickable(false);


                        if (resp.getInt("status") == 0) {
                            txt_taluka.setText(resp.getString("taluka_name"));
*//*
                            JSONArray data = resp.getJSONArray("data");
                            Toast.makeText(getContext(), "dfghdshfhd", Toast.LENGTH_SHORT).show();
                            final String[] stringArray = new String[data.length()];
                            final int[] intArray = new int[data.length()];
                            for (int i = 0, count = data.length(); i < count; i++) {
                                try {
                                    JSONObject object = (JSONObject) data.get(i);
                                    String jsonString = object.getString("talukas_name");
                                    stringArray[i] = jsonString;
                                    int id = object.getInt("id");
                                    intArray[i] = id;

*//*
*//*
                                    txt_taluka.setText(stringArray[i]);
                                    txt_id1.setText(String.valueOf(intArray[i]));*//**//*

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
*//*
                            }




                        } else {
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
                    return new HashMap<>();
                }
            };
            Volley.newRequestQueue(getContext()).add(stringRequest);


        }*/




        /*linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  if (txt_id.getText().toString().equals("0") ) {


                    if (preferences.getString("districts_id", "").equals("0")) {
                        //as it is

                        //setContentView(R.layout.list);


                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.list);
                        lv = dialog.findViewById(R.id.lv);
                        dialog.setCancelable(true);

                        String url = " http://kisanunnati.com/kisan/getDistrictData";
                        final KProgressHUD hud = KProgressHUD.create(getContext())
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setCancellable(true)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .show();
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Dialog Responce", response);
                                try {
                                    JSONObject resp = new JSONObject(response);
                                    SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    if (resp.getInt("status") == 0) {
                                        JSONArray data = resp.getJSONArray("data");


                                        final String[] stringArray = new String[data.length()];
                                        final int[] intArray = new int[data.length()];
                                        for (int i = 0, count = data.length(); i < count; i++) {
                                            try {
                                                JSONObject object = (JSONObject) data.get(i);
                                                String jsonString = object.getString("districts_name");
                                                stringArray[i] = jsonString;
                                                int id = object.getInt("id");
                                                intArray[i] = id;
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
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

                                    } else {
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
                                return new HashMap<>();
                            }
                        };
                        Volley.newRequestQueue(getContext()).add(stringRequest);

                    } else {
                        txt_id.setText(preferences.getString("districts_id",""));

                        //new api for district
                        txt_dist.setClickable(false
                        );
                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.list);
                        lv = dialog.findViewById(R.id.lv);
                        dialog.setCancelable(true);

                        String url = " http://kisanunnati.com/kisan/districts_talukas_list?districts_id=" + txt_id.getText().toString();

                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Dialog Responce", response);
                                try {
                                    JSONObject resp = new JSONObject(response);
                                    SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    txt_dist.setText(resp.getString("District_name"));
                                    txt_dist.setClickable(false);

                                    if (resp.getInt("status") == 0) {
                                        JSONArray data = resp.getJSONArray("data");


                                        final String[] stringArray = new String[data.length()];
                                        final int[] intArray = new int[data.length()];
                                        for (int i = 0, count = data.length(); i < count; i++) {
                                            try {
                                                JSONObject object = (JSONObject) data.get(i);
                                                String jsonString = object.getString("districts_name");
                                                stringArray[i] = jsonString;
                                                int id = object.getInt("id");
                                                intArray[i] = id;
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
                                        lv1.setAdapter(adapter);
                                 *//*   dialog.show();
                                        *//*
                                        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                TextView textView = (TextView) view;
                                                txt_taluka.setText(stringArray[i]);
                                                txt_id1.setText(String.valueOf(intArray[i]));
                                                dialog.dismiss();
                                            }

                                        });
*//*
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
                                    lv.setAdapter(adapter);*//*
                                    *//*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            TextView textView = (TextView) view;
                                            txt_dist.setText(intArray[i]);
                                            txt_id.setText(String.valueOf(intArray[i]));
                                            dialog.dismiss();
                                        }
                                    }*//*
                                        ;

                                    } else {
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
                                return new HashMap<>();
                            }
                        };
                        Volley.newRequestQueue(getContext()).add(stringRequest);

                    }
                //}
                //as it is




                //=====


            }
        });
        */
        LinearLayout lin_unid2=view.findViewById(R.id.lin_unid2);
        /*lin_unid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_id1.getText().toString().equals("0")) {
                    if (preferences.getString("talukas_id", "").equals("0"))
                    //setContentView(R.layout.list);
                    {
                        dialog.show();
                        *//*String url = " http://kisanunnati.com/kisan/getTalukaData?districts_id=" + txt_id.getText().toString();
                        final KProgressHUD hud = KProgressHUD.create(getContext())
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setCancellable(true)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .show();
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Dialog Taluka Responce", response);
                                try {
                                    JSONObject resp = new JSONObject(response);
                                    SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    if (resp.getInt("status") == 0) {
                                        JSONArray data = resp.getJSONArray("talukas");


                                        final String[] stringArray = new String[data.length()];
                                        final int[] intArray = new int[data.length()];
                                        for (int i = 0, count = data.length(); i < count; i++) {
                                            try {
                                                JSONObject object = (JSONObject) data.get(i);
                                                String jsonString = object.getString("talukas_name");
                                                stringArray[i] = jsonString;

                                                int id = object.getInt("id");
                                                intArray[i] = id;
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
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

                                    } else {
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
                                return new HashMap<>();

                            }
                        };
                        Volley.newRequestQueue(getContext()).add(stringRequest);*//*
                    } else {
                        //new api for taluka


                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.listtaluka);
                        lv1 = dialog.findViewById(R.id.lv1);
                        dialog.setCancelable(true);

                        String url = " http://kisanunnati.com/kisan/fetch_talukas_name?talukas_id=" + txt_id.getText().toString();
                        final KProgressHUD hud = KProgressHUD.create(getContext())
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setCancellable(true)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .show();
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Dialog Taluka Responce", response);
                                try {
                                    JSONObject resp = new JSONObject(response);
                                    SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    if (resp.getInt("status") == 0) {
                                        JSONArray data = resp.getJSONArray("talukas");


                                        final String[] stringArray = new String[data.length()];
                                        final int[] intArray = new int[data.length()];
                                        for (int i = 0, count = data.length(); i < count; i++) {
                                            try {
                                                JSONObject object = (JSONObject) data.get(i);
                                                String jsonString = object.getString("talukas_name");
                                                stringArray[i] = jsonString;

                                                int id = object.getInt("id");
                                                intArray[i] = id;
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
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

                                    } else {
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
                                return new HashMap<>();

                            }
                        };
                        Volley.newRequestQueue(getContext()).add(stringRequest);


                    }
                }
            }
        });*/

       /* linear_taluka = view.findViewById(R.id.lin_unid2);
        linear_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), txt_id1.getText().toString(), Toast.LENGTH_SHORT).show();
                if (txt_id1.getText().toString().equals("0")) {

                    //setContentView(R.layout.list);
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.listtaluka);
                    lv1 = dialog.findViewById(R.id.lv1);
                    dialog.setCancelable(true);

                    String url = " http://kisanunnati.com/kisan/getTalukaData?districts_id=" + txt_id.getText().toString();
                    final KProgressHUD hud = KProgressHUD.create(getContext())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(true)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Dialog Taluka Responce", response);
                            try {
                                JSONObject resp = new JSONObject(response);
                                SharedPreferences preferences = getContext().getSharedPreferences("status", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                if (resp.getInt("status") == 0) {
                                    JSONArray data = resp.getJSONArray("talukas");


                                    final String[] stringArray = new String[data.length()];
                                    final int[] intArray = new int[data.length()];
                                    for (int i = 0, count = data.length(); i < count; i++) {
                                        try {
                                            JSONObject object = (JSONObject) data.get(i);
                                            String jsonString = object.getString("talukas_name");
                                            stringArray[i] = jsonString;

                                            int id = object.getInt("id");
                                            intArray[i] = id;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArray);
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

                                } else {
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
                            return new HashMap<>();

                        }
                    };
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } else {
                    //new api for taluka
                }
            }
        });*/

        String df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        datepiker1.setText(df);
        datepiker2.setText(df);
        String df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        datepiker_date_txt1.setText(df1);
        datepiker_date_txt2.setText(df1);

/*
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  */              //setContentView(R.layout.list);

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
/*
    }
        });*/



        txt_taluka = view.findViewById(R.id.taluka);
        /*linear_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        */
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
/*
            }
        });
*/


        edt_empcode.setText(getContext().getSharedPreferences("status", MODE_PRIVATE).getString("empcode", ""));
        btn_submit = view.findViewById(R.id.reg_submit);
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


                /* edt_email.getText().toString().equals("")*/
                /*edt_landpics.getText().toString().equals("") ||*/


                if (edt_title.getText().toString().equals("") || edt_fname.getText().toString().equals("") || edt_lname.getText().toString().equals("")

                        || edt_pnum.getText().toString().equals("") || txt_village.getText().toString().equals("") || edt_zipcode.getText().toString().equals("") || edt_nomifname.getText().toString().equals("")
                        || edt_nomipnum.getText().toString().equals("") || edt_address.getText().toString().equals("") || edt_addhar1.getText().toString().equals("") /*|| edt_addhar2_.getText().toString().equals("")*/ ||
                        edt_empcode.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter all fields.", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches()) {
                    Toast.makeText(getContext(), "Invalid Email.", Toast.LENGTH_SHORT).show();


                } else if (edt_pnum.getText().toString().length() != 10) {
                    Toast.makeText(getContext(), "Invalid Phone number", Toast.LENGTH_SHORT).show();
                } else if (edt_zipcode.getText().toString().length() != 6) {
                    Toast.makeText(getContext(), "Invalid Zipcode", Toast.LENGTH_SHORT).show();
                } else if (edt_nomipnum.getText().toString().length() != 10) {
                    Toast.makeText(getContext(), "Invalid Nominee Phone number", Toast.LENGTH_SHORT).show();
                } else if (edt_addhar1.getText().toString().length() != 16) {
                    Toast.makeText(getContext(), "Invalid Farmer's Aadhar number", Toast.LENGTH_SHORT).show();
                }

                /*else if (Aadhar1Str.equals("") || Aadhar2Str.equals("") ||LandpicsStr.equals("") ){
                    Toast.makeText(getContext(), "Upload pics.", Toast.LENGTH_SHORT).show();
                }*/
                else if (addimg_uid.getText().toString().equals("") || addimg_uid2.getText().toString().equals("") || addimg_uid3.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Upload All Images.", Toast.LENGTH_SHORT).show();
                } else {
                    /*String df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    String url = " http://kisanunnati.com/kisan/form_register?" +
//                    String url = "http://192.168.1.200/kisaan2/form_register?" +
                            "&title=" + edt_title.getText().toString().replace(" ", "%20") +
                            "&first_name=" + edt_fname.getText().toString().replace(" ", "%20") +
                            "&last_name=" + edt_lname.getText().toString().replace(" ", "%20") +
                            "&phone_number=" + edt_pnum.getText().toString().replace(" ", "%20") +
                            "&email=" + edt_email.getText().toString().replace(" ", "%20") +
                            "&gender=" + gender.replace(" ", "%20") +
                            "&date_of_birth=" + datepiker_date_txt1.getText().toString().replace(" ", "%20") +
                            "&address=" + edt_address.getText().toString().replace(" ", "%20") +
                            "&country=" + "india".replace(" ", "%20") +
                            "&state=" + "Gujarat".replace(" ", "%20") +
                            "&district=" + txt_dist.getText().toString().replace(" ", "%20") +
                            "&taluka=" + txt_taluka.getText().toString().replace(" ", "%20") +
                            "&village=" + edt_village.getText().toString().replace(" ", "%20") +
                            "&zipcode=" + edt_zipcode.getText().toString().replace(" ", "%20") +
                            "&nominee_full_name=" + edt_nomifname.getText().toString().replace(" ", "%20") +
                            "&nominee_phone=" + edt_nomipnum.getText().toString().replace(" ", "%20") +
                            "&nominee_birthdate=" + datepiker_date_txt2.getText().toString().replace(" ", "%20") +
                            "&nominee_aadhar=" + edt_addhar1.getText().toString().replace(" ", "%20") +
                            "&farmers_aadhar_number=" + edt_addhar2_.getText().toString().replace(" ", "%20") +
                            "&empcode=" + edt_empcode.getText().toString().replace(" ", "%20")+
                            "&date="+df+
                            "&district_id="+txt_id.getText().toString()+
                            "&taluka_id="+txt_id1.getText().toString()
                            ;

                    final KProgressHUD hud = KProgressHUD.create(getContext())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();


                    Log.e("form_url",url);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Form Responce", response);
                            try {
                                JSONObject resp = new JSONObject(response);
                                SharedPreferences preferences = getContext().getSharedPreferences("status", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                if (resp.getInt("status") == 0) {
                                    JSONArray data = resp.getJSONArray("data");
                                    JSONObject object = (JSONObject) data.get(0);
                                    hud.dismiss();

*//*
                                    if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 1) {
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminProfileFragment()).commit();
                                    }
                                    if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 2) {
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                                    }
                                    if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 3) {
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new TalukaProfileFragment()).commit();
                                    }
                                    if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 4) {
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new EmployeeProfileFragment(Integer.parseInt(preferences.getString("id", "")))).commit();
                                    }*//*

                                    Intent intent = new Intent(getContext(), Preview.class);

                                    intent.putExtra("title", edt_title.getText().toString());




                                    startActivity(intent);
                                } else {
                                    hud.dismiss();
                                    Toast.makeText(getContext(), "Invalid Registration.", Toast.LENGTH_SHORT).show();
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
                    Volley.newRequestQueue(getContext()).add(stringRequest);*/

                    Intent intent = new Intent(getContext(), Preview.class);
                    intent.putExtra("title", edt_title.getText().toString());
                    intent.putExtra("First Name", edt_fname.getText().toString());
                    intent.putExtra("Last Name", edt_lname.getText().toString());
                    intent.putExtra("Phone Number", edt_pnum.getText().toString());
                    intent.putExtra("E-mail", edt_email.getText().toString());
                    intent.putExtra("Gender", gender);
                    intent.putExtra("Date of birth", datepiker_date_txt1.getText().toString());
                    intent.putExtra("District", txt_dist.getText().toString());
                    intent.putExtra("Taluka", txt_taluka.getText().toString());
                    intent.putExtra("Village", txt_village.getText().toString());
                    intent.putExtra("Villageid", txt_villageid.getText().toString());
                    intent.putExtra("Zipcode", edt_zipcode.getText().toString());
                    intent.putExtra("Nominee Full Name", edt_nomifname.getText().toString());
                    intent.putExtra("Nominee phone number", edt_nomipnum.getText().toString());
                    intent.putExtra("Nominee Birth date", datepiker_date_txt2.getText().toString());
                    intent.putExtra("Address", edt_address.getText().toString());
                    intent.putExtra("Aadhar1", edt_addhar1.getText().toString());
                    intent.putExtra("Aadharimg", addimg_uid.getText().toString());
                    intent.putExtra("Aadharimg2", addimg_uid2.getText().toString());
                    intent.putExtra("landpics", addimg_uid3.getText().toString());
                    //  intent.putExtra("Aadhar2", edt_addhar2_.getText().toString());

                    if (!LandpicsStr.equals(""))
                        intent.putExtra("Landpics", LandpicsStr);

                    if (!Aadhar1Str.equals(""))
                        intent.putExtra("Aadhar1", Aadhar1Str);

                    if (!Aadhar2Str.equals(""))
                        intent.putExtra("Aadhar2", Aadhar2Str);
                    intent.putExtra("Emp Code", edt_empcode.getText().toString());
                    intent.putExtra("dist_id", txt_id.getText().toString());
                    intent.putExtra("taluka_id", txt_id1.getText().toString());

                    startActivity(intent);
                }
            }
        });

        imgcal1.setOnClickListener(new View.OnClickListener()

        {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Date dx = new Date(year - 1900, monthOfYear, dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(dx);

//                                datepiker1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                datepiker_date_txt1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//                                datepiker1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                datepiker1.setText(cdate);
                                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();


            }
        });
        imgcal2.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Date dx = new Date(year - 1900, monthOfYear, dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(dx);
                                datepiker2.setText(cdate);
//                                datepiker2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                datepiker_date_txt2.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

            }
        });

//image uploading
//        if (addimg_uid == null) {

        btn_addimg1.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                selectImage();
                Aadhar1 = 1;
                Aadhar2 = 0;
                Landpics = 0;
            }
        });

        btn_addimg2.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                selectImage();
                Aadhar1 = 0;
                Aadhar2 = 1;
                Landpics = 0;
            }
        });
        btn_addimg3.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                selectImage();
                Aadhar1 = 0;
                Aadhar2 = 0;
                Landpics = 1;
            }
        });


/*
        else {

            btn_addimg1.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    updateselectImage();
                    Aadhar1 = 1;
                    Aadhar2 = 0;
                    Landpics = 0;
                }
            });

            btn_addimg2.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    updateselectImage();
                    Aadhar1 = 0;
                    Aadhar2 = 1;
                    Landpics = 0;
                }
            });
            btn_addimg3.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    updateselectImage();
                    Aadhar1 = 0;
                    Aadhar2 = 0;
                    Landpics = 1;
                }
            });

        }
*/
        return view;

    }


    int Aadhar1 = 0;
    int Aadhar2 = 0;
    int Landpics = 0;

    String Aadhar1Str = "";
    String Aadhar2Str = "";
    String LandpicsStr = "";

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

/*

    private void updateselectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

*/


    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                onSelectFromGalleryResult(data);
                Bundle extras = data.getExtras();
                assert extras != null;
//                final Bitmap imageBitmap = (Bitmap) extras.get("data");

                Bitmap imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), data.getData());
                    uploadBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                convertToString(imageBitmap);
            } else if (requestCode == CAMERA_REQUEST) {
                onCaptureImageResult(data);
                Bundle extras = data.getExtras();
                assert extras != null;
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
//                convertToString(imageBitmap);
                uploadBitmap(imageBitmap);
            } else if (requestCode == CROP_REQUEST) {
                Bundle extras = data.getExtras();
                assert extras != null;
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
            }

        }
    }

    public void convertToString(Bitmap encodedImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        assert encodedImage != null;
        encodedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        uploadImage(encodedImageString);
    }

    //Gallary image result
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), data.getData());

                //uploadImage("data");
                //addimg_uid.getText().toString();
/*

                String uploadURL;
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadURL,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {
                                    //hud.dismiss();
                                    JSONObject obj = new JSONObject(new String(response.data));

                                    if (obj.getInt("status") == 1) {


                                        Log.e("response", obj.toString());
                                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                        if (Aadhar1 == 1) {
                                            addimg_uid.setText(obj.getString("imageurl"));

                                        }
                                        if (Aadhar2 == 1)
                                            addimg_uid2.setText(obj.getString("imageurl"));

                                        if (Landpics == 1)
                                            addimg_uid3.setText(obj.getString("imageurl"));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });*/


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap bb = getResizedBitmap(bm, 500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();


        assert data != null;

    }


    //Resize of bitmap
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

    //Upload image
    public void uploadImage(final String encodedImageString) {

        String uploadURL = "";
        if (Aadhar1 == 1) {
//            uploadURL = "192.168.1.200/kishaan2/aadhar1_image";/*?aadhar1_image="+encodedImageString;*/
            uploadURL = " http://kisanunnati.com/kisan/aadhar1_image?image=" + encodedImageString;/*?aadhar1_image="+encodedImageString;*/

        }
        if (Aadhar2 == 1)
            uploadURL = " http://kisanunnati.com/kisan/aadhar2_image";/*?aadhar1_image="+encodedImageString;*/
//            uploadURL = "192.168.1.200/kishaan2/aadhar2_image";/*?aadhar1_image="+encodedImageString;*/
        if (Landpics == 1)
            uploadURL = " http://kisanunnati.com/kisan/lands_pics";/*?aadhar1_image="+encodedImageString;*/
//            uploadURL = "192.168.1.200/kishaan2/lands_pics";/*?aadhar1_image="+encodedImageString;*/

        Log.e("image uploading url", uploadURL);
        Log.e("image uploading url", uploadURL);
        Log.e("encoded image string ", encodedImageString.replace("\n", "%0A").replace(" ", "%20"));

        if (!uploadURL.equals("")) {
            //sending image to server
            StringRequest request = new StringRequest(Request.Method.POST, uploadURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getInt("status") == 0) {
                            if (Aadhar1 == 1) {
                                Aadhar1Str = object.getString("imageurl");
                                //btn_addimg1.setText("Done");
                            }
                            if (Aadhar2 == 1) {
                                Aadhar2Str = object.getString("imageurl");
                                //btn_addimg1.setText("Done");

                            }
                            if (Landpics == 1) {
                                LandpicsStr = object.getString("imageurl");
                                //btn_addimg1.setText("Done");

                            }

                            Toast.makeText(getContext(), "Upload Sucessfully", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                    Log.e("Error ", volleyError.toString());
                }
            })/* {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("image", encodedImageString);
                    return checkParams(parameters);
                }
            }*/;

            request.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue rQueue = Volley.newRequestQueue(getContext());
            rQueue.add(request);
        }
    }

    private Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bb = getResizedBitmap(thumbnail, 500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();


    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext

        //our custom volley request

        String uploadURL = "";
        if (Aadhar1 == 1) {
            uploadURL = Config.FILE_UPLOAD_URL;
        }
        if (Aadhar2 == 1)
            uploadURL = Config.FILE_UPLOAD_URL1;
        if (Landpics == 1)
            uploadURL = Config.FILE_UPLOAD_URL2;

        if (!uploadURL.equals("")) {
            final KProgressHUD hud = KProgressHUD.create(getContext())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadURL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                hud.dismiss();
                                JSONObject obj = new JSONObject(new String(response.data));

                                if (obj.getInt("status") == 1) {


                                    Log.e("response", obj.toString());
                                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                    if (Aadhar1 == 1) {
                                        addimg_uid.setText(obj.getString("imageurl"));
                                        btn_addimg1.setText("Done/Edit");
                                        btn_addimg1.setTextColor(getContext().getResources().getColor(R.color.green));
                                    }
                                    if (Aadhar2 == 1) {
                                        addimg_uid2.setText(obj.getString("imageurl"));
                                        btn_addimg2.setText("Done/Edit");
                                        btn_addimg2.setTextColor(getContext().getResources().getColor(R.color.green));
                                    }
                                    if (Landpics == 1) {
                                        addimg_uid3.setText(obj.getString("imageurl"));

                                        btn_addimg3.setText("Done/Edit");
                                        btn_addimg3.setTextColor(getContext().getResources().getColor(R.color.green));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                * If you want to add more parameters with the image
                * you can do it here
                * here we have only one parameter with the image
                * which is tags
                * */
                /*
                * Here we are passing image by renaming it with a unique name
                * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                    return params;
                }
            };

            //adding the request to volley
            Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    //particular login for district


}