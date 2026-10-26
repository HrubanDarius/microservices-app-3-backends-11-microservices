export default function HomePage({ users, devices }) {
  const assigned = devices.filter((d) => d.userId).length;
  const unassigned = devices.length - assigned;

  const cards = [
    { icon: '👥', label: 'Total Utilizatori',     value: users.length,   colorClass: 'text-info',    bg: 'rgba(59,130,246,0.1)'  },
    { icon: '📟', label: 'Total Dispozitive',     value: devices.length, colorClass: 'text-purple',  bg: 'rgba(168,85,247,0.1)'  },
    { icon: '🔗', label: 'Dispozitive Alocate',   value: assigned,       colorClass: 'text-success', bg: 'rgba(16,185,129,0.1)'  },
    { icon: '⚠️', label: 'Dispozitive Nealocate', value: unassigned,     colorClass: 'text-warning', bg: 'rgba(245,158,11,0.1)'  },
  ];

  return (
    <div className="container-fluid py-4 px-4">
      <h4 className="mb-4 fw-semibold" style={{ color: '#f8fafc' }}>📊 Vedere Generala</h4>

      <div className="row g-3">
        {cards.map(({ icon, label, value, colorClass, bg }) => (
          <div className="col-12 col-sm-6 col-xl-3" key={label}>
            <div
              className="d-flex align-items-center gap-3 p-3 rounded-3"
              style={{ background: '#1e293b', border: '1px solid #334155', transition: 'border-color 0.2s' }}
            >
              <div
                className="d-flex align-items-center justify-content-center rounded-3 fs-4 flex-shrink-0"
                style={{ width: 52, height: 52, background: bg }}
              >
                {icon}
              </div>
              <div>
                <div style={{ fontSize: '12px', color: '#94a3b8', textTransform: 'uppercase', letterSpacing: '0.5px' }}>{label}</div>
                <div className={`fw-bold fs-3 ${colorClass}`}>{value}</div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
