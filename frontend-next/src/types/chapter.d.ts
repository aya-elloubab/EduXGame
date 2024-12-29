export type Flipcard = {
    id?: number; // Optional for new entries
    front: string;
    back: string;
};

export type Match = {
    id?: number;
    element: string;
    matchText: string;
};

export type Quiz = {
    id?: number; // Optional for new entries
    question: string;
    answers: string[];
    correctAnswer: string[];
    explanation: string; // Explanation for the correct answer
};
export type ShortContent = {
    id?: number;
    content: string;
};

export type Chapter = {
    id: number; // Optional for new chapters
    chapterName: string;
    description: string;
    course: { id: courseId }, // Keep the course association
    flipcards: Flipcard[];
    match: Match[];
    quiz: Quiz[];
    shortContent: ShortContent[];
};
