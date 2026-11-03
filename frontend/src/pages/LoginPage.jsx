import { useState } from 'react';
import { login, register } from '../services/authService';
import { createUser } from '../services/userService';

export default function LoginPage({ onLoginSuccess }) {
  const [activeTab, setActiveTab] = useState('login'); // 'login' | 'register'

  // Login form state
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  // Register form state
  const [regUsername, setRegUsername] = useState('');
  const [regPassword, setRegPassword] = useState('');
  const [regName, setRegName] = useState('');
  const [regAddress, setRegAddress] = useState('');
  const [regAge, setRegAge] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // ===================== LOGIN =====================
  const performLogin = async (user, pass) => {
    setError('');
    setLoading(true);
    try {
      const loggedUser = await login(user, pass);
      onLoginSuccess(loggedUser);
    } catch (err) {
      setError(err.message || 'Autentificare esuata. Verificati datele introduse.');
    } finally {
      setLoading(false);
    }
  };

  const handleLoginSubmit = (e) => {
    e.preventDefault();
    if (!username.trim() || !password) {
      setError('Completati atat utilizatorul cat si parola.');
      return;
    }
    performLogin(username.trim(), password);
  };

  const handleQuickLogin = (quickUser, quickPass) => {
    setUsername(quickUser);
    setPassword(quickPass);
    performLogin(quickUser, quickPass);
  };

  // ===================== REGISTER =====================
  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (
      !regUsername.trim() ||
      !regPassword ||
      !regName.trim() ||
      !regAddress.trim() ||
      !regAge
    ) {
      setError('Toate campurile sunt obligatorii pentru inregistrare.');
      return;
    }

    const ageNum = parseInt(regAge, 10);
    if (isNaN(ageNum) || ageNum < 18) {
      setError('Varsta trebuie sa fie de minim 18 ani.');
      return;
    }

    setLoading(true);
    try {
      // 1. Inregistrare cont in auth-service (ruta publica in gateway)
      await register({
        username: regUsername.trim(),
        password: regPassword,
        role: 'CLIENT',
      });

      // 2. Autentificare imediata pentru obtinerea token-ului JWT si a ID-ului sincronizat
      const loggedUser = await login(regUsername.trim(), regPassword);

      // 3. Creare profil in user-service folosind token-ul primit si ID-ul sincronizat
      await createUser(
        {
          id: loggedUser.id,
          name: regName.trim(),
          address: regAddress.trim(),
          age: ageNum,
        },
        loggedUser.token
      );

      // 4. Directionare catre aplicatie ca utilizator CLIENT conectat
      onLoginSuccess(loggedUser);
    } catch (err) {
      setError(err.message || 'Inregistrare esuata. Incercati din nou.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="d-flex align-items-center justify-content-center px-3 py-5"
      style={{ minHeight: 'calc(100vh - 70px)' }}
    >
      <div
        className="card shadow-lg p-4"
        style={{
          width: '100%',
          maxWidth: '460px',
          background: '#1e293b',
          border: '1px solid #334155',
          borderRadius: '16px',
        }}
      >
        {/* Header */}
        <div className="text-center mb-3">
          <div className="fs-1 mb-2">⚡</div>
          <h3 className="fw-bold text-white mb-1">Energy Management</h3>
          <p className="text-secondary small mb-0">
            Sistem Distribuit de Monitorizare Energetica
          </p>
        </div>

        {/* Taburi Comutare Login / Register */}
        <div
          className="d-flex rounded-3 p-1 mb-4"
          style={{ background: '#0f172a', border: '1px solid #334155' }}
        >
          <button
            type="button"
            className={`btn flex-fill py-2 fw-semibold ${
              activeTab === 'login' ? 'btn-primary' : 'btn-link text-secondary'
            }`}
            style={{ textDecoration: 'none' }}
            onClick={() => {
              setActiveTab('login');
              setError('');
            }}
          >
            🔐 Autentificare
          </button>
          <button
            type="button"
            className={`btn flex-fill py-2 fw-semibold ${
              activeTab === 'register' ? 'btn-primary' : 'btn-link text-secondary'
            }`}
            style={{ textDecoration: 'none' }}
            onClick={() => {
              setActiveTab('register');
              setError('');
            }}
          >
            📝 Inregistrare Cont
          </button>
        </div>

        {/* Mesaj de eroare */}
        {error && (
          <div
            className="alert alert-danger py-2 px-3 mb-3 d-flex align-items-center gap-2"
            style={{ fontSize: '13px' }}
          >
            <span>⚠️</span>
            <span>{error}</span>
          </div>
        )}

        {/* ===================== TABUL DE AUTENTIFICARE ===================== */}
        {activeTab === 'login' && (
          <>
            <form onSubmit={handleLoginSubmit}>
              <div className="mb-3">
                <label className="form-label text-light small fw-semibold">
                  Nume Utilizator
                </label>
                <input
                  type="text"
                  className="form-control form-control-dark"
                  placeholder="ex: admin"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  disabled={loading}
                  autoFocus
                  required
                />
              </div>

              <div className="mb-4">
                <label className="form-label text-light small fw-semibold">
                  Parola
                </label>
                <input
                  type="password"
                  className="form-control form-control-dark"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  disabled={loading}
                  required
                />
              </div>

              <button
                type="submit"
                className="btn btn-primary w-100 py-2 fw-semibold d-flex align-items-center justify-content-center gap-2"
                disabled={loading}
              >
                {loading ? (
                  <>
                    <span
                      className="spinner-border spinner-border-sm"
                      role="status"
                      aria-hidden="true"
                    />
                    <span>Se autentifica...</span>
                  </>
                ) : (
                  <span>Conectare</span>
                )}
              </button>
            </form>

            {/* Buton rapid de testare Admin */}
            <div
              className="mt-4 pt-3 text-center"
              style={{ borderTop: '1px solid #334155' }}
            >
              <p className="text-secondary small mb-2">
                Cont demo administrator:
              </p>
              <button
                type="button"
                className="btn btn-sm btn-outline-info w-100 py-2 fw-semibold"
                onClick={() => handleQuickLogin('admin', 'admin123')}
                disabled={loading}
              >
                👑 Log in as Admin (admin / admin123)
              </button>
            </div>
          </>
        )}

        {/* ===================== TABUL DE INREGISTRARE ===================== */}
        {activeTab === 'register' && (
          <form onSubmit={handleRegisterSubmit}>
            <div className="mb-3">
              <label className="form-label text-light small fw-semibold">
                Nume Utilizator (pentru Login) *
              </label>
              <input
                type="text"
                className="form-control form-control-dark"
                placeholder="ex: utilizator_nou"
                value={regUsername}
                onChange={(e) => setRegUsername(e.target.value)}
                disabled={loading}
                required
              />
            </div>

            <div className="mb-3">
              <label className="form-label text-light small fw-semibold">
                Parola *
              </label>
              <input
                type="password"
                className="form-control form-control-dark"
                placeholder="••••••••"
                value={regPassword}
                onChange={(e) => setRegPassword(e.target.value)}
                disabled={loading}
                required
              />
            </div>

            <div className="mb-3">
              <label className="form-label text-light small fw-semibold">
                Nume Complet *
              </label>
              <input
                type="text"
                className="form-control form-control-dark"
                placeholder="ex: Popescu Ion"
                value={regName}
                onChange={(e) => setRegName(e.target.value)}
                disabled={loading}
                required
              />
            </div>

            <div className="mb-3">
              <label className="form-label text-light small fw-semibold">
                Adresa de Domiciliu *
              </label>
              <input
                type="text"
                className="form-control form-control-dark"
                placeholder="ex: Str. Memorandumului 28, Cluj-Napoca"
                value={regAddress}
                onChange={(e) => setRegAddress(e.target.value)}
                disabled={loading}
                required
              />
            </div>

            <div className="mb-4">
              <label className="form-label text-light small fw-semibold">
                Varsta (ani) *
              </label>
              <input
                type="number"
                min="18"
                max="120"
                className="form-control form-control-dark"
                placeholder="Minim 18 ani"
                value={regAge}
                onChange={(e) => setRegAge(e.target.value)}
                disabled={loading}
                required
              />
              <div className="form-text" style={{ color: '#64748b' }}>
                Regula sistem: varsta minima admisa este de 18 ani.
              </div>
            </div>

            <button
              type="submit"
              className="btn btn-success w-100 py-2 fw-semibold d-flex align-items-center justify-content-center gap-2"
              disabled={loading}
            >
              {loading ? (
                <>
                  <span
                    className="spinner-border spinner-border-sm"
                    role="status"
                    aria-hidden="true"
                  />
                  <span>Se creeaza contul...</span>
                </>
              ) : (
                <span>Creeaza Cont si Conecteaza-te</span>
              )}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}
