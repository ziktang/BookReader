package example.tctctc.com.tybookreader.bookshelf.importBook;

import java.io.File;
import java.util.Comparator;
import example.tctctc.com.tybookreader.bean.ScanFile;

/**
 * Created by tctctc on 2017/3/20.
 */


/**
 * 导入，扫描书籍结果比较器
 */
public class ScanFileComparater extends BaseComparator<ScanFile>{

    @Override
    int realCompare(ScanFile scanFile1, ScanFile scanFile2) {

        File file1 = scanFile1.getFile();
        File file2 = scanFile2.getFile();

        int result = nullCheckComparator(file1,file2);
        if (result == 0){
            if (file1.isDirectory() && file2.isDirectory()){
                //文件名字符串升序
                Comparator<String> comparator = new BookComparater.StringHeadCharComparater();
                return comparator.compare(file1.getName(),file2.getName());
            }else if (!file1.isDirectory() && !file2.isDirectory()){
                //导入在前，未导入在后
                Comparator<Boolean> importComparator = new BookComparater.BooleanComparater();
                if (importComparator.compare(scanFile1.isImported(),scanFile2.isImported())!=0){
                    return importComparator.compare(scanFile1.isImported(),scanFile2.isImported());
                }

                //文件大小，降序
                Comparator<Long> fileSizeComparator = new BookComparater.LongComparater();
                if (fileSizeComparator.compare(file1.length(),file2.length())!=0){
                    return fileSizeComparator.compare(file1.length(),file2.length());
                }

                //文件名字符串升序
                Comparator<String> fileNameComparator = new BookComparater.StringHeadCharComparater();
                if (fileNameComparator.compare(file1.getName(),file2.getName())!=0){
                    return fileNameComparator.compare(file1.getName(),file2.getName());
                }

                return 0;
            }else if (file1.isDirectory() && !file2.isDirectory()){
                return -1;
            }else{
                return 1;
            }
        }else{
            return result;
        }
    }
}
