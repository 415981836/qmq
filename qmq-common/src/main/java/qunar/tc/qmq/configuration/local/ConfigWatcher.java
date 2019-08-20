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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.qmq.concurrent.NamedThreadFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author keli.wang
 * @since 2018-11-27
 */
class ConfigWatcher {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigWatcher.class);
    /**
     * 这个ConfigWatcher持有的监视器
     */
    private final CopyOnWriteArrayList<Watch> watches;
    /**
     * 线程池
     */
    private final ScheduledExecutorService watcherExecutor;

    ConfigWatcher() {
        /**
         * 初始化watches
         */
        this.watches = new CopyOnWriteArrayList<>();
        /**
         * 初始化线程池
         */
        this.watcherExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("local-config-watcher"));

        start();
    }

    /**
     * 启动线程，扫描配置文件的变化
     */
    private void start() {
        watcherExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                checkAllWatches();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 对ConfigWatcher中持有的对配置文件的Watch进行加载
     */
    private void checkAllWatches() {
        for (Watch watch : watches) {
            try {
                checkWatch(watch);
            } catch (Exception e) {
                LOG.error("check config failed. config: {}", watch.getConfig(), e);
            }
        }
    }

    /**
     * 检查Config中的修改时间与Watch监视的修改时间进行对比
     * 如果不同，则更新Watch中的修改时间，并重新加载Config
     * @param watch
     */
    private void checkWatch(final Watch watch) {
        final LocalDynamicConfig config = watch.getConfig();
        final long lastModified = config.getLastModified();
        if (lastModified == watch.getLastModified()) {
            return;
        }

        watch.setLastModified(lastModified);
        config.onConfigModified();
    }

    /**
     * 添加监视器
     * @param config
     */
    void addWatch(final LocalDynamicConfig config) {
        final Watch watch = new Watch(config);
        watch.setLastModified(config.getLastModified());
        watches.add(watch);
    }

    /**
     * 内部实现类watch
     */
    private static final class Watch {
        /**
         * 配置
         */
        private final LocalDynamicConfig config;
        /**
         * 修改时间点
         */
        private volatile long lastModified;

        private Watch(final LocalDynamicConfig config) {
            this.config = config;
        }

        public LocalDynamicConfig getConfig() {
            return config;
        }

        long getLastModified() {
            return lastModified;
        }

        void setLastModified(final long lastModified) {
            this.lastModified = lastModified;
        }
    }
}
