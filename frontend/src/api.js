import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' },
});

export const cubixAPI = {
  health: () => api.get('/health'),
  scramble: (length = 20) => api.get(`/scramble?length=${length}`),
  solve: (scramble) => api.post('/solve', { scramble }),
};