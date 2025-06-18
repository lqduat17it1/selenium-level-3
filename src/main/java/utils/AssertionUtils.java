package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssertionUtils {
    /**
     * Checks if the list is sorted in ascending order
     *
     * @param list List of comparable elements to check
     * @param <T> Type of elements that implement Comparable
     * @throws AssertionError if elements are not sorted in ascending order
     */
    public static <T extends Comparable<T>> void assertSortedAscending(List<T> list) {
        List<T> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        if (!list.equals(sorted)) {
            throw new AssertionError("List is not sorted in ascending order: " + list);
        }
    }
}
