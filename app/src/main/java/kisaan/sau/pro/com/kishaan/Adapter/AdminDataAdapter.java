package kisaan.sau.pro.com.kishaan.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kisaan.sau.pro.com.kishaan.EmployeeProfileFragment;
import kisaan.sau.pro.com.kishaan.ProfileFragment;
import kisaan.sau.pro.com.kishaan.R;
import kisaan.sau.pro.com.kishaan.TalukaProfileFragment;

/**
 * Created by Payal on 3/19/2018.
 */

public class AdminDataAdapter extends RecyclerView.Adapter<AdminDataAdapter.DistDataViewHolder> {
    private Context mCtx;
    //    private List<Data> dataList;
    JSONArray array;
FragmentManager fragmentManager;

    public AdminDataAdapter(Context mCtx, JSONArray array,FragmentManager fragmentManager) {
        this.mCtx = mCtx;
        this.array = array;
        this.fragmentManager=fragmentManager;
    }


    @Override
    public DistDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.district_row, parent, false);
        return new DistDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DistDataViewHolder holder, int position) {
//        Data data = dataList.get(position);

        try {
            final JSONObject obj = array.getJSONObject(position);
            Typeface tf = Typeface.createFromAsset(mCtx.getAssets(), "fonts/segoeui.ttf");
            holder.textname.setTypeface(tf);
            holder.textcontact.setTypeface(tf);




                holder.textname.setText(obj.getString("districts_name"));


            holder.textcontact.setText(obj.getString("count"));

            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override


                public void onClick(View view) {

                    SharedPreferences preferences=mCtx.getSharedPreferences("status",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    try {


                        ProfileFragment fragment = new ProfileFragment();
                        final Bundle bundle = new Bundle();
                        bundle.putString("districts_name", holder.textname.getText().toString());
                        fragment.setArguments(bundle);



                        editor.putString("districts_id",obj.getString("id")).apply();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack("Admin Dashboard");


                        /* Animation animFadein = AnimationUtils.loadAnimation(getContext(),
                                R.anim.fadein);
                        animFadein.setAnimationListener((Animation.AnimationListener) getContext());
                        datelv.setAnimation(animFadein);
                        datelv.startAnimation(animFadein);
*/
//                        fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left);

                        fragmentTransaction.replace(R.id.fragment_container,fragment).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    class DistDataViewHolder extends RecyclerView.ViewHolder {

        TextView textname, textcontact;
        LinearLayout item_layout;

        public DistDataViewHolder(View itemView) {
            super(itemView);

            textname = itemView.findViewById(R.id.dist_name);
            textcontact = itemView.findViewById(R.id.dist_contact);
            item_layout = itemView.findViewById(R.id.item_layout);
        }
    }
}
