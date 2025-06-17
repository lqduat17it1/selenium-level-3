package driver;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;

public class DriverUtils {

    /**
     * Scrolls to the last element in the collection and waits for lazy loading.
     *
     * @param elements The collection of elements to scroll through.
     */
    public static void scrollToLastElement(ElementsCollection elements) {
        if (!elements.isEmpty()) {
            elements.last().scrollTo();
            Selenide.sleep(500); // Wait for lazy loading
        }
    }

    /**
     * Scrolls to the top of the page.
     */
    public static void scrollToTop() {
        Selenide.executeJavaScript("window.scrollTo(0, 0);");
    }

    /**
     * Scrolls to the end of the page.
     */
    public static void pageDown() {
        Selenide.executeJavaScript("window.scrollBy(0, window.innerHeight);");

    }
}
