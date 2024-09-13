/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2024. All rights reserved.
 */

package com.hihonor.linkturbokit;

import java.util.Map;
import java.util.Optional;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.hihonor.android.emcom.EmcomManagerEx;
import com.hihonor.android.emcom.LinkTurboKitConstants;

/**
 * 功能描述：通过广播事件调用各接口
 *
 * @since 2022-04-16
 */
public class LinkTurboKitProxy {
    private static final String TAG = "LinkTurboKit_Test_App_Api";

    private static volatile LinkTurboKitProxy kitProxyInstance;

    private Handler mKitHandler = null;

    private LinkTurboKitProxy() {
        HandlerThread kitCallbackThread = new HandlerThread("COM_HIHONOR_KINKTURBO_TEST_APP");
        kitCallbackThread.start();
        if (kitCallbackThread.getLooper() != null) {
            Log.i(TAG, "inti kitHandler");
            mKitHandler = new KitHandler(kitCallbackThread.getLooper());
        }
    }

    /**
     * 获取单例
     *
     * @return LinkTurboKitProxy实例
     */
    public static LinkTurboKitProxy getInstance() {
        if (kitProxyInstance == null) {
            synchronized (LinkTurboKitProxy.class) {
                if (kitProxyInstance == null) {
                    kitProxyInstance = new LinkTurboKitProxy();
                }
            }
        }
        return kitProxyInstance;
    }

