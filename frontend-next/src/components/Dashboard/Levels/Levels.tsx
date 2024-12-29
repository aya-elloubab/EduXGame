"use client"
import { useEffect, useState } from 'react';
import { getAllLevels, createLevel, updateLevel, deleteLevel } from '@/Services/levelService';
import { Level } from '@/types/level';
import Link from 'next/link';
import { Plus, Pencil, Trash2, Save } from 'lucide-react';

const Levels = () => {
    const [levels, setLevels] = useState<Level[]>([]);
    const [newLevelName, setNewLevelName] = useState('');
    const [editingLevel, setEditingLevel] = useState<Level | null>(null);

    useEffect(() => {
        fetchLevels();
    }, []);

    const fetchLevels = async () => {
        try {
            const data = await getAllLevels();
            setLevels(data);
        } catch (error) {
            console.error('Error fetching levels:', error);
        }
    };

    const handleCreateLevel = async () => {
        if (!newLevelName.trim()) return;
        try {
            await createLevel({ name: newLevelName });
            setNewLevelName('');
            fetchLevels();
        } catch (error) {
            console.error('Error creating level:', error);
        }
    };

    const handleUpdateLevel = async () => {
        if (!editingLevel || !editingLevel.name.trim()) return;
        try {
            await updateLevel(editingLevel.id, { name: editingLevel.name });
            setEditingLevel(null);
            fetchLevels();
        } catch (error) {
            console.error('Error updating level:', error);
        }
    };

    const handleDeleteLevel = async (id: number) => {
        try {
            await deleteLevel(id);
            fetchLevels();
        } catch (error) {
            console.error('Error deleting level:', error);
        }
    };

    return (
        <div className="w-full max-w-4xl mx-auto">
            <div className="mb-8 bg-white/80 backdrop-blur-lg rounded-xl p-6 shadow-lg border border-gray-100">
                <div className="flex items-center gap-4">
                    <input
                        type="text"
                        placeholder="New Level Name"
                        value={newLevelName}
                        onChange={(e) => setNewLevelName(e.target.value)}
                        className="flex-1 px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
                    />
                    <button
                        onClick={handleCreateLevel}
                        className="flex items-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-500 hover:from-indigo-600 hover:to-purple-600 text-white px-6 py-2 rounded-lg transition-all shadow-md hover:shadow-lg"
                    >
                        <Plus size={20} />
                        Add Level
                    </button>
                </div>
            </div>

            <div className="bg-white/80 backdrop-blur-lg rounded-xl shadow-lg border border-gray-100 overflow-hidden">
                <table className="w-full">
                    <thead className="bg-gray-50/50">
                        <tr>
                            <th className="px-6 py-4 text-left text-sm font-semibold text-gray-600">Name</th>
                            <th className="px-6 py-4 text-right text-sm font-semibold text-gray-600">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-100">
                        {levels.map((level) => (
                            <tr key={level.id} className="hover:bg-gray-50/50 transition-colors">
                                <td className="px-6 py-4">
                                    {editingLevel?.id === level.id ? (
                                        <input
                                            type="text"
                                            value={editingLevel.name}
                                            onChange={(e) =>
                                                setEditingLevel({ ...editingLevel, name: e.target.value })
                                            }
                                            className="w-full px-4 py-2 rounded-lg border border-gray-200 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
                                        />
                                    ) : (
                                        <Link
                                            href={`/dashboard/branches/${level.id}`}
                                            className="text-indigo-600 hover:text-indigo-800 font-medium"
                                        >
                                            {level.name}
                                        </Link>
                                    )}
                                </td>
                                <td className="px-6 py-4">
                                    <div className="flex justify-end gap-2">
                                        {editingLevel?.id === level.id ? (
                                            <button
                                                onClick={handleUpdateLevel}
                                                className="flex items-center gap-1 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg transition-all"
                                            >
                                                <Save size={16} />
                                                Save
                                            </button>
                                        ) : (
                                            <button
                                                onClick={() => setEditingLevel(level)}
                                                className="flex items-center gap-1 bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-lg transition-all"
                                            >
                                                <Pencil size={16} />
                                                Edit
                                            </button>
                                        )}
                                        <button
                                            onClick={() => handleDeleteLevel(level.id)}
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
        </div>
    );
};

export default Levels;