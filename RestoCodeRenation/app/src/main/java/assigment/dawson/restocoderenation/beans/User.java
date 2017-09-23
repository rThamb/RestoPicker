package assigment.dawson.restocoderenation.beans;
import java.io.Serializable;

/**
 * This class is a bean for a User object, contains constructors, getters and setters
 * @author CodeRenation
 * @since 2016-12-03.
 */

public class User implements Serializable {

    private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String postalCode;
    private String dateUpdated;

    /**
     * Default constructor
     */
    public User() {
        this.userId = 0;
        this.email = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.postalCode= "";
        this.dateUpdated = "";
    }

    /**
     * Constructor with parameters
     * @param userId
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param postalCode
     * @param dateUpdated
     */
    public User(int userId, String email, String password, String firstName, String lastName, String postalCode, String dateUpdated)
    {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode= postalCode;
        this.dateUpdated = dateUpdated;
    }

    //getters and setters are listed below

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
