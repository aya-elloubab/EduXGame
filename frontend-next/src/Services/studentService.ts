import api from '../utils/api';
import { Student } from '@/types/student';

// Fetch a student profile by ID
export const getStudentProfile = async (id: number): Promise<Student> => {
    const response = await api.get(`/students/${id}/profile`);
    return response.data;
};
