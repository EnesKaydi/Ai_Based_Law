// 🏥 Health Check Routes

const express = require('express');
const { testConnection, getDatabaseStats } = require('../config/database');
const User = require('../models/User');

const router = express.Router();

// Basic health check
router.get('/', async (req, res) => {
    try {
        const healthStatus = {
            status: 'healthy',
            timestamp: new Date().toISOString(),
            uptime: process.uptime(),
            version: process.env.npm_package_version || '1.0.0',
            node: process.version,
            environment: process.env.NODE_ENV || 'development',
            memory: {
                used: Math.round(process.memoryUsage().heapUsed / 1024 / 1024),
                total: Math.round(process.memoryUsage().heapTotal / 1024 / 1024),
                external: Math.round(process.memoryUsage().external / 1024 / 1024),
            },
            database: {
                connected: false,
                stats: null
            }
        };

        // Test database connection
        const dbConnected = await testConnection();
        healthStatus.database.connected = dbConnected;

        if (dbConnected) {
            healthStatus.database.stats = await getDatabaseStats();
        }

        const httpStatus = dbConnected ? 200 : 503;
        if (!dbConnected) {
            healthStatus.status = 'unhealthy';
        }

        res.status(httpStatus).json({
            success: true,
            data: healthStatus
        });
    } catch (error) {
        console.error('Health check error:', error);
        res.status(503).json({
            success: false,
            message: 'Health check failed',
            data: {
                status: 'unhealthy',
                timestamp: new Date().toISOString(),
                error: error.message
            }
        });
    }
});

// Detailed system status
router.get('/status', async (req, res) => {
    try {
        const status = {
            server: {
                status: 'running',
                uptime: process.uptime(),
                environment: process.env.NODE_ENV || 'development',
                pid: process.pid,
                platform: process.platform,
                arch: process.arch
            },
            database: {
                connected: false,
                stats: null
            },
            memory: {
                rss: process.memoryUsage().rss,
                heapTotal: process.memoryUsage().heapTotal,
                heapUsed: process.memoryUsage().heapUsed,
                external: process.memoryUsage().external,
                arrayBuffers: process.memoryUsage().arrayBuffers
            },
            users: null
        };

        // Database status
        const dbConnected = await testConnection();
        status.database.connected = dbConnected;

        if (dbConnected) {
            status.database.stats = await getDatabaseStats();
            status.users = await User.getStats();
        }

        res.json({
            success: true,
            data: status
        });
    } catch (error) {
        console.error('Status check error:', error);
        res.status(500).json({
            success: false,
            message: 'Status check failed',
            error: error.message
        });
    }
});

// Database connectivity test
router.get('/db', async (req, res) => {
    try {
        const connected = await testConnection();
        const stats = connected ? await getDatabaseStats() : null;

        res.json({
            success: true,
            data: {
                connected,
                stats,
                timestamp: new Date().toISOString()
            }
        });
    } catch (error) {
        console.error('Database health check error:', error);
        res.status(500).json({
            success: false,
            message: 'Database health check failed',
            error: error.message
        });
    }
});

module.exports = router;
