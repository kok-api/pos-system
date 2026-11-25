package pos.com.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import pos.com.common.exception.ExceptionAdvices;

@AutoConfiguration
public class CommonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ExceptionAdvices exceptionAdvices() {
        return new ExceptionAdvices();
    }
}
