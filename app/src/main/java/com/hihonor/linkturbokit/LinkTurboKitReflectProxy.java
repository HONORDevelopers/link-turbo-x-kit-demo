/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2024. All rights reserved.
 */

package com.hihonor.linkturbokit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * 功能描述：通过反射调用各接口
 *
 * @since 2022-04-16
 */
public class LinkTurboKitReflectProxy {
    private static final String TAG = "LinkTurboKitReflectProxy_Test";

    private static volatile LinkTurboKitReflectProxy kitProxyInstance;

    private Handler mHandler = null;

    Class<?> classEmcomManagerEx;

    Class<?> classLinkTurboKitConstants;

    /**
     * LinkTurbo kit error code
     */
    int SUCCESS;

    int NOT_SUPPORT;

    int PARAM_INVALID;

    int REMOTE_EXCEPTION;

    int AUTH_FAILURE;

    int UNREGISTERED;

    int SWITCH_OFF;

    int NETIFACE_INVALID;

    int VERSION_NOT_MATCH;

    int NOT_ALLOW_CURRENT_SCENE;

    int UNKNOWN;

    /**
     * LinkTurbo kit capibility code
     */
    int CAPIBILITY_LINKTURBO_AVAILABLE_STATE_NOTIFY;

    int CAPIBILITY_LAGS_PREDICTION_NOTIFY;

    int CAPIBILITY_QOE_INFO_NOTIFY;

    int CAPIBILITY_SOCKET_STATE_NOTIFY;

    int CAPIBILITY_AVAILABLE_NETIFACE_NOTIFY;

    int ALL_CAPIBILITY;

    /**
     * LinkTurbo kit message id
     */
    int MESSAGE_LINKTURBO_AVAILABLE_STATE_NOTIFY;

    int MESSAGE_LAGS_PREDICTION_NOTIFY;

    int MESSAGE_QOE_INFO_NOTIFY;

    int MESSAGE_SOCKET_STATE_NOTIFY;

    int MESSAGE_AVAILABLE_NETIFACE_NOTIFY;

    /**
     * LinkTurbo available state
     */
    int LINKTURBO_DISABLED;

    int LINKTURBO_ENABLED;

    /**
     * LinkTurbo collaborative mode
     */
    int COLLABORATIVE_MODE_SYSTEM_EXECUTE;

    int COLLABORATIVE_MODE_APP_EXECUTE;

    /**
     * app scene
     */
    int SCENE_INVALID;

    int SCENE_LOGIN;

    int SCENE_UPDATE;

    int SCENE_GAME_BATTLE;

    int SCENE_LONG_VIDEO;

    int SCENE_SHORT_VIDEO;

    int SCENE_LIVE_VIDEO_DOWNLOAD;

    int SCENE_LIVE_VIDEO_UPLOAD;

    int SCENE_DOWNLOAD;

    int SCENE_UPLOAD;

    int SCENE_PAGE_LOADING;

    int SCENE_NAVIGATING;

    int SCENE_CHAT;

    int SCENE_AUDIO_CALL;

    int SCENE_VIDEO_CALL;

    int SCENE_MUSIC;

    /**
     * app action
     */
    int INITIAL_BUFFER;

    int SEEK;

    int VIEW_LAGER_IMAGE;

    int SERVICE_SUSPEND;

    int SERVICE_RESTORE;

    /**
     * network use state
     */
    int NETWORK_UNUSE;

    int NETWORK_INUSE;

    /**
     * qoe level
     */
    int QOE_INVALID;

    int QOE_GOOD;

    int QOE_BAD;

    /**
     * linkturbo kit eventId
     */
    int EVENT_LINKTURBO_REGISTER_RESULT;

    int EVENT_LINKTURBO_AVAILABLE_STATE;

    int EVENT_ELEVATOR_FENCE;

    int EVENT_AWAY_FROM_FIX_AREA_FENCE;

    int EVENT_ROAD_LAGS_CELL_FENCE;

    int EVENT_SUBWAY_LAGS_CELL_FENCE;

    int EVENT_HIGH_SPEED_RAIL_LAGS_CELL_FENCE;

    int EVENT_QOE_INFO;

    int EVENT_SOCKET_BOUND;

    int EVENT_SOCKET_RESET;

    int EVENT_AVAILABLE_NETIFACE;

    /**
     * state of fence
     */
    int OUT;

    int ENTER;

    /**
     * bind flow result
     */
    int BIND_SUCCESS;

    int BIND_FAILURE;

    /**
     * flow reset result
     */
    int RESET_NEEDED;

    int RESET_OCCURRED;

    /**
     * linktrubo netiface available state
     */
    int NETIFACE_STATE_UNAVAILABLE;

    int NETIFACE_STATE_AVAILABLE;

    /**
     * net signal level
     */
    int SIGNAL_LEVEL_UNKNOWN;

    int SIGNAL_LEVEL_POOR;

    int SIGNAL_LEVEL_NORMAL;

    int SIGNAL_LEVEL_GOOD;

    /**
     * net iface id
     */
    int NETIFACEID_INVALID;

    int NETIFACEID_WIFI0;

    int NETIFACEID_DATA0;

    /**
     * app notify info
     */
    String SCENE;

    String ACTION;

    String BWREQ;

    String LATREQ;

    String TPLREQ;

    String RATE;

    String LAT;

    String TPL;

    String LAGBEGIN;

    String LAGEND;

    String LAGPHENOMENON;

    String LAGDOMAINNAME;

    String REASON;

