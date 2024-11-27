package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComicTest {
    private Comic comic;
    private final LocalDate sampleDate = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        // Testing required fields constructor.
        comic = new Comic("Marvel", "Spider-Man", 1, 1, sampleDate);
    }

    @Test
    void testRequiredFieldsConstructor() {
        assertEquals("Marvel", comic.getPublisher());
        assertEquals("Spider-Man", comic.getSeriesTitle());
        assertEquals(1, comic.getVolumeNumber());
        assertEquals(1, comic.getIssueNumber());
        assertEquals(sampleDate, comic.getPublicationDate());
    }

    @Test
    void testOptionalFields() {
        // Testing optional fields are initially null.
        assertNull(comic.getCreators());
        assertNull(comic.getPrincipleCharacters());
        assertNull(comic.getDescription());
        assertNull(comic.getValue());

        // Setting and testing optional fields.
        comic.setCreators(Arrays.asList("Stan Lee", "Steve Ditko"));
        comic.setPrincipleCharacters(Arrays.asList("Peter Parker", "Mary Jane"));
        comic.setDescription("First appearance of Spider-Man");
        comic.setValue(new BigDecimal("1000.00"));

        assertEquals(2, comic.getCreators().size());
        assertEquals(2, comic.getPrincipleCharacters().size());
        assertEquals("First appearance of Spider-Man", comic.getDescription());
        assertEquals(new BigDecimal("1000.00"), comic.getValue());
    }

    @Test
    void testToString() {
        comic.setCreators(Arrays.asList("Stan Lee"));
        comic.setValue(new BigDecimal("100.00"));
        
        String result = comic.toString();
        assertTrue(result.contains("Marvel"));
        assertTrue(result.contains("Spider-Man"));
        assertTrue(result.contains("Stan Lee"));
        assertTrue(result.contains("100.00"));
    }

    @Test
    void testValuePrecisionAndRounding() {
        // Test rounding behavior with a valid value.
        comic.setValue(new BigDecimal("999.99")); // This should be accepted.
        assertEquals(new BigDecimal("999.99"), comic.getValue(), "Should accept value with two decimal places");

        // Test rejection of values with more than 2 decimal places.
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setValue(new BigDecimal("999.999")); // This should throw an exception.
        }, "Should reject values with more than 2 decimal places");
        
        // Test minimum valid value.
        comic.setValue(new BigDecimal("0.01"));
        assertEquals(new BigDecimal("0.01"), comic.getValue(), "Should accept minimum valid value");
        
        // Test valid value with exactly 2 decimal places.
        comic.setValue(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), comic.getValue(), "Should accept valid value with 2 decimal places");
    }

    @Test
    void testDateValidation() {
        // Test future date
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setPublicationDate(LocalDate.now().plusDays(1));
        }, "Should reject future dates");

        // Test very old date
        LocalDate oldestAcceptableDate = LocalDate.of(1930, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setPublicationDate(oldestAcceptableDate.minusDays(1));
        }, "Should reject dates before 1930");

        // Test leap year date
        LocalDate leapDay = LocalDate.of(2024, 2, 29);
        comic.setPublicationDate(leapDay);
        assertEquals(leapDay, comic.getPublicationDate(), "Should accept leap day dates");
    }

    @Test
    void testSeriesTitleValidation() {
        // Test empty title
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setSeriesTitle("");
        }, "Should reject empty title");

        // Test title with only whitespace
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setSeriesTitle("   ");
        }, "Should reject whitespace-only title");

        // Test very long title
        String longTitle = "X".repeat(256);
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setSeriesTitle(longTitle);
        }, "Should reject titles longer than 255 characters");

        // Test title with special characters
        comic.setSeriesTitle("X-Men: Age of Apocalypse #1");
        assertEquals("X-Men: Age of Apocalypse #1", comic.getSeriesTitle(), 
            "Should accept titles with special characters");
    }

    @Test
    void testCreatorsListValidation() {
        // Test empty creator names
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setCreators(Arrays.asList("Stan Lee", "", "Jack Kirby"));
        }, "Should reject empty creator names");

        // Test null in creators list
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setCreators(Arrays.asList("Stan Lee", null));
        }, "Should reject null creator names");

        // Test duplicate creators
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setCreators(Arrays.asList("Stan Lee", "Stan Lee"));
        }, "Should reject duplicate creator names");

        // Test valid creators list with special characters
        List<String> creators = Arrays.asList("Stan Lee", "Jack Kirby Jr.", "Steve Ditko-Smith");
        comic.setCreators(creators);
        assertEquals(creators, comic.getCreators(), 
            "Should accept creator names with special characters");
    }

    @Test
    void testIssueNumberValidation() {
        // Test zero
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setIssueNumber(0);
        }, "Should reject zero issue number");

        // Test maximum value
        comic.setIssueNumber(9999);
        assertEquals(9999, comic.getIssueNumber(), "Should accept maximum issue number");

        // Test beyond maximum
        assertThrows(IllegalArgumentException.class, () -> {
            comic.setIssueNumber(10000);
        }, "Should reject issue numbers above 9999");
    }

    @Test
    void testValueComparison() {
        Comic comic1 = new Comic("Marvel", "X-Men", 1, 1, sampleDate);
        Comic comic2 = new Comic("Marvel", "X-Men", 1, 2, sampleDate);
        
        comic1.setValue(new BigDecimal("100.00"));
        comic2.setValue(new BigDecimal("200.00"));
        
        assertTrue(comic2.getValue().compareTo(comic1.getValue()) > 0,
            "Comic2 should be worth more than Comic1");

        comic1.setValue(new BigDecimal("200.00"));
        assertEquals(0, comic2.getValue().compareTo(comic1.getValue()),
            "Comics should have equal value");
    }

    @Test
    void testEquivalentComics() {
        Comic comic1 = new Comic("Marvel", "X-Men", 1, 1, sampleDate);
        Comic comic2 = new Comic("Marvel", "X-Men", 1, 1, sampleDate);
        
        assertEquals(comic1.getPublisher(), comic2.getPublisher());
        assertEquals(comic1.getSeriesTitle(), comic2.getSeriesTitle());
        assertEquals(comic1.getVolumeNumber(), comic2.getVolumeNumber());
        assertEquals(comic1.getIssueNumber(), comic2.getIssueNumber());
        assertEquals(comic1.getPublicationDate(), comic2.getPublicationDate());
        
        // If equals() is implemented
        assertEquals(comic1, comic2, "Identical comics should be equal");
    }
} 