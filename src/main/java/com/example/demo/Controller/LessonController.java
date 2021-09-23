package com.example.demo.Controller;

import com.example.demo.Model.Lesson;
import com.example.demo.Repository.LessonRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final LessonRepository repository;

    public LessonController(LessonRepository repository) {
        this.repository = repository;
    }

    // Read
    @GetMapping("")
    public Iterable<Lesson> all() { return this.repository.findAll(); }

    @GetMapping("/")
    public Lesson singleRecord(@PathVariable Long id){
        return this.repository.findById(id).get();
    }

    // Create
    @PostMapping("")
    public Lesson create(@RequestBody Lesson lesson) {
        return this.repository.save(lesson);
    }
    //Update
    @PatchMapping("/{id}")
    public Lesson updateLessonTitle(@PathVariable Long id, @RequestBody Lesson lesson){
        Lesson bob = this.repository.findById(id).get();
        lesson.setId(id);
        lesson.setDeliveredOn(bob.getDeliveredOn());

        return this.repository.save(lesson);
    }
    //Delete
    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id){
        this.repository.deleteById(id);

    }


}
