package com.example.attendencemanager.activities.model;

public class Model {
     private int id;
     private String subject;
     private String present;
     private String absent;
     private String total;

     public Model(int id, String subject,String present,String absent, String total ) {
          this.id = id;
          this.subject=subject;
          this.present=present;
          this.absent=absent;
          this.total=total;

     }

     public int getId() {
          return id;
     }

     public String getTitle() {
          return subject;
     }

     public String getPresent() {
          return present;
     }

     public String getAbsent() {
          return absent;
     }

     public String getTotal() {
          return total;
     }

}