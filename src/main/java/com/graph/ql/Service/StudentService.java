package com.graph.ql.Service;

import com.graph.ql.Dto.StudentRequest;
import com.graph.ql.Entity.Student;
import com.graph.ql.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student saveStudent(StudentRequest student) {
        Student newStudent = Student.builder().name(student.getName()).email(student.getEmail()).build();
        return studentRepository.save(newStudent);
    }

    public Student updateStudent(Integer id, StudentRequest request) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setName(request.getName());
            student.setEmail(request.getEmail());
            return studentRepository.save(student);
        }
        return null;
    }

    public boolean deleteStudent(Integer id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}