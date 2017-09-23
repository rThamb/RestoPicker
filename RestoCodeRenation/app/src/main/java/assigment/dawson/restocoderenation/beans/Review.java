package assigment.dawson.restocoderenation.beans;

/**
 * Bean for review object
 * @author pishchalov 1430169
 * @since 12/7/2016.
 */

public class Review {

    private String user;
    private String title;
    private String comment;
    private int rating;


    public Review(){}

    public Review(String user, String title, String comment, int rating) {
        this.user = user;
        this.title = title;
        this.comment = comment;
        this.rating = rating;
    }

    //getters and setters for fields
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setDate(int rating) {
        this.rating = rating;
    }

//toString takes only author id and comment text
    public String toString()
    {
        return user + " " + comment;
    }

}
