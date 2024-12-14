package com.learn.dbconfig.controller;

import com.learn.dbconfig.entity.Student;
import com.learn.dbconfig.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    private final StudentService studentService;

    @Autowired
    public HomeController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("master")
    public ResponseEntity<List<Student>> master(){
        return  new ResponseEntity<>(studentService.master(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("slave")
    public ResponseEntity<List<Student>> slave(){
        return  new ResponseEntity<>(studentService.slave(), HttpStatusCode.valueOf(200));
    }
}
