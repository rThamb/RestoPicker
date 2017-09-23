package assigment.dawson.restocoderenation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import assigment.dawson.restocoderenation.beans.Restaurant;
import assigment.dawson.restocoderenation.beans.User;

/**
 *DAO class containig SQLite CRUD method. Also responsible for creation of the DB and its population
 *@author CodeRenation
 * @since 2016/11/27
 */
public class RestoDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "RestoDBHelper";
    private static final String DB_NAME = " resto.db";
    private static final int DB_VERSION = 20;

    //Users Table components
    public static final String TABLE_USERS = "USERS";
    public static final String COLUMN_ID = "USER_ID";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PWD = "PASSWORD";
    public static final String COLUMN_FN = "FNAME";
    public static final String COLUMN_LN = "LNAME";
    public static final String COLUMN_POST = "POSTALCODE";
    public static final String COLUMN_UPDATED = "DATE";

    private static final String TABLE1_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PWD + " TEXT NOT NULL," +
                    COLUMN_FN + " TEXT NOT NULL," +
                    COLUMN_LN + " TEXT NOT NULL," +
                    COLUMN_POST + " TEXT NOT NULL," +
                    COLUMN_UPDATED + " TEXT NOT NULL DEFAULT current_timestamp" +
                    ");";

    //Restos table components
    public static final String TABLE2_RESTOS = "RESTOS";
    public static final String RESTOS_ID = "RESTO_ID";
    public static final String RESTOS_NAME = "NAME";
    public static final String RESTOS_NOTES = "NOTES";
    public static final String RESTOS_PRICERANGE = "PRICERANGE";
    public static final String RESTOS_GENRE = "GENRE";
    public static final String RESTOS_ADDRESS = "ADDRESS";
    public static final String RESTOS_CITY = "CITY";
    public static final String RESTOS_POST = "POSTALCODE";
    public static final String RESTOS_LONG = "LONGITUDE";
    public static final String RESTOS_LAT = "LATITUDE";
    public static final String RESTOS_CREATED = "DATE";
    public static final String RESTOS_TELE = "TELEPHONE";


    private static final String TABLE2_RESTOS_CREATE =
            "CREATE TABLE " + TABLE2_RESTOS + " (" +
                    RESTOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RESTOS_NAME + " TEXT NOT NULL, " +
                    RESTOS_NOTES + " TEXT NOT NULL," +
                    RESTOS_PRICERANGE + " INTEGER NOT NULL," +
                    RESTOS_GENRE + " TEXT NOT NULL," +
                    RESTOS_ADDRESS + " TEXT NOT NULL," +
                    RESTOS_CITY + " TEXT NOT NULL," +
                    RESTOS_POST + " TEXT NOT NULL, " +
                    RESTOS_LONG + " REAL NOT NULL," +
                    RESTOS_LAT + " REAL NOT NULL," +
                    RESTOS_CREATED + " TEXT NOT NULL," +
                    RESTOS_TELE + " TEXT NOT NULL" +
                    ");";

    //Favorite restos table components
    public static final String TABLE3_FAVS = "FAVS";
    public static final String FAVS_ID = "FAV_ID";
    public static final String FAVS_USER = "USER_ID";
    public static final String FAVS_RESTO = "RESTO_ID";

    private static final String TABLE3_FAV_CREATE =
            "CREATE TABLE " + TABLE3_FAVS + " (" +
                    FAVS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FAVS_USER + " INTEGER, " +
                    FAVS_RESTO + " INTEGER, " +
                    "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID), " +
                    "FOREIGN KEY (RESTO_ID) REFERENCES RESTOS(RESTO_ID) " +
                    ");";

    /**
     * Constructor
     * @param context
     */
    public RestoDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * This method will create the database
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(TABLE1_USERS_CREATE);
        sqLiteDatabase.execSQL(TABLE2_RESTOS_CREATE);
        sqLiteDatabase.execSQL(TABLE3_FAV_CREATE);
        //sqLiteDatabase.execSQL(TABLE4_REVIEWS_CREATE);
        Log.i(DEBUG_TAG, "Table has been created");

        populateDB(sqLiteDatabase);
        Log.i(DEBUG_TAG, "Inserted data to tables");

    }

    /**
     * The method responsible for populating database with restaurants test entries
     *@param db
     */
    private void populateDB(SQLiteDatabase db)
    {
        String columns = RESTOS_NAME + "," + RESTOS_NOTES + "," + RESTOS_PRICERANGE + ","
                + RESTOS_GENRE + "," + RESTOS_ADDRESS + "," + RESTOS_CITY + "," + RESTOS_POST
                + "," + RESTOS_LONG + "," + RESTOS_LAT + "," + RESTOS_CREATED + "," + RESTOS_TELE;

        String values = "\'McDonalds\',\'Has soo much food thats cheap and great\',1,\'Fast Food\',\'8754 Jarry Est\'"+
                ",\'Montreal\',\'H6F4T7\',45.03221,-77.2355,\'Feb 12, 2000\', \'514-986-9874\'";

        String values2 = "\'KFC\',\'YUMMY CHICKEN\',1,\'Fast Food\',\'8754 Jarry Est\'"+
                ",\'Montreal\',\'H6F4T7\',45.03221,-77.2355,\'Feb 12, 2000\', '514-186-9874'";

        String values3 = "\'MAMA FOOD\',\'JUST LIKE MAMA USED TO MAKE\',1,\'Fine Dining\',\'8754 Jarry Est\'"+
                ",\'Montreal\',\'H6F4T7\',45.03221,-77.2355,\'Feb 12, 2000\', '514-886-9874'";

        String values4 = "\'PAPA FOOD\',\'Hardy meals man\',1,\'BBQ\',\'8754 Jarry Est\'"+
                ",\'Montreal\',\'H6F4T7\',45.03221,-77.2355,\'Feb 12, 2000\', '514-686-9874'";

        String addResto1 = "INSERT INTO " + TABLE2_RESTOS + " (" + columns + ") VALUES (" + values + ");";
        String addResto2 = "INSERT INTO " + TABLE2_RESTOS + " (" + columns + ") VALUES (" + values2 + ");";
        String addResto3 = "INSERT INTO " + TABLE2_RESTOS + " (" + columns + ") VALUES (" + values3 + ");";
        String addResto4 = "INSERT INTO " + TABLE2_RESTOS + " (" + columns + ") VALUES (" + values4 + ");";


        String userCol = COLUMN_EMAIL + "," + COLUMN_PWD + "," + COLUMN_FN + "," + COLUMN_LN + "," + COLUMN_POST;
        String userValue = "\'G_speedy4@gmail.com\',\'test\',\'Tom\',\'Jim\',\'H4R3E4\'";
        String addUser = "INSERT INTO " + TABLE_USERS + " (" + userCol + ") VALUES (" + userValue + ");";


        String favCol = FAVS_USER +"," + FAVS_RESTO;
        String addFav = "INSERT INTO " + TABLE3_FAVS + " (" + favCol + ") VALUES (1,1);";
        String addFav2 = "INSERT INTO " + TABLE3_FAVS + " (" + favCol + ") VALUES (1,2);";
        String addFav3 = "INSERT INTO " + TABLE3_FAVS + " (" + favCol + ") VALUES (1,3);";

        db.execSQL(addResto1);
        db.execSQL(addResto2);
        db.execSQL(addResto3);
        db.execSQL(addResto4);
        db.execSQL(addUser);
        db.execSQL(addFav);
        db.execSQL(addFav2);
        db.execSQL(addFav3);

    }

    /**
     * Overriden onOpen method
     * @param sqLiteDatabase
     */
    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase)
    {
        super.onOpen(sqLiteDatabase);
        Log.i(DEBUG_TAG, "onOpen() executed");
    }

    /**
     * Overrriden onUpgrade method dpops old verions of db tables and creates new tables
     * @param sqLiteDatabase
     * @param oldVer
     * @param newVer
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE3_FAVS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE2_RESTOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        Log.i(DEBUG_TAG, "Dropped all tables");

        onCreate(sqLiteDatabase);
        Log.i(DEBUG_TAG, "Created all tables");

    }



    /**
     * THeis method creates new user entry in the table using parameters provided
     */
    public void addNewUser(String[] arguments)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAIL, arguments[0]);
        cv.put(COLUMN_PWD, arguments[1]);
        cv.put(COLUMN_FN, arguments[2]);
        cv.put(COLUMN_LN, arguments[3]);
        cv.put(COLUMN_POST, arguments[4]);
        cv.put(COLUMN_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        database.insert(TABLE_USERS, null, cv);
        database.close();
    }

    /**
     * Present method is used to modify details for existing user
     */
    public int modifyUser(String[] arguments) {

        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAIL, arguments[0]);
        cv.put(COLUMN_PWD, arguments[1]);
        cv.put(COLUMN_FN, arguments[2]);
        cv.put(COLUMN_LN, arguments[3]);
        cv.put(COLUMN_POST, arguments[4]);
        cv.put(COLUMN_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        String whereClause = COLUMN_ID+ "=?";
        String[] whereArgs = { "1" };

        int affectedRows = database.update(TABLE_USERS, cv, whereClause, whereArgs);
        return affectedRows;
    }

    /**
     * THe present method query users table basing on the email provided as a parameter
     * @param email
     * @return
     */
    public ArrayList<User> findUserByEmail(String email)
    {

        //cv.put(USER_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        ArrayList<User> users = new ArrayList<User>();
        SQLiteDatabase database = getReadableDatabase();

        String[] columns = {COLUMN_ID, COLUMN_EMAIL, COLUMN_PWD, COLUMN_FN, COLUMN_LN, COLUMN_POST, COLUMN_UPDATED};

        Cursor cur = database.query(TABLE_USERS, columns, COLUMN_EMAIL+"=?",
                new String[] {email}, null, null, null);

        while(cur.moveToNext())
        {
            int userId = cur.getInt(cur.getColumnIndex(COLUMN_ID));
            String emailObj = cur.getString(cur.getColumnIndex(COLUMN_EMAIL));
            String password = cur.getString(cur.getColumnIndex(COLUMN_PWD));
            String firstName = cur.getString(cur.getColumnIndex(COLUMN_FN));
            String lastName = cur.getString(cur.getColumnIndex(COLUMN_LN));
            String postalCode = cur.getString(cur.getColumnIndex(COLUMN_POST));
            String dateUpdated = cur.getString(cur.getColumnIndex(COLUMN_UPDATED));
            User user = new User(userId, emailObj, password, firstName, lastName, postalCode, dateUpdated);
            users.add(user);
        }
        database.close();
        return users;
    }

	
	
	
	public void updateRestoInfo(Restaurant rest)
    {
        ContentValues cv = new ContentValues();
        SQLiteDatabase database = getWritableDatabase();
        cv.put(RESTOS_NAME, rest.getName());
        cv.put(RESTOS_NOTES, rest.getNotes());
        cv.put(RESTOS_PRICERANGE, rest.getPriceRange());
        cv.put(RESTOS_GENRE, rest.getGenre());
        cv.put(RESTOS_ADDRESS, rest.getAddress());
        cv.put(RESTOS_CITY, rest.getCity());
        cv.put(RESTOS_POST, rest.getPostalCode());
        cv.put(RESTOS_LONG, rest.getLongitude());
        cv.put(RESTOS_LAT, rest.getLatitude());
        cv.put(RESTOS_CREATED, rest.getDateCreated());
        cv.put(RESTOS_TELE, rest.getTelephone());

        database.update(TABLE2_RESTOS, cv, RESTOS_ID+"="+rest.getResto_id(), null);
        database.close();
    }

    /**
     * Deletes entry from restos table
     * @param restoID primary key in the table
     */
    public void deleteResto(int restoID)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE2_RESTOS, RESTOS_ID + "= ?",  new String[]{restoID + ""});
        database.close();
    }




    /**
     *
     * The present method is adding new entry to the Restaurants table
     * @param rest
     */
    public int addRestaurant(Restaurant rest)
    {
        ContentValues cv = new ContentValues();
        SQLiteDatabase database = getWritableDatabase();
        cv.put(RESTOS_NAME, rest.getName());
        cv.put(RESTOS_NOTES, rest.getNotes());
        cv.put(RESTOS_PRICERANGE, rest.getPriceRange());
        cv.put(RESTOS_GENRE, rest.getGenre());
        cv.put(RESTOS_ADDRESS, rest.getAddress());
        cv.put(RESTOS_CITY, rest.getCity());
        cv.put(RESTOS_POST, rest.getPostalCode());
        cv.put(RESTOS_LONG, rest.getLongitude());
        cv.put(RESTOS_LAT, rest.getLatitude());
        cv.put(RESTOS_CREATED, rest.getDateCreated());
        cv.put(RESTOS_TELE, rest.getTelephone());

        int id = (int)database.insert(TABLE2_RESTOS, null, cv);
        database.close();
        return id;
    }


    /**
     * The method querying favourite resos table for particular user
     * @param userID primary key for the user
     * @return
     */
    public ArrayList<Restaurant> getFavouriteRestos(int userID)
    {
        ArrayList<Restaurant> restos = new ArrayList<Restaurant>();
        SQLiteDatabase database = getReadableDatabase();

        String[] columns = {FAVS_RESTO, FAVS_ID};

        Cursor cur = database.query(TABLE3_FAVS, columns, FAVS_USER+"=?",
                new String[]{userID+""}, null, null, null);

        while(cur.moveToNext())
        {
            int restoID = cur.getInt(cur.getColumnIndex(FAVS_RESTO));
            int favID = cur.getInt(cur.getColumnIndex(FAVS_ID));
            Restaurant rest = getRestoInfo(restoID, database);
            rest.setRestoFavID(favID);
            restos.add(rest);
        }
        database.close();
        return restos;

    }

    /**
     * Deletes entry from favourite restos table
     * @param FavID primary key in the table
     */
    public void deleteFav(int FavID)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE3_FAVS, FAVS_ID + "= ?",  new String[]{FavID + ""});
        database.close();
    }

    /**
     * Adds a resto to the Favourite Restos table
     * @param restoID
     * @param userID
     */
    public int addToFav(int restoID, int userID)
    {
        //check if the resto in fav
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FAVS_RESTO, restoID);
        cv.put(FAVS_USER, userID);

        int id = (int)database.insert(TABLE3_FAVS, null, cv);
        database.close();
        return id;
    }


    /**
     * Searches restaurants table using search parameters provided by the user in the form
     * @param searchStr
     * @return
     */
    public ArrayList<Restaurant> findRestosByName(String[] searchStr)
    {
        ArrayList<Restaurant> restos = new ArrayList<Restaurant>();
        SQLiteDatabase database = getReadableDatabase();

        String[] columns = {RESTOS_ID};
        //String sValue = "%"+name+"%";
        Log.i("DBHelper", searchStr[0] + "  " + searchStr[1] + "   " + searchStr[2]);
        Cursor cur = database.query(TABLE2_RESTOS, columns, RESTOS_NAME + " like ? AND " + RESTOS_GENRE + " like ? AND "
                        + RESTOS_CITY + " like ? ",
                searchStr, null, null, null);

        while(cur.moveToNext())
        {
            int restoID = cur.getInt(cur.getColumnIndex(RESTOS_ID));
            restos.add(getRestoInfo(restoID, database));

        }
        database.close();
        return restos;
    }


    /**
     * Retrives details for a particular restaurant basing on primary key
     * @param restoID primary key
     * @param database database
     * @return
     */
    private Restaurant getRestoInfo(int restoID, SQLiteDatabase database)
    {

        String[] columns = {RESTOS_NAME, RESTOS_NOTES, RESTOS_PRICERANGE, RESTOS_GENRE, RESTOS_ADDRESS, RESTOS_CITY,
                RESTOS_POST, RESTOS_LONG, RESTOS_LAT, RESTOS_CREATED, RESTOS_TELE};

        String where = RESTOS_ID+"=?";
        String[] whereArg = {String.valueOf(restoID)};

        Cursor cur = database.query(TABLE2_RESTOS, columns, where, whereArg, null, null,null);

        Restaurant resto = new Restaurant();

        if(cur.moveToNext())
        {
            String name = cur.getString(cur.getColumnIndex(RESTOS_NAME));
            String note = cur.getString(cur.getColumnIndex(RESTOS_NOTES));
            int priceRange = cur.getInt(cur.getColumnIndex(RESTOS_PRICERANGE));
            String genre = cur.getString(cur.getColumnIndex(RESTOS_GENRE));
            String address = cur.getString(cur.getColumnIndex(RESTOS_ADDRESS));
            String city = cur.getString(cur.getColumnIndex(RESTOS_CITY));
            String postalCode = cur.getString(cur.getColumnIndex(RESTOS_POST));
            double longitude = cur.getDouble(cur.getColumnIndex(RESTOS_LONG));
            double latitude = cur.getDouble(cur.getColumnIndex(RESTOS_LAT));
            String date = cur.getString(cur.getColumnIndex(RESTOS_CREATED));
            String tele = cur.getString(cur.getColumnIndex(RESTOS_TELE));

            resto.setResto_id(restoID);
            resto.setName(name);
            resto.setNotes(note);
            resto.setPriceRange(priceRange);
            resto.setGenre(genre);
            resto.setAddress(address);
            resto.setCity(city);
            resto.setPostalCode(postalCode);
            resto.setLongitude(longitude);
            resto.setLatitude(latitude);
            resto.setDateCreated(date);
            resto.setTelephone(tele);
        }


        return resto;
    }


}
