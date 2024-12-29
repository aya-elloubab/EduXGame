import api from '../utils/api';
import { Level } from '../types/level';

export const getAllLevels = async (): Promise<Level[]> => {
    const response = await api.get('/levels');
    return response.data;
};

export const createLevel = async (level: { name: string }): Promise<Level> => {
    const response = await api.post('/levels', level);
    return response.data;
};

export const updateLevel = async (id: number, level: { name: string }): Promise<Level> => {
    const response = await api.put(`/levels/${id}`, level);
    return response.data;
};

export const deleteLevel = async (id: number): Promise<void> => {
    await api.delete(`/levels/${id}`);
};
