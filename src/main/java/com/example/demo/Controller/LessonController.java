package com.example.demo.Controller;

import com.example.demo.Model.Lesson;
import com.example.demo.Repository.LessonRepository;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final LessonRepository repository;

    public LessonController(LessonRepository repository) {
        this.repository = repository;
    }

    // Read
    @GetMapping("/all")
    public Iterable<Lesson> all() { return this.repository.findAll(); }

    @GetMapping("{id}")
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
    public Lesson updateLessonTitle(@PathVariable Long id, @RequestBody Map<String, Object> newLesson){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Lesson oldLesson = this.repository.findById(id).get();
        newLesson.forEach((key,value) ->{
            switch (key){
                case "title": oldLesson.setTitle((String) value);break;
                case "deliveredOn" :
                    try {
                        oldLesson.setDeliveredOn(df.parse(value.toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
            }
                }

        );


        return this.repository.save(oldLesson);
    }

//
//    @PatchMapping("/{id}")
//    public Lesson updateLessonTitle(@PathVariable Long id, @RequestBody Map<String, Object> newLesson){
//        Lesson oldlesson = this.repository.findById(id).get();
//            newLesson.forEach((key, value) -> {
//                switch(key){
//                    case "title": oldlesson.setTitle((String) value); break;
//                    case "deliveredOn": oldlesson.setDeliveredOn((Date) value); break;
//            } return this.repository.save(newLesson);
//        }
//    }



    //Delete
    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id){
        this.repository.deleteById(id);

    }

    //Delete all entries
    @DeleteMapping()
    public void deleteALlEntries(){
        this.repository.deleteAll();
    }

    @GetMapping("/find/{title}")
    public Lesson findByName(@PathVariable String title){
//        String newTitle = title.replace("%20"," ");
        Lesson bob =  this.repository.findByTitle(title);

        return bob;
    }

    @GetMapping("/between")
    public Iterable<Lesson> findBetweenDates(@RequestParam String date1, @RequestParam String date2) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate1 = df.parse(date1);
        Date newDate2 = df.parse(date2);
        Iterable<Lesson> bob = this.repository.findAllByDeliveredOnBetween(newDate1, newDate2);

        return bob;
    }

}
