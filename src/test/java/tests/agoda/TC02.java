package tests.agoda;

import data.StayType;
import models.FilterInfo;
import models.HotelSearchRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.agoda.HomePage;
import pages.agoda.SearchResultPage;
import tests.BaseTest;
import utils.DateTimeUtils;

public class TC02 extends BaseTest {

    HomePage homePage;
    SearchResultPage searchResultPage;
    HotelSearchRequest hotelSearchRequest;
    FilterInfo filterInfo;

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

        filterInfo = FilterInfo.builder()
                .minimumPrice(500000)
                .maximumPrice(1000000)
                .starRating(3)
                .build();
    }

    @Test(description = "Search and filter hotel successfully")
    public void searchAndFilterHotelSuccessfully() {
        homePage.search(hotelSearchRequest);
        searchResultPage.checkResultDestination(hotelSearchRequest.getDestination());

        searchResultPage.fillPriceFilter(filterInfo.getMinimumPrice(), filterInfo.getMaximumPrice());
        searchResultPage.selectStarRatingFilter(filterInfo.getStarRating());
        searchResultPage.filterSearchResults(filterInfo);
        searchResultPage.checkResultFiltered(filterInfo);
    }
}
