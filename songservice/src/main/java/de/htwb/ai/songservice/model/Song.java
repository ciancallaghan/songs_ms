package de.htwb.ai.songservice.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String artist;

    private String label;

    private Integer released;

    @ManyToMany(mappedBy = "playlist")
    private Set<Playlist> song;

    public Song() {
    }

    public Song(String title, String artist, String label, Integer released) {
        this.title = title;
        this.artist = artist;
        this.label = label;
        this.released = released;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getArtist() { return artist; }

    public String getLabel() { return label; }

    public Integer getReleased() { return released; }

    public void setTitle(String title) { this.title = title; }

    public void setArtist(String artist) { this.artist = artist; }

    public void setLabel(String label) { this.label = label; }

    public void setReleased(Integer released) { this.released = released; }
}
