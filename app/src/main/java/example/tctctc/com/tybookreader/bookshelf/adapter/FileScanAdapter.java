package example.tctctc.com.tybookreader.bookshelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.app.Constant;
import example.tctctc.com.tybookreader.bean.ScanFile;
import example.tctctc.com.tybookreader.utils.FileUtils;

/**
 * Created by tctctc on 2017/3/19.
 */

public class FileScanAdapter extends RecyclerView.Adapter<FileScanAdapter.FileViewHolder> {

    private List<ScanFile> mScanFiles;
    private LayoutInflater mInflater;
    public Context mContext;

    /**
     * 当有长按或点击发生时给父view的回调
     */
    private FileScanAdapter.OnClickCallBack mBack;

    public interface OnClickCallBack {

        void clickFolder(ScanFile scanFile);

        void selectFile(ScanFile scanFile);

        void unSelectFile(ScanFile scanFile);
    }

    public FileScanAdapter(List<ScanFile> mScanFiles, Context context, FileScanAdapter.OnClickCallBack mBack) {
        this.mScanFiles = mScanFiles;
        this.mContext = context;
        this.mBack = mBack;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.file_item, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, final int position) {
        final ScanFile scanFile = mScanFiles.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanFile.getFile().isFile()) {
                    select(scanFile, position);
                } else {
                    mBack.clickFolder(scanFile);
                }
            }
        });
        holder.setData(scanFile);
    }

    private void select(ScanFile scanFile, Integer position) {
        if(scanFile.isImported()){
            return;
        }
        if (scanFile.isChecked()) {
            scanFile.setChecked(false);
            mBack.unSelectFile(scanFile);
        } else {
            scanFile.setChecked(true);
            mBack.selectFile(scanFile);
        }
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mScanFiles == null ? 0 : mScanFiles.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.file_img)
        ImageView fileImg;

        @BindView(R.id.fileName)
        TextView fileName;
        @BindView(R.id.fileInfo)
        TextView fileInfo;
        @BindView(R.id.normal)
        ImageView normal;
        @BindView(R.id.checked)
        ImageView checked;
        @BindView(R.id.isImport)
        TextView isImported;

        public FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ScanFile scanFile) {
            File file = scanFile.getFile();
            fileName.setText(file.getName().trim());
            if (file.isFile()) {
                fileImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.general__shared__txt_icon));
                fileInfo.setText(FileUtils.getFileSize(file));
                if (scanFile.isImported()) {
                    showImported();
                    return;
                }
                if (scanFile.isChecked()) {
                    showChecked();
                } else {
                    showNomal();
                }
            } else {
                fileImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.general__shared__folder_icon));
                fileInfo.setText(FileUtils.listFilterOneFolder(file, mContext.getString(R.string.file_type_txt), Constant.BookShelf.SCAN_BOOK_SIZE).size() + "项");
                showFolder();
            }
        }

        private void showNomal() {
            show(View.INVISIBLE,View.VISIBLE,View.INVISIBLE);
        }

        private void showChecked() {
            show(View.VISIBLE,View.INVISIBLE,View.INVISIBLE);
        }

        private void showImported() {
            show(View.INVISIBLE,View.INVISIBLE,View.VISIBLE);
        }

        private void showFolder() {
            show(View.INVISIBLE,View.INVISIBLE,View.INVISIBLE);
        }

        private void show(int checkedVisible, int normalVisible, int isImportedVisible) {
            checked.setVisibility(checkedVisible);
            normal.setVisibility(normalVisible);
            isImported.setVisibility(isImportedVisible);
        }
    }
}
