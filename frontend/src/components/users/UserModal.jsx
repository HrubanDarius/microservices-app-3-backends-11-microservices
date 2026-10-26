import { useState, useEffect } from 'react';

const EMPTY = {
  name: '',
  address: '',
  age: '',
  username: '',
  password: '',
  role: 'CLIENT',
};

export default function UserModal({ show, mode, initialData, onSubmit, onClose }) {
  const [form, setForm] = useState(EMPTY);
  const [error, setError] = useState('');

  useEffect(() => {
    if (show) {
      setForm(
        initialData
          ? {
              name: initialData.name || '',
              address: initialData.address || '',
              age: initialData.age || '',
              username: '',
              password: '',
              role: 'CLIENT',
            }
          : EMPTY
      );
      setError('');
    }
  }, [show, initialData]);

  if (!show) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.name.trim() || !form.address.trim() || !form.age) {
      setError('Numele, adresa si varsta sunt obligatorii.');
      return;
    }
    const ageNum = parseInt(form.age, 10);
    if (isNaN(ageNum) || ageNum < 18) {
      setError('Varsta trebuie sa fie de minim 18 ani.');
      return;
    }

    if (mode === 'create') {
      if (!form.username.trim() || !form.password) {
        setError('Numele de utilizator si parola sunt obligatorii pentru crearea contului.');
        return;
      }
    }

    onSubmit({
      name: form.name.trim(),
      address: form.address.trim(),
      age: ageNum,
      username: form.username.trim(),
      password: form.password,
      role: form.role || 'CLIENT',
    });
  };

  const set = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  return (
    <div className="modal-backdrop-custom" onClick={onClose}>
      <div
        className="shadow-lg"
        style={{
          background: '#1e293b',
          border: '1px solid #334155',
          borderRadius: '14px',
          width: '100%',
          maxWidth: '520px',
          padding: 0,
          overflow: 'hidden',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div
          className="d-flex justify-content-between align-items-center px-4 py-3"
          style={{ borderBottom: '1px solid #334155' }}
        >
          <h5 className="m-0 fw-semibold" style={{ color: '#ffffff' }}>
            {mode === 'create'
              ? '➕ Adauga Utilizator & Cont Acces'
              : '✏️ Editeaza Utilizator'}
          </h5>
          <button type="button" className="btn-close btn-close-white" onClick={onClose} />
        </div>

        {/* Body */}
        <form onSubmit={handleSubmit}>
          <div className="px-4 py-3">
            {error && (
              <div className="alert alert-danger py-2 px-3 mb-3" style={{ fontSize: '13px' }}>
                {error}
              </div>
            )}

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>
                Nume Complet *
              </label>
              <input
                type="text"
                className="form-control form-control-dark"
                placeholder="ex: Popescu Ion"
                value={form.name}
                onChange={set('name')}
                required
              />
            </div>

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>
                Adresa de Domiciliu *
              </label>
              <input
                type="text"
                className="form-control form-control-dark"
                placeholder="ex: Str. Memorandumului 28, Cluj-Napoca"
                value={form.address}
                onChange={set('address')}
                required
              />
            </div>

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>
                Varsta (ani) *
              </label>
              <input
                type="number"
                min="18"
                max="120"
                className="form-control form-control-dark"
                placeholder="Minim 18 ani"
                value={form.age}
                onChange={set('age')}
                required
              />
              <div className="form-text" style={{ color: '#64748b' }}>
                Regula backend: varsta minima permisa este de 18 ani.
              </div>
            </div>

            {/* Campuri suplimentare de autentificare la creare cont */}
            {mode === 'create' && (
              <div
                className="mt-3 pt-3"
                style={{ borderTop: '1px dashed #334155' }}
              >
                <h6 className="text-info small fw-bold mb-3">
                  🔐 Date Cont Autentificare (auth-service)
                </h6>

                <div className="mb-3">
                  <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>
                    Nume Utilizator (Username Login) *
                  </label>
                  <input
                    type="text"
                    className="form-control form-control-dark"
                    placeholder="ex: ion_popescu"
                    value={form.username}
                    onChange={set('username')}
                    required
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>
                    Parola *
                  </label>
                  <input
                    type="password"
                    className="form-control form-control-dark"
                    placeholder="••••••••"
                    value={form.password}
                    onChange={set('password')}
                    required
                  />
                </div>

                <div className="mb-2">
                  <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>
                    Rol in Sistem *
                  </label>
                  <select
                    className="form-select form-select-dark"
                    value={form.role}
                    onChange={set('role')}
                  >
                    <option value="CLIENT">👤 CLIENT (Acces doar la dispozitive proprii)</option>
                    <option value="ADMIN">👑 ADMIN (Acces complet administrare)</option>
                  </select>
                </div>
              </div>
            )}
          </div>

          {/* Footer */}
          <div
            className="d-flex justify-content-end gap-2 px-4 py-3"
            style={{ background: '#0f172a', borderTop: '1px solid #334155' }}
          >
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Anuleaza
            </button>
            <button type="submit" className="btn btn-primary">
              {mode === 'create' ? 'Creeaza Utilizator si Cont' : 'Salveaza Modificarile'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
