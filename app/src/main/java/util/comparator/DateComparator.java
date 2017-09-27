package util.comparator;

import java.util.Comparator;

import db.entry.Book;

/**
 * Created by HanyLuv on 2015-10-26.
 */
public class DateComparator implements Comparator<Book> {

    @Override
    public int compare(Book b1, Book b2) {
        String published1 = b1.getPublished().replaceAll("[^0-9]", "");
        String published2 = b2.getPublished().replaceAll("[^0-9]", "");
        return published2.compareTo(published1);
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}
