package service;

/**
 * 文件存储服务
 * 负责从文件保存和加载系统数据
 */
public class FileStorageService {
    private GameDataManager dataManager;

    public FileStorageService(GameDataManager dataManager) {
        this.dataManager = dataManager;
    }

    // 将在后续实现中完善 CSV 读写逻辑
}
