package example.tctctc.com.tybookreader;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by tctctc on 2017/3/15.
 */

public class BookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
    }
}
