package data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterType {
    POPULAR_FILTERS("filter-menu-RecommendedByDestinationCity"),
    PROPERTY_TYPE("filter-menu-AccomdType"),
    STAR_RATING("filter-menu-StarRatingWithLuxury");

    private String id;

}
