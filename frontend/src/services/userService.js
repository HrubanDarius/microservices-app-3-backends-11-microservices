const BASE = '/user-service/person';

async function handleResponse(res) {
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(text || `HTTP ${res.status}`);
  }
  // 204 No Content → return null
  if (res.status === 204) return null;
  return res.json();
}

export async function getUsers() {
  const res = await fetch(BASE);
  return handleResponse(res);
}

export async function getUserById(id) {
  const res = await fetch(`${BASE}/${id}`);
  return handleResponse(res);
}

export async function createUser(data) {
  const res = await fetch(BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  return handleResponse(res);
}

export async function updateUser(id, data) {
  const res = await fetch(`${BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id, ...data }),
  });
  return handleResponse(res);
}

export async function deleteUser(id) {
  const res = await fetch(`${BASE}/${id}`, { method: 'DELETE' });
  return handleResponse(res);
}
