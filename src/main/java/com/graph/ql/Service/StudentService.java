package com.graph.ql.Service;

import com.graph.ql.Dto.StudentRequest;
import com.graph.ql.Entity.Student;
import com.graph.ql.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    private final List<FluxSink<Student>> subscribers = new CopyOnWriteArrayList<>();

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student saveStudent(StudentRequest student) {
        Student newStudent = Student.builder().name(student.getName()).email(student.getEmail()).build();
        Student savedStudent = studentRepository.save(newStudent);

        // Publish the event to subscribers
        for (FluxSink<Student> subscriber : subscribers) {
            subscriber.next(savedStudent);
        }

        return savedStudent;
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

    public List<Student> createMultipleStudentsRecords(List<Student> students){
        return studentRepository.saveAll(students);
    }

    // SUBSCRIPTION method
    public Flux<Student> studentCreatedPublisher() {
        return Flux.create(emitter -> subscribers.add(emitter.onDispose(() -> subscribers.remove(emitter))));
    }
}