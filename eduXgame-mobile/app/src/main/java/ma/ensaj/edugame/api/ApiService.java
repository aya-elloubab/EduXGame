package ma.ensaj.edugame.api;

import java.util.List;
import java.util.Map;

import ma.ensaj.edugame.models.Branch;
import ma.ensaj.edugame.models.Chapter;
import ma.ensaj.edugame.models.Course;
import ma.ensaj.edugame.models.Flipcard;
import ma.ensaj.edugame.models.LeaderboardEntry;
import ma.ensaj.edugame.models.Flipcard;
import ma.ensaj.edugame.models.Level;
import ma.ensaj.edugame.models.LoginRequest;
import ma.ensaj.edugame.models.LoginResponse;
import ma.ensaj.edugame.models.Match;
import ma.ensaj.edugame.models.Planet;
import ma.ensaj.edugame.models.Quiz;
import ma.ensaj.edugame.models.RegisterRequest;
import ma.ensaj.edugame.models.RegisterResponse;
import ma.ensaj.edugame.models.ShortContent;
import ma.ensaj.edugame.models.StudentActivityStatsDto;
import ma.ensaj.edugame.models.StudentAvatar;
import ma.ensaj.edugame.models.StudentStats;
import ma.ensaj.edugame.models.Subject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/register/student")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    @GET("api/students/levels")
    Call<List<Level>> getAllLevels();

    @PUT("api/students/{id}/level-branch")
    Call<ResponseBody> updateLevelAndBranch(
            @Path("id") Long studentId,
            @Body Map<String, Long> payload
    );

    @GET("/api/levels")
    Call<List<Level>> getLevels();

    @GET("/api/branches/level/{levelId}")
    Call<List<Branch>> getBranchesByLevel(@Path("levelId") Long levelId);


    @GET("/api/students/{id}/level-branch")
    Call<Map<String, Long>> getStudentLevelAndBranch(@Path("id") Long studentId);

    @GET("/api/students/{id}/profile")
    Call<Map<String, String>> getStudentProfile(@Path("id") Long id);
    @GET("/api/subjects/branch/{branchId}")
    Call<List<Subject>> getSubjectsByBranch(@Path("branchId") Long branchId);
    @GET("/api/chapters/subject/{subjectId}")
    Call<List<Chapter>> getChaptersBySubject(@Path("subjectId") Long subjectId);
    @GET("api/courses/subject/{subjectId}")
    Call<List<Course>> getCoursesBySubject(@Path("subjectId") Long subjectId);
    @GET("api/chapters/course/{courseId}")
    Call<List<Chapter>> getChaptersByCourse(@Path("courseId") Long courseId);
    @GET("/api/chapter-progress/progress")
    Call<Double> getChapterProgress(@Query("studentId") Long studentId, @Query("chapterId") Long chapterId);
    @GET("/api/chapter-progress/course-progress")
    Call<Double> getCourseProgress(@Query("studentId") Long studentId, @Query("courseId") Long chapterId);
    @GET("/api/chapter-progress/subject-progress")
    Call<Double> getSubjectProgress(@Query("studentId") Long studentId, @Query("subjectId") Long chapterId);
    @GET("/api/matches")
    Call<List<Match>> getMatchesByChapter(@Query("chapterId") Long chapterId);

    @POST("/api/chapter-progress/complete/match-game")
    Call<Void> submitMatchScore(@Query("studentId") Long studentId, @Query("chapterId") Long chapterId, @Query("score") int score);
    @POST("/api/points/{studentId}/{chapterId}/match")
    Call<Void> addScoreToPoints(
            @Path("studentId") Long studentId,
            @Path("chapterId") Long chapterId,
            @Body int score
    );
    @GET("/api/shortContents/chapter/{chapterId}")
    Call<List<ShortContent>> getShortContentsByChapter(@Path("chapterId") Long chapterId);
    @POST("/api/chapter-progress/complete/short-content")
    Call<Void> markShortContentAsComplete(
            @Query("studentId") Long studentId,
            @Query("chapterId") Long chapterId
    );
    @GET("/api/flipcards/chapter/{chapterId}")
    Call<List<Flipcard>> getFlashcardsByChapter(@Path("chapterId") Long chapterId);
    @POST("/api/chapter-progress/complete/flipcard")
    Call<Void> markFlipcardsAsComplete(
            @Query("studentId") Long studentId,
            @Query("chapterId") Long chapterId
    );
    @GET("/api/student-stats")
    Call<StudentActivityStatsDto> getStats(@Query("studentId") Long studentId);

    @GET("api/planets/unlocked")
    Call<List<Planet>> getUnlockedPlanets(@Query("studentId") Long studentId);
    @GET("api/planets/unlocked/all")
    Call<List<Planet>> getAllUnlockedPlanets(@Query("studentId") Long studentId);
    @GET("/api/quizzes/chapter/{chapterId}")
    Call<List<Quiz>> getQuizzesByChapter(@Path("chapterId") Long chapterId);

    @POST("/api/points/{studentId}/{chapterId}/quiz")
    Call<Void> submitQuizResults(@Path("studentId") Long studentId,@Path("chapterId") Long chapterId, @Body int points);
    @GET("/api/avatars/students/{studentId}")
    Call<List<StudentAvatar>> getStudentAvatars(@Path("studentId") Long studentId);

    @GET("/api/points/{studentId}/totalScore")
    Call<Integer> getTotalScore(@Path("studentId") Long studentId);
    @GET("/api/student-stats")
    Call<StudentStats> getStudentStats(@Query("studentId") Long studentId);

    @GET("/api/study-sessions/{studentId}/total-hours")
    Call<Integer> getTotalStudyHours(@Path("studentId") Long studentId);
    @GET("/api/points/leaderboard/{studentId}")
    Call<List<LeaderboardEntry>> getLeaderboard(@Path("studentId") Long studentId);
    @GET("/api/avatars/students/{studentId}/most-collected")
    Call<StudentAvatar> getMostCollectedAvatar(@Path("studentId") Long studentId);
    @POST("/api/avatars/students/{studentId}/claim/{avatarId}")
    Call<Void> collectAvatar(@Path("studentId") Long studentId, @Path("avatarId") Long avatarId);

    @GET("api/planets")
    Call<List<Planet>> getAllPlanets(@Query("studentId") Long studentId);
    @GET("api/chapter-progress/completed-courses-count")
    Call<Integer> getCompletedCoursesCount(@Query("studentId") Long studentId);
    @GET("/api/streaks/{studentId}")
    Call<Integer> getStreak(@Path("studentId") Long studentId);
    @POST("/api/streaks/{studentId}/update")
    Call<Void> updateStreak(@Path("studentId") long studentId);
    @POST("/api/chapter-progress/complete/quiz")
    Call<Void> markQuizComplete(
            @Query("studentId") Long studentId,
            @Query("chapterId") Long chapterId
    );

}