package com.example._studio_nagran;

import javafx.scene.image.Image;

// definiuje model Płyty
public class Disk {
    private int id;
    private String name;
    private Image diskAvatar;
    // definiuje konstruktor parametryczny oraz wywołuje w nim super konstruktor
    public Disk(int id, String name, Image diskAvatar){
        super();
        this.id = id;
        this.name = name;
        this.diskAvatar = diskAvatar;

    }
    // określam Settery oraz Gettery
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }

    public Image getDiskAvatar(){ return diskAvatar;}

    public void setDiskAvatar(Image diskAvatar){
        this.diskAvatar = diskAvatar;
    }

    // nadpisuje domyślną metodę toString w typie String, aby rozwiązać problem z wyświetlaniem się tekstu w GUI.
    @Override
    public String toString(){
        return this.getName();
    }
}
