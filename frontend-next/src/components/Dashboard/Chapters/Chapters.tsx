"use client";

import { useEffect, useState } from "react";
import { getChaptersByCourse, createChapter, updateChapter, deleteChapter } from "@/Services/chapterService";
import { Chapter } from "@/types/chapter";
import Link from "next/link";
import { Plus, Pencil, Trash2, Save, X } from "lucide-react";

type ChaptersProps = {
    courseId: number;
};

const Chapters = ({ courseId }: ChaptersProps) => {
    const [chapters, setChapters] = useState<Chapter[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingChapter, setEditingChapter] = useState<Chapter | null>(null);
    const [chapterData, setChapterData] = useState({ chapterName: "", description: "" });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchChapters();
    }, [courseId]);

    const fetchChapters = async () => {
        setLoading(true);
        try {
            const data = await getChaptersByCourse(courseId);
            setChapters(data);
        } catch (error) {
            console.error("Error fetching chapters:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleSaveChapter = async () => {
        if (!chapterData.chapterName.trim() || !chapterData.description.trim()) return;

        try {
            if (editingChapter) {
                await updateChapter(editingChapter.id, {
                    chapterName: chapterData.chapterName,
                    description: chapterData.description,
                    course: { id: courseId },
                });
            } else {
                await createChapter({
                    chapterName: chapterData.chapterName,
                    description: chapterData.description,
                    course: { id: courseId },
                });
            }

            setChapterData({ chapterName: "", description: "" });
            setEditingChapter(null);
            setShowModal(false);
            fetchChapters();
        } catch (error) {
            console.error("Error saving chapter:", error);
        }
    };

    const handleDeleteChapter = async (id: number) => {
        try {
            await deleteChapter(id);
            fetchChapters();
        } catch (error) {
            console.error("Error deleting chapter:", error);
        }
    };

    const openModal = (chapter?: Chapter) => {
        if (chapter) {
            setEditingChapter(chapter);
            setChapterData({ chapterName: chapter.chapterName, description: chapter.description });
        } else {
            setEditingChapter(null);
            setChapterData({ chapterName: "", description: "" });
        }
        setShowModal(true);
    };

    return (
        <div className="w-full max-w-4xl mx-auto">
            <div className="mb-8 bg-white/80 backdrop-blur-lg rounded-xl p-6 shadow-lg border border-gray-100">
                <button
                    onClick={() => openModal()}
                    className="flex items-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-2 rounded-lg transition-all shadow-md hover:shadow-lg"
                >
                    <Plus size={20} />
                    Add Chapter
                </button>
            </div>

            <div className="bg-white/80 backdrop-blur-lg rounded-xl shadow-lg border border-gray-100 overflow-hidden">
                {loading ? (
                    <p className="p-6 text-gray-500">Loading chapters...</p>
                ) : chapters.length === 0 ? (
                    <p className="p-6 text-gray-500">No chapters available for this course.</p>
                ) : (
                    <table className="w-full">
                        <thead className="bg-gray-50/50">
                            <tr>
                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-600">Chapter Name</th>
                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-600">Description</th>
                                <th className="px-6 py-4 text-right text-sm font-semibold text-gray-600">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {chapters.map((chapter) => (
                                <tr key={chapter.id} className="hover:bg-gray-50/50 transition-colors">
                                    <td className="px-6 py-4">
                                        <Link
                                            href={`/dashboard/chapter/${chapter.id}/overview`}
                                            className="text-indigo-600 hover:text-indigo-800 font-medium"
                                        >
                                            {chapter.chapterName}
                                        </Link>
                                    </td>
                                    <td className="px-6 py-4">{chapter.description}</td>
                                    <td className="px-6 py-4">
                                        <div className="flex justify-end gap-2">
                                            <button
                                                onClick={() => openModal(chapter)}
                                                className="flex items-center gap-1 bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-lg transition-all"
                                            >
                                                <Pencil size={16} />
                                                Edit
                                            </button>
                                            <button
                                                onClick={() => handleDeleteChapter(chapter.id)}
                                                className="flex items-center gap-1 bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg transition-all"
                                            >
                                                <Trash2 size={16} />
                                                Delete
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>

            {showModal && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white rounded-xl p-6 shadow-lg w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">
                            {editingChapter ? "Edit Chapter" : "Add Chapter"}
                        </h2>
                        <div className="mb-4">
                            <label className="block text-sm font-medium mb-2">Chapter Name</label>
                            <input
                                type="text"
                                value={chapterData.chapterName}
                                onChange={(e) =>
                                    setChapterData({ ...chapterData, chapterName: e.target.value })
                                }
                                className="w-full px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block text-sm font-medium mb-2">Description</label>
                            <textarea
                                value={chapterData.description}
                                onChange={(e) =>
                                    setChapterData({ ...chapterData, description: e.target.value })
                                }
                                className="w-full px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                                rows={3}
                            />
                        </div>
                        <div className="flex justify-end gap-4">
                            <button
                                onClick={() => setShowModal(false)}
                                className="flex items-center gap-1 bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-lg transition-all"
                            >
                                <X size={16} />
                                Cancel
                            </button>
                            <button
                                onClick={handleSaveChapter}
                                className="flex items-center gap-1 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg transition-all"
                            >
                                <Save size={16} />
                                Save
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Chapters;
