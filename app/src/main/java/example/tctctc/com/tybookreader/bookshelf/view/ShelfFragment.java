package example.tctctc.com.tybookreader.bookshelf.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseFragment;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bookshelf.adapter.ShelfBookAdapter;
import example.tctctc.com.tybookreader.bookshelf.contact.ShelfContact;
import example.tctctc.com.tybookreader.bookshelf.model.BookDao;
import example.tctctc.com.tybookreader.bookshelf.presenter.ShelfPresenter;
import example.tctctc.com.tybookreader.common.view.GridSpacingItemDecoration;

/**
 * Created by tctctc on 2017/3/18.
 */

public class ShelfFragment extends BaseFragment implements ShelfBookAdapter.OnClickCallBack, ShelfContact.View {


    @BindView(R.id.bookRecycle)
    RecyclerView mBookRecycle;

    @BindView(R.id.add_book)
    FloatingActionButton mAddBookBt;

    private ShelfBookAdapter mAdapter;
    private List<BookBean> mBooks;

    private ShelfPresenter mPresenter;

    @Override
    protected void initView() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBooks = new ArrayList<>();
        mAdapter = new ShelfBookAdapter(mBooks, getActivity(), this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mBookRecycle.setLayoutManager(layoutManager);
        mBookRecycle.setAdapter(mAdapter);
        mBookRecycle.setLongClickable(true);
        mBookRecycle.addItemDecoration(new GridSpacingItemDecoration(3, 80, true));
        mPresenter = new ShelfPresenter();
        mPresenter.setVM(new BookDao(), this);
        mPresenter.onLoadBookList();

    }

    @Override
    public void OnLongClick(int position,View view) {
        Toast.makeText(getContext(),"长按书本",Toast.LENGTH_SHORT).show();
        showBookLongClickMenu(position,view);
    }

    @Override
    public void OnBookClick(int position,View view) {
        Intent intent = new Intent(getActivity(), ReadBookActivity.class);
        intent.putExtra("book", mBooks.get(position));
        startActivity(intent);
    }



    @OnClick({R.id.add_book})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_book:
                Intent intent = new Intent(getActivity(), ImportBookActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    @Override
    public void refreshBookList(List<BookBean> books) {
        mBooks.clear();
        mBooks.addAll(books);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    public void showBookLongClickMenu(int position,View view) {
        PopupWindow popupWindow  = new PopupWindow();
        View contentView = buildBookLongClickMenuView(position,popupWindow);
        if (contentView == null){
            return;
        }
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setContentView(contentView);

        popupWindow.showAsDropDown(view,view.getMeasuredWidth()/2,-view.getMeasuredHeight()/2);
    }


    private View buildBookLongClickMenuView(final int position, final PopupWindow popupWindow){
        final BookBean bookBean = mBooks.get(position);
        if (bookBean.getBookType() == BookBean.BOOK_TYPE_LOCAL){
            View view = View.inflate(getContext(),R.layout.menu_book_long_click_local,null);

            View.OnClickListener onBookMenuClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        //移除book
                        case R.id.remove:
                            mBooks.remove(position);
                            mAdapter.notifyItemChanged(position);
                            mPresenter.onDeleteBook(bookBean.getBookId());
                            popupWindow.dismiss();
                            break;
                    }
                }
            };

            TextView deleteTv = view.findViewById(R.id.remove);
            deleteTv.setOnClickListener(onBookMenuClickListener);

            return view;
        }else{

        }
        return null;
    }
}
