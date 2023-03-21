package cn.easii.relation;

import cn.easii.relation.core.Constants;
import cn.easii.relation.core.DefaultRelationCache;
import cn.easii.relation.core.InjectRelation;
import cn.easii.relation.core.JsonMapToBeanHandle;
import cn.easii.relation.core.properties.RelationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(EasyRelationHandlerInitializationConfiguration.class)
public class EasyRelationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RelationProperties.class)
    @ConfigurationProperties(prefix = Constants.EasyRelation)
    public RelationProperties relationProperties() {
        return new RelationProperties();
    }

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
    public InjectRelation injectRelation(RelationCache relationCache,
        MapToBeanHandle mapToBeanHandle,
        RelationProperties relationProperties) {
        return new InjectRelation(relationCache, mapToBeanHandle, relationProperties);
    }

}
