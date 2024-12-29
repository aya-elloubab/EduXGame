"use client";

import React, { useEffect, useState } from "react";
import { getStudentProfile } from "@/Services/studentService";
import { Student } from "@/types/student";
import { AxiosError } from "axios"; // Ensure AxiosError is imported

// Define the structure of the error response
interface ErrorResponse {
    message: string;
}
interface StudentProfileProps {
    studentId: number;
}

const StudentProfile: React.FC<StudentProfileProps> = ({ studentId }) => {
    const [student, setStudent] = useState<Student | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const data = await getStudentProfile(studentId);
                setStudent(data);
            } catch (err) {
                const axiosError = err as AxiosError<ErrorResponse>; // Explicitly type the AxiosError with ErrorResponse
                console.error("Error fetching student profile:", axiosError);
            
                const errorMessage = axiosError.response?.data?.message || `Failed to fetch student profile. ${axiosError.message}`;
                setError(errorMessage);
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, [studentId]);

    if (loading) {
        return <p className="text-center text-gray-500">Loading...</p>;
    }

    if (error) {
        return <p className="text-center text-red-500">{error}</p>;
    }

    return (
        <div className="space-y-4">
            <div className="flex justify-between">
                <span className="font-semibold text-gray-700 dark:text-gray-300">First Name:</span>
                <span className="text-gray-800 dark:text-gray-200">{student?.firstName}</span>
            </div>
            <div className="flex justify-between">
                <span className="font-semibold text-gray-700 dark:text-gray-300">Last Name:</span>
                <span className="text-gray-800 dark:text-gray-200">{student?.lastName}</span>
            </div>
            <div className="flex justify-between">
                <span className="font-semibold text-gray-700 dark:text-gray-300">Email:</span>
                <span className="text-gray-800 dark:text-gray-200">{student?.email}</span>
            </div>
            <div className="flex justify-between">
                <span className="font-semibold text-gray-700 dark:text-gray-300">Phone:</span>
                <span className="text-gray-800 dark:text-gray-200">{student?.phone}</span>
            </div>
            <div className="flex justify-between">
                <span className="font-semibold text-gray-700 dark:text-gray-300">School:</span>
                <span className="text-gray-800 dark:text-gray-200">{student?.school}</span>
            </div>
        </div>
    );
};

export default StudentProfile;
