package assigment.dawson.restocoderenation.beans;

import java.io.Serializable;

/**
 * This class is a bean for a Restaurant object, contains constructors, getters and setters
 * @author CodeRenation
 * @since 2016/11/23
 */

public class Restaurant implements Serializable {


    private int resto_id;
    private String name;
    private String address;
    private String postalCode;
    private String genre;

    private String city;

    private String dateCreated;

    private int priceRange;

    private int rating;

    private double longitude;
    private double latitude;

    private String telephone;
    private String notes;

    //to hold user fav Id of the resto
    private int restoFavID;

    //vars for heroku
    private int herokuID;


    public Restaurant()
    {
        super();
    }

    public Restaurant(Restaurant rest)
    {
        this.resto_id = rest.resto_id;
        this.name = rest.name;
        this.address = rest.address;
        this.postalCode = rest.postalCode;
        this.genre = rest.genre;

        this.city = rest.city;

        this.dateCreated = rest.dateCreated;

        this.priceRange = rest.priceRange;

        this.rating = rest.rating;

        this.longitude = rest.longitude;
        this.latitude = rest.latitude;

        this.telephone = rest.telephone;
        this.notes = rest.notes;
        this.restoFavID = rest.restoFavID;
    }

    /*
        Getters and setters below
     */

    public int getHerokuID() {
        return herokuID;
    }

    public void setHerokuID(int herokuID) {
        this.herokuID = herokuID;
    }

    public int getRestoFavID() {
        return restoFavID;
    }

    public void setRestoFavID(int restoFavID) {
        this.restoFavID = restoFavID;
    }

    public int getResto_id() {
        return resto_id;
    }

    public void setResto_id(int resto_id) {
        this.resto_id = resto_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String time) {
        this.dateCreated = time;
    }

    public int getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(int priceRange) {
        this.priceRange = priceRange;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * To string method
     * @return String form of object
     */
    public String toString()
    {
        return resto_id + "\n" + name + "\n" + address + "\n" + city + "\n" + postalCode + "\n" +
                genre + "\n" + dateCreated + "\n" + priceRange + "\n" + longitude + "\n" +
                latitude + "\n" + notes + "\n" + rating + "\n" + telephone + "\n" + herokuID;
    }

}

