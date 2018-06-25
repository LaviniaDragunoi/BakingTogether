package com.example.user.bakingtogether.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Recipe entity that is stored in the Room database , has recipeId as indices that can help to
 * identify each children entities by it's id
 */
@Entity(tableName = "recipes")
public class RecipeEntity implements Parcelable {

    public static final Creator<RecipeEntity> CREATOR = new Creator<RecipeEntity>() {
        @Override
        public RecipeEntity createFromParcel(Parcel in) {
            return new RecipeEntity(in);
        }

        @Override
        public RecipeEntity[] newArray(int size) {
            return new RecipeEntity[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int servings;
    private String name, image;

    public RecipeEntity(int id, String name, int servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    protected RecipeEntity(Parcel in) {
        id = in.readInt();
        servings = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(servings);
        dest.writeString(name);
        dest.writeString(image);
    }
}
