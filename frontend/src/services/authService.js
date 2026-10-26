const BASE = '/auth-service/auth';
const USER_KEY = 'auth_user';

async function handleResponse(res) {
  if (!res.ok) {
    let errorMsg = `HTTP ${res.status}`;
    try {
      const data = await res.json();
      errorMsg = data.message || data.error || errorMsg;
    } catch {
      const text = await res.text().catch(() => '');
      if (text) errorMsg = text;
    }
    throw new Error(errorMsg);
  }
  return res.json();
}

/**
 * Autentifica utilizatorul trimitand credentialele catre auth-service.
 * La succes, salveaza token-ul si datele utilizatorului in localStorage.
 */
export async function login(username, password) {
  const res = await fetch(`${BASE}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });

  const data = await handleResponse(res);
  const user = {
    token: data.token,
    id: data.id,
    userId: data.id,
    username: data.username,
    role: data.role,
  };

  localStorage.setItem(USER_KEY, JSON.stringify(user));
  return user;
}

/**
 * Inregistreaza un nou utilizator in auth-service.
 * Primeste { id, username, password, role }.
 */
export async function register(data) {
  const res = await fetch(`${BASE}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  return handleResponse(res);
}

/**
 * Sterge datele de sesiune din localStorage la deconectare.
 */
export function logout() {
  localStorage.removeItem(USER_KEY);
}

/**
 * Returneaza obiectul utilizatorului conectat sau null daca nu exista sesiune.
 */
export function getCurrentUser() {
  try {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

/**
 * Verifica daca exista un utilizator autentificat cu token valid.
 */
export function isAuthenticated() {
  const user = getCurrentUser();
  return Boolean(user && user.token);
}

/**
 * Header-e HTTP standard pentru cereri autorizate catre microservicii.
 */
export function getAuthHeaders() {
  const user = getCurrentUser();
  const headers = { 'Content-Type': 'application/json' };
  if (user && user.token) {
    headers['Authorization'] = `Bearer ${user.token}`;
  }
  return headers;
}
