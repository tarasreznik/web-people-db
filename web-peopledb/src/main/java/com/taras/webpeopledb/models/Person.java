package com.taras.webpeopledb.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty(message = "First name can not be empty")
  private String firstName;

  @NotEmpty(message = "Last name can not be empty")
  private String lastName;

  @Email(message = "Email must be valid")
  @NotEmpty(message = "Email must not be empty")
  private String email;

  @Past(message = "Date of birth must be in the past")
  @NotNull(message = "Date of birth must be specified")
  private LocalDate dob;

  @NotNull(message = "Salary must be specified")
  @PositiveOrZero(message = "Salary can not be negative")
  private BigDecimal salary;

  private String photoFileName;

  public static Person parse(String csvLine) {
    String[] fields = csvLine.split(",\\s*");
    LocalDate dob = LocalDate.parse(fields[3], DateTimeFormatter.ofPattern("M/d/yyyy"));

    return new Person(null, fields[1], fields[2], fields[4], dob, new BigDecimal(fields[5]), null);
  }
}
