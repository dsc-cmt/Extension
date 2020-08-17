package io.github.cmt.extension.provider.annotation;

import io.github.cmt.extension.provider.configuration.ExtensionConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author shengchaojie
 * @date 2020/8/17
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ExtensionConfiguration.class)
public @interface EnableExtensionProvider {
}
