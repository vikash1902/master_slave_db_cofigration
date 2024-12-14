package com.learn.dbconfig.repository;

import com.learn.dbconfig.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student,Integer> {

    @Query(value = "SELECT * FROM student", nativeQuery = true)
    List<Student> findAllFromSlave();
}
