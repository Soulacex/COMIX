package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllComics {
    private final List<Comic> comics;

    public AllComics(List<Comic> comics) {
        this.comics = new ArrayList<>(comics);
    }

    public List<Comic> search(String query) {
        return comics.stream()
                .filter(comic -> comic.matches(query))
                .collect(Collectors.toList());
    }
}
