import { useState, useEffect, useCallback } from 'react';
import './App.css';
import Navbar from './components/Navbar';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import UsersPage from './pages/UsersPage';
import DevicesPage from './pages/DevicesPage';
import ClientDevicesPage from './pages/ClientDevicesPage';
import { getCurrentUser, logout } from './services/authService';
import { getUsers } from './services/userService';
import { getDevices } from './services/deviceService';

function App() {
  const [currentUser, setCurrentUser] = useState(() => getCurrentUser());
  const [activePage, setActivePage] = useState(() => {
    const user = getCurrentUser();
    if (!user) return 'login';
    return user.role === 'ADMIN' ? 'home' : 'my-devices';
  });

  const [users, setUsers] = useState([]);
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState(null);

  const notify = useCallback((type, message) => {
    setNotification({ type, message });
    setTimeout(() => setNotification(null), 4500);
  }, []);

  const fetchUsers = useCallback(async () => {
    try {
      setUsers(await getUsers());
    } catch (err) {
      notify('error', `Eroare User Microservice: ${err.message}`);
    }
  }, [notify]);

  const fetchDevices = useCallback(async () => {
    try {
      setDevices(await getDevices());
    } catch (err) {
      notify('error', `Eroare Device Microservice: ${err.message}`);
    }
  }, [notify]);

  const loadAdminData = useCallback(async () => {
    setLoading(true);
    await Promise.all([fetchUsers(), fetchDevices()]);
    setLoading(false);
  }, [fetchUsers, fetchDevices]);

  // Incarca datele de administrare doar daca utilizatorul curent este ADMIN
  useEffect(() => {
    if (currentUser?.role === 'ADMIN') {
      loadAdminData();
    }
  }, [currentUser, loadAdminData]);

  const handleLoginSuccess = (user) => {
    setCurrentUser(user);
    if (user.role === 'ADMIN') {
      setActivePage('home');
      notify('success', `Bine ai venit, ${user.username}! Conectat ca Administrator.`);
    } else {
      setActivePage('my-devices');
      notify('success', `Bine ai venit, ${user.username}! Conectat ca Client.`);
    }
  };

  const handleLogout = () => {
    logout();
    setCurrentUser(null);
    setUsers([]);
    setDevices([]);
    setActivePage('login');
    notify('success', 'Te-ai deconectat cu succes.');
  };

  const cascadeRefresh = () => Promise.all([fetchUsers(), fetchDevices()]);

  // 1. Daca utilizatorul nu este autentificat -> afiseaza doar LoginPage
  if (!currentUser) {
    return (
      <>
        <Navbar currentUser={null} />
        {notification && (
          <div
            className={`alert alert-${notification.type === 'success' ? 'success' : 'danger'} d-flex justify-content-between align-items-center mx-4 mt-3`}
            style={{ fontSize: '14px' }}
          >
            <span>{notification.message}</span>
            <button type="button" className="btn-close" onClick={() => setNotification(null)} />
          </div>
        )}
        <LoginPage onLoginSuccess={handleLoginSuccess} />
      </>
    );
  }

  // 2. Daca utilizatorul este CLIENT -> afiseaza exclusiv pagina sa de dispozitive
  if (currentUser.role === 'CLIENT') {
    return (
      <>
        <Navbar
          activePage={activePage}
          onNavigate={setActivePage}
          currentUser={currentUser}
          onLogout={handleLogout}
        />
        {notification && (
          <div
            className={`alert alert-${notification.type === 'success' ? 'success' : 'danger'} d-flex justify-content-between align-items-center mx-4 mt-3`}
            style={{ fontSize: '14px' }}
          >
            <span>{notification.message}</span>
            <button type="button" className="btn-close" onClick={() => setNotification(null)} />
          </div>
        )}
        <ClientDevicesPage currentUser={currentUser} activeTab={activePage} onNotify={notify} />
      </>
    );
  }

  // 3. Daca utilizatorul este ADMIN -> acces complet la paginile de gestiune
  return (
    <>
      <Navbar
        activePage={activePage}
        onNavigate={setActivePage}
        onRefresh={loadAdminData}
        currentUser={currentUser}
        onLogout={handleLogout}
      />

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
          {activePage === 'home' && <HomePage users={users} devices={devices} />}
          {activePage === 'users' && (
            <UsersPage
              users={users}
              onUsersChange={fetchUsers}
              onCascadeRefresh={cascadeRefresh}
              onNotify={notify}
            />
          )}
          {activePage === 'devices' && (
            <DevicesPage
              devices={devices}
              users={users}
              onDevicesChange={fetchDevices}
              onNotify={notify}
            />
          )}
        </>
      )}
    </>
  );
}

export default App;