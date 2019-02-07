package zw.co.appsareus.nannymeets.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.interfaces.OnSignInStatusListener;
import zw.co.appsareus.nannymeets.models.Country;
import zw.co.appsareus.nannymeets.models.Employer;
import zw.co.appsareus.nannymeets.models.Employee;
import zw.co.appsareus.nannymeets.models.ProfilePicture;
import zw.co.appsareus.nannymeets.models.User;


/**
 * Created by a_man on 5/5/2017.
 */

public class Helper {


    private SharedPreferenceHelper sharedPreferenceHelper;
    private String UUID = "uuid";
    private static final String ACCOUNT_TYPE = "account_type";
    private static final String EMPLOYEE = "Employee";
    private static final String EMPLOYER = "Employer";
    private static final String SEND_OTP = "SEND_OTP";

    private OnSignInStatusListener onSignInStatusListener;

    public Helper setOnSignInStatusListener(OnSignInStatusListener onSignInStatusListener) {
        this.onSignInStatusListener = onSignInStatusListener;
        return this;
    }

    public Helper(Context context) {
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
    }

    public void setPhoneInstanceID() {
        if (getPhoneInstanceID() == null) {
            String uniqueID = java.util.UUID.randomUUID().toString();
            sharedPreferenceHelper.setStringPreference(UUID, uniqueID);
        }
    }

    public String getPhoneInstanceID() {
        return sharedPreferenceHelper.getStringPreference(UUID);
    }

    public void setPhoneNumberForVerification(String phone) {
        sharedPreferenceHelper.setStringPreference(SEND_OTP, phone);
    }

    public String getPhoneNumberForVerification() {
        return sharedPreferenceHelper.getStringPreference(SEND_OTP);
    }

    public void clearPhoneNumberForVerification() {
        sharedPreferenceHelper.clearPreference(SEND_OTP);
    }
    public void setAccountType(String acc) {
        sharedPreferenceHelper.setStringPreference(ACCOUNT_TYPE, acc);
    }

    public String getAccountType() {
        return sharedPreferenceHelper.getStringPreference(ACCOUNT_TYPE);
    }

    public String getUserId(){
        return getAccountType().equals(EMPLOYEE) ? getEmployeeProfile().getId() : getEmployerProfile().getId();
    }

    public void loggedIn() {
        setSignedInStatus(true);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        setSignedInStatus(false);
        sharedPreferenceHelper.clear();
    }

