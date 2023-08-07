package de.htwb.ai.songservice.repo;

import de.htwb.ai.songservice.model.Playlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Long> {
    List<Playlist> getById(Long id);
}
