package com.example.acholyte.itnote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by acholyte on 2015-09-23.
 * 연결 상태 확인 클래스
 */
public class ConnectStatus {
    private Context mContext;

    ConnectStatus(Context context) {
        mContext = context;
    }

    // 인터넷에 연결되었는지 여부를 알려줌
    public boolean isOnLine() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // 와이파이
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 3G, LTE

        if (wifi.isConnected() || mobile.isConnected()) // 인터넷에 연결
            return true;

        return false;
    }
}
