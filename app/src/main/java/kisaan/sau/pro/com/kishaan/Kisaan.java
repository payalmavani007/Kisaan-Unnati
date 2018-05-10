package kisaan.sau.pro.com.kishaan;

import android.app.Application;

public class Kisaan extends Application {

/*
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    public void onCreate() {
        super.onCreate();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/bauhaus.ttf");
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("res/segoeui.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
    }
}