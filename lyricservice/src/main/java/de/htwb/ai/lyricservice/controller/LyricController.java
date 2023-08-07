package de.htwb.ai.lyricservice.controller;

import com.jagrosh.jlyrics.Lyrics;
import com.jagrosh.jlyrics.LyricsClient;
import de.htwb.ai.lyricservice.model.Song;
import de.htwb.ai.lyricservice.repo.LyricRepository;
import de.htwb.ai.lyricservice.util.TokenValidator;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/lyric")
public class LyricController {
	private final LyricRepository lyricRepository;

	public LyricController(LyricRepository lyricRepository) {
		this.lyricRepository = lyricRepository;
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getLyricsBySongId(@PathVariable(value = "id") Long id,
											   @RequestHeader(name = "Authorization", required = false) String token) {
		if (token == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		try {
			TokenValidator tokenValidator = new TokenValidator();
			tokenValidator.validate(token);
			Song song = lyricRepository.getLyricsBySongId(id, token);
			LyricsClient client = new LyricsClient();
			Lyrics lyrics = client.getLyrics(song.getTitle() + " " + song.getArtist()).get();
			if (lyrics == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(lyrics.getContent(), HttpStatus.OK);
		} catch (JwtException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}


