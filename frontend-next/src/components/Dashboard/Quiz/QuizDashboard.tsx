"use client";

import React, { useState, ChangeEvent } from "react";
import axios from "axios";
import { PlusCircle, MinusCircle, Upload } from "lucide-react";

type QuizItem = {
    question: string;
    answers: string[];
    correctAnswer: string[];
    explanation: string;
};

type QuizFormProps = {
    chapterId: number;
};

const QuizForm = ({ chapterId }: QuizFormProps) => {
    const [quizItems, setQuizItems] = useState<QuizItem[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    const handleFileUpload = async (event: ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("pdf", file);

        try {
            setIsLoading(true);
            const response = await fetch("https://test-287664750302.us-central1.run.app/quiz-query", {
                method: "POST",
                body: formData,
            });
            const data = await response.json();

            if (data.answer?.quiz) {
                setQuizItems([...quizItems, ...data.answer.quiz]);
            }
        } catch (error) {
            console.error("Error processing PDF:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const addManualQuiz = () => {
        setQuizItems([...quizItems, { question: "", answers: [], correctAnswer: [], explanation: "" }]);
    };

    const removeQuiz = (index: number) => {
        setQuizItems(quizItems.filter((_, i) => i !== index));
    };

    const updateQuiz = (index: number, field: keyof QuizItem, value: string | string[]) => {
        const updatedQuizzes = [...quizItems];
        updatedQuizzes[index] = { ...updatedQuizzes[index], [field]: value };
        setQuizItems(updatedQuizzes);
    };

    const handleSubmit = async () => {
        try {
            for (const quiz of quizItems) {
                await axios.post(`/api/chapters/${chapterId}/quizzes`, quiz);
            }
            alert("Quizzes added successfully!");
        } catch (error) {
            console.error("Error adding quizzes:", error);
            alert("Failed to add quizzes.");
        }
    };

    return (
        <div className="w-full max-w-4xl mx-auto space-y-6">
            {/* PDF Upload */}
            <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                <button
                    type="button"
                    className="flex items-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-2 rounded-lg shadow-md hover:shadow-lg transition-all"
                    onClick={() => document.getElementById("quizUpload")?.click()}
                >
                    <Upload size={20} />
                    Upload PDF
                </button>
                <input
                    id="quizUpload"
                    type="file"
                    accept=".pdf"
                    className="hidden"
                    onChange={handleFileUpload}
                />
            </div>

            {/* Manual Add */}
            <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                <div className="flex justify-between items-center mb-4">
                    <h3 className="text-xl font-bold">Quiz Questions</h3>
                    <button
                        type="button"
                        className="flex items-center gap-2 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg shadow-md transition-all"
                        onClick={addManualQuiz}
                    >
                        <PlusCircle size={20} />
                        Add Question
                    </button>
                </div>

                {isLoading ? (
                    <p className="text-gray-500">Loading quizzes...</p>
                ) : (
                    <div className="space-y-6">
                        {quizItems.map((quiz, index) => (
                            <div key={index} className="p-6 bg-gray-50 rounded-lg shadow-sm border border-gray-200">
                                <div className="space-y-4">
                                    {/* Question */}
                                    <input
                                        type="text"
                                        placeholder="Question"
                                        value={quiz.question}
                                        onChange={(e) => updateQuiz(index, "question", e.target.value)}
                                        className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                                    />

                                    {/* Answers */}
                                    <div>
                                        <label className="text-sm font-medium">Answers (semicolon-separated)</label>
                                        <input
                                            type="text"
                                            placeholder="Option 1; Option 2; Option 3"
                                            value={quiz.answers.join("; ")}
                                            onChange={(e) =>
                                                updateQuiz(
                                                    index,
                                                    "answers",
                                                    e.target.value.split(";").map((a) => a.trim())
                                                )
                                            }
                                            className="w-full mt-1 px-4 py-2 border rounded-lg"
                                        />
                                    </div>

                                    {/* Correct Answers */}
                                    <div>
                                        <label className="text-sm font-medium">Correct Answers (semicolon-separated)</label>
                                        <input
                                            type="text"
                                            placeholder="Correct answer(s)"
                                            value={quiz.correctAnswer.join("; ")}
                                            onChange={(e) =>
                                                updateQuiz(
                                                    index,
                                                    "correctAnswer",
                                                    e.target.value.split(";").map((a) => a.trim())
                                                )
                                            }
                                            className="w-full mt-1 px-4 py-2 border rounded-lg"
                                        />
                                    </div>

                                    {/* Explanation */}
                                    <div>
                                        <label className="text-sm font-medium">Explanation</label>
                                        <textarea
                                            placeholder="Explanation"
                                            value={quiz.explanation}
                                            onChange={(e) => updateQuiz(index, "explanation", e.target.value)}
                                            className="w-full mt-1 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                                            rows={3}
                                        />
                                    </div>
                                </div>

                                {/* Remove Button */}
                                <button
                                    type="button"
                                    className="flex items-center gap-2 bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg mt-4"
                                    onClick={() => removeQuiz(index)}
                                >
                                    <MinusCircle size={20} />
                                    Remove Question
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Submit Button */}
            <button
                type="button"
                onClick={handleSubmit}
                className="w-full flex items-center justify-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-3 rounded-lg shadow-md hover:shadow-lg transition-all"
            >
                Submit Quizzes
            </button>
        </div>
    );
};

export default QuizForm;
