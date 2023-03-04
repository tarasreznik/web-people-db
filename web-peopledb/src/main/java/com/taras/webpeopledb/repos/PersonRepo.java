package com.taras.webpeopledb.repos;

import com.taras.webpeopledb.models.Person;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends PagingAndSortingRepository<Person, Long>, CrudRepository<Person, Long> {
  @Query(nativeQuery = true, value = "SELECT PHOTO_FILE_NAME FROM PERSON WHERE ID IN :ids")
  public Set<String> findFileNameByIds(@Param("ids") Iterable<Long> ids);
}
