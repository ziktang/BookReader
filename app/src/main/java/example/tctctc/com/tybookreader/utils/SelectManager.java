package example.tctctc.com.tybookreader.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import example.tctctc.com.tybookreader.view.SelectView;

/**
 * Created by tctctc on 2017/4/1.
 * Function:
 */

public class SelectManager<K extends SelectView, V> {

    private Map<K, V> mViewMap = new HashMap<>();
    private SelectView mOldSelectView;

    public SelectManager(Map<K, V> viewMap) {
        mViewMap = viewMap;
    }

    public V select(SelectView selectView) {
        if (!mViewMap.containsKey(selectView) || mOldSelectView == selectView) return null;

        selectView.select();
        if (mOldSelectView != null) {
            mOldSelectView.unSelect();
        }
        mOldSelectView = selectView;
        return mViewMap.get(selectView);
    }

    public V select(V value) {
        if (!mViewMap.containsValue(value)) {
            return null;
        }

        for (Map.Entry<K, V> entry : mViewMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return select(entry.getKey());
            }
        }
        return null;
    }
}
