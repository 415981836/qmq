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

package qunar.tc.qmq.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * notk
 * ConsumerGroup用于记录主题和分组
 */


/**
 * @author yunfeng.yang
 * @since 2017/7/6
 */
@JsonDeserialize
public class ConsumerGroup {
    private final String subject;
    private final String group;

    /**
     * Json反序列化时，默认选择无参构造函数，如果没有，则报错
     * JsonCreator则是指定Json反序列化时的构造对象的方法
     * 参数前必须带上JsonProperty的注解，不然报错
     * @param subject
     * @param group
     */
    @JsonCreator
    public ConsumerGroup(@JsonProperty("subject") String subject,
                         @JsonProperty("group") String group) {
        this.subject = subject;
        this.group = group;
    }

    public String getSubject() {
        return subject;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsumerGroup that = (ConsumerGroup) o;

        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        return group != null ? group.equals(that.group) : that.group == null;
    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConsumerGroup{" +
                "subject='" + subject + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
