package example.tctctc.com.tybookreader.bookshelf.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseAdapter;
import example.tctctc.com.tybookreader.base.BasePageFragment;
import example.tctctc.com.tybookreader.bean.BookBean;
import example.tctctc.com.tybookreader.bean.MarkBean;
import example.tctctc.com.tybookreader.bookshelf.contact.MarkContact;
import example.tctctc.com.tybookreader.bookshelf.model.MarkDao;
import example.tctctc.com.tybookreader.bookshelf.presenter.MarkPresenter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by tctctc on 2017/4/7.
 * Function:
 */
public class MarkListFragment extends BasePageFragment implements MarkContact.View {

    private List<MarkBean> mList;

    private BaseAdapter adapter;

    @BindView(R.id.directory_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    TextView mEmpty;

    private MarkPresenter mPresenter;

    private BookBean mBookBean;

    private MarkListFragment() {
    }

    public static MarkListFragment getInstance(BookBean book) {
        MarkListFragment fragment = new MarkListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBookBean = (BookBean) getArguments().getSerializable("book");
        }
    }

    @Override
    protected void initView() {
        mList = new ArrayList<>();
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mPresenter = new MarkPresenter();
        mPresenter.setVM(new MarkDao(mBookBean), this);
        adapter = new BaseAdapter<>(getContext(), R.layout.mark_list_item, mList, new BaseAdapter.OnItem() {
            @Override
            public void onClick(int position) {
                MarkBean markBean = mList.get(position);
                mPresenter.itemClick(markBean);
            }

            @Override
            public void onLongClick(int position) {
                showDeleteDialog();
            }

            @Override
            public void onBind(BaseAdapter.BaseViewHolder mHolder, int position) {
                MarkBean markBean = mList.get(position);
                mHolder.setText(R.id.chapterName, markBean.getChapterName());
                mHolder.setText(R.id.content, markBean.getDescribe());
                mHolder.setText(R.id.time, markBean.getTime());
                mHolder.setText(R.id.per, markBean.getProgress());
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);

        mRxManager.onEvent("updateMark", new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) {
                judgeLoadData(true);
            }
        });
    }

    private void showDeleteDialog() {
        Toast.makeText(getContext(), "弹出删除", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.mark_list_fragment;
    }

    @Override
    public boolean fetchData() {
        mPresenter.loadMarkList();
        return true;
    }

    @Override
    public void showMarkList(List<MarkBean> list) {
        mList.clear();
        mList.addAll(list);
        adapter.notifyDataSetChanged();

        if (mList.isEmpty()) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(MarkBean bean) {
        mRxManager.post("mark", bean);
        mRxManager.post("close drawer", "");
    }
}
