/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lyzzcw.work.common.enums;

/**
 * @author lzy
 * @version 1.0.0
 * @description 发送编码
 */
public enum SendStatus{

    SUCCESS(0,"发送成功"),
    NOT_ONLINE(1,"对方当前不在线"),
    NOT_FIND_CHANNEL(2,"未找到对方的channel"),
    UNKONW_ERROR(9999,"未知异常");

    private final Integer code;
    private final String desc;

    SendStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code(){
        return this.code;
    }

}
