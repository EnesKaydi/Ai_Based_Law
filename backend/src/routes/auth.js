// 🔐 Authentication Routes

const express = require('express');
const rateLimit = require('express-rate-limit');
const { body, validationResult } = require('express-validator');
const User = require('../models/User');
const { generateTokens, verifyRefreshToken, authenticateToken } = require('../middleware/auth');

const router = express.Router();

// Rate limiting for auth endpoints
const authLimiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 5, // limit each IP to 5 requests per windowMs
    message: {
        success: false,
        message: 'Çok fazla giriş denemesi. 15 dakika sonra tekrar deneyin.',
        code: 'RATE_LIMIT_AUTH'
    },
    standardHeaders: true,
    legacyHeaders: false,
});

// Validation rules
const registerValidation = [
    body('fullName')
        .trim()
        .isLength({ min: 2, max: 100 })
        .withMessage('Ad soyad 2-100 karakter arasında olmalıdır')
        .matches(/^[a-zA-ZçğıöşüÇĞIİÖŞÜ\s]+$/)
        .withMessage('Ad soyad sadece harf ve boşluk içerebilir'),
    
    body('email')
        .isEmail()
        .normalizeEmail()
        .withMessage('Geçerli bir email adresi girin'),
    
    body('password')
        .isLength({ min: 8 })
        .withMessage('Şifre en az 8 karakter olmalıdır')
        .matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/)
        .withMessage('Şifre en az 1 küçük harf, 1 büyük harf, 1 rakam ve 1 özel karakter içermelidir'),
    
    body('confirmPassword')
        .custom((value, { req }) => {
            if (value !== req.body.password) {
                throw new Error('Şifre tekrarı eşleşmiyor');
            }
            return true;
        }),
    
    body('termsAccepted')
        .equals('true')
        .withMessage('Kullanım şartlarını kabul etmelisiniz')
];

const loginValidation = [
    body('email')
        .isEmail()
        .normalizeEmail()
        .withMessage('Geçerli bir email adresi girin'),
    
    body('password')
        .notEmpty()
        .withMessage('Şifre gereklidir')
];

// Helper function to handle validation errors
const handleValidationErrors = (req, res, next) => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        const errorMessages = {};
        errors.array().forEach(error => {
            errorMessages[error.path] = errorMessages[error.path] || [];
            errorMessages[error.path].push(error.msg);
        });

        return res.status(400).json({
            success: false,
            message: 'Doğrulama hatası',
            code: 'VALIDATION_ERROR',
            errors: errorMessages
        });
    }
    next();
};

// Register endpoint
router.post('/register', authLimiter, registerValidation, handleValidationErrors, async (req, res) => {
    try {
        const { fullName, email, password } = req.body;

        // Create user
        const user = await User.create({
            fullName,
            email,
            password
        });

        // Generate tokens
        const tokens = generateTokens(user);

        res.status(201).json({
            success: true,
            message: 'Kullanici basariyla olusturuldu',
            data: {
                user: user.toJSON(),
                tokens: {
                    accessToken: tokens.accessToken,
                    refreshToken: tokens.refreshToken,
                    expiresIn: 3600, // 1 hour
                    tokenType: 'Bearer'
                },
                emailVerificationRequired: !user.email_verified
            }
        });

        // TODO: Send email verification (will be implemented later)
        console.log(`📧 Email verification needed for: ${email}`);
        
    } catch (error) {
        console.error('Register error:', error);
        
        if (error.message.includes('email adresi zaten')) {
            return res.status(409).json({
                success: false,
                message: error.message,
                code: 'DUPLICATE_EMAIL'
            });
        }

        res.status(500).json({
            success: false,
            message: 'Kayıt işlemi sırasında hata oluştu',
            code: 'REGISTER_ERROR'
        });
    }
});

// Login endpoint
router.post('/login', authLimiter, loginValidation, handleValidationErrors, async (req, res) => {
    try {
        const { email, password, rememberMe = false } = req.body;

        // Authenticate user
        const user = await User.authenticate(email, password);
        
        if (!user) {
            return res.status(401).json({
                success: false,
                message: 'Email veya şifre hatalı',
                code: 'INVALID_CREDENTIALS'
            });
        }

        // Check if email is verified (optional for now)
        if (!user.email_verified) {
            console.log(`⚠️ User ${email} logged in without email verification`);
        }

        // Generate tokens
        const tokens = generateTokens(user);

        // Log successful login
        console.log(`✅ User logged in: ${email} from ${req.ip}`);

        res.json({
            success: true,
            message: 'Giriş başarılı',
            data: {
                user: user.toJSON(),
                tokens: {
                    accessToken: tokens.accessToken,
                    refreshToken: tokens.refreshToken,
                    expiresIn: 3600, // 1 hour
                    tokenType: 'Bearer'
                },
                emailVerificationRequired: !user.email_verified
            }
        });

    } catch (error) {
        console.error('Login error:', error);
        res.status(500).json({
            success: false,
            message: 'Giriş işlemi sırasında hata oluştu',
            code: 'LOGIN_ERROR'
        });
    }
});

// Refresh token endpoint
router.post('/refresh', async (req, res) => {
    try {
        const { refreshToken } = req.body;

        if (!refreshToken) {
            return res.status(400).json({
                success: false,
                message: 'Refresh token gerekli',
                code: 'REFRESH_TOKEN_REQUIRED'
            });
        }

        // Verify refresh token
        const decoded = verifyRefreshToken(refreshToken);
        
        // Get user
        const user = await User.findById(decoded.userId);
        if (!user) {
            return res.status(401).json({
                success: false,
                message: 'Geçersiz refresh token',
                code: 'INVALID_REFRESH_TOKEN'
            });
        }

        // Generate new access token
        const tokens = generateTokens(user);

        res.json({
            success: true,
            message: 'Token yenilendi',
            data: {
                accessToken: tokens.accessToken,
                expiresIn: 3600,
                tokenType: 'Bearer'
            }
        });

    } catch (error) {
        console.error('Refresh token error:', error);
        
        if (error.name === 'JsonWebTokenError' || error.name === 'TokenExpiredError') {
            return res.status(401).json({
                success: false,
                message: 'Geçersiz refresh token',
                code: 'INVALID_REFRESH_TOKEN'
            });
        }

        res.status(500).json({
            success: false,
            message: 'Token yenileme hatası',
            code: 'REFRESH_ERROR'
        });
    }
});

// Get current user profile
router.get('/profile', authenticateToken, async (req, res) => {
    try {
        res.json({
            success: true,
            data: {
                user: req.user.toJSON()
            }
        });
    } catch (error) {
        console.error('Profile error:', error);
        res.status(500).json({
            success: false,
            message: 'Profil bilgileri alınamadı',
            code: 'PROFILE_ERROR'
        });
    }
});

// Logout endpoint (optional, for token blacklisting in future)
router.post('/logout', authenticateToken, async (req, res) => {
    try {
        // TODO: Implement token blacklisting
        console.log(`👋 User logged out: ${req.user.email}`);
        
        res.json({
            success: true,
            message: 'Başarıyla çıkış yapıldı'
        });
    } catch (error) {
        console.error('Logout error:', error);
        res.status(500).json({
            success: false,
            message: 'Çıkış işlemi sırasında hata oluştu',
            code: 'LOGOUT_ERROR'
        });
    }
});

module.exports = router;
