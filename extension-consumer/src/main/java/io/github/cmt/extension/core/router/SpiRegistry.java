package io.github.cmt.extension.core.router;

import static io.github.cmt.extension.common.annotation.Extension.DEFAULT_BIZ_CODE;

import io.github.cmt.extension.core.call.WrapperGeneratorComposite;
import io.github.cmt.extension.core.configcenter.SpiConfigChangeListener;
import io.github.cmt.extension.common.model.SpiChangeType;
import io.github.cmt.extension.common.model.SpiConfigChangeEvent;
import io.github.cmt.extension.common.model.SpiConfigDTO;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SpiRegistry implements SpiConfigChangeListener {

    @Getter
    private final static SpiRegistry registry = new SpiRegistry();

    private WrapperGeneratorComposite composite = new WrapperGeneratorComposite();

    /**
     * 根据admin 配置中心的应该配置 建立一个应用放到SpiContainer中
     *
     * @param configDTO
     */
    private static void register(SpiConfigDTO configDTO) {
        if (Objects.isNull(configDTO)) {
            return;
        }
        Object wrapper = registry.composite.generateWrapper(configDTO);
        //默认实现额外注册
        if (configDTO.getIsDefault() != null && configDTO.getIsDefault() == 1) {
            SpiContainer.put(configDTO.getSpiInterface(), DEFAULT_BIZ_CODE, wrapper);
        }
        SpiContainer.put(configDTO.getSpiInterface(), configDTO.getBizCode(), wrapper);
    }

    /**
     * 销毁一个扩展点引用
     *
     * @param configDTO
     */
    private static void unRegister(SpiConfigDTO configDTO) {
        if (Objects.isNull(configDTO)) {
            return;
        }
        registry.composite.destroyWrapper(configDTO);
        SpiContainer.remove(configDTO.getSpiInterface(), configDTO.getBizCode());
        if(configDTO.getIsDefault()==1){
            SpiContainer.remove(configDTO.getSpiInterface(), DEFAULT_BIZ_CODE);
        }
    }

    @Override
    public void onChange(SpiConfigChangeEvent event) {
        if (!CollectionUtils.isEmpty(event.getConfigChanges())) {
            event.getConfigChanges().forEach(e -> {
                if (SpiChangeType.ADDED.equals(e.getChangeType()) || SpiChangeType.INIT.equals(e.getChangeType()) || SpiChangeType.MODIFIED.equals(e.getChangeType())) {
                    register(e.getConfig());
                } else if (SpiChangeType.DELETED.equals(e.getChangeType())) {
                    unRegister(e.getConfig());
                }
            });
        }
    }
}
