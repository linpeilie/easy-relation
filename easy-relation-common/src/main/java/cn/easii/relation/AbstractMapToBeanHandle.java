package cn.easii.relation;

import cn.easii.relation.exception.RelationException;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractMapToBeanHandle implements MapToBeanHandle {

    @Override
    @SuppressWarnings("unchecked")
    public <P> P mapToBean(final Map<String, Object> map, final Class<P> clazz) {
        if (String.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz) ||
            Long.class.isAssignableFrom(clazz)) {
            if (map.size() != 1) {
                throw new RelationException(
                    "parameter class type : " + clazz.getName() + ", but has multiple relation condition");
            }
            return (P) new ArrayList<Object>(map.values()).get(0);
        } else {
            return toBean(map, clazz);
        }
    }

    protected abstract <P> P toBean(final Map<String, Object> map, final Class<P> clazz);

}
