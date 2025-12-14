package com.practice.smallbee.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractActivity<VB extends ViewBinding> extends AppCompatActivity {

    protected VB binding = null;
    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            // 网络可用
            onNetworkAvailable();
        }
        @Override
        public void onLost(Network network) {
            // 网络丢失
            onNetworkLost();
        }
        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            // 网络能力变化，可在此判断是Wi-Fi还是移动网络
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                // WiFi网络
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                // 移动网络
            }
        }
    };

    protected void onNetworkLost() {

    }

    protected void onNetworkAvailable() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        SkinManager.INSTANCE.hook(this);
        super.onCreate(savedInstanceState);
        initBinding();
        initView();
        initData();
        if(isEnableNetworkCallback()) {
            registerNetworkCallback();
        }
    }

    protected void initBinding() {
        if(this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            Type[] typeArr = ((ParameterizedType) this.getClass()
                    .getGenericSuperclass()).getActualTypeArguments();
            if(typeArr.length == 0) return;
            Class<VB> bindingClass = (Class<VB>) typeArr[0];
            try {
                Method inflate = bindingClass.getDeclaredMethod("inflate", LayoutInflater.class);
                binding = (VB) inflate.invoke(null, getLayoutInflater());
                setContentView(binding.getRoot());
            } catch(Exception exception) {
            }
        }
    }

    public abstract void initView();

    public abstract void initData();

    protected boolean isEnableNetworkCallback() {
        return true;
    }
    /**
     * 允许网络检测
     *
     * @return
     */
//    protected boolean enableNetworkVerify() {
//        return true;
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        unregisterNetworkCallback();
    }

    private void unregisterNetworkCallback() {
        if(isEnableNetworkCallback()) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            } catch(Exception e) {

            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public Context getContext() {
        return this;
    }

    public void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
    }

    //是否为黑夜模式
    public boolean isNightMode() {
        int uiMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return uiMode == Configuration.UI_MODE_NIGHT_YES;
    }
}