    public void checkSignInStatus() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null || !getSignedInStatus()) {
            onSignInStatusListener.onSignedOut();
        } else {
            onSignInStatusListener.onSignedIn();
        }
    }

    public void setSignedInStatus(Boolean status) {
        sharedPreferenceHelper.setBooleanPreference("signed_in", status);
    }

    public boolean getSignedInStatus() {
        return sharedPreferenceHelper.getBooleanPreference("signed_in", false);
    }

    public void setEmployeeProfile(User user) {
        Employee mEmployee = (Employee) user;
        sharedPreferenceHelper.setStringPreference("church", mEmployee.getChurch());
        sharedPreferenceHelper.setStringPreference("drivers_licence", mEmployee.getDriversLicence());
        sharedPreferenceHelper.setStringPreference("experience", mEmployee.getExperience());
        sharedPreferenceHelper.setStringPreference("passport", mEmployee.getPassport());
        sharedPreferenceHelper.setStringPreference("qualifications", mEmployee.getQualifications());
        sharedPreferenceHelper.setStringPreference("religion", mEmployee.getReligion());
        sharedPreferenceHelper.setStringPreference("type_of_employment", mEmployee.getOccupation());
        sharedPreferenceHelper.setStringPreference(ACCOUNT_TYPE, mEmployee.getAccountType());
        sharedPreferenceHelper.setStringPreference("address", mEmployee.getAddress());
        sharedPreferenceHelper.setStringPreference("dob", mEmployee.getDob());
        sharedPreferenceHelper.setStringPreference("email", mEmployee.getEmail());
        sharedPreferenceHelper.setStringPreference("gender", mEmployee.getGender());
        sharedPreferenceHelper.setStringPreference("name", mEmployee.getName());
        sharedPreferenceHelper.setStringPreference("nat_id", mEmployee.getNatID());
        sharedPreferenceHelper.setStringPreference("phone", mEmployee.getPhone());
        sharedPreferenceHelper.setStringPreference("surname", mEmployee.getSurname());
        sharedPreferenceHelper.setStringPreference("username", mEmployee.getUsername());
        sharedPreferenceHelper.setStringPreference("id", mEmployee.getId());
        sharedPreferenceHelper.setFloatPreference("salary", mEmployee.getExpectedSalary());
        sharedPreferenceHelper.setStringPreference("job_status", mEmployee.getJobStatus());
        sharedPreferenceHelper.setStringPreference("city", mEmployee.getCity());
        sharedPreferenceHelper.setStringPreference("country", mEmployee.getCountry());
        sharedPreferenceHelper.setStringPreference("marital_status", mEmployee.getMaritalStatus());
        sharedPreferenceHelper.setStringPreference("languages", mEmployee.getLanguages());
        sharedPreferenceHelper.setStringPreference("profession", mEmployee.getProfession());
        sharedPreferenceHelper.setStringPreference("pic_id", mEmployee.getProfilePic().getId());
        sharedPreferenceHelper.setStringPreference("pic_name", mEmployee.getProfilePic().getName());
        sharedPreferenceHelper.setStringPreference("pic_bucket", mEmployee.getProfilePic().getBucketReference());
        sharedPreferenceHelper.setStringPreference("pic_path", mEmployee.getProfilePic().getPath());
    }

    public Employee getEmployeeProfile() {
        Employee mEmployee = new Employee(
                sharedPreferenceHelper.getStringPreference("name"),
                sharedPreferenceHelper.getStringPreference("surname"),
                sharedPreferenceHelper.getStringPreference("dob"),
                sharedPreferenceHelper.getStringPreference("gender"),
                sharedPreferenceHelper.getStringPreference("id"),
                sharedPreferenceHelper.getStringPreference("email"),
                sharedPreferenceHelper.getStringPreference("phone"),
                sharedPreferenceHelper.getStringPreference("address"),
                sharedPreferenceHelper.getStringPreference("nat_id"),
                "",
                sharedPreferenceHelper.getStringPreference(ACCOUNT_TYPE),
                sharedPreferenceHelper.getStringPreference("username"),
                sharedPreferenceHelper.getStringPreference("city"),
                sharedPreferenceHelper.getStringPreference("country"),
                sharedPreferenceHelper.getStringPreference("experience"),
                sharedPreferenceHelper.getStringPreference("qualifications"),
                sharedPreferenceHelper.getStringPreference("passport"),
                sharedPreferenceHelper.getStringPreference("drivers_licence"),
                sharedPreferenceHelper.getStringPreference("religion"),
                sharedPreferenceHelper.getStringPreference("church"),
                sharedPreferenceHelper.getFloatPreference("salary", 0f),
                sharedPreferenceHelper.getStringPreference("type_of_employment"),
                sharedPreferenceHelper.getStringPreference("job_status"),
                sharedPreferenceHelper.getStringPreference("marital_status"),
                sharedPreferenceHelper.getStringPreference("languages"),
                sharedPreferenceHelper.getStringPreference("profession")
        );

        mEmployee.setProfilePic(new ProfilePicture(
                //id,name,path,bucket
                sharedPreferenceHelper.getStringPreference("pic_id"),
                sharedPreferenceHelper.getStringPreference("pic_name"),
                sharedPreferenceHelper.getStringPreference("pic_path"),
                sharedPreferenceHelper.getStringPreference("pic_bucket")
        ));
        return mEmployee;
    }

    public User getProfile(){
        String acctype = sharedPreferenceHelper.getStringPreference(ACCOUNT_TYPE);

        if (acctype.equals(EMPLOYEE)){
            return getEmployeeProfile();
        }else if (acctype.equals(EMPLOYER)){
            return getEmployerProfile();
        }else {
            return null;
        }
    }

    public void setProfile(User user){
        String acctype = user.getAccountType();

        if (acctype.equals(EMPLOYEE)){
            setEmployeeProfile(user);
        }else if (acctype.equals(EMPLOYER)){
            setEmployerProfile(user);
        }
    }

    public void setEmployerProfile(User user) {
        Employer mEmployer = (Employer) user;
        sharedPreferenceHelper.setStringPreference(ACCOUNT_TYPE, mEmployer.getAccountType());
        sharedPreferenceHelper.setStringPreference("address", mEmployer.getAddress());
        sharedPreferenceHelper.setStringPreference("dob", mEmployer.getDob());
        sharedPreferenceHelper.setStringPreference("email", mEmployer.getEmail());
        sharedPreferenceHelper.setStringPreference("gender", mEmployer.getGender());
        sharedPreferenceHelper.setStringPreference("name", mEmployer.getName());
        sharedPreferenceHelper.setStringPreference("nat_id", mEmployer.getNatID());
        sharedPreferenceHelper.setStringPreference("phone", mEmployer.getPhone());
        sharedPreferenceHelper.setStringPreference("surname", mEmployer.getSurname());
        sharedPreferenceHelper.setStringPreference("username", mEmployer.getUsername());
        sharedPreferenceHelper.setStringPreference("id", mEmployer.getId());
        sharedPreferenceHelper.setStringPreference("pic_id", mEmployer.getProfilePic().getId());
        sharedPreferenceHelper.setStringPreference("pic_name", mEmployer.getProfilePic().getName());
        sharedPreferenceHelper.setStringPreference("pic_bucket", mEmployer.getProfilePic().getBucketReference());
        sharedPreferenceHelper.setStringPreference("pic_path", mEmployer.getProfilePic().getPath());
        sharedPreferenceHelper.setStringPreference("city", mEmployer.getCity());
        sharedPreferenceHelper.setStringPreference("country", mEmployer.getCountry());
        sharedPreferenceHelper.setIntegerPreference("credits", mEmployer.getCredits());
    }

    public Employer getEmployerProfile() {
        Employer mEmployer = new Employer(
                sharedPreferenceHelper.getStringPreference("name"),
                sharedPreferenceHelper.getStringPreference("surname"),
                sharedPreferenceHelper.getStringPreference("dob"),
                sharedPreferenceHelper.getStringPreference("gender"),
                sharedPreferenceHelper.getStringPreference("id"),
                sharedPreferenceHelper.getStringPreference("email"),
                sharedPreferenceHelper.getStringPreference("phone"),
                sharedPreferenceHelper.getStringPreference("address"),
                sharedPreferenceHelper.getStringPreference("nat_id"),
                "",
                sharedPreferenceHelper.getStringPreference(ACCOUNT_TYPE),
                sharedPreferenceHelper.getStringPreference("username"),
                sharedPreferenceHelper.getStringPreference("city"),
                sharedPreferenceHelper.getStringPreference("country"),
                sharedPreferenceHelper.getIntegerPreference("credits",5)
        );
        mEmployer.setProfilePic(new ProfilePicture(
                //id,name,path,bucket
                sharedPreferenceHelper.getStringPreference("pic_id"),
                sharedPreferenceHelper.getStringPreference("pic_name"),
                sharedPreferenceHelper.getStringPreference("pic_path"),
                sharedPreferenceHelper.getStringPreference("pic_bucket")
        ));
        return mEmployer;
    }
