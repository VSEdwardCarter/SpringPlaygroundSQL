package com.example.demo.Controller;


import com.example.demo.Model.Fruit;
import com.example.demo.Repository.FruitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FruitController {

    //Create Bean
    private final FruitRepository repository;

    public FruitController(FruitRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/fruits")
    public Iterable<Fruit> getAllFruitsFromDB(){
        return this.repository.findAll();
    }


    @PostMapping("/fruits")
    public void addFruitToDataBase(@RequestBody Fruit fruit){
        this.repository.save(fruit);

    }

//    @PostMapping("/fruits")
//    public void addListOfFruitToDataBase(@RequestBody List<Fruit> fruitList){
//        this.repository.saveAll(fruitList);
//    }




}
