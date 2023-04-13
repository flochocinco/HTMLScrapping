package org.example;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class HtmlUnitScraperTest {

    @Test
    void getCookieSession() {
        assertNotNull(HtmlUnitScraper.getCookieSession());
    }

    @Test
    void isCookieValid(){
        assertTrue(HtmlUnitScraper.isCookieValid());
    }

    @Test
    void getExtractedTest(){
        String cookie = "_vinted_fr_session=UUVyeGI0TU9GS0xyOWpobDhwRFRtU1BlZ001aXBTMWtHWUxSY0ZMU0ZsTHk5ZTdzMjlSZDVqUHVzL3U2U2tveDVxQ1BETzVVdkN6MEpiY2hmWkxKZStlN2RLcDV1QmpiNVlsRG9PV1QvSGhYUDhFQkl3aEtYcEZ4VmNzejIzV1l0YVMydXZkQXlGYXd6VUJ3OGFBWUo5TW1BYlEyRG9Rd2VrNDdFaitySDRFaDFwWktpTVVVU2lRNGNpYnVpd3pKcVlDTjExTWswbmFKUWlRNDBhREJoNlh6Nnk3TWw1K0F5dVFjM284a0Vmbi9vckxHYXZyMlJIanBwa3NLQ1FzbnErdmVUVWg5V3hOczY3TkhVeVQ2Zzhkdk1OSSt2SWhWZEdoOUdWay9XMHFzL2pwSDh3TXd1bzlKeTZ1akVoVFJtY01lbzVXSG03M2JXVFk3UW90dUdITlJxdkFZd2JuSDhWdnNhbHF4UlNmV1R0N1FVNDhkajlIb29mdjZkbm9lZHV6Y2tUbjV1VFdieHQ0SGtwRkd3VWJtYUF4dC8wZnRsbU9MazN5Q29qZU4zbHovb0pUKzRWd0ZJdGE3VExtRTRFR2FqcmQzeXVyMUlMZGt3VENvditJVkM1cUs5SCtvUEZ0OTZtcXhXeEliSUVrcXlBNFZwWlVqSkVmbnl5SHVvcEMzUFVDOHIwWEtwT2NBUllodlhsTXF4eEdoU0JLMkQyTFJSWjIrZUkyZTNWTUlPd1JwSlJaeHdIeDV2VkxtNVhWcERiT2F0VUlZZ3laVy81Y2VhalJRZlRGeWN4SG9mdUV6OEw1VHJNZmZ0UzhsZnNtWStIeUZzeEpvTnVNcTBDRVZlRXFCQytTRG1heWRRYlVWMHZJZlUzeDI2VzlSN3p1WE90TUZSb2M1eGNPMGRKZWNnUFVVT0NidmRKTU5jQnBkb0cvSXh0d0FRM0J2Y1Ixd2hLbW5ydmxTbUVBcUxlenFFMFM1N2twZGpHUkFieGxiNVRxZ1dncUNGQkFMU1l3ZC8vODVhU1Z4RWRFYWU0d3A2cENqYWhqRG9sb1F5WGJIZW5rdFB1NE00bTk4TXBhVUtFTTVkTUQ3UGdGWkZUZGpwbXozMG54K2FPcG5xeHlSQWlTaW1Nc251clFWb09TNWtFeS9NbWdvNnl1dnlXbkNoZ2htR2hLN2JCdnlpWDYrSUk4ek92ZExFUTBnTWJmMmhhY2kzaUlnVjZEYnJXZjI5aVdRc2x4SVI5UTU3UUkrTVdGOXQxSFEzaWF6aVMyY2RaNkpVTzdCSGFPR2F6R0MxcXFpNTl6WWx0a2U0d05qMU1vZEpzL3Y4NC9oTnMyV0hDVzcyT0ZYRkgzalRTVlo4U0VOMnlKQkh3eDFiMEFBbkZRT3grWEtCczdoY2E5TXVEbW5lTlJoaWtWV1VrZ1I3U1E0b3Z1Nyt5aUsyZy9Na0hScEpacDlBUUZYdE9wV1NnPT0tLUVyL0VrMHZFdUMvQ1NDa1pjc3RxNUE9PQ==--7aa42422b3d2f0f2f51f267693e62fcddc087328; domain=.www.vinted.fr; path=/; expires=Thu, 20 Apr 2023 20:03:25 GMT; secure; HttpOnly; SameSite=Lax";
        assertEquals(HtmlUnitScraper.getExpireDate(cookie), "Thu, 20 Apr 2023 20:03:25 GMT");
    }

    @Test
    void isExpired(){
        assertTrue(HtmlUnitScraper.isExpired("Thu, 20 Apr 1990 20:03:25 GMT"));
        Instant twoHoursInTheFuture = Instant.now().plus(2, ChronoUnit.HOURS);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        assertFalse(HtmlUnitScraper.isExpired(format.format(Date.from(twoHoursInTheFuture))));
    }
}