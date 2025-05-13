package com.graph.ql.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graph.ql.Dto.Checksum;
import com.graph.ql.Dto.StudentInput;
import com.graph.ql.Dto.StudentRequest;
import com.graph.ql.Entity.Student;
import com.graph.ql.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.graph.ql.utils.Decode.decryptText;

@Controller
public class StudentResolver {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @QueryMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @QueryMapping
    public Student getStudentById(@Argument Integer id) {
        return studentService.getStudentById(id);
    }

    @MutationMapping
    public Student createStudentWithChecksum(@Argument Checksum checksum) {
        try {
            String decryptedJson = decryptText(checksum.getChecksum());
            StudentRequest studentRequest = objectMapper.readValue(decryptedJson, StudentRequest.class);
            return studentService.saveStudent(studentRequest);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or malformed request");
        }
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

    @MutationMapping
    public List<Student> createMultipleStudentsRecords(@Argument List<StudentInput> students) {

        List<Student> studentsList = students.stream().map(request -> {
            Student student = new Student();
            student.setName(request.getName());
            student.setEmail(request.getEmail());
            return student;
        }).toList();
        return studentService.createMultipleStudentsRecords(studentsList);
    }

}
