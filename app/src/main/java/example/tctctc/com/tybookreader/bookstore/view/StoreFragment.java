package example.tctctc.com.tybookreader.bookstore.view;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import example.tctctc.com.tybookreader.R;
import example.tctctc.com.tybookreader.base.BaseFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tctctc on 2017/3/18.
 */

public class StoreFragment extends BaseFragment {

    @BindView(R.id.search_et)
    EditText searchEt;

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookstore;
    }

    @OnClick({R.id.search_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_tv:
                searchBook();
                break;
            default:
                break;
        }
    }

    private void searchBook() {
        String keyWord = searchEt.getText().toString();
        if (TextUtils.isEmpty(keyWord)){
            return;
        }

        String site = "https://www.qu.la";

        final String searchSite = "https://sou.xanbhx.com/search?siteid=qula&q=" + "遮天";
        Observable.just(1).map(new Function<Integer, Integer>() {


            @Override
            public Integer apply(Integer integer) throws Exception {
                try {
                    Document document = Jsoup.connect(searchSite).get();
                    Elements elements = document.getElementsByClass("search-list");
                    Element element = elements.get(0);

                    Elements list = element.getElementsByTag("li");

                    if (list!=null){
                        Log.i(TAG,"size:"+list.size());
                        for (int i = 1;i<list.size();i++){
                            Element item = list.get(i);
                            Elements infoSpanList = item.getElementsByTag("span");
                            if (infoSpanList!=null&&infoSpanList.size() == 7){
                                Element type = infoSpanList.get(0);
                                Log.i(TAG,"      ");
                                Log.i(TAG,"书籍类型:"+type.ownText());

                                Element name = infoSpanList.get(1);
                                Elements aList = name.getElementsByTag("a");
                                Element a = aList.get(0);

                                Log.i(TAG,"书名:"+a.ownText());
                                Log.i(TAG,"主页:"+a.attr("href"));

                                Element newChapterSpan = infoSpanList.get(2);
                                Elements newChapterAList = newChapterSpan.getElementsByTag("a");
                                Element newChapterA = newChapterAList.get(0);

                                Log.i(TAG,"最新章节:"+newChapterA.ownText());
                                Log.i(TAG,"最新章节链接:"+newChapterA.attr("href"));

                                Element author = infoSpanList.get(3);
                                Log.i(TAG,"作者:"+author.ownText());

                                Element updateTime = infoSpanList.get(5);
                                Log.i(TAG,"更新时间:"+updateTime.ownText());

                                Element updateStatus = infoSpanList.get(6);
                                Log.i(TAG,"更新状态:"+updateStatus.ownText());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return integer;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        });


    }
}
