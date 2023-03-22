package cn.easii.relation.msp;

import cn.easii.relation.AbstractMapToBeanHandle;
import cn.hutool.core.util.ReflectUtil;
import io.github.linpeilie.Converter;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MapStructPlusMapToBeanHandle extends AbstractMapToBeanHandle {

    private final Converter converter;

    public MapStructPlusMapToBeanHandle(final Converter converter) {
        this.converter = converter;
    }

    @Override
    public <P> P toBean(final Map<String, Object> map, final Class<P> clazz) {
        final P obj = converter.convert(map, clazz);
        return obj == null ?  ReflectUtil.newInstance(clazz) : obj;
    }
}
