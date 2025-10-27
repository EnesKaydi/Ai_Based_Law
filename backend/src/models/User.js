// 👤 User Model

const bcrypt = require('bcrypt');
const { v4: uuidv4 } = require('uuid');
const { executeQuery } = require('../config/database');

class User {
    constructor(data) {
        this.id = data.id;
        this.uuid = data.uuid;
        this.full_name = data.full_name;
        this.email = data.email;
        this.password_hash = data.password_hash;
        this.email_verified = data.email_verified;
        this.email_verified_at = data.email_verified_at;
        this.created_at = data.created_at;
        this.updated_at = data.updated_at;
        this.last_login_at = data.last_login_at;
        this.status = data.status;
    }

    // Create new user
    static async create(userData) {
        try {
            const { fullName, email, password } = userData;
            
            // Check if user already exists
            const existingUser = await this.findByEmail(email);
            if (existingUser) {
                throw new Error('Bu email adresi zaten kullanılıyor');
            }

            // Generate UUID and hash password
            const uuid = uuidv4();
            const saltRounds = parseInt(process.env.BCRYPT_ROUNDS) || 12;
            const passwordHash = await bcrypt.hash(password, saltRounds);

            // Insert user into database
            const query = `
                INSERT INTO users (uuid, full_name, email, password_hash)
                VALUES (?, ?, ?, ?)
            `;
            
            const result = await executeQuery(query, [uuid, fullName, email, passwordHash]);
            
            // Return created user (without password)
            return await this.findById(result.insertId);
        } catch (error) {
            console.error('Error creating user:', error);
            throw error;
        }
    }

    // Find user by ID
    static async findById(id) {
        try {
            const query = 'SELECT * FROM users WHERE id = ? AND status != "deleted"';
            const results = await executeQuery(query, [id]);
            
            if (results.length === 0) {
                return null;
            }
            
            const userData = results[0];
            delete userData.password_hash; // Don't return password hash
            return new User(userData);
        } catch (error) {
            console.error('Error finding user by ID:', error);
            throw error;
        }
    }

    // Find user by UUID
    static async findByUuid(uuid) {
        try {
            const query = 'SELECT * FROM users WHERE uuid = ? AND status != "deleted"';
            const results = await executeQuery(query, [uuid]);
            
            if (results.length === 0) {
                return null;
            }
            
            const userData = results[0];
            delete userData.password_hash; // Don't return password hash
            return new User(userData);
        } catch (error) {
            console.error('Error finding user by UUID:', error);
            throw error;
        }
    }

    // Find user by email
    static async findByEmail(email) {
        try {
            const query = 'SELECT * FROM users WHERE email = ? AND status != "deleted"';
            const results = await executeQuery(query, [email.toLowerCase()]);
            
            if (results.length === 0) {
                return null;
            }
            
            return new User(results[0]);
        } catch (error) {
            console.error('Error finding user by email:', error);
            throw error;
        }
    }

    // Authenticate user (for login)
    static async authenticate(email, password) {
        try {
            const query = 'SELECT * FROM users WHERE email = ? AND status = "active"';
            const results = await executeQuery(query, [email.toLowerCase()]);
            
            if (results.length === 0) {
                return null;
            }
            
            const userData = results[0];
            
            // Check password
            const isValidPassword = await bcrypt.compare(password, userData.password_hash);
            if (!isValidPassword) {
                return null;
            }
            
            // Update last login
            await this.updateLastLogin(userData.id);
            
            // Return user without password hash
            delete userData.password_hash;
            return new User(userData);
        } catch (error) {
            console.error('Error authenticating user:', error);
            throw error;
        }
    }

    // Update last login timestamp
    static async updateLastLogin(userId) {
        try {
            const query = 'UPDATE users SET last_login_at = CURRENT_TIMESTAMP WHERE id = ?';
            await executeQuery(query, [userId]);
        } catch (error) {
            console.error('Error updating last login:', error);
            throw error;
        }
    }

    // Update email verification status
    static async verifyEmail(userId) {
        try {
            const query = `
                UPDATE users 
                SET email_verified = TRUE, email_verified_at = CURRENT_TIMESTAMP 
                WHERE id = ?
            `;
            await executeQuery(query, [userId]);
        } catch (error) {
            console.error('Error verifying email:', error);
            throw error;
        }
    }

    // Update user password
    static async updatePassword(userId, newPassword) {
        try {
            const saltRounds = parseInt(process.env.BCRYPT_ROUNDS) || 12;
            const passwordHash = await bcrypt.hash(newPassword, saltRounds);
            
            const query = 'UPDATE users SET password_hash = ? WHERE id = ?';
            await executeQuery(query, [passwordHash, userId]);
        } catch (error) {
            console.error('Error updating password:', error);
            throw error;
        }
    }

    // Get user statistics
    static async getStats() {
        try {
            const totalQuery = 'SELECT COUNT(*) as total FROM users WHERE status != "deleted"';
            const activeQuery = 'SELECT COUNT(*) as active FROM users WHERE status = "active"';
            const verifiedQuery = 'SELECT COUNT(*) as verified FROM users WHERE email_verified = TRUE';
            const recentQuery = 'SELECT COUNT(*) as recent FROM users WHERE created_at > DATE_SUB(NOW(), INTERVAL 30 DAY)';
            
            const [totalResult] = await executeQuery(totalQuery);
            const [activeResult] = await executeQuery(activeQuery);
            const [verifiedResult] = await executeQuery(verifiedQuery);
            const [recentResult] = await executeQuery(recentQuery);
            
            return {
                total: totalResult.total,
                active: activeResult.active,
                verified: verifiedResult.verified,
                recent: recentResult.recent
            };
        } catch (error) {
            console.error('Error getting user stats:', error);
            throw error;
        }
    }

    // Convert to JSON (excluding sensitive data)
    toJSON() {
        const { password_hash, ...userWithoutPassword } = this;
        return userWithoutPassword;
    }
}

module.exports = User;
