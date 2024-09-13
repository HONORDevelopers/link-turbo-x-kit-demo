/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2024. All rights reserved.
 */

package com.hihonor.linkturbokit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/**
 * 功能描述：通过广播事件调用各接口
 *
 * @since 2022-04-16
 */
public class KitAppBroadReceiver extends BroadcastReceiver {
    private static final String TAG = "LinkTurboKit_Test_App_BroadcastReceiver";
    private static final String ACTION_LINKTURBO_GETKITVERSION = "com.hihonor.linkturbokit.getLinkTurboVersion";
    private static final String ACTION_LINKTURBO_REGISTERAPP = "com.hihonor.linkturbokit.registerApp";
    private static final String ACTION_LINKTURBO_UNREGISTERAPP = "com.hihonor.linkturbokit.undregisterApp";
    private static final String ACTION_LINKTURBO_GETLINKTURBOENABLESTATE =
            "com.hihonor.linkturbokit.getLinkTurboEnableState";
    private static final String ACTION_LINKTURBO_GETNETWORKQOE = "com.hihonor.linkturbokit.getNetworkQoE";
    private static final String ACTION_LINKTURBO_GETAVNETINTF = "com.hihonor.linkturbokit.getAvailableNetInterface";
    private static final String ACTION_LINKTURBO_GETNETSIGNLEL = "com.hihonor.linkturbokit.getNetSignalLevel";
    private static final String ACTION_LINKTURBO_ACTNETINTF = "com.hihonor.linkturbokit.activeNetInterface";
    private static final String ACTION_LINKTURBO_BINDTONETINTF = "com.hihonor.linkturbokit.bindToNetInterface";
    private static final String ACTION_LINKTURBO_BINDTONETINTFFD = "com.hihonor.linkturbokit.bindToNetInterfacefd";
    private static final String ACTION_LINKTURBO_NOTIFYAPPINFO = "com.hihonor.linkturbokit.notifyAppInfo";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        int uid = MainActivity.getsMainActivity().getUid();
        String action = intent.getAction();
        if (ACTION_LINKTURBO_NOTIFYAPPINFO.equals(action)) {
            // 模拟广播发送，传递业务场景和开始卡顿
            // adb shell am broadcast -a com.hihonor.linkturbokit.notifyAppInfo --ei scene 2 --el lagBegin 1
            // 模拟广播发送，传递业务场景和卡顿结束
            // adb shell am broadcast -a com.hihonor.linkturbokit.notifyAppInfo --ei scene 2 --el lagEnd 1
            actionLinkturboNotifyappinfo(intent);
        } else if (ACTION_LINKTURBO_GETKITVERSION.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.getLinkTurboVersion
            actionLinkturboGetkitversion();
        } else if (ACTION_LINKTURBO_REGISTERAPP.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.registerApp --ei capabilities 15
            // --ei collaborativeMode 3 --ei collaborativeScene 2
            actionLinkturboRegisterapp(intent);
        } else if (ACTION_LINKTURBO_UNREGISTERAPP.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.undregisterApp
            actionLinkturboUnregisterapp();
        } else if (ACTION_LINKTURBO_GETLINKTURBOENABLESTATE.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.getLinkTurboEnableState
            actionLinkturboGetlinkturboenablestate(uid);
        } else if (ACTION_LINKTURBO_GETNETWORKQOE.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.getNetworkQoE
            actionLinkturboGetnetworkqoe(uid);
        } else if (ACTION_LINKTURBO_GETAVNETINTF.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.getAvailableNetInterface
            actionLinkturboGetavnetintf();
        } else if (ACTION_LINKTURBO_GETNETSIGNLEL.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.getNetSignalLevel --ei netInterfaceId 1
            actionLinkturboGetnetsignlel(intent);
        } else if (ACTION_LINKTURBO_ACTNETINTF.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.activeNetInterface --ei netInterfaceId 1
            actionLinkturboActnetintf(intent);
        } else if (ACTION_LINKTURBO_BINDTONETINTF.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.bindToNetInterface --ei netInterfaceId 1
            actionLinkturboBindtonetintf(intent);
        } else if (ACTION_LINKTURBO_BINDTONETINTFFD.equals(action)) {
            // adb shell am broadcast -a com.hihonor.linkturbokit.bindToNetInterfacefd --ei fd 123 --ei netInterfaceId 1
            actionLinkturboBindtonetintffd(intent);
        } else {
            Log.i(TAG, "received error");
        }
    }

    private static void actionLinkturboBindtonetintffd(Intent intent) {
        try {
            Bundle bunde = intent.getExtras();
            int fd = bunde.getInt("fd");
            int netInterfaceId = bunde.getInt("netInterfaceId");
            Log.i(TAG, "received broad: " + "bindToNetInterface, " + "fd: " + fd +
                    "netInterfaceId: " + netInterfaceId);
            LinkTurboKitProxy.getInstance().bindToNetInterface(fd, netInterfaceId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboBindtonetintf(Intent intent) {
        try {
            Bundle bunde = intent.getExtras();
            int netInterfaceId = bunde.getInt("netInterfaceId");
            Log.i(TAG, "received broad: " + "bindToNetInterface, " + "netInterfaceId: " + netInterfaceId);
            LinkTurboKitProxy.getInstance().bindToNetInterface(netInterfaceId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboActnetintf(Intent intent) {
        try {
            Bundle bunde = intent.getExtras();
            int netInterfaceId = bunde.getInt("netInterfaceId");
            Log.i(TAG, "received broad: " + "activeNetInterface, " + "netInterfaceId: " + netInterfaceId);
            LinkTurboKitProxy.getInstance().activeNetInterface(netInterfaceId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboGetnetsignlel(Intent intent) {
        try {
            Bundle bunde = intent.getExtras();
            int netInterfaceId = bunde.getInt("netInterfaceId");
            Log.i(TAG, "received broad: " + "getNetSignalLevel, " + "netInterfaceId: " + netInterfaceId);
            LinkTurboKitProxy.getInstance().getNetSignalLevel(netInterfaceId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboGetavnetintf() {
        try {
            Log.i(TAG, "received broad: " + "getAvailableNetInterface");
            LinkTurboKitProxy.getInstance().getAvailableNetInterface();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboGetnetworkqoe(int uid) {
        try {
            Log.i(TAG, "received broad: " + "getNetworkQoE, " + "uid: " + uid);
            LinkTurboKitProxy.getInstance().getNetworkQoE(uid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboGetlinkturboenablestate(int uid) {
        try {
            Log.i(TAG, "received broad: " + "getLinkTurboEnableState");
            LinkTurboKitProxy.getInstance().getLinkTurboEnableState(uid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboUnregisterapp() {
        try {
            Log.i(TAG, "received broad: undregisterApp");
            LinkTurboKitProxy.getInstance().undregisterApp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboRegisterapp(Intent intent) {
        try {
            Bundle bunde = intent.getExtras();
            int capabilities = bunde.getInt("capabilities");
            int collaborativeMode = bunde.getInt("collaborativeMode");
            int collaborativeScene = bunde.getInt("collaborativeScene");
            Log.i(TAG, "received broad: registerApp," + "capabilities:" + capabilities +
                    "collaborativeMode:" + collaborativeMode + "collaborativeMode:" + collaborativeScene);
            LinkTurboKitProxy.getInstance().registerApp(capabilities, collaborativeMode, collaborativeScene);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboGetkitversion() {
        try {
            Log.i(TAG, "received broad: getLinkTurboVersion");
            LinkTurboKitProxy.getInstance().getLinkTurboVersion();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void actionLinkturboNotifyappinfo(Intent intent) {
        Bundle bunde = intent.getExtras();
        Bundle appInfo = new Bundle();
        if (bunde.containsKey("scene")) {
            int appScene = bunde.getInt("scene");
            appInfo.putInt("scene", appScene);
        }
        if (bunde.containsKey("action")) {
            int appAction = bunde.getInt("action");
            appInfo.putInt("action", appAction);
        }
        if (bunde.containsKey("lagBegin")) {
            Long lagBeginTemp = bunde.getLong("lagBegin");
            if (lagBeginTemp != 0L) {
                Long lagBeginTimeStamp = System.currentTimeMillis();
                appInfo.putLong("lagBegin", lagBeginTimeStamp);
            }
        }
        if (bunde.containsKey("lagEnd")) {
            Long lagEndTemp = bunde.getLong("lagEnd");
            if (lagEndTemp != 0L) {
                Long lagEndTimeStamp = System.currentTimeMillis();
                appInfo.putLong("lagEnd", lagEndTimeStamp);
            }
        }
        if (bunde.containsKey("reason")) {
            int appLagReason = bunde.getInt("reason");
            appInfo.putInt("reason", appLagReason);
        }
        Log.i(TAG, "received broad:" + appInfo);
        try {
            LinkTurboKitProxy.getInstance().notifyAppInfo(appInfo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
