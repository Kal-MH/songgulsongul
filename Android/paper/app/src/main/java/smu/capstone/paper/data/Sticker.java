package smu.capstone.paper.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Sticker extends RealmObject {

    @PrimaryKey
    private int id;
    private byte[] image;

    public int getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public void set( byte[] image_){
        image = image_;
    }

}
