package example.tctctc.com.tybookreader;

import android.app.Application;
import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by tctctc on 2017/3/15.
 */

public class BookApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
        sContext = getApplicationContext();
    }

    public static Context getContext(){
        return sContext;
    }

}
