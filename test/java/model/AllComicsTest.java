package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AllComicsTest {
    private AllComics allComics;

    @BeforeEach
    public void setUp() {
        List<Comic> comics = Arrays.asList(
            new Comic("Marvel", "The Amazing Spider-Man", 1, 1, LocalDate.of(1962, 3, 1)),
            new Comic("DC", "The Dark Knight", 1, 1, LocalDate.of(1986, 2, 1)),
            new Comic("Marvel", "The X-Men", 1, 1, LocalDate.of(1963, 9, 1))
        );
        allComics = new AllComics(comics);
    }

    @Test
    public void testSearchBySeriesTitle() {
        List<Comic> result = allComics.search("The Amazing Spider-Man");
        assertEquals(1, result.size());
        assertEquals("The Amazing Spider-Man", result.get(0).getSeriesTitle());
    }

    @Test
    public void testSearchByPublisher() {
        List<Comic> result = allComics.search("Marvel");
        assertEquals(2, result.size());
    }

    @Test
    public void testSearchByIssueNumber() {
        List<Comic> result = allComics.search("1");
        assertEquals(3, result.size());
    }

    @Test
    public void testSearchWithNoResults() {
        List<Comic> result = allComics.search("Nonexistent Comic");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSearchWithEmptyQuery() {
        List<Comic> result = allComics.search("");
        assertEquals(3, result.size()); 
    }

    @Test
    public void testSearchCaseInsensitivity() {
        List<Comic> result = allComics.search("the dark knight");
        assertEquals(1, result.size());
        assertEquals("The Dark Knight", result.get(0).getSeriesTitle());
    }

    @Test
    public void testEmptyComicList() {
        AllComics emptyComics = new AllComics(Collections.emptyList());
        List<Comic> result = emptyComics.search("Anything");
        assertTrue(result.isEmpty());
    }
} 