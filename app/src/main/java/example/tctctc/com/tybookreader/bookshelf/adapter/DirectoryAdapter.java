package example.tctctc.com.tybookreader.bookshelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.bean.Chapter;

/**
 * Created by tctctc on 2017/4/7.
 * Function:
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {


    private List<Chapter> mChapterList = new ArrayList();
    private Context mContext;
    private LayoutInflater mInflater;

    private OnDirectoryListener mDirectoryListener;

    public interface OnDirectoryListener {
        void onCLick(Chapter chapter);
    }


    public DirectoryAdapter(Context context, List<Chapter> directories, OnDirectoryListener listener) {
        mChapterList = directories;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDirectoryListener = listener;
    }

    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.directory_item, null);
        return new DirectoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        holder.setData(mChapterList.get(position));
    }

    @Override
    public int getItemCount() {
        return mChapterList == null ? 0 : mChapterList.size();
    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder {

        private TextView mChapterName;

        public DirectoryViewHolder(View itemView) {
            super(itemView);
            mChapterName = (TextView) itemView.findViewById(R.id.chapterName);
        }

        public void setData(Chapter chapter) {
            mChapterName.setText(chapter.getName());
            Log.i("bbb", chapter.getName()+"  start:"+ chapter.getStartPosition());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDirectoryListener.onCLick(mChapterList.get(getAdapterPosition()));
                }
            });
        }
    }
}
