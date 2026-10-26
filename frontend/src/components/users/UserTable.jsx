export default function UserTable({ users, onEdit, onDelete }) {
  if (users.length === 0) {
    return (
      <p className="text-center py-5" style={{ color: '#94a3b8' }}>
        Nu a fost gasit niciun utilizator.
      </p>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table table-hover align-middle mb-0" style={{ color: '#e2e8f0', borderColor: '#334155' }}>
        <thead style={{ backgroundColor: '#0f172a', color: '#94a3b8', fontSize: '11px', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
          <tr>
            <th>Nume</th>
            <th>Adresa</th>
            <th>Varsta</th>
            <th>ID Utilizator (UUID)</th>
            <th className="text-end">Actiuni</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id} style={{ borderColor: '#1e293b' }}>
              <td style={{ fontWeight: 600, color: '#f8fafc' }}>{user.name}</td>
              <td>{user.address || '—'}</td>
              <td>{user.age} ani</td>
              <td>
                <span className="id-badge" title={user.id}>{user.id}</span>
              </td>
              <td>
                <div className="action-buttons">
                  <button className="btn btn-sm btn-outline-primary" onClick={() => onEdit(user)}>
                    ✏️ Editeaza
                  </button>
                  <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(user)}>
                    🗑️ Sterge
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
