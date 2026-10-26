import { useState } from 'react';
import UserTable from '../components/users/UserTable';
import UserModal from '../components/users/UserModal';
import { createUser, updateUser, deleteUser } from '../services/userService';
import { register } from '../services/authService';

export default function UsersPage({ users, onUsersChange, onCascadeRefresh, onNotify }) {
  const [search, setSearch] = useState('');
  const [modalShow, setModalShow] = useState(false);
  const [modalMode, setModalMode] = useState('create');
  const [editTarget, setEditTarget] = useState(null);
  const [deleteTarget, setDeleteTarget] = useState(null);

  const filtered = users.filter((u) =>
    [u.name, u.address, u.id].some((v) => (v || '').toLowerCase().includes(search.toLowerCase()))
  );

  const openAdd = () => {
    setEditTarget(null);
    setModalMode('create');
    setModalShow(true);
  };

  const openEdit = (user) => {
    setEditTarget(user);
    setModalMode('edit');
    setModalShow(true);
  };

  const closeModal = () => setModalShow(false);

  const handleSubmit = async (formData) => {
    try {
      if (modalMode === 'create') {
        // 1. Creare profil in user-service (returneaza generatedUserId si declanseaza sincronizarea in device-service)
        const generatedUserId = await createUser({
          name: formData.name,
          address: formData.address,
          age: formData.age,
        });

        // 2. Creare cont de acces in auth-service cu acelasi ID
        if (formData.username && formData.password) {
          await register({
            id: generatedUserId,
            username: formData.username,
            password: formData.password,
            role: formData.role || 'CLIENT',
          });
        }

        onNotify('success', 'Utilizatorul si contul de acces au fost create cu succes!');
      } else {
        await updateUser(editTarget.id, {
          name: formData.name,
          address: formData.address,
          age: formData.age,
        });
        onNotify('success', 'Utilizatorul a fost actualizat cu succes!');
      }
      closeModal();
      onUsersChange();
    } catch (err) {
      onNotify('error', `Operatie esuata: ${err.message}`);
    }
  };

  const openDelete = (user) => setDeleteTarget(user);
  const closeDelete = () => setDeleteTarget(null);

  const confirmDelete = async () => {
    try {
      await deleteUser(deleteTarget.id);
      onNotify('success', `Utilizatorul "${deleteTarget.name}" si dispozitivele asociate au fost sterse.`);
      closeDelete();
      onCascadeRefresh();
    } catch (err) {
      onNotify('error', `Stergere esuata: ${err.message}`);
    }
  };

  return (
    <div className="container-fluid py-4 px-4">
      <div className="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
        <div className="d-flex align-items-center gap-2">
          <h4 className="m-0 fw-semibold" style={{ color: '#f8fafc' }}>👥 Gestiune Utilizatori</h4>
          <span className="badge" style={{ background: 'rgba(96,165,250,0.15)', color: '#60a5fa', border: '1px solid rgba(96,165,250,0.3)' }}>
            {users.length} inregistrati
          </span>
        </div>
        <div className="d-flex gap-2">
          <input
            type="text"
            className="form-control form-control-dark"
            style={{ width: '220px' }}
            placeholder="Cauta utilizator..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button className="btn btn-primary" onClick={openAdd}>➕ Adauga Utilizator</button>
        </div>
      </div>

      <div className="rounded-3 overflow-hidden" style={{ border: '1px solid #334155', background: '#1e293b' }}>
        <UserTable users={filtered} onEdit={openEdit} onDelete={openDelete} />
      </div>

      <UserModal
        show={modalShow}
        mode={modalMode}
        initialData={editTarget}
        onSubmit={handleSubmit}
        onClose={closeModal}
      />

      {deleteTarget && (
        <div className="modal-backdrop-custom" onClick={closeDelete}>
          <div
            className="shadow-lg"
            style={{ background: '#1e293b', border: '1px solid #334155', borderRadius: '14px', width: '100%', maxWidth: '440px', overflow: 'hidden' }}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="d-flex justify-content-between align-items-center px-4 py-3" style={{ borderBottom: '1px solid #334155' }}>
              <h5 className="m-0" style={{ color: '#f87171' }}>⚠️ Confirmare Stergere</h5>
              <button type="button" className="btn-close btn-close-white" onClick={closeDelete} />
            </div>
            <div className="px-4 py-3">
              <p style={{ color: '#e2e8f0' }}>Esti sigur ca vrei sa stergi utilizatorul:</p>
              <div className="p-3 rounded-2" style={{ background: '#0f172a', border: '1px solid #334155' }}>
                <strong style={{ color: '#ffffff' }}>{deleteTarget.name}</strong>
                <div className="id-badge mt-2" style={{ display: 'block', maxWidth: '100%' }}>{deleteTarget.id}</div>
              </div>
              <div className="alert mt-3 py-2 px-3" style={{ background: 'rgba(245,158,11,0.1)', border: '1px solid rgba(245,158,11,0.3)', color: '#fbbf24', fontSize: '13px' }}>
                ⚠️ Atentie: Stergerea utilizatorului va declansa sincronizarea in cascada (ON DELETE CASCADE) si va sterge automat toate dispozitivele asociate!
              </div>
            </div>
            <div className="d-flex justify-content-end gap-2 px-4 py-3" style={{ background: '#0f172a', borderTop: '1px solid #334155' }}>
              <button className="btn btn-secondary" onClick={closeDelete}>Anuleaza</button>
              <button className="btn btn-danger" onClick={confirmDelete}>Confirma Stergerea</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
