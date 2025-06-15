package tests.agoda;

import com.codeborne.selenide.Configuration;
import data.SortBy;
import data.StayType;
import models.HotelSearchRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.agoda.HomePage;
import pages.agoda.SearchResultPage;
import tests.BaseTest;
import utils.DateTimeUtils;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class TC01 extends BaseTest {

    HomePage homePage;
    SearchResultPage searchResultPage;

    @BeforeMethod
    public void setUp() {
        homePage = new HomePage();
        searchResultPage = new SearchResultPage();
    }

    @Test(description = "Search and sort hotel successfully")
    public void searchAndSortHotelSuccessfully() {
        open("/");

        HotelSearchRequest hotelSearchRequest = HotelSearchRequest.builder()
                .stayType(StayType.OVERNIGHT)
                .destination("Da Nang")
                .checkInDate(DateTimeUtils.getNextFriday())
                .checkOutDate(DateTimeUtils.getNextFriday().plusDays(3))
                .numberOfRooms(2)
                .numberOfAdults(4)
                .build();

        homePage.search(hotelSearchRequest);
        searchResultPage.checkResultDestination(hotelSearchRequest.getDestination());

        searchResultPage.sortResult(SortBy.LOWEST_PRICE_FIRST);
        searchResultPage.checkResultSortByLowestPrice();
        searchResultPage.checkResultDestination(hotelSearchRequest.getDestination());

    }
}
