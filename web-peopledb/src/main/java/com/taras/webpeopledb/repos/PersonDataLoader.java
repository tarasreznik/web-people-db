package com.taras.webpeopledb.repos;

import com.taras.webpeopledb.models.Person;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
public class PersonDataLoader implements ApplicationRunner {
  private final PersonRepo personRepo;

  public PersonDataLoader(PersonRepo personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if(personRepo.count() == 0) {
      List<Person> people = List.of(
          new Person(null, "Taras", "Reznik", "tarasR@gmail.com", LocalDate.of(2004, 3, 26), new BigDecimal("16000"),null),
          new Person(null, "Sofia", "Kashchuk", "sofiaK@gmail.com",LocalDate.of(2004, 7, 12), new BigDecimal("14000"),null),
          new Person(null, "Sofia", "Reznik", "sofiaR@gmail.com",LocalDate.of(2009, 11, 6), new BigDecimal("10000"), null));

      personRepo.saveAll(people);
    }
  }
}
