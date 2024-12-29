import api from "@/utils/api";
import { Branch } from "@/types/branch";

export const getAllBranches = async (): Promise<Branch[]> => {
    const response = await api.get("/branches");
    return response.data;
};

export const getBranchById = async (id: number): Promise<Branch> => {
    const response = await api.get(`/branches/${id}`);
    return response.data;
};

export const getBranchesByLevel = async (levelId: number): Promise<Branch[]> => {
    const response = await api.get(`/branches/level/${levelId}`);
    return response.data;
};

export const createBranch = async (branch: { name: string; level: { id: number } }): Promise<Branch> => {
    const response = await api.post("/branches", branch);
    return response.data;
};


export const updateBranch = async (id: number, branch: { name: string; levelId: number }): Promise<Branch> => {
    const response = await api.put(`/branches/${id}`, branch);
    return response.data;
};

export const deleteBranch = async (id: number): Promise<void> => {
    await api.delete(`/branches/${id}`);
};
