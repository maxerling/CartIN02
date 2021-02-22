package Objects.Feedback;

/**
 * Created by Max Erling
 * Date: 2021-02-19
 * Copyright: MIT
 * Class: Java20B
 */
public class Grade {
    private int id;
    private int rating;
    private String ratingText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    @Override
    public String toString() {
        return "Grade{" +
                + id +
                "," + rating +
                "," + ratingText  +
                '}';
    }
}
