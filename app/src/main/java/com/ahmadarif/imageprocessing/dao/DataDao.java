package com.ahmadarif.imageprocessing.dao;

import com.ahmadarif.imageprocessing.model.Data;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DataDao {

    private static Realm realm;

    static {
        RealmConfiguration config = new RealmConfiguration.Builder()
            .name("default")
            .schemaVersion(4)
            .deleteRealmIfMigrationNeeded()
            .build();

        realm = Realm.getInstance(config);
    }

    public static void save(String character, String pattern) {
        realm.beginTransaction();

        Number lastId = realm.where(Data.class).max("id");
        int nextID = (lastId != null) ? (lastId.intValue() + 1) : 1;

        Data data = realm.createObject(Data.class);
        data.setId(nextID);
        data.setPattern(pattern);
        data.setCharacter(character);

        realm.commitTransaction();
    }

    public static void update(int id, String character, String pattern) {
        realm.beginTransaction();

        Data data = realm.where(Data.class).equalTo("id", id).findFirst();

        if (data != null) {
            data.setCharacter(character);
            data.setPattern(pattern);
        }

        realm.commitTransaction();
    }

    public static void delete(int id) {
        realm.beginTransaction();

        Data data = realm.where(Data.class).equalTo("id", id).findFirst();

        if (data != null) {
            data.deleteFromRealm();
        }

        realm.commitTransaction();
    }

    public static List<Data> getAll() {
        return realm.where(Data.class).findAll();
    }

    public static Data findById(int id) {
        return realm.where(Data.class).equalTo("id", id).findFirst();
    }

}