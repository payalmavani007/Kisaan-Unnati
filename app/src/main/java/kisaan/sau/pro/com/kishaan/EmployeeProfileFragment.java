package kisaan.sau.pro.com.kishaan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Locale;

/**
 * Created by Payal on 3/26/2018.
 */

@SuppressLint("ValidFragment")
public class EmployeeProfileFragment extends Fragment {

    View view;
    TextView employee_name, employee_empcode, employee_total;
    int employee_id;

    ImageView datepiker, logout;
    TextView txtdate, totalEntries;
    private int mYear, mMonth, mDay;
    RecyclerView recyclerView;
    Button customstartdate, custoendsdate, btn_custom_ok;
    ListView datelv;


    String date1 = "";
    String date2 = "";

    @SuppressLint("ValidFragment")
    public EmployeeProfileFragment(int employee_id) {
        this.employee_id = employee_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.employee_layout, container, false);

        datepiker = view.findViewById(R.id.employee_btn_date);
        txtdate = view.findViewById(R.id.employee_in_date);
        totalEntries = view.findViewById(R.id.employee_totalEntries);
        totalEntries.setText("Employee Details");
        employee_name = view.findViewById(R.id.employee_name);
        employee_empcode = view.findViewById(R.id.employee_empcode);
        employee_total = view.findViewById(R.id.employee_total);


