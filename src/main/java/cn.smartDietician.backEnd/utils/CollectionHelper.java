package cn.smartDietician.backEnd.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class CollectionHelper<T> {

    public <T> Collection<T> iterableToCollection(Iterable<T> iterable) {
        Collection<T> collection = new ArrayList<T>();
        while(iterable.iterator().hasNext()){
            collection.add(iterable.iterator().next());
        }
        //iterable.forEach(collection::add);
        return collection;
    }

    public <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> collection = new ArrayList<T>();
//        Iterator<T> it = iterable.iterator();
        for (T item : iterable) {
            collection.add(item);
        }
//        while(it.hasNext()){
//            collection.add(it.next());
//        }
        //iterable.forEach(collection::add);
        return collection;
    }

}
