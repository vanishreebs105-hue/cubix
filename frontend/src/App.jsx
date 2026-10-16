import { useState } from 'react';
import Cube3D from './components/Cube3D';
import { cubixAPI } from './api';

export default function App() {
  const [scramble, setScramble] = useState("R U R' U' F R2 B' L D2 F'");
  const [solution, setSolution] = useState(null);
  const [loading, setLoading] = useState(false);
  const [solving, setSolving] = useState(false);
  const [error, setError] = useState('');

  const handleGenerateScramble = async () => {
    setLoading(true);
    setError('');
    try {
      const { data } = await cubixAPI.scramble(10);
      setScramble(data.scramble);
      setSolution(null);
    } catch (err) {
      setError('Failed to generate scramble. Backend might be waking up...');
    } finally {
      setLoading(false);
    }
  };

  const handleSolve = async () => {
    if (!scramble.trim()) return;
    setSolving(true);
    setError('');
    try {
      const { data } = await cubixAPI.solve(scramble);
      setSolution(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to solve. Try again.');
    } finally {
      setSolving(false);
    }
  };

  return (
    <div className="min-h-screen">
      {/* ============ HEADER ============ */}
      <header className="border-b border-white/60 bg-white/60 backdrop-blur-xl sticky top-0 z-20">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3 group cursor-pointer">
            <div className="w-9 h-9 rounded-xl flex items-center justify-center shadow-lg transition-transform group-hover:scale-110 group-hover:rotate-6"
                 style={{ background: 'linear-gradient(135deg, #FB7185 0%, #A78BFA 100%)' }}>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <rect x="3" y="3" width="18" height="18" rx="2" stroke="white" strokeWidth="2" />
                <line x1="9" y1="3" x2="9" y2="21" stroke="white" strokeWidth="2" />
                <line x1="15" y1="3" x2="15" y2="21" stroke="white" strokeWidth="2" />
                <line x1="3" y1="9" x2="21" y2="9" stroke="white" strokeWidth="2" />
                <line x1="3" y1="15" x2="21" y2="15" stroke="white" strokeWidth="2" />
              </svg>
            </div>
            <span className="text-lg font-semibold tracking-tight">Cubix</span>
          </div>

          <a
            href="https://github.com/vanishreebs105-hue/cubix"
            target="_blank"
            rel="noreferrer"
            className="text-sm text-stone-500 hover:text-rose-500 transition-colors font-medium"
          >
            GitHub ↗
          </a>
        </div>
      </header>

      {/* ============ HERO ============ */}
      <section className="max-w-7xl mx-auto px-6 pt-14 pb-10 text-center animate-fade-in">
        <div className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full bg-white/70 backdrop-blur border border-stone-200/60 mb-6 text-xs font-medium text-stone-600">
          <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse" />
          Live · Powered by Spring Boot + Thistlethwaite
        </div>

        <h1 className="text-5xl md:text-7xl font-semibold tracking-tight leading-[1.05] mb-5">
          <span className="gradient-text">Solve any Rubik's Cube.</span>
          <br />
          <span className="text-stone-400 font-light">Mathematically.</span>
        </h1>

        <p className="text-stone-500 max-w-xl mx-auto text-lg leading-relaxed">
          A Java-powered solver that uses{' '}
          <span className="font-semibold text-rose-500">BFS</span> over a{' '}
          <span className="font-semibold text-purple-500">2048-state</span> coordinate space to
          orient all edges in optimal moves.
        </p>
      </section>

      {/* ============ MAIN GRID ============ */}
      <section className="max-w-7xl mx-auto px-6 pb-20 grid grid-cols-1 lg:grid-cols-5 gap-6">
        {/* 3D CUBE PANEL */}
        <div className="lg:col-span-3 card card-sky overflow-hidden">
          <div className="h-[520px] bg-gradient-to-br from-sky-50 via-white to-rose-50">
            <Cube3D />
          </div>
          <div className="px-6 py-4 border-t border-stone-100 flex items-center justify-between text-xs text-stone-500">
            <span>Drag to rotate · Scroll to zoom</span>
            <span className="flex items-center gap-1.5">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse" />
              Live 3D
            </span>
          </div>
        </div>

        {/* CONTROLS PANEL */}
        <div className="lg:col-span-2 space-y-4">
          {/* SCRAMBLE CARD — Rose */}
          <div className="card card-rose p-6">
            <label className="label">Scramble Sequence</label>
            <textarea
              value={scramble}
              onChange={(e) => setScramble(e.target.value)}
              className="input font-mono text-sm resize-none"
              rows={3}
              placeholder="e.g., R U R' U' F R2 B'"
            />
            <button
              onClick={handleGenerateScramble}
              disabled={loading}
              className="btn-secondary w-full mt-3"
            >
              {loading ? 'Generating...' : '🎲 Random Scramble'}
            </button>
          </div>

          {/* SOLVE BUTTON */}
          <button
            onClick={handleSolve}
            disabled={solving || !scramble.trim()}
            className="btn-primary w-full text-base py-4"
          >
            {solving ? 'Solving...' : 'Solve →'}
          </button>

          {error && (
            <div className="card p-4 bg-rose-50/80 border-rose-200">
              <p className="text-sm text-rose-700">{error}</p>
            </div>
          )}

          {/* SOLUTION CARD — Mint */}
          {solution && (
            <div className="card card-teal p-6 animate-slide-up">
              <div className="flex items-center justify-between mb-4">
                <h3 className="font-semibold text-stone-900">Solution</h3>
                <span className="badge bg-emerald-100 text-emerald-700">
                  {solution.movesUsed} moves
                </span>
              </div>

              <div className="flex flex-wrap gap-1.5">
                {solution.solution.map((move, i) => (
                  <span
                    key={i}
                    className="px-2.5 py-1.5 rounded-lg bg-gradient-to-br from-emerald-50 to-teal-50 font-mono text-sm font-medium text-emerald-800 border border-emerald-100 hover:shadow-md hover:-translate-y-0.5 transition-all cursor-default"
                    style={{ animationDelay: `${i * 40}ms` }}
                  >
                    {move}
                  </span>
                ))}
              </div>

              <div className="mt-4 pt-4 border-t border-stone-100 flex items-center gap-2 text-xs">
                {solution.isSolved ? (
                  <>
                    <span className="w-1.5 h-1.5 rounded-full bg-emerald-500" />
                    <span className="text-emerald-700 font-medium">
                      All edges oriented
                    </span>
                  </>
                ) : (
                  <>
                    <span className="w-1.5 h-1.5 rounded-full bg-amber-500" />
                    <span className="text-amber-700">Partial solution</span>
                  </>
                )}
              </div>
            </div>
          )}

          {/* HOW IT WORKS — Amber */}
          <div className="card card-amber p-6">
            <h4 className="label">How it works</h4>
            <ol className="space-y-3 text-sm text-stone-700">
              <li className="flex gap-3">
                <span className="w-6 h-6 rounded-lg flex items-center justify-center text-xs font-bold flex-shrink-0 text-white shadow-sm"
                      style={{ background: 'linear-gradient(135deg, #FB7185, #F472B6)' }}>1</span>
                <span>Encode edge orientations into a 2048-state space</span>
              </li>
              <li className="flex gap-3">
                <span className="w-6 h-6 rounded-lg flex items-center justify-center text-xs font-bold flex-shrink-0 text-white shadow-sm"
                      style={{ background: 'linear-gradient(135deg, #A78BFA, #818CF8)' }}>2</span>
                <span>BFS precomputes the shortest path to orientation</span>
              </li>
              <li className="flex gap-3">
                <span className="w-6 h-6 rounded-lg flex items-center justify-center text-xs font-bold flex-shrink-0 text-white shadow-sm"
                      style={{ background: 'linear-gradient(135deg, #34D399, #2DD4BF)' }}>3</span>
                <span>Greedy search returns optimal moves</span>
              </li>
            </ol>
          </div>
        </div>
      </section>

      {/* FOOTER */}
      <footer className="border-t border-white/60 bg-white/40 backdrop-blur py-8 text-center">
        <p className="text-xs text-stone-500">
          Built with <span className="text-rose-400">Spring Boot</span> ·{' '}
          <span className="text-sky-400">React</span> ·{' '}
          <span className="text-purple-400">Three.js</span> ·{' '}
          <span className="text-emerald-400">BFS Algorithm</span>
        </p>
      </footer>
    </div>
  );
}