package cn.easii.example;

import cn.easii.relation.msp.MapStructPlusMapToBeanHandle;
import io.github.linpeilie.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EasyRelationConfiguration {

    @Bean
    public MapStructPlusMapToBeanHandle mapToBeanHandle(Converter converter) {
        return new MapStructPlusMapToBeanHandle(converter);
    }

}
