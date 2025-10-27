// 🔗 API Client for Backend Communication

// API Base URL
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8000/v1';

// API Client class
class ApiClient {
    constructor() {
        this.baseURL = API_BASE_URL;
        this.token = null;
        
        // Load token from localStorage if available
        if (typeof window !== 'undefined') {
            this.token = localStorage.getItem('accessToken');
        }
    }

    // Set authentication token
    setToken(token) {
        this.token = token;
        if (typeof window !== 'undefined') {
            if (token) {
                localStorage.setItem('accessToken', token);
            } else {
                localStorage.removeItem('accessToken');
            }
        }
    }

    // Get authentication headers
    getHeaders() {
        const headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        return headers;
    }

    // Make HTTP request
    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        
        const config = {
            headers: this.getHeaders(),
            ...options
        };

        try {
            console.log(`📡 API Request: ${options.method || 'GET'} ${url}`);
            
            const response = await fetch(url, config);
            
            // Handle response
            const data = await response.json();
            
            if (!response.ok) {
                console.error('❌ API Error:', data);
                throw new ApiError(data.message || 'API request failed', response.status, data);
            }

            console.log('✅ API Success:', data);
            return data;

        } catch (error) {
            console.error('❌ Network Error:', error);
            
            if (error instanceof ApiError) {
                throw error;
            }
            
            throw new ApiError('Network error occurred', 0, { error: error.message });
        }
    }

    // GET request
    async get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    }

    // POST request
    async post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    // PUT request
    async put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    // DELETE request
    async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }

    // Authentication methods
    async register(userData) {
        try {
            const response = await this.post('/auth/register', userData);
            
            if (response.success && response.data.tokens) {
                this.setToken(response.data.tokens.accessToken);
                
                // Store refresh token
                if (typeof window !== 'undefined') {
                    localStorage.setItem('refreshToken', response.data.tokens.refreshToken);
                }
            }
            
            return response;
        } catch (error) {
            throw error;
        }
    }

    async login(credentials) {
        try {
            const response = await this.post('/auth/login', credentials);
            
            if (response.success && response.data.tokens) {
                this.setToken(response.data.tokens.accessToken);
                
                // Store refresh token
                if (typeof window !== 'undefined') {
                    localStorage.setItem('refreshToken', response.data.tokens.refreshToken);
                }
            }
            
            return response;
        } catch (error) {
            throw error;
        }
    }

    async logout() {
        try {
            // Call logout endpoint
            await this.post('/auth/logout');
        } catch (error) {
            console.warn('Logout endpoint failed:', error);
        } finally {
            // Clear tokens regardless
            this.setToken(null);
            if (typeof window !== 'undefined') {
                localStorage.removeItem('refreshToken');
            }
        }
    }

    async refreshToken() {
        try {
            const refreshToken = typeof window !== 'undefined' 
                ? localStorage.getItem('refreshToken') 
                : null;
            
            if (!refreshToken) {
                throw new Error('No refresh token available');
            }

            const response = await this.post('/auth/refresh', { refreshToken });
            
            if (response.success && response.data.accessToken) {
                this.setToken(response.data.accessToken);
                return response.data.accessToken;
            }
            
            throw new Error('Failed to refresh token');
        } catch (error) {
            // Clear tokens on refresh failure
            this.setToken(null);
            if (typeof window !== 'undefined') {
                localStorage.removeItem('refreshToken');
            }
            throw error;
        }
    }

    async getProfile() {
        return this.get('/auth/profile');
    }

    // Health check
    async healthCheck() {
        return this.get('/health');
    }
}

// Custom API Error class
class ApiError extends Error {
    constructor(message, status, data) {
        super(message);
        this.name = 'ApiError';
        this.status = status;
        this.data = data;
    }
}

// Create and export API client instance
const apiClient = new ApiClient();

export default apiClient;
export { ApiError };
