package com.amyhuyen.energizer;

import com.amyhuyen.energizer.models.Nonprofit;
import com.amyhuyen.energizer.models.User;
import com.amyhuyen.energizer.models.Volunteer;

public class UserDataProvider {
    private Volunteer mVolunteer;
    private Nonprofit mNonprofit;
    private String mUserType;

    private static UserDataProvider sInstance;

    private UserDataProvider() {

    }

    public static UserDataProvider getInstance() {
        if (sInstance == null) {
            sInstance = new UserDataProvider();
        }
        return sInstance;
    }

    public User getCurrentUser(){
        User user;
        if (mUserType.equals(DBKeys.KEY_VOLUNTEER)){
            user = this.getCurrentVolunteer();
        } else {
            user = this.getCurrentNPO();
        }
        return user;
    }

    public void setCurrentVolunteer(Volunteer volunteer) {
        mVolunteer = volunteer;
    }

    public Volunteer getCurrentVolunteer() {
        return mVolunteer;
    }

    public void setCurrentNPO(Nonprofit nonprofit) {
        mNonprofit = nonprofit;
    }

    public Nonprofit getCurrentNPO() {
        return mNonprofit;
    }

    public void setCurrentUserType(String UserType) {
        mUserType = UserType;
    }

    public String getCurrentUserType() {
        return mUserType;
    }

    public String getCurrentUserName(){
        String UserName = null;
        if (mUserType.equals(DBKeys.KEY_VOLUNTEER)){
            UserName = mVolunteer.getName();
        } else {
            UserName = mNonprofit.getName();
        }
        return UserName;
    }

    public String getCurrentUserEmail(){
        String email = null;
        if (mUserType.equals(DBKeys.KEY_VOLUNTEER)){
            email = mVolunteer.getEmail();
        } else {
            email = mNonprofit.getEmail();
        }
        return email;
    }

    public String getCurrentUserId(){
        String userId = null;
        if (mUserType.equals(DBKeys.KEY_VOLUNTEER)){
            userId = mVolunteer.getUserID();
        } else {
            userId = mNonprofit.getUserID();
        }
        return userId;
    }

    public String getCurrentUserAddress(){
        String userAddress = null;
        if (mUserType.equals("Volunteer")){
            userAddress = mVolunteer.getAddress();
        } else {
            userAddress = mNonprofit.getAddress();
        }
        return userAddress;
    }

    public String getCurrentUserPhone() {
        String userPhone = null;
        if (mUserType.equals("Volunteer")) {
            userPhone = mVolunteer.getPhone();
        } else {
            userPhone = mNonprofit.getPhone();
        }
        return userPhone;
    }


    public String getCurrentUserDescription() {
        String userDescription = null;
        if (mUserType.equals("NPO")) {
            userDescription = mNonprofit.getDescription();
        }
        return userDescription;
    }

    public String getCurrentUserLatLong() {
        String userLatLong= null;
        if (mUserType.equals("Volunteer")) {
            userLatLong = mVolunteer.getLatLong();
        } else {
            userLatLong = mNonprofit.getLatLong();
        }
        return userLatLong;
    }

}
