package example.tctctc.com.tybookreader.bookstore;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import example.tctctc.com.tybookreader.bookstore.bean.SearchBook;
import example.tctctc.com.tybookreader.bookstore.bean.site.BabaSite;
import example.tctctc.com.tybookreader.bookstore.bean.site.BaseSite;
import example.tctctc.com.tybookreader.bookstore.bean.site.BiqugeSite;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Function:
 * Created by tanchao on 2018/10/20.
 */

public class SearchManager {

    public Observable<SearchBook> searchBook(final String keyWord){
        List<BaseSite> searchSites = new ArrayList<>();
        searchSites.add(new BiqugeSite());
        searchSites.add(new BabaSite());

        List<Observable<List<SearchBook>>>  observables = new ArrayList<>();
        for (final BaseSite baseSite:searchSites){
            Observable<List<SearchBook>> observable = Observable.create(new ObservableOnSubscribe<List<SearchBook>>() {
                @Override
                public void subscribe(ObservableEmitter<List<SearchBook>> e) throws Exception {
                    List<SearchBook> searchBooks = baseSite.searchBooks(keyWord);
                    e.onNext(searchBooks);
                    e.onComplete();
                }
            }).subscribeOn(Schedulers.io());
            observables.add(observable);
        }

        Observable.concat(observables).map(new Function<List<SearchBook>, List<SearchBook>>() {
            @Override
            public List<SearchBook> apply(List<SearchBook> searchBooks) throws Exception {

                List<Comparator> comparators = new ArrayList<>();
                return null;
            }
        })
    }
}
