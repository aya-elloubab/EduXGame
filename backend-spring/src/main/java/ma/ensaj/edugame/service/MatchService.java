package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.Matches;
import ma.ensaj.edugame.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;


    public List<Matches> getMatchesByChapterId(Long chapterId) {
        return matchRepository.findByChapterId(chapterId);
    }

    public Matches saveMatch(Matches match) {
        return matchRepository.save(match);
    }

    public List<Matches> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Matches> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
