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

package qunar.tc.qmq.configuration;

import java.util.ServiceLoader;

/**
 * DynamicConfig加载器
 */

/**
 * @author keli.wang
 * @since 2018-11-23
 */
public final class DynamicConfigLoader {
    // TODO(keli.wang): can we set this using config?
    private static final DynamicConfigFactory FACTORY;

    static {
        /**
         * ServiceLoader用于动态获取接口的实现类的实例（主要通过配置文件执行实现类的全限定名）
         * 配置文件在 CLASSPATH/META-INF/services 目录下
         */
        ServiceLoader<DynamicConfigFactory> factories = ServiceLoader.load(DynamicConfigFactory.class);
        DynamicConfigFactory instance = null;
        for (DynamicConfigFactory factory : factories) {
            instance = factory;
            break;
        }

        FACTORY = instance;
    }

    private DynamicConfigLoader() {
    }

    /**
     * 加载动态配置DynamicConfig
     * @param name
     * @return
     */
    public static DynamicConfig load(final String name) {
        return load(name, true);
    }

    /**
     * 加载动态配置DynamicConfig
     * @param name 文件名
     * @param failOnNotExist 不存在时是否失败
     * @return
     */
    public static DynamicConfig load(final String name, final boolean failOnNotExist) {
        return FACTORY.create(name, failOnNotExist);
    }
}
