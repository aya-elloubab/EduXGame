"use client"
import React, { useState, useEffect } from 'react';
import Link from "next/link";
import { Trophy, Target, Book, Brain, Activity, ArrowRight, Mail, Github, Linkedin } from 'lucide-react';

// Particle Background Component
const ParticleBackground: React.FC = () => {
  const [particles, setParticles] = useState<Array<{ x: number; y: number; size: number; speed: number }>>([]);
  
  useEffect(() => {
    const createParticles = () => {
      const newParticles = Array.from({ length: 50 }, () => ({
        x: Math.random() * window.innerWidth,
        y: Math.random() * window.innerHeight,
        size: Math.random() * 4 + 1,
        speed: Math.random() * 1 + 0.5,
      }));
      setParticles(newParticles);
    };

    createParticles();
    window.addEventListener('resize', createParticles);
    
    const animateParticles = () => {
      setParticles(prev => prev.map(particle => ({
        ...particle,
        y: particle.y + particle.speed,
        x: particle.x + Math.sin(particle.y * 0.02) * 0.5,
        ...(particle.y > window.innerHeight ? { y: -10 } : {})
      })));
    };

    const interval = setInterval(animateParticles, 50);
    return () => {
      clearInterval(interval);
      window.removeEventListener('resize', createParticles);
    };
  }, []);

  return (
    <div className="fixed inset-0 pointer-events-none">
      {particles.map((particle, index) => (
        <div
          key={index}
          className="absolute rounded-full bg-blue-500/20"
          style={{
            left: `${particle.x}px`,
            top: `${particle.y}px`,
            width: `${particle.size}px`,
            height: `${particle.size}px`,
          }}
        />
      ))}
    </div>
  );
};


// Responsive Header
const Header: React.FC = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <header className="sticky top-0 z-40 backdrop-blur-md bg-white/80 border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center gap-4">
            
            <h1 className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-green-500 bg-clip-text text-transparent">
              EduXGame
            </h1>
          </div>

          {/* Mobile menu button */}
          <button 
            onClick={() => setIsMenuOpen(!isMenuOpen)}
            className="md:hidden p-2"
          >
            <div className="w-6 h-0.5 bg-gray-600 mb-1" />
            <div className="w-6 h-0.5 bg-gray-600 mb-1" />
            <div className="w-6 h-0.5 bg-gray-600" />
          </button>

          {/* Desktop navigation */}
          <nav className="hidden md:flex gap-8 items-center text-sm">
            <Link href="#features" className="hover:text-blue-600 transition-colors">Quests</Link>
            <Link href="#leaderboard" className="hover:text-blue-600 transition-colors">Leaderboard</Link>
            <Link href="#rewards" className="hover:text-blue-600 transition-colors">Rewards</Link>
            <Link href="/login/admin" className="bg-gradient-to-r from-blue-600 to-green-500 text-white px-6 py-3 rounded-full hover:opacity-90 transition-opacity shadow-lg">
              Start Adventure
            </Link>
          </nav>
        </div>

        {/* Mobile navigation */}
        <div className={`md:hidden transition-all duration-300 ${isMenuOpen ? 'max-h-64' : 'max-h-0'} overflow-hidden`}>
          <div className="py-4 space-y-4">
            <Link href="#features" className="block hover:text-blue-600 transition-colors">Quests</Link>
            <Link href="#leaderboard" className="block hover:text-blue-600 transition-colors">Leaderboard</Link>
            <Link href="#rewards" className="block hover:text-blue-600 transition-colors">Rewards</Link>
            <Link href="/login" className="block bg-gradient-to-r from-blue-600 to-green-500 text-white px-6 py-3 rounded-full hover:opacity-90 transition-opacity text-center">
              Start Adventure
            </Link>
          </div>
        </div>
      </div>
    </header>
  );
};

