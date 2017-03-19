package example.tctctc.com.tybookreader.bookshelf.view;

import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bookshelf.adapter.FileScanAdapter;
import example.tctctc.com.tybookreader.utils.FileUtils;

public class ImportBookActivity extends BaseActivity implements FileScanAdapter.OnClickCallBack {

    @BindView(R.id.scan_recycle)
    RecyclerView mScanRecycle;

    @BindView(R.id.currentPath)
    TextView mCurrentPath;

    @BindView(R.id.book_all_select)
    TextView mAllSelect;

    @BindView(R.id.book_add)
    TextView mBookAdd;

    @BindView(R.id.import_toorbar)
    Toolbar mToolbar;

    private List<File> mFiles;
    private FileScanAdapter mAdapter;
    private File mFile;

    @Override
    protected void initView(View contextView) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("本机书籍");

        mBookAdd.setOnClickListener(this);
        mAllSelect.setOnClickListener(this);

        mFiles = new ArrayList<>();
        mAdapter = new FileScanAdapter(mFiles, this, this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mScanRecycle.setLayoutManager(manager);
        mScanRecycle.setAdapter(mAdapter);
        changeFolder(Environment.getExternalStorageDirectory());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_book;
    }

    @Override
    protected void initParams() {

    }

    @Override
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.book_all_select:
                selectAll(mAdapter.getFileMap());
                break;
            case android.R.id.home:
                finish();
                break;
        }
    }

    private void selectAll(HashMap<File, Integer> fileMap) {
        Iterator<Map.Entry<File, Integer>> iterator = fileMap.entrySet().iterator();
        if (!mAdapter.isAllSelect()) {
            while (iterator.hasNext()) {
                Map.Entry<File, Integer> entry = iterator.next();
                entry.setValue(1);
            }
            mAllSelect.setText(getResources().getString(R.string.all_un_select));
            mAdapter.setAllSelect(true);
        } else {
            while (iterator.hasNext()) {
                Map.Entry<File, Integer> entry = iterator.next();
                entry.setValue(2);
            }
            mAllSelect.setText(getResources().getString(R.string.all_select));
            mAdapter.setAllSelect(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    private List<File> scanFile(File scanfile) {
        File[] files = scanfile.listFiles();
        if (files == null) return null;
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory() && !file.isHidden()) {
                fileList.addAll(scanFile(file));
            } else if (file.isFile() && !file.isHidden() && file.canRead()) {
                if (file.getName().endsWith(".txt")) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    public List<File> listFile(File file) {
        List<File> files = FileUtils.listFiles(file);
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isFile() && rhs.isFile()) {
                    return lhs.length() > rhs.length() ? -1 : 1;
                } else if (!lhs.isFile() && rhs.isFile()) {
                    return 1;
                } else if (lhs.isFile() && !rhs.isFile()) {
                    return -1;
                } else {
                    return lhs.getName().compareTo(rhs.getName());
                }
            }
        });
        Log.d(TAG, "fileSize:" + files.size());
        return files;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void changeStatus(boolean isAllSelect) {
        if (isAllSelect)
            mAllSelect.setText(getResources().getString(R.string.all_un_select));
        else
            mAllSelect.setText(getResources().getString(R.string.all_select));
    }

    @Override
    public void clickFolder(File file) {
        changeFolder(file);
    }

    private void changeFolder(File file) {
        mFile = file;
        mFiles.clear();
        mFiles.addAll(listFile(file));
        mCurrentPath.setText(file.getAbsolutePath());
        mAdapter.dataInit();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        if (!mFile.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            changeFolder(mFile.getParentFile());
        } else {
            super.onBackPressed();
        }
    }
}
