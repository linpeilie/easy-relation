package cn.easii.relation.msp;

import cn.easii.relation.MapToBeanHandle;
import io.github.linpeilie.Converter;
import java.util.Map;

public class MapStructPlusMapToBeanHandle implements MapToBeanHandle {

    private final Converter converter;

    public MapStructPlusMapToBeanHandle(final Converter converter) {
        this.converter = converter;
    }

    @Override
    public Object mapToBean(final Map<String, Object> map, final Class<?> clazz) {
        return converter.convert(map, clazz);
    }
}
