package com.example.acholyte.itnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ShowActivity extends Activity {
    Button insert, update, delete, ok;
    TextView tvRegdate, tvIp, tvSubject, tvContent;
    EditText etSubject, etContent;

    private String mNum;
    private int pos;
    private ArrayList subjects;
    private ArrayList numbers;
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        String num = intent.getStringExtra("num");
        String subject = intent.getStringExtra("subject");
        String content = intent.getStringExtra("content");
        String regdate = intent.getStringExtra("regdate");
        String ip = intent.getStringExtra("ip");
        numbers = intent.getParcelableArrayListExtra("numbers");
        subjects = intent.getParcelableArrayListExtra("subjects");

        ConnectStatus cs = new ConnectStatus(this);

        insert = (Button) findViewById(R.id.insert);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        ok = (Button) findViewById(R.id.ok);
        tvRegdate = (TextView) findViewById(R.id.regdate);
        tvIp = (TextView) findViewById(R.id.ip);
        tvSubject = (TextView) findViewById(R.id.tvsubject);
        tvContent = (TextView) findViewById(R.id.tvcontent);
        etSubject = (EditText) findViewById(R.id.etsubject);
        etContent = (EditText) findViewById(R.id.etcontent);

        connected = cs.isOnLine();

        if (subject == null || content == null) { // ITNote 액티비티의 추가(register) 버튼을 통해 들어온 경우
            update.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
            tvRegdate.setVisibility(View.INVISIBLE);
            tvIp.setVisibility(View.INVISIBLE);
            tvSubject.setVisibility(View.INVISIBLE);
            tvContent.setVisibility(View.INVISIBLE);

            etSubject.setMinimumWidth(100 * (int) (getResources().getDisplayMetrics().density)); // 제목 입력 창의 기본 크기 지정
        } else { // 리스트뷰를 통해 들어온 경우
            insert.setVisibility(View.INVISIBLE);

            if (!connected) { // 인터넷에 연결되지 않은 경우 수정 버튼과 삭제 버튼을 보이지 않음
                update.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
            }

            mNum = num;
            etSubject.setVisibility(View.INVISIBLE);
            etContent.setVisibility(View.INVISIBLE);
        }

        ok.setVisibility(View.INVISIBLE);

        tvRegdate.setText(regdate);
        tvIp.setText(" " + ip);
        tvSubject.setText(subject);
        tvContent.setText(content);
        etSubject.setText(subject);
        etContent.setText(content);
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.insert:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.insert)
                        .setMessage(R.string.insert_desc)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String subject = etSubject.getText().toString();

                                // 이름과 제목이 빈 경우
                                if (subject.equals("")
                                        || etContent.getText().toString().equals("")) {
                                    new AlertDialog.Builder(ShowActivity.this)
                                            .setTitle(R.string.error)
                                            .setMessage(R.string.blank)
                                            .setPositiveButton(R.string.ok, null)
                                            .show();

                                    return;
                                }

                                // 같은 이름의 제목이 이미 존재
                                for (Object obj : subjects) {
                                    if (subject.equalsIgnoreCase(obj.toString())) {
                                        new AlertDialog.Builder(ShowActivity.this)
                                                .setTitle(R.string.error)
                                                .setMessage(R.string.samesub)
                                                .setPositiveButton(R.string.ok, null)
                                                .show();
                                        return;
                                    }
                                }

                                HashMap map;

                                map = new HashMap();
                                map.put("subject", etSubject.getText().toString());
                                map.put("content", etContent.getText().toString());
                                doButton(map, "insert");
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                break;

            case R.id.update:
                etSubject.setVisibility(View.VISIBLE);
                etContent.setVisibility(View.VISIBLE);
                tvSubject.setVisibility(View.INVISIBLE);
                tvContent.setVisibility(View.INVISIBLE);
                update.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
                ok.setVisibility(View.VISIBLE);

                etSubject.setMinimumWidth(100 * (int) (getResources().getDisplayMetrics().density)); // 제목 입력 창의 기본 크기 지정

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(ShowActivity.this)
                                .setTitle(R.string.update)
                                .setMessage(R.string.update_desc)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String subject = etSubject.getText().toString();

                                        // 이름과 제목의 변경이 없는 경우
                                        if (subject.equalsIgnoreCase(tvSubject.getText().toString())
                                                && etContent.getText().toString().equalsIgnoreCase(tvContent.getText().toString())) {
                                            new AlertDialog.Builder(ShowActivity.this)
                                                    .setTitle(R.string.error)
                                                    .setMessage(R.string.notchange)
                                                    .setPositiveButton(R.string.ok, null)
                                                    .show();
                                            return;
                                        }

                                        // 같은 이름의 제목이 이미 존재
                                        for (Object obj : subjects) {
                                            if (subject.equalsIgnoreCase(obj.toString())) {
                                                if (!numbers.get(subjects.indexOf(obj)).equals(mNum)) {
                                                    new AlertDialog.Builder(ShowActivity.this)
                                                            .setTitle(R.string.error)
                                                            .setMessage(R.string.samesub)
                                                            .setPositiveButton(R.string.ok, null)
                                                            .show();
                                                    return;
                                                }
                                            }
                                        }

                                        HashMap map;

                                        map = new HashMap<String, String>();
                                        map.put("num", mNum);
                                        map.put("subject", etSubject.getText().toString());
                                        map.put("content", etContent.getText().toString());
                                        doButton(map, "update");

                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .show();
                    }
                });
                break;

            case R.id.delete:
                new AlertDialog.Builder(ShowActivity.this)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_desc)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap map;

                                map = new HashMap<String, String>();
                                map.put("num", mNum);
                                doButton(map, "delete");
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                break;

            case R.id.close:
                finish();
                break;
        }
    }

    // 해당 웹에 연결하는 함수
    private void doButton(HashMap map, String page) {
        SendHandler send;
        String response = "";
        send = new SendHandler();
        response = send.init(this, map, "http://acholyte.iptime.org:8000/dictionary/" + page + ".php");

        if (response.equals("")) { // 정상적으로 서버에서 처리하여 아무런 응답이 없는 경우
            send.init(this, null, "http://acholyte.iptime.org:8000/dictionary/changeXML.php");
            Toast.makeText(this, R.string.done, Toast.LENGTH_SHORT).show();
        } else { // 그렇지 않은 경우 서버의 메시지 출력
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
