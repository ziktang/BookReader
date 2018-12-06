package example.tctctc.com.tybookreader.bookshelf.importBook;

import java.util.Comparator;

/**
 * Function:
 * Created by tanchao on 2018/10/15.
 */

public abstract class BaseComparator<T> implements Comparator<T>{

    @Override
    public int compare(T t1, T t2) {
        int result = nullCheckComparator(t1,t2);
        return result == 0?realCompare(t1, t2):result;
    }

    public int nullCheckComparator(Object o1,Object o2){
        if (o1 !=null && o2 != null){
            return 0;
        }else if (o1 == null && o2 == null){
            return 0;
        }else if (o1 != null && o2 == null){
            return 1;
        }else{
            return -1;
        }
    }

    protected abstract int realCompare(T t1, T t2);
}
