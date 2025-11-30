package fr.cda.findboat.service;

import fr.cda.findboat.factory.BoatFactory;
import fr.cda.findboat.factory.TypeFactory;
import fr.cda.findboat.helper.CapacityParser;
import fr.cda.findboat.model.Boat;
import fr.cda.findboat.repository.TypeRepository;

import java.sql.SQLException;
import java.util.HashMap;

public class BoatService {

    public Boat createVoilierFromApiData(HashMap<String,String> boatData) {
        String lengthString = boatData.get("length");
        String numLength = lengthString.split(" ")[0];
        double length = Double.parseDouble(numLength);

        int year = Integer.parseInt(boatData.get("year"));
        int capacity = CapacityParser.parseCapacity(boatData.get("capacity"));
        double weekPrice = Double.parseDouble(boatData.get("weekPrice"));

        return BoatFactory.create(
                boatData.get("title"),
                boatData.get("description"),
                length,
                year,
                capacity,
                boatData.get("pictureUrl"),
                weekPrice,
                TypeFactory.create(boatData.get("type"))
        );
    }
}
