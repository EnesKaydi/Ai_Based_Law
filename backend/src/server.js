// 🏛️ AI Tabanlı Hukuk Sistemi - Backend Server

const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const compression = require('compression');
const morgan = require('morgan');
const rateLimit = require('express-rate-limit');
require('dotenv').config();

// Route imports
const authRoutes = require('./routes/auth');
const healthRoutes = require('./routes/health');

// Middleware imports
const errorHandler = require('./middleware/errorHandler');
const notFound = require('./middleware/notFound');

// Database imports
const { testConnection, initializeDatabase } = require('./config/database');

const app = express();
const PORT = process.env.PORT || 8000;

// Security middleware
app.use(helmet({
    crossOriginResourcePolicy: { policy: "cross-origin" }
}));

// CORS configuration
const corsOrigins = (process.env.CORS_ORIGIN || 'http://localhost:3000').split(',').map(origin => origin.trim());
app.use(cors({
    origin: corsOrigins,
    credentials: true,
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization']
}));

// General middleware
app.use(compression());
app.use(morgan('combined'));
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// Rate limiting
const limiter = rateLimit({
    windowMs: parseInt(process.env.RATE_LIMIT_WINDOW_MS) || 15 * 60 * 1000, // 15 minutes
    max: parseInt(process.env.RATE_LIMIT_MAX_REQUESTS) || 100, // limit each IP to 100 requests per windowMs
    message: {
        success: false,
        message: 'Çok fazla istek gönderildi. Lütfen daha sonra tekrar deneyin.',
        retryAfter: Math.ceil((parseInt(process.env.RATE_LIMIT_WINDOW_MS) || 15 * 60 * 1000) / 1000)
    },
    standardHeaders: true, // Return rate limit info in the `RateLimit-*` headers
    legacyHeaders: false, // Disable the `X-RateLimit-*` headers
});

app.use('/v1/', limiter);

// API Routes
app.use('/v1/health', healthRoutes);
app.use('/v1/auth', authRoutes);

// Root endpoint
app.get('/', (req, res) => {
    res.json({
        success: true,
        message: '🏛️ AI Tabanlı Hukuk Sistemi API',
        version: '1.0.0',
        docs: '/v1/health',
        endpoints: {
            health: '/v1/health',
            auth: '/v1/auth'
        }
    });
});

// 404 handler
app.use(notFound);

// Error handler
app.use(errorHandler);

// Initialize database and start server
async function startServer() {
    try {
        // Test database connection
        console.log('🔄 Testing database connection...');
        const dbConnected = await testConnection();
        
        if (dbConnected) {
            // Initialize database tables
            console.log('🔄 Initializing database...');
            await initializeDatabase();
            console.log('✅ Database initialized successfully');
        } else {
            console.warn('⚠️ Database connection failed, server will start anyway');
        }

        // Start server
        app.listen(PORT, '0.0.0.0', () => {
            console.log(`
🏛️ AI Tabanlı Hukuk Sistemi Backend
📡 Server running on port ${PORT}
🌍 Environment: ${process.env.NODE_ENV || 'development'}
🔗 API Base URL: http://localhost:${PORT}/v1
🗄️ Database: ${dbConnected ? '✅ Connected' : '❌ Disconnected'}
⚖️ Hukuk ve Adalet için hazır!
            `);
        });

    } catch (error) {
        console.error('❌ Server startup failed:', error);
        process.exit(1);
    }
}

// Start the server
startServer();

// Graceful shutdown
process.on('SIGTERM', () => {
    console.log('SIGTERM received, shutting down gracefully');
    process.exit(0);
});

process.on('SIGINT', () => {
    console.log('SIGINT received, shutting down gracefully');
    process.exit(0);
});

module.exports = app;
