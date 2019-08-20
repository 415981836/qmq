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

import java.util.Map;

/**
 * notk
 * 动态配置
 */

/**
 * User: zhaohuiyu Date: 12/24/12 Time: 4:12 PM
 */
public interface DynamicConfig {
    void addListener(Listener listener);

    String getString(String name);

    String getString(String name, String defaultValue);

    int getInt(String name);

    int getInt(String name, int defaultValue);

    long getLong(String name);

    long getLong(String name, long defaultValue);

    double getDouble(String name);

    /**
     * 获取double
     * @param name
     * @param defaultValue
     * @return
     */
    double getDouble(String name, double defaultValue);

    /**
     * 如果不存在，返回默认值
     * 否则返回获取的true或false
     * @param name
     * @param defaultValue
     * @return
     */
    boolean getBoolean(String name, boolean defaultValue);

    /**
     * 是否包含该配置
     * @param name
     * @return
     */
    boolean exist(String name);

    /**
     * 获取配置的Map结构
     * @return
     */
    Map<String, String> asMap();
}
