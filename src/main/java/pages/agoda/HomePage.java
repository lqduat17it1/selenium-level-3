package pages.agoda;

import data.StayType;
import models.HotelSearchRequest;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HomePage {
    private static final String DESTINATION_INPUT = "#textInput";
    private static final String SEARCH_BUTTON = "//button[@data-selenium='searchButton']";
    private static final String AUTOCOMPLETE_RESULT = "//div[@id='search-box-autocomplete-id']//li[@data-element-index='%d']";
    private static final String DATE_PICKER = "//span[@data-selenium-date='%s']";
    private static final String OCCUPANCY = "//div[@data-selenium='%s']";
    private static final String OCCUPANCY_PLUS = OCCUPANCY + "//button[@data-selenium='plus']";
    private static final String OCCUPANCY_VALUE = OCCUPANCY + "/div/p";

    /**
     * Search hotels using the provided request.
     * @param hotelSearchRequest the search request
     * @return SearchResultPage
     */
    public SearchResultPage search(HotelSearchRequest hotelSearchRequest) {
        selectStayType(hotelSearchRequest.getStayType());
        $(DESTINATION_INPUT).shouldBe(visible).setValue(hotelSearchRequest.getDestination());
        selectResult(0);
        selectDate(hotelSearchRequest.getCheckInDate());
        selectDate(hotelSearchRequest.getCheckOutDate());
        selectOccupancy("occupancyRooms", hotelSearchRequest.getNumberOfRooms());
        selectOccupancy("occupancyAdults", hotelSearchRequest.getNumberOfAdults());
        selectOccupancy("occupancyChildren", hotelSearchRequest.getNumberOfChildren());
        $x(SEARCH_BUTTON).shouldBe(enabled).click();
        switchTo().window(1); // Switch to the newly opened tab
        return page(SearchResultPage.class);
    }

    private void selectStayType(StayType stayType) {
        $x("//button[.='" + stayType.getText() + "']").shouldBe(visible).click();
    }

    private void selectResult(int index) {
        $x(String.format(AUTOCOMPLETE_RESULT, index)).shouldBe(visible).click();
    }

    private void selectDate(LocalDate date) {
        $x(String.format(DATE_PICKER, date)).shouldBe(visible).click();
    }

    private void selectOccupancy(String dataSelenium, int value) {
        if (value <= 0) return;
        String valueLocator = String.format(OCCUPANCY_VALUE, dataSelenium);
        String plusLocator = String.format(OCCUPANCY_PLUS, dataSelenium);
        int current = Integer.parseInt($x(valueLocator).shouldBe(visible).getText());
        while (current < value) {
            $x(plusLocator).shouldBe(enabled).click();
            current++;
        }
    }
}
