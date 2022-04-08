//package com.example.mysqlserver;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "student")
//public class Student {
//    @Id
//    @Column(name = "id", nullable = false)
//    private Integer id;
//
//    @Column(name = "name", nullable = false, length = 50)
//    private String name;
//
//    @Column(name = "gender", nullable = false, length = 50)
//    private String gender;
//
//    @Column(name = "age", nullable = false)
//    private Integer age;
//
//    @Column(name = "total_score", nullable = false)
//    private Integer totalScore;
//
//    public Integer getTotalScore() {
//        return totalScore;
//    }
//
//    public void setTotalScore(Integer totalScore) {
//        this.totalScore = totalScore;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//}