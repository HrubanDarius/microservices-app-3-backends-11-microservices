import { getAuthHeaders } from './authService';

const BASE = '/device-service/device';

async function handleResponse(res) {
  if (!res.ok) {
    if (res.status === 401) {
      throw new Error('Sesiune expirata sau neautorizata (HTTP 401). Te rugam sa te reconectezi.');
    }
    const text = await res.text().catch(() => '');
    throw new Error(text || `HTTP ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export async function getDevices() {
  const res = await fetch(BASE, {
    headers: getAuthHeaders(),
  });
  return handleResponse(res);
}

export async function getDevicesByUserId(userId) {
  const res = await fetch(`${BASE}/user/${userId}`, {
    headers: getAuthHeaders(),
  });
  return handleResponse(res);
}

export async function createDevice(data) {
  const res = await fetch(BASE, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(data),
  });
  return handleResponse(res);
}

export async function updateDevice(id, data) {
  const res = await fetch(`${BASE}/${id}`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify({ id, ...data }),
  });
  return handleResponse(res);
}

export async function deleteDevice(id) {
  const res = await fetch(`${BASE}/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });
  return handleResponse(res);
}
