package cn.easii.relation;

import cn.easii.relation.core.DataProvideService;
import cn.easii.relation.core.DataProviderRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EasyDataProviderInitializationConfiguration.Register.class})
public class EasyDataProviderInitializationConfiguration {

    static class Register implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
            if (DataProvideService.class.isAssignableFrom(bean.getClass())) {
                DataProviderRepository.registerProvider((DataProvideService) bean);
            }
            return bean;
        }
    }

}
