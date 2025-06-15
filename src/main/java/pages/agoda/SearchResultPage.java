package pages.agoda;

import com.codeborne.selenide.Selenide;
import data.SortBy;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import pages.BasePage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResultPage extends BasePage {

    /**
     * Sorts the search results based on the specified criteria.
     *
     * @param sortBy The sorting criteria to apply.
     */
    public void sortResult(SortBy sortBy) {
        $x("//button[@data-element-name='" + sortBy.getDataElementName() + "']").shouldBe(visible).click();
    }

    /**
     * Checks if the search results are sorted by lowest price.
     * It verifies that the first 5 prices are sorted from lowest to highest.
     */
    public void checkResultSortByLowestPrice() {
        final int MIN_PRICES = 5;
        final int MAX_SCROLL_ATTEMPTS = 10;
        final Duration TIMEOUT = Duration.ofSeconds(2);

        int attempt = 0;
        ElementsCollection priceElements = $$("li[data-selenium='hotel-item'] div[data-element-name='final-price'] span:last-child");
        int lastSize = priceElements.size();

        // Scroll and wait for more prices if less than required
        while (priceElements.size() < MIN_PRICES && attempt < MAX_SCROLL_ATTEMPTS) {
            pageDown();
            attempt++;
            // Wait for more elements to load (timeout)
            priceElements.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(lastSize + 1), TIMEOUT);
            priceElements = $$("li[data-selenium='hotel-item'] div[data-element-name='final-price'] span:last-child");
            lastSize = priceElements.size();
        }

        priceElements.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(MIN_PRICES));

        List<Double> prices = new ArrayList<>();
        for (int i = 0; i < MIN_PRICES; i++) {
            String priceText = priceElements.get(i)
                    .shouldBe(visible)
                    .getText()
                    .replaceAll("[^\\d.]+", "");
            if (!priceText.isEmpty()) {
                prices.add(Double.parseDouble(priceText));
            }
        }

        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        if (!prices.equals(sorted)) {
            throw new AssertionError("Prices are not sorted from lowest to highest: " + prices);
        }
    }

    /**
     * Checks if the search results displayed correctly with first 5 hotels(destination).
     *
     * @param destination The destination to check in the search results.
     */
    public void checkResultDestination(String destination) {
        final int MIN_DESTINATIONS = 5;
        final int MAX_SCROLL_ATTEMPTS = 10;
        final Duration TIMEOUT = Duration.ofSeconds(2);

        int attempt = 0;
        ElementsCollection destinations = $$("div[data-selenium='area-city'] span");
        int lastSize = destinations.size();

        // Scroll and wait for more destinations if less than required
        while (destinations.size() < MIN_DESTINATIONS && attempt < MAX_SCROLL_ATTEMPTS) {
            pageDown();
            attempt++;
            destinations.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(lastSize + 1), TIMEOUT);
            destinations = $$("div[data-selenium='area-city'] span");
            lastSize = destinations.size();
        }

        destinations.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(MIN_DESTINATIONS));

        for (int i = 0; i < MIN_DESTINATIONS; i++) {
            destinations.get(i)
                .shouldBe(visible)
                .shouldHave(text(destination));
        }
        scrollToTop();
    }
}
