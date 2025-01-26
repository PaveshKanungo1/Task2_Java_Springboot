package com.example.demo.service;

import com.example.demo.exception.CarNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Car;
import com.example.demo.model.User;
import com.example.demo.repository.CarRepo;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private UserRepo userRepo;

    public List<Car> getAllCars(){
        List<Car> cars = carRepo.findAll();
        System.out.println("hi");
        if(cars.isEmpty()){
            System.out.println("cars is empty");
            throw new CarNotFoundException("No Cars Found");
        }
        return cars;
    }

    public Car getCarById(Long id){
        return carRepo.findById(id).orElseThrow(() -> new CarNotFoundException("Car with ID " + id + " not found"));
    }

    public void addCar(Car car) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User dbUser = userRepo.findByUsername(username);
        if(dbUser == null){
            throw new UserNotFoundException("User not found");
        }

        if("Admin".equals(dbUser.getRole())){
            if (car.getBrand() == null || car.getBrand().isEmpty()) {
                throw new RuntimeException("Brand is required");
            }
            if (car.getModel() == null || car.getModel().isEmpty()) {
                throw new RuntimeException("Model is required");
            }
            if (car.getPrice() == null || car.getPrice() <= 0) {
                throw new RuntimeException("Price must be greater than zero");
            }
            carRepo.save(car);
        }

        throw new AccessDeniedException("You are not authorized to add Car");
    }

    public void deleteCar(Long id) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User dbUser = userRepo.findByUsername(username);
        if(dbUser == null){
            throw new UserNotFoundException("User not found");
        }

        if("Admin".equals(dbUser.getRole())){
            if(!carRepo.existsById(id)){
                throw new CarNotFoundException("Car with ID " + id + " not found");
            }
            carRepo.deleteById(id);
        }

        throw new AccessDeniedException("You are not authorized to delete Car");
    }

    public Car updateCar(Long id, Car car) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User dbUser = userRepo.findByUsername(username);
        if(dbUser == null){
            throw new UserNotFoundException("User not found");
        }

        if("Admin".equals(dbUser.getRole())){
            if(!carRepo.existsById(id)){
                throw new CarNotFoundException("Car with ID " + id + " not found");
            }
            car.setId(id);
            return carRepo.save(car);
        }

        throw new AccessDeniedException("You are not authorized to delete Car");
    }

    public List<Car> getCarsByBrand(String brand){
        List<Car> cars = carRepo.findByBrand(brand);
        if (cars.isEmpty()) {
            throw new CarNotFoundException("No cars found for brand: " + brand);
        }
        return cars;
    }

    public Car patchCar(Long id, Car car) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User dbUser = userRepo.findByUsername(username);
        if(dbUser == null){
            throw new UserNotFoundException("User not found");
        }

        if("Admin".equals(dbUser.getRole())){
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
            return carRepo.save(existingCar);
        }

        throw new AccessDeniedException("You are not authorized to delete Car");
    }
}
