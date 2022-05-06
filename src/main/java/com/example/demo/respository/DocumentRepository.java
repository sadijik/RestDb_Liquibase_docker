package com.example.demo.respository;

import com.example.demo.entity.Documents;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Documents,Long> {
}
