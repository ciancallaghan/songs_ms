package de.htwb.ai.songservice.repo;

import de.htwb.ai.songservice.model.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends CrudRepository<Song, Long> {
    List<Song> findById(long Id);
}
