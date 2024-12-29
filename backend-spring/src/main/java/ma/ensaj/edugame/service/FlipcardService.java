package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.Flipcard;
import ma.ensaj.edugame.repository.FlipcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlipcardService {

    @Autowired
    private FlipcardRepository flipcardRepository;
    public List<Flipcard> getFlipcardsByChapterId(Long chapterId) {
        return flipcardRepository.findByChapter_Id(chapterId);
    }

    public Flipcard saveFlipcard(Flipcard flipcard) {
        return flipcardRepository.save(flipcard);
    }

    public List<Flipcard> getAllFlipcards() {
        return flipcardRepository.findAll();
    }

    public Optional<Flipcard> getFlipcardById(Long id) {
        return flipcardRepository.findById(id);
    }

    public void deleteFlipcard(Long id) {
        flipcardRepository.deleteById(id);
    }
}
