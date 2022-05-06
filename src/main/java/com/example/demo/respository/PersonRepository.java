package com.example.demo.respository;

import com.example.demo.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface PersonRepository extends CrudRepository<Person, Long> {

}
