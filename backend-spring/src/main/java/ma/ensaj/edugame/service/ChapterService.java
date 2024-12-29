package ma.ensaj.edugame.service;

import jakarta.transaction.Transactional;
import ma.ensaj.edugame.dto.ChapterUpdateDTO;
import ma.ensaj.edugame.dto.FlipcardDTO;
import ma.ensaj.edugame.dto.MatchDTO;
import ma.ensaj.edugame.dto.QuizDTO;
import ma.ensaj.edugame.entity.*;
import ma.ensaj.edugame.exceptions.ChapterNotFoundException;
import ma.ensaj.edugame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private FlipcardRepository flipcardRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private ShortContentRepository shortContentRepository;

    // Créer ou mettre à jour un chapitre
    public Chapter saveChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    // Récupérer tous les chapitres
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    // Récupérer un chapitre par ID
    public Optional<Chapter> getChapterById(Long id) {
        return chapterRepository.findById(id);
    }

    // Supprimer un chapitre
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
    @Transactional
    public Chapter updateChapter(Long chapterId, ChapterUpdateDTO chapterUpdateDTO) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));

        // Update Chapter fields
        chapter.setChapterName(chapterUpdateDTO.getChapterName());
        chapter.setDescription(chapterUpdateDTO.getDescription());

        // Process Flipcards
        if (chapterUpdateDTO.getFlipcards() != null) {
            for (FlipcardDTO flipcardDTO : chapterUpdateDTO.getFlipcards()) {
                Flipcard flipcard = new Flipcard();
                flipcard.setFront(flipcardDTO.getFront());
                flipcard.setBack(flipcardDTO.getBack());
                flipcard.setChapter(chapter);
                flipcardRepository.save(flipcard);
            }
        }

        // Process Matches
        if (chapterUpdateDTO.getMatch() != null) {
            for (MatchDTO matchDTO : chapterUpdateDTO.getMatch()) {
                Matches matches = new Matches();
                matches.setElement(matchDTO.getElement());
                matches.setMatchText(matchDTO.getMatchText());
                matches.setChapter(chapter);
                matchRepository.save(matches);
            }
        }

        // Process Quizzes
        if (chapterUpdateDTO.getQuiz() != null) {
            for (QuizDTO quizDTO : chapterUpdateDTO.getQuiz()) {
                Quiz quiz = new Quiz();
                quiz.setQuestion(quizDTO.getQuestion());
                quiz.setExplanation(quizDTO.getExplanation());
                quiz.setAnswers(quizDTO.getAnswers());
                quiz.setCorrectAnswers(quizDTO.getCorrectAnswer());
                quiz.setChapter(chapter);
                quizRepository.save(quiz);
            }
        }

        // Process ShortContents
        if (chapterUpdateDTO.getShortContent() != null) {
            for (String content : chapterUpdateDTO.getShortContent()) {
                ShortContent shortContent = new ShortContent();
                shortContent.setContent(content);
                shortContent.setChapter(chapter);
                shortContentRepository.save(shortContent);
            }
        }

        // Save the updated Chapter
        return chapterRepository.save(chapter);
    }
    public List<Chapter> getChaptersByCourse(Long courseId) {
        return chapterRepository.findByCourseId(courseId);
    }

}
