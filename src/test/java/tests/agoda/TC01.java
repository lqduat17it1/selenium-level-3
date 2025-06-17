package tests.agoda;

import data.StayType;
import models.HotelSearchRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.agoda.HomePage;
import pages.agoda.SearchResultPage;
import tests.BaseTest;
import utils.DateTimeUtils;

public class TC01 extends BaseTest {

    HomePage homePage;
    SearchResultPage searchResultPage;
    HotelSearchRequest hotelSearchRequest;

    @BeforeMethod
    public void setUp() {
        homePage = new HomePage();
        searchResultPage = new SearchResultPage();

        hotelSearchRequest = HotelSearchRequest.builder()
                .stayType(StayType.OVERNIGHT)
                .destination("Da Nang")
                .checkInDate(DateTimeUtils.getNextFriday())
                .checkOutDate(DateTimeUtils.getNextFriday().plusDays(3))
                .numberOfRooms(2)
                .numberOfAdults(4)
                .build();
    }

    @Test(description = "Search and sort hotel successfully")
    public void searchAndSortHotelSuccessfully() {
        homePage.search(hotelSearchRequest);
        searchResultPage.checkResultDestination(hotelSearchRequest.getDestination());

        searchResultPage.sortByLowestPrice();
        searchResultPage.checkResultSortByLowestPrice();
        searchResultPage.checkResultDestination(hotelSearchRequest.getDestination());
    }
}
