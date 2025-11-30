package fr.cda.findboat.factory;

import fr.cda.findboat.model.Type;

public class TypeFactory {

    public static Type create(String name) {
        return new Type(name);
    }

    public static Type create(int id, String name) {
        return new Type(id, name);
    }
}
