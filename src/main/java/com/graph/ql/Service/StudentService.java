package com.graph.ql.Service;

import com.graph.ql.Dto.StudentRequest;
import com.graph.ql.Entity.Student;
import com.graph.ql.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    private final List<FluxSink<Student>> subscribers = new CopyOnWriteArrayList<>();

    @Cacheable(value = "StudentList")
    public List<Student> getAllStudents() {
        System.out.println("Fetching from DB...");
        return studentRepository.findAll();
    }

    @Cacheable(value = "Student", key = "#id")
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = {"StudentList"}, allEntries = true)
    public Student saveStudent(StudentRequest student) {
        Student newStudent = Student.builder()
                .name(student.getName())
                .email(student.getEmail())
                .build();

        Student savedStudent = studentRepository.save(newStudent);

        // Push to subscribers
        for (FluxSink<Student> subscriber : subscribers) {
            subscriber.next(savedStudent);
        }

        return savedStudent;
    }

    @CacheEvict(value = {"StudentList", "Student"}, key = "#id", allEntries = true)
    public Student updateStudent(Integer id, StudentRequest request) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setName(request.getName());
            student.setEmail(request.getEmail());
            return studentRepository.save(student);
        }
        return null;
    }

    @Caching(evict = {
            @CacheEvict(value = "Student", key = "#id"),
            @CacheEvict(value = "StudentList", allEntries = true)
    })
    public boolean deleteStudent(Integer id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @CacheEvict(value = "StudentList", allEntries = true)
    public List<Student> createMultipleStudentsRecords(List<Student> students) {
        return studentRepository.saveAll(students);
    }

    // SUBSCRIPTION method
    public Flux<Student> studentCreatedPublisher() {
        return Flux.create(emitter -> subscribers.add(emitter.onDispose(() -> subscribers.remove(emitter))));
    }
}

//
//@Service
//public class StudentService {
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    private final Duration TTL = Duration.ofMinutes(10);
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    private final String STUDENT_KEY_PREFIX = "Student:";
//    private final String STUDENT_LIST_KEY = "StudentList";
//
//    public List<Student> getAllStudents() {
//        List<Student> students = (List<Student>) redisTemplate.opsForValue().get(STUDENT_LIST_KEY);
//        if (students != null) {
//            return students;
//        }
//
//        System.out.println("Fetching from DB...");
//        students = studentRepository.findAll();
//        redisTemplate.opsForValue().set(STUDENT_LIST_KEY, students, TTL);
//        return students;
//    }
//
//    public Student getStudentById(Integer id) {
//        String key = STUDENT_KEY_PREFIX + id;
//        Student student = (Student) redisTemplate.opsForValue().get(key);
//        if (student != null) {
//            return student;
//        }
//
//        student = studentRepository.findById(id).orElse(null);
//        if (student != null) {
//            redisTemplate.opsForValue().set(key, student, TTL);
//        }
//        return student;
//    }
//
//    public Student saveStudent(StudentRequest request) {
//        Student newStudent = Student.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .build();
//
//        Student savedStudent = studentRepository.save(newStudent);
//
//        // Invalidate cache
//        redisTemplate.delete(STUDENT_LIST_KEY);
//        redisTemplate.opsForValue().set(STUDENT_KEY_PREFIX + savedStudent.getId(), savedStudent);
//
//        return savedStudent;
//    }
//
//    public Student updateStudent(Integer id, StudentRequest request) {
//        Student student = studentRepository.findById(id).orElse(null);
//        if (student != null) {
//            student.setName(request.getName());
//            student.setEmail(request.getEmail());
//
//            Student updatedStudent = studentRepository.save(student);
//
//            // Invalidate cache
//            redisTemplate.delete(STUDENT_LIST_KEY);
//            redisTemplate.opsForValue().set(STUDENT_KEY_PREFIX + id, updatedStudent);
//
//            return updatedStudent;
//        }
//        return null;
//    }
//
//    public boolean deleteStudent(Integer id) {
//        if (studentRepository.existsById(id)) {
//            studentRepository.deleteById(id);
//
//            // Invalidate cache
//            redisTemplate.delete(STUDENT_KEY_PREFIX + id);
//            redisTemplate.delete(STUDENT_LIST_KEY);
//
//            return true;
//        }
//        return false;
//    }
//
//    public List<Student> createMultipleStudentsRecords(List<Student> students) {
//        List<Student> savedStudents = studentRepository.saveAll(students);
//
//        // Invalidate cache
//        redisTemplate.delete(STUDENT_LIST_KEY);
//        for (Student student : savedStudents) {
//            redisTemplate.opsForValue().set(STUDENT_KEY_PREFIX + student.getId(), student);
//        }
//
//        return savedStudents;
//    }
//}
