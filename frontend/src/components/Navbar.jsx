export default function Navbar({
  activePage,
  onNavigate,
  onRefresh,
  currentUser,
  onLogout,
}) {
  const isAdmin = currentUser?.role === 'ADMIN';
  const isClient = currentUser?.role === 'CLIENT';

  const adminNavItems = [
    { key: 'home', label: '📊 Acasa' },
    { key: 'users', label: '👥 Utilizatori' },
    { key: 'devices', label: '📟 Dispozitive' },
  ];

  const clientNavItems = [
    { key: 'my-devices', label: '📟 Dispozitivele Mele' },
    { key: 'profile', label: '👤 Profil' },
  ];

  const currentNavItems = isAdmin
    ? adminNavItems
    : isClient
    ? clientNavItems
    : [];

  return (
    <nav
      className="navbar navbar-expand-lg navbar-dark"
      style={{
        backgroundColor: '#1e293b',
        borderBottom: '1px solid #334155',
      }}
    >
      <div className="container-fluid px-4">
        {/* Brand / Logo */}
        <span
          className="navbar-brand fw-bold d-flex align-items-center gap-2"
          style={{ color: '#60a5fa', cursor: 'pointer' }}
          onClick={() => {
            if (isAdmin) onNavigate('home');
            else if (isClient) onNavigate('my-devices');
          }}
        >
          <span>⚡</span>
          <span>Energy Management System</span>
        </span>

        {currentUser && (
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#mainNav"
          >
            <span className="navbar-toggler-icon" />
          </button>
        )}

        {currentUser && (
          <div className="collapse navbar-collapse" id="mainNav">
            {/* Nav Links bazate pe rol */}
            <ul className="navbar-nav me-auto">
              {currentNavItems.map(({ key, label }) => (
                <li className="nav-item" key={key}>
                  <button
                    className={`nav-link btn btn-link text-decoration-none ${
                      activePage === key ? 'active fw-semibold' : ''
                    }`}
                    style={{
                      color: activePage === key ? '#60a5fa' : '#94a3b8',
                    }}
                    onClick={() => onNavigate(key)}
                  >
                    {label}
                  </button>
                </li>
              ))}
            </ul>

            {/* Zona Utilizator & Actiuni Dreapta */}
            <div className="d-flex align-items-center gap-3 flex-wrap">
              {/* Badge Rol */}
              {isAdmin ? (
                <span
                  className="badge rounded-pill px-3 py-1 fw-semibold"
                  style={{
                    background: 'rgba(59, 130, 246, 0.2)',
                    color: '#60a5fa',
                    border: '1px solid rgba(59, 130, 246, 0.4)',
                    fontSize: '12px',
                  }}
                >
                  👑 ADMIN
                </span>
              ) : (
                <span
                  className="badge rounded-pill px-3 py-1 fw-semibold"
                  style={{
                    background: 'rgba(245, 158, 11, 0.2)',
                    color: '#fbbf24',
                    border: '1px solid rgba(245, 158, 11, 0.4)',
                    fontSize: '12px',
                  }}
                >
                  👤 CLIENT
                </span>
              )}

              {/* Nume Utilizator */}
              <span className="text-light small fw-semibold">
                {currentUser.username}
              </span>

              {/* Buton Refresh (doar pentru Admin la panoul general) */}
              {isAdmin && onRefresh && (
                <button
                  className="btn btn-sm btn-outline-secondary"
                  onClick={onRefresh}
                  title="Reimprospateaza datele"
                >
                  🔄
                </button>
              )}

              {/* Buton Deconectare */}
              <button
                className="btn btn-sm btn-outline-danger d-flex align-items-center gap-1"
                onClick={onLogout}
                title="Deconectare din cont"
              >
                <span>🚪</span>
                <span>Deconectare</span>
              </button>
            </div>
          </div>
        )}
      </div>
    </nav>
  );
}
