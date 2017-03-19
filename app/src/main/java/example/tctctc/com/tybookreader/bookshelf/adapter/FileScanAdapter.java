package example.tctctc.com.tybookreader.bookshelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.HashMap;
import java.util.List;

import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.utils.FileUtils;

/**
 * Created by tctctc on 2017/3/19.
 */

public class FileScanAdapter extends RecyclerView.Adapter<FileScanAdapter.FileViewHolder> {

    private List<File> mFiles;
    private LayoutInflater mInflater;
    public Context mContext;

    //当有长按或点击发生时给父view的回调
    private FileScanAdapter.OnClickCallBack mBack;

    //位置和状态 1 选中状态  2未选中状态
    private HashMap<File, Integer> mHashMap;

    //是否处于全选状态
    private boolean isAllSelect;
    //已选择的数量
    private int selectedNum;
    private int fileTotal;

    public interface OnClickCallBack {
        //点击选择，恰好达到全不选或者全选,或者隐藏
        void changeStatus(boolean isAllSelect);

        void clickFolder(File file);
    }

    public FileScanAdapter(List<File> mFiles, Context context, FileScanAdapter.OnClickCallBack mBack) {
        this.mFiles = mFiles;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = mFiles.get(position);
                if (file.isFile()) {
                    select(holder, position);
                }else{
                    mBack.clickFolder(file);
                }
            }
        });
        holder.setData(mFiles.get(position));
    }

    private void select(FileViewHolder holder, Integer position) {
        File file = mFiles.get(position);
        if (mHashMap.get(file) == 1) {
            mHashMap.put(file, 2);
            selectedNum--;
        } else {
            mHashMap.put(file, 1);
            selectedNum++;
        }

        Log.i("aaa","selectedNum"+selectedNum+"--fileTotal:"+fileTotal);
        if (selectedNum == fileTotal) {
            isAllSelect = true;
            mBack.changeStatus(isAllSelect);
        } else if (selectedNum == 0) {
            isAllSelect = false;
            mBack.changeStatus(isAllSelect);
        }
        notifyItemChanged(position);
    }

    public void setAllSelect(boolean allSelect) {
        isAllSelect = allSelect;
        if (allSelect) selectedNum = fileTotal;
        else selectedNum = 0;
    }

    public void dataInit() {
        fileTotal = 0;
        selectedNum = 0;
        mHashMap = new HashMap<>();
        for (int i = 0; i < mFiles.size(); i++) {
            if (mFiles.get(i).isFile()) {
                mHashMap.put(mFiles.get(i), 2);
                fileTotal++;
            }
        }
        isAllSelect = false;
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public boolean isAllSelect() {
        return isAllSelect;
    }

    public HashMap<File, Integer> getFileMap() {
        return mHashMap;
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView fileImg;
        TextView fileName;
        TextView fileInfo;
        ImageView normal;
        ImageView checked;

        public FileViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            fileInfo = (TextView) itemView.findViewById(R.id.fileInfo);
            fileImg = (ImageView) itemView.findViewById(R.id.file_img);
            normal = (ImageView) itemView.findViewById(R.id.normal);
            checked = (ImageView) itemView.findViewById(R.id.checked);
        }

        public void setData(File file) {
            if (file.isFile()) {
                fileImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.general__shared__txt_icon));
                fileInfo.setText(FileUtils.getFileSize(file));
                if (mHashMap.get(file) == 1){
                    normal.setVisibility(View.INVISIBLE);
                    checked.setVisibility(View.VISIBLE);
                }else {
                    normal.setVisibility(View.VISIBLE);
                    checked.setVisibility(View.INVISIBLE);
                }
            } else {
                fileImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.general__shared__folder_icon));
                fileInfo.setText(FileUtils.listFiles(file).size() + "项");
                normal.setVisibility(View.INVISIBLE);
                checked.setVisibility(View.INVISIBLE);
            }
            fileName.setText(file.getName().trim());
        }
    }
}
