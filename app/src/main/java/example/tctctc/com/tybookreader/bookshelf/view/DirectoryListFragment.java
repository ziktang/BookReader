package example.tctctc.com.tybookreader.bookshelf.view;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseFragment;
import example.tctctc.com.tybookreader.bean.Directory;
import example.tctctc.com.tybookreader.bookshelf.adapter.DirectoryAdapter;
import example.tctctc.com.tybookreader.bookshelf.common.ContentManager;
import example.tctctc.com.tybookreader.bookshelf.common.PageManager;

/**
 * Created by tctctc on 2017/4/7.
 * Function:
 */

public class DirectoryListFragment extends BaseFragment implements DirectoryAdapter.OnDirectoryListener {
    
    private List<Directory> mDirectoryList;

    private ContentManager mManager;

    @BindView(R.id.directory_recycle)
    RecyclerView mRecyclerView;


    @Override
    protected void initView() {
        mManager = ContentManager.getInstance();
        mDirectoryList = mManager.getDirectory();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        DirectoryAdapter adapter = new DirectoryAdapter(getContext(),mDirectoryList,this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.directory_list_fragment;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCLick(Directory directory) {
        mRxManager.post("chapter",directory);
        mRxManager.post("close drawer","");
    }
}
