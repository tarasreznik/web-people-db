package com.taras.webpeopledb.services;

import com.taras.webpeopledb.models.Person;
import com.taras.webpeopledb.repos.FileStorageRepo;
import com.taras.webpeopledb.repos.PersonRepo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipInputStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {
  private final PersonRepo personRepo;
  private final FileStorageRepo storageRepo;

  public PersonService(PersonRepo personRepo, FileStorageRepo fileStorageRepo) {
    this.personRepo = personRepo;
    this.storageRepo = fileStorageRepo;
  }

  @Transactional
  public Person savePersonWithPhoto(Person person, InputStream photoStream) {
    Person savedPerson = savePerson(person);
    storageRepo.save(person.getPhotoFileName(), photoStream);
    return savedPerson;
  }

  public Page<Person> findAll(Pageable pageable) {
    return personRepo.findAll(pageable);
  }

  public Person savePerson(Person person) {
    return personRepo.save(person);
  }

  public Optional<Person> findById(Long aLong) {
    return personRepo.findById(aLong);
  }

  public Iterable<Person> findAll() {
    return personRepo.findAll();
  }

  public void deleteAllById(Iterable<Long> ids) {
//    Iterable<Person> peopleToDelete = personRepo.findAllById(ids);
//    Stream<Person> peopleStream = StreamSupport.stream(peopleToDelete.spliterator(), false);
//
//    Set<String> fileNames = peopleStream
//        .map(Person::getPhotoFileName)
//        .collect(Collectors.toSet());

    Set<String> fileNames = personRepo.findFileNameByIds(ids);
    personRepo.deleteAllById(ids);
    storageRepo.deleteAllByName(fileNames);
  }

  public void importCSV(InputStream csvFileStream) {
    try {
      ZipInputStream zipInputStream = new ZipInputStream(csvFileStream);//unzipping bytes
      zipInputStream.getNextEntry();
      InputStreamReader inputStreamReader = new InputStreamReader(zipInputStream);//chars
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//lines
      bufferedReader.lines()
          .skip(1)
          .limit(10)
          .map(Person::parse)
          .forEach(personRepo::save);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}

