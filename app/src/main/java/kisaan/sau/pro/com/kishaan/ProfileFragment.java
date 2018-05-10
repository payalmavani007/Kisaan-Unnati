package kisaan.sau.pro.com.kishaan;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kisaan.sau.pro.com.kishaan.Adapter.DistDataAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String URL_DIST_DATA = "";
    List<Data> dataList;
    ImageView datepiker, logout;
    TextView txtdate, totalEntries;
    private int mYear, mMonth, mDay;
    RecyclerView recyclerView;
    Button customstartdate, custoendsdate, btn_custom_ok;
    ListView datelv;
    Bundle bundle;

    public ProfileFragment() {
    }

    String date1 = "";
    String date2 = "";

    private Drawable mDivider;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final SharedPreferences preferences = getContext().getSharedPreferences("status", Context.MODE_PRIVATE);
        bundle = getArguments();
        datepiker = view.findViewById(R.id.btn_date);
        txtdate = view.findViewById(R.id.in_date);


        SharedPreferences sp = getContext().getSharedPreferences("monthly",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();



        totalEntries = view.findViewById(R.id.totalEntries);
        /*recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()

                ));*/

/*
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        datelv.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        datelv.setDividerHeight(1);
*/


        recyclerView = view.findViewById(R.id.recylr_dist);
        recyclerView.addItemDecoration(new kisaan.sau.pro.com.kishaan.DividerItemDecoration(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        txtdate.setText(df);
        //dialog
        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @SuppressLint("ResourceType") final Dialog dialogroot = new Dialog(getContext());
                dialogroot.setContentView(R.layout.datelist);
                datelv = dialogroot.findViewById(R.id.datelv);
                dialogroot.setCancelable(true);
                // dialogroot.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                final String names[] = {"Monthly", "Custom"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);
                datelv.setAdapter(adapter);
                dialogroot.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                dialogroot.show();
                datelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                       /* Animation animFadein = AnimationUtils.loadAnimation(getContext(),
                                R.anim.fadein);
                        animFadein.setAnimationListener((Animation.AnimationListener) getContext());
                        datelv.setAnimation(animFadein);
                        datelv.startAnimation(animFadein);
*/

/*
                        Animation animation1 = Al phaAnimation(R.anim.fadein);
                       // animation1.setDuration(4000);
                        view.startAnimation(animation1);
*/

                        if (i == 0) {

                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String date = df.format(c);
                            final KProgressHUD hud = KProgressHUD.create(getContext())
                                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                    .setCancellable(false)
                                    .setAnimationSpeed(2)
                                    .setDimAmount(0.5f)
                                    .show();
/*

                            Fragment fragment = new TalukaProfileFragment();
                            Bundle bundle2 = new Bundle();
                            bundle.putInt("monthdate", i);
                            fragment.setArguments(bundle);
*/



                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "  http://kisanunnati.com/kisan/user_district?user_id=" + preferences.getString("id", "") + "&dist_id=" + preferences.getString("districts_id", "")
                                    + "&date1=" + date + "&date2&month_flag=1", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject object = new JSONObject(response);
                                        JSONArray array = object.getJSONArray("data");


                                        editor.putString("month","yes").apply();

                                        if (bundle!=null)
                                            totalEntries.setText(bundle.getString("districts_name") + ": " + object.getString("Total Form Count"));
                                        else
                                            totalEntries.setText(object.getString("District Name")+": " + object.getString("Total Form Count"));
                                        hud.dismiss();
                                        if (array.length() > 0) {
                                            recyclerView.removeAllViews();
                                            DistDataAdapter distDataAdapter = new DistDataAdapter(getActivity(), array, getFragmentManager());
                                            recyclerView.setAdapter(distDataAdapter);
                                        } else {
                                            Toast.makeText(getContext(), "No Values..!", Toast.LENGTH_SHORT).show();
                                        }
                                        dialogroot.dismiss();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                            Volley.newRequestQueue(getContext()).add(stringRequest);
                        }
                        if (i == 1) {

                            @SuppressLint("ResourceType") final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.customdate);
                            customstartdate = dialog.findViewById(R.id.btn_custom_start_date);
                            btn_custom_ok = dialog.findViewById(R.id.btn_custom_ok);

                            dialog.show();
                            customstartdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Calendar c = Calendar.getInstance();
                                    mYear = c.get(Calendar.YEAR);
                                    mMonth = c.get(Calendar.MONTH);
                                    mDay = c.get(Calendar.DAY_OF_MONTH);

                                    String dd=txtdate.getText().toString();

                                    final String day=dd.substring(0,2);
                                    final String month=dd.substring(3,5);
                                    final String year=dd.substring(6,10);

                                    @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.color.holo_green_light,
                                            new DatePickerDialog.OnDateSetListener() {

                                                @Override
                                                public void onDateSet(DatePicker view, int year,
                                                                      int monthOfYear, int dayOfMonth) {

                                                    Date dx=new Date(year-1900,monthOfYear,dayOfMonth);
                                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                                    String cdate = formatter.format(dx);
                                                    customstartdate.setText(cdate);
                                                    /*if (customstartdate.equals(""))
                                                    {
                                                        Toast.makeText(getContext(), "Select date ", Toast.LENGTH_SHORT).show();
                                                    }*/
//                                                    customstartdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                    date1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                                }
                                            }, Integer.parseInt(year),Integer.parseInt(month)-1, Integer.parseInt(day));
                                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                                    datePickerDialog.show();

                                }
                            });
                            btn_custom_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                     {
                                        String url = "";
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                        try {

                                            if (date1.equals("") && (date2.equals("")) )
                                            {
                                                Toast.makeText(getContext(), "Select both date.", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (date2.equals(""))
                                                Toast.makeText(getContext(), "select end date", Toast.LENGTH_SHORT).show();


                                            else if (date1.equals(""))
                                                Toast.makeText(getContext(), "select start date", Toast.LENGTH_SHORT).show();
                                            else {
                                                Date d1 = sdf.parse(date1);
                                                Date d2 = sdf.parse(date2);


                                                if (d1.before(d2)) {
                                                    url = "  http://kisanunnati.com/kisan/user_district?user_id=" + preferences.getString("id", "") + "&dist_id=" + preferences.getString("districts_id", "")
                                                            + "&date1=" + date2 + "&date2=" + date1 + "&month_flag=1";

                                                }
                                                if (d2.before(d1)) {
                                                    url = "  http://kisanunnati.com/kisan/user_district?user_id=" + preferences.getString("id", "") + "&dist_id=" + preferences.getString("districts_id", "")
                                                            + "&date1=" + date1 + "&date2=" + date2 + "&month_flag=1";
                                                } else if (!url.equals("")) {

                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {

                                                            try {
                                                                JSONObject object = new JSONObject(response);
                                                                JSONArray array = object.getJSONArray("data");
                                                                if (bundle != null)
                                                                    totalEntries.setText(bundle.getString("districts_name") + ": " + object.getString("Total Form Count"));
                                                                else
                                                                    totalEntries.setText(object.getString("District Name") + ": " + object.getString("Total Form Count"));
/*
                                                            hud.dismiss();
*/
                                                                if (array.length() > 0) {
                                                                    recyclerView.removeAllViews();
                                                                    DistDataAdapter distDataAdapter = new DistDataAdapter(getActivity(), array, getFragmentManager());
                                                                    recyclerView.setAdapter(distDataAdapter);
                                                                } else {
                                                                    Toast.makeText(getContext(), "No Values..!", Toast.LENGTH_SHORT).show();
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

                                                    Volley.newRequestQueue(getContext()).add(stringRequest);


                                                    dialog.dismiss();
                                                    dialogroot.dismiss();
                                                } else {
                                                    Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                                    btn_custom_ok.performClick();
                                                }
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    }

                            });
                            custoendsdate = dialog.findViewById(R.id.btn_custom_end_date);

                            custoendsdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Calendar c = Calendar.getInstance();
                                    mYear = c.get(Calendar.YEAR);
                                    mMonth = c.get(Calendar.MONTH);
                                    mDay = c.get(Calendar.DAY_OF_MONTH);

                                    String dd=txtdate.getText().toString();
                                    final String day=dd.substring(0,2);
                                    final String month=dd.substring(3,5);
                                    final String year=dd.substring(6,10);

                                    @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                            new DatePickerDialog.OnDateSetListener() {

                                                @Override
                                                public void onDateSet(DatePicker view, int year,
                                                                      int monthOfYear, int dayOfMonth) {

                                                    Date dx=new Date(year-1900,monthOfYear,dayOfMonth);
                                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                                    String cdate = formatter.format(dx);
                                                    custoendsdate.setText(cdate);
//                                                    custoendsdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                    date2 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                                }
                                            }, Integer.parseInt(year),Integer.parseInt(month)-1, Integer.parseInt(day));
                                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                                    datePickerDialog.show();
                                }
                            });
                            dialog.setCancelable(true);

                        }
                    }
                });

            }

        });

        logout = view.findViewById(R.id.logout);

        datepiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                String dd=txtdate.getText().toString();
                final String day=dd.substring(0,2);
                final String month=dd.substring(3,5);
                final String year=dd.substring(6,10);

                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                final KProgressHUD hud = KProgressHUD.create(getContext())
                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                        .setCancellable(false)
                                        .setAnimationSpeed(2)
                                        .setDimAmount(0.5f)
                                        .show();
                                Date dx=new Date(year-1900,monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(dx);
                                txtdate.setText(cdate);
//                                txtdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, "  http://kisanunnati.com/kisan/user_district?user_id=" + preferences.getString("id", "") + "&dist_id=" + preferences.getString("districts_id", "")
                                        + "&date1=" + date + "&date2="+date+"&month_flag=0", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject object = new JSONObject(response);
                                            JSONArray array = object.getJSONArray("data");
                                            if (bundle!=null)
                                                totalEntries.setText(bundle.getString("districts_name") + ": " + object.getString("Total Form Count"));
                                            else
                                                totalEntries.setText(object.getString("District Name")+": " + object.getString("Total Form Count"));
                                            hud.dismiss();
                                            if (array.length() > 0) {
                                                DistDataAdapter distDataAdapter = new DistDataAdapter(getActivity(), array, getFragmentManager());
                                                recyclerView.setAdapter(distDataAdapter);
                                            } else {
                                                Toast.makeText(getContext(), "No Values..!", Toast.LENGTH_SHORT).show();
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

                                Volley.newRequestQueue(getContext()).add(stringRequest);

                            }
                        },  Integer.parseInt(year),Integer.parseInt(month)-1, Integer.parseInt(day));
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });

        final KProgressHUD hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = df1.format(c);



        final String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        String url = "";
        if (sp.getString("month","").equals("yes")) {
            url = "  http://kisanunnati.com/kisan/user_district?user_id=" +
                    preferences.getString("id", "") + "&dist_id=" + preferences.getString("districts_id", "") + "&date1=" +
                    date + "&date2=&month_flag=1";
        }
        else
            url = "  http://kisanunnati.com/kisan/user_district?user_id=" +
                    preferences.getString("id", "") + "&dist_id=" + preferences.getString("districts_id", "") + "&date1=" +
                    formattedDate + "&date2="+formattedDate+"&month_flag=0";
        if (!url.equals("")) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        Log.e("dist page", "  http://kisanunnati.com/kisan/user_district?user_id=" +
                                preferences.getString("id", "") + "&dist_id=" + preferences.getString("districts_id", "") + "&date1=" +
                                formattedDate + "&date2&month_flag=0");
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("data");
                        //totalEntries.setText("Total Entries: "+object.getString("Total Form Count"));
                        if (bundle != null)
                            totalEntries.setText(bundle.getString("districts_name") + ": " + object.getString("Total Form Count"));
                        else
                            totalEntries.setText(object.getString("District Name") + ": " + object.getString("Total Form Count"));
                        hud.dismiss();
                        if (array.length() > 0) {
                            DistDataAdapter distDataAdapter = new DistDataAdapter(getActivity(), array, getFragmentManager());
                            recyclerView.setAdapter(distDataAdapter);
                        } else {
                            Toast.makeText(getContext(), "No Values..!", Toast.LENGTH_SHORT).show();
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

            Volley.newRequestQueue(getContext()).add(stringRequest);
        }
        return view;

    }


}
