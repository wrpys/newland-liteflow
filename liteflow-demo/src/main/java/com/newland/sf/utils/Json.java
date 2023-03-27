package com.newland.sf.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author WRP
 * @since 2023/3/14
 */
public class Json {

  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    // 存在多余字段
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * 功能：将 Object 对象转换成 JSON String串
   *
   * @param object 需要转换成Json String的 Object 对象
   * @return 返回 String 串 当传入的对象参数为空时，返回 null 的串 否则转换成正常的Json String串
   */
  public static String toJson(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("对象转json字符串错误", e);
    }
  }

  /**
   * 格式化json
   *
   * @param object
   * @return
   */
  public static String toJsonWithDefaultPrettyPrinter(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("对象转json字符串错误", e);
    }
  }

  /**
   * 将 JSON 文本解析成 你直接需要的 对象（反序列化----推荐使用：反序列化的结果与原装的数据一致）。
   * 第二个参数要输入你要转的对象类名,可不必对其进行对象的强转 @param str 要解析的 JSON
   * String串。 @param s 解析 Json 为具体的对象名字 @return 返回指定的第二个参数对象 @throws
   */
  public static <T> T toObject(String str, Class<T> s) {
    try {
      return objectMapper.readValue(str, s);
    } catch (Exception e) {
      throw new RuntimeException("Json的String串转换成具体对象失败", e);
    }
  }

  /**
   * 多层对象转换
   * eg: valueTypeRef = new TypeReference<List<String>>() {}
   *
   * @param str
   * @param valueTypeRef
   * @param <T>
   * @return
   */
  public static <T> T toObject(String str, TypeReference<T> valueTypeRef) {
    try {
      return objectMapper.readValue(str, valueTypeRef);
    } catch (Exception e) {
      throw new RuntimeException("Json的String串转换成具体对象失败", e);
    }
  }

}
