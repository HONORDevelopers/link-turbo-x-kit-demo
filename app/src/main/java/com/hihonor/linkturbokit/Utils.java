/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2024. All rights reserved.
 */

package com.hihonor.linkturbokit;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import static com.hihonor.android.emcom.LinkTurboKitConstants.NETIFACEID_DATA0;
import static com.hihonor.android.emcom.LinkTurboKitConstants.NETIFACEID_WIFI0;

/**
 * common utils for all use
 *
 * @since 2022-04-16
 */
public class Utils {
    private static final String TAG = "LinkTurboKit_Test_App_Utils";

    /**
     * parser bundle to String
     *
     * @param bundle info
     * @return string of bundle
     */
    public static Optional<String> parseBundleToString(Bundle bundle) {
        String jsonStr = null;
        if (bundle == null) {
            Log.e(TAG, "parseNotifyAppInfo failed as appInfo is null");
            return Optional.ofNullable(jsonStr);
        }
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                json.put(key, JSONObject.wrap(bundle.get(key)));
            } catch (JSONException e) {
                Log.e(TAG, "parseBundleToString failed");
                break;
            }
        }
        return Optional.of(json.toString());
    }

    /**
     * parser map to String
     *
     * @param map info
     * @return string of bundle
     */
    public static Optional<String> parseMapToString(Map<Integer, Integer> map) {
        if (map == null) {
            Log.e(TAG, "parseNotifyAppInfo failed as appInfo is null");
            return Optional.ofNullable(null);
        }
        JSONObject json = new JSONObject();
        try {
            if (map.containsKey(NETIFACEID_WIFI0)) {
                json.put("WIFI0", map.get(NETIFACEID_WIFI0));
            }
            if (map.containsKey(NETIFACEID_DATA0)) {
                json.put("DATA0", map.get(NETIFACEID_DATA0));
            }
        } catch (JSONException e) {
            Log.e(TAG, "parseMapToString failed");
        }
        return Optional.of(json.toString());
    }
}
