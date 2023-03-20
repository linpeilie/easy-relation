package cn.easii.relation.core;

import cn.easii.relation.AbstractMapToBeanHandle;
import cn.hutool.json.JSONUtil;
import java.util.Map;

public class JsonMapToBeanHandle extends AbstractMapToBeanHandle {
    @Override
    protected <P> P toBean(final Map<String, Object> map, final Class<P> clazz) {
        return JSONUtil.toBean(JSONUtil.toJsonStr(map), clazz);
    }
}
