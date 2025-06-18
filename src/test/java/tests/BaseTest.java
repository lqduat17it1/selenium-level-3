package tests;

import org.testng.annotations.BeforeClass;

import static com.codeborne.selenide.Selenide.*;

public class BaseTest {
    @BeforeClass
    public void init() {
        open("/");
    }
}
