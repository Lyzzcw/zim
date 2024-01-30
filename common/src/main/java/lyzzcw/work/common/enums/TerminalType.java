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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @version 1.0.0
 * @description 终端类型
 */
public enum TerminalType {

    WEB(0,"web"),
    APP(1,"app");

    private final Integer code;

    private final String desc;

    TerminalType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TerminalType fromCode(Integer code){
        for (TerminalType typeEnum:values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

    public static List<Integer> codes(){
        return Arrays.stream(values()).map(TerminalType::code).collect(Collectors.toList());
    }

    public Integer code(){
        return this.code;
    }
}
