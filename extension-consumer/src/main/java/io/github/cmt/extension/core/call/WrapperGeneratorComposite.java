package io.github.cmt.extension.core.call;

import io.github.cmt.extension.common.exception.SpiException;
import io.github.cmt.extension.core.call.dubbo.DubboWrapperGenerator;
import io.github.cmt.extension.core.call.local.LocalWrapperGenerator;
import io.github.cmt.extension.common.model.SpiConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WrapperGeneratorComposite implements WrapperGenerator {

    private static List<WrapperGenerator> generators = new ArrayList<>();

    private static final boolean DUBBO_PRESENT = ClassUtils.isPresent("com.alibaba.dubbo.common.URL", WrapperGeneratorComposite.class.getClassLoader());

    private static final boolean SPRING_PRESENT = ClassUtils.isPresent("org.springframework.context.ApplicationContext", WrapperGeneratorComposite.class.getClassLoader());

    public WrapperGeneratorComposite() {
        init();
    }

    private void init() {

        if (SPRING_PRESENT) {
            generators.add(new LocalWrapperGenerator());
        } else {
            log.info("invokeMethod为local的方式需要添加spring依赖");
        }

        if (DUBBO_PRESENT && SPRING_PRESENT) {
            generators.add(new DubboWrapperGenerator());
        } else {
            log.info("spi框架支持invokeMethod为dubbo的方式, 但是未加入dubbo或spring依赖包");
        }
        preCheck();
    }

    @Override
    public void preCheck() {
        generators.stream().forEach(WrapperGenerator::preCheck);
    }

    @Override
    public boolean support(SpiConfigDTO configDTO) {
        for (WrapperGenerator generator : generators) {
            if (generator != null && generator.support(configDTO)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object generateWrapper(SpiConfigDTO configDTO) {
        for (WrapperGenerator generator : generators) {
            if (generator.support(configDTO)) {
                return generator.generateWrapper(configDTO);
            }
        }
        throw new SpiException("不支持的协议: " + configDTO.getInvokeMethod());
    }

    @Override
    public void destroyWrapper(SpiConfigDTO configDTO) {
        for (WrapperGenerator generator : generators) {
            if (generator != null && generator.support(configDTO)) {
                generator.destroyWrapper(configDTO);
                return;
            }
        }
        throw new SpiException("不支持的协议: " + configDTO.getInvokeMethod());
    }
}
