/*
 * Copyright (c) Honor Device Co., Ltd. 2022-2024. All rights reserved.
 */

package com.hihonor.linkturbokit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 功能描述：LinkTurbo Kit开放能力测试demo apk，测试开放的api接口调用
 *
 * @since 2022-04-01
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LinkTurboKit_Test_App_Main";
    /**
     * ux handler
     */
    public static Handler sUxHandler;

    /**
     * 用户标识符
     */
    public static int uid;

    private static MainActivity sMainActivity;

    /**
     * 滞后开始时间标记
     */
    public long tempLagBeginTimeStamp;

    private Button btnRegApp;
    private Button btnUnRegApp;
    private Button btnGetLTVersion;
    private Button btnGetLTEnState;
    private Button btnGetAvNetInf;
    private Button btnGetNetSignalLevel;
    private Button btnGetNetQoe;
    private Button btnActNetInf;
    private Button btnBindToNetInfAll;
    private Button btnBindToNetInfSokt;
    private Button btnNotifyAppInfo;
    private Button clearTextView;
    private Button btnSocketTest;
    private Button btnAccessBaidu;

    private Button TestCase;

    private Context mContext;
    private TextView txtRevMsg;
    private WebView webBaidu;

    private String BuildModel;

    private int choice = -1;

    private boolean[] checkItems;

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    public MainActivity() {
        sMainActivity = this;
    }

    public static MainActivity getsMainActivity() {
        return sMainActivity;
    }

    /**
     * 获取handler
     *
     * @return Handler 返回handler
     */
    public static Handler getUxHandler() {
        return sUxHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        bindView();
        uid = getUid();

        /* 注册广播 */
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hihonor.linkturbokit.getLinkTurboVersion");
        filter.addAction("com.hihonor.linkturbokit.registerApp");
        filter.addAction("com.hihonor.linkturbokit.undregisterApp");
        filter.addAction("com.hihonor.linkturbokit.getLinkTurboEnableState");
        filter.addAction("com.hihonor.linkturbokit.getNetworkQoE");
        filter.addAction("com.hihonor.linkturbokit.activeNetInterface");
        filter.addAction("com.hihonor.linkturbokit.getAvailableNetInterface");
        filter.addAction("com.hihonor.linkturbokit.getNetSignalLevel");
        filter.addAction("com.hihonor.linkturbokit.bindToNetInterface");
        filter.addAction("com.hihonor.linkturbokit.bindToNetInterfacefd");
        filter.addAction("com.hihonor.linkturbokit.notifyAppInfo");
        mContext.registerReceiver(new KitAppBroadReceiver(), filter);
        sUxHandler = new UxHandler(getMainLooper());
    }

    private void bindView() {
        btnRegApp = findViewById(R.id.btnRegApp);
        btnUnRegApp = findViewById(R.id.btnUnRegApp);
        btnGetLTVersion = findViewById(R.id.btnGetLTVersion);
        btnGetLTEnState = findViewById(R.id.btnGetLTEnState);
        btnGetAvNetInf = findViewById(R.id.btnGetAvNetInf);
        btnGetNetSignalLevel = findViewById(R.id.btnGetNetSignalLevel);
        btnGetNetQoe = findViewById(R.id.btnGetNetQoe);
        btnActNetInf = findViewById(R.id.btnActNetInf);
        btnBindToNetInfAll = findViewById(R.id.btnBindToNetInfAll);
        btnBindToNetInfSokt = findViewById(R.id.btnBindToNetInfSokt);
        btnNotifyAppInfo = findViewById(R.id.btnNotifyAppInfo);
        clearTextView = findViewById(R.id.clear_text);
        txtRevMsg = findViewById(R.id.RepMsgView);
        TestCase = findViewById(R.id.TestCase);
        btnRegApp.setOnClickListener(this);
        btnUnRegApp.setOnClickListener(this);
        btnGetLTVersion.setOnClickListener(this);
        btnGetLTEnState.setOnClickListener(this);
        btnGetAvNetInf.setOnClickListener(this);
        btnGetNetSignalLevel.setOnClickListener(this);
        btnGetNetQoe.setOnClickListener(this);
        btnActNetInf.setOnClickListener(this);
        btnBindToNetInfAll.setOnClickListener(this);
        btnBindToNetInfSokt.setOnClickListener(this);
        btnNotifyAppInfo.setOnClickListener(this);
        clearTextView.setOnClickListener(this);
        TestCase.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 多选列表对话框
            case R.id.btnRegApp:
                regApp();
                break;
            case R.id.btnUnRegApp:
                unRegApp();
                break;
            case R.id.btnGetLTVersion:
                getLTVersion();
                break;
            case R.id.btnGetLTEnState:
                getLTEnState();
                break;
            case R.id.btnGetAvNetInf:
                getAvNetInf();
                break;
            case R.id.btnGetNetSignalLevel:
                getNetSignalLevel();
                break;
            case R.id.btnGetNetQoe:
                getNetQoe();
                break;
            case R.id.btnActNetInf:
                actNetInf();
                break;
            case R.id.btnBindToNetInfAll:
                bindToNetInfAll();
                break;
            case R.id.btnBindToNetInfSokt:
                bindToNetInfSokt();
                break;
            case R.id.btnNotifyAppInfo:
                notifyAppInfo();
                break;
            case R.id.clear_text:
                clearTextMessage();
                break;
            case R.id.TestCase:
                BuildModel = Build.MODEL;
                setTextMessage(BuildModel);
                break;
        }
    }

    private void notifyAppInfo() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入通告的app信息");
        View subNotifyAppInfo = LayoutInflater.from(this)
            .inflate(R.layout.activity_subnotifyappinfo, null);

        // 获得布局信息
        final EditText sceneInfo = subNotifyAppInfo.findViewById(R.id.scene_info);
        final EditText sceneStatusInfo = subNotifyAppInfo.findViewById(R.id.scene_status_info);
        final EditText sceneKfdInfo = subNotifyAppInfo.findViewById(R.id.scene_kfd_info);
        final EditText sceneExtraInfo = subNotifyAppInfo.findViewById(R.id.scene_extra_info);
        final EditText msgTypeInfo = subNotifyAppInfo.findViewById(R.id.msg_type_info);
        final EditText lagbeginInfo = subNotifyAppInfo.findViewById(R.id.lagBegin_info);
        final EditText lagEndInfo = subNotifyAppInfo.findViewById(R.id.lagEnd_info);
        final EditText rateInfo = subNotifyAppInfo.findViewById(R.id.rate_info);
        final EditText latreqInfo = subNotifyAppInfo.findViewById(R.id.latreq_info);
        builder.setView(subNotifyAppInfo);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int scene = 0;
                int sceneStatus = 0;
                int sceneKfd = 0;
                int sceneExtra = 0;
                if (sceneInfo.length() != 0) {
                    scene = Integer.parseInt(sceneInfo.getText().toString());
                }
                if (sceneStatusInfo.length() != 0) {
                    sceneStatus = Integer.parseInt(sceneStatusInfo.getText().toString());
                }
                if (sceneKfdInfo.length() != 0) {
                    sceneKfd = Integer.parseInt(sceneKfdInfo.getText().toString());
                }
                if (sceneExtraInfo.length() != 0) {
                    sceneExtra = Integer.parseInt(sceneExtraInfo.getText().toString());
                }
                int msgType = 0;
                if (msgTypeInfo.length() != 0) {
                    msgType = Integer.parseInt(msgTypeInfo.getText().toString());
                }
                long lagBeginTimeStamp = 0;
                if (lagbeginInfo.length() != 0) {
                    lagBeginTimeStamp = System.currentTimeMillis();
                    tempLagBeginTimeStamp = lagBeginTimeStamp;
                }
                long lagEndTimeStamp = 0;
                if (lagEndInfo.length() != 0) {
                    lagBeginTimeStamp = tempLagBeginTimeStamp;
                    lagEndTimeStamp = System.currentTimeMillis();
                    tempLagBeginTimeStamp = 0;
                }
                int rate = 0;
                if (rateInfo.length() != 0) {
                    rate = Integer.parseInt(rateInfo.getText().toString());
                }
                int latreq = 0;
                if (latreqInfo.length() != 0) {
                    latreq = Integer.parseInt(latreqInfo.getText().toString());
                }
                try {
                    Bundle bundle = new Bundle();
                    if (scene != 0) {
                        bundle.putInt("scene", scene);
                    }
                    if (sceneStatus != 0) {
                        bundle.putInt("status", sceneStatus);
                    }
                    if (sceneKfd != 0) {
                        bundle.putInt("KFD", sceneKfd);
                    }
                    if (sceneExtra != 0) {
                        bundle.putString("extra", "{" + "\"eventType\": 1," + "\"accPkgName\":" +
                                " \"com.tencent.mm\"" + "}");
                    }
                    if (msgType != 0) {
                        bundle.putInt("msgType", msgType);
                    }
                    if (lagBeginTimeStamp != 0) {
                        bundle.putLong("lagBegin", lagBeginTimeStamp);
                    }
                    if (lagEndTimeStamp != 0) {
                        bundle.putLong("lagEnd", lagEndTimeStamp);
                    }
                    if (rate != 0) {
                        bundle.putInt("rate", rate);
                    }
                    if (latreq != 0) {
                        bundle.putInt("LATREQ", latreq);
                    }

                    LinkTurboKitProxy.getInstance().notifyAppInfo(bundle);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private void bindToNetInfSokt() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入绑定业务的fd和网卡");
        View bindintfacefdsubview = LayoutInflater.from(this)
            .inflate(R.layout.activity_subbindintfaceidfd, null);

        // 获得布局信息
        final EditText bindfd = bindintfacefdsubview.findViewById(R.id.bindfd);
        final EditText bindNetIfaceId = bindintfacefdsubview.findViewById(R.id.bindNetIfaceId);
        builder.setView(bindintfacefdsubview);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int bindinfFd = 0;
                int bindinfNetIfaceId = 1;

                if (bindfd.length() != 0) {
                    bindinfFd = Integer.parseInt(bindfd.getText().toString());
                }
                if (bindNetIfaceId.length() != 0) {
                    bindinfNetIfaceId = Integer.parseInt(bindNetIfaceId.getText().toString());
                }
                try {
                    LinkTurboKitProxy.getInstance().bindToNetInterface(bindinfFd, bindinfNetIfaceId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private void bindToNetInfAll() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入绑定业务的网卡");
        View bindintfacesubview = LayoutInflater
            .from(this)
            .inflate(R.layout.activity_subbindintfaceid, null);

        // 获得布局信息
        final EditText bindnetifaceid = bindintfacesubview.findViewById(R.id.bindNetIfaceId);
        builder.setView(bindintfacesubview);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int bindNetInterfaceId = 1;
                if (bindnetifaceid.length() != 0) {
                    bindNetInterfaceId = Integer.parseInt(bindnetifaceid.getText().toString());
                }
                try {
                    LinkTurboKitProxy.getInstance().bindToNetInterface(bindNetInterfaceId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private void actNetInf() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入查询的网卡");
        View actintfacesubview = LayoutInflater
            .from(this)
            .inflate(R.layout.activity_subintfaceid, null);

        // 获得布局信息
        final EditText actnetifaceid = actintfacesubview.findViewById(R.id.NetIfaceId);
        builder.setView(actintfacesubview);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int actNetInterfaceId = 0;
                if (actnetifaceid.length() != 0) {
                    actNetInterfaceId = Integer.parseInt(actnetifaceid.getText().toString());
                }
                try {
                    LinkTurboKitProxy.getInstance().activeNetInterface(actNetInterfaceId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private static void getNetQoe() {
        try {
            LinkTurboKitProxy.getInstance().getNetworkQoE(uid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void getNetSignalLevel() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入查询的网卡");
        View intfacesubview = LayoutInflater
            .from(this)
            .inflate(R.layout.activity_subintfaceid, null);

        // 获得布局信息
        final EditText netifaceid = intfacesubview.findViewById(R.id.NetIfaceId);
        builder.setView(intfacesubview);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int netInterfaceId = 0;
                if (netifaceid.length() != 0) {
                    netInterfaceId = Integer.parseInt(netifaceid.getText().toString());
                }
                try {
                    LinkTurboKitProxy.getInstance().getNetSignalLevel(netInterfaceId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private static void getAvNetInf() {
        try {
            LinkTurboKitProxy.getInstance().getAvailableNetInterface();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void getLTEnState() {
        try {
            LinkTurboKitProxy.getInstance().getLinkTurboEnableState(uid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void getLTVersion() {
        try {
            LinkTurboKitProxy.getInstance().getLinkTurboVersion();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void unRegApp() {
        try {
            LinkTurboKitProxy.getInstance().undregisterApp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void regApp() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("输入注册能力参数");
        View view = LayoutInflater.from(this).inflate(R.layout.activity_subability, null);

        // 获得布局信息
        final EditText subability = view.findViewById(R.id.SubAbility);
        final EditText subabilitymode = view.findViewById(R.id.SubAbilityMode);
        final EditText sceneid = view.findViewById(R.id.SceneID);
        builder.setView(view);
        builder.setPositiveButton("注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int capabilities = 0;
                int collaborativeMode = 4;
                int collaborativeScene = 0x400;

                if (subability.length() != 0) {
                    capabilities = Integer.parseInt(subability.getText().toString());
                }
                if (subabilitymode.length() != 0) {
                    collaborativeMode = Integer.parseInt(subabilitymode.getText().toString());
                }
                if (sceneid.length() != 0) {
                    collaborativeScene = Integer.parseInt(sceneid.getText().toString());
                }
                try {
                    LinkTurboKitProxy.getInstance()
                        .registerApp(capabilities, collaborativeMode, collaborativeScene);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    /**
     * 功能描述：将调用接口，返回值打印在界面textView中
     *
     * @param message 接口返回值信息
     * @since 2022-04-01
     */
    public void setTextMessage(String message) {
        TextView textView = this.txtRevMsg;
        if (txtRevMsg != null) {
            CharSequence str = textView.getText();
            str = str + System.lineSeparator() + message;
            textView.setText(str);
        }
    }

    /**
     * 功能描述：清除textView显示信息
     *
     * @since 2022-04-18
     */
    public void clearTextMessage() {
        TextView textView = this.txtRevMsg;
        if (textView != null) {
            textView.setText("");
        }
    }

    /**
     * 功能描述：获取应用Uid信息
     *
     * @return int uid信息
     * @since 2022-04-18
     */
    public int getUid() {
        try {
            PackageManager pm = mContext.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo("com.hihonor.linkturbokit", PackageManager.GET_META_DATA);
            return ai.uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "get uid failed");
            return -1;
        }
    }

    /**
     * 功能描述：获取UxHandler
     */
    public class UxHandler extends Handler {
        UxHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                Log.e(TAG, "handleMessage msg is illegal");
                return;
            }
            if (msg.obj instanceof String) {
                String str = (String) msg.obj;
            }
            switch (msg.what) {
                default:
                    setTextMessage(msg.obj.toString());
                    break;
            }
        }
    }
}