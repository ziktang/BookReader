package example.tctctc.com.tybookreader.bookshelf.view;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseActivity;
import example.tctctc.com.tybookreader.bean.ScanFile;
import example.tctctc.com.tybookreader.bookshelf.adapter.FileScanAdapter;
import example.tctctc.com.tybookreader.bookshelf.contact.ImportContact;
import example.tctctc.com.tybookreader.bookshelf.model.BookDao;
import example.tctctc.com.tybookreader.bookshelf.presenter.ImportPresenter;
import example.tctctc.com.tybookreader.main.MainActivity;

public class ImportBookActivity extends BaseActivity implements FileScanAdapter.OnClickCallBack, ImportContact.View {

    @BindView(R.id.scan_recycle)
    RecyclerView mScanRecycle;

    @BindView(R.id.currentPath)
    TextView mCurrentPath;

    @BindView(R.id.book_all_select)
    TextView mAllSelect;

    @BindView(R.id.book_add)
    TextView mBookAdd;

    @BindView(R.id.import_toolbar)
    Toolbar mToolbar;

    private List<ScanFile> mScanFiles;
    private FileScanAdapter mAdapter;
    private File mFile;
    private List<ScanFile> mSelectFiles;
    private ImportPresenter mPresenter;
    private int importableNum;

    @Override
    protected void initView(View contextView) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mScanFiles = new ArrayList<>();
        mAdapter = new FileScanAdapter(mScanFiles, this, this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mScanRecycle.setLayoutManager(manager);
        mScanRecycle.setAdapter(mAdapter);

        mSelectFiles = new ArrayList<>();
        mPresenter = new ImportPresenter();
        mPresenter.setVM(new BookDao(), this);
        mFile = Environment.getExternalStorageDirectory();
        mPresenter.onGetFileList(mFile);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_book;
    }

    @Override
    protected void initParams() {

    }

    @OnClick({R.id.book_all_select, R.id.book_add})
    @Override
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.book_all_select:
                selectAll();
                break;
            case R.id.book_add:
                addBooks();
                break;
        }
    }

    private void addBooks() {
        mPresenter.onAddBooks(mSelectFiles);
    }

    /**
     * 全选或全不选
     */
    private void selectAll() {
        if (mSelectFiles.size() >= importableNum){
            for (ScanFile scanFile : mScanFiles) {
                scanFile.setChecked(false);
            }
            mSelectFiles.clear();
        }else{
            mSelectFiles.clear();
            for (ScanFile scanFile : mScanFiles) {

                if (scanFile.getFile()!=null && scanFile.getFile().isFile()){
                    scanFile.setChecked(true);
                    mSelectFiles.add(scanFile);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        updateSelectView();
    }

    private void updateSelectView(){
        String text = String.format(getString(R.string.add_to_bookshelf),mSelectFiles.size()+"");
        mBookAdd.setText(text);
        if (mSelectFiles.size() >= importableNum){
            mAllSelect.setText(getResources().getString(R.string.all_un_select));
        }else{
            mAllSelect.setText(getResources().getString(R.string.all_select));
        }

        if (mSelectFiles.size() == 0){
            mBookAdd.setClickable(false);
        }else {
            mBookAdd.setClickable(true);
        }
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
                return true;
            case R.id.scan:
                Intent intent = new Intent(ImportBookActivity.this, ScanBookActivity.class);
                intent.putExtra("scanFilePath", mFile.getAbsolutePath());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void clickFolder(ScanFile scanFile) {
        mFile = scanFile.getFile();
        mPresenter.onGetFileList(scanFile.getFile());
    }

    @Override
    public void selectFile(ScanFile scanFile) {
        mSelectFiles.add(scanFile);
        updateSelectView();
    }

    @Override
    public void unSelectFile(ScanFile scanFile) {
        mSelectFiles.remove(scanFile);
        updateSelectView();
    }

    @Override
    public void onBackPressed() {
        if (!mFile.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            mPresenter.onGetFileList(mFile.getParentFile());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showScanBookList(List<ScanFile> scanFiles, int importableNum) {
        this.importableNum = importableNum;
        mSelectFiles.clear();
        mScanFiles.clear();
        mScanFiles.addAll(scanFiles);
        mCurrentPath.setText(mFile.getAbsolutePath());
        mAdapter.notifyDataSetChanged();
        updateSelectView();
    }

    @Override
    public void toShelf() {
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private boolean icContainScanFile(List<ScanFile> mScanFiles,ScanFile scanFile){
        if (mScanFiles == null || mScanFiles.size() == 0 || scanFile == null){
            return false;
        }

        for (ScanFile s:mScanFiles){
            if (s.getFile() != null && s.getFile().exists() && scanFile.getFile()!=null && s.getFile().getAbsolutePath().equals(scanFile.getFile().getAbsolutePath())){
                return true;
            }
        }
        return false;
    }
}
