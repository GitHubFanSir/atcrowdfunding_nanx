package com.atnanx.atcrowdfunding.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TProjectImages {
    private Integer id;

    private Integer projectid;

    private String imgurl;

    private Byte imgtype;


}