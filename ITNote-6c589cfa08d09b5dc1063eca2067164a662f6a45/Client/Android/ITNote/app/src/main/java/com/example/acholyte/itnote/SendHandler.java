package com.example.acholyte.itnote;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by acholyte on 2015-09-20.
 * 매개변수를 DB서버에 보내고 그 결과를 받는 클래스
 */
public class SendHandler {
    private ProgressDialog mProgress;

    String mResult = ""; // 외부로 보낼 결과값을 저장하는 멤버 변수

    // 서버에서 결과를 읽어들여 외부로 반환하는 함수
    public String init(Context context, HashMap<String, String> map, String addr) {
        mProgress = ProgressDialog.show(context, context.getString(R.string.doing), context.getString(R.string.wait));
        SendThread thread = new SendThread(map, addr);
        thread.start();
        while (thread.getState() != Thread.State.TERMINATED) ; // 쓰레드의 작업이 끝날 때까지 대기
        return mResult;
    }

    // DB 서버에 연결하기 위한 Thread 클래스
    class SendThread extends Thread {
        private HashMap<String, String> mMap; // 매개변수를 담는 HashMap 변수
        private String mAddr; // 서버 주소

        SendThread(HashMap<String, String> map, String addr) {
            mMap = map;
            mAddr = addr;
        }

        @Override
        public void run() {
            String result = send(mMap, mAddr);
            mResult = result;
            Message msg = mHandler.obtainMessage();
            mHandler.sendMessage(msg);
        }
    }

    // 매개변수를 DB 서버로 보내고 그 결과를 반환하는 함수
    // 참고: http://stackoverflow.com/questions/9767952/how-to-add-parameters-to-httpurlconnection-using-post
    private String send(HashMap<String, String> map, String addr) {
        String response = ""; // DB 서버의 응답을 담는 변수

        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 해당 URL에 연결

            conn.setConnectTimeout(1000); // 타임아웃: 1초
            conn.setUseCaches(false); // 캐시 사용 안 함
            conn.setRequestMethod("POST"); // POST로 연결

            if (map != null) { // DB 서버로 보낼 매개변수가 있는 경우
                OutputStream os = conn.getOutputStream(); // 서버로 보내기 위한 출력 스트림
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8")); // UTF-8로 전송
                bw.write(getPostString(map)); // 매개변수 전송
                bw.flush();
                bw.close();
                os.close();
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { // 연결에 성공한 경우
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream())); // 서버의 응답을 읽기 위한 입력 스트림

                while ((line = br.readLine()) != null) // 서버의 응답을 읽어옴
                    response += line;
            } else { // 실패 시 에러 메시지 반환
                response = conn.getResponseMessage();
            }

            conn.disconnect();
        } catch (MalformedURLException me) {
            me.printStackTrace();
            return me.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return response;
    }

    // 매개변수를 URL에 붙이는 함수
    // 참고: http://stackoverflow.com/questions/9767952/how-to-add-parameters-to-httpurlconnection-using-post
    private String getPostString(HashMap<String, String> map) {
        StringBuilder result = new StringBuilder();
        boolean first = true; // 첫 번째 매개변수 여부

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (first)
                first = false;
            else // 첫 번째 매개변수가 아닌 경우엔 앞에 &를 붙임
                result.append("&");

            try { // UTF-8로 주소에 키와 값을 붙임
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException ue) {
                ue.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgress.dismiss();
        }
    };

}
