import api from "@/utils/api";
import { Course } from "@/types/course";

// Fetch all courses for a specific subject
export const getCoursesBySubject = async (subjectId: number): Promise<Course[]> => {
    const response = await api.get(`/courses/subject/${subjectId}`);
    return response.data;
};
export const getCourseById = async (id: number): Promise<Course> => {
    const response = await api.get(`/courses/${id}`);
    return response.data;
};
// Create a new course
export const createCourse = async (course: { courseName: string; description: string; subject: { id: number } }): Promise<Course> => {
    const response = await api.post("/courses", course);
    return response.data;
};

// Update an existing course
export const updateCourse = async (id: number, course: { courseName: string; description: string; subject: { id: number } }): Promise<Course> => {
    const response = await api.put(`/courses/${id}`, course);
    return response.data;
};

// Delete a course
export const deleteCourse = async (id: number): Promise<void> => {
    await api.delete(`/courses/${id}`);
};
