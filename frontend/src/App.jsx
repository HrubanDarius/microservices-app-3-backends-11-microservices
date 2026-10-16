import { useState, useEffect } from 'react';

function App() {
    const [devices, setDevices] = useState([]);
    const [devicesError, setDevicesError] = useState(null);
    const [devicesLoading, setDevicesLoading] = useState(true);

    const [users, setUsers] = useState([]);
    const [usersError, setUsersError] = useState(null);
    const [usersLoading, setUsersLoading] = useState(true);

    useEffect(() => {
        // 1. Apel catre Device prin Traefik (fara port, cale relativa)
        fetch('/device-service/device')
            .then((res) => {
                if (!res.ok) throw new Error(`Status ${res.status}`);
                return res.json();
            })
            .then((data) => {
                setDevices(data);
                setDevicesLoading(false);
            })
            .catch((err) => {
                setDevicesError(err.message);
                setDevicesLoading(false);
            });

        // 2. Apel catre User prin Traefik (fara port, cale relativa)
        fetch('/user-service/person')
            .then((res) => {
                if (!res.ok) throw new Error(`Status ${res.status}`);
                return res.json();
            })
            .then((data) => {
                setUsers(data);
                setUsersLoading(false);
            })
            .catch((err) => {
                setUsersError(err.message);
                setUsersLoading(false);
            });
    }, []);

    return (
        <div style={{ padding: '15px', fontFamily: 'sans-serif', background: '#121212', minHeight: '100vh', color: '#fff' }}>
            <div style={{textAlign: 'center', marginBottom: '35px'}}>
                <h1 style={{fontSize: '28px', lineHeight: '1.4', margin: '0 0 8px 0', color: '#ffffff'}}>
                    Status Conexiuni Microservicii
                </h1>
                <p style={{fontSize: '16px', color: '#61dafb', margin: 0, fontWeight: '500'}}>
                    prin Traefik Reverse Proxy & Load Balancer
                </p>
            </div>

            <div style={{
                display: 'grid',
                gridTemplateColumns: '1fr 1fr',
                gap: '20px',
                maxWidth: '1200px',
                margin: '0 auto'
            }}>

                {/* Coloana 1: User Microservice */}
                <div style={{background: '#1e1e1e', padding: '20px', borderRadius: '8px', border: '1px solid #333'}}>
                    <h2 style={{color: '#61dafb', borderBottom: '1px solid #333', paddingBottom: '10px' }}>
                        User Microservice
                    </h2>
                    <p style={{ color: '#aaa', fontSize: '14px' }}>Rutat prin Traefik: /user-service</p>

                    {usersLoading && <p>Se incarca utilizatorii...</p>}
                    {usersError && (
                        <div style={{ color: '#ff6b6b', background: '#2c1515', padding: '10px', borderRadius: '4px' }}>
                            Eroare conexiune: {usersError}
                        </div>
                    )}
                    {!usersLoading && !usersError && (
                        <div>
                            <p style={{ color: '#4caf50', fontWeight: 'bold' }}>
                                Conectat! {users.length} utilizatori gasiti.
                            </p>
                            <pre style={{ background: '#2b2b2b', padding: '15px', borderRadius: '5px', overflowX: 'auto', maxHeight: '400px' }}>
                {JSON.stringify(users, null, 2)}
              </pre>
                        </div>
                    )}
                </div>

                {/* Coloana 2: Device Microservice */}
                <div style={{ background: '#1e1e1e', padding: '20px', borderRadius: '8px', border: '1px solid #333' }}>
                    <h2 style={{ color: '#61dafb', borderBottom: '1px solid #333', paddingBottom: '10px' }}>
                        Device Microservice
                    </h2>
                    <p style={{ color: '#aaa', fontSize: '14px' }}>Rutat prin Traefik: /device-service</p>

                    {devicesLoading && <p>Se incarca dispozitivele...</p>}
                    {devicesError && (
                        <div style={{ color: '#ff6b6b', background: '#2c1515', padding: '10px', borderRadius: '4px' }}>
                            Eroare conexiune: {devicesError}
                        </div>
                    )}
                    {!devicesLoading && !devicesError && (
                        <div>
                            <p style={{ color: '#4caf50', fontWeight: 'bold' }}>
                                Conectat! {devices.length} dispozitive gasite.
                            </p>
                            <pre style={{ background: '#2b2b2b', padding: '15px', borderRadius: '5px', overflowX: 'auto', maxHeight: '400px' }}>
                {JSON.stringify(devices, null, 2)}
              </pre>
                        </div>
                    )}
                </div>

            </div>
        </div>
    );
}

export default App;