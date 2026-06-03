package model;

/**
 * 管理员类
 * 继承 Person，拥有数据管理权限
 */
public class Admin extends Person {
    private int adminLevel;

    public Admin() {
        super();
        this.adminLevel = 1;
    }

    public Admin(String id, String name, int adminLevel) {
        super(id, name, Role.ADMIN);
        this.adminLevel = adminLevel;
    }

    // === Getters and Setters ===

    public int getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }

    @Override
    public String getDescription() {
        return "Admin: " + getName() + " (Level " + adminLevel + ")";
    }

    @Override
    public String getInfo() {
        return "=== Admin Info ===\n" +
               "ID: " + getId() + "\n" +
               "Name: " + getName() + "\n" +
               "Admin Level: " + adminLevel + "\n" +
               "Permissions: Full data management\n";
    }
}
