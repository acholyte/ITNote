package com.example.acholyte.itnote;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by acholyte on 2015-09-13.
 * XML을 읽어오는 클래스
 */
public class XMLHandler {
    private ProgressDialog mProgress;
    private HashMap mMap = new HashMap(); // XML을 파싱한 결과를 담아 외부로 보내기 위한 HashMap 변수

    // XML을 파싱한 결과를 외부로 반환하는 함수
    public HashMap init(Context context) {
        mProgress = ProgressDialog.show(context, context.getString(R.string.doing), context.getString(R.string.wait));
        GetXMLThread thread = new GetXMLThread("http://acholyte.iptime.org:8000/dictionary/dicXML.xml");
        thread.start();
        while (thread.getState() != Thread.State.TERMINATED) ; // 쓰레드의 작업이 끝날 때까지 대기
        return mMap;
    }

    class GetXMLThread extends Thread {
        private String mAddr; // 웹 주소

        GetXMLThread(String addr) {
            mAddr = addr;
        }

        @Override
        public void run() {
            String result = getXML(mAddr);

            if (result != null) {
                mMap = parse(result);
            }

            Message msg = mHandler.obtainMessage();
            mHandler.sendMessage(msg);
        }
    }

    // XML을 읽어오는 함수
    private String getXML(String addr) {
        StringBuilder xml = new StringBuilder();

        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 해당 URL에 연결

            conn.setConnectTimeout(1000); // 타임아웃: 1초
            conn.setUseCaches(false); // 캐시 사용 안 함
            conn.setRequestMethod("POST"); // POST로 연결

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { // 연결에 성공한 경우
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream())); // XML을 읽기 위한 입력 스트림

                while ((line = br.readLine()) != null) {
                    xml.append(line);
                }
                br.close();
            }

            conn.disconnect();
        } catch (NetworkOnMainThreadException ne) {
            ne.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return xml.toString();
    }

    // 읽어온 XML을 파싱하는 함수
   private HashMap parse(String xml) {
        HashMap map = new HashMap();

        if (xml != null) { // XML을 성공적으로 읽어온 경우
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder(); // 새 문서를 만듦
                InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8")); // UTF-8로 읽어옴
                Document doc = builder.parse(is);

                Element root = doc.getDocumentElement(); // XML의 최상위 원소로 이동
                NodeList items = root.getElementsByTagName("item"); // 원소 이름이 item인 것을 뽑아 List로 만듦

                for (int i = 0; i < items.getLength(); i++) { // List의 모든 원소의 키와 값을 읽어들임
                    Node item = items.item(i);
                    String[] arrData = new String[5];
                    Node text = item.getFirstChild(); // Num의 값을 읽어옴
                    arrData[0] = text.getTextContent();
                    text = text.getNextSibling(); // Subject의 값을 읽어옴
                    arrData[1] = text.getTextContent();
                    text = text.getNextSibling(); // Content의 값을 읽어옴
                    arrData[2] = text.getTextContent();
                    text = text.getNextSibling(); // Register Date의 값을 읽어옴
                    arrData[3] = text.getTextContent();
                    text = text.getNextSibling(); // IP의 값을 읽어옴
                    arrData[4] = text.getTextContent();

                    map.put(i, arrData); // 파싱 결과 저장
                }

            } catch (ParserConfigurationException pe) {
                pe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgress.dismiss();
        }
    };
}
