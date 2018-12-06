package example.tctctc.com.tybookreader.bookstore.bean.site;

import android.text.TextUtils;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import example.tctctc.com.tybookreader.bookstore.bean.SearchBook;

/**
 * Function:
 * Created by tanchao on 2018/10/20.
 */

public class BabaSite extends BaseSite {

    public static final String SEARCH_SITE = "https://so.88dus.com/search/so.php?search_field=0&q=";
    public static final String SITE_NAME = "八八读书网";
    public static final String SITE = "https://www.88dus.com";

    public BabaSite() {

    }

    @Override
    List<SearchBook> searchBooks(String keyWord) {
        List<SearchBook> searchBooks = new ArrayList<>();
        if (TextUtils.isEmpty(keyWord)) {
            return searchBooks;
        }
        try {
            String searchSite = SEARCH_SITE + keyWord;

            Document document = Jsoup.connect(searchSite).get();
            Element table = document.getElementsByTag("ops_cover").first();

            Elements bookList = table.getElementsByClass("block");
            Log.i(TAG, "***********************  " + SITE_NAME + "  ******************************");


            for (int i = 0; i < bookList.size() && i <50; i++) {
                Element element = bookList.get(i);
                //搜索结果字段
                for (Element e : element.getAllElements()) {
                    Log.i(TAG, e.ownText());
                }

                SearchBook searchBook = new SearchBook();
                searchBook.setBookName(element.getElementsByTag("a").get(1).ownText());
                searchBook.setBookHome(SITE+element.getElementsByTag("a").get(1).attr("href"));

                String authorText = element.getElementsContainingOwnText("作者").first().ownText();
                searchBook.setAuthor(authorText.substring(3,authorText.length()));
                searchBook.setSite(SEARCH_SITE);
                searchBooks.add(searchBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchBooks;
    }
}
