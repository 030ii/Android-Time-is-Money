package com.example.helllo.tim;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.helllo.tim.R.layout.record_item;

/**
 * Created by Owner on 2017-05-20.
 */

public class RecordFragment extends Fragment {
    MainActivity activity;
    MyAdapter adapter;
    HorizontalListView listView;
    String sql, info,  goalTime, date, year, month, day;
    int time, total, days, dayTotal;
    Cursor cursor;
    SQLiteDatabase database;
    TextView recTextView, goalTextView;

    ArrayList<RecordItem> record = new ArrayList<RecordItem>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        record.clear();

        openDatabase("Tim.db");

        sql = "select w_id, w_date, w_time  from work group by w_date";

        total = 0;
        days = 0;

        cursor = database.rawQuery(sql, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            int w_id = cursor.getInt(0);
            String w_date = cursor.getString(1);
            int w_time = cursor.getInt(2);
            record.add(new RecordItem(w_id, w_time, w_date));
            total += w_time;
        }

        sql = "select count(*) from work group by w_date";
        cursor = database.rawQuery(sql, null);
        days = cursor.getCount();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_record, container, false);

        // 어댑터 객체 생성
        adapter = new MyAdapter(getActivity(), record_item, record);

        //listView 레이아웃 참조
        listView = (HorizontalListView) rootView.findViewById(R.id.recListView);

        recTextView = (TextView) rootView.findViewById(R.id.recTextView);
        goalTextView = (TextView) rootView.findViewById(R.id.goalTextView);

        //어댑터 객체를 리스트 뷰에 설정
        listView.setAdapter(adapter);

        //리스트뷰에서 아이템 클릭시 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* params
               - parent : 클릭한 아이템을 포함하는 부모 뷰(ListView)
               - view : 클릭한 항목의 View
               - position : 클릭한 아이템의 Adepter에서의 위치값(0, 1, 2,...)
               - id : DB를 사용했을 때 Cursor의 id 값값
            */

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position을 이용해 어댑터에서 아이템을 가져옴

                Bundle bundle = new Bundle();
                bundle.putInt("time", record.get(position).r_time);
                bundle.putString("date", record.get(position).r_date);

                date = bundle.getString("date");
                year = date.substring(0, 2);
                month = date.substring(3, 5);
                day = date.substring(6, 8);
                time = bundle.getInt("time");
                info = "20" + year + "년 " + month + "월 " + day + "일의 집중 시간은 \n" + time/3600 + "시간 "
                        + time%3600/60 + "분으로 코인 " + time/1800 + "개를 적립하였습니다.";
                goalTime = "현재 목표시간 : " + "2" + "시간 \n 총 집중시간 : " + total/86400 + "일 " + total%86400/3600 + "시간 " +
                        total%86400%3600/60 + "분 \n Tim 사용일 수 : " + days + "일\n" + " 총 코인 개수 : " + total/1800 + "개";

                recTextView.setText(info); // recTextView에 출력
                goalTextView.setText(goalTime);

            }
        });//end of setOnItemClickListener
        cursor.close();
        return rootView;
    }

    //어댑터객체 클래스 선언(리스트뷰에 사용할 데이터를 관리하고, 각 아이템을 위한 뷰 객체를 생성)
    class MyAdapter extends BaseAdapter {
        Context mContext;//전달받은 Context 객체를 저장할 변수
        int record_item;
        ArrayList<RecordItem> record;
        LayoutInflater inflater;

        //어댑터 생성자
        public MyAdapter(Context context, int record_item, ArrayList<RecordItem> record) {
            mContext = context;
            this.record_item = record_item;
            this.record = record;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        /* 어댑터에서 관리하고 있는 아이템의 갯수를 반환
           (itemsList의 크기(size) 반환) */
        @Override
        public int getCount() {
            return record.size();//리스트의 크기
        }

        //파라미터로 전달된 인덱스에 해당하는 데이터를 반환
        @Override
        public Object getItem(int position) {
            return record.get(position);//리스트에서 아이템을 가져와 반환
        }

        //현재 아이템의 Id값을 인덱스값(position)을 반환
        @Override
        public long getItemId(int position) {
            return position;
        }

        //리스트에 아이템을 추가
        public void addItem(RecordItem item) {
            record.add(item);
        }

        //리스트의 모든 아이템을 삭제
        public void clear() {
            record.clear();
        }

        @Override//화면에 보일 아이템을 위한 뷰를 만들어 반환
        public View getView(int position, View convertView, ViewGroup parent) {
            //아이템을 위한 레이아웃 생성
            RecordLayout recordLayout = null;

            if (convertView == null) {
                recordLayout = new RecordLayout(mContext);
            } else {
                recordLayout = (RecordLayout) convertView;
            }

            //아이템의 인덱스값(position)을 이용해 리스트에 들어있는 아이템을 가져옴
            RecordItem items = record.get(position);

            //아이템에서 날짜를 가져와, 레이아웃에 날짜 설정
            recordLayout.setDate(items.getDate());
            //아이템에서 시간을 가져와, 레이아웃에 시간 설정
            recordLayout.setTime(items.getTime());


            return recordLayout;//레이아웃을 리턴
        }//end of getView()
    }//end of MyAdapter class

    public void openDatabase(String databaseName) {
        database = getActivity().openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
    }
}
