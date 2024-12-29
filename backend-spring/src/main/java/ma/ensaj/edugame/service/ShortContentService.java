package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.ShortContent;
import ma.ensaj.edugame.repository.ShortContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShortContentService {

    @Autowired
    private ShortContentRepository shortContentRepository;

    public ShortContent saveShortContent(ShortContent shortContent) {
        return shortContentRepository.save(shortContent);
    }

    public List<ShortContent> getShortContentsByChapterId(Long chapterId) {
        return shortContentRepository.findByChapterId(chapterId);
    }
    public List<ShortContent> getAllShortContents() {
        return shortContentRepository.findAll();
    }

    public Optional<ShortContent> getShortContentById(Long id) {
        return shortContentRepository.findById(id);
    }

    public void deleteShortContent(Long id) {
        shortContentRepository.deleteById(id);
    }
}
