package io.github.cmt.extension.core.router;

import io.github.cmt.extension.common.annotation.Extension;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI容器
 * 目前 如果创建了某个接口的扩展点 及时后来销毁了所有实现 也不会删除某个接口的map 即第一层map创建了不会删除
 *
 * @author yonghuang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpiContainer extends ConcurrentHashMap<String, Map<String, Object>>{
    private final static SpiContainer instance = new SpiContainer();

    public static Object get(String spiInterface, String bizCode) {
        Map<String, Object> impls = instance.get(spiInterface);
        if (impls == null) {
            return null;
        }
        if(bizCode==null||impls.get(bizCode)==null){
            return impls.get(Extension.DEFAULT_BIZ_CODE);
        }
        return impls.get(bizCode);
    }

    /**
     * @param spiInterface
     * @param bizCode
     * @param wrapper
     */
    public static void put(String spiInterface, String bizCode, Object wrapper) {
        Map<String, Object> impls = instance.get(spiInterface);
        if (impls == null) {
            impls = instance.computeIfAbsent(spiInterface, key -> new ConcurrentHashMap<>());
        }
        impls.put(bizCode, wrapper);
    }

    /**
     * 删除一个bizCode对应的实现 但是不会删除第一层的map
     *
     * @param spiInterface
     * @param bizCode
     */
    public static void remove(String spiInterface, String bizCode) {
        Map<String, Object> impls = instance.get(spiInterface);
        if (impls == null) {
            return;
        }
        impls.remove(bizCode);
    }
}
