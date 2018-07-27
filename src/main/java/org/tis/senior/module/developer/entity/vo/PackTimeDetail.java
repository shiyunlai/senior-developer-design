package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.enums.OptionsPackTime;

@Data
public class PackTimeDetail {

    private OptionsPackTime isOptions;

    private String packTime;

}
