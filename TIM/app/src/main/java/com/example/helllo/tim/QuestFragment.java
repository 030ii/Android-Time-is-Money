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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Owner on 2017-05-20.
 */

public class QuestFragment extends Fragment {
    MainActivity activity;
    MyAdapter adapter;
    ListView listView;
    String sql;
    Cursor cursor;
    SQLiteDatabase database;
    int total, days, focus, coin;

    ArrayList<QuestItem> quest = new ArrayList<QuestItem>();

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
        quest.clear();

        openDatabase("Tim.db");

        sql = "select q_id, q_number, q_name  from quest";

        cursor = database.rawQuery(sql, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            int q_id = cursor.getInt(0);
            int q_number = cursor.getInt(1);
            String q_name = cursor.getString(2);
            String q_img = "@drawable/img_" + q_number;
            int resId = getActivity().getResources().getIdentifier(q_img, "drawable", getActivity().getPackageName());

            quest.add(new QuestItem(resId, q_number, q_name));
        }

        sql = "select w_id, w_date, w_time  from work group by w_date";

        cursor = database.rawQuery(sql, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            int w_time = cursor.getInt(2);

            total += w_time;
        }

        sql = "select w_id from work group by w_date";
        cursor = database.rawQuery(sql, null);





        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_quest, container, false);

        // 어댑터 객체 생성
        adapter = new MyAdapter(getActivity(), R.layout.quest_item, quest);

        //listView 레이아웃 참조
        listView = (ListView) rootView.findViewById(R.id.questListView);

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
                QuestItem questItem = (QuestItem) adapter.getItem(position);

                //getName() 메서드를 이용하여 아이템에서 이름을 가져옴
                String questName = questItem.getName();

                Toast.makeText(getActivity(),
                        questName + " 업적입니다.",
                        Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putInt("img", quest.get(position).resId);
                bundle.putInt("number", quest.get(position).q_number);
                bundle.putString("name", quest.get(position).q_name);
            }
        });//end of setOnItemClickListener



        return rootView;
    }

    //어댑터객체 클래스 선언(리스트뷰에 사용할 데이터를 관리하고, 각 아이템을 위한 뷰 객체를 생성)
    class MyAdapter extends BaseAdapter {
        Context mContext;//전달받은 Context 객체를 저장할 변수
        int quest_item;
        ArrayList<QuestItem> quest;
        LayoutInflater inflater;

        //어댑터 생성자
        public MyAdapter(Context context, int quest_item, ArrayList<QuestItem> quest) {
            mContext = context;
            this.quest_item = quest_item;
            this.quest = quest;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /* 어댑터를 리스트뷰에 설정하면 리스트뷰(위젯)가 자동 호출함
                - 리스트뷰가 아댑터에게 요청하는 메서드들... */

        /* 어댑터에서 관리하고 있는 데이터(아이템)의 갯수를 반환
           (itemsList의 크기(size) 반환) */
        @Override
        public int getCount() {
            return quest.size();//리스트의 크기
        }

        //파라미터로 전달된 인덱스에 해당하는 데이터를 반환
        @Override
        public Object getItem(int position) {
            return quest.get(position);//리스트에서 아이템을 가져와 반환
        }

        //현재 아이템의 Id값을 인덱스값(position)을 반환
        @Override
        public long getItemId(int position) {
            return position;
        }

        //리스트에 아이템을 추가
        public void addItem(QuestItem item) {
            quest.add(item);
        }

        //리스트의 모든 아이템을 삭제
        public void clear() {
            quest.clear();
        }

        @Override//화면에 보일 아이템을 위한 뷰를 만들어 반환
        public View getView(int position, View convertView, ViewGroup parent) {
            //아이템을 위한 레이아웃 생성
            QuestLayout questLayout = null;

            if (convertView == null) {
                questLayout = new QuestLayout(mContext);
            } else {
                questLayout = (QuestLayout) convertView;
            }

            //아이템의 인덱스값(position)을 이용해 리스트에 들어있는 아이템을 가져옴
            QuestItem items = quest.get(position);

            //아이템에서 이미지 리소스 id를 가져와, 레이아웃에 이미지 설정
            questLayout.setImage(items.getResId());

            //아이템에서 이름을 가져와, 레이아웃에 이름 설정
            questLayout.setNameText(items.getName());

            return questLayout;//레이아웃을 리턴
        }//end of getView()
    }//end of MyAdapter class

    public void openDatabase(String databaseName) {
        database = getActivity().openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
    }

    // quest 테이블 입력 메소드
    public void insertQuest(int number, String name) {
        String sql = "insert into quest (q_number, q_name) values(?, ?);";

        Object[] params = {number, name};

        database.execSQL(sql, params);
    }
}
