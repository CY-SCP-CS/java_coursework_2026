package model;

import java.util.List;

/**
 * 可搜索接口
 * 提供按 ID 和名称搜索的统一方法签名
 */
public interface Searchable<T> {
    /**
     * 按 ID 精确查找
     * @param id 实体 ID
     * @return 匹配的实体，未找到返回 null
     */
    T searchById(String id);

    /**
     * 按名称模糊匹配
     * @param name 名称关键字
     * @return 匹配的实体列表
     */
    List<T> searchByName(String name);
}
