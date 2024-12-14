package com.learn.dbconfig.service;

import com.learn.dbconfig.entity.Student;
import com.learn.dbconfig.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepo studentRepo;

    @Autowired
    public StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Transactional
    public List<Student> master(){

        List<Student> students = studentRepo.findAll();

        for (Student student :students){
            System.err.println("student name master = " + student.getName());
        }

        return studentRepo.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<Student> slave(){
//        System.out.println("Current DbContextHolder from service: " + DbContextHolder.getDbType());

        List<Student> students = studentRepo.findAll();

        for (Student student :students){
            System.err.println("student name  from slave = " + student.getName());
        }
        return studentRepo.findAll();
    }


}
