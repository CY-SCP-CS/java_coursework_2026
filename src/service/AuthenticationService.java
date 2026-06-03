package service;

import model.Person;
import model.Role;
import util.GameLogger;

/**
 * 认证服务
 * 处理用户登录、登出和会话管理
 */
public class AuthenticationService {
    private GameDataManager dataManager;
    private Person currentUser;

    public AuthenticationService(GameDataManager dataManager) {
        this.dataManager = dataManager;
        this.currentUser = null;
    }

    /**
     * 用户登录
     * @param id 用户 ID
     * @param password 密码
     * @return 登录成功的 Person 对象，失败返回 null
     */
    public Person login(String id, String password) {
        Person person = dataManager.findPersonById(id);
        if (person == null) {
            GameLogger.warn("AuthService", "Login failed: user not found - " + id);
            return null;
        }
        String storedPassword = dataManager.getPassword(id);
        if (storedPassword == null || !storedPassword.equals(password)) {
            GameLogger.warn("AuthService", "Login failed: wrong password for " + id);
            return null;
        }
        this.currentUser = person;
        GameLogger.info("AuthService", "User logged in: " + id + " (" + person.getRole() + ")");
        return person;
    }

    /**
     * 用户登出
     */
    public void logout() {
        if (currentUser != null) {
            GameLogger.info("AuthService", "User logged out: " + currentUser.getId());
        }
        this.currentUser = null;
    }

    /**
     * 获取当前登录用户
     */
    public Person getCurrentUser() {
        return currentUser;
    }

    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * 检查当前用户是否为 Admin
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == Role.ADMIN;
    }
}
