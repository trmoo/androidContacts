package com.example.homework04;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText name, alias, phone;
    private ListView list;

    private Button search, erase, add, update, delete;

    private MyDBHelper helper;
    private SQLiteDatabase db;

    private ArrayList<MyData> data;
    private MyAdapter adapter;

    private String[] projection = new String[]{"_id", "name", "alias", "phone"};
    private int position = AdapterView.INVALID_POSITION; // 선택 항목이 없는 것으로 초기화
    int index = 0;
    Cursor c;

    InputMethodManager inputMethodManager;
    boolean delete_enabled = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        name = findViewById(R.id.name);
        alias = findViewById(R.id.alias);
        phone = findViewById(R.id.phone);
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("KR"));

        name.setSelection(name.getText().length());
        alias.setSelection(alias.getText().length());
        phone.setSelection(phone.getText().length());

        list = findViewById(R.id.list);
        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        search = findViewById(R.id.search);
        erase = findViewById(R.id.erase);
        add = findViewById(R.id.add);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);

        search.setEnabled(false);
        erase.setEnabled(false);
        add.setEnabled(false);
        update.setEnabled(false);
        delete.setEnabled(false);

        helper = new MyDBHelper(this);
        data = new ArrayList<MyData>();

        getAllData(); // 앱 실행 시 자동으로 DB에서 주소록 가져다가 화면에 보여줌

        adapter = new MyAdapter(this, data);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = list.getCheckedItemPosition();
                index = position;

                search.setEnabled(true);
                erase.setEnabled(true);
                delete_enabled = true;
                delete.setEnabled(true);
                MyData m1 = (MyData) adapter.getItem(i); // 어댑터를 사용하면 현재 데이터를 가져올 수 있음
                String s1 = m1.getName();
                String s2 = ((MyData) adapter.getItem(i)).getAlias();
                String s3 = ((MyData) adapter.getItem(i)).getPhone();
                name.setText(s1);
                alias.setText(s2);
                phone.setText(s3);
            }
        });


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (name.getText().toString().length() != 0) {
                    search.setEnabled(true);
                    erase.setEnabled(true);
                    if (phone.getText().toString().length() != 0) {
                        add.setEnabled(true);
                        if (position != AdapterView.INVALID_POSITION) {
                            update.setEnabled(true);
                        }
                    }
                } else {
                    search.setEnabled(false);
                    add.setEnabled(false);
                    if (phone.getText().toString().length() == 0 && alias.getText().toString().length() == 0)
                        erase.setEnabled(false);
                    if (phone.getText().toString().length() == 0) {
                        add.setEnabled(false);}
                }

                if (delete_enabled == true) {
                    if (!(name.getText().toString().equals(((MyData) adapter.getItem(index)).getName()))) {
                        delete_enabled = false;
                        delete.setEnabled(false);
                        add.setEnabled(true);
                    } else if (name.getText().toString().equals(((MyData) adapter.getItem(index)).getName())) {
                        add.setEnabled(false);
                        update.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (phone.getText().toString().length() != 0) {
                    erase.setEnabled(true);
                    if (name.getText().toString().length() != 0) {
                        add.setEnabled(true);
                        if (position != AdapterView.INVALID_POSITION) {
                            update.setEnabled(true);
                        }
                    }
                } else {
                    add.setEnabled(false);
                    if (name.getText().toString().length() == 0 && alias.getText().toString().length() == 0)
                        erase.setEnabled(false);
                    if (name.getText().toString().length() == 0) {
                        add.setEnabled(false);
                    }

                    if (delete_enabled == true) {
                        if (!(phone.getText().toString().equals(((MyData) adapter.getItem(index)).getPhone()))) {
                            delete_enabled = false;
                            delete.setEnabled(false);
                            add.setEnabled(true);
                        } else if((phone.getText().toString().equals(((MyData) adapter.getItem(index)).getPhone()))) {
                            add.setEnabled(false);
                            update.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        alias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (alias.getText().toString().length() != 0) {
                    erase.setEnabled(true);
                    if (name.getText().toString().length() != 0 && phone.getText().toString().length() != 0) {
                        add.setEnabled(true);
                        if (position != AdapterView.INVALID_POSITION) {
                            update.setEnabled(true);
                        }
                    }
                } else {
                    if (name.getText().toString().length() == 0 && phone.getText().toString().length() == 0) {
                        erase.setEnabled(false);
                    }
                }

                if (name.getText().toString().length() == 0) {
                    search.setEnabled(false);
                }

                if (delete_enabled == true) {
                    if (!(alias.getText().toString().equals(((MyData) adapter.getItem(index)).getAlias()))) {
                        delete_enabled = false;
                        delete.setEnabled(false);
                        add.setEnabled(true);
                    } else if((alias.getText().toString().equals(((MyData) adapter.getItem(index)).getAlias()))) {
                        add.setEnabled(false);
                        update.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onClick(View view) {
        inputMethodManager.hideSoftInputFromWindow(name.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(alias.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(phone.getWindowToken(), 0);


        switch (view.getId()) {
            case (R.id.dial):
                String tel = "tel:" + phone.getText().toString();
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                name.setText("");
                alias.setText("");
                phone.setText("");

                search.setEnabled(false);
                erase.setEnabled(false);
                add.setEnabled(false);
                update.setEnabled(false);
                delete.setEnabled(false);

                delete_enabled = false;

                position = AdapterView.INVALID_POSITION;
                finish();

                break;


            case (R.id.showAll):
                getAllData();
                adapter.notifyDataSetChanged();

                position = AdapterView.INVALID_POSITION;
                break;


            case (R.id.search):
                alias.setText("");
                phone.setText("");
                add.setEnabled(false);
                update.setEnabled(false);
                delete.setEnabled(false);

                boolean find_s = false;
                db = helper.getReadableDatabase();
                c = db.query("people", projection, null, null, null, null, "name, alias");
                data.clear();
                while (c.moveToNext()) {
                    if (c.getString(c.getColumnIndex("name")).toString().equals(name.getText().toString())) {
                        find_s = true;
                        MyData item = new MyData(c.getString(1), c.getString(2), c.getString(3));
                        data.add(item);
                    }
                }
                if (find_s == false) {
                    Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyDataSetChanged();
                }

                c.close();
                db.close();

                position = AdapterView.INVALID_POSITION;
                break;


            case (R.id.erase):
                name.setText("");
                alias.setText("");
                phone.setText("");

                search.setEnabled(false);
                erase.setEnabled(false);
                add.setEnabled(false);
                update.setEnabled(false);
                delete.setEnabled(false);

                position = AdapterView.INVALID_POSITION;
                break;


            case (R.id.add):
                if (name.getText().toString().length() == 0 || phone.getText().toString().length() == 0) {
                    Toast.makeText(this, "이름과 전화번호는 있어야 합니다.", Toast.LENGTH_SHORT).show();
                    break;
                }

                boolean find_a = false;
                db = helper.getWritableDatabase();
                c = db.query("people", projection, null, null, null, null, null);
                while (c.moveToNext()) {
                    if (c.getString(1).equals(name.getText().toString()) &&
                            c.getString(2).equals(alias.getText().toString()) &&
                            c.getString(3).equals(phone.getText().toString())) {
                        find_a = true;
                        break;
                    }
                }
                c.close();

                if (find_a == false) {
                    ContentValues values;
                    values = new ContentValues();
                    values.put("name", name.getText().toString());
                    values.put("alias", alias.getText().toString());
                    values.put("phone", phone.getText().toString());
                    db.insert("people", null, values);
                    name.setText("");
                    alias.setText("");
                    phone.setText("");
                    update.setEnabled(false);
                    search.setEnabled(false);
                    erase.setEnabled(false);
                    db.close();

                    adapter.notifyDataSetChanged();
                    getAllData();
                    add.setEnabled(false);
                } else {
                    Toast.makeText(this, "내용이 모두 같은 항목은 추가하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

                position = AdapterView.INVALID_POSITION;
                break;


            case (R.id.update):
                if (name.getText().toString().length() == 0 || phone.getText().toString().length() == 0) {
                    Toast.makeText(this, "이름과 전화번호는 있어야 합니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                boolean find_u = false;
                db = helper.getWritableDatabase();
                c = db.query("people", projection, null, null, null, null, null);
                while (c.moveToNext()) {
                    if (c.getString(1).equals(name.getText().toString()) &&
                            c.getString(2).equals(alias.getText().toString()) &&
                            c.getString(3).equals(phone.getText().toString())) {
                        find_u = true;
                        break;
                    }
                }
                c.close();

                if (find_u == false) {
                    ContentValues values = new ContentValues();
                    values.put("name", name.getText().toString());
                    values.put("alias", alias.getText().toString());
                    values.put("phone", phone.getText().toString());

                    db.update("people", values, "name=? and alias=? and phone=?",
                            new String[]{((MyData) adapter.getItem(index)).getName(),
                                    ((MyData) adapter.getItem(index)).getAlias(), ((MyData) adapter.getItem(index)).getPhone()});

                    name.setText("");
                    alias.setText("");
                    phone.setText("");
                    adapter.notifyDataSetChanged();
                    getAllData();
                    db.close();
                    search.setEnabled(false);
                    erase.setEnabled(false);
                    add.setEnabled(false);
                    update.setEnabled(false);

                    position = AdapterView.INVALID_POSITION;
                } else {
                    Toast.makeText(this, "같은 내용의 항목이 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                }


                break;


            case (R.id.delete):
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("삭제 확인");
                builder.setMessage("정말로 삭제하시겠습니까?\n");
                builder.setCancelable(false);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db = helper.getWritableDatabase();
                        db.delete("people", "name=? and alias=? and phone=?",
                                new String[]{((MyData) adapter.getItem(index)).getName(),
                                        ((MyData) adapter.getItem(index)).getAlias(), ((MyData) adapter.getItem(index)).getPhone()});
                        name.setText("");
                        alias.setText("");
                        phone.setText("");
                        search.setEnabled(false);
                        erase.setEnabled(false);
                        add.setEnabled(false);
                        update.setEnabled(false);
                        position = AdapterView.INVALID_POSITION;
                        db.close();
                        getAllData();
                        adapter.notifyDataSetChanged();
                        delete.setEnabled(false);
                    }
                });
                builder.setNegativeButton("아니오", null);
                builder.create().show();
                position = AdapterView.INVALID_POSITION;

                break;
        }
        helper.close();
    }


    public void getAllData() {
        db = helper.getReadableDatabase();
        c = db.query("people", projection, null, null, null, null, "name, alias");
        data.clear(); // 커서를 직접 이용하지 않고 ArrayList를 이용
        while (c.moveToNext()) {
            MyData item = new MyData(c.getString(1), c.getString(2), c.getString(3));
            data.add(item);
        }
        c.close();
    }
}
