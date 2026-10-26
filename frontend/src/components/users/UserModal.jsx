import { useState, useEffect } from 'react';

const EMPTY = { name: '', address: '', age: '' };

export default function UserModal({ show, mode, initialData, onSubmit, onClose }) {
  const [form, setForm] = useState(EMPTY);
  const [error, setError] = useState('');

  useEffect(() => {
    if (show) {
      setForm(initialData ? { name: initialData.name || '', address: initialData.address || '', age: initialData.age || '' } : EMPTY);
      setError('');
    }
  }, [show, initialData]);

  if (!show) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.name.trim() || !form.address.trim() || !form.age) {
      setError('Toate câmpurile sunt obligatorii.');
      return;
    }
    const ageNum = parseInt(form.age, 10);
    if (isNaN(ageNum) || ageNum < 18) {
      setError('Vârsta trebuie să fie de minim 18 ani.');
      return;
    }
    onSubmit({ name: form.name.trim(), address: form.address.trim(), age: ageNum });
  };

  const set = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  return (
    <div className="modal-backdrop-custom" onClick={onClose}>
      <div
        className="modal-dialog-custom shadow-lg"
        style={{ background: '#1e293b', border: '1px solid #334155', borderRadius: '14px', width: '100%', maxWidth: '520px', padding: 0, overflow: 'hidden' }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="d-flex justify-content-between align-items-center px-4 py-3" style={{ borderBottom: '1px solid #334155' }}>
          <h5 className="m-0 fw-semibold" style={{ color: '#ffffff' }}>
            {mode === 'create' ? '➕ Adaugă Utilizator Nou' : '✏️ Editează Utilizator'}
          </h5>
          <button type="button" className="btn-close btn-close-white" onClick={onClose} />
        </div>

        {/* Body */}
        <form onSubmit={handleSubmit}>
          <div className="px-4 py-3">
            {error && <div className="alert alert-danger py-2 px-3 mb-3" style={{ fontSize: '13px' }}>{error}</div>}

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>Nume Complet *</label>
              <input type="text" className="form-control form-control-dark" placeholder="ex: Popescu Ion" value={form.name} onChange={set('name')} required />
            </div>

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>Adresă de Domiciliu *</label>
              <input type="text" className="form-control form-control-dark" placeholder="ex: Str. Memorandumului 28, Cluj-Napoca" value={form.address} onChange={set('address')} required />
            </div>

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>Vârstă (ani) *</label>
              <input type="number" min="18" max="120" className="form-control form-control-dark" placeholder="Minim 18 ani" value={form.age} onChange={set('age')} required />
              <div className="form-text" style={{ color: '#64748b' }}>Regulă backend: vârsta minimă permisă este de 18 ani.</div>
            </div>
          </div>

          {/* Footer */}
          <div className="d-flex justify-content-end gap-2 px-4 py-3" style={{ background: '#0f172a', borderTop: '1px solid #334155' }}>
            <button type="button" className="btn btn-secondary" onClick={onClose}>Anulează</button>
            <button type="submit" className="btn btn-primary">
              {mode === 'create' ? 'Creează Utilizator' : 'Salvează Modificările'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
