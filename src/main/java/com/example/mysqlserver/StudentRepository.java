package com.example.mysqlserver;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "student", collectionResourceRel = "student")
public interface StudentRepository extends PagingAndSortingRepository<Student, Integer> {
}
