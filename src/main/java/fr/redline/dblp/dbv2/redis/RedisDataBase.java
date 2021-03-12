package fr.redline.dblp.dbv2.redis;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.ArrayList;

public class RedisDataBase {

    static ArrayList<RedisDataBase> redisDataBases = new ArrayList<>();

    public static void disconnectAll(){
        for(RedisDataBase rdb : new ArrayList<>(redisDataBases)){
            rdb.disconnect();
            redisDataBases.remove(rdb);
        }
    }

    String ip;
    Integer port;

    RedissonClient redisClient = null;

    public RedisDataBase(String ip, Integer port){

        this.ip = ip;
        this.port = port;

    }

    public RedissonClient getClient(){
        return redisClient;
    }

    public void connect(){

        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+ip+"/"+port);
        redisClient = Redisson.create(config);

    }

    public void disconnect(){

        if(redisClient != null){
            redisClient.shutdown();
            redisClient = null;
        }

    }

    public void setObject(String key, Object object, boolean aSync){

        if(redisClient != null) {

            RBucket<Object> bucket = redisClient.getBucket(key);
            if(aSync) bucket.setAsync(object);
            else bucket.set(object);

        }

    }

    public Object getObject(String key){
        return redisClient.getBucket(key).get();
    }

    public Object removeObject(String key){
        return redisClient.getBucket(key).getAndDelete();
    }

    public boolean hasObject(String key){
        return redisClient.getBucket(key).get() != null;
    }

}
