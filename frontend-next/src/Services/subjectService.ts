import api from "@/utils/api";
import { Subject } from "@/types/subject";

// Fetch all subjects for a specific branch
export const getSubjectsByBranch = async (branchId: number): Promise<Subject[]> => {
    const response = await api.get(`/subjects/branch/${branchId}`);
    return response.data;
};

// Create a new subject
export const createSubject = async (subject: { name: string; branch: { id: number } }): Promise<Subject> => {
    const response = await api.post("/subjects", subject);
    return response.data;
};

// Update an existing subject
export const updateSubject = async (id: number, subject: { name: string; branch: { id: number } }): Promise<Subject> => {
    const response = await api.put(`/subjects/${id}`, subject);
    return response.data;
};

// Delete a subject
export const deleteSubject = async (id: number): Promise<void> => {
    await api.delete(`/subjects/${id}`);
};
