package com.cmt.extension.core.router;

import com.cmt.extension.core.call.WrapperGeneratorComposite;
import com.cmt.extension.core.configcenter.SpiConfigChangeListener;
import com.cmt.extension.core.annotation.Extension;
import com.cmt.extension.core.configcenter.model.SpiChangeType;
import com.cmt.extension.core.configcenter.model.SpiConfigChangeEvent;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Objects;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SpiRegistry implements SpiConfigChangeListener {

    @Getter
    private final static SpiRegistry instance = new SpiRegistry();

    private WrapperGeneratorComposite composite = new WrapperGeneratorComposite();

    /**
     *
     * 根据admin 配置中心的应该配置 建立一个应用放到SpiContainer中
     * @param configDTO
     */
    private static void register(SpiConfigDTO configDTO) {
        if (Objects.isNull(configDTO)) {
            return;
        }
        Object wrapper = instance.composite.genericWrapper(configDTO);
        SpiContainer.put(configDTO.getSpiInterface(), configDTO.getBizCode(), wrapper);
    }

    /**
     * 销毁一个扩展点引用
     * @param configDTO
     */
    private static void unRegister(SpiConfigDTO configDTO) {
        if (Objects.isNull(configDTO)) {
            return;
        }
        instance.composite.destroyWrapper(configDTO);
        SpiContainer.remove(configDTO.getSpiInterface(), configDTO.getBizCode());
    }

    @Override
    public void onChange(SpiConfigChangeEvent event) {
        if(!CollectionUtils.isEmpty(event.getConfigChanges())){
            event.getConfigChanges().stream().forEach(e->{
                if(SpiChangeType.ADDED.equals(e.getChangeType())||SpiChangeType.INIT.equals(e.getChangeType()) || SpiChangeType.MODIFIED.equals(e.getChangeType())){
                    //默认实现额外注册一个 DEFAULT_BIZ_CODE
                    if(e.getConfig().getIsDefault()==1){
                        SpiConfigDTO dto=e.getConfig();
                        dto.setBizCode(Extension.DEFAULT_BIZ_CODE);
                        register(dto);
                    }
                    register(e.getConfig());
                }else if(SpiChangeType.DELETED.equals(e.getChangeType())){
                    unRegister(e.getConfig());
                }
            });
        }
    }
}
