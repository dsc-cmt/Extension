/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.cmt.extension.core.call.dubbo;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.cmt.extension.core.call.WrapperGenerator;
import io.github.cmt.extension.common.exception.SpiException;
import io.github.cmt.extension.common.ExtensionTypeEnum;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import io.github.cmt.extension.common.util.ApplicationContextHolder;
import io.github.cmt.extension.common.util.DubboConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

/**
 * dubbo proxy service is  use GenericService.
 *
 * @author FD
 */
@Slf4j
public class DubboWrapperGenerator implements WrapperGenerator {

    @Override
    public void preCheck() {
        if (ApplicationContextHolder.getApplicationContext() == null) {
            throw new SpiException("spring容器未初始化");
        }
    }

    @Override
    public boolean support(SpiConfigDTO configDTO) {
        return ExtensionTypeEnum.DUBBO.name().equalsIgnoreCase(configDTO.getInvokeMethod());
    }

    /**
     * 生成dubbo代理
     * @return the object
     * @throws RuntimeException
     */
    @Override
    public Object generateWrapper(SpiConfigDTO configDTO) {
        ReferenceConfig reference = buildReferenceConfig(configDTO);

        ReferenceConfigCache referenceConfigCache = ReferenceConfigCache.getCache();

        Object wrapper;

        try {
            wrapper = referenceConfigCache.get(reference);
        } catch (Exception ex) {
            log.error("生成dubbo代理失败", ex);
            referenceConfigCache.destroy(reference);
            throw new SpiException(ex.getMessage());
        }

        if (Objects.isNull(wrapper)) {
            log.error("生成dubbo代理失败");
            throw new SpiException("生成dubbo代理失败");
        }
        return wrapper;
    }

    @Override
    public void destroyWrapper( final SpiConfigDTO configDTO) {
        ReferenceConfig<GenericService> reference = buildReferenceConfig(configDTO);

        ReferenceConfigCache referenceConfigCache = ReferenceConfigCache.getCache();

        referenceConfigCache.destroy(reference);
    }


    private ReferenceConfig buildReferenceConfig(final SpiConfigDTO configDTO) {
        ReferenceConfig referenceConfig = new ReferenceConfig();

        referenceConfig.setConsumer(DubboConfigUtils.getConsumerConfig());
        referenceConfig.setApplication(DubboConfigUtils.getApplicationConfig());
        referenceConfig.setRegistries(DubboConfigUtils.getRegestries());
        referenceConfig.setInterface(configDTO.getSpiInterface());
        referenceConfig.setGroup(configDTO.getBizCode());

        Optional.ofNullable(configDTO.getExpireTime()).ifPresent(referenceConfig::setTimeout);

        return referenceConfig;
    }
}
