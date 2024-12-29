"use client";

import { useParams } from "next/navigation";
import Chapters from "@/components/Dashboard/Chapters/Chapters";
import Navbar from "@/components/Dashboard/Navbar/Navbar";

const ChaptersPage = () => {
    const { idCourse } = useParams();

    if (!idCourse) {
        return <p>Loading...</p>;
    }

    return (
        <div className="flex flex-col min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800">
            <Navbar />
            <main className="flex-grow container mx-auto px-4 sm:px-6 lg:px-8">
                <div className="mt-24 max-w-7xl mx-auto">
                    <div className="backdrop-blur-xl bg-white/70 dark:bg-slate-800/70 rounded-2xl shadow-lg border border-gray-100 dark:border-gray-700">
                        <div className="p-6 sm:p-8">
                            <h1 className="text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 dark:from-indigo-400 dark:to-purple-400 bg-clip-text text-transparent mb-8">
                                Chapters for the Course
                            </h1>
                            <div className="space-y-6">
                                <Chapters courseId={Number(idCourse)} />
                            </div>
                        </div>
                    </div>
                </div>
            </main>
            <footer className="mt-auto py-6 text-center text-sm text-gray-500 dark:text-gray-400">
                © 2024 Your Company. All rights reserved.
            </footer>
        </div>
    );
};

export default ChaptersPage;
