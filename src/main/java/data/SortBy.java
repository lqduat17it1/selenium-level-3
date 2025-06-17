package data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortBy {
    BEST_MATCH("search-sort-recommended"),
    LOWEST_PRICE_FIRST("search-sort-price"),
    DISTANCE("search-sort-distance-landmark"),
    TOP_REVIEWED("search-sort-guest-rating"),
    HOT_DEALS("search-sort-secret-deals");

    private final String dataElementName;
}
