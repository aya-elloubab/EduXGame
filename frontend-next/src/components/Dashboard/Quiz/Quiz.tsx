"use client"
import React, { useState } from 'react';
import QuizForm from './QuizDashboard';

const Quiz = ({ chapterId }:{ chapterId: number }) => {

    const [isModalOpen, setIsModalOpen] = useState(false);
    return (
        <div className="container mx-auto p-6">
            <h1 className="text-2xl font-bold mb-4">Manage Quizzes for Chapter {chapterId}</h1>
        
            <button
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                onClick={() => setIsModalOpen(true)}
            >
                Add Quizzes
            </button>
                {isModalOpen && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-51">
                    {/* Modal container with max height and scroll */}
                    <div className="bg-white rounded-lg shadow-lg w-full max-w-3xl max-h-[80vh] overflow-y-auto p-4">
                        {/* Modal header */}
                        <div className="flex justify-between items-center mb-4">
                        <h2 className="text-xl font-bold">Add Quizzes</h2>
                        <button
                            onClick={() => setIsModalOpen(false)}
                            className="text-red-500 hover:text-red-700"
                        >
                            Close
                        </button>
                        </div>

                        {/* Quiz form component inside the modal */}
                        <QuizForm chapterId={chapterId} />
                    </div>
                </div>
                )}
        </div>
    );
};

export default Quiz;