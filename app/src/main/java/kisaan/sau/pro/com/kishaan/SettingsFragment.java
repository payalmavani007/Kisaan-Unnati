package kisaan.sau.pro.com.kishaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {
    TextView text_name,text_version;
    Button btn_logout;



    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
    text_name = view.findViewById(R.id.set_name);
    text_version = view.findViewById(R.id.version);
    btn_logout = view.findViewById(R.id.set_logout);


        SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        text_name.setText(preferences.getString("username",""));

    btn_logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
    editor.clear().apply();
            Intent i = new Intent(getContext(), Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    });
    return view;
    }

    }


