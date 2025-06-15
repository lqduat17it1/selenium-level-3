package models;

import data.StayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class HotelSearchRequest {
    private StayType stayType = StayType.OVERNIGHT;
    private String destination;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfRooms;
    private int numberOfAdults;
    private int numberOfChildren = 0;

}
