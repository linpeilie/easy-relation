package cn.easii.relation;

import java.util.Map;

public interface MapToBeanHandle {

    <P> P mapToBean(final Map<String, Object> map, final Class<P> clazz);

}
