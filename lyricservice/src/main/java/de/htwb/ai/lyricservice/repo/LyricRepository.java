package de.htwb.ai.lyricservice.repo;

import de.htwb.ai.lyricservice.model.Song;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "lyricservice", url = "http://localhost:8080/song/")
public interface LyricRepository {
	@GetMapping("/{id}")
	Song getLyricsBySongId(@PathVariable("id") Long id, @RequestHeader(name = "Authorization", required = false) String token);
}
