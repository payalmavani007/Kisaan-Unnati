package kisaan.sau.pro.com.kishaan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kisaan.sau.pro.com.kishaan.Adapter.VillageDataAdapter;

public class VillageProfileFragment extends Fragment {
    RecyclerView village_recycler;
    ImageView village_cal;
    TextView village_set_date, village_total_entry;
    VillageDataAdapter villageDataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_village_profile, container, false);
       village_recycler = view.findViewById(R.id.recylr_village);
       village_cal = view.findViewById(R.id.village_btn_date);
       village_set_date = view.findViewById(R.id.village_in_date);
       village_total_entry = view.findViewById(R.id.village_totalEntries);
       village_recycler.addItemDecoration(new DividerItemDecoration(getContext()));
       village_recycler.setHasFixedSize(true);
        village_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        village_recycler.setAdapter(villageDataAdapter);


       return  view;
    }

}
