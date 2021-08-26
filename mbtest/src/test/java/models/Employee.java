package models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
    public static enum Gender {
        MALE, FEMALE
    }

    public Employee(){

    }

    private long id;
    private String name;
    private Gender gender;
    private LocalDate createTime;
    private double income;
    private boolean alive;

    public boolean isMale() {
        return this.gender == Gender.MALE;
    }

    public boolean isFemale() {
        return this.gender == Gender.FEMALE;
    }

}
