// 🔐 Authentication Utilities

import apiClient from './api';

// Check if user is authenticated
export function isAuthenticated() {
    if (typeof window === 'undefined') return false;
    
    const token = localStorage.getItem('accessToken');
    return !!token;
}

// Get current user from localStorage
export function getCurrentUser() {
    if (typeof window === 'undefined') return null;
    
    try {
        const userData = localStorage.getItem('currentUser');
        return userData ? JSON.parse(userData) : null;
    } catch (error) {
        console.error('Error parsing user data:', error);
        return null;
    }
}

// Set current user to localStorage
export function setCurrentUser(user) {
    if (typeof window === 'undefined') return;
    
    if (user) {
        localStorage.setItem('currentUser', JSON.stringify(user));
    } else {
        localStorage.removeItem('currentUser');
    }
}

// Login function
export async function login(email, password, rememberMe = false) {
    try {
        const response = await apiClient.login({
            email,
            password,
            rememberMe
        });

        if (response.success) {
            // Store user data
            setCurrentUser(response.data.user);
            
            return {
                success: true,
                user: response.data.user,
                emailVerificationRequired: response.data.emailVerificationRequired
            };
        }

        return {
            success: false,
            message: response.message
        };

    } catch (error) {
        console.error('Login error:', error);
        
        return {
            success: false,
            message: error.data?.message || error.message || 'Giriş işlemi başarısız oldu'
        };
    }
}

// Register function
export async function register(userData) {
    try {
        const response = await apiClient.register(userData);

        if (response.success) {
            // Store user data
            setCurrentUser(response.data.user);
            
            return {
                success: true,
                user: response.data.user,
                emailVerificationRequired: response.data.emailVerificationRequired
            };
        }

        return {
            success: false,
            message: response.message
        };

    } catch (error) {
        console.error('Register error:', error);
        
        return {
            success: false,
            message: error.data?.message || error.message || 'Kayıt işlemi başarısız oldu',
            errors: error.data?.errors
        };
    }
}

// Logout function
export async function logout() {
    try {
        await apiClient.logout();
    } catch (error) {
        console.error('Logout error:', error);
    } finally {
        // Clear user data
        setCurrentUser(null);
    }
}

// Refresh token function
export async function refreshAuthToken() {
    try {
        await apiClient.refreshToken();
        return true;
    } catch (error) {
        console.error('Token refresh failed:', error);
        // Clear user data on refresh failure
        setCurrentUser(null);
        return false;
    }
}

// Get user profile
export async function getUserProfile() {
    try {
        const response = await apiClient.getProfile();
        
        if (response.success) {
            setCurrentUser(response.data.user);
            return response.data.user;
        }
        
        return null;
    } catch (error) {
        console.error('Get profile error:', error);
        return null;
    }
}
