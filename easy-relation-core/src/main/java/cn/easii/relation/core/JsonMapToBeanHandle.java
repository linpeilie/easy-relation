package cn.easii.relation.core;

import cn.easii.relation.MapToBeanHandle;
import cn.hutool.json.JSONUtil;
import java.util.Map;

public class JsonMapToBeanHandle implements MapToBeanHandle {
    @Override
    public <P> P mapToBean(final Map<String, Object> map, final Class<P> clazz) {
        return JSONUtil.toBean(JSONUtil.toJsonStr(map), clazz);
    }
}