    /**
     * 功能描述：获取系统中集成的Kit版本号
     *
     * @return String eg.2.1.0
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public String getLinkTurboVersion() throws RemoteException {
        try {
            String version = EmcomManagerEx.getLinkTurboVersion();
            String str = Optional.of(version).orElse("getLinkTurboVersion result is null");
            Log.i(TAG, "getLinkTurboVersion" + str);
            MainActivity.getsMainActivity().setTextMessage(str);
            return version;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "getLinkTurboVersion, result: " + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return "";
        }
    }

    /**
     * 注册接口
     *
     * @param capabilities capabilities
     * @param collaborativeMode collaborativeMode
     * @param collaborativeScene collaborativeScene
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public void registerApp(int capabilities, int collaborativeMode, int collaborativeScene) throws RemoteException {
        try {
            int result = EmcomManagerEx.registerApp(capabilities, collaborativeMode, collaborativeScene, mKitHandler);
            String str = "Processing register:" + result + ", waiting for register result.";
            Log.i(TAG, "registerApp:" + str);
            MainActivity.getsMainActivity().setTextMessage(str);
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "registerApp, result: " + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
        }
    }

    /**
     * 功能描述：
     * 应用调用该接口后，系统仅保留 Android 默认网络接口，关闭其余网口，清空记录的业务规则，所有数据流绑回到 Android 默认网络接口。
     * 当应用在调整策略中发生丢失网络状态等异常情况，可以使用该接口将业务的网络行为进行重置。
     *
     * @return int 0是调用成功
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public int undregisterApp() throws RemoteException {
        try {
            int result = EmcomManagerEx.unregisterApp();
            String str = "Cancel register, result: " + result;
            Log.i(TAG, "undregisterApp:" + str);
            MainActivity.getsMainActivity().setTextMessage(str);
            return result;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "undregisterApp, result: " + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return -1;
        }
    }

    /**
     * 功能描述：获取当前系统中指定应用的网络加速可用状态。
     *
     * @param uid 应用 UID
     * @return int 返回的是当前系统中指定应用的网络加速可用状态，0为不可用，1为可用。
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public int getLinkTurboEnableState(int uid) throws RemoteException {
        try {
            int result = EmcomManagerEx.getLinkTurboEnableState(uid);
            String str = "Get LinkTurbo Enable State, result: " + result;
            Log.i(TAG, "getLinkTurboEnableState:" + str);
            MainActivity.getsMainActivity().setTextMessage(str);
            // 0：功能不可用，1：功能可用
            return result;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "getLinkTurboEnableState, result: " + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return -1;
        }
    }

    /**
     * 功能描述：应用调用该接口向系统查询网卡的QoE评估结果
     *
     * @param uid 用户标识符
     * @return int
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public String getNetworkQoE(int uid) throws RemoteException {
        try {
            // 结果显示：{"uid": 10185,“netIfaceId”:1,"qoeLevel": 4}
            Bundle result = EmcomManagerEx.getNetworkQoE(uid);
            if (result == null) {
                Log.e(TAG, "result is null");
            }
            String str = Utils.parseBundleToString(result).orElse("getNetworkQoE null");
            Log.i(TAG, "getNetworkQoE:" + result);
            MainActivity.getsMainActivity().setTextMessage(str);
            return str;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "getNetworkQoE, result: " + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return "";
        }
    }

    /**
     * 功能描述：应用调用该接口激活指定的网卡
     *
     * @param netIfaceId 网络接口 ID
     * @return int 0是调用成功
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public int activeNetInterface(int netIfaceId) throws RemoteException {
        try {
            // 0：调用成功
            int result = EmcomManagerEx.activeNetInterface(netIfaceId);
            String str = "activeNetInterface, result:" + result;
            Log.i(TAG, "activeNetInterface, result:" + result);
            MainActivity.getsMainActivity().setTextMessage(str);
            return result;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "activeNetInterface, result:" + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return -1;
        }
    }

    /**
     * 功能描述：应用调用该接口获取系统当前可用网卡
     *
     * @return String tst
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public String getAvailableNetInterface() throws RemoteException {
        try {
            // 结果显示：{"WIFI0":1,"DATA0":0}
            Map<Integer, Integer> result = EmcomManagerEx.getAvailableNetInterface();
            if (result == null) {
                Log.e(TAG, "result is null");
            }
            String resultMap = Utils.parseMapToString(result).orElse("getAvailableNetInterface result is null");
            String str = "getAvailableNetInterface: " + resultMap;
            Log.i(TAG, "getAvailableNetInterface: " + str);
            MainActivity.getsMainActivity().setTextMessage(str);
            return str;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "getAvailableNetInterface, result: " + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return "";
        }
    }

    /**
     * 功能描述：应用调用该接口获取网卡的信号强度
     *
     * @param netInterfaceId 查询的网卡的名称
     * @return int
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public int getNetSignalLevel(int netInterfaceId) throws RemoteException {
        try {
            // 0：网络接口不存在，1：信号差，2：信号中等，3：信号强
            int signal = EmcomManagerEx.getNetSignalLevel(netInterfaceId);
            String str = "getNetSignalLevel，result: " + signal;
            Log.i(TAG, "getNetSignalLevel, signal:" + signal);
            MainActivity.getsMainActivity().setTextMessage(str);
            return signal;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "getNetSignalLevel, result: " + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return -1;
        }
    }

    /**
     * 功能描述：应用调用该接口将业务流全部绑定到指定网卡
     *
     * @param netIfaceId 要绑定业务流的网卡，0：wlan0，1：data0
     * @return int
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public int bindToNetInterface(int netIfaceId) throws RemoteException {
        try {
            int result = EmcomManagerEx.bindToNetInterface(netIfaceId);
            String str = "bindToNetInterface，result:" + result;
            Log.i(TAG, "bindToNetInterface, result:" + result);
            MainActivity.getsMainActivity().setTextMessage(str);
            return result;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "bindToNetInterface, result:" + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return -1;
        }
    }

    /**
     * 功能描述：应用调用该接口将业务指定的socket绑定到指定网卡
     *
     * @param fd 业务流socket
     * @param netIfaceId 要绑定业务流的网卡，0：wlan0，1：data0
     * @return int 0是调用成功
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public int bindToNetInterface(int fd, int netIfaceId) throws RemoteException {
        try {
            int result = EmcomManagerEx.bindToNetInterface(netIfaceId);
            String str = "bindToNetInterface with fd，result:" + result;
            Log.i(TAG, "bindToNetInterface with fd, result:" + result);
            MainActivity.getsMainActivity().setTextMessage(str);
            return result;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "bindToNetInterface, result:" + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
            return -1;
        }
    }

    /**
     * 功能描述：应用调用该接口将业务指定的socket绑定到指定网卡
     *
     * @param bundle 应用通告搞系统的信息内容，包括业务特征、通信指标、卡顿统计等信息
     * @throws RemoteException 远程方法调用异常
     * @since 2022-04-01
     */
    public void notifyAppInfo(Bundle bundle) throws RemoteException {
        try {
            Log.i(TAG, "notifyAppInfo:" + bundle);
            String str = "notifyAppInfo:" + bundle;
            MainActivity.getsMainActivity().setTextMessage(str);
            EmcomManagerEx.notifyAppInfo(bundle);
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            String str = "notifyAppInfo, result: " + LinkTurboKitConstants.REMOTE_EXCEPTION;
            MainActivity.getsMainActivity().setTextMessage(str);
        }
    }

    private void handleLinkTurboStateChanged(Message msg) {
        int eventId = msg.arg1;
        String str = null;
        Log.i(TAG, "handleLinkTurboKit receive evenetId:" + eventId);
        switch (msg.arg1) {
            case LinkTurboKitConstants.EVENT_LINKTURBO_REGISTER_RESULT:
                int regresult = (int) msg.obj;
                str = "LinkTurboKit Register Result:" + regresult;
                break;
            case LinkTurboKitConstants.EVENT_LINKTURBO_AVAILABLE_STATE:
                int linkTurboEableState = (int) msg.obj;
                str = "LinkTurboStateChanged currentState:" + linkTurboEableState;
                break;
        }
        Log.i(TAG, "handleLinkTurboStateChanged:" + str);
        Message notifyMsg = new Message();
        notifyMsg.what = 12;
        notifyMsg.obj = str;
        MainActivity.sUxHandler.sendMessage(notifyMsg);
    }

