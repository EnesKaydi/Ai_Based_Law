// 🔍 404 Not Found Middleware

const notFound = (req, res, next) => {
    res.status(404).json({
        success: false,
        message: `Endpoint bulunamadı: ${req.method} ${req.originalUrl}`,
        code: 'ENDPOINT_NOT_FOUND',
        data: {
            method: req.method,
            url: req.originalUrl,
            availableEndpoints: {
                health: '/v1/health',
                auth: '/v1/auth'
            }
        }
    });
};

module.exports = notFound;
