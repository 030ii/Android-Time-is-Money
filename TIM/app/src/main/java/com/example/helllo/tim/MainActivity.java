package com.example.helllo.tim;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    TodayFragment todayFragment;
    RecordFragment fragment2;
    QuestFragment fragment3;
    Button button;
    SQLiteDatabase database;
    String sql; // sql 쿼리문
    final int REQUEST_CODE = 1001; // 타이머 엑티비티


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 상태 바 제거 */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        todayFragment = new TodayFragment();
        fragment2 = new RecordFragment();
        fragment3 = new QuestFragment();


        openDatabase("Tim.db");
        sql = "delete from work";
        database.execSQL(sql);
        sql = "delete from quest";
        database.execSQL(sql);



        insertWork("17-06-07", 3000);
        insertWork("17-06-10", 14400);
        insertWork("17-06-15", 10000);
        insertWork("17-06-18", 7200);
        insertWork("17-06-25", 1800);
        insertWork("17-06-27", 28800);
        insertWork("17-06-27", 65646);


        insertQuest(1, "첫 출석");
        insertQuest(2, "출석 3회");
        insertQuest(101, "집중 1회 달성");
        insertQuest(102, "집중 3회 달성");
        insertQuest(201, "누적 10시간 달성");
        insertQuest(202, "누적 30시간 달성");
        insertQuest(301, "코인 10개 달성");
        insertQuest(302, "코인 30개 달성");



        getSupportFragmentManager().beginTransaction().add(R.id.container, todayFragment).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("오늘 기록"));
        tabs.addTab(tabs.newTab().setText("전체 기록"));
        tabs.addTab(tabs.newTab().setText("업적"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = todayFragment;
                } else if (position == 1) {
                    selected = fragment2;
                } else if (position == 2) {
                    selected = fragment3;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 다른 탭 눌렀을 때
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 현재 탭 눌렀을 때
            }
        });
    }

    public void onTimerStartClicked (View v){
        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // DB 오픈 메소드
    public void openDatabase(String databaseName) {
        DatabaseHelper helper = new DatabaseHelper(this, databaseName, null, 1);
        database = helper.getWritableDatabase();
    }

    // 테이블 생성 메소드
    public void createTable(String tableName) {
        if (database != null) {
            if (tableName == "work") {
                sql = "create table " + tableName + "(w_id integer PRIMARY KEY autoincrement, w_date date, w_time integer)";
                database.execSQL(sql);
            } else if (tableName == "quest") {
                sql = "create table " + tableName + "(q_id integer PRIMARY KEY autoincrement, q_number integer unique, q_name text)";
                database.execSQL(sql);
            }
        }
    }

    // work 테이블 입력 메소드
    public void insertWork(String date, int time) {
        String sql = "insert into work (w_date, w_time) values(?, ?);";

        Object[] params = {date, time};

        database.execSQL(sql, params);
    }

    // quest 테이블 입력 메소드
    public void insertQuest(int number, String name) {
        String sql = "insert into quest (q_number, q_name) values(?, ?);";

        Object[] params = {number, name};

        database.execSQL(sql, params);
    }


    // 레코드 조회 메소드
    public void selectData(String tableName) {

        String sql;
        Cursor cursor;

        if (tableName == "work") {
            sql = "select w_id, w_date, w_time  from " + tableName;
            cursor = database.rawQuery(sql, null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                int w_id = cursor.getInt(0);
                String w_date = cursor.getString(1);
                int w_time = cursor.getInt(2);

                //println("#" + i + " -> " + w_id + ", " + w_date + ", " + w_time); // 출력 테스트용
            }
            cursor.close();

        } else if (tableName == "quest") {
            sql = "select q_id, q_number, q_name  from " + tableName;
            cursor = database.rawQuery(sql, null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                int q_id = cursor.getInt(0);
                int q_number = cursor.getInt(1);
                String q_name = cursor.getString(2);

                //println("#" + i + " -> " + q_id + ", " + q_number + ", " + q_name); // 출력 테스트용
            }
            cursor.close();
        }
    }

    // 헬퍼 클래스
    class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // DB 최초 생성 시 수행
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql, tableName;

            createTable("work");
            createTable("quest");

            tableName = "work";
            sql = "create table if not exists " + tableName + "(w_id integer PRIMARY KEY autoincrement, w_date date, w_time integer)";
            //database.execSQL(sql);
            db.execSQL(sql);
            tableName = "quest";
            sql = "create table if not exists " + tableName + "(q_id integer PRIMARY KEY autoincrement, q_number integer, q_name text)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}