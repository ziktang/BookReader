package example.tctctc.com.tybookreader.bookshelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
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

    //记录选中的item的数据和状态 1 选中状态  2未选中状态
    private HashMap<BookBean, Integer> mHashMap;

    //是否处于长按选择状态
    private boolean isLongClick = false;

    public interface OnClickCallBack {
        //长按回调
        void OnLongClick();
        void OnBookClick(BookBean bookBean);

        void selectBook(BookBean bookBean);
        void unSelectBook(BookBean bookBean);
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
                //只有当前不处于长按这个状态时才能进入，否则已经处于的话就无任何操作
                if (!isLongClick) {
                    select(holder, position);
                    isLongClick = true;
                    notifyDataSetChanged();
                    mBack.OnLongClick();
                }
                return false;
            }
        });

        //设置每本书的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当不处于长按状态，则直接打开书籍
                if (!isLongClick) {
                    //打开图书
                    mBack.OnBookClick(mBooks.get(position));
                }
                //当正处于长按状态，则选择或取消选择
                else {
                    select(holder, position);
                }
            }
        });
        holder.setData(mBooks.get(position));
    }

    /**
     * 当处于长按状态，选择某本书
     * @param holder 选择的item的holder
     * @param position 选择的item的位置
     */
    private void select(ViewHolder holder, Integer position) {
        BookBean book = mBooks.get(position);
        //若原本此书处于选中状态，则现在应该变为未选中
        if (mHashMap.get(book) == 1) {
            mHashMap.put(book, 2);
            holder.mNormal.setVisibility(View.VISIBLE);
            holder.mCheck.setVisibility(View.INVISIBLE);
            mBack.unSelectBook(book);
        }
        //若原本此书处于未选中状态，则现在应该变为选中
        else {
            mHashMap.put(book, 1);
            holder.mNormal.setVisibility(View.INVISIBLE);
            holder.mCheck.setVisibility(View.VISIBLE);
            mBack.selectBook(book);
        }
        notifyItemChanged(position);
    }

    /**
     * 根据每个数据的状态判断应该显示什么图标
     * @param holder
     * @param position
     */
    private void show(ViewHolder holder, Integer position) {
        BookBean book = mBooks.get(position);
        if (mHashMap.get(book) == 1) {
            holder.mNormal.setVisibility(View.INVISIBLE);
            holder.mCheck.setVisibility(View.VISIBLE);
        } else {
            holder.mNormal.setVisibility(View.VISIBLE);
            holder.mCheck.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 退出长按状态时，需要重置长按状态、全选状态和已选数量
     */
    public void statusInit() {
        isLongClick = false;
        mHashMap = new HashMap<>();
        for (int i = 0; i < mBooks.size(); i++) {
            mHashMap.put(mBooks.get(i), 2);
        }
    }

    public boolean isLongClick() {
        return isLongClick;
    }

    @Override
    public int getItemCount() {
        return mBooks==null?0:mBooks.size();
    }

    /**
     * 返回记录数据选择状态的map
     * @return
     */
    public HashMap<BookBean, Integer> getBookMap() {
        return mHashMap;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mBookName;
        ImageView mNormal;
        ImageView mCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            mBookName = (TextView) itemView.findViewById(R.id.bookName);
            mCheck = (ImageView) itemView.findViewById(R.id.checked);
            mNormal = (ImageView) itemView.findViewById(R.id.normal);
        }

        public void setData(BookBean book) {
            mBookName.setText(book.getBookName());
            if (isLongClick) {
                show(this, getAdapterPosition());
            } else {
                mNormal.setVisibility(View.INVISIBLE);
                mCheck.setVisibility(View.INVISIBLE);
            }
        }
    }
}


