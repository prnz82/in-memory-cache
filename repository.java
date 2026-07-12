import java.util.*;
import java.util.concurrent.*;

class Store {
    // key, (val, ttl) pairs
    public Map<Integer, ValuePair> keyValStore;
    
    // stores expiry times as keys, and the keys as vals in a list
    public Map<Long, List<Integer>> expiryTimes;

    public Store(){
        keyValStore = new ConcurrentHashMap<>();

        // we need it sorted by times
        expiryTimes = new TreeMap<>();
    }

}