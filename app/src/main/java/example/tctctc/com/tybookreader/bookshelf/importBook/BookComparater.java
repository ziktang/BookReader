package example.tctctc.com.tybookreader.bookshelf.importBook;

/**
 * Function:
 * Created by tanchao on 2018/10/15.
 */

public class BookComparater {
    public static class BooleanComparater extends BaseComparator<Boolean>{

        @Override
        int realCompare(Boolean t1, Boolean t2) {
            if (t1 && t2) {
                return 0;
            } else if (t1 && !t2) {
                return -1;
            } else if (!t1 && t2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static class LongComparater extends BaseComparator<Long>{
        @Override
        int realCompare(Long t1, Long t2) {
            if (t1 > t2) {
                return -1;
            } else if (t1 < t2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static class StringHeadCharComparater extends BaseComparator<String>{

        @Override
        int realCompare(String t1, String t2) {
            return t1.compareTo(t2);
        }
    }
}
