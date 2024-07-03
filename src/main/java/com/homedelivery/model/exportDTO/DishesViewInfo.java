package com.homedelivery.model.exportDTO;

import java.util.ArrayList;
import java.util.List;

public class DishesViewInfo {

    private List<DishDetailsDTO> salads;

    private List<DishDetailsDTO> starters;

    private List<DishDetailsDTO> mainDishes;

    private List<DishDetailsDTO> desserts;

    private int saladsCount;

    private int startersCount;

    private int mainDishesCount;

    private int dessertsCount;

    public DishesViewInfo() {
        this.salads = new ArrayList<>();
        this.starters = new ArrayList<>();
        this.mainDishes = new ArrayList<>();
        this.desserts = new ArrayList<>();
    }

    public DishesViewInfo(List<DishDetailsDTO> salads, List<DishDetailsDTO> starters,
                          List<DishDetailsDTO> mainDishes, List<DishDetailsDTO> desserts) {
        this.salads = salads;
        this.starters = starters;
        this.mainDishes = mainDishes;
        this.desserts = desserts;
    }

    public List<DishDetailsDTO> getSalads() {
        return salads;
    }

    public void setSalads(List<DishDetailsDTO> salads) {
        this.salads = salads;
    }

    public List<DishDetailsDTO> getStarters() {
        return starters;
    }

    public void setStarters(List<DishDetailsDTO> starters) {
        this.starters = starters;
    }

    public List<DishDetailsDTO> getMainDishes() {
        return mainDishes;
    }

    public void setMainDishes(List<DishDetailsDTO> mainDishes) {
        this.mainDishes = mainDishes;
    }

    public List<DishDetailsDTO> getDesserts() {
        return desserts;
    }

    public void setDesserts(List<DishDetailsDTO> desserts) {
        this.desserts = desserts;
    }

    public int getSaladsCount() {
        return this.salads.size();
    }

    public void setSaladsCount(int saladsCount) {
        this.saladsCount = saladsCount;
    }

    public int getStartersCount() {
        return this.starters.size();
    }

    public void setStartersCount(int startersCount) {
        this.startersCount = startersCount;
    }

    public int getMainDishesCount() {
        return this.mainDishes.size();
    }

    public void setMainDishesCount(int mainDishesCount) {
        this.mainDishesCount = mainDishesCount;
    }

    public int getDessertsCount() {
        return this.desserts.size();
    }

    public void setDessertsCount(int dessertsCount) {
        this.dessertsCount = dessertsCount;
    }
}