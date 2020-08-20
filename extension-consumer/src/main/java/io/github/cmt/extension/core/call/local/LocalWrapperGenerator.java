package io.github.cmt.extension.core.call.local;

import io.github.cmt.extension.core.call.WrapperGenerator;
import io.github.cmt.extension.common.exception.SpiException;
import io.github.cmt.extension.common.ExtensionTypeEnum;
import io.github.cmt.extension.common.annotation.Extension;
import io.github.cmt.extension.common.model.SpiConfigDTO;
import io.github.cmt.extension.common.util.ApplicationContextHolder;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 本地实现类Generator
 */
public class LocalWrapperGenerator implements WrapperGenerator {

    @Override
    public void preCheck() {
        if (ApplicationContextHolder.getApplicationContext() == null) {
            throw new SpiException("spring容器未初始化");
        }
    }

    @Override
    public boolean support(SpiConfigDTO configDTO) {
        return ExtensionTypeEnum.LOCAL.name().equalsIgnoreCase(configDTO.getInvokeMethod());
    }

    /**
     * local实现可能有多个实现 所以使用bizCode+interface方式获取保证唯一
     * @param configDTO
     * @return
     */
    @Override
    public Object generateWrapper(SpiConfigDTO configDTO) {
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        try {
            String[] names = applicationContext.getBeanNamesForType(ClassUtils.getClass(configDTO.getSpiInterface()));
            for(String name : names){
                Object bean = applicationContext.getBean(name);
                Extension extension = AnnotationUtils.findAnnotation(AopUtils.getTargetClass(bean), Extension.class);
                if(extension !=null && extension.bizCode().equals(configDTO.getBizCode())){
                    return bean;
                }
            }
            throw new RuntimeException("spi配置有误,找不到本地接口实现");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("spi配置有误,加载不到接口类");
        }
    }

    @Override
    public void destroyWrapper(SpiConfigDTO spiConfigDTO) {

    }
}
