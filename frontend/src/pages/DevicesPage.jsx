import { useState } from 'react';
import DeviceTable from '../components/devices/DeviceTable';
import DeviceModal from '../components/devices/DeviceModal';
import { createDevice, updateDevice, deleteDevice } from '../services/deviceService';

export default function DevicesPage({ devices, users, onDevicesChange, onNotify }) {
  const [search, setSearch] = useState('');
  const [modalShow, setModalShow] = useState(false);
  const [modalMode, setModalMode] = useState('create');
  const [editTarget, setEditTarget] = useState(null);
  const [deleteTarget, setDeleteTarget] = useState(null);

  const filtered = devices.filter((d) =>
    [d.description, d.address, d.id].some((v) => (v || '').toLowerCase().includes(search.toLowerCase()))
  );

  // ---------- Modal handlers ----------
  const openAdd = () => { setEditTarget(null); setModalMode('create'); setModalShow(true); };
  const openEdit = (device) => { setEditTarget(device); setModalMode('edit'); setModalShow(true); };
  const closeModal = () => setModalShow(false);

  const handleSubmit = async (formData) => {
    try {
      if (modalMode === 'create') {
        await createDevice(formData);
        onNotify('success', 'Dispozitivul a fost salvat cu succes!');
      } else {
        await updateDevice(editTarget.id, formData);
        onNotify('success', 'Dispozitivul a fost actualizat cu succes!');
      }
      closeModal();
      onDevicesChange();
    } catch (err) {
      onNotify('error', `Operație eșuată: ${err.message}`);
    }
  };

  // ---------- Delete handlers ----------
  const openDelete = (device) => setDeleteTarget(device);
  const closeDelete = () => setDeleteTarget(null);

  const confirmDelete = async () => {
    try {
      await deleteDevice(deleteTarget.id);
      onNotify('success', `Dispozitivul "${deleteTarget.description}" a fost șters.`);
      closeDelete();
      onDevicesChange();
    } catch (err) {
      onNotify('error', `Ștergere eșuată: ${err.message}`);
    }
  };

  return (
    <div className="container-fluid py-4 px-4">
      {/* Panel header */}
      <div className="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
        <div className="d-flex align-items-center gap-2">
          <h4 className="m-0 fw-semibold" style={{ color: '#f8fafc' }}>📟 Gestiune Dispozitive Energetice</h4>
          <span className="badge" style={{ background: 'rgba(168,85,247,0.15)', color: '#c084fc', border: '1px solid rgba(168,85,247,0.3)' }}>
            {devices.length} înregistrate
          </span>
        </div>
        <div className="d-flex gap-2">
          <input
            type="text"
            className="form-control form-control-dark"
            style={{ width: '220px' }}
            placeholder="Caută dispozitiv..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button className="btn btn-primary" onClick={openAdd}>➕ Adaugă Dispozitiv</button>
        </div>
      </div>

      {/* Table */}
      <div className="rounded-3 overflow-hidden" style={{ border: '1px solid #334155', background: '#1e293b' }}>
        <DeviceTable devices={filtered} users={users} onEdit={openEdit} onDelete={openDelete} />
      </div>

      {/* Create / Edit modal */}
      <DeviceModal show={modalShow} mode={modalMode} initialData={editTarget} users={users} onSubmit={handleSubmit} onClose={closeModal} />

      {/* Delete confirm modal */}
      {deleteTarget && (
        <div className="modal-backdrop-custom" onClick={closeDelete}>
          <div
            className="shadow-lg"
            style={{ background: '#1e293b', border: '1px solid #334155', borderRadius: '14px', width: '100%', maxWidth: '440px', overflow: 'hidden' }}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="d-flex justify-content-between align-items-center px-4 py-3" style={{ borderBottom: '1px solid #334155' }}>
              <h5 className="m-0" style={{ color: '#f87171' }}>⚠️ Confirmare Ștergere</h5>
              <button type="button" className="btn-close btn-close-white" onClick={closeDelete} />
            </div>
            <div className="px-4 py-3">
              <p style={{ color: '#e2e8f0' }}>Ești sigur că vrei să ștergi dispozitivul:</p>
              <div className="p-3 rounded-2" style={{ background: '#0f172a', border: '1px solid #334155' }}>
                <strong style={{ color: '#ffffff' }}>{deleteTarget.description}</strong>
                <div className="id-badge mt-2" style={{ display: 'block', maxWidth: '100%' }}>{deleteTarget.id}</div>
              </div>
            </div>
            <div className="d-flex justify-content-end gap-2 px-4 py-3" style={{ background: '#0f172a', borderTop: '1px solid #334155' }}>
              <button className="btn btn-secondary" onClick={closeDelete}>Anulează</button>
              <button className="btn btn-danger" onClick={confirmDelete}>Confirmă Ștergerea</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
