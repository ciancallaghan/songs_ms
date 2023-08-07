package de.htwb.ai.songservice.controller;

import de.htwb.ai.songservice.exception.ResourceNotFoundException;
import de.htwb.ai.songservice.model.Playlist;
import de.htwb.ai.songservice.model.Song;
import de.htwb.ai.songservice.repo.PlaylistRepository;
import de.htwb.ai.songservice.repo.SongRepository;
import de.htwb.ai.songservice.util.TokenValidator;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    public PlaylistController(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    @GetMapping
    public ResponseEntity<?> getByUserId(@RequestParam(value = "userId") String userId,
                                         @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            String loggedIn = tokenValidator.validate(token);
            List<HashMap> playlists = new ArrayList<>();
            for (Playlist pl : playlistRepository.findAll()) {
                HashMap<String, Object> map = new HashMap<>();
                if (loggedIn.equals(userId) && pl.getUserid().equals(userId)) {
                    map.put("id", pl.getId());
                    map.put("userid", pl.getUserid());
                    map.put("name", pl.getName());
                    map.put("is_private", pl.getIs_private());
                    map.put("songs", pl.getPlaylist());
                    playlists.add(map);
                } else if (!loggedIn.equals(userId) && pl.getUserid().equals(userId) && pl.getIs_private().equals(Boolean.FALSE)) {
                    map.put("id", pl.getId());
                    map.put("userid", pl.getUserid());
                    map.put("name", pl.getName());
                    map.put("is_private", pl.getIs_private());
                    map.put("songs", pl.getPlaylist());
                    playlists.add(map);
                }
            }
            if (playlists.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(playlists, HttpStatus.OK);
            }
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id,
                                     @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            String loggedIn = tokenValidator.validate(token);
            Playlist pl = playlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));
            if (pl.getUserid().equals(loggedIn)) {
                return new ResponseEntity<>(pl, HttpStatus.OK);
            } else if (!pl.getUserid().equals(loggedIn) && pl.getIs_private().equals(Boolean.FALSE)) {
                return new ResponseEntity<>(pl, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping
    public ResponseEntity<?> postPlaylist(@RequestBody Playlist playlist,
                                          @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            String loggedIn = tokenValidator.validate(token);
            playlist.setUserid(loggedIn);
            for (Song song : playlist.getPlaylist()) {
                Song songToCheck = songRepository.findById(song.getId()).orElseThrow(() -> new ResourceNotFoundException("Note", "id", song.getId()));
                if (!song.getId().equals(songToCheck.getId())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            Playlist savedPlaylist = playlistRepository.save(playlist);
            String path = "/playlist/" + savedPlaylist.getId();
            HttpHeaders headers = new HttpHeaders();
            headers.set("location", path);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putPlaylist(@PathVariable(value = "id") Long id,
                                         @RequestBody Playlist playlistToPut,
                                         @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            String loggedIn = tokenValidator.validate(token);
            Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));
            if (playlist.getUserid().equals(loggedIn)) {
                if (playlistToPut.getUserid() != null)
                    playlist.setUserid(playlistToPut.getUserid());
                if (playlistToPut.getName() != null)
                    playlist.setName(playlistToPut.getName());
                if (playlistToPut.getIs_private() != null)
                    playlist.setIs_private(playlistToPut.getIs_private());
                if (playlistToPut.getPlaylist() != null)
                    playlist.setPlaylist(playlistToPut.getPlaylist());
                playlistRepository.save(playlist);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable(value = "id") Long id,
                                            @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            String loggedIn = tokenValidator.validate(token);
            Playlist pl = playlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));
            if (pl.getUserid().equals(loggedIn)) {
                playlistRepository.delete(pl);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
