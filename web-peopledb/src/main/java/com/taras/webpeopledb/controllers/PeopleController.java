package com.taras.webpeopledb.controllers;

import static java.lang.String.format;

import com.taras.webpeopledb.exceptions.StorageException;
import com.taras.webpeopledb.models.Person;
import com.taras.webpeopledb.repos.FileStorageRepo;
import com.taras.webpeopledb.repos.PersonRepo;
import com.taras.webpeopledb.services.PersonService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/people")
@Log4j2
public class PeopleController {
  public static final String DISPO = """
      attachment; filename="%s"
      """;
  private final PersonRepo personRepo;
  private final FileStorageRepo fileRepo;
  private final PersonService personService;

  public PeopleController(PersonRepo personRepo, FileStorageRepo fileRepo, PersonService personService) {
    this.personRepo = personRepo;
    this.fileRepo = fileRepo;
    this.personService = personService;
  }

  @ModelAttribute("people")
  public Page<Person> getPeople(@PageableDefault(size = 5) Pageable page) {
    return personService.findAll(page);
  }

  @ModelAttribute
  public Person getPerson() {
    return new Person();
  }

  @GetMapping
  public String showPeoplePage() {
    return "people";
  }

  @GetMapping(path = "/images/{resource}")
  public ResponseEntity<Resource> getResource(@PathVariable String resource){
    return ResponseEntity
        .ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,format(DISPO, resource))
        .body(fileRepo.findByName(resource));
  }

  @PostMapping
  public String savePerson(Model model, @Valid Person person, Errors errors, @RequestParam("photoFileName") MultipartFile photoFile)
      throws IOException {
    log.info(person);
    log.info("File name : " + photoFile.getOriginalFilename());
    log.info("File size : " + photoFile.getSize());
    log.info("Errors : " + errors);
    if (!errors.hasErrors()) {
      try {
        if(photoFile.isEmpty()){
          personService.savePerson(person);
        }else{
          personService.savePersonWithPhoto(person, photoFile.getInputStream());
        }
        return "redirect:people";
      } catch (StorageException e) {
        model.addAttribute("errorMsg", "System is currently can not accept photo file.");
        return "people";
      }
    }
    return "people";
  }

  @PostMapping(params = "action=delete")
  public String deletePeople(@RequestParam Optional<List<Long>> selections){
    log.info(selections);
    selections.ifPresent(personService::deleteAllById);
    return "redirect:people";
  }

  @PostMapping(params = "action=edit")
  public String editPerson(@RequestParam Optional<List<Long>> selections, Model model){
    log.info(selections);
    if(selections.isPresent()){
      Optional<Person> person = personRepo.findById(selections.get().get(0));
      model.addAttribute("person",person);
    }
    return "people";
  }
  @PostMapping(params = "action=import")
  public String importSCV(@RequestParam MultipartFile csvFile){
    log.info("File name is : " + csvFile.getOriginalFilename());
    log.info("File size is : " + csvFile.getSize());
    try {
      personService.importCSV(csvFile.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "redirect:people";
  }
}
