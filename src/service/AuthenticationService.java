package service;

import model.Person;
import model.Role;

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
            return null;
        }
        String storedPassword = dataManager.getPassword(id);
        if (storedPassword == null || !storedPassword.equals(password)) {
            return null;
        }
        this.currentUser = person;
        return person;
    }

    /**
     * 用户登出
     */
    public void logout() {
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
