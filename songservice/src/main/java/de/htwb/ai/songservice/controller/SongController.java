package de.htwb.ai.songservice.controller;

import de.htwb.ai.songservice.model.Song;
import de.htwb.ai.songservice.repo.SongRepository;
import de.htwb.ai.songservice.exception.ResourceNotFoundException;
import de.htwb.ai.songservice.util.TokenValidator;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/song")
public class SongController {
    private final SongRepository songRepository;

    public SongController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllSongs(@RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            tokenValidator.validate(token);
            return new ResponseEntity<>(songRepository.findAll(), HttpStatus.OK);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable("id") Long id,
                                         @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            tokenValidator.validate(token);
            return new ResponseEntity<>(songRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Song", "id", id)), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    public ResponseEntity<?> createSong(@RequestBody Song song,
                                        @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            tokenValidator.validate(token);
            Song savedSong = songRepository.save(song);
            String path = "/song/" + savedSong.getId();
            HttpHeaders headers = new HttpHeaders();
            headers.set("location", path);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSong(@PathVariable(value = "id") Long id,
                                        @RequestBody Song songToPut,
                                        @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            tokenValidator.validate(token);
            Song song = songRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));
            if (songToPut.getTitle() != null)
                song.setTitle(songToPut.getTitle());
            if (songToPut.getArtist() != null)
                song.setArtist(songToPut.getArtist());
            if (songToPut.getLabel() != null)
                song.setLabel(songToPut.getLabel());
            if (songToPut.getReleased() != null)
                song.setReleased(songToPut.getReleased());
            songRepository.save(song);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable(value = "id") Long id,
                                        @RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            TokenValidator tokenValidator = new TokenValidator();
            tokenValidator.validate(token);
            Song song = songRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
            songRepository.delete(song);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
