package cn.easii.relation;

import cn.easii.relation.core.DataProviderRepository;
import cn.easii.relation.core.DataProvideService;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EasyDataProviderInitializationConfiguration.Register.class})
public class EasyDataProviderInitializationConfiguration {

    static class Register implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(final ConfigurableListableBeanFactory configurableListableBeanFactory)
            throws BeansException {
            final Map<String, DataProvideService> relationService =
                configurableListableBeanFactory.getBeansOfType(DataProvideService.class);
            relationService.values().forEach(DataProviderRepository::registerProvider);
        }
    }

}