    private void handleFenceEvenetNotify(Message msg) {
        if (!(msg.obj instanceof Bundle)) {
            Log.e(TAG, "handleFenceEvenetNotify failed as msg is error");
            return;
        }
        int eventId = msg.arg1;
        Bundle bundle = (Bundle) msg.obj;
        int fenceValue = bundle.getInt(LinkTurboKitConstants.ACTION);
        Log.i(TAG, "handleFenceEvenetNotify, eventId:" + eventId + ", fenceValue:" + fenceValue);
        String text = "handleFenceEvenetNotify, eventId:" + eventId + ", fenceValue:" + fenceValue;
        Message notifyMsg = new Message();
        notifyMsg.what = 13;
        notifyMsg.obj = text;
        MainActivity.sUxHandler.sendMessage(notifyMsg);
    }

    private void handleQoeStateChanged(Message msg) {
        int eventId = msg.arg1;
        Bundle bundle = (Bundle) msg.obj;
        String str = Utils.parseBundleToString(bundle).orElse("handleQoeStateChanged msg is null");
        Log.i(TAG, "handleQoeStateChanged, eventId:" + eventId + ", qoeString:" + str);
        Message notifyMsg = new Message();
        notifyMsg.what = 18;
        notifyMsg.obj = str;
        MainActivity.sUxHandler.sendMessage(notifyMsg);
    }

    private void handleFlowStateChanged(Message msg) {
        int eventId = msg.arg1;
        Bundle bundle = (Bundle) msg.obj;
        String str = Utils.parseBundleToString(bundle).orElse("handleQoeStateChanged msg is null");
        Log.i(TAG, "handleQoeStateChanged, eventId:" + eventId + ", flow:" + str);
        Message notifyMsg = new Message();
        notifyMsg.what = 19;
        notifyMsg.obj = str;
        MainActivity.sUxHandler.sendMessage(notifyMsg);
    }

    private void handleNetworkStateChanged(Message msg) {
        int eventId = msg.arg1;
        Map<Integer, Integer> map = (Map) msg.obj;
        String resultMap = Utils.parseMapToString(map).orElse("handleNetworkStateChanged msg is null");
        String str = "NetworkStateChanged:" + resultMap;
        Log.i(TAG, "handleNetworkStateChanged, eventId:" + eventId + ", qoeString:" + str);
        Message notifyMsg = new Message();
        notifyMsg.what = 20;
        notifyMsg.obj = str;
        MainActivity.sUxHandler.sendMessage(notifyMsg);
    }

    private void handledefaultnetintfacechanged(Message msg) {
        int eventId = msg.arg1;
        Bundle bundle = (Bundle) msg.obj;
        int newDefNetwork = bundle.getInt("defNetIfaceId");
        String resultMap = Utils.parseBundleToString(bundle).orElse("handleQoeStateChanged msg is null");
        String str = "DefaultNetIntfaceChanged：" + resultMap;
        Log.i(TAG, "Default NetIntface Changed to" + newDefNetwork);
        Message notifyMsg = new Message();
        notifyMsg.what = 21;
        notifyMsg.obj = str;
        MainActivity.sUxHandler.sendMessage(notifyMsg);
    }

    /**
     * 功能描述：KitHandler
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
            switch (msg.what) {
                case LinkTurboKitConstants.MESSAGE_LINKTURBO_AVAILABLE_STATE_NOTIFY:
                    handleLinkTurboStateChanged(msg);
                    break;
                case LinkTurboKitConstants.MESSAGE_LAGS_PREDICTION_NOTIFY:
                    handleFenceEvenetNotify(msg);
                    break;
                case LinkTurboKitConstants.MESSAGE_QOE_INFO_NOTIFY:
                    handleQoeStateChanged(msg);
                    break;
                case LinkTurboKitConstants.MESSAGE_SOCKET_STATE_NOTIFY:
                    handleFlowStateChanged(msg);
                    break;
                case LinkTurboKitConstants.MESSAGE_AVAILABLE_NETIFACE_NOTIFY:
                    handleNetworkStateChanged(msg);
                    break;
                case LinkTurboKitConstants.MESSAGE_DEFAULT_NETIFACE_NOTIFY:
                    handledefaultnetintfacechanged(msg);
                    break;
                default:
                    Log.e(TAG, "msg is unknown, msg:" + msg.what);
                    break;
            }
        }
    }
}
