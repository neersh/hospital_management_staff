package controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Patient {

    //a. name, age, room, doctor name, admit date, expenses, status(admitted, discharged)
    private Long id;
    private String name;
    private int age;
    private String room;
    private String doctorName;
    private LocalDate admitDate;
    private double expenses;
    private String status;


}
