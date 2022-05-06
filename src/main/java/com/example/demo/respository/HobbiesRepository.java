package com.example.demo.respository;

import com.example.demo.entity.Hobbies;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HobbiesRepository extends CrudRepository<Hobbies,Long> {

}
