/*
 * Copyright 2018 Qunar, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package qunar.tc.qmq.configuration.local;

import qunar.tc.qmq.configuration.DynamicConfig;
import qunar.tc.qmq.configuration.DynamicConfigFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * notk
 * DynamicConfig工厂类的实现
 */

/**
 * @author keli.wang
 * @since 2018-11-27
 */
public class LocalDynamicConfigFactory implements DynamicConfigFactory {
    /**
     * 该工厂的配置监视器
     */
    private final ConfigWatcher watcher = new ConfigWatcher();
    /**
     * 对配置的缓存处理
     * key：配置名
     * value：配置实例
     */
    private final ConcurrentMap<String, LocalDynamicConfig> configs = new ConcurrentHashMap<>();

    /**
     * 创建配置。内部进行缓存处理
     * @param name
     * @param failOnNotExist
     * @return
     */
    @Override
    public DynamicConfig create(final String name, final boolean failOnNotExist) {
        /**
         * 如果有已加载的缓存，则取缓存中的数据
         */
        if (configs.containsKey(name)) {
            return configs.get(name);
        }
        /**
         * 无缓存，则直接初始化
         */
        return doCreate(name, failOnNotExist);
    }

    private LocalDynamicConfig doCreate(final String name, final boolean failOnNotExist) {
        /**
         * 返回了新建的配置项
         */
        final LocalDynamicConfig prev = configs.putIfAbsent(name, new LocalDynamicConfig(name, failOnNotExist));
        /**
         * 根据名称获取配置项
         */
        final LocalDynamicConfig config = configs.get(name);
        /**
         * 表示原来没有这个config，所以需要加载Config
         */
        if (prev == null) {
            watcher.addWatch(config);
            config.onConfigModified();
        }
        return config;
    }
}
