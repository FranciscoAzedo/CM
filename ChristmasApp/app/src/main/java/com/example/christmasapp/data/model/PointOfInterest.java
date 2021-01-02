package com.example.christmasapp.data.model;


import android.graphics.Bitmap;
import android.widget.ImageView;

public class PointOfInterest {

    private String name;
    private String imageUrl;
    private Type type;
    private Location location;
    private Bitmap bitmap;

    public PointOfInterest(String name, String imageUrl, Type type, Location location, Bitmap bitmap) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.location = location;
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", type=" + type +
                ", location=" + location +
                ", imageView=" + bitmap +
                '}';
    }

    private enum Type {
        MARKET,
        EVENT,
        MONUMENT
    }

    private class Location {
        private double latitude;
        private double longitude;

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }


}
