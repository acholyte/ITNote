package com.example.acholyte.itnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ITNote extends Activity {
    EditText text;
    RadioGroup checker;
    Button search, register, exit;
    ListView list;

    private String selector;
    private boolean connected;

    private HashMap map;
    private ArrayList subjects;
    private ArrayList numbers;

    private ConnectStatus cs = new ConnectStatus(ITNote.this); // 연결 상태 확인 객체
    private DBHelper helper = new DBHelper(this); // SQLite 기록을 위한 객체
    private Parcelable state; // 리스트뷰 상태

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_it_note);

        text = (EditText) findViewById(R.id.text);
        checker = (RadioGroup) findViewById(R.id.checker);
        search = (Button) findViewById(R.id.search);
        register = (Button) findViewById(R.id.register);
        exit = (Button) findViewById(R.id.exit);
        list = (ListView) findViewById(R.id.lists);

        connected = cs.isOnLine();
        map = setDisplay(this, connected, helper);

        buttonVisible(map, register);
        numbers = setList(map, 0);
        subjects = setListView(map, 1, list, mItemClickListener);
        checker.setOnCheckedChangeListener(mRadioCheck);

        if (!connected) {
            failDialog();
        }
    }

    @Override
    protected void onPause() {
        state = list.onSaveInstanceState();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cs.isOnLine() != connected) {
            connected = cs.isOnLine();

            if (!connected) {
                failDialog();
            }
        }

        map = setDisplay(this, connected, helper);

        buttonVisible(map, register);
        numbers = setList(map, 0);
        subjects = setListView(map, 1, list, mItemClickListener);
        if (state != null)
            list.onRestoreInstanceState(state);
        checker.setOnCheckedChangeListener(mRadioCheck);
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String[] data = (String[]) map.get(position);

            Intent intent = new Intent(ITNote.this, ShowActivity.class);
            intent.putExtra("num", data[0]);
            intent.putExtra("subject", data[1]);
            intent.putExtra("content", data[2]);
            intent.putExtra("regdate", data[3]);
            intent.putExtra("ip", data[4]);
            intent.putExtra("pos", position);
            intent.putParcelableArrayListExtra("numbers", numbers);
            intent.putParcelableArrayListExtra("subjects", subjects);
            startActivity(intent);
        }
    };

    RadioGroup.OnCheckedChangeListener mRadioCheck = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group.getId() == R.id.checker) {
                switch (checkedId) {
                    case R.id.setsub:
                        selector = "subject";
                        break;

                    case R.id.setcon:
                        selector = "content";
                        break;
                }
            }
        }
    };

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                map = helper.dbRead(selector, text.getText().toString());
                setListView(map, 1, list, mItemClickListener);
                break;

            case R.id.register:
                Intent intent = new Intent(ITNote.this, ShowActivity.class);
                intent.putParcelableArrayListExtra("subjects", subjects);
                startActivity(intent);
                break;

            case R.id.exit:
                finish();
                break;
        }
    }

    // 연결 실패 시 띄우는 대화 상자
   public void failDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.connect_fail)
                .setMessage(R.string.connect_fail_desc)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    // 리스트에 맵의 원소들을 등록
    private ArrayList setList(HashMap map, int n) {
        ArrayList arr = new ArrayList();

        for (int i = 0; i < map.size(); i++) {
            String[] data = (String[]) map.get(i);
            arr.add(data[n]);
        }

        return arr;
    }

    // 리스트뷰에 맵의 원소들을 등록
    private ArrayList setListView(HashMap map, int n, ListView list, AdapterView.OnItemClickListener listener) {
        ArrayList arr = new ArrayList();

        for (int i = 0; i < map.size(); i++) {
            String[] data = (String[]) map.get(i);
            arr.add(data[n]);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arr);
        list.setAdapter(adapter);
        list.setOnItemClickListener(listener);

        return arr;
    }

    // DB의 내용을 읽어와 액티비티에 뿌리는 함수
    private HashMap setDisplay(Context context, boolean connected, DBHelper helper) {
        HashMap map;

        if (connected) { // 인터넷에 연결된 경우
            SendHandler send = new SendHandler();
            String latest =
                    send.init(context, null, "http://acholyte.iptime.org:8000/dictionary/notify.php");

            if (latest.equals(helper.getLatestRecordTime())) { // 서버 버전과 로컬 버전이 차이가 없는 경우
                map = helper.dbRead();
            } else { // 서버 버전과 로컬 버전이 차이가 있는 경우
                XMLHandler xml = new XMLHandler();
                map = xml.init(context);

                if (!map.isEmpty()) { // 서버에 정상적으로 연결된 경우에는 로컬 DB를 업데이트 함
                    helper.dbWrite(map);
                } else { // 서버에 정상적으로 연결되지 않은 경우
                    Toast.makeText(context, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                    map = helper.dbRead();
                }
            }
        } else { // 인터넷에 연결되지 않은 경우
            map = helper.dbRead();
        }

        return map;
    }

    // 버튼의 투명 여부를 설정하는 함수
    private void buttonVisible(HashMap map, Button b) {
        if (!connected || map.isEmpty())
            b.setVisibility(View.INVISIBLE);
        else
            b.setVisibility(View.VISIBLE);
    }
}
