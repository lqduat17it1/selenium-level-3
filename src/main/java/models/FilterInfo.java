package models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FilterInfo {
    private double minimumPrice;
    private double maximumPrice;
    private int starRating;
}
