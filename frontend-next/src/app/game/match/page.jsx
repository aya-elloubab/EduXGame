"use client";

import React, { useState, useEffect } from 'react';

// Custom Button Component
const Button = ({ children, variant = 'default', className = '', ...props }) => (
  <button
    className={`
      px-4 py-2 rounded-lg font-medium transition-all duration-200
      ${variant === 'outline' ? 
        'border-2 border-gray-200 hover:bg-gray-50' : 
        'bg-blue-500 text-white hover:bg-blue-600'}
      ${className}
    `}
    {...props}
  >
    {children}
  </button>
);

// Custom Card Component
const Card = ({ children, className = '' }) => (
  <div className={`bg-white rounded-xl shadow-md ${className}`}>
    {children}
  </div>
);

// Custom Badge Component
const Badge = ({ children, variant = 'default' }) => (
  <div className={`
    inline-flex items-center px-3 py-1 rounded-full text-sm font-medium
    ${variant === 'outline' ? 
      'border-2 border-gray-200 bg-white' : 
      'bg-blue-100 text-blue-800'}
  `}>
    {children}
  </div>
);

// Icon Components
const Icons = {
  Timer: () => (
    <svg className="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <circle cx="12" cy="12" r="10"/>
      <path d="M12 6v6l4 2"/>
    </svg>
  ),
  Trophy: () => (
    <svg className="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <path d="M8 21h8m-4-4v4m-5.2-4h10.4c1.68 0 2.52 0 3.162-.327a3 3 0 0 0 1.311-1.311C22 14.72 22 13.88 22 12.2V7.8c0-1.68 0-2.52-.327-3.162a3 3 0 0 0-1.311-1.311C19.72 3 18.88 3 17.2 3H6.8c-1.68 0-2.52 0-3.162.327a3 3 0 0 0-1.311 1.311C2 5.28 2 6.12 2 7.8v4.4c0 1.68 0 2.52.327 3.162a3 3 0 0 0 1.311 1.311C4.28 17 5.12 17 6.8 17Z"/>
    </svg>
  ),
  Streak: () => (
    <svg className="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
    </svg>
  ),
  Refresh: () => (
    <svg className="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <path d="M3 12a9 9 0 0 1 9-9 9 9 0 0 1 6.36 2.64L21 9m0-5v5h-5m5 5a9 9 0 0 1-9 9 9 9 0 0 1-6.36-2.64L3 15m0 5v-5h5"/>
    </svg>
  )
};

