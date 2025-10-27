// 🔐 Authentication Middleware

const jwt = require('jsonwebtoken');
const User = require('../models/User');

// Verify JWT token
const authenticateToken = async (req, res, next) => {
    try {
        const authHeader = req.headers['authorization'];
        const token = authHeader && authHeader.split(' ')[1]; // Bearer TOKEN

        if (!token) {
            return res.status(401).json({
                success: false,
                message: 'Erişim tokeni gerekli',
                code: 'AUTH_001'
            });
        }

        // Verify token
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        
        // Get user from database
        const user = await User.findById(decoded.userId);
        if (!user) {
            return res.status(401).json({
                success: false,
                message: 'Geçersiz token - kullanici bulunamadi',
                code: 'AUTH_002'
            });
        }

        // Check if user is active
        if (user.status !== 'active') {
            return res.status(403).json({
                success: false,
                message: 'Hesap askiya alinmis veya deaktif',
                code: 'AUTH_003'
            });
        }

        // Add user to request object
        req.user = user;
        next();
    } catch (error) {
        if (error.name === 'JsonWebTokenError') {
            return res.status(401).json({
                success: false,
                message: 'Geçersiz token',
                code: 'AUTH_004'
            });
        } else if (error.name === 'TokenExpiredError') {
            return res.status(401).json({
                success: false,
                message: 'Token suresi dolmus',
                code: 'AUTH_005'
            });
        } else {
            console.error('Auth middleware error:', error);
            return res.status(500).json({
                success: false,
                message: 'Token dogrulama hatasi',
                code: 'AUTH_006'
            });
        }
    }
};

// Optional authentication (doesn't fail if no token)
const optionalAuth = async (req, res, next) => {
    try {
        const authHeader = req.headers['authorization'];
        const token = authHeader && authHeader.split(' ')[1];

        if (token) {
            const decoded = jwt.verify(token, process.env.JWT_SECRET);
            const user = await User.findById(decoded.userId);
            
            if (user && user.status === 'active') {
                req.user = user;
            }
        }
        
        next();
    } catch (error) {
        // Ignore token errors in optional auth
        next();
    }
};

// Require email verification
const requireEmailVerification = (req, res, next) => {
    if (!req.user) {
        return res.status(401).json({
            success: false,
                message: 'Kimlik dogrulama gerekli',
            code: 'AUTH_007'
        });
    }

    if (!req.user.email_verified) {
        return res.status(403).json({
            success: false,
                message: 'Email adresinizi dogrulamaniz gerekli',
            code: 'AUTH_008',
            data: {
                emailVerificationRequired: true
            }
        });
    }

    next();
};

// Generate JWT tokens
const generateTokens = (user) => {
    const payload = {
        userId: user.id,
        uuid: user.uuid,
        email: user.email
    };

    const accessToken = jwt.sign(
        payload,
        process.env.JWT_SECRET,
        { 
            expiresIn: process.env.JWT_ACCESS_EXPIRES_IN || '1h',
            issuer: 'ai-hukuk-api',
            audience: 'ai-hukuk-frontend'
        }
    );

    const refreshToken = jwt.sign(
        payload,
        process.env.JWT_REFRESH_SECRET || process.env.JWT_SECRET,
        { 
            expiresIn: process.env.JWT_REFRESH_EXPIRES_IN || '30d',
            issuer: 'ai-hukuk-api',
            audience: 'ai-hukuk-frontend'
        }
    );

    return { accessToken, refreshToken };
};

// Verify refresh token
const verifyRefreshToken = (token) => {
    try {
        return jwt.verify(token, process.env.JWT_REFRESH_SECRET || process.env.JWT_SECRET);
    } catch (error) {
        throw error;
    }
};

module.exports = {
    authenticateToken,
    optionalAuth,
    requireEmailVerification,
    generateTokens,
    verifyRefreshToken
};