    private LinkTurboKitReflectProxy() {
        HandlerThread kitCallbackThread = new HandlerThread("COM_HIHONOR_LINKTURBO_TEST");
        kitCallbackThread.start();
        if (kitCallbackThread.getLooper() != null) {
            Log.i(TAG, "inti kitHandler");
            mHandler = new KitHandler(kitCallbackThread.getLooper());
        }
        try {
            classEmcomManagerEx = Class.forName("com.hihonor.android.emcom.EmcomManagerEx");
            classLinkTurboKitConstants = Class.forName("com.hihonor.android.emcom.LinkTurboKitConstants");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        getAllConstants();
    }

    /**
     * 功能描述：返回实例单例
     *
     * @return LinkTurboKitReflectProxy
     */
    public static LinkTurboKitReflectProxy getInstance() {
        if (kitProxyInstance == null) {
            synchronized (LinkTurboKitReflectProxy.class) {
                if (kitProxyInstance == null) {
                    kitProxyInstance = new LinkTurboKitReflectProxy();
                }
            }
        }
        return kitProxyInstance;
    }

    /**
     * 功能描述：获取系统中集成的Kit版本号
     */
    public void getLinkTurboVersion() {
        String version = "none";
        try {
            Log.i(TAG, "getLinkTurboVersion");
            Method method = classEmcomManagerEx.getMethod("getLinkTurboVersion");
            Object obj = method.invoke(null);
            if (obj instanceof String) {
                version = (String) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        String str = Optional.of(version).orElse("getLinkTurboVersion result is null");
        Log.i(TAG, "getLinkTurboVersion " + str);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：向系统注册使用LinkTurbo能力
     *
     * @param capabilities       需要注册使用的子能力
     * @param collaborativeMode  与系统协同的模式（与collaborativeScene联合使用）
     * @param collaborativeScene 与系统协同的场景（与collaborativeMode联合使用）
     */
    public void registerApp(int capabilities, int collaborativeMode, int collaborativeScene) {
        int res = UNKNOWN;
        try {
            Log.i(TAG, "registerApp");
            Method method = classEmcomManagerEx.getMethod("registerApp",
                    int.class, int.class, int.class, Handler.class);
            Object obj = method.invoke(null, capabilities, collaborativeMode, collaborativeScene, mHandler);
            if (obj instanceof Integer) {
                res = (int) obj;
            }
            Log.i(TAG, "testGetLinkTurboVersion res is：" + res);
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        String str = "Process registerApp, result: " + res;
        Log.i(TAG, "registerApp:" + str);
        MainActivity.getsMainActivity().setTextMessage(str);
        // if result != SUCCESS，建议根据返回原因值的原因值进行对应处理
    }

    /**
     * 功能描述：向系统去注册使用LinkTurbo能力
     */
    public void unregisterApp() {
        int res = UNKNOWN;
        try {
            Log.i(TAG, "unregisterApp");
            Method method = classEmcomManagerEx.getMethod("unregisterApp");
            Object obj = method.invoke(null);
            if (obj instanceof Integer) {
                res = (int) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        String str = "Process unregisterApp, result: " + res;
        Log.i(TAG, "unregisterApp:" + str);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：应用调用该接口向系统查询LinkTurbo能力可用状态
     *
     * @param uid app uid
     */
    public void getLinkTurboEnableState(int uid) {
        int res = UNKNOWN;
        try {
            Log.i(TAG, "getLinkTurboEnableState");
            Method method = classEmcomManagerEx.getMethod("getLinkTurboEnableState", int.class);
            Object obj = method.invoke(null, uid);
            if (obj instanceof Integer) {
                res = (int) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        String str = "Get LinkTurbo Enable State, result: " + res;
        Log.i(TAG, "getLinkTurboEnableState:" + str);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：应用调用该接口向系统查询网卡的QoE评估结果
     *
     * @param uid app uid
     */
    public void getNetworkQoE(int uid) {
        Bundle result = null;
        try {
            Log.i(TAG, "getNetworkQoE");
            Method method = classEmcomManagerEx.getMethod("getNetworkQoE", int.class);
            Object obj = method.invoke(null, uid);
            if (obj instanceof Bundle) {
                result = (Bundle) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        if (result == null) {
            Log.e(TAG, "result is null");
        }
        String str = Utils.parseBundleToString(result).orElse("getNetworkQoE null");
        Log.i(TAG, "getNetworkQoE: " + result);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：应用调用该接口激活指定的网卡
     *
     * @param netIfaceId 需要激活的目标网卡ID
     */
    public void activeNetInterface(int netIfaceId) {
        int res = UNKNOWN;
        try {
            Log.i(TAG, "activeNetInterface");
            Method method = classEmcomManagerEx.getMethod("activeNetInterface", int.class);
            Object obj = method.invoke(null, netIfaceId);
            if (obj instanceof Integer) {
                res = (int) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        String str = "Active Net Interface, result: " + res;
        Log.i(TAG, "activeNetInterface: " + res);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：应用调用该接口获取系统当前可用网卡
     */
    public void getAvailableNetInterface() {
        Map<Integer, Integer> result = null;
        try {
            Log.i(TAG, "getAvailableNetInterface");
            Method method = classEmcomManagerEx.getMethod("getAvailableNetInterface");
            Object obj = method.invoke(null);
            if (obj instanceof Map) {
                result = (Map<Integer, Integer>) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        if (result == null) {
            Log.e(TAG, "result is null");
        }
        String str = Utils.parseMapToString(result).orElse("getAvailableNetInterface result is null");
        Log.i(TAG, "getAvailableNetInterface " + str);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：应用调用该接口获取网卡的信号强度
     *
     * @param netInterfaceId 目标网卡的ID
     */
    public void getNetSignalLevel(int netInterfaceId) {
        int res = UNKNOWN;
        try {
            Log.i(TAG, "getNetSignalLevel");
            Method method = classEmcomManagerEx.getMethod("getNetSignalLevel", int.class);
            Object obj = method.invoke(null, netInterfaceId);
            if (obj instanceof Integer) {
                res = (int) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        String str = "Get net signal level，result: " + res;
        Log.i(TAG, "getNetSignalLevel, signal:" + res);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：应用调用该接口将业务流全部绑定到指定网卡
     *
     * @param netIfaceId 要绑定的目标网卡，0：wlan0，1：data0
     */
    public void bindToNetInterface(int netIfaceId) {
        int res = UNKNOWN;
        try {
            Log.i(TAG, "bindToNetInterface");
            Method method = classEmcomManagerEx.getMethod("bindToNetInterface", int.class);
            Object obj = method.invoke(null, netIfaceId);
            if (obj instanceof Integer) {
                res = (int) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        String str = "bindToNetInterface all, result: " + res;
        Log.i(TAG, "bindToNetInterface all, result:" + res);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：应用调用该接口将业务指定的socket绑定到指定网卡
     *
     * @param fd         业务流socket
     * @param netIfaceId 要绑定业务流的网卡，0：wlan0，1：data0
     */
    public void bindToNetInterface(int fd, int netIfaceId) {
        int res = UNKNOWN;
        try {
            Log.i(TAG, "bindToNetInterface");
            Method method = classEmcomManagerEx.getMethod("bindToNetInterface", int.class, int.class);
            Object obj = method.invoke(null, fd, netIfaceId);
            if (obj instanceof Integer) {
                res = (int) obj;
            }
        } catch (NoSuchMethodException | UnsupportedOperationException | InvocationTargetException
            | IllegalArgumentException | IllegalAccessException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        String str = "bindToNetInterface fd, result: " + res;
        Log.i(TAG, "bindToNetInterface fd, result:" + res);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：应用调用该接口将业务指定的socket绑定到指定网卡
     */
    public void notifyAppInfo() {
        try {
            Log.i(TAG, "notifyAppInfo");
            Method method = classEmcomManagerEx.getMethod("notifyAppInfo", Bundle.class);
            method.invoke(null, getAppInfo());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private void handleLinkTurboStateChanged(Message msg) {
        if (!(msg.obj instanceof Integer)) {
            Log.e(TAG, "handleLinkTurboStateChanged msg is illegal");
            return;
        }
        int eventId = msg.arg1;
        int linkTurboEnableState = (int) msg.obj;
        String str = "handleLinkTurboStateChanged eventId:" + eventId + ", msg:" + linkTurboEnableState;
        Log.i(TAG, "handleLinkTurboStateChanged:" + str);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    private void handleFenceEventNotify(Message msg) {
        if (!(msg.obj instanceof Bundle)) {
            Log.e(TAG, "handleFenceEventNotify failed as msg is null");
            return;
        }
        int eventId = msg.arg1;
        Bundle bundle = (Bundle) msg.obj;
        int fenceValue = bundle.getInt("action");
        Log.i(TAG, "handleFenceEventNotify, eventId:" + eventId + ", fenceValue:" + fenceValue);
        String text = "handleFenceEventNotify, eventId:" + eventId + ", fenceValue:" + fenceValue;
        MainActivity.getsMainActivity().setTextMessage(text);
    }

    private void handleQoeStateChanged(Message msg) {
        if (!(msg.obj instanceof Bundle)) {
            Log.e(TAG, "handleQoeStateChanged failed as msg is null");
            return;
        }
        int eventId = msg.arg1;
        Bundle bundle = (Bundle) msg.obj;
        String str = Utils.parseBundleToString(bundle).orElse("handleQoeStateChanged msg is null");
        Log.i(TAG, "handleQoeStateChanged, eventId:" + eventId + ", qoeString:" + str + "; bundle:" + bundle);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    private void handleFlowStateChanged(Message msg) {
        int eventId = msg.arg1;
        Bundle bundle = (Bundle) msg.obj;
        String str = Utils.parseBundleToString(bundle).orElse("handleQoeStateChanged msg is null");
        Log.i(TAG, "handleFlowStateChanged, eventId:" + eventId + ", flow:" + str + "; bundle:" + bundle);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    private void handleNetworkStateChanged(Message msg) {
        int eventId = msg.arg1;
        Map<Integer, Integer> map = (Map) msg.obj;
        String str = Utils.parseMapToString(map).orElse("handleNetworkStateChanged msg is null");
        Log.i(TAG, "handleNetworkStateChanged, eventId:" + eventId + ", qoeString:" + str);
        MainActivity.getsMainActivity().setTextMessage(str);
    }

    /**
     * 功能描述：handler
     */
    public class KitHandler extends Handler {
        KitHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                Log.e(TAG, "handleMessage msg is illegal");
                return;
            }
            if (msg.what == CAPIBILITY_LINKTURBO_AVAILABLE_STATE_NOTIFY) {
                handleLinkTurboStateChanged(msg);
            }
            if (msg.what == CAPIBILITY_LAGS_PREDICTION_NOTIFY) {
                handleFenceEventNotify(msg);
            }
            if (msg.what == CAPIBILITY_QOE_INFO_NOTIFY) {
                handleQoeStateChanged(msg);
            }
            if (msg.what == CAPIBILITY_SOCKET_STATE_NOTIFY) {
                handleFlowStateChanged(msg);
            }
            if (msg.what == CAPIBILITY_AVAILABLE_NETIFACE_NOTIFY) {
                handleNetworkStateChanged(msg);
            }
        }
    }

    private int getIntConstant(String field) {
        int res = -1;
        Object obj = getConstant(field);
        if (obj != null && (obj instanceof Integer)) {
            res = (int) obj;
        }
        return res;
    }

    private Optional<String> getStringConstant(String field) {
        String str = null;
        Object obj = getConstant(field);
        if (obj != null && (obj instanceof String)) {
            str = (String) obj;
        }
        return Optional.ofNullable(str);
    }

    private Object getConstant(String field) {
        try {
            return classLinkTurboKitConstants.getField(field).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getCapability() {
        int cap = getIntConstant("CAPIBILITY_LAGS_PREDICTION_NOTIFY");
        cap |= getIntConstant("CAPIBILITY_SOCKET_STATE_NOTIFY");
        return cap;
    }

    private int getCollaMode() {
        int collaMode = getIntConstant("COLLABORATIVE_MODE_SYSTEM_EXECUTE");
        return collaMode;
    }

    private int getCollaScene() {
        int scene = getIntConstant("SCENE_AUDIO_CALL");
        scene |= getIntConstant("SCENE_GAME_BATTLE");
        return scene;
    }

    private Bundle getAppInfo() {
        Bundle bundle = new Bundle();
        int scene = getIntConstant("SCENE_SHORT_VIDEO");
        int action = getIntConstant("NETWORK_INUSE");

        Log.i(TAG, "scene: " + scene + ", action: " + action);
        bundle.putInt("scene", scene);
        bundle.putInt("action", action);
        bundle.putLong("lagBegin", System.currentTimeMillis());
        return bundle;
    }

    private void getAllConstants() {
        /**
         * LinkTurbo kit error code
         */
        errorCode();

        /**
         * LinkTurbo kit capibility code
         */
        capibilityCode();

        /**
         * LinkTurbo kit message id
         */
        messageId();

        /**
         * LinkTurbo available state
         */
        availableState();

        /**
         * LinkTurbo collaborative mode
         */
        collaborativeMode();

        /**
         * app scene
         */
        appScene();

        /**
         * app action
         */
        appAction();

        /**
         * linkturbo kit eventId
         */
        eventId();

        /**
         * network use state
         */
        NETWORK_UNUSE = getIntConstant("NETWORK_UNUSE");
        NETWORK_INUSE = getIntConstant("NETWORK_INUSE");

        /**
         * qoe level
         */
        QOE_INVALID = getIntConstant("QOE_INVALID");
        QOE_GOOD = getIntConstant("QOE_GOOD");
        QOE_BAD = getIntConstant("QOE_BAD");

        /**
         * state of fence
         */
        stateOfFence();

        /**
         * bind flow result
         */
        bindFlowResult();

        /**
         * flow reset result
         */
        flowResetResult();

        /**
         * linktrubo netiface available state
         */
        netifaceAvailableState();

        /**
         * net signal level
         */
        netSignalLevel();

        /**
         * net iface id
         */
        netIfaceId();

        /**
         * app notify info
         */
        appNotifyInfo();
    }

    private void appNotifyInfo() {
        SCENE = getStringConstant("SCENE").orElse("scene");
        ACTION = getStringConstant("ACTION").orElse("action");
        BWREQ = getStringConstant("BWREQ").orElse("BWREQ");
        LATREQ = getStringConstant("LATREQ").orElse("LATREQ");
        TPLREQ = getStringConstant("TPLREQ").orElse("TPLREQ");
        RATE = getStringConstant("TPLREQ").orElse("rate");
        LAT = getStringConstant("LAT").orElse("LAT");
        TPL = getStringConstant("TPL").orElse("TPL");
        LAGBEGIN = getStringConstant("LAGBEGIN").orElse("lagBegin");
        LAGEND = getStringConstant("LAGEND").orElse("lagEnd");
        LAGPHENOMENON = getStringConstant("LAGPHENOMENON").orElse("lagPhenomenon");
        LAGDOMAINNAME = getStringConstant("LAGDOMAINNAME").orElse("lagDomainName");
        REASON = getStringConstant("REASON").orElse("reason");
    }

    private void netIfaceId() {
        NETIFACEID_INVALID = getIntConstant("NETIFACEID_INVALID");
        NETIFACEID_WIFI0 = getIntConstant("NETIFACEID_WIFI0");
        NETIFACEID_DATA0 = getIntConstant("NETIFACEID_DATA0");
    }

    private void netSignalLevel() {
        SIGNAL_LEVEL_UNKNOWN = getIntConstant("SIGNAL_LEVEL_UNKNOWN");
        SIGNAL_LEVEL_POOR = getIntConstant("SIGNAL_LEVEL_POOR");
        SIGNAL_LEVEL_NORMAL = getIntConstant("SIGNAL_LEVEL_NORMAL");
        SIGNAL_LEVEL_GOOD = getIntConstant("SIGNAL_LEVEL_GOOD");
    }

    private void netifaceAvailableState() {
        NETIFACE_STATE_UNAVAILABLE = getIntConstant("NETIFACE_STATE_UNAVAILABLE");
        NETIFACE_STATE_AVAILABLE = getIntConstant("NETIFACE_STATE_AVAILABLE");
    }

    private void flowResetResult() {
        RESET_NEEDED = getIntConstant("RESET_NEEDED");
        RESET_OCCURRED = getIntConstant("RESET_OCCURRED");
    }

    private void bindFlowResult() {
        BIND_SUCCESS = getIntConstant("BIND_SUCCESS");
        BIND_FAILURE = getIntConstant("BIND_FAILURE");
    }

    private void stateOfFence() {
        OUT = getIntConstant("OUT");
        ENTER = getIntConstant("ENTER");
    }

    private void eventId() {
        EVENT_LINKTURBO_REGISTER_RESULT = getIntConstant("EVENT_LINKTURBO_REGISTER_RESULT");
        EVENT_LINKTURBO_AVAILABLE_STATE = getIntConstant("EVENT_LINKTURBO_AVAILABLE_STATE");
        EVENT_ELEVATOR_FENCE = getIntConstant("EVENT_ELEVATOR_FENCE");
        EVENT_AWAY_FROM_FIX_AREA_FENCE = getIntConstant("EVENT_AWAY_FROM_FIX_AREA_FENCE");
        EVENT_ROAD_LAGS_CELL_FENCE = getIntConstant("EVENT_ROAD_LAGS_CELL_FENCE");
        EVENT_SUBWAY_LAGS_CELL_FENCE = getIntConstant("EVENT_SUBWAY_LAGS_CELL_FENCE");
        EVENT_HIGH_SPEED_RAIL_LAGS_CELL_FENCE = getIntConstant("EVENT_HIGH_SPEED_RAIL_LAGS_CELL_FENCE");
        EVENT_QOE_INFO = getIntConstant("EVENT_QOE_INFO");
        EVENT_SOCKET_BOUND = getIntConstant("EVENT_SOCKET_BOUND");
        EVENT_SOCKET_RESET = getIntConstant("EVENT_SOCKET_RESET");
        EVENT_AVAILABLE_NETIFACE = getIntConstant("EVENT_AVAILABLE_NETIFACE");
    }

    private void appAction() {
        INITIAL_BUFFER = getIntConstant("INITIAL_BUFFER");
        SEEK = getIntConstant("SEEK");
        VIEW_LAGER_IMAGE = getIntConstant("VIEW_LAGER_IMAGE");
        SERVICE_SUSPEND = getIntConstant("SERVICE_SUSPEND");
        SERVICE_RESTORE = getIntConstant("SERVICE_RESTORE");
    }

    private void appScene() {
        SCENE_INVALID = getIntConstant("SCENE_INVALID");
        SCENE_LOGIN = getIntConstant("SCENE_LOGIN");
        SCENE_UPDATE = getIntConstant("SCENE_UPDATE");
        SCENE_GAME_BATTLE = getIntConstant("SCENE_GAME_BATTLE");
        SCENE_LONG_VIDEO = getIntConstant("SCENE_LONG_VIDEO");
        SCENE_SHORT_VIDEO = getIntConstant("SCENE_SHORT_VIDEO");
        SCENE_LIVE_VIDEO_DOWNLOAD = getIntConstant("SCENE_LIVE_VIDEO_DOWNLOAD");
        SCENE_LIVE_VIDEO_UPLOAD = getIntConstant("SCENE_LIVE_VIDEO_UPLOAD");
        SCENE_DOWNLOAD = getIntConstant("SCENE_DOWNLOAD");
        SCENE_UPLOAD = getIntConstant("SCENE_UPLOAD");
        SCENE_PAGE_LOADING = getIntConstant("SCENE_PAGE_LOADING");
        SCENE_NAVIGATING = getIntConstant("SCENE_NAVIGATING");
        SCENE_CHAT = getIntConstant("SCENE_CHAT");
        SCENE_AUDIO_CALL = getIntConstant("SCENE_AUDIO_CALL");
        SCENE_VIDEO_CALL = getIntConstant("SCENE_VIDEO_CALL");
        SCENE_MUSIC = getIntConstant("SCENE_MUSIC");
    }

    private void collaborativeMode() {
        COLLABORATIVE_MODE_SYSTEM_EXECUTE = getIntConstant("COLLABORATIVE_MODE_SYSTEM_EXECUTE");
        COLLABORATIVE_MODE_APP_EXECUTE = getIntConstant("COLLABORATIVE_MODE_APP_EXECUTE");
    }

    private void availableState() {
        LINKTURBO_DISABLED = getIntConstant("LINKTURBO_DISABLED");
        LINKTURBO_ENABLED = getIntConstant("LINKTURBO_ENABLED");
    }

    private void messageId() {
        MESSAGE_LINKTURBO_AVAILABLE_STATE_NOTIFY = getIntConstant("MESSAGE_LINKTURBO_AVAILABLE_STATE_NOTIFY");
        MESSAGE_LAGS_PREDICTION_NOTIFY = getIntConstant("MESSAGE_LAGS_PREDICTION_NOTIFY");
        MESSAGE_QOE_INFO_NOTIFY = getIntConstant("MESSAGE_QOE_INFO_NOTIFY");
        MESSAGE_SOCKET_STATE_NOTIFY = getIntConstant("MESSAGE_SOCKET_STATE_NOTIFY");
        MESSAGE_AVAILABLE_NETIFACE_NOTIFY = getIntConstant("MESSAGE_AVAILABLE_NETIFACE_NOTIFY");
    }

    private void capibilityCode() {
        CAPIBILITY_LINKTURBO_AVAILABLE_STATE_NOTIFY = getIntConstant("CAPIBILITY_LINKTURBO_AVAILABLE_STATE_NOTIFY");
        CAPIBILITY_LAGS_PREDICTION_NOTIFY = getIntConstant("CAPIBILITY_LAGS_PREDICTION_NOTIFY");
        CAPIBILITY_QOE_INFO_NOTIFY = getIntConstant("CAPIBILITY_QOE_INFO_NOTIFY");
        CAPIBILITY_SOCKET_STATE_NOTIFY = getIntConstant("CAPIBILITY_SOCKET_STATE_NOTIFY");
        CAPIBILITY_AVAILABLE_NETIFACE_NOTIFY = getIntConstant("CAPIBILITY_AVAILABLE_NETIFACE_NOTIFY");
        ALL_CAPIBILITY = getIntConstant("ALL_CAPIBILITY");
    }

    private void errorCode() {
        SUCCESS = getIntConstant("SUCCESS");
        NOT_SUPPORT = getIntConstant("NOT_SUPPORT");
        PARAM_INVALID = getIntConstant("PARAM_INVALID");
        REMOTE_EXCEPTION = getIntConstant("REMOTE_EXCEPTION");
        AUTH_FAILURE = getIntConstant("AUTH_FAILURE");
        UNREGISTERED = getIntConstant("UNREGISTERED");
        SWITCH_OFF = getIntConstant("SWITCH_OFF");
        NETIFACEID_INVALID = getIntConstant("NETIFACE_INVALID");
        VERSION_NOT_MATCH = getIntConstant("VERSION_NOT_MATCH");
        NOT_ALLOW_CURRENT_SCENE = getIntConstant("NOT_ALLOW_CURRENT_SCENE");
        UNKNOWN = getIntConstant("UNKNOWN");
    }
}
