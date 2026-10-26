import { useState, useEffect, useCallback } from 'react';
import { getDevicesByUserId } from '../services/deviceService';
import { getUserById } from '../services/userService';

export default function ClientDevicesPage({ currentUser, activeTab = 'my-devices', onNotify }) {
  const [profile, setProfile] = useState(null);
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);

  const targetUserId = currentUser?.userId || currentUser?.id;

  const loadClientData = useCallback(async () => {
    if (!targetUserId) {
      setLoading(false);
      return;
    }

    setLoading(true);
    try {
      // 1. Preluare profil personal din user-service
      const profilePromise = getUserById(targetUserId).catch(() => null);
      // 2. Preluare dispozitive alocate din device-service
      const devicesPromise = getDevicesByUserId(targetUserId).catch(() => []);

      const [userProfile, userDevices] = await Promise.all([
        profilePromise,
        devicesPromise,
      ]);

      setProfile(userProfile);
      setDevices(Array.isArray(userDevices) ? userDevices : []);
    } catch (err) {
      if (onNotify) {
        onNotify('error', `Eroare la incarcarea datelor: ${err.message}`);
      }
    } finally {
      setLoading(false);
    }
  }, [targetUserId, onNotify]);

  useEffect(() => {
    loadClientData();
  }, [loadClientData]);

  const totalConsumption = devices.reduce(
    (sum, d) => sum + (Number(d.maxHourlyEnergyConsumption) || 0),
    0
  );

  return (
    <div className="container-fluid py-4 px-4">
      {/* ========================================================
          SECTIUNEA 1: PROFILUL MEU (Apare cand se apasa pe "Profil" in Navbar)
         ======================================================== */}
      {activeTab === 'profile' && (
        <>
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h4 className="m-0 fw-semibold text-white d-flex align-items-center gap-2">
              <span>👤</span>
              <span>Profilul Meu</span>
            </h4>
            <button
              className="btn btn-sm btn-outline-secondary"
              onClick={loadClientData}
              disabled={loading}
              title="Reimprospateaza datele de profil"
            >
              🔄 Reimprospateaza
            </button>
          </div>

          <div
            className="card shadow-sm p-4 mb-4"
            style={{
              background: '#1e293b',
              border: '1px solid #334155',
              borderRadius: '14px',
            }}
          >
            <div
              className="d-flex justify-content-between align-items-center mb-3 pb-2"
              style={{ borderBottom: '1px solid #334155' }}
            >
              <h5 className="m-0 text-white fw-semibold">
                Informatii Personale
              </h5>
              <span
                className="badge rounded-pill px-3 py-1 fw-semibold"
                style={{
                  background: 'rgba(245, 158, 11, 0.2)',
                  color: '#fbbf24',
                  border: '1px solid rgba(245, 158, 11, 0.4)',
                  fontSize: '12px',
                }}
              >
                👤 {currentUser?.role || 'CLIENT'}
              </span>
            </div>

            <div className="row g-3">
              <div className="col-12 col-md-4">
                <span className="text-secondary small text-uppercase d-block mb-1">
                  Nume Complet
                </span>
                <strong className="text-white fs-6">
                  {profile?.name || 'Client Demonstrativ'}
                </strong>
              </div>

              <div className="col-12 col-md-4">
                <span className="text-secondary small text-uppercase d-block mb-1">
                  Adresa de Domiciliu
                </span>
                <span className="text-light">
                  {profile?.address || 'Strada Memorandumului 28'}
                </span>
              </div>

              <div className="col-12 col-md-4">
                <span className="text-secondary small text-uppercase d-block mb-1">
                  Varsta
                </span>
                <span className="text-light">
                  {profile?.age ? `${profile.age} ani` : '25 ani'}
                </span>
              </div>

              <div className="col-12 col-md-4">
                <span className="text-secondary small text-uppercase d-block mb-1">
                  Nume Utilizator (Login)
                </span>
                <code className="text-info">
                  @{currentUser?.username || 'client'}
                </code>
              </div>

              <div className="col-12 col-md-8">
                <span className="text-secondary small text-uppercase d-block mb-1">
                  Identificator Unic Utilizator (UUID sincronizat)
                </span>
                <span className="id-badge" style={{ maxWidth: '100%' }} title={targetUserId}>
                  {targetUserId}
                </span>
              </div>
            </div>
          </div>
        </>
      )}

      {/* ========================================================
          SECTIUNEA 2: DISPOZITIVELE MELE (Apare cand se apasa pe "Dispozitivele Mele" in Navbar)
         ======================================================== */}
      {activeTab === 'my-devices' && (
        <>
          {/* Header cu buton Refresh */}
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h4 className="m-0 fw-semibold text-white d-flex align-items-center gap-2">
              <span>📟</span>
              <span>Dispozitivele Mele</span>
            </h4>
            <button
              className="btn btn-sm btn-outline-secondary"
              onClick={loadClientData}
              disabled={loading}
              title="Reimprospateaza datele dispozitivelor"
            >
              🔄 Reimprospateaza
            </button>
          </div>

          {/* Carduri Statistici Rapide pentru Consum */}
          <div className="row g-3 mb-4">
            <div className="col-12 col-sm-6">
              <div
                className="p-3 rounded-3 d-flex align-items-center gap-3"
                style={{ background: '#1e293b', border: '1px solid #334155' }}
              >
                <div
                  className="d-flex align-items-center justify-content-center rounded-3 fs-3"
                  style={{
                    width: 48,
                    height: 48,
                    background: 'rgba(56, 189, 248, 0.15)',
                    color: '#38bdf8',
                  }}
                >
                  📟
                </div>
                <div>
                  <div className="text-secondary small text-uppercase">
                    Dispozitive Alocate
                  </div>
                  <div className="fw-bold fs-4 text-white">{devices.length}</div>
                </div>
              </div>
            </div>

            <div className="col-12 col-sm-6">
              <div
                className="p-3 rounded-3 d-flex align-items-center gap-3"
                style={{ background: '#1e293b', border: '1px solid #334155' }}
              >
                <div
                  className="d-flex align-items-center justify-content-center rounded-3 fs-3"
                  style={{
                    width: 48,
                    height: 48,
                    background: 'rgba(16, 185, 129, 0.15)',
                    color: '#34d399',
                  }}
                >
                  ⚡
                </div>
                <div>
                  <div className="text-secondary small text-uppercase">
                    Consum Maxim Total Cumulat
                  </div>
                  <div className="fw-bold fs-4 text-success">
                    {totalConsumption.toFixed(2)} kWh
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Tabel Dispozitive Alocate (Strict Read-Only) */}
          <div
            className="rounded-3 overflow-hidden shadow-sm"
            style={{ border: '1px solid #334155', background: '#1e293b' }}
          >
            <div className="px-4 py-3" style={{ borderBottom: '1px solid #334155' }}>
              <h5 className="m-0 text-white fw-semibold">
                Lista Contoare Inteligente Alocate
              </h5>
            </div>

            {loading ? (
              <div className="text-center py-5 text-secondary">
                <div className="loading-spinner mb-3" />
                <p>Se incarca dispozitivele dumneavoastra...</p>
              </div>
            ) : devices.length === 0 ? (
              <div className="text-center py-5 px-3">
                <div className="fs-1 mb-2">ℹ️</div>
                <h5 className="text-white mb-2">Niciun dispozitiv alocat</h5>
                <p className="text-secondary mb-0" style={{ maxWidth: '480px', margin: '0 auto' }}>
                  Nu aveti niciun contor inteligent alocat contului dumneavoastra.
                  Contactati un administrator pentru a va asigna un dispozitiv energetic.
                </p>
              </div>
            ) : (
              <div className="table-responsive">
                <table
                  className="table table-hover align-middle mb-0"
                  style={{ color: '#e2e8f0', borderColor: '#334155' }}
                >
                  <thead
                    style={{
                      backgroundColor: '#0f172a',
                      color: '#94a3b8',
                      fontSize: '11px',
                      textTransform: 'uppercase',
                      letterSpacing: '0.5px',
                    }}
                  >
                    <tr>
                      <th>Descriere Contor</th>
                      <th>Adresa Instalare</th>
                      <th>Consum Maxim Orar (kWh)</th>
                      <th>ID Dispozitiv (UUID)</th>
                    </tr>
                  </thead>
                  <tbody>
                    {devices.map((device) => (
                      <tr key={device.id} style={{ borderColor: '#1e293b' }}>
                        <td style={{ fontWeight: 600, color: '#000000' }}>
                          {device.description}
                        </td>
                        <td>{device.address || '—'}</td>
                        <td>
                          <span style={{ color: '#38bdf8', fontWeight: 600 }}>
                            {device.maxHourlyEnergyConsumption} kWh
                          </span>
                        </td>
                        <td>
                          <span className="id-badge" title={device.id}>
                            {device.id}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}
