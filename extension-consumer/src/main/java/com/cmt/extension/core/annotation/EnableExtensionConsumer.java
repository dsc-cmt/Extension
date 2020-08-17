package com.cmt.extension.core.annotation;

import com.cmt.extension.core.common.ConfigMode;
import com.cmt.extension.core.config.ExtensionConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author shengchaojie
 * @date 2020/8/16
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ExtensionConfiguration.class)
public @interface EnableExtensionConsumer {

    String appName();

    ConfigMode configMode() default ConfigMode.LOCAL;

}
