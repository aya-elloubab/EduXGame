"use client";

import React, { useState } from "react";
import { BookOpen, Library, Brain, CheckSquare, FileText, X } from "lucide-react";
import ChapterForm from "./ChapterForm";

interface Chapter {
    id: number;
    chapterName: string;
    description: string;
    flipcardCount: number;
    quizCount: number;
    matchCount: number;
    shortContentCount: number;
}

interface ChapterOverviewProps {
    chapter: Chapter | null;
}

const ChapterOverview: React.FC<ChapterOverviewProps> = ({ chapter }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const stats = [
        {
            title: "Flipcards",
            value: chapter?.flipcardCount || 0,
            icon: Library,
            description: "Interactive flashcards",
            color: "text-blue-500",
            bgColor: "bg-blue-50",
            href: `/dashboard/flipcard/${chapter?.id}`,
        },
        {
            title: "Quizzes",
            value: chapter?.quizCount || 0,
            icon: Brain,
            description: "Knowledge tests",
            color: "text-purple-500",
            bgColor: "bg-purple-50",
            href: `/dashboard/quiz/${chapter?.id}`,
        },
        {
            title: "Match Exercises",
            value: chapter?.matchCount || 0,
            icon: CheckSquare,
            description: "Matching activities",
            color: "text-green-500",
            bgColor: "bg-green-50",
            href: `/dashboard/match/${chapter?.id}`,
        },
        {
            title: "Short Contents",
            value: chapter?.shortContentCount || 0,
            icon: FileText,
            description: "Brief learning materials",
            color: "text-orange-500",
            bgColor: "bg-orange-50",
            href: `/dashboard/short-content/${chapter?.id}`,
        },
    ];

    return (
        <div className="w-full max-w-7xl mx-auto p-6 space-y-8 overflow-y-auto">
            {/* Chapter Header */}
            <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                <div className="flex items-center gap-4">
                    <div className="p-4 rounded-full bg-blue-50">
                        <BookOpen className="h-8 w-8 text-blue-500" />
                    </div>
                    <div>
                        <h1 className="text-3xl font-bold text-gray-800">
                            {chapter?.chapterName || "Chapter Title"}
                        </h1>
                        <p className="text-lg text-gray-500">Chapter Overview</p>
                    </div>
                </div>
                <p className="mt-4 text-gray-600 leading-relaxed">
                    {chapter?.description || "No description available"}
                </p>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="mt-6 flex items-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-2 rounded-lg shadow-md hover:shadow-lg transition-all"
                >
                    Generate from PDF
                </button>
            </div>

            {/* Stats Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {stats.map((stat, index) => (
                    <a
                        href={stat.href}
                        key={index}
                        className="block no-underline"
                        onClick={(e) => {
                            e.preventDefault();
                            window.location.href = stat.href;
                        }}
                    >
                        <div className="bg-white rounded-lg border shadow hover:shadow-lg hover:scale-105 transition-all duration-200">
                            <div className={`p-4 ${stat.bgColor} rounded-t-lg`}>
                                <div className="flex items-center justify-between">
                                    <h3 className="text-base font-semibold text-gray-700">{stat.title}</h3>
                                    <div className="p-2 rounded-full">
                                        <stat.icon className={`h-6 w-6 ${stat.color}`} />
                                    </div>
                                </div>
                            </div>
                            <div className="p-4">
                                <span className="text-3xl font-bold">{stat.value}</span>
                                <p className="text-sm text-gray-500">{stat.description}</p>
                                <p className="mt-2 text-xs text-indigo-600">Click to view {stat.title.toLowerCase()} →</p>
                            </div>
                        </div>
                    </a>
                ))}
            </div>

            {/* Modal */}
            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 overflow-y-auto">
                    <div className="bg-white rounded-xl shadow-lg w-full max-w-3xl max-h-[90vh] overflow-y-auto p-6">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-xl font-bold">Generate Chapter from PDF</h2>
                            <button
                                onClick={() => setIsModalOpen(false)}
                                className="flex items-center gap-1 text-red-500 hover:text-red-600"
                            >
                                <X size={20} />
                                Close
                            </button>
                        </div>
                        <ChapterForm chapter={chapter} />
                    </div>
                </div>
            )}
        </div>
    );
};

export default ChapterOverview;
