package fr.cda.findboat.factory;

import fr.cda.findboat.model.Boat;
import fr.cda.findboat.model.Type;

public class BoatFactory {

    public static Boat create(String title, String description, double length, int year, int capacity, String pictureUrl, double weekPrice, Type type) {
        return new Boat(title, description, length, year, capacity, pictureUrl, weekPrice, type);
    }

    public static Boat create(Integer id, String title, String description, double length, int year, int capacity, String pictureUrl, double weekPrice, Type type){
        return new Boat(id, title, description, length, year, capacity, pictureUrl, weekPrice, type);
    }
}
