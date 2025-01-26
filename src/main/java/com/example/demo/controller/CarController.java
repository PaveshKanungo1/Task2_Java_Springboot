package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    @Autowired
    private CarService carService;

//    private Map<Long, Car> carEntries = new HashMap<>();

    //common
    @GetMapping("/user/getAllCars")
    public ResponseEntity<List<Car>> getAll(){
        List<Car> cars = carService.getAllCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/user/getCar/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id){
        Car car = carService.getCarById(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/user/getCarsByBrand/{brand}")
    public ResponseEntity<List<Car>> getCarsByBrand(@PathVariable String brand){
        List<Car> cars = carService.getCarsByBrand(brand);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    //admin-specific
    @PostMapping("/user/addCar")
    public ResponseEntity<String> enterCar(@RequestBody Car car) throws AccessDeniedException {
        carService.addCar(car);
        return new ResponseEntity<>("Car added successfully!", HttpStatus.CREATED);
    }

    @DeleteMapping("/user/deleteCar/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) throws AccessDeniedException {
        carService.deleteCar(id);
        return new ResponseEntity<>("Car deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/user/updateCar/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) throws AccessDeniedException {
        Car updatedCar = carService.updateCar(id, car);
        return new ResponseEntity<>(updatedCar, HttpStatus.OK);
    }

    @PatchMapping("/user/updateCar/{id}")
    public ResponseEntity<Car> patchCar(@PathVariable Long id, @RequestBody Car car) throws AccessDeniedException {
        Car patchedCar = carService.patchCar(id, car);
        return new ResponseEntity<>(patchedCar, HttpStatus.OK);
    }
}
