import { useState, useEffect } from 'react';

const EMPTY = { description: '', address: '', maxHourlyEnergyConsumption: '', userId: '' };

export default function DeviceModal({ show, mode, initialData, users, onSubmit, onClose }) {
  const [form, setForm] = useState(EMPTY);
  const [error, setError] = useState('');

  useEffect(() => {
    if (show) {
      setForm(
        initialData
          ? {
              description: initialData.description || '',
              address: initialData.address || '',
              maxHourlyEnergyConsumption: initialData.maxHourlyEnergyConsumption || '',
              userId: initialData.userId || '',
            }
          : EMPTY
      );
      setError('');
    }
  }, [show, initialData]);

  if (!show) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.description.trim() || !form.address.trim() || !form.maxHourlyEnergyConsumption) {
      setError('Descrierea, adresa si consumul maxim sunt obligatorii.');
      return;
    }
    const consumptionNum = parseFloat(form.maxHourlyEnergyConsumption);
    if (isNaN(consumptionNum) || consumptionNum <= 0) {
      setError('Consumul maxim orar trebuie sa fie pozitiv (> 0).');
      return;
    }
    onSubmit({
      description: form.description.trim(),
      address: form.address.trim(),
      maxHourlyEnergyConsumption: consumptionNum,
      userId: form.userId || null,
    });
  };

  const set = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  return (
    <div className="modal-backdrop-custom" onClick={onClose}>
      <div
        className="shadow-lg"
        style={{ background: '#1e293b', border: '1px solid #334155', borderRadius: '14px', width: '100%', maxWidth: '540px', padding: 0, overflow: 'hidden' }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="d-flex justify-content-between align-items-center px-4 py-3" style={{ borderBottom: '1px solid #334155' }}>
          <h5 className="m-0 fw-semibold" style={{ color: '#ffffff' }}>
            {mode === 'create' ? '➕ Adauga Dispozitiv Nou' : '✏️ Editeaza Dispozitiv'}
          </h5>
          <button type="button" className="btn-close btn-close-white" onClick={onClose} />
        </div>

        {/* Body */}
        <form onSubmit={handleSubmit}>
          <div className="px-4 py-3">
            {error && <div className="alert alert-danger py-2 px-3 mb-3" style={{ fontSize: '13px' }}>{error}</div>}

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>Descriere Dispozitiv *</label>
              <input type="text" className="form-control form-control-dark" placeholder="ex: Smart Meter - Panou Solar Bucatarie" value={form.description} onChange={set('description')} required />
            </div>

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>Adresa Instalare *</label>
              <input type="text" className="form-control form-control-dark" placeholder="ex: Str. Observatorului 34, Ap. 12" value={form.address} onChange={set('address')} required />
            </div>

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>Consum Maxim Orar (kWh) *</label>
              <input type="number" step="0.1" min="0.1" className="form-control form-control-dark" placeholder="ex: 18.5" value={form.maxHourlyEnergyConsumption} onChange={set('maxHourlyEnergyConsumption')} required />
              <div className="form-text" style={{ color: '#64748b' }}>Valoare strict pozitiva reprezentand limita maxima admisa.</div>
            </div>

            <div className="mb-3">
              <label className="form-label" style={{ color: '#cbd5e1', fontSize: '13px' }}>Utilizator Asociat (One-to-Many)</label>
              <select className="form-select form-select-dark" value={form.userId} onChange={set('userId')}>
                <option value="">-- Neasignat (None) --</option>
                {users.map((u) => (
                  <option key={u.id} value={u.id}>
                    👤 {u.name} (ID: {u.id.substring(0, 8)}...)
                  </option>
                ))}
              </select>
              <div className="form-text" style={{ color: '#64748b' }}>
                Selectia aloca dispozitivul catre un utilizator din tabela replica <code>user_account</code>.
              </div>
            </div>
          </div>

          {/* Footer */}
          <div className="d-flex justify-content-end gap-2 px-4 py-3" style={{ background: '#0f172a', borderTop: '1px solid #334155' }}>
            <button type="button" className="btn btn-secondary" onClick={onClose}>Anuleaza</button>
            <button type="submit" className="btn btn-primary">
              {mode === 'create' ? 'Creeaza Dispozitiv' : 'Salveaza Modificarile'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
