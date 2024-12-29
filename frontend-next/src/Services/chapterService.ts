import api from "@/utils/api";
import { Chapter } from "@/types/chapter";

// Fetch all chapters for a specific course
export const getChaptersByCourse = async (courseId: number): Promise<Chapter[]> => {
    const response = await api.get(`/chapters/course/${courseId}`);
    return response.data;
};

// Create a new chapter
export const createChapter = async (chapter: { chapterName: string; description: string; course: { id: number } }): Promise<Chapter> => {
    const response = await api.post("/chapters", chapter);
    return response.data;
};

// Update an existing chapter
export const updateChapter = async (id: number, chapter: { chapterName: string; description: string; course: { id: number } }): Promise<Chapter> => {
    const response = await api.put(`/chapters/${id}`, chapter);
    return response.data;
};

// Delete a chapter
export const deleteChapter = async (id: number): Promise<void> => {
    await api.delete(`/chapters/${id}`);
};

// Fetch chapter data by ID
export const getChapterById = async (id: number): Promise<Chapter> => {
    const response = await api.get(`/chapters/${id}`);
    return response.data;
};

// Create or update chapter data
export const saveChapter = async (chapter: Chapter): Promise<Chapter> => {
    const response = chapter.id
        ? await api.put(`/chapters/${chapter.id}`, chapter)
        : await api.post(`/chapters`, chapter);
    return response.data;
};

// Parse PDF and return chapter data
export const parsePDF = async (file: File): Promise<Chapter> => {
    const formData = new FormData();
    formData.append("pdf", file);

    const response = await api.post("/process-pdf", formData, {
        headers: { "Content-Type": "multipart/form-data" },
    });
    return response.data.answer;
};
