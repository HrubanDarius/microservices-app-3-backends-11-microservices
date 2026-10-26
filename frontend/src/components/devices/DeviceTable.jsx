export default function DeviceTable({ devices, users, onEdit, onDelete }) {
  const getUserName = (userId) => {
    if (!userId) return null;
    const u = users.find((u) => u.id === userId);
    return u ? u.name : null;
  };

  if (devices.length === 0) {
    return (
      <p className="text-center py-5" style={{ color: '#94a3b8' }}>
        Nu a fost găsit niciun dispozitiv energetic.
      </p>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table table-hover align-middle mb-0" style={{ color: '#e2e8f0', borderColor: '#334155' }}>
        <thead style={{ backgroundColor: '#0f172a', color: '#94a3b8', fontSize: '11px', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
          <tr>
            <th>Descriere</th>
            <th>Adresă Instalare</th>
            <th>Consum Maxim (kWh)</th>
            <th>Utilizator Asociat</th>
            <th>ID Dispozitiv (UUID)</th>
            <th className="text-end">Acțiuni</th>
          </tr>
        </thead>
        <tbody>
          {devices.map((device) => {
            const userName = getUserName(device.userId);
            return (
              <tr key={device.id} style={{ borderColor: '#1e293b' }}>
                <td style={{ fontWeight: 600, color: '#f8fafc' }}>{device.description}</td>
                <td>{device.address || '—'}</td>
                <td>
                  <span style={{ color: '#38bdf8', fontWeight: 600 }}>
                    {device.maxHourlyEnergyConsumption} kWh
                  </span>
                </td>
                <td>
                  {device.userId ? (
                    <span className="user-badge" title={`UUID: ${device.userId}`}>
                      👤 {userName ?? `User: ${device.userId.substring(0, 8)}...`}
                    </span>
                  ) : (
                    <span className="unassigned-badge">Neasignat</span>
                  )}
                </td>
                <td>
                  <span className="id-badge" title={device.id}>{device.id}</span>
                </td>
                <td>
                  <div className="action-buttons">
                    <button className="btn btn-sm btn-outline-primary" onClick={() => onEdit(device)}>
                      ✏️ Editează
                    </button>
                    <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(device)}>
                      🗑️ Șterge
                    </button>
                  </div>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
