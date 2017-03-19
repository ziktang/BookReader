package example.tctctc.com.tybookreader.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by tctctc on 2017/3/18.
 */

public class MainAdapter extends FragmentPagerAdapter {

    private String[] tabs;
    private List<Fragment> lists;

    public MainAdapter(FragmentManager fm, List<Fragment> lists, String[] tabs) {
        super(fm);
        this.lists = lists;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return lists.get(position);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
