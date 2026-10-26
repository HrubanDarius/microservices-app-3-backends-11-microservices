import { useState, useEffect, useCallback } from 'react';
import './App.css';
import Navbar from './components/Navbar';
import HomePage from './pages/HomePage';
import UsersPage from './pages/UsersPage';
import DevicesPage from './pages/DevicesPage';
import { getUsers } from './services/userService';
import { getDevices } from './services/deviceService';

function App() {
  const [activePage, setActivePage] = useState('home');
  const [users, setUsers] = useState([]);
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notification, setNotification] = useState(null);

  const notify = (type, message) => {
    setNotification({ type, message });
    setTimeout(() => setNotification(null), 4500);
  };

  const fetchUsers = useCallback(async () => {
    try { setUsers(await getUsers()); }
    catch (err) { notify('error', `Eroare User Microservice: ${err.message}`); }
  }, []);

  const fetchDevices = useCallback(async () => {
    try { setDevices(await getDevices()); }
    catch (err) { notify('error', `Eroare Device Microservice: ${err.message}`); }
  }, []);

  const loadAll = useCallback(async () => {
    setLoading(true);
    await Promise.all([fetchUsers(), fetchDevices()]);
    setLoading(false);
  }, [fetchUsers, fetchDevices]);

  useEffect(() => { loadAll(); }, [loadAll]);

  const cascadeRefresh = () => Promise.all([fetchUsers(), fetchDevices()]);

  return (
    <>
      <Navbar activePage={activePage} onNavigate={setActivePage} onRefresh={loadAll} />

      {notification && (
        <div
          className={`alert alert-${notification.type === 'success' ? 'success' : 'danger'} d-flex justify-content-between align-items-center mx-4 mt-3`}
          style={{ fontSize: '14px' }}
        >
          <span>{notification.message}</span>
          <button type="button" className="btn-close" onClick={() => setNotification(null)} />
        </div>
      )}

      {loading ? (
        <div className="d-flex flex-column align-items-center justify-content-center py-5" style={{ color: '#94a3b8' }}>
          <div className="loading-spinner mb-3" />
          <p>Se incarca datele din microservicii...</p>
        </div>
      ) : (
        <>
          {activePage === 'home'    && <HomePage users={users} devices={devices} />}
          {activePage === 'users'   && <UsersPage users={users} onUsersChange={fetchUsers} onCascadeRefresh={cascadeRefresh} onNotify={notify} />}
          {activePage === 'devices' && <DevicesPage devices={devices} users={users} onDevicesChange={fetchDevices} onNotify={notify} />}
        </>
      )}
    </>
  );
}

export default App;