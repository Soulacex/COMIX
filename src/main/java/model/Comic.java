package model;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.ArrayList;

public class Comic {
    private String publisher;
    private String seriesTitle;
    private int volumeNumber;
    private int issueNumber;
    private LocalDate publicationDate;
    private List<String> creators = new ArrayList<>();
    private List<String> principleCharacters;  // Optional.
    private String description;           // Optional.  
    private BigDecimal value;            // Optional.

    // Constructor.
    public Comic(String publisher, String seriesTitle, int volumeNumber, int issueNumber, LocalDate publicationDate) {
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
    }

    // Getters and Setters.
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public void setSeriesTitle(String seriesTitle) {
        if (seriesTitle == null || seriesTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Series title cannot be empty");
        }
        if (seriesTitle.length() > 255) { 
            throw new IllegalArgumentException("Series title cannot exceed 255 characters.");
        }

        this.seriesTitle = seriesTitle;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(int volumeNumber) {
        if (volumeNumber < 0) {
            throw new IllegalArgumentException("Volume number cannot be negative.");
        }
        this.volumeNumber = volumeNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        if (issueNumber <= 0) {
            throw new IllegalArgumentException("Issue number must be positive.");
        }
        if (issueNumber > 9999) {  
            throw new IllegalArgumentException("Issue number cannot exceed 9999.");
        }
        this.issueNumber = issueNumber;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        if (publicationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publication date cannot be in the future.");
        }
        if (publicationDate.isBefore(LocalDate.of(1930, 1, 1))) {
            throw new IllegalArgumentException("Publication date cannot be before 1930.");
        }
        this.publicationDate = publicationDate;
    }

    public List<String> getCreators() {
        return creators;
    }

    public void setCreators(List<String> creators) {
        if (creators.stream().anyMatch(name -> name == null || name.trim().isEmpty())) {
            throw new IllegalArgumentException("Creator names cannot be null or empty");
        }
        if (creators.size() != creators.stream().distinct().count()) {
            throw new IllegalArgumentException("Duplicate creator names are not allowed");
        }
        this.creators = new ArrayList<>(creators);
    }

    public List<String> getPrincipleCharacters() {
        return principleCharacters;
    }

    public void setPrincipleCharacters(List<String> principleCharacters) {
        this.principleCharacters = principleCharacters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        if (value.scale() > 2) {
            throw new IllegalArgumentException("Value cannot have more than 2 decimal places");
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Comic{" +
                "publisher='" + publisher + '\'' +
                ", seriesTitle='" + seriesTitle + '\'' +
                ", volumeNumber=" + volumeNumber +
                ", issueNumber=" + issueNumber +
                ", publicationDate=" + publicationDate +
                ", creators=" + creators +
                ", principleCharacters=" + principleCharacters +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Comic comic = (Comic) o;
        return volumeNumber == comic.volumeNumber &&
               issueNumber == comic.issueNumber &&
               Objects.equals(publisher, comic.publisher) &&
               Objects.equals(seriesTitle, comic.seriesTitle) &&
               Objects.equals(publicationDate, comic.publicationDate);
    }

    public boolean matches(String query) {
        if (query == null) {
            return false; // Handle null query
        }
        if (query.trim().isEmpty()) {
            return true; // Match all comics for empty query
        }
        String lowerCaseQuery = query.toLowerCase(); // Convert query to lower case
        return seriesTitle.toLowerCase().contains(lowerCaseQuery) ||
               publisher.toLowerCase().contains(lowerCaseQuery) ||
               String.valueOf(issueNumber).contains(query) || // Issue number comparison remains case-sensitive
               creators.stream().anyMatch(creator -> creator.toLowerCase().contains(lowerCaseQuery)) ||
               publicationDate.toString().contains(query); // Publication date comparison remains case-sensitive
    }
}