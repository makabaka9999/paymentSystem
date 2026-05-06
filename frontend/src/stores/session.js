import { defineStore } from 'pinia'
import { post } from '../api/http'

export const useSessionStore = defineStore('session', {
  state: () => ({
    accessToken: localStorage.getItem('accessToken') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null')
  }),
  actions: {
    async login(username, password) {
      const session = await post('/api/auth/login', { username, password })
      this.accessToken = session.accessToken
      this.user = { userId: session.userId, username: session.username, role: session.role }
      localStorage.setItem('accessToken', session.accessToken)
      localStorage.setItem('user', JSON.stringify(this.user))
    },
    logout() {
      this.accessToken = ''
      this.user = null
      localStorage.removeItem('accessToken')
      localStorage.removeItem('user')
    }
  }
})
