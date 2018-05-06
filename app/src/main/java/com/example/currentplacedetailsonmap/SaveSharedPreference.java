package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by macbook on 21/03/2018.
 */

public class SaveSharedPreference {
    static final String PREF_USER_NAME = "username";
    static final String PREF_GROUP_NAME = "groupname";
    static final String PREF_EMOJI_ID = "emojiId";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setGroupName(Context ctx, String groupName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_GROUP_NAME, groupName);
        editor.commit();
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getGroupName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_GROUP_NAME, "");
    }

    public static int getEmojiId(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_EMOJI_ID, 0);
    }

    public static void setEmojiId(Context ctx, int emojiId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_EMOJI_ID, emojiId);
        editor.commit();
    }

}
