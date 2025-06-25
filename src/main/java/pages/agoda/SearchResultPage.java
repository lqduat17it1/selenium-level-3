package pages.agoda;

import com.codeborne.selenide.*;
import common.Constants;
import data.SortBy;
import models.FilterInfo;
import utils.AssertionUtils;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.*;

import driver.DriverUtils;
import lombok.Getter;
import pages.BasePage;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchResultPage extends BasePage {
    private static final int MIN_REQUIRED_ELEMENTS = 5;

    private final ElementsCollection priceElements = $$("li[data-selenium='hotel-item'] div[data-element-name='final-price'] span:last-child");
    private final ElementsCollection destinationElements = $$("div[data-selenium='area-city'] span");
    private final ElementsCollection ratingElements = $$("div[data-testid='rating-container']");
    private final SelenideElement minimumPriceBoxElement = $("#SideBarLocationFilters #price_box_0");
    private final SelenideElement maximumPriceBoxElement = $("#SideBarLocationFilters #price_box_1");

    private SelenideElement getSortButton(SortBy sortBy) {
        return $x(String.format("//button[@data-element-name='%s']", sortBy.getDataElementName()));
    }

    private SelenideElement getStarRatingCheckbox(int starRating) {
        return $x(String.format("//legend[@id='filter-menu-StarRatingWithLuxury']/following-sibling::ul//label[@data-element-value=%d]//input", starRating));
    }

    @Step("Click sort button to sort results by lowest price")
    public void sortByLowestPrice() {
        sortResult(SortBy.LOWEST_PRICE_FIRST);
    }

    @Step("Sort results by: {sortBy}")
    private void sortResult(SortBy sortBy) {
        getSortButton(sortBy).shouldBe(visible).click();
    }

    /**
     * Scrolls and waits for elements to load until the minimum required number is reached
     * or maximum attempts are exhausted.
     *
     * @param elements The collection to check and wait for
     * @return Updated collection after scrolling
     */
    @Step("Scroll until minimum {MIN_REQUIRED_ELEMENTS} elements are loaded")
    private ElementsCollection scrollUntilEnoughElements(ElementsCollection elements) {
        new SelenideWait(WebDriverRunner.getWebDriver(), Constants.SCROLL_TIMEOUT.toMillis(), Constants.SCROLL_TIMEOUT.toMillis() / 2)
                .until(driver -> {
                    if (elements.size() < MIN_REQUIRED_ELEMENTS) {
                        DriverUtils.pageDown();
                        return false;
                    }
                    return true;
                });

        return elements.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(MIN_REQUIRED_ELEMENTS));
    }

    /**
     * Extracts price values from the first 5 price elements
     *
     * @param priceElements Collection of price elements
     * @return List of parsed price values
     */
    @Step("Extract price values from elements")
    private List<Double> extractPrices(ElementsCollection priceElements) {
        return priceElements.stream()
                .limit(MIN_REQUIRED_ELEMENTS)
                .map(element -> element.shouldBe(visible).getText().replaceAll("[^\\d.]+", ""))
                .filter(priceText -> !priceText.isEmpty())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the search results are sorted by lowest price.
     * It verifies that the first 5 prices are sorted from lowest to highest.
     */
    @Step("Verify that search results are sorted by lowest price")
    public void checkResultSortByLowestPrice() {
        ElementsCollection priceElements = scrollUntilEnoughElements(getPriceElements());
        List<Double> prices = extractPrices(priceElements);
        AssertionUtils.assertSortedAscending(prices);
    }

    /**
     * Checks destination text for each element
     *
     * @param destinations Collection of destination elements to verify
     * @param expectedDestination Expected destination text
     */
    @Step("Check if destinations match expected value: {expectedDestination}")
    private void checkDestinations(ElementsCollection destinations, String expectedDestination) {
        for (int i = 0; i < MIN_REQUIRED_ELEMENTS; i++) {
            destinations.get(i)
                .shouldBe(visible)
                .shouldHave(text(expectedDestination));
        }
    }

    /**
     * Checks if the search results displayed correctly with first 5 hotels(destination).
     *
     * @param destination The destination to check in the search results.
     */
    @Step("Verify search results display correct destination: {destination}")
    public void checkResultDestination(String destination) {
        ElementsCollection destinations = scrollUntilEnoughElements(getDestinationElements());
        checkDestinations(destinations, destination);
        DriverUtils.scrollToTop();
    }

    /**
     * Fills the price filter with minimum and maximum price values.
     *
     * @param minPrice Minimum price to set in the filter
     * @param maxPrice Maximum price to set in the filter
     */
    @Step("Fill price filter with min: {minPrice} and max: {maxPrice}")
    public void fillPriceFilter(double minPrice, double maxPrice) {
        minimumPriceBoxElement.shouldBe(visible).setValue(String.valueOf(minPrice));
        maximumPriceBoxElement.shouldBe(visible).setValue(String.valueOf(maxPrice)).pressEnter();
    }

    /**
     * Selects a star rating checkbox based on the provided star rating.
     *
     * @param starRating The star rating to select (1 to 5)
     */
    @Step("Select star rating: {starRating}")
    public void selectStarRatingFilter(int starRating) {
        getStarRatingCheckbox(starRating).shouldBe(visible).click();
    }

    /**
     * Filters search results based on the provided filter information.
     * It fills the price filter and selects the star rating.
     *
     * @param filterInfo The filter information containing minimum price, maximum price, and star rating
     */
    @Step("Filter search results with provided filter information")
    public void filterSearchResults(FilterInfo filterInfo) {
        fillPriceFilter(filterInfo.getMinimumPrice(), filterInfo.getMaximumPrice());
        selectStarRatingFilter(filterInfo.getStarRating());
    }

    @Step("Check filtered results based on provided filter information")
    public void checkResultFiltered(FilterInfo filterInfo) {
        ElementsCollection priceElements = scrollUntilEnoughElements(getPriceElements());
        List<Double> prices = extractPrices(priceElements);

        for (Double price : prices) {
            assertThat(price).isBetween(filterInfo.getMinimumPrice(), filterInfo.getMaximumPrice());
        }

        for (SelenideElement ratingElement : getRatingElements()) {
            int count = ratingElement.$$("svg").size();
            assertThat(count).isLessThanOrEqualTo(filterInfo.getStarRating());
        }

    }

    public void removeFilter() {

    }
}
