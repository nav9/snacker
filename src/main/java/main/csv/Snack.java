package main.csv;

import java.io.Serializable;

public class Snack implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Integer rating;
    private Integer recent;
    
    public Snack(final String name, final Integer rating, final Integer recent) {
        this.name = name;
        this.rating = rating;
        this.recent = recent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRecent() {
        return recent;
    }

    public void setRecent(Integer recent) {
        this.recent = recent;
    }
    
}
