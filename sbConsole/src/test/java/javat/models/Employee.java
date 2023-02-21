package javat.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {
    public Employee(int i, String jake, Gender male,
                    LocalDate of, double v, boolean b, Date toDate) {
        id = i;
        name = jake;
        gender = male;
        createTime = of;
        income = v;
        alive = b;
        birthday = toDate;
    }

    public static enum Gender {
        MALE, FEMALE
    }

    public Employee() {

    }

    private long id;
    private String name;
    private Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDate createTime;
    private int salary;
    private int age;
    private double income;
    private boolean alive;
    private Date birthday;

    public boolean isMale() {
        return this.gender == Gender.MALE;
    }

    public boolean isFemale() {
        return this.gender == Gender.FEMALE;
    }

}
