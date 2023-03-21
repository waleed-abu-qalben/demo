package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {
        System.out.println("find all");
        return studentService.getStudents();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@RequestBody final Student student) {
        return studentService.addStudent(student);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id) {
        Student result = studentService.findById(id);
        if(result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody Student student) {
        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
        if (id != student.getId()) {
            response = new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);

        } else if (studentService.update(student)) {
            response = new ResponseEntity(HttpStatus.NO_CONTENT);

        }
        return response;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final int id) {
        if (studentService.deleteById(id)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


}
