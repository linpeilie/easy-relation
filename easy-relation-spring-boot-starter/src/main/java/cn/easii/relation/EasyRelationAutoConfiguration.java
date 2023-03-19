package cn.easii.relation;

import cn.easii.relation.core.DefaultRelationCache;
import cn.easii.relation.core.InjectRelation;
import cn.easii.relation.core.JsonMapToBeanHandle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EasyRelationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RelationCache.class)
    public RelationCache relationCache() {
        return new DefaultRelationCache();
    }

    @Bean
    @ConditionalOnMissingBean(MapToBeanHandle.class)
    public MapToBeanHandle mapToBeanHandle() {
        return new JsonMapToBeanHandle();
    }

    @Bean
    public InjectRelation injectRelation(RelationCache relationCache, MapToBeanHandle mapToBeanHandle) {
        return new InjectRelation(relationCache, mapToBeanHandle);
    }

}
