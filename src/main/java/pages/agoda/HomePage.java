package pages.agoda;

import com.codeborne.selenide.SelenideElement;
import data.OccupancyType;
import data.StayType;
import models.HotelSearchRequest;
import pages.BasePage;
import io.qameta.allure.Step;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HomePage extends BasePage {
    private final SelenideElement destinationInput = $("#textInput");
    private final SelenideElement searchButton = $x("//button[@data-selenium='searchButton']");

    private SelenideElement getStayTypeElement(StayType stayType) {
        return $x(String.format("//button[@data-element-name='%s' and @data-hh-booking-duration-type=%d]", stayType.getDataElementName(), stayType.getDataHhBookingDurationType()));
    }

    private SelenideElement getAutoCompleteResult(int index) {
        return $x(String.format("//div[@id='search-box-autocomplete-id']//li[@data-element-index='%d']", index));
    }

    private SelenideElement getDateElement(LocalDate date) {
        return $x(String.format("//span[@data-selenium-date='%s']", date));
    }

    private SelenideElement getOccupancyElement(String dataSelenium) {
        return $x(String.format("//div[@data-selenium='%s']", dataSelenium));
    }

    private SelenideElement getOccupancyValueElement(SelenideElement container) {
        return container.$("div p");
    }

    private SelenideElement getOccupancyPlusButton(SelenideElement container) {
        return container.$x(".//button[@data-selenium='plus']");
    }

    private SelenideElement getOccupancyMinusButton(SelenideElement container) {
        return container.$x(".//button[@data-selenium='minus']");
    }

    /**
     * Search hotels using the provided request.
     * This method fills out the search form with the given parameters and submits it.
     *
     * @param request the hotel search request containing all search parameters
     * @return SearchResultPage instance representing the results page
     */
    @Step("Search for hotels with specified parameters")
    public SearchResultPage search(HotelSearchRequest request) {
        selectStayType(request.getStayType());
        fillDestination(request.getDestination());
        selectDates(request.getCheckInDate(), request.getCheckOutDate());
        setOccupancy(request);

        submitSearch();
        return page(SearchResultPage.class);
    }

    @Step("Fill destination with value: {destination}")
    private void fillDestination(String destination) {
        destinationInput
            .shouldBe(visible)
            .setValue(destination);
        selectFirstSuggestion();
    }

    @Step("Select first suggestion from autocomplete")
    private void selectFirstSuggestion() {
        selectResult(0);
    }

    @Step("Select check-in date: {checkIn} and check-out date: {checkOut}")
    private void selectDates(LocalDate checkIn, LocalDate checkOut) {
        selectDate(checkIn);
        selectDate(checkOut);
    }

    @Step("Set occupancy parameters: rooms={request.numberOfRooms}, adults={request.numberOfAdults}, children={request.numberOfChildren}")
    private void setOccupancy(HotelSearchRequest request) {
        selectOccupancy(OccupancyType.ROOMS, request.getNumberOfRooms());
        selectOccupancy(OccupancyType.ADULTS, request.getNumberOfAdults());
        selectOccupancy(OccupancyType.CHILDREN, request.getNumberOfChildren());
    }

    @Step("Submit search and switch to results window")
    private void submitSearch() {
        searchButton.shouldBe(enabled).click();
        switchTo().window(1);
    }

    @Step("Select stay type: {stayType}")
    private void selectStayType(StayType stayType) {
        getStayTypeElement(stayType)
            .shouldBe(visible)
            .click();
    }

    @Step("Select autocomplete result at index: {index}")
    private void selectResult(int index) {
        getAutoCompleteResult(index)
            .shouldBe(visible)
            .click();
    }

    @Step("Select date: {date}")
    private void selectDate(LocalDate date) {
        getDateElement(date)
            .shouldBe(visible)
            .click();
    }

    @Step("Set {type} occupancy to {value}")
    private void selectOccupancy(OccupancyType type, int value) {
        if (value <= 0) return;

        SelenideElement occupancyContainer = getOccupancyElement(type.getDataSelenium());
        SelenideElement valueElement = getOccupancyValueElement(occupancyContainer);
        SelenideElement plusButton = getOccupancyPlusButton(occupancyContainer);
        SelenideElement minusButton = getOccupancyMinusButton(occupancyContainer);

        int current = Integer.parseInt(valueElement.shouldBe(visible).getText());

        while (current < value) {
            plusButton.shouldBe(enabled).click();
            current++;
        }
        while (current > value) {
            minusButton.shouldBe(enabled).click();
            current--;
        }
    }
}
