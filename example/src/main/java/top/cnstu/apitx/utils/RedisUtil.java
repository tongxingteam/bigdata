package top.cnstu.apitx.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Set;

/**
 * Redis适合做分页查询的可用，SortedSet
 * <pre>
 *    Redis中的5中数据类型
 *      1、String: 主要用于存储字符串；
 *      2、Hash: 主要用于存储key-value型数据；
 *      3、List: 主要用于存储一个列表，列表中的每一个元素按元素的插入时的顺序进行保存；
 *      4、Set: 主要存储无序集合，无序；
 *      5、SortedSet: 主要存储有序集合
 * <pre/>
 *
 *
 * Created by yore on 2018-06-02 15:56
 */
public class RedisUtil {
    private static JedisPool jedisPool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(PropertiesUtil.getPropInteger("redis.max_total"));
        config.setMaxIdle(PropertiesUtil.getPropInteger("redis.max_idle"));
        config.setMaxWaitMillis(PropertiesUtil.getPropInteger("redis.max_wait"));
        config.setTestOnBorrow(PropertiesUtil.getPropBoolean("redis.test.on.borrow"));
        //config.setTestOnReturn();
        //jedisPool = new JedisPool("jedis://:yore@192.168.17.139:6379");
        jedisPool = new JedisPool(
                config,
                PropertiesUtil.getPropValue("redis.host"),
                PropertiesUtil.getPropInteger("redis.port"),
                5000,
                PropertiesUtil.getPropValue("redis.auth"));
    }


    /**
     *
     * 获取Jedis对象
     * @author yore
     * @return redis.clients.jedis.Jedis
     * @date 2018/6/3 16:52
     */
    public static synchronized Jedis getJedis() {
        Jedis jedis = null;
        jedis = jedisPool.getResource();
        jedis.select(PropertiesUtil.getPropInteger("redis.index"));
        return jedis;
    }



    /* ------- String ------- */
    /**
     *
     * 将key对应的表中的值自增1
     * @author yore
     * @param key 类似于表
     * @return java.lang.Long 自增后的值
     * @date 2018/6/3 17:53
     */
    public static  Long incr(String key) {
        Jedis jedis = getJedis();
        Long result = jedis.incr(key);
        jedis.close();
        return result;
    }
    /**
     *
     * 将key对应的表中的值自减1
     * @author yore
     * @param key 类似于表
     * @return java.lang.Long 自减后的值
     * @date 2018/6/3 17:55
     */
    public static  Long decr(String key) {
        Jedis jedis = getJedis();
        Long result = jedis.decr(key);
        jedis.close();
        return result;
    }

    /**
     *
     * 在指定的键上设置超时。超时后，密钥将被自动删除
     * @author yore
     * @param key 类似于表
     * @param second 到期秒数
     * @return java.lang.Long 1：设置了超时时间。 0：从此以后没有设置超时
     * @date 2018/6/3 17:58
     */
    public static Long expire(String key, int second) {
        Jedis jedis = getJedis();
        Long result = jedis.expire(key, second);
        jedis.close();
        return result;
    }
    /**
     *
     * TTL命令返回剩余时间，以秒为单位生存
     * @author yore
     * @param key 类似于表
     * @return java.lang.Long 返回剩余时间，以秒为单位生存一个密钥
     * @date 2018/6/3 18:05
     */
    public static Long ttl(String key) {
        Jedis jedis = getJedis();
        Long result = jedis.ttl(key);
        jedis.close();
        return result;
    }

    /* ------ set集合操作的方法 ------ */
    /**
     *
     * 以set形式保存value
     * @author yore
     * @param key 类似于表
     * @param value 保存的值
     * @return java.lang.String 成功返回OK
     * @date 2018/6/3 16:54
     */
    public static  String insertSet(String key, String value) {
        Jedis jedis = getJedis();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }
    /**
     *
     * 以set形式保存value<br/>
     * 第二个参数可以传入多个值，如果重复只保存一个
     *
     * @author yore
     * @param key 类似于表
     * @param value 保存的值
     * @return java.lang.String 成功返回添加的value的个数
     * @date 2018/6/3 16:54
     */
    public static long insertSet(String key, String ... value) {
        Jedis jedis = getJedis();
        Long result = jedis.sadd(key,value);
        jedis.close();
        return result;
    }
    /**
     *
     * 设置set的值，并返回旧的val
     * @author yore
     * @param key 类似于表
     * @param newVal 新的val
     * @return java.lang.String 旧的值
     * @date 2018/6/3 17:24
     */
    public static String upinsertSet(String key,String newVal) {
        Jedis jedis = getJedis();
        // 获取一条数据时，set中有多条数据时会报错
        String result = jedis.getSet(key,newVal);
        //获取set集合中所有的值
        jedis.close();
        return result;
    }
    /**
     *
     * 获取set中的值
     * @author yore
     * @param key 类似于表
     * @return java.lang.String
     * @date 2018/6/3 16:57
     */
    public static String getSet(String key) {
        Jedis jedis = getJedis();
        // 获取一条数据时，set中有多条数据时会报错
        String result = jedis.get(key);
        //获取set集合中所有的值
        jedis.close();
        return result;
    }
    /**
     *
     * 获取set中所有的值
     * @author yore
     * @param key 类似于表
     * @return java.lang.String
     * @date 2018/6/3 16:57
     */
    public static Set<String> getSetList(String key) {
        Jedis jedis = getJedis();
        //获取set集合中所有的值
        Set<String> result = jedis.smembers(key);
        jedis.close();
        return result;
    }

    /* ------ zset ------ */
    /**
     *
     * 将一个或多个成员元素及其分数值加入到有序集当中
     * <pre>
     *    如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     *    分数值可以是整数值或双精度浮点数。
     *    如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     *    当 key 存在但不是有序集类型时，返回一个错误。
     * </pre>
     * @author yore
     * @param key 类似于表
     * @param member value值
     * @param score 对应的分数
     * @return java.lang.Long 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     * @date 2018/6/3 19:06
     */
    public static Long zadd(String key,String member, double score) {
        Jedis jedis = getJedis();
        Long result = jedis.zadd(key, score, member);
        jedis.close();
        return result;
    }

    /**
     *
     * 返回有序集中指定分数区间内的所有的成员。
     * 有序集成员按分数值递减(从大到小)的次序排列
     * <p>
     *     具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。
     *     除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样.
     * </p>
     * @author yore
     * @param key 类似于表,
     * @param max 最大分数值
     * @param min 最小分数值
     * @param offset 取值的下标
     * @param count 取值的个数
     * @return java.util.LinkedHashSet<java.lang.String> 指定区间内，带有分数值(可选)的有序集成员的列表。
     * @date 2018/6/3 19:21
     */
    public static Set<String> zrevrangebyscore(String key, String max,
                                               String min, int offset, int count){
        Jedis jedis = getJedis();
        Set<String> result = (Set<String>)
                jedis.zrevrangeByScore(key, max, min, offset, count);
        jedis.close();
        return result;
    }

    /* ------ Hash ------ */
    /**
     *
     * 向hash中添加元素，如果item有就修改其值
     * @author yore
     * @param key 类似于表
     * @param item hash中的字段
     * @param value 值
     * @return java.lang.Long 插入返回1，修改返回0
     * @date 2018/6/3 17:33
     */
    public static Long upInsetHash(String key, String item, String value) {
        Jedis jedis = getJedis();
        Long result = jedis.hset(key, item, value);
        jedis.close();
        return result;
    }
    /**
     *
     * 查询hash中的值
     * @author yore
     * @param key 类似于表
     * @param item hash中的字段
     * @return java.lang.String 返回字段对应的值; 若没有对应的字段，返回null
     * @date 2018/6/3 17:33
     */
    public static  String getHashVal(String key, String item) {
        Jedis jedis = getJedis();
        String result = jedis.hget(key, item);
        jedis.close();
        return result;
    }
    /**
     *
     * Redis Hmget 命令用于返回哈希表中，一个或多个给定字段的值。
     如果指定的字段不存在于哈希表，那么返回一个 nil 值。
     * @author yore
     * @param key 类似于表
     * @param item 字段名
     * @return java.util.List<java.lang.String>
     *     一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。
     * @date 2018/6/3 17:47
     */
    public static List<String> getHashList(String key, String... item) {
        Jedis jedis = getJedis();
        List<String> result = jedis.hmget(key, item);
        jedis.close();
        return result;
    }
    /**
     *
     * 删除Hash中给定字段对应的值
     * @author yore
     * @param key 类似于表
     * @param item 字段名
     * @return java.lang.Long 1删除成功；
     * @date 2018/6/3 18:12
     */
    public static Long hashDel(String key, String item) {
        Jedis jedis = getJedis();
        Long result = jedis.hdel(key, item);
        jedis.close();
        return result;
    }

    /* ------ list ------ */
    /**
     *
     * @param key
     * @param strings 添加的val数
     * @return
     */
    /**
     *
     * 向list中添加val
     * @author yore
     * @param key 类似于表
     * @param strings 值数组
     * @return java.lang.Long 列表中的元素个数
     * @date 2018/6/3 18:22
     */
    public static Long insertList(String key, String... strings) {
        Jedis jedis = getJedis();
        Long result = jedis.rpush(key, strings);
        jedis.close();
        return result;
    }

    /**
     *
     * Redis Lrange 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 <br/>
     * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。<br/>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * @author yore
     * @param key 类似于表
     * @param start 起始下标 从0开始
     * @param end 终止下标
     * @return java.util.List<java.lang.String> 区间内的值列表
     * @date 2018/6/3 18:23
     */
    public static List<String> lrange(String key, int start, int end) {
        Jedis jedis = getJedis();
        List<String> result = jedis.lrange(key, start, end);
        jedis.close();
        return result;
    }
    /**
     *
     * 分页显示List中的值
     * <p>
     *     当传入的页数小于1时，返回默认第一页的内容
     *     当传入的页数没有对应的值时，返回[]
     * </p>
     * @author yore
     * @param key 类似于表
     * @param pageNum 页数
     * @param pageSize 每页显示的记录数
     * @return java.util.List<java.lang.String> 给定页面的数据
     * @date 2018/6/3 18:34
     */
    public static List<String> pageList(String key, int pageNum, int pageSize) {
        pageNum = pageNum<1? 1:pageNum;
        return lrange("list_table",(pageNum-1)*pageSize,pageNum*pageSize-1);
    }
    /**
     *
     * 从列表中从头部开始移除count个匹配的value。
     * @author yore
     * @param key 类似于表
     * @param count 移出的行数<br/>
     *              如果count为0，所有匹配的元素都被删除。
     *              如果count是负数，内容从尾部开始删除。
     * @param value
     * @return java.lang.Long
     * @date 2018/6/3 18:42
     */
    public static Long lrem(String key, Long count, String value) {
        Jedis jedis = getJedis();
        Long result = jedis.lrem(key, count, value);
        jedis.close();
        return result;
    }

    /* ------ 表操作 ------ */
    /**
     *
     * 删除表
     * @author yore
     * @param key 类似于表
     * @return java.lang.Long 1删除成功
     * @date 2018/6/3 18:16
     */
    public static Long del(String key) {
        Jedis jedis = getJedis();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }




    public static void main(String[] args) {
//        String str = insertSet("set_table","yuan");
//        long str = insertSet("set_table2","yore","yuan","dong","dong");
//        String str = getSet("set_table2");
//        System.out.println(upinsertSet("set_table3","val"));
//        System.out.println(getSetList("set_table2"));

//        System.out.println(upInsetHash("hset_table","item3","dong2"));
//        System.out.println(getHashVal("hset_table","item11"));
//        System.out.println(getHashList("hset_table","item1","item2"));
//        System.out.println(hashDel("hset_table","item3"));

//        System.out.println(incr("increase_table"));
//        System.out.println(decr("increase_table"));
//        System.out.println(decr("expire_table"));
//        System.out.println(expire("expire_table",5000));
//        System.out.println(ttl("expire_table"));
//        System.out.println(del("del_table"));

//        System.out.println(insertList("list_table","yuan2","yore2","dong2","dong2"));
//        System.out.println(lrange("list_table",0,3));
//        System.out.println(pageList("list_table",4,3));

//        System.out.println(lrem("lrem_table",2L,"d"));
//        System.out.println(zadd("zadd_table","a",3.14));
//        System.out.println(zrevrangebyscore("zadd_table","3.14159","3.1",1,3));

    }

}
