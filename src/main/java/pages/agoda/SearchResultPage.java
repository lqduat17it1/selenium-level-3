package pages.agoda;

import common.Constants;
import data.SortBy;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import driver.DriverUtils;
import pages.BasePage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResultPage extends BasePage {
    private static final int MIN_REQUIRED_ELEMENTS = 5;

    private SelenideElement getSortButton(SortBy sortBy) {
        return $x(String.format("//button[@data-element-name='%s']", sortBy.getDataElementName()));
    }

    private ElementsCollection getPriceElements() {
        return $$("li[data-selenium='hotel-item'] div[data-element-name='final-price'] span:last-child");
    }

    private ElementsCollection getDestinationElements() {
        return $$("div[data-selenium='area-city'] span");
    }

    public void sortByLowestPrice() {
        sortResult(SortBy.LOWEST_PRICE_FIRST);
    }

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
    private ElementsCollection scrollUntilEnoughElements(ElementsCollection elements) {
        int attempt = 0;
        int lastSize = elements.size();

        while (elements.size() < MIN_REQUIRED_ELEMENTS && attempt < Constants.MAX_SCROLL_ATTEMPTS) {
            DriverUtils.pageDown();
            attempt++;
            elements.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(lastSize + 1), Constants.SCROLL_TIMEOUT);
            elements = getDestinationElements();
            lastSize = elements.size();
        }

        elements.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(MIN_REQUIRED_ELEMENTS));
        return elements;
    }

    /**
     * Extracts price values from the first 5 price elements
     *
     * @param priceElements Collection of price elements
     * @return List of parsed price values
     */
    private List<Double> extractPrices(ElementsCollection priceElements) {
        List<Double> prices = new ArrayList<>();
        for (int i = 0; i < MIN_REQUIRED_ELEMENTS; i++) {
            String priceText = priceElements.get(i)
                    .shouldBe(visible)
                    .getText()
                    .replaceAll("[^\\d.]+", "");
            if (!priceText.isEmpty()) {
                prices.add(Double.parseDouble(priceText));
            }
        }
        return prices;
    }

    /**
     * Checks if the list is sorted in ascending order
     *
     * @param prices List of prices to check
     * @throws AssertionError if prices are not sorted from lowest to highest
     */
    private void checkPricesAreSorted(List<Double> prices) {
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        if (!prices.equals(sorted)) {
            throw new AssertionError("Prices are not sorted from lowest to highest: " + prices);
        }
    }

    /**
     * Checks if the search results are sorted by lowest price.
     * It verifies that the first 5 prices are sorted from lowest to highest.
     */
    public void checkResultSortByLowestPrice() {
        ElementsCollection priceElements = scrollUntilEnoughElements(getPriceElements());
        List<Double> prices = extractPrices(priceElements);
        checkPricesAreSorted(prices);
    }

    /**
     * Checks destination text for each element
     *
     * @param destinations Collection of destination elements to verify
     * @param expectedDestination Expected destination text
     */
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
    public void checkResultDestination(String destination) {
        ElementsCollection destinations = scrollUntilEnoughElements(getDestinationElements());
        checkDestinations(destinations, destination);
        DriverUtils.scrollToTop();
    }
}
