package com.example._studio_nagran;

import javafx.scene.image.Image;

public class Song {
    private int id;
    private String name;
    private String authorName;
    private Image songAvatar;
    private String diskName;

    public Song(int id, String name, String authorName, String diskName, Image songAvatar){
        super();
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.diskName = diskName;
        this.songAvatar = songAvatar;
    }
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
    public void setSongAvatar(Image songAvatar){
        this.songAvatar = songAvatar;
    }
    public void setAuthorName(String authorName){
        this.authorName = authorName;
    }
    public String getAuthorName(){ return authorName;}
    public Image getSongAvatar(){ return songAvatar;}


    public String getRelatedTo(){
        return diskName;
    }
    public void setRelatedTo(String diskName){
        this.diskName = diskName;
    }
    @Override
    public String toString(){
        return this.getName();
    }
}
