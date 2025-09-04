package com.example.test;

import java.util.List;

//import org.springframework.stereotype.Component;

//import jakarta.annotation.PostConstruct;

//@Component
public class DataLoader {
    private final CoffeeRepository coffeeRepository;

    public DataLoader(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    
//    @PostConstruct
    private void loadData(){
        coffeeRepository.saveAll(List.of(
        new Coffee("Espresso"),
        new Coffee("Latte"),
        new Coffee("Cappuccino"),
        new Coffee("Macchiato"),
        new Coffee("Americano"),
        new Coffee("Flat White")
        ));
    }

}
