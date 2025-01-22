# Car Management System

A simple Spring Boot project demonstrating CRUD (Create, Read, Update, Delete) operations using an H2 database.

## Features

- **Add a car to the system**: `POST /api/admin/enterCar`
- **Retrieve all cars**: `GET /api/admin/getAllCars`
- **Retrieve a car by ID**: `GET /api/admin/getCar/{id}`
- **Update a car's details**: `PUT /api/admin/updateCar/{id}`
- **Partially update a car's details**: `PATCH /api/admin/updateCar/{id}`
- **Delete a car by ID**: `DELETE /api/admin/deleteCar/{id}`

## Technologies Used

- **Java**
- **Spring Boot**
- **H2 Database**
- **Spring Data JPA**
- **RESTful API**
