package com.example.helllo.tim;

//데이터를 담아둘 DTO 클래스 선언(DTO:Data Transfer Object)
public class RecordItem {

    int r_id; // id
    int r_time; // 공부량
    String r_date;// 날짜

    //생성자
    public RecordItem(int id, int time, String date ) {
        this.r_id = id;
        this.r_time = time;
        this.r_date = date;
    }

    public int getId() {
        return r_id;
    }

    public void setId(int id) {
        this.r_id = id;
    }

    public int getTime() {
        return r_time;
    }

    public void setTime(int time) {
        this.r_time = time;
    }

    public String getDate() {
        return r_date;
    }

    public void setDate(String date) {
        this.r_date = date;
    }
}
