package com.example.demo;

import com.example.demo.Controller.LessonController;
import com.example.demo.Model.Lesson;
import com.example.demo.Repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class LessonControllerTests {
    @Autowired
    MockMvc mvc;

    @Autowired
    LessonRepository repository;




    @Test
    @Transactional
    @Rollback
    public void testLessonCreation() throws Exception{
        MockHttpServletRequestBuilder request = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"Intro to Economics\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Intro to Economics")));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetLessonController() throws Exception{
        Lesson lesson = new Lesson();
        lesson.setTitle("Intro to Economics");
        repository.save(lesson);



        //Get Request to see that item successfully added and queried by id number
        MockHttpServletRequestBuilder request = get("/lessons/all")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title",is("Intro to Economics")))
                .andExpect(jsonPath("$[0].id", equalTo(lesson.getId().intValue())));
        ;

    }

    @Test
    @Transactional
    @Rollback
    public void testPatchLessonController() throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Lesson lesson = new Lesson();
        lesson.setTitle("Intro to Economics");
        lesson.setDeliveredOn(df.parse("2012-12-12"));
        repository.save(lesson);

        MockHttpServletRequestBuilder request = patch("/lessons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deliveredOn\" : \"2021-08-12\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveredOn",is("2021-08-12")))
                .andExpect(jsonPath("$.id", equalTo(lesson.getId().intValue())))
                .andExpect(jsonPath("$.title", equalTo("Intro to Economics")));
        ;

    }

    @Test
    @Transactional
    @Rollback
    public void testCustomQueryFunctions() throws Exception{
        Lesson lesson = new Lesson();
        lesson.setTitle("The ins and outs of Math");
        this.repository.save(lesson);

        Lesson lesson1 = new Lesson();
        lesson1.setTitle("SQL");
        this.repository.save(lesson1);

        MockHttpServletRequestBuilder request = get("/lessons/find/SQL")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("SQL")));
    }

    @Test
    @Transactional
    @Rollback
    public void testReturnLessonBetweenDates() throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Lesson lesson = new Lesson();
        lesson.setTitle("The ins and outs of Math");
        lesson.setDeliveredOn(df.parse("2021-01-12"));
        this.repository.save(lesson);

        Lesson lesson1 = new Lesson();
        lesson1.setTitle("SQL");
        lesson1.setDeliveredOn(df.parse("2021-12-12"));
        this.repository.save(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setTitle("Java");
        lesson2.setDeliveredOn(df.parse("2021-05-12"));
        this.repository.save(lesson2);

        MockHttpServletRequestBuilder request = get("/lessons/between?date1=2021-01-01&date2=2021-06-01")
                .contentType(MediaType.APPLICATION_JSON);


        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", iterableWithSize(2)));
    }
}
