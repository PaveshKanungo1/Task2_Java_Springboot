package com.example.demo.controller;

import com.example.demo.exception.CarNotFoundException;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class MainController {

    @Autowired
    private CarRepo carRepo;

//    private Map<Long, Car> carEntries = new HashMap<>();

    @GetMapping("/getAllCars")
    public ResponseEntity<List<Car>> getAll(){
        List<Car> cars = carRepo.findAll();
        if(cars.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @PostMapping("/enterCar")
    public ResponseEntity<String> enterCar(@RequestBody Car car){
        carRepo.save(car);
        return new ResponseEntity<>("Car added successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/getCar/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id){
        Car car = carRepo.findById(id).orElseThrow(() ->
                new CarNotFoundException("Car with ID " + id + " not found"));
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @DeleteMapping("/deleteCar/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id){
        if (!carRepo.existsById(id)) {
            throw new CarNotFoundException("Car with ID " + id + " not found");
        }
        carRepo.deleteById(id);
        return new ResponseEntity<>("Car deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/updateCar/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car){
        if (!carRepo.existsById(id)) {
            throw new CarNotFoundException("Car with ID " + id + " not found");
        }
        car.setId(id);
        Car updatedCar = carRepo.save(car);
        return new ResponseEntity<>(updatedCar, HttpStatus.OK);
    }

    @PatchMapping("/updateCar/{id}")
    public ResponseEntity<Car> patchCar(@PathVariable Long id, @RequestBody Car car) {
        Car existingCar = carRepo.findById(id).orElseThrow(() ->
                new CarNotFoundException("Car with ID " + id + " not found"));

        if (car.getBrand() != null) {
            existingCar.setBrand(car.getBrand());
        }
        if (car.getModel() != null) {
            existingCar.setModel(car.getModel());
        }
        if (car.getPrice() != null) {
            existingCar.setPrice(car.getPrice());
        }
        Car patchedCar = carRepo.save(existingCar);
        return new ResponseEntity<>(patchedCar, HttpStatus.OK);
    }
}
