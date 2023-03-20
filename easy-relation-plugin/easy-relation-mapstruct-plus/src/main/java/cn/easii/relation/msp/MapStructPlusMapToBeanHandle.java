package cn.easii.relation.msp;

import cn.easii.relation.AbstractMapToBeanHandle;
import cn.easii.relation.MapToBeanHandle;
import io.github.linpeilie.Converter;
import java.util.Map;

public class MapStructPlusMapToBeanHandle extends AbstractMapToBeanHandle {

    private final Converter converter;

    public MapStructPlusMapToBeanHandle(final Converter converter) {
        this.converter = converter;
    }

    @Override
    public <P> P toBean(final Map<String, Object> map, final Class<P> clazz) {
        return converter.convert(map, clazz);
    }
}
