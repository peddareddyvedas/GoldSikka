package com.goldsikka.goldsikka.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class AccountUtils {

    private static final String isPin = "pin";

    private static final String AccessToken = "accesstoken";
    private static final String LivePrice = "Liveprice";
    private static final String SellPrice = "sellliveprice";
    private static final String NomineeID = "id";
    private static final String Schemename = "Schemename";
    private static final String SchemeTitle = "SchemeTitle";
    private static final String Password = "password";
    private static final String SchemeID = "SchemeID";
    private static final String Purity = "Purity";
    private static final String Carat = "Carat";
    private static final String CartItemCount = "ItemCount";
    private static final String MainCategoryId = "MainCategoryId";
    private static final String SubCategoryId = "SubCategoryId";
    private static final String ProductID = "ProductID";
    private static final String CustomerID = "CustomerID";

    private static final String OrgAvathar = "OrgAvathar";
    private static final String OrgProfileImg = "OrgProfileImg";
    private static final String WalletAmount = "WalletAmount";

    private static final String ProductQuantity = "ProductQuantity";

    private static final String Email = "Email";
    private static final String Name = "Name";
    private static final String Mobile = "Mobile";
    private static final String Loadify = "load";

    private static final String Avathar = "Avathar";
    private static final String ProfileImg = "ProfileImg";

    private static final String SchemeNickname = "SchemeNickname";
    private static final String GSTTAX = "GSTTAX";
    private static final String ROLEID = "ROLEID";

    private static final String ShowCaseInfo = "ShowCaseInfo";
    private static final String isGrams = "Grams";
    private static final String isprice = "Price";

    private static final String isGiftCard = "GictCard";


    private static SharedPreferences getSharedPreferences(final Context context) {
        return context.getSharedPreferences("Goldsikka", Context.MODE_PRIVATE);
    }


    public static void setIsPin(Context context, Boolean value) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putBoolean(isPin, value).apply();
    }

    public static Boolean getIsPin(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getBoolean(isPin, true);
    }

    public static void setAccessToken(Context context, String Token) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(AccessToken, Token).apply();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(AccessToken, "");
    }

    public static void setLivePrice(Context context, String livePrice) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(LivePrice, livePrice).apply();
    }

    public static String getLivePrice(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(LivePrice, "");
    }

    public static void setSellPrice(Context context, String selllivePrice) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(SellPrice, selllivePrice).apply();
    }

    public static String getSellPrice(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(SellPrice, "");
    }


    public static void setNomineeID(Context context, String id) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(NomineeID, id).apply();
    }

    public static String getNomineeID(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(NomineeID, "");
    }

    public static void setSchemeID(Context context, String id) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(SchemeID, id).apply();
    }

    public static String getSchemeID(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(SchemeID, "");
    }

    public static void setSchemename(Context context, String name) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Schemename, name).apply();
    }

    public static String getSchemename(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Schemename, "");
    }

    public static void setSchemeTitle(Context context, String sctile) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(SchemeTitle, sctile).apply();
    }

    public static String getSchemeTitle(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(SchemeTitle, "");
    }

    public static void setPassword(Context context, String password) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Password, password).apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Password, "");
    }

    public static void setPurity(Context context, String purity) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Purity, purity).apply();
    }

    public static String getPurity(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Purity, "");
    }

    public static void setCarat(Context context, String carat) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Carat, carat).apply();
    }

    public static String getCarat(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Carat, "");
    }

    public static void setCartItemCount(Context context, String ItemCount) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(CartItemCount, ItemCount).apply();
    }

    public static String getCartItemCount(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(CartItemCount, "");
    }

    public static void setMainCategoryId(Context context, String CategoryId) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(MainCategoryId, CategoryId).apply();
    }

    public static String getMainCategoryId(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(MainCategoryId, "");
    }

    public static void setSubCategoryId(Context context, String SubCayId) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(SubCategoryId, SubCayId).apply();
    }

    public static String getSubCategoryId(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(SubCategoryId, "");
    }

    public static void setProductID(Context context, String productID) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(ProductID, productID).apply();
    }

    public static String getProductID(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(ProductID, "");
    }


    public static void setProductQuantity(Context context, String quanty) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(ProductQuantity, quanty).apply();
    }

    public static String getProductQuantity(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(ProductQuantity, "");
    }


    public static void setWalletAmount(Context context, String WalletAmounts) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(WalletAmount, WalletAmounts).apply();
    }

    public static String getWalletAmount(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(WalletAmount, "");
    }


    public static void setEmail(Context context, String email) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Email, email).apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Email, "");
    }

    public static void setName(Context context, String name) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Name, name).apply();
    }

    public static String getName(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Name, "");
    }

    public static void setMobile(Context context, String mobile) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Mobile, mobile).apply();
    }

    public static String getMobile(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Mobile, "");
    }

    public static void setLoadify(Context context, String load) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Loadify, load).apply();
    }

    public static String getLoadify(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Loadify, "");
    }

    public static String getAvathar(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(Avathar, "");
    }

    public static void setAvathar(Context context, String avathar) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(Avathar, avathar).apply();
    }

    public static String getProfileImg(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(ProfileImg, "");
    }

    public static void setProfileImg(Context context, String profileImg) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(ProfileImg, profileImg).apply();
    }

    public static String getSchemeNickname(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(SchemeNickname, "");
    }

    public static void setSchemeNickname(Context context, String nickname) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(SchemeNickname, nickname).apply();
    }

    public static void setCustomerID(Context context, String customerid) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(CustomerID, customerid).apply();
    }

    public static String getCustomerID(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(CustomerID, "");
    }

    public static void setGsttax(Context context, String GSTTAX) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(GSTTAX, GSTTAX).apply();
    }

    public static String getGsttax(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(GSTTAX, "");
    }

    public static void setRoleid(Context context, String roleid) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(ROLEID, roleid).apply();
    }

    public static String getRoleid(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(ROLEID, "");
    }

    public static String getOrgAvathar(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(OrgAvathar, "");
    }

    public static void setOrgAvathar(Context context, String avathar) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(OrgAvathar, avathar).apply();
    }

    public static String getOrgProfileImg(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(OrgProfileImg, "");
    }

    public static void setOrgProfileImg(Context context, String profileImg) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(OrgProfileImg, profileImg).apply();
    }

    public static String getShowCaseInfo(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(ShowCaseInfo, "");
    }

    public static void setShowCaseInfo(Context context, String profileImg) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(ShowCaseInfo, profileImg).apply();
    }


    public static void setGrams(Context context, String Token) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(isGrams, Token).apply();
    }

    public static String getGrams(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(isGrams, "");
    }


    public static void setPrice(Context context, String Token) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(isprice, Token).apply();
    }

    public static String getPrice(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(isprice, "");
    }

    public static void setIsGiftCard(Context context, String Token) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        sharedPref.edit().putString(isGiftCard, Token).apply();
    }

    public static String getIsGiftCard(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(isGiftCard, "");
    }
}
