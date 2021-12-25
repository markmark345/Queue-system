package com.example.final_project_cs361;


import java.util.Date;

public class Lane {
    int A_;
    int B_;
    int QA_;
    int QB_;
    Date a_timestamp_;
    Date b_timestamp_;

    public Lane() {
        A_ = 0;
        B_ = 0;
        QA_ = 0;
        QB_ = 0;
        a_timestamp_ = new Date();
        b_timestamp_ = new Date();
    }
    public Lane(int a, int b, int qa, int qb, Date a_timestamp, Date b_timestamp) {
        A_ = a;
        B_ = b;
        QA_ = qa;
        QB_ = qb;
        a_timestamp_ = a_timestamp;
        b_timestamp_ = b_timestamp;
    }

    public int getA_() {
        return A_;
    }

    public int getB_() {
        return B_;
    }

    public int getQA_() {
        return QA_;
    }

    public int getQB_() {
        return QB_;
    }

    public Date getA_timestamp_() {
        return a_timestamp_;
    }

    public Date getB_timestamp_() {
        return b_timestamp_;
    }

    public void setA_(int a_) {
        A_ = a_;
    }

    public void setB_(int b_) {
        B_ = b_;
    }

    public void setQA_(int QA_) {
        this.QA_ = QA_;
    }

    public void setQB_(int QB_) {
        this.QB_ = QB_;
    }

    public void setA_timestamp_(Date a_timestamp_) {
        this.a_timestamp_ = a_timestamp_;
    }

    public void setB_timestamp_(Date b_timestamp_) {
        this.b_timestamp_ = b_timestamp_;
    }
}

