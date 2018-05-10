package kisaan.sau.pro.com.kishaan.Adapter;

import android.content.Context;
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
import kisaan.sau.pro.com.kishaan.R;

public class VillageDataAdapter extends RecyclerView.Adapter<VillageDataAdapter.ViewHolder> {
    private Context mCtx;
    JSONArray array;
    FragmentManager fragmentManager;

    public VillageDataAdapter(Context mCtx, JSONArray array, FragmentManager fragmentManager) {
        this.mCtx = mCtx;
        this.array = array;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public VillageDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.district_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VillageDataAdapter.ViewHolder holder, int position) {
        try {
            final JSONObject obj = array.getJSONObject(position);
            Typeface tf = Typeface.createFromAsset(mCtx.getAssets(), "fonts/segoeui.ttf");
            holder.textname.setTypeface(tf);
            holder.textcontact.setTypeface(tf);
            holder.textname.setText(obj.getString("name"));

            holder.textcontact.setText(obj.getString("count"));

            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    try {

                        EmployeeProfileFragment fragment = new EmployeeProfileFragment(obj.getInt("id"));
                        final Bundle bundle = new Bundle();
                        bundle.putString("taluka_name", holder.textname.getText().toString());
                        fragment.setArguments(bundle);


                        fragmentTransaction.addToBackStack("taluka admin");
                        // fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left);


                        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();

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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textname, textcontact;
        LinearLayout item_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            textname = itemView.findViewById(R.id.dist_name);
            textcontact = itemView.findViewById(R.id.dist_contact);
            item_layout = itemView.findViewById(R.id.item_layout);
        }
    }
}
