"use client";

import React, { useState, ChangeEvent } from "react";
import { PlusCircle, MinusCircle, Upload, Save } from "lucide-react";
import api from "@/utils/api";

interface Flipcard {
    front: string;
    back: string;
}

interface MatchItem {
    element: string;
    matchText: string;
}

interface QuizItem {
    question: string;
    answers: string[];
    correctAnswer: string[];
    explanation: string;
}

interface Answer {
    chapterName: string;
    description: string;
    flipcards: Flipcard[];
    match: MatchItem[];
    quiz: QuizItem[];
    shortContent: string[];
}

type Chapter = {
    id: number;
    chapterName: string;
    description: string;
};

interface ChapterFormProps {
    chapter: Chapter | null;
}

const ChapterCreationForm = ({ chapter }: ChapterFormProps) => {
    const [activeTab, setActiveTab] = useState<'flipcards' | 'match' | 'quiz' | 'shortContent'>('flipcards');
    const [isLoading, setIsLoading] = useState(false);

    const [formData, setFormData] = useState<Answer>({
        chapterName: chapter?.chapterName || "",
        description: chapter?.description || "",
        flipcards: [],
        match: [],
        quiz: [],
        shortContent: [],
    });

    const handleFileUpload = async (event: ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file) return;

        const uploadFormData = new FormData();
        uploadFormData.append("pdf", file);

        try {
            setIsLoading(true);
            const response = await fetch("https://test-287664750302.us-central1.run.app/query-pdf", {
                method: "POST",
                body: uploadFormData,
            });
            const data = await response.json();
            setFormData(data.answer);
        } catch (error) {
            console.error("Error processing PDF:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSubmit = async () => {
        try {
            const response = await api.post(`/chapters/${chapter?.id}/data`, formData);
            if (response.status === 200 || response.status === 201) {
                alert("Chapter created successfully!");
            } else {
                alert("Error creating chapter. Please check the server logs.");
            }
        } catch (error) {
            console.error("Error creating chapter:", error);
            alert("An unexpected error occurred. Please try again.");
        }
    };

    const updateSectionItem = <T,>(section: keyof Answer, index: number, field: keyof T, value: T[keyof T]) => {
        const current = [...(formData[section] as T[])];
        current[index] = { ...current[index], [field]: value };
        setFormData({ ...formData, [section]: current });
    };

    const addItem = <T,>(section: keyof Answer, template: T) => {
        const current = formData[section] as T[];
        setFormData({ ...formData, [section]: [...current, template] });
    };

    const removeItem = <T,>(section: keyof Answer, index: number) => {
        const current = formData[section] as T[];
        setFormData({ ...formData, [section]: current.filter((_, i) => i !== index) });
    };
    

    return (
        <div className="w-full max-w-5xl mx-auto p-6 space-y-6">
            {/* Chapter Information and Upload */}
            <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                <h2 className="text-2xl font-bold">Create New Chapter</h2>
                <div className="mt-4">
                    <button
                        type="button"
                        className="flex items-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-2 rounded-lg shadow-md hover:shadow-lg transition-all"
                        onClick={() => document.getElementById("pdfUpload")?.click()}
                    >
                        <Upload size={20} />
                        Upload PDF
                    </button>
                    <input
                        id="pdfUpload"
                        type="file"
                        accept=".pdf"
                        className="hidden"
                        onChange={handleFileUpload}
                    />
                </div>
                <div className="mt-6">
                    <label className="block text-sm font-medium">Chapter Name</label>
                    <input
                        type="text"
                        value={formData.chapterName}
                        onChange={(e) => setFormData({ ...formData, chapterName: e.target.value })}
                        className="mt-1 px-4 py-2 w-full border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                    />
                </div>
                <div className="mt-6">
                    <label className="block text-sm font-medium">Description</label>
                    <textarea
                        value={formData.description}
                        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                        className="mt-1 px-4 py-2 w-full border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                        rows={4}
                    />
                </div>
            </div>

            {/* Tabs and Content */}
            <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                <div className="grid grid-cols-4 gap-4">
                    {(['flipcards', 'match', 'quiz', 'shortContent'] as const).map((tab) => (
                        <button
                            key={tab}
                            className={`p-3 rounded-lg ${
                                activeTab === tab
                                    ? "bg-indigo-500 text-white"
                                    : "bg-gray-200 hover:bg-gray-300"
                            }`}
                            onClick={() => setActiveTab(tab)}
                        >
                            {tab.charAt(0).toUpperCase() + tab.slice(1)}
                        </button>
                    ))}
                </div>

                <div className="mt-6">
                    {/* Render Flipcards */}
                    {activeTab === "flipcards" && (
                        <div>
                            <h3 className="text-xl font-bold mb-4">Flipcards</h3>
                            <button
                                onClick={() => addItem("flipcards", { front: "", back: "" })}
                                className="flex items-center gap-2 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg shadow-md transition-all"
                            >
                                <PlusCircle size={20} />
                                Add Flipcard
                            </button>
                            <div className="mt-4 space-y-4">
                                {formData.flipcards.map((card, index) => (
                                    <div key={index} className="flex items-start gap-4">
                                        <div className="flex-1">
                                            <input
                                                type="text"
                                                placeholder="Front"
                                                value={card.front}
                                                onChange={(e) =>
                                                    updateSectionItem("flipcards", index, "front", e.target.value)
                                                }
                                                className="w-full px-4 py-2 border rounded-lg"
                                            />
                                            <input
                                                type="text"
                                                placeholder="Back"
                                                value={card.back}
                                                onChange={(e) =>
                                                    updateSectionItem("flipcards", index, "back", e.target.value)
                                                }
                                                className="w-full mt-2 px-4 py-2 border rounded-lg"
                                            />
                                        </div>
                                        <button
                                            onClick={() => removeItem("flipcards", index)}
                                            className="bg-red-500 hover:bg-red-600 text-white p-2 rounded-lg"
                                        >
                                            <MinusCircle size={20} />
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* Match Items */}
                    {activeTab === "match" && (
                        <div>
                            <h3 className="text-xl font-bold mb-4">Match Items</h3>
                            <button
                                onClick={() => addItem("match", { element: "", matchText: "" })}
                                className="flex items-center gap-2 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg shadow-md transition-all"
                            >
                                <PlusCircle size={20} />
                                Add Match
                            </button>
                            <div className="mt-4 space-y-4">
                                {formData.match.map((item, index) => (
                                    <div key={index} className="flex items-start gap-4">
                                        <div className="flex-1">
                                            <input
                                                type="text"
                                                placeholder="Element"
                                                value={item.element}
                                                onChange={(e) =>
                                                    updateSectionItem("match", index, "element", e.target.value)
                                                }
                                                className="w-full px-4 py-2 border rounded-lg"
                                            />
                                            <input
                                                type="text"
                                                placeholder="Match Text"
                                                value={item.matchText}
                                                onChange={(e) =>
                                                    updateSectionItem("match", index, "matchText", e.target.value)
                                                }
                                                className="w-full mt-2 px-4 py-2 border rounded-lg"
                                            />
                                        </div>
                                        <button
                                            onClick={() => removeItem("match", index)}
                                            className="bg-red-500 hover:bg-red-600 text-white p-2 rounded-lg"
                                        >
                                            <MinusCircle size={20} />
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* Quiz Questions */}
                    {activeTab === "quiz" && (
                        <div>
                            <h3 className="text-xl font-bold mb-4">Quiz Questions</h3>
                            <button
                                onClick={() =>
                                    addItem("quiz", {
                                        question: "",
                                        answers: [],
                                        correctAnswer: [],
                                        explanation: "",
                                    })
                                }
                                className="flex items-center gap-2 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg shadow-md transition-all"
                            >
                                <PlusCircle size={20} />
                                Add Question
                            </button>
                            <div className="mt-4 space-y-4">
                                {formData.quiz.map((quiz, index) => (
                                    <div key={index} className="p-4 border rounded-lg">
                                        <div className="space-y-4">
                                            <input
                                                type="text"
                                                placeholder="Question"
                                                value={quiz.question}
                                                onChange={(e) =>
                                                    updateSectionItem("quiz", index, "question", e.target.value)
                                                }
                                                className="w-full px-4 py-2 border rounded-lg"
                                            />
                                            <textarea
                                                placeholder="Explanation"
                                                value={quiz.explanation}
                                                onChange={(e) =>
                                                    updateSectionItem("quiz", index, "explanation", e.target.value)
                                                }
                                                className="w-full px-4 py-2 border rounded-lg"
                                                rows={3}
                                            />
                                        </div>
                                        <button
                                            onClick={() => removeItem("quiz", index)}
                                            className="bg-red-500 hover:bg-red-600 text-white p-2 rounded-lg mt-2"
                                        >
                                            <MinusCircle size={20} />
                                            Remove
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* Short Content */}
                    {activeTab === "shortContent" && (
                        <div>
                            <h3 className="text-xl font-bold mb-4">Short Content</h3>
                            <button
                                onClick={() => addItem("shortContent", "")}
                                className="flex items-center gap-2 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg shadow-md transition-all"
                            >
                                <PlusCircle size={20} />
                                Add Content
                            </button>
                            <div className="mt-4 space-y-4">
                                {formData.shortContent.map((content, index) => (
                                    <div key={index} className="flex gap-4">
                                        <textarea
                                            value={content}
                                            onChange={(e) => {
                                                const current = [...formData.shortContent];
                                                current[index] = e.target.value;
                                                setFormData({ ...formData, shortContent: current });
                                            }}
                                            placeholder="Enter short content"
                                            className="flex-1 px-4 py-2 border rounded-lg"
                                        />
                                        <button
                                            onClick={() => removeItem("shortContent", index)}
                                            className="bg-red-500 hover:bg-red-600 text-white p-2 rounded-lg"
                                        >
                                            <MinusCircle size={20} />
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
            </div>

            {/* Submit Button */}
            <button
                onClick={handleSubmit}
                disabled={isLoading}
                className="w-full bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-3 rounded-lg shadow-md hover:shadow-lg transition-all"
            >
                <Save size={20} />
                {isLoading ? "Processing..." : "Save Chapter"}
            </button>
        </div>
    );
};

export default ChapterCreationForm;
