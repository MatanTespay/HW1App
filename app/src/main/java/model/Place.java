package model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.id;

/**
 * Created by Matan on 24/11/2016.
 */

public class Place  implements Parcelable {
    private String name;
    private String description, imgFileName;
    private Long id;
    boolean isFavorite;
    Bitmap imgData;


    public Bitmap getImgData() {
        return imgData;
    }

    public void setImgData(Bitmap imgData) {
        this.imgData = imgData;
    }

    public Place(){

    }

/*    public Place(String name, String des, Long id, boolean isFav, Bitmap img) {
        this.name = name;
        this.description = des;
        this.id = id;
        this.isFavorite = isFav;
        this.imgData = img;
    }*/

/*    public Place(Long id, String name, String des, boolean isFav, String imgFileName) {
        this.id = id;
        this.name = name;
        this.description = des;
        this.isFavorite = isFav;
        this.imgFileName = imgFileName;
    }*/

    public Place(Long id, String name, String des, boolean isFav, Bitmap imgData) {
        this.id = id;
        this.name = name;
        this.description = des;
        this.isFavorite = isFav;
        this.imgData = imgData;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }


    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** Used to give additional hints on how to process the received parcel.*/
    @Override
    public int describeContents() {
// ignore for now
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {

        pc.writeString(name);
        pc.writeString(imgFileName);
        pc.writeString(description);
        pc.writeLong(id);
        pc.writeInt( isFavorite ? 1 :0 );
        pc.writeParcelable(imgData, flags);
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel pc) {
            return new Place(pc);
        }
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public Place(Parcel pc){

        name = pc.readString();
        imgFileName = pc.readString();
        description      = pc.readString();
        id =  pc.readLong();
        isFavorite = ( pc.readInt() == 1 );
        imgData = pc.readParcelable(Bitmap.class.getClassLoader());
    }

}
