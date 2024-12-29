"use client";

import { useEffect, useState } from "react";
import { getCoursesBySubject, createCourse, updateCourse, deleteCourse } from "@/Services/courseService";
import { Course } from "@/types/course";
import Link from "next/link";
import { Plus, Pencil, Trash2, Save, X } from "lucide-react";

type CoursesProps = {
    subjectId: number;
};

const Courses = ({ subjectId }: CoursesProps) => {
    const [courses, setCourses] = useState<Course[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingCourse, setEditingCourse] = useState<Course | null>(null);
    const [courseData, setCourseData] = useState({ courseName: "", description: "" });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchCourses();
    }, [subjectId]);

    const fetchCourses = async () => {
        setLoading(true);
        try {
            const data = await getCoursesBySubject(subjectId);
            setCourses(data);
        } catch (error) {
            console.error("Error fetching courses:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleSaveCourse = async () => {
        if (!courseData.courseName.trim() || !courseData.description.trim()) return;

        try {
            if (editingCourse) {
                await updateCourse(editingCourse.id, {
                    courseName: courseData.courseName,
                    description: courseData.description,
                    subject: { id: subjectId },
                });
            } else {
                await createCourse({
                    courseName: courseData.courseName,
                    description: courseData.description,
                    subject: { id: subjectId },
                });
            }

            setCourseData({ courseName: "", description: "" });
            setEditingCourse(null);
            setShowModal(false);
            fetchCourses();
        } catch (error) {
            console.error("Error saving course:", error);
        }
    };

    const handleDeleteCourse = async (id: number) => {
        try {
            await deleteCourse(id);
            fetchCourses();
        } catch (error) {
            console.error("Error deleting course:", error);
        }
    };

    const openModal = (course?: Course) => {
        if (course) {
            setEditingCourse(course);
            setCourseData({ courseName: course.courseName, description: course.description });
        } else {
            setEditingCourse(null);
            setCourseData({ courseName: "", description: "" });
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
                    Add Course
                </button>
            </div>

            <div className="bg-white/80 backdrop-blur-lg rounded-xl shadow-lg border border-gray-100 overflow-hidden">
                {loading ? (
                    <p className="p-6 text-gray-500">Loading courses...</p>
                ) : courses.length === 0 ? (
                    <p className="p-6 text-gray-500">No courses available for this subject.</p>
                ) : (
                    <table className="w-full">
                        <thead className="bg-gray-50/50">
                            <tr>
                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-600">Course Name</th>
                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-600">Description</th>
                                <th className="px-6 py-4 text-right text-sm font-semibold text-gray-600">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {courses.map((course) => (
                                <tr key={course.id} className="hover:bg-gray-50/50 transition-colors">
                                    <td className="px-6 py-4">
                                        <Link
                                            href={`/dashboard/chapters/${course.id}`}
                                            className="text-indigo-600 hover:text-indigo-800 font-medium"
                                        >
                                            {course.courseName}
                                        </Link>
                                    </td>
                                    <td className="px-6 py-4">{course.description}</td>
                                    <td className="px-6 py-4">
                                        <div className="flex justify-end gap-2">
                                            <button
                                                onClick={() => openModal(course)}
                                                className="flex items-center gap-1 bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-lg transition-all"
                                            >
                                                <Pencil size={16} />
                                                Edit
                                            </button>
                                            <button
                                                onClick={() => handleDeleteCourse(course.id)}
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
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-xl font-bold">
                                {editingCourse ? "Edit Course" : "Add Course"}
                            </h2>
                            <button
                                onClick={() => setShowModal(false)}
                                className="flex items-center gap-1 text-red-500 hover:text-red-600"
                            >
                                <X size={20} />
                                Close
                            </button>
                        </div>
                        <div className="mb-4">
                            <label className="block text-sm font-medium mb-2">Course Name</label>
                            <input
                                type="text"
                                value={courseData.courseName}
                                onChange={(e) =>
                                    setCourseData({ ...courseData, courseName: e.target.value })
                                }
                                className="w-full px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block text-sm font-medium mb-2">Description</label>
                            <textarea
                                value={courseData.description}
                                onChange={(e) =>
                                    setCourseData({ ...courseData, description: e.target.value })
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
                                onClick={handleSaveCourse}
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

export default Courses;
