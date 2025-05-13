package com.graph.ql.Controller;

import com.graph.ql.Entity.Student;
import com.graph.ql.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller()
public class AnotherResolver {
    @Autowired
    private StudentService studentService;


}
