
-----------------
### 1.大数据架构简介
可查看《[大数据技术_简介_yore.pdf](./大数据技术_简介_yore.pdf)》文件

### 2. redis中分页查询
redis中分页可以使用`list`,list是一个列表，并且时按时间顺序排序插入每行的数据，所以可以行数来进行分页获取。
```java
List<String> result = jedisPool.getResource().lrange(key, start, end);
result/*.stream()*/.forEach(str -> {
    System.out.println(str);
});
```
但是对也简单的按时间用list是没有问题的，对于复杂的则需要借助`SortedSet`来实现，
<br/>
对于List分页可以参考 [RedisUtil.pageList()](./src/main/java/top/cnstu/apitx/utils/RedisUtil.java)
对于复杂的分页可参考 [RedisUtil.pageListWithSort()](./src/main/java/top/cnstu/apitx/utils/RedisUtil.java)


### 3. 项目配置信息
项目配置文件: `my.properties`
```
## redis config
redis.host=192.168.17.139
redis.port=6379
redis.auth=yore
redis.max_total=1024
redis.max_idle=200
redis.max_wait=-1
redis.test.on.borrow=false
redis.index=10

```

### 4.打包
 直接用Maven的`package`命令打包
 
### 5.发布
```
 nohup java -jar jar包 /dev/null 2>&1 &
```


##### 6. 项目贡献者
@yore



