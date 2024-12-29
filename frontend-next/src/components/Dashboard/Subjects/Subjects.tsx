"use client";

import { useEffect, useState } from "react";
import { getSubjectsByBranch, createSubject, updateSubject, deleteSubject } from "@/Services/subjectService";
import { Subject } from "@/types/subject";
import Link from "next/link";
import { Plus, Pencil, Trash2, Save } from "lucide-react";

type SubjectsProps = {
    branchId: number;
};

const Subjects = ({ branchId }: SubjectsProps) => {
    const [subjects, setSubjects] = useState<Subject[]>([]);
    const [newSubjectName, setNewSubjectName] = useState("");
    const [editingSubject, setEditingSubject] = useState<Subject | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchSubjects();
    }, [branchId]);

    const fetchSubjects = async () => {
        setLoading(true);
        try {
            const data = await getSubjectsByBranch(branchId);
            setSubjects(data);
        } catch (error) {
            console.error("Error fetching subjects:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleCreateSubject = async () => {
        if (!newSubjectName.trim()) return;

        try {
            await createSubject({ name: newSubjectName, branch: { id: branchId } });
            setNewSubjectName("");
            fetchSubjects();
        } catch (error) {
            console.error("Error creating subject:", error);
        }
    };

    const handleUpdateSubject = async () => {
        if (!editingSubject || !editingSubject.name.trim()) return;

        try {
            await updateSubject(editingSubject.id, { name: editingSubject.name, branch: { id: branchId } });
            setEditingSubject(null);
            fetchSubjects();
        } catch (error) {
            console.error("Error updating subject:", error);
        }
    };

    const handleDeleteSubject = async (id: number) => {
        try {
            await deleteSubject(id);
            fetchSubjects();
        } catch (error) {
            console.error("Error deleting subject:", error);
        }
    };

    return (
        <div className="w-full max-w-4xl mx-auto">
            <div className="mb-8 bg-white/80 backdrop-blur-lg rounded-xl p-6 shadow-lg border border-gray-100">
                <div className="flex items-center gap-4">
                    <input
                        type="text"
                        placeholder="New Subject Name"
                        value={newSubjectName}
                        onChange={(e) => setNewSubjectName(e.target.value)}
                        className="flex-1 px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
                    />
                    <button
                        onClick={handleCreateSubject}
                        className="flex items-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-2 rounded-lg transition-all shadow-md hover:shadow-lg"
                    >
                        <Plus size={20} />
                        Add Subject
                    </button>
                </div>
            </div>

            <div className="bg-white/80 backdrop-blur-lg rounded-xl shadow-lg border border-gray-100 overflow-hidden">
                {loading ? (
                    <p className="p-6 text-gray-500">Loading subjects...</p>
                ) : subjects.length === 0 ? (
                    <p className="p-6 text-gray-500">No subjects available for this branch.</p>
                ) : (
                    <table className="w-full">
                        <thead className="bg-gray-50/50">
                            <tr>
                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-600">Subject Name</th>
                                <th className="px-6 py-4 text-right text-sm font-semibold text-gray-600">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {subjects.map((subject) => (
                                <tr key={subject.id} className="hover:bg-gray-50/50 transition-colors">
                                    <td className="px-6 py-4">
                                        {editingSubject?.id === subject.id ? (
                                            <input
                                                type="text"
                                                value={editingSubject.name}
                                                onChange={(e) =>
                                                    setEditingSubject({ ...editingSubject, name: e.target.value })
                                                }
                                                className="w-full px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                                            />
                                        ) : (
                                            <Link
                                                href={`/dashboard/courses/${subject.id}`}
                                                className="text-indigo-600 hover:text-indigo-800 font-medium"
                                            >
                                                {subject.name}
                                            </Link>
                                        )}
                                    </td>
                                    <td className="px-6 py-4">
                                        <div className="flex justify-end gap-2">
                                            {editingSubject?.id === subject.id ? (
                                                <button
                                                    onClick={handleUpdateSubject}
                                                    className="flex items-center gap-1 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg transition-all"
                                                >
                                                    <Save size={16} />
                                                    Save
                                                </button>
                                            ) : (
                                                <button
                                                    onClick={() => setEditingSubject(subject)}
                                                    className="flex items-center gap-1 bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-lg transition-all"
                                                >
                                                    <Pencil size={16} />
                                                    Edit
                                                </button>
                                            )}
                                            <button
                                                onClick={() => handleDeleteSubject(subject.id)}
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
        </div>
    );
};

export default Subjects;
