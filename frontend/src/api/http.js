const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:9000'

export async function request(path, options = {}) {
  const token = localStorage.getItem('accessToken')
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {})
    }
  })
  const payload = await response.json()
  if (!payload.success) {
    throw new Error(payload.message || payload.code || '请求失败')
  }
  return payload.data
}

export function post(path, body) {
  return request(path, { method: 'POST', body: JSON.stringify(body || {}) })
}

export function put(path, body) {
  return request(path, { method: 'PUT', body: JSON.stringify(body || {}) })
}
