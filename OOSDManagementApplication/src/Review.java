public class Review {
    private String date, comment;
    private int rating;

    public Review(String date, String comment, int rating) {
        this.date = date;
        this.comment = comment;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Comment: " + comment + ", Rating: " + rating;
    }
}
