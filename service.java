import java.util.*;
import java.util.concurrent.locks.*;

class StoreService {
    private ReentrantLock lock;
    private Store store;

    public StoreService(Store stor){
        lock = new ReentrantLock();
        this.store = stor;
    }

    public void clearTillTime(Long currTime){
        List<Long> temp = new ArrayList<>();

            for(Map.Entry<Long, List<Integer>> entry : store.expiryTimes.entrySet()){
                Long keyt = entry.getKey();
                if(keyt <= currTime) temp.add(keyt);
                else break;
            }

            for(int i = 0; i<temp.size(); i++){
                Long currKey = temp.get(i);
                List<Integer> keysToRemove = store.expiryTimes.get(currKey);
                for(int j = 0; j < keysToRemove.size(); j++){
                    store.keyValStore.remove(keysToRemove.get(j));
                }
                store.expiryTimes.remove(currKey);
            }
    }

    public int get(int key){
        lock.lock();
        try{
            Long currTime = System.currentTimeMillis();
            clearTillTime(currTime);
            if(store.keyValStore.containsKey(key)) return store.keyValStore.get(key).getValue();
            else throw new RuntimeException("key not found");
        }
        finally{
            lock.unlock();
        }
    }

    public void put(int key, int val, Long ttl){
        lock.lock();
        try{
            Long currTime = System.currentTimeMillis();
            Long expTime = currTime + ttl;
            // already called in delete
            //clearTillTime(currTime, store);
            delete(key);
            ValuePair vp = new ValuePair(val, expTime);
            store.keyValStore.put(key, vp);
            if(store.expiryTimes.containsKey(expTime)) store.expiryTimes.get(expTime).add(key);
            else{
                List<Integer> temp = new ArrayList<>();
                temp.add(key);
                store.expiryTimes.put(expTime, temp);
            }
        }
        finally{
            lock.unlock();
        }
    }

    public boolean delete(int key){
        lock.lock();
        try{
            Long currTime = System.currentTimeMillis();
            clearTillTime(currTime);
            
            if(store.keyValStore.containsKey(key)){
                Long expTime = store.keyValStore.get(key).getExpTime();
                store.keyValStore.remove(key);

                int ind = -1;
                List<Integer> keyvals = store.expiryTimes.get(expTime);

                for(int i = 0; i<keyvals.size(); i++){
                    if(keyvals.get(i) == key){
                        ind = i;
                        break;
                    }
                }

                store.expiryTimes.get(expTime).remove(ind);
                if(store.expiryTimes.get(expTime).isEmpty()) store.expiryTimes.remove(expTime);

                return true;
            }
            else return false;
        }
        finally{
            lock.unlock();
        }
    }

}