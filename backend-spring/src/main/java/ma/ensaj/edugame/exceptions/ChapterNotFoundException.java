package ma.ensaj.edugame.exceptions;


public class ChapterNotFoundException extends RuntimeException {
    public ChapterNotFoundException(Long id) {
        super("Chapter not found with id: " + id);
    }
}