"use client";

import Link from "next/link";
import Navbar from "@/components/Dashboard/Navbar/Navbar";
import { Book, ListChecks, FileText, Users, Library } from "lucide-react";

const DashboardPage = () => {
    const cards = [
        {
            title: "Levels",
            description: "Manage all levels in the system.",
            icon: Book,
            href: "/dashboard/levels",
            bgColor: "bg-blue-50",
            iconColor: "text-blue-500",
        },
        {
            title: "Branches",
            description: "Organize branches under each level.",
            icon: ListChecks,
            href: "/dashboard/branches",
            bgColor: "bg-green-50",
            iconColor: "text-green-500",
        },
        {
            title: "Subjects",
            description: "Handle subjects for branches.",
            icon: Library,
            href: "/dashboard/subjects",
            bgColor: "bg-purple-50",
            iconColor: "text-purple-500",
        },
        {
            title: "Chapters",
            description: "Oversee chapters within subjects.",
            icon: FileText,
            href: "/dashboard/chapters",
            bgColor: "bg-orange-50",
            iconColor: "text-orange-500",
        },
        {
            title: "Students",
            description: "View and manage student information.",
            icon: Users,
            href: "/dashboard/students",
            bgColor: "bg-pink-50",
            iconColor: "text-pink-500",
        },
    ];

    return (
        <div className="flex flex-col min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800">
            <Navbar />
            <main className="flex-grow container mx-auto px-4 sm:px-6 lg:px-8">
                <div className="mt-24 max-w-7xl mx-auto">
                    <h1 className="text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 dark:from-indigo-400 dark:to-purple-400 bg-clip-text text-transparent mb-8">
                        Dashboard
                    </h1>
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                        {cards.map((card, index) => (
                            <Link href={card.href} key={index}>
                                <div
                                    className={`p-6 rounded-lg shadow-lg hover:shadow-xl transition-all ${card.bgColor}`}
                                >
                                    <div className="flex items-center justify-between">
                                        <h2 className="text-xl font-bold text-gray-800 dark:text-gray-100">
                                            {card.title}
                                        </h2>
                                        <card.icon
                                            className={`h-8 w-8 ${card.iconColor}`}
                                        />
                                    </div>
                                    <p className="text-gray-600 dark:text-gray-300 mt-2">
                                        {card.description}
                                    </p>
                                </div>
                            </Link>
                        ))}
                    </div>
                </div>
            </main>
            <footer className="mt-auto py-6 text-center text-sm text-gray-500 dark:text-gray-400">
                © 2024 Your Company. All rights reserved.
            </footer>
        </div>
    );
};

export default DashboardPage;
