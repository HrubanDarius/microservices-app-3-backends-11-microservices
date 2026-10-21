import { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [users, setUsers] = useState([]);
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('all'); // 'all', 'users', 'devices'
  const [userSearch, setUserSearch] = useState('');
  const [deviceSearch, setDeviceSearch] = useState('');
  const [notification, setNotification] = useState(null);

  // User Modal State
  const [isUserModalOpen, setIsUserModalOpen] = useState(false);
  const [userModalMode, setUserModalMode] = useState('create'); // 'create' | 'edit'
  const [userForm, setUserForm] = useState({ id: '', name: '', address: '', age: '' });

  // Device Modal State
  const [isDeviceModalOpen, setIsDeviceModalOpen] = useState(false);
  const [deviceModalMode, setDeviceModalMode] = useState('create'); // 'create' | 'edit'
  const [deviceForm, setDeviceForm] = useState({
    id: '',
    description: '',
    address: '',
    maxHourlyEnergyConsumption: '',
    userId: ''
  });

  // Delete Confirmation Modal State
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null); // { type: 'user' | 'device', id: '', name: '' }

  const showNotification = (type, message) => {
    setNotification({ type, message });
    setTimeout(() => {
      setNotification(null);
    }, 4500);
  };

  // Preluare utilizatori din user-service via Traefik
  const fetchUsers = async () => {
    try {
      const res = await fetch('/user-service/person');
      if (!res.ok) throw new Error(`Status ${res.status}`);
      const data = await res.json();
      setUsers(data);
    } catch (err) {
      showNotification('error', `Eroare conexiune User Microservice: ${err.message}`);
    }
  };

  // Preluare dispozitive din device-service via Traefik
  const fetchDevices = async () => {
    try {
      const res = await fetch('/device-service/device');
      if (!res.ok) throw new Error(`Status ${res.status}`);
      const data = await res.json();
      setDevices(data);
    } catch (err) {
      showNotification('error', `Eroare conexiune Device Microservice: ${err.message}`);
    }
  };

  const loadAllData = async () => {
    setLoading(true);
    await Promise.all([fetchUsers(), fetchDevices()]);
    setLoading(false);
  };

  useEffect(() => {
    loadAllData();
  }, []);

  // ==========================================
  // Handlers CRUD Utilizatori
  // ==========================================
  const handleOpenAddUser = () => {
    setUserForm({ id: '', name: '', address: '', age: '' });
    setUserModalMode('create');
    setIsUserModalOpen(true);
  };

  const handleOpenEditUser = (user) => {
    setUserForm({
      id: user.id,
      name: user.name || '',
      address: user.address || '',
      age: user.age || ''
    });
    setUserModalMode('edit');
    setIsUserModalOpen(true);
  };

  const handleSubmitUser = async (e) => {
    e.preventDefault();
    if (!userForm.name.trim() || !userForm.address.trim() || !userForm.age) {
      showNotification('error', 'Toate câmpurile sunt obligatorii.');
      return;
    }

    const ageNum = parseInt(userForm.age, 10);
    if (isNaN(ageNum) || ageNum < 18) {
      showNotification('error', 'Vârsta trebuie să fie de minim 18 ani.');
      return;
    }

    try {
      if (userModalMode === 'create') {
        const res = await fetch('/user-service/person', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            name: userForm.name.trim(),
            address: userForm.address.trim(),
            age: ageNum
          })
        });

        if (!res.ok) {
          const errMsg = await res.text();
          throw new Error(errMsg || `Eroare HTTP ${res.status}`);
        }
        showNotification('success', 'Utilizatorul a fost adăugat cu succes!');
      } else {
        const res = await fetch(`/user-service/person/${userForm.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            id: userForm.id,
            name: userForm.name.trim(),
            address: userForm.address.trim(),
            age: ageNum
          })
        });

        if (!res.ok) {
          const errMsg = await res.text();
          throw new Error(errMsg || `Eroare HTTP ${res.status}`);
        }
        showNotification('success', 'Utilizatorul a fost actualizat cu succes!');
      }

      setIsUserModalOpen(false);
      await fetchUsers();
    } catch (err) {
      showNotification('error', `Operație eșuată: ${err.message}`);
    }
  };

  // ==========================================
  // Handlers CRUD Dispozitive
  // ==========================================
  const handleOpenAddDevice = () => {
    setDeviceForm({
      id: '',
      description: '',
      address: '',
      maxHourlyEnergyConsumption: '',
      userId: ''
    });
    setDeviceModalMode('create');
    setIsDeviceModalOpen(true);
  };

  const handleOpenEditDevice = (device) => {
    setDeviceForm({
      id: device.id,
      description: device.description || '',
      address: device.address || '',
      maxHourlyEnergyConsumption: device.maxHourlyEnergyConsumption || '',
      userId: device.userId || ''
    });
    setDeviceModalMode('edit');
    setIsDeviceModalOpen(true);
  };

  const handleSubmitDevice = async (e) => {
    e.preventDefault();
    if (!deviceForm.description.trim() || !deviceForm.address.trim() || !deviceForm.maxHourlyEnergyConsumption) {
      showNotification('error', 'Descrierea, adresa și consumul maxim sunt obligatorii.');
      return;
    }

    const consumptionNum = parseFloat(deviceForm.maxHourlyEnergyConsumption);
    if (isNaN(consumptionNum) || consumptionNum <= 0) {
      showNotification('error', 'Consumul maxim orar trebuie să fie pozitiv (> 0).');
      return;
    }

    const payload = {
      description: deviceForm.description.trim(),
      address: deviceForm.address.trim(),
      maxHourlyEnergyConsumption: consumptionNum,
      userId: deviceForm.userId ? deviceForm.userId : null
    };

    try {
      if (deviceModalMode === 'create') {
        const res = await fetch('/device-service/device', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });

        if (!res.ok) {
          const errMsg = await res.text();
          throw new Error(errMsg || `Eroare HTTP ${res.status}`);
        }
        showNotification('success', 'Dispozitivul a fost salvat cu succes!');
      } else {
        payload.id = deviceForm.id;
        const res = await fetch(`/device-service/device/${deviceForm.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });

        if (!res.ok) {
          const errMsg = await res.text();
          throw new Error(errMsg || `Eroare HTTP ${res.status}`);
        }
        showNotification('success', 'Dispozitivul a fost actualizat cu succes!');
      }

      setIsDeviceModalOpen(false);
      await fetchDevices();
    } catch (err) {
      showNotification('error', `Operație eșuată: ${err.message}`);
    }
  };

  // ==========================================
  // Ștergere (cu cascadă la ștergere user)
  // ==========================================
  const handleOpenDelete = (type, item) => {
    setDeleteTarget({
      type,
      id: item.id,
      name: type === 'user' ? item.name : item.description
    });
    setIsDeleteModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!deleteTarget) return;

    try {
      if (deleteTarget.type === 'user') {
        const res = await fetch(`/user-service/person/${deleteTarget.id}`, {
          method: 'DELETE'
        });

        if (!res.ok) {
          const errMsg = await res.text();
          throw new Error(errMsg || `Eroare HTTP ${res.status}`);
        }

        // IMPORTANT: Reîmprospătăm ambele liste deoarece ștergerea userului
        // declanșează ștergerea în cascadă a dispozitivelor sale!
        await Promise.all([fetchUsers(), fetchDevices()]);
        showNotification('success', `Utilizatorul "${deleteTarget.name}" și dispozitivele asociate au fost șterse.`);
      } else {
        const res = await fetch(`/device-service/device/${deleteTarget.id}`, {
          method: 'DELETE'
        });

        if (!res.ok) {
          const errMsg = await res.text();
          throw new Error(errMsg || `Eroare HTTP ${res.status}`);
        }

        await fetchDevices();
        showNotification('success', `Dispozitivul "${deleteTarget.name}" a fost șters.`);
      }

      setIsDeleteModalOpen(false);
      setDeleteTarget(null);
    } catch (err) {
      showNotification('error', `Ștergere eșuată: ${err.message}`);
    }
  };

  // Găsire nume utilizator pentru afișare în tabelul de dispozitive
  const getAssignedUserName = (userId) => {
    if (!userId) return null;
    const found = users.find((u) => u.id === userId);
    return found ? found.name : null;
  };

  // Filtrare căutare
  const filteredUsers = users.filter((u) =>
    (u.name || '').toLowerCase().includes(userSearch.toLowerCase()) ||
    (u.address || '').toLowerCase().includes(userSearch.toLowerCase()) ||
    (u.id || '').toLowerCase().includes(userSearch.toLowerCase())
  );

  const filteredDevices = devices.filter((d) =>
    (d.description || '').toLowerCase().includes(deviceSearch.toLowerCase()) ||
    (d.address || '').toLowerCase().includes(deviceSearch.toLowerCase()) ||
    (d.id || '').toLowerCase().includes(deviceSearch.toLowerCase())
  );

  // Statistici rapide
  const totalAssignedDevices = devices.filter((d) => d.userId).length;
  const totalUnassignedDevices = devices.length - totalAssignedDevices;

  return (
    <div className="app-container">
      {/* Header Panou */}
      <header className="dashboard-header">
        <div className="header-title">
          <h1>
            ⚡ Energy Management System
          </h1>
          <p>Panou de Administrare - Microservicii orchestrate prin Traefik Reverse Proxy (:80)</p>
        </div>
        <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
          <span className="header-badge">Online • 8 Containere</span>
          <button className="btn btn-secondary" onClick={loadAllData} title="Reîmprospătează datele">
            🔄 Refresh
          </button>
        </div>
      </header>

      {/* Mesaj Notificare */}
      {notification && (
        <div className={`alert alert-${notification.type}`}>
          <span>{notification.message}</span>
          <button className="alert-close" onClick={() => setNotification(null)}>×</button>
        </div>
      )}

      {/* Statistici Rapide */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon icon-blue">👥</div>
          <div className="stat-info">
            <h3>Total Utilizatori</h3>
            <p className="stat-value">{users.length}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon icon-purple">📟</div>
          <div className="stat-info">
            <h3>Total Dispozitive</h3>
            <p className="stat-value">{devices.length}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon icon-emerald">🔗</div>
          <div className="stat-info">
            <h3>Dispozitive Alocate</h3>
            <p className="stat-value">{totalAssignedDevices}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon icon-amber">⚠️</div>
          <div className="stat-info">
            <h3>Dispozitive Nealocate</h3>
            <p className="stat-value">{totalUnassignedDevices}</p>
          </div>
        </div>
      </div>

      {/* Taburi Navigare Secțiuni */}
      <div className="view-tabs">
        <button
          className={`tab-btn ${activeTab === 'all' ? 'active' : ''}`}
          onClick={() => setActiveTab('all')}
        >
          📊 Vedere Generală
        </button>
        <button
          className={`tab-btn ${activeTab === 'users' ? 'active' : ''}`}
          onClick={() => setActiveTab('users')}
        >
          👥 Utilizatori ({users.length})
        </button>
        <button
          className={`tab-btn ${activeTab === 'devices' ? 'active' : ''}`}
          onClick={() => setActiveTab('devices')}
        >
          📟 Dispozitive ({devices.length})
        </button>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 20px', color: '#94a3b8' }}>
          <div className="loading-spinner" style={{ width: '32px', height: '32px', marginBottom: '16px' }}></div>
          <p>Se încarcă datele din microservicii...</p>
        </div>
      ) : (
        <>
          {/* ========================================================
              SECȚIUNEA 1: UTILIZATORI (USER MANAGEMENT)
             ======================================================== */}
          {(activeTab === 'all' || activeTab === 'users') && (
            <div className="section-panel">
              <div className="panel-header">
                <div className="panel-title">
                  <h2>👥 Gestiune Utilizatori</h2>
                  <span className="header-badge" style={{ background: 'rgba(96, 165, 250, 0.1)', color: '#60a5fa' }}>
                    {users.length} înregistrați
                  </span>
                </div>
                <div className="panel-actions">
                  <input
                    type="text"
                    className="search-input"
                    placeholder="Caută utilizator..."
                    value={userSearch}
                    onChange={(e) => setUserSearch(e.target.value)}
                  />
                  <button className="btn btn-primary" onClick={handleOpenAddUser}>
                    ➕ Adaugă Utilizator
                  </button>
                </div>
              </div>

              <div className="table-wrapper">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Nume</th>
                      <th>Adresă</th>
                      <th>Vârstă</th>
                      <th>ID Utilizator (UUID)</th>
                      <th style={{ textAlign: 'right' }}>Acțiuni</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredUsers.length === 0 ? (
                      <tr>
                        <td colSpan="5" className="empty-state">
                          Nu a fost găsit niciun utilizator.
                        </td>
                      </tr>
                    ) : (
                      filteredUsers.map((user) => (
                        <tr key={user.id}>
                          <td style={{ fontWeight: 600, color: '#f8fafc' }}>{user.name}</td>
                          <td>{user.address || '—'}</td>
                          <td>{user.age} ani</td>
                          <td>
                            <span className="id-badge" title={user.id}>
                              {user.id}
                            </span>
                          </td>
                          <td style={{ textAlign: 'right' }}>
                            <div className="action-buttons" style={{ justifyContent: 'flex-end' }}>
                              <button
                                className="btn btn-outline-primary"
                                onClick={() => handleOpenEditUser(user)}
                              >
                                ✏️ Editează
                              </button>
                              <button
                                className="btn btn-outline-danger"
                                onClick={() => handleOpenDelete('user', user)}
                              >
                                🗑️ Șterge
                              </button>
                            </div>
                          </td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* ========================================================
              SECȚIUNEA 2: DISPOZITIVE (DEVICE MANAGEMENT)
             ======================================================== */}
          {(activeTab === 'all' || activeTab === 'devices') && (
            <div className="section-panel">
              <div className="panel-header">
                <div className="panel-title">
                  <h2>📟 Gestiune Dispozitive Energetice</h2>
                  <span className="header-badge" style={{ background: 'rgba(168, 85, 247, 0.1)', color: '#c084fc' }}>
                    {devices.length} înregistrate
                  </span>
                </div>
                <div className="panel-actions">
                  <input
                    type="text"
                    className="search-input"
                    placeholder="Caută dispozitiv..."
                    value={deviceSearch}
                    onChange={(e) => setDeviceSearch(e.target.value)}
                  />
                  <button className="btn btn-primary" onClick={handleOpenAddDevice}>
                    ➕ Adaugă Dispozitiv
                  </button>
                </div>
              </div>

              <div className="table-wrapper">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Descriere</th>
                      <th>Adresă Instalare</th>
                      <th>Consum Maxim (kWh)</th>
                      <th>Utilizator Asociat</th>
                      <th>ID Dispozitiv (UUID)</th>
                      <th style={{ textAlign: 'right' }}>Acțiuni</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredDevices.length === 0 ? (
                      <tr>
                        <td colSpan="6" className="empty-state">
                          Nu a fost găsit niciun dispozitiv energetic.
                        </td>
                      </tr>
                    ) : (
                      filteredDevices.map((device) => {
                        const userName = getAssignedUserName(device.userId);
                        return (
                          <tr key={device.id}>
                            <td style={{ fontWeight: 600, color: '#f8fafc' }}>{device.description}</td>
                            <td>{device.address || '—'}</td>
                            <td>
                              <span style={{ color: '#38bdf8', fontWeight: 600 }}>
                                {device.maxHourlyEnergyConsumption} kWh
                              </span>
                            </td>
                            <td>
                              {device.userId ? (
                                <div className="user-badge" title={`UUID: ${device.userId}`}>
                                  👤 {userName ? userName : `User: ${device.userId.substring(0, 8)}...`}
                                </div>
                              ) : (
                                <span className="unassigned-badge">Neasignat</span>
                              )}
                            </td>
                            <td>
                              <span className="id-badge" title={device.id}>
                                {device.id}
                              </span>
                            </td>
                            <td style={{ textAlign: 'right' }}>
                              <div className="action-buttons" style={{ justifyContent: 'flex-end' }}>
                                <button
                                  className="btn btn-outline-primary"
                                  onClick={() => handleOpenEditDevice(device)}
                                >
                                  ✏️ Editează
                                </button>
                                <button
                                  className="btn btn-outline-danger"
                                  onClick={() => handleOpenDelete('device', device)}
                                >
                                  🗑️ Șterge
                                </button>
                              </div>
                            </td>
                          </tr>
                        );
                      })
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </>
      )}

      {/* ========================================================
          MODAL ADĂUGARE / EDITARE UTILIZATOR
         ======================================================== */}
      {isUserModalOpen && (
        <div className="modal-backdrop" onClick={() => setIsUserModalOpen(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>{userModalMode === 'create' ? '➕ Adaugă Utilizator Nou' : '✏️ Editează Utilizator'}</h3>
              <button className="modal-close-btn" onClick={() => setIsUserModalOpen(false)}>×</button>
            </div>
            <form onSubmit={handleSubmitUser}>
              <div className="modal-body">
                <div className="form-group">
                  <label>Nume Complet *</label>
                  <input
                    type="text"
                    className="form-input"
                    placeholder="ex: Popescu Ion"
                    value={userForm.name}
                    onChange={(e) => setUserForm({ ...userForm, name: e.target.value })}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Adresă de Domiciliu *</label>
                  <input
                    type="text"
                    className="form-input"
                    placeholder="ex: Str. Memorandumului 28, Cluj-Napoca"
                    value={userForm.address}
                    onChange={(e) => setUserForm({ ...userForm, address: e.target.value })}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Vârstă (ani) *</label>
                  <input
                    type="number"
                    min="18"
                    max="120"
                    className="form-input"
                    placeholder="Minim 18 ani"
                    value={userForm.age}
                    onChange={(e) => setUserForm({ ...userForm, age: e.target.value })}
                    required
                  />
                  <span className="form-helper">Regulă backend: vârsta minimă permisă este de 18 ani.</span>
                </div>
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setIsUserModalOpen(false)}
                >
                  Anulează
                </button>
                <button type="submit" className="btn btn-primary">
                  {userModalMode === 'create' ? 'Creează Utilizator' : 'Salvează Modificările'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* ========================================================
          MODAL ADĂUGARE / EDITARE DISPOZITIV (CU ASOCIERE USER)
         ======================================================== */}
      {isDeviceModalOpen && (
        <div className="modal-backdrop" onClick={() => setIsDeviceModalOpen(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>{deviceModalMode === 'create' ? '➕ Adaugă Dispozitiv Nou' : '✏️ Editează Dispozitiv'}</h3>
              <button className="modal-close-btn" onClick={() => setIsDeviceModalOpen(false)}>×</button>
            </div>
            <form onSubmit={handleSubmitDevice}>
              <div className="modal-body">
                <div className="form-group">
                  <label>Descriere Dispozitiv *</label>
                  <input
                    type="text"
                    className="form-input"
                    placeholder="ex: Smart Meter - Panou Solar Bucătărie"
                    value={deviceForm.description}
                    onChange={(e) => setDeviceForm({ ...deviceForm, description: e.target.value })}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Adresă Instalare *</label>
                  <input
                    type="text"
                    className="form-input"
                    placeholder="ex: Str. Observatorului 34, Ap. 12"
                    value={deviceForm.address}
                    onChange={(e) => setDeviceForm({ ...deviceForm, address: e.target.value })}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Consum Maxim Orar (kWh) *</label>
                  <input
                    type="number"
                    step="0.1"
                    min="0.1"
                    className="form-input"
                    placeholder="ex: 18.5"
                    value={deviceForm.maxHourlyEnergyConsumption}
                    onChange={(e) => setDeviceForm({ ...deviceForm, maxHourlyEnergyConsumption: e.target.value })}
                    required
                  />
                  <span className="form-helper">Valoare strict pozitivă reprezentând limita maximă admisă.</span>
                </div>

                {/* Dropdown de Asociere Dispozitiv -> Utilizator */}
                <div className="form-group">
                  <label>Utilizator Asociat (One-to-Many)</label>
                  <select
                    className="form-select"
                    value={deviceForm.userId}
                    onChange={(e) => setDeviceForm({ ...deviceForm, userId: e.target.value })}
                  >
                    <option value="">-- Neasignat (None) --</option>
                    {users.map((u) => (
                      <option key={u.id} value={u.id}>
                        👤 {u.name} (ID: {u.id.substring(0, 8)}...)
                      </option>
                    ))}
                  </select>
                  <span className="form-helper">
                    Selecția alocă dispozitivul către un utilizator din tabela replică `user_account`.
                  </span>
                </div>
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setIsDeviceModalOpen(false)}
                >
                  Anulează
                </button>
                <button type="submit" className="btn btn-primary">
                  {deviceModalMode === 'create' ? 'Creează Dispozitiv' : 'Salvează Modificările'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* ========================================================
          MODAL CONFIRMARE ȘTERGERE
         ======================================================== */}
      {isDeleteModalOpen && deleteTarget && (
        <div className="modal-backdrop" onClick={() => setIsDeleteModalOpen(false)}>
          <div className="modal-content" style={{ maxWidth: '440px' }} onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3 style={{ color: '#f87171' }}>⚠️ Confirmare Ștergere</h3>
              <button className="modal-close-btn" onClick={() => setIsDeleteModalOpen(false)}>×</button>
            </div>
            <div className="modal-body">
              <p style={{ color: '#e2e8f0', margin: '0 0 12px' }}>
                Ești sigur că vrei să ștergi {deleteTarget.type === 'user' ? 'utilizatorul' : 'dispozitivul'}:
              </p>
              <div style={{ background: '#0f172a', padding: '10px 14px', borderRadius: '8px', border: '1px solid #334155' }}>
                <strong style={{ color: '#ffffff' }}>{deleteTarget.name}</strong>
                <div className="id-badge" style={{ display: 'block', marginTop: '6px', maxWidth: '100%' }}>
                  ID: {deleteTarget.id}
                </div>
              </div>
              {deleteTarget.type === 'user' && (
                <p style={{ color: '#fbbf24', fontSize: '13px', marginTop: '12px', background: 'rgba(245, 158, 11, 0.1)', padding: '8px 12px', borderRadius: '6px', border: '1px solid rgba(245, 158, 11, 0.3)' }}>
                  ⚠️ Atenție: Ștergerea utilizatorului va declanșa sincronizarea în cascadă (ON DELETE CASCADE) și va șterge automat toate dispozitivele asociate acestuia!
                </p>
              )}
            </div>
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={() => setIsDeleteModalOpen(false)}
              >
                Anulează
              </button>
              <button
                type="button"
                className="btn btn-danger"
                onClick={handleConfirmDelete}
              >
                Confirmă Ștergerea
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;