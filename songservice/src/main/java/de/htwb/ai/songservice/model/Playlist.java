package de.htwb.ai.songservice.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userid;
    private String name;
    private Boolean is_private;

    @ManyToMany
    @JoinTable(
            name = "playlist_song",
            joinColumns = { @JoinColumn(name = "playlist_id") },
            inverseJoinColumns = { @JoinColumn(name = "song_id")}
    )
    private Set<Song> playlist;

    public Playlist() {

    }

    public Playlist(String userid, String name, Boolean is_private) {
        this.userid = userid;
        this.name = name;
        this.is_private = is_private;
    }

    public Long getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public Boolean getIs_private() {
        return is_private;
    }

    public Set<Song> getPlaylist() {
        return playlist;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIs_private(Boolean is_private) {
        this.is_private = is_private;
    }

    public void setPlaylist(Set<Song> songs) {
        this.playlist = songs;
    }
}
