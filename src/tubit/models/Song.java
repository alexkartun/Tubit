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
    private String artist;
    private String album;
    private int duration;
    private int yearReleased;
    
    public Song(int id, String name, int duration, int year, String artist, String album) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.yearReleased = year;
        this.artist = artist;
        this.album = album;
    }
    
    public String getName() {
        return name;
    }
}
