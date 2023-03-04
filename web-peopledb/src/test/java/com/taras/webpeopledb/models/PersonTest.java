package com.taras.webpeopledb.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PersonTest {

  @Test
  void canParse() {
    String csvLine = "1,Taras,Reznik,3/26/2004,tt.reznik@gmail.com,10000";
    Person person = Person.parse(csvLine);
    assertThat(person.getDob()).isEqualTo(LocalDate.of(2004, 3, 26));

  }

}