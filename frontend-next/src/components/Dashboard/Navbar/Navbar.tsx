"use client";

import React from "react";
import Link from "next/link";
import { Menu, Book, Users, ListChecks, FileText, LogOut, User } from "lucide-react";

const Navbar = () => {
  return (
    <header className="w-full bg-white/80 backdrop-blur-lg shadow-lg fixed top-0 left-0 z-50 border-b border-gray-200">
      <div className="container mx-auto flex justify-between items-center px-6 py-4">
        {/* Logo */}
        <div className="flex items-center gap-3">
          <Menu size={24} className="text-indigo-500" />
          <h1 className="text-2xl font-bold text-gray-800">Admin Dashboard</h1>
        </div>

        {/* Navigation Links */}
        <nav className="hidden md:flex gap-6">
          <Link href="/dashboard" className="flex items-center text-gray-600 hover:text-indigo-500 transition gap-1">
            <Book size={20} />
            Dashboard
          </Link>
          <Link
            href="/dashboard/levels"
            className="flex items-center text-gray-600 hover:text-indigo-500 transition gap-1"
          >
            <Book size={20} />
            Levels
          </Link>
          <Link
            href="/dashboard/branches"
            className="flex items-center text-gray-600 hover:text-indigo-500 transition gap-1"
          >
            <ListChecks size={20} />
            Branches
          </Link>
          <Link
            href="/dashboard/chapters"
            className="flex items-center text-gray-600 hover:text-indigo-500 transition gap-1"
          >
            <FileText size={20} />
            Chapters
          </Link>
          <Link
            href="/dashboard/students"
            className="flex items-center text-gray-600 hover:text-indigo-500 transition gap-1"
          >
            <Users size={20} />
            Students
          </Link>
        </nav>

        {/* Profile Section */}
        <div className="relative group">
          <button className="flex items-center gap-2 text-gray-800 hover:text-indigo-500 transition">
            <User size={20} />
            Admin
          </button>
          {/* Dropdown */}
          <div className="absolute right-0 mt-2 hidden group-hover:block bg-white shadow-lg rounded-lg w-40 border border-gray-100">
            <Link
              href="/profile"
              className="block px-4 py-2 text-gray-700 hover:bg-indigo-50 hover:text-indigo-500 transition"
            >
              Profile
            </Link>
            <button
              className="block w-full text-left px-4 py-2 text-red-500 hover:bg-gray-100 hover:text-red-600 transition"
            >
              <LogOut size={16} className="inline-block mr-1" />
              Logout
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Navbar;
