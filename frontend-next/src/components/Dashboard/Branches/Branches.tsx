"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { getBranchesByLevel, createBranch, updateBranch, deleteBranch } from "@/Services/branchService";
import { Branch } from "@/types/branch";
import Link from "next/link";
import { Plus, Pencil, Trash2, Save } from 'lucide-react';

const Branches = () => {
    const { idLevel } = useParams();
    const [branches, setBranches] = useState<Branch[]>([]);
    const [newBranchName, setNewBranchName] = useState("");
    const [editingBranch, setEditingBranch] = useState<Branch | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (idLevel) fetchBranches();
    }, [idLevel]);

    const fetchBranches = async () => {
        setLoading(true);
        try {
            const data = await getBranchesByLevel(Number(idLevel));
            setBranches(data);
        } catch (error) {
            console.error("Error fetching branches:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleCreateBranch = async () => {
        if (!newBranchName.trim()) return;
        try {
            await createBranch({ name: newBranchName, level: { id: Number(idLevel) } });
            setNewBranchName("");
            fetchBranches();
        } catch (error) {
            console.error("Error creating branch:", error);
        }
    };

    const handleUpdateBranch = async () => {
        if (!editingBranch || !editingBranch.name.trim()) return;
        try {
            await updateBranch(editingBranch.id, { name: editingBranch.name, levelId: Number(idLevel) });
            setEditingBranch(null);
            fetchBranches();
        } catch (error) {
            console.error("Error updating branch:", error);
        }
    };

    const handleDeleteBranch = async (id: number) => {
        try {
            await deleteBranch(id);
            fetchBranches();
        } catch (error) {
            console.error("Error deleting branch:", error);
        }
    };

    return (
        <div className="w-full max-w-6xl mx-auto p-6">
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-2xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                    Branches for Level {idLevel}
                </h1>
            </div>

            <div className="mb-8 bg-white/80 backdrop-blur-lg rounded-xl p-6 shadow-lg border border-gray-100">
                <div className="flex items-center gap-4">
                    <input
                        type="text"
                        placeholder="New Branch Name"
                        value={newBranchName}
                        onChange={(e) => setNewBranchName(e.target.value)}
                        className="flex-1 px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
                    />
                    <button
                        onClick={handleCreateBranch}
                        className="flex items-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-2 rounded-lg transition-all shadow-md hover:shadow-lg"
                    >
                        <Plus size={20} />
                        Add Branch
                    </button>
                </div>
            </div>

            {loading ? (
                <div className="flex justify-center items-center h-64">
                    <div className="animate-spin rounded-full h-12 w-12 border-4 border-indigo-500 border-t-transparent"></div>
                </div>
            ) : branches.length === 0 ? (
                <div className="text-center py-12 bg-white/80 backdrop-blur-lg rounded-xl shadow-lg border border-gray-100">
                    <p className="text-gray-500">No branches available for this level.</p>
                </div>
            ) : (
                <div className="bg-white/80 backdrop-blur-lg rounded-xl shadow-lg border border-gray-100 overflow-hidden">
                    <table className="w-full">
                        <thead className="bg-gray-50/50">
                            <tr>
                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-600">Branch Name</th>
                                <th className="px-6 py-4 text-right text-sm font-semibold text-gray-600">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {branches.map((branch) => (
                                <tr key={branch.id} className="hover:bg-gray-50/50 transition-colors">
                                    <td className="px-6 py-4">
                                        {editingBranch?.id === branch.id ? (
                                            <input
                                                type="text"
                                                value={editingBranch.name}
                                                onChange={(e) =>
                                                    setEditingBranch({ ...editingBranch, name: e.target.value })
                                                }
                                                className="w-full px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                                            />
                                        ) : (
                                            <Link
                                                href={`/dashboard/subjects/${branch.id}`}
                                                className="text-indigo-600 hover:text-indigo-800 font-medium"
                                            >
                                                {branch.name}
                                            </Link>
                                        )}
                                    </td>
                                    <td className="px-6 py-4">
                                        <div className="flex justify-end gap-2">
                                            {editingBranch?.id === branch.id ? (
                                                <button
                                                    onClick={handleUpdateBranch}
                                                    className="flex items-center gap-1 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg transition-all"
                                                >
                                                    <Save size={16} />
                                                    Save
                                                </button>
                                            ) : (
                                                <button
                                                    onClick={() => setEditingBranch(branch)}
                                                    className="flex items-center gap-1 bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-lg transition-all"
                                                >
                                                    <Pencil size={16} />
                                                    Edit
                                                </button>
                                            )}
                                            <button
                                                onClick={() => handleDeleteBranch(branch.id)}
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
                </div>
            )}
        </div>
    );
};

export default Branches;