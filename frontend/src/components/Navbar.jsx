export default function Navbar({ activePage, onNavigate, onRefresh }) {
  const navItems = [
    { key: 'home',    label: '📊 Acasa' },
    { key: 'users',   label: '👥 Utilizatori' },
    { key: 'devices', label: '📟 Dispozitive' },
  ];

  return (
    <nav className="navbar navbar-expand-lg navbar-dark" style={{ backgroundColor: '#1e293b', borderBottom: '1px solid #334155' }}>
      <div className="container-fluid px-4">
        <span className="navbar-brand fw-bold" style={{ color: '#60a5fa' }}>
          ⚡ Energy Management System
        </span>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#mainNav"
        >
          <span className="navbar-toggler-icon" />
        </button>
        <div className="collapse navbar-collapse" id="mainNav">
          <ul className="navbar-nav me-auto">
            {navItems.map(({ key, label }) => (
              <li className="nav-item" key={key}>
                <button
                  className={`nav-link btn btn-link text-decoration-none ${activePage === key ? 'active fw-semibold' : ''}`}
                  style={{ color: activePage === key ? '#60a5fa' : '#94a3b8' }}
                  onClick={() => onNavigate(key)}
                >
                  {label}
                </button>
              </li>
            ))}
          </ul>
          <div className="d-flex align-items-center gap-2">
            <span
              className="badge rounded-pill"
              style={{ background: 'rgba(56,189,248,0.15)', color: '#38bdf8', border: '1px solid rgba(56,189,248,0.3)', fontSize: '11px' }}
            >
              Online • 8 Containere
            </span>
            <button className="btn btn-sm btn-outline-secondary" onClick={onRefresh} title="Reimprospateaza datele">
              🔄 Refresh
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}
