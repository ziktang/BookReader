package example.tctctc.com.tybookreader.bookshelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.bean.BookBean;

/**
 * Created by tctctc on 2017/3/18.
 */

public class ShelfBookAdapter extends RecyclerView.Adapter<ShelfBookAdapter.ViewHolder> {

    private List<BookBean> mBooks;
    private LayoutInflater mInflater;
    public Context mContext;

    //当有长按或点击发生时给父view的回调
    private OnClickCallBack mBack;

    public interface OnClickCallBack {
        //长按回调
        void OnLongClick(int position,View view);
        void OnBookClick(int position,View view);
    }

    public ShelfBookAdapter(List<BookBean> mBooks, Context context, OnClickCallBack mBack) {
        this.mBooks = mBooks;
        this.mContext = context;
        this.mBack = mBack;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shelf_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //设置每本书的长按事件
                mBack.OnLongClick(position,v);
                return false;
            }
        });

        //设置每本书的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当不处于长按状态，则直接打开书籍
                mBack.OnBookClick(position,v);
                return;
            }
        });
        holder.setData(mBooks.get(position));
    }

    @Override
    public int getItemCount() {
        return mBooks==null?0:mBooks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mBookName;

        public ViewHolder(View itemView) {
            super(itemView);
            mBookName = itemView.findViewById(R.id.bookName);
        }
        public void setData(BookBean book) {
            mBookName.setText(book.getBookName());
        }
    }
}


