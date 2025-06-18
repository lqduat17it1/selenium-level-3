package data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OccupancyType {
    ROOMS("occupancyRooms"),
    ADULTS("occupancyAdults"),
    CHILDREN("occupancyChildren");

    private final String dataSelenium;
}
