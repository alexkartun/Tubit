/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

/**
 *
 * @author Ofir
 */
public class Song {
    private int id;
    private String name;
    private String singer;
    private String album;
    private int duration;
    private int yearReleased;
    
    public Song(int id, String name, int duration, int year, String singer, String album) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.yearReleased = year;
        this.singer = singer;
        this.album = album;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSinger() {
        return singer;
    }
    
    public String getAlbum() {
        return album;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public int getYear() {
        return yearReleased;
    }
}
