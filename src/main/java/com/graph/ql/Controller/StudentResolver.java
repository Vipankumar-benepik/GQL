package com.graph.ql.Controller;

import com.graph.ql.Dto.StudentRequest;
import com.graph.ql.Entity.Student;
import com.graph.ql.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

@org.springframework.stereotype.Controller
public class StudentResolver {

    @Autowired
    private StudentService studentService;

    @QueryMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @QueryMapping
    public Student getStudentById(@Argument Integer id) {
        return studentService.getStudentById(id);
    }

    @MutationMapping
    public Student createStudent(@Argument String name, @Argument String email) {
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setName(name);
        studentRequest.setEmail(email);
        return studentService.saveStudent(studentRequest);
    }

    @MutationMapping
    public Student updateStudent(@Argument Integer id, @Argument String name, @Argument String email) {
        StudentRequest studentDetails = new StudentRequest();
        studentDetails.setName(name);
        studentDetails.setEmail(email);
        return studentService.updateStudent(id, studentDetails);
    }

    @MutationMapping
    public Boolean deleteStudent(@Argument Integer id) {
        return studentService.deleteStudent(id);
    }
}
