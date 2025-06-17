package pages.agoda;

import common.Constants;
import data.SortBy;
import utils.AssertionUtils;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SelenideWait;
import com.codeborne.selenide.WebDriverRunner;
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

    private SelenideElement getSortButton(SortBy sortBy) {
        return $x(String.format("//button[@data-element-name='%s']", sortBy.getDataElementName()));
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
}