//    public void saveFilters(SearchFilter mSearchFilter){
//        sharedPreferenceHelper.setIntegerPreference("max_rent",mSearchFilter.getMaxRent());
//        sharedPreferenceHelper.setIntegerPreference("min_rent",mSearchFilter.getMinRent());
//        sharedPreferenceHelper.setIntegerPreference("number_of_rooms",mSearchFilter.getNumberOfRooms());
//        sharedPreferenceHelper.setStringPreference("location",mSearchFilter.getLocation());
//        sharedPreferenceHelper.setBooleanPreference("use_filter",mSearchFilter.getUseFilter());
//    }
//
//    public SearchFilter getFilters(){
//        SearchFilter mFilter = new SearchFilter(
//                sharedPreferenceHelper.getStringPreference("location"),
//                sharedPreferenceHelper.getIntegerPreference("min_rent",0),
//                sharedPreferenceHelper.getIntegerPreference("max_rent",1000),
//                sharedPreferenceHelper.getIntegerPreference("number_of_rooms",0),
//                sharedPreferenceHelper.getBooleanPreference("use_filter",false)
//        );
//
//        return mFilter;
//    }

    public static void openPlayStore(Context context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void openSupportMail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "support@appsareus.co.zw", null));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static void closeKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String timeFormater(float time) {
        Long secs = (long) (time / 1000);
        Long mins = (long) ((time / 1000) / 60);
        Long hrs = (long) (((time / 1000) / 60) / 60); /* Convert the seconds to String * and format to ensure it has * a leading zero when required */
        secs = secs % 60;
        String seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        } /* Convert the minutes to String and format the String */
        mins = mins % 60;
        String minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        } /* Convert the hours to String and format the String */
        String hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "00";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }

        return hours + ":" + minutes + ":" + seconds;
