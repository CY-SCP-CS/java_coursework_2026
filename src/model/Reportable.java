package model;

/**
 * 可展示接口
 * 提供格式化的信息展示方法
 */
public interface Reportable {
    /**
     * 返回实体的详细信息字符串
     * @return 格式化后的详细信息
     */
    String getInfo();
}
