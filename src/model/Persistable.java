package model;

/**
 * 可持久化接口
 * 提供数据实体的序列化/反序列化方法，支持文件 I/O
 */
public interface Persistable {
    /**
     * 将实体序列化为 CSV 格式字符串
     * @return CSV 格式的字符串表示
     */
    String toCSVString();

    /**
     * 从 CSV 格式字符串解析实体
     * @param csvLine CSV 格式的字符串
     */
    void fromCSVString(String csvLine);
}
