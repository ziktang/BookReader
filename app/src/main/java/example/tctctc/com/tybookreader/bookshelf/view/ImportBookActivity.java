package example.tctctc.com.tybookreader.bookshelf.view;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.ScanBook;
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

    private List<ScanBook> mScanBooks;
    private FileScanAdapter mAdapter;
    private File mFile;
    private List<ScanBook> mSelectFiles;
    private ImportPresenter mPresenter;
    private int importableNum;
    private boolean isSelectAll = false;

    @Override
    protected void initView(View contextView) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mScanBooks = new ArrayList<>();
        mAdapter = new FileScanAdapter(mScanBooks, this, this);
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

    private void selectAll() {
        HashMap<ScanBook, Integer> fileMap = mAdapter.getFileMap();
        Iterator<Map.Entry<ScanBook, Integer>> iterator = fileMap.entrySet().iterator();
        if (!isSelectAll) {
            while (iterator.hasNext()) {
                Map.Entry<ScanBook, Integer> entry = iterator.next();
                if (entry.getValue() == 2) {
                    entry.setValue(1);
                    selectFile(entry.getKey());
                }
            }
            mAllSelect.setText(getResources().getString(R.string.all_un_select));
        } else {
            while (iterator.hasNext()) {
                Map.Entry<ScanBook, Integer> entry = iterator.next();
                if (entry.getValue() == 1) {
                    entry.setValue(2);
                    unSelectFile(entry.getKey());
                }
            }
            mAllSelect.setText(getResources().getString(R.string.all_select));
        }
        mAdapter.notifyDataSetChanged();
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
    public void clickFolder(ScanBook scanBook) {
        mFile = scanBook.getFile();
        mPresenter.onGetFileList(scanBook.getFile());
    }

    @Override
    public void selectFile(ScanBook scanBook) {
        mSelectFiles.add(scanBook);
        mBookAdd.setText("加入书架(" + mSelectFiles.size() + ")");
        if (mSelectFiles.size() == importableNum) {
            isSelectAll = true;
            mAllSelect.setText(getResources().getString(R.string.all_un_select));
        }
    }

    @Override
    public void unSelectFile(ScanBook scanBook) {
        mSelectFiles.remove(scanBook);
        mBookAdd.setText("加入书架(" + mSelectFiles.size() + ")");
        if (mSelectFiles.size() < importableNum) {
            isSelectAll = false;
            mAllSelect.setText(getResources().getString(R.string.all_select));
        }
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
    public void showScanBookList(List<ScanBook> scanBooks, int fileNum, int importableNum) {
        this.importableNum = importableNum;
        mScanBooks.clear();
        mScanBooks.addAll(scanBooks);
        mAdapter.dataInit();
        mCurrentPath.setText(mFile.getAbsolutePath());
        mAdapter.notifyDataSetChanged();
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
}