//        String milliseconds = String.valueOf((long) time);
//        if (milliseconds.length() == 2) {
//            milliseconds = "0" + milliseconds;
//        }
//        if (milliseconds.length() <= 1) {
//            milliseconds = "00";
//        }
//        milliseconds = milliseconds.substring(milliseconds.length() - 3, milliseconds.length() - 2); /* Setting the timer text to the elapsed time */
    }

    public ArrayList<Country> getCountries(Context context) {
        ArrayList<Country> toReturn = new ArrayList<>();
//        toReturn.add(new Country("RU", "Russia", "+7"));
//        toReturn.add(new Country("TJ", "Tajikistan", "+992"));
//        toReturn.add(new Country("ZW", "Zimbabwe", "+263"));
//        return toReturn;

        try {
            JSONArray countrArray = new JSONArray(readEncodedJsonString(context));
            toReturn = new ArrayList<>();
            for (int i = 0; i < countrArray.length(); i++) {
                JSONObject jsonObject = countrArray.getJSONObject(i);
                String countryName = jsonObject.getString("name");
                String countryDialCode = jsonObject.getString("dial_code");
                String countryCode = jsonObject.getString("code");
                Country country = new Country(countryCode, countryName, countryDialCode);
                toReturn.add(country);
            }
            Collections.sort(toReturn, new Comparator<Country>() {
                @Override
                public int compare(Country lhs, Country rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private String readEncodedJsonString(Context context) throws java.io.IOException {
        String base64 = context.getResources().getString(R.string.countries_code);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    public void setPreferredEmployees(ArrayList<String> recordIds){
        sharedPreferenceHelper.setStringPreference("recordIds",arrayToString(recordIds));
    }

    private String arrayToString(ArrayList<String> array){
        StringBuilder output = new StringBuilder();
        for (String a : array) {
            output.append(a);
        }
        return output.toString();
    }

    public ArrayList<String> getPreferredEmployees(){
        return stringToArray(sharedPreferenceHelper.getStringPreference("recordsIds"));
    }

    private ArrayList<String> stringToArray(String mString){
        return new ArrayList<>(Arrays.asList(mString.split(",")));
    }
}