const MatchingGame = () => {
  const [terms, setTerms] = useState([]);
  const [selectedTerm, setSelectedTerm] = useState(null);
  const [selectedDefinition, setSelectedDefinition] = useState(null);
  const [matches, setMatches] = useState([]);
  const [score, setScore] = useState(0);
  const [streak, setStreak] = useState(0);
  const [bestStreak, setBestStreak] = useState(0);
  const [timer, setTimer] = useState(0);
  const [isPlaying, setIsPlaying] = useState(false);
  const [lastMatchTime, setLastMatchTime] = useState(Date.now());
  const [bonusPoints, setBonusPoints] = useState(null);

  const initialData = [
    { element: "Sum", matchText: "The result of addition" },
    { element: "Terms of sum", matchText: "Numbers being added together" },
    { element: "Difference", matchText: "The result of subtraction" },
    { element: "Terms of difference", matchText: "Numbers being subtracted" },
    { element: "Product", matchText: "The result of multiplication" },
    { element: "Factors", matchText: "Numbers being multiplied together" },
    { element: "Quotient", matchText: "The result of division" },
    { element: "Dividend", matchText: "Number being divided" }
  ];

  useEffect(() => {
    let interval;
    if (isPlaying) {
      interval = setInterval(() => {
        setTimer(prev => prev + 1);
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [isPlaying]);

  useEffect(() => {
    shuffleTerms();
  }, []);

  const shuffleTerms = () => {
    const shuffledTerms = [...initialData].sort(() => Math.random() - 0.5);
    setTerms(shuffledTerms);
    setMatches([]);
    setScore(0);
    setStreak(0);
    setTimer(0);
    setSelectedTerm(null);
    setSelectedDefinition(null);
    setIsPlaying(true);
    setLastMatchTime(Date.now());
  };

  const calculateTimeBonus = () => {
    const timeSinceLastMatch = (Date.now() - lastMatchTime) / 1000;
    if (timeSinceLastMatch < 3) return 50;
    if (timeSinceLastMatch < 5) return 30;
    if (timeSinceLastMatch < 8) return 20;
    return 10;
  };

  const handleTermClick = (term) => {
    if (matches.find(match => match.element === term.element)) return;
    setSelectedTerm(term);
    if (selectedDefinition) {
      checkMatch(term, selectedDefinition);
    }
  };

  const handleDefinitionClick = (term) => {
    if (matches.find(match => match.element === term.element)) return;
    setSelectedDefinition(term);
    if (selectedTerm) {
      checkMatch(selectedTerm, term);
    }
  };

  const checkMatch = (term1, term2) => {
    if (term1.element === term2.element) {
      const timeBonus = calculateTimeBonus();
      const newStreak = streak + 1;
      setStreak(newStreak);
      setBestStreak(Math.max(bestStreak, newStreak));
      
      const streakBonus = Math.floor(newStreak / 3) * 25;
      const totalBonus = timeBonus + streakBonus;
      
      setMatches([...matches, term1]);
      setScore(score + 100 + totalBonus);
      setBonusPoints({
        amount: totalBonus,
        position: { x: Math.random() * 200, y: Math.random() * 200 }
      });
      
      setTimeout(() => setBonusPoints(null), 1000);
      setLastMatchTime(Date.now());
    } else {
      setStreak(0);
    }
    setSelectedTerm(null);
    setSelectedDefinition(null);
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  const isGameComplete = matches.length === initialData.length;

  if (isGameComplete && isPlaying) {
    setIsPlaying(false);
  }

  return (
    <div className="flex flex-col items-center w-full max-w-6xl p-4 gap-6 bg-gray-50 min-h-screen">
      {/* Game Stats Header */}
      <Card className="w-full p-4">
        <div className="flex justify-between items-center flex-wrap gap-4">
          <div className="flex items-center gap-4">
            <Badge variant="outline">
              <span className="flex items-center gap-2">
                <Icons.Trophy />
                {score}
              </span>
            </Badge>
            <Badge variant="outline">
              <span className="flex items-center gap-2">
                <Icons.Streak />
                {streak}×
              </span>
            </Badge>
          </div>
          
          <Badge variant="outline">
            <span className="flex items-center gap-2">
              <Icons.Timer />
              {formatTime(timer)}
            </span>
          </Badge>
          
          <Button 
            onClick={shuffleTerms}
            variant="outline"
            className="flex items-center gap-2"
          >
            <Icons.Refresh /> New Game
          </Button>
        </div>
      </Card>

      {/* Main Game Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 w-full">
        {/* Terms Column */}
        <Card className="p-6">
          <h3 className="text-xl font-bold mb-4">Terms</h3>
          <div className="space-y-3">
            {terms.map((term) => {
              const isMatched = matches.find(match => match.element === term.element);
              const isSelected = selectedTerm?.element === term.element;

              return (
                <Button
                  key={term.element}
                  onClick={() => handleTermClick(term)}
                  className={`w-full justify-start text-base truncate ${
                    isMatched ? 'bg-green-100 text-green-800 hover:bg-green-200' : 
                    isSelected ? 'bg-blue-100 text-blue-800 hover:bg-blue-200' : 
                    'hover:bg-gray-100'
                  }`}
                  variant="outline"
                  disabled={isMatched}
                >
                  {term.element}
                </Button>
              );
            })}
          </div>
        </Card>

        {/* Definitions Column */}
        <Card className="p-6">
          <h3 className="text-xl font-bold mb-4">Definitions</h3>
          <div className="space-y-3">
            {terms.map((term) => {
              const isMatched = matches.find(match => match.element === term.element);
              const isSelected = selectedDefinition?.element === term.element;

              return (
                <Button
                  key={term.element}
                  onClick={() => handleDefinitionClick(term)}
                  className={`w-full justify-start text-base truncate ${
                    isMatched ? 'bg-green-100 text-green-800 hover:bg-green-200' : 
                    isSelected ? 'bg-blue-100 text-blue-800 hover:bg-blue-200' : 
                    'hover:bg-gray-100'
                  }`}
                  variant="outline"
                  disabled={isMatched}
                >
                  {term.matchText}
                </Button>
              );
            })}
          </div>
        </Card>
      </div>

      {/* Game Complete Message */}
      {isGameComplete && (
        <Card className="w-full p-6 bg-green-50">
          <div className="text-center">
            <h3 className="text-2xl font-bold text-green-800 mb-2">Congratulations!</h3>
            <p className="text-lg text-green-700 mb-4">
              Completed in {formatTime(timer)}!
            </p>
            <div className="flex justify-center gap-4 flex-wrap">
              <Badge variant="outline">
                <span className="flex items-center gap-2">
                  <Icons.Trophy />
                  Final Score: {score}
                </span>
              </Badge>
              <Badge variant="outline">
                <span className="flex items-center gap-2">
                  <Icons.Streak />
                  Best Streak: {bestStreak}
                </span>
              </Badge>
            </div>
          </div>
        </Card>
      )}

      {/* Floating Score Animations */}
      {bonusPoints && (
        <div 
          className="fixed text-2xl font-bold text-yellow-500 pointer-events-none animate-bonus"
          style={{
            left: `${bonusPoints.position.x}px`,
            top: `${bonusPoints.position.y}px`,
          }}
        >
          +{bonusPoints.amount}
        </div>
      )}

      <style jsx>{`
        @keyframes bonus {
          0% { transform: translateY(0); opacity: 1; }
          100% { transform: translateY(-50px); opacity: 0; }
        }
        .animate-bonus {
          animation: bonus 1s ease-out forwards;
        }
      `}</style>
    </div>
  );
};

export default MatchingGame;