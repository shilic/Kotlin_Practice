package ktCore.ch07_java_and_kotlin.java_pack;

import java.util.HashMap;
import java.util.Map;

public class JavaUserDao {
    Map<Integer,JavaUser> userMap = new HashMap<>();
    public void addUser(JavaUser user){
        userMap.put(user.getId(), user);
    }
    /** 有可能返回一个空值 */
    public JavaUser findUserById(int id){
        return userMap.get(id);
    }
}
