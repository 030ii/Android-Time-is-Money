package com.example.helllo.tim;

//데이터를 담아둘 DTO 클래스 선언(DTO:Data Transfer Object)
public class QuestItem {

    int resId; // 업적 이미지 id
    int q_number; // 업적 번호
    String q_name;// 업적 이름

    //생성자
    public QuestItem(int resId, int number, String name ) {
        this.resId = resId;
        this.q_number = number;
        this.q_name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return q_name;
    }

    public void setName(String name) {
        this.q_name = name;
    }

    public int getNumber() {
        return q_number;
    }

    public void setNumber(int number) {
        this.q_number = number;
    }
}
