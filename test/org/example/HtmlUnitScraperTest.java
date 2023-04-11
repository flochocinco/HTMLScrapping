package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlUnitScraperTest {

    @Test
    void getCookieSession() {
        assertNotNull(HtmlUnitScraper.getCookieSession());
    }
}