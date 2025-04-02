package ktCore.ch07_java_and_kotlin.java_pack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/** 一个普通的java类  */
public class JavaUser {
    /** 非空类型：不可能为 null 的值 */
    private final int id ;
    /** 非空类型：不可能为 null 的值。使用 @NotNull 注解，显示指定一个不为空的值 */
    @NotNull
    private final String name ;
    /** 平台类型：一个可能为 null 的值 */
    private String info ;
    /** 可空类型：显示指定有可能为 null 的值 */
    @Nullable
    private String nullValue = null;
    /** 没有设置 get 和set 的值 */
    private String value = null;
    private Boolean boolValue = false;

    public Boolean isBoolValue() {
        return boolValue;
    }
    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }
    public void setNullValue(@Nullable String nullValue) {
        this.nullValue = nullValue;
    }
    @Nullable
    public String getNullValue() {
        return nullValue;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public JavaUser(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    @NotNull
    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        return "JavaUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaUser javaUser = (JavaUser) o;
        return id == javaUser.id && name.equals(javaUser.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
