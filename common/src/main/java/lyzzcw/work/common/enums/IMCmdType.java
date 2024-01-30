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
 * @description 命令类型
 */
public enum IMCmdType {

    LOGIN(0,"登录"),
    HEART_BEAT(1,"心跳"),
    FORCE_LOGUT(2,"强制下线"),
    PRIVATE_MESSAGE(3,"私聊消息"),
    GROUP_MESSAGE(4,"群发消息");


    private final Integer code;

    private final String desc;

    IMCmdType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static IMCmdType fromCode(Integer code){
        for (IMCmdType typeEnum:values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }


    public Integer code(){
        return this.code;
    }

}
