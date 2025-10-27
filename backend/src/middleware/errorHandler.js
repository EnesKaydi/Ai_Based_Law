// 🚨 Error Handler Middleware

const errorHandler = (err, req, res, next) => {
    console.error('Error occurred:', {
        message: err.message,
        stack: err.stack,
        url: req.url,
        method: req.method,
        ip: req.ip,
        userAgent: req.get('User-Agent'),
        timestamp: new Date().toISOString()
    });

    // Default error response
    let error = {
        success: false,
        message: 'Sunucu hatası oluştu',
        code: 'SERVER_ERROR'
    };

    // Handle specific error types
    if (err.code === 'ER_DUP_ENTRY') {
        // MySQL duplicate entry error
        if (err.message.includes('email')) {
            error.message = 'Bu email adresi zaten kullanılıyor';
            error.code = 'DUPLICATE_EMAIL';
            return res.status(409).json(error);
        }
        error.message = 'Bu veri zaten mevcut';
        error.code = 'DUPLICATE_ENTRY';
        return res.status(409).json(error);
    }

    if (err.code === 'ER_NO_REFERENCED_ROW_2') {
        // MySQL foreign key constraint error
        error.message = 'İlişkili veri bulunamadı';
        error.code = 'INVALID_REFERENCE';
        return res.status(400).json(error);
    }

    if (err.name === 'ValidationError') {
        // Validation errors
        error.message = 'Veri doğrulama hatası';
        error.code = 'VALIDATION_ERROR';
        error.details = err.details || err.message;
        return res.status(400).json(error);
    }

    if (err.name === 'CastError') {
        // Database cast errors
        error.message = 'Geçersiz veri formatı';
        error.code = 'INVALID_FORMAT';
        return res.status(400).json(error);
    }

    if (err.code === 'ECONNREFUSED') {
        // Database connection error
        error.message = 'Veritabanı bağlantı hatası';
        error.code = 'DB_CONNECTION_ERROR';
        return res.status(503).json(error);
    }

    if (err.code === 'ETIMEDOUT') {
        // Request timeout
        error.message = 'İstek zaman aşımına uğradı';
        error.code = 'REQUEST_TIMEOUT';
        return res.status(408).json(error);
    }

    if (err.message && err.message.includes('JWT')) {
        // JWT related errors
        error.message = 'Token doğrulama hatası';
        error.code = 'TOKEN_ERROR';
        return res.status(401).json(error);
    }

    // Handle known application errors
    if (err.statusCode) {
        error.message = err.message;
        error.code = err.code || 'APPLICATION_ERROR';
        return res.status(err.statusCode).json(error);
    }

    // Development vs Production error details
    if (process.env.NODE_ENV === 'development') {
        error.details = {
            message: err.message,
            stack: err.stack,
            ...err
        };
    }

    // Send generic error for unknown errors
    res.status(500).json(error);
};

module.exports = errorHandler;
