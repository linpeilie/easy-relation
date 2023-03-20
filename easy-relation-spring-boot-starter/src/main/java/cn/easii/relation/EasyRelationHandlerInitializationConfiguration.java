package cn.easii.relation;

import cn.easii.relation.core.RelationHandlerRepository;
import cn.easii.relation.core.RelationService;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EasyRelationHandlerInitializationConfiguration.Register.class})
public class EasyRelationHandlerInitializationConfiguration {

    static class Register implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(final ConfigurableListableBeanFactory configurableListableBeanFactory)
            throws BeansException {
            final Map<String, RelationService> relationService =
                configurableListableBeanFactory.getBeansOfType(RelationService.class);
            relationService.values().forEach(RelationHandlerRepository::registerHandler);
        }
    }

}