        SharedPreferences sp = getContext().getSharedPreferences("monthly",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();




        final Bundle b = getArguments();

        final SharedPreferences preferences = getContext().getSharedPreferences("status", Context.MODE_PRIVATE);

        String df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        txtdate.setText(df);

        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("ResourceType") final Dialog dialogroot = new Dialog(getContext());
                dialogroot.setContentView(R.layout.datelist);
                datelv = dialogroot.findViewById(R.id.datelv);
                dialogroot.setCancelable(true);
                String names[] = {"Monthly", "Custom"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);
                datelv.setAdapter(adapter);

                dialogroot.show();
                datelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                            String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            String url = "  http://kisanunnati.com/kisan/user_fieldemployee?emp_id=" + employee_id +
                                    "&date1=" + formattedDate + "&date2=&month_flag=1";

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("emplopyee response", response);

                                    try {
                                        JSONObject object = new JSONObject(response);
                                        JSONArray ary = object.getJSONArray("data");


                                        editor.putString("month","yes").apply();


//                                        totalEntries.setText(b.getString("taluka_name")+": ");
                                        JSONObject o = ary.getJSONObject(0);
                                        employee_name.setText(o.getString("name"));
                                        employee_total.setText(String.valueOf(o.getInt("count")));
                                        employee_empcode.setText(o.getString("empcode"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    hud.dismiss();

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

                                    String dd = txtdate.getText().toString();
                                    final String day = dd.substring(0, 2);
                                    final String month = dd.substring(3, 5);
                                    final String year = dd.substring(6, 10);

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                            new DatePickerDialog.OnDateSetListener() {

                                                @Override
                                                public void onDateSet(DatePicker view, int year,
                                                                      int monthOfYear, int dayOfMonth) {

                                                    Date dx = new Date(year - 1900, monthOfYear, dayOfMonth);
                                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                                    String cdate = formatter.format(dx);
                                                    customstartdate.setText(cdate);
//                                                    customstartdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                    date1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                                }
                                            }, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                                    datePickerDialog.show();

                                }
                            });
                            btn_custom_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
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

                                        Date d1 = sdf.parse(date1);
                                        Date d2 = sdf.parse(date2);
                                        if(d1.before(d2))
                                        {
                                            url = "  http://kisanunnati.com/kisan/user_fieldemployee?emp_id=" + employee_id +
                                                    "&date1=" + date2 + "&date2=" + date1 + "&month_flag=0";
                                        }
                                        if (d2.before(d1))
                                        {
                                            url = "  http://kisanunnati.com/kisan/user_fieldemployee?emp_id=" + employee_id +
                                                    "&date1=" + date1    + "&date2=" + date2 + "&month_flag=0";

                                        }
                                        if (!url.equals(""))
                                        {
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.e("emplopyee response", response);

                                                    try {
                                                        JSONObject object = new JSONObject(response);
                                                        JSONArray ary = object.getJSONArray("data");
//                                                totalEntries.setText(b.getString("taluka_name")+": ");
                                                        JSONObject o = ary.getJSONObject(0);
                                                        employee_name.setText(o.getString("name"));
                                                        employee_total.setText(String.valueOf(o.getInt("count")));
                                                        employee_empcode.setText(o.getString("empcode"));
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
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                            btn_custom_ok.performClick();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
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

                                    String dd = txtdate.getText().toString();
                                    final String day = dd.substring(0, 2);
                                    final String month = dd.substring(3, 5);
                                    final String year = dd.substring(6, 10);

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                            new DatePickerDialog.OnDateSetListener() {

                                                @Override
                                                public void onDateSet(DatePicker view, int year,
                                                                      int monthOfYear, int dayOfMonth) {

                                                    Date dx = new Date(year - 1900, monthOfYear, dayOfMonth);
                                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                                    String cdate = formatter.format(dx);
                                                    custoendsdate.setText(cdate);
//                                                    custoendsdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                    date2 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                                }
                                            }, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                                    datePickerDialog.show();
                                }
                            });
                            dialog.setCancelable(true);

                        }
                    }
                });

            }
        });

        datepiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                String dd = txtdate.getText().toString();
                final String day = dd.substring(0, 2);
                final String month = dd.substring(3, 5);
                final String year = dd.substring(6, 10);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                Date dx = new Date(year - 1900, monthOfYear, dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(dx);
                                txtdate.setText(cdate);
//                                txtdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                String url = "  http://kisanunnati.com/kisan/user_fieldemployee?emp_id=" + employee_id +
                                        "&date1=" + date + "&date2=" + date + "&month_flag=0";
                                final KProgressHUD hud = KProgressHUD.create(getContext())
                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                        .setCancellable(false)
                                        .setAnimationSpeed(2)
                                        .setDimAmount(0.5f)
                                        .show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e("emplopyee response", response);

                                        try {
                                            JSONObject object = new JSONObject(response);
                                            JSONArray ary = object.getJSONArray("data");
//                                            totalEntries.setText(b.getString("taluka_name")+": ");
                                            JSONObject o = ary.getJSONObject(0);
                                            employee_name.setText(o.getString("name"));
                                            employee_total.setText(String.valueOf(o.getInt("count")));
                                            employee_empcode.setText(o.getString("empcode"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        hud.dismiss();

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                Volley.newRequestQueue(getContext()).add(stringRequest);

                            }
                        }, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                datePickerDialog.show();


            }
        });

//        employee_empcode.setText(preferences.getString("empcode", ""));

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String date = df2.format(c);



        String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String url = "";
        if (sp.getString("month","").equals("yes")) {
            url = "  http://kisanunnati.com/kisan/user_fieldemployee?emp_id=" + employee_id +
                "&date1=" + date + "&date2=&month_flag=1";
        }
        else {
            url ="  http://kisanunnati.com/kisan/user_fieldemployee?emp_id=" + employee_id +
                    "&date1=" + formattedDate + "&date2=" + formattedDate + "&month_flag=0";
        }

        if (!url.equals("")) {

/*

            String url = "  http://kisanunnati.com/kisan/user_fieldemployee?emp_id=" + employee_id +
                "&date1=" + formattedDate + "&date2=" + formattedDate + "&month_flag=0";

*/
            final KProgressHUD hud = KProgressHUD.create(getContext())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("emplopyee response", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray ary = object.getJSONArray("data");

//                    totalEntries.setText(b.getString("taluka_name")+": ");
                        hud.dismiss();
                        JSONObject o = ary.getJSONObject(0);
                        employee_name.setText(o.getString("name"));
                        employee_total.setText(String.valueOf(o.getInt("count")));
                        employee_empcode.setText(o.getString("empcode"));
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