// Enhanced Footer with Animation
const Footer: React.FC = () => {
  return (
    <footer className="bg-gray-900 text-white py-16">
      <div className="max-w-7xl mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-12">
          <div className="space-y-4">
            <h4 className="text-xl font-bold">EduGame</h4>
            <p className="text-gray-400">Transform your learning journey into an epic adventure.</p>
            <div className="flex gap-4">
              <Link href="#" className="text-gray-400 hover:text-white transition-colors">
                <Mail className="w-5 h-5" />
              </Link>
              <Link href="#" className="text-gray-400 hover:text-white transition-colors">
                <Github className="w-5 h-5" />
              </Link>
              <Link href="#" className="text-gray-400 hover:text-white transition-colors">
                <Linkedin className="w-5 h-5" />
              </Link>
            </div>
          </div>
          
          <div>
            <h5 className="font-bold mb-4">Quick Links</h5>
            <ul className="space-y-2">
              <li><Link href="#" className="text-gray-400 hover:text-white transition-colors">Home</Link></li>
              <li><Link href="#" className="text-gray-400 hover:text-white transition-colors">About</Link></li>
              <li><Link href="#" className="text-gray-400 hover:text-white transition-colors">Features</Link></li>
              <li><Link href="#" className="text-gray-400 hover:text-white transition-colors">Pricing</Link></li>
            </ul>
          </div>
          
          <div>
            <h5 className="font-bold mb-4">Resources</h5>
            <ul className="space-y-2">
              <li><Link href="#" className="text-gray-400 hover:text-white transition-colors">Documentation</Link></li>
              <li><Link href="#" className="text-gray-400 hover:text-white transition-colors">Tutorials</Link></li>
              <li><Link href="#" className="text-gray-400 hover:text-white transition-colors">Blog</Link></li>
              <li><Link href="#" className="text-gray-400 hover:text-white transition-colors">Support</Link></li>
            </ul>
          </div>
          
          <div>
            <h5 className="font-bold mb-4">Newsletter</h5>
            <p className="text-gray-400 mb-4">Stay updated with our latest features and releases.</p>
            <div className="flex gap-2">
              <input 
                type="email" 
                placeholder="Enter your email" 
                className="bg-gray-800 text-white px-4 py-2 rounded-lg flex-1 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button className="bg-blue-500 hover:bg-blue-600 px-4 py-2 rounded-lg transition-colors">
                Subscribe
              </button>
            </div>
          </div>
        </div>
        
        <div className="mt-12 pt-8 border-t border-gray-800 text-center text-gray-400">
          <p>&copy; {new Date().getFullYear()} EduGame. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};
/*
// Experience Points Banner Component
const XPBanner: React.FC = () => {
  const [xp, setXP] = useState(0);
  
  useEffect(() => {
    const interval = setInterval(() => {
      setXP(prev => (prev + 1) % 100);
    }, 50);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="fixed top-4 right-4 bg-yellow-400 text-black px-4 py-2 rounded-full font-bold flex items-center gap-2">
      <Star className="w-4 h-4" />
      <span>{xp} XP to Next Level</span>
    </div>
  );
};
*/

/*
// Animated Header Component
const Header: React.FC = () => {
  return (
    <header className="sticky top-0 z-50 backdrop-blur-md bg-white/80 border-b border-gray-200">
      <div className="max-w-7xl mx-auto flex justify-between items-center p-4">
        <div className="flex items-center gap-4">
          <div className="relative">
            <Image src="/logo.svg" alt="EduGame Logo" width={48} height={48} className="animate-bounce" />
            <div className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
              3
            </div>
          </div>
          <h1 className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-green-500 bg-clip-text text-transparent">
            EduGame
          </h1>
        </div>
        <nav className="flex gap-8 items-center text-sm">
          <Link href="#features" className="hover:text-blue-600 transition-colors">Quests</Link>
          <Link href="#leaderboard" className="hover:text-blue-600 transition-colors">Leaderboard</Link>
          <Link href="#rewards" className="hover:text-blue-600 transition-colors">Rewards</Link>
          <Link href="/login" className="bg-gradient-to-r from-blue-600 to-green-500 text-white px-6 py-3 rounded-full hover:opacity-90 transition-opacity shadow-lg">
            Start Adventure
          </Link>
        </nav>
      </div>
    </header>
  );
};
*/
// Enhanced Hero Section
const Hero: React.FC = () => {
  return (
    <section className="relative min-h-screen flex items-center">
      <div className="absolute inset-0 bg-gradient-to-br from-blue-600 to-green-500 opacity-10 -z-10" />
      <div className="max-w-7xl mx-auto px-4 py-20 grid md:grid-cols-2 gap-12 items-center">
        <div className="space-y-8">
          <div className="space-y-4">
            <div className="flex items-center gap-2 text-sm">
              <span className="bg-green-100 text-green-700 px-3 py-1 rounded-full">Level 1</span>
              <span className="bg-blue-100 text-blue-700 px-3 py-1 rounded-full">+500 Daily XP</span>
            </div>
            <h2 className="text-5xl font-bold">Transform Learning into an Epic Adventure</h2>
            <p className="text-xl text-gray-600">Complete quests, earn rewards, and level up your knowledge in a gamified learning experience.</p>
          </div>
          <div className="flex gap-4">
            <Link href="/login/admin" className="group bg-gradient-to-r from-blue-600 to-green-500 text-white px-8 py-4 rounded-full hover:opacity-90 transition-opacity shadow-lg flex items-center gap-2">
              Manage Your Quest
              <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
            </Link>
            <Link href="/demo" className="px-8 py-4 rounded-full border-2 border-gray-300 hover:border-gray-400 transition-colors flex items-center gap-2">
              Watch Demo
            </Link>
          </div>
          <div className="flex items-center gap-8 text-sm text-gray-600">
            <div className="flex items-center gap-2">
              <Trophy className="w-5 h-5 text-yellow-500" />
              <span>10k+ Active Learners</span>
            </div>
            <div className="flex items-center gap-2">
              <Target className="w-5 h-5 text-green-500" />
              <span>500+ Learning Quests</span>
            </div>
          </div>
        </div>
        <div className="relative">
          <div className="absolute inset-0 bg-gradient-to-br from-blue-600 to-green-500 opacity-10 rounded-2xl" />
          <div className="bg-white p-8 rounded-2xl shadow-xl">
            <div className="space-y-6">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                    <Brain className="w-6 h-6 text-blue-600" />
                  </div>
                  <div>
                    <h3 className="font-bold">Current Quest</h3>
                    <p className="text-sm text-gray-600">Mathematics Level 3</p>
                  </div>
                </div>
                <span className="text-sm bg-green-100 text-green-700 px-3 py-1 rounded-full">
                  75% Complete
                </span>
              </div>
              <div className="h-2 bg-gray-100 rounded-full overflow-hidden">
                <div className="h-full w-3/4 bg-gradient-to-r from-blue-600 to-green-500" />
              </div>
              <div className="grid grid-cols-3 gap-4">
                {[
                  { icon: Book, label: "5 Chapters" },
                  { icon: Activity, label: "10 Quizzes" },
                  { icon: Trophy, label: "3 Achievements" }
                ].map((item, index) => (
                  <div key={index} className="text-center">
                    <item.icon className="w-6 h-6 mx-auto mb-2 text-gray-600" />
                    <span className="text-sm text-gray-600">{item.label}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

// Features Section with Animation
const Features: React.FC = () => {
  const features = [
    {
      icon: Brain,
      title: "Adaptive Learning Paths",
      description: "Personalized quests that adapt to your learning style and pace.",
      color: "blue"
    },
    {
      icon: Trophy,
      title: "Achievement System",
      description: "Earn badges and unlock special rewards as you progress.",
      color: "yellow"
    },
    {
      icon: Activity,
      title: "Real-time Progress",
      description: "Track your journey with detailed analytics and insights.",
      color: "green"
    }
  ];

  return (
    <section id="features" className="py-20 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4">
        <div className="text-center mb-16">
          <h3 className="text-3xl font-bold mb-4">Level Up Your Learning</h3>
          <p className="text-gray-600 max-w-2xl mx-auto">
            Discover powerful features designed to make your educational journey exciting and effective.
          </p>
        </div>
        <div className="grid md:grid-cols-3 gap-8">
          {features.map((feature, index) => (
            <div key={index} className="group bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow">
              <div className={`w-16 h-16 mb-6 rounded-2xl bg-${feature.color}-100 flex items-center justify-center group-hover:scale-110 transition-transform`}>
                <feature.icon className={`w-8 h-8 text-${feature.color}-600`} />
              </div>
              <h4 className="text-xl font-bold mb-4">{feature.title}</h4>
              <p className="text-gray-600">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

// Main Export
export default function Home() {
  return (
    <div className="font-sans relative overflow-x-hidden">
      <ParticleBackground />
      <Header />
      <Hero />
      <Features />
      <Footer />
    </div>
  );
}