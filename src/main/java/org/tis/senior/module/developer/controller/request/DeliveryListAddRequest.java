package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

@Data
public class DeliveryListAddRequest {

    /**
     * 工程名
     */
    @NotBlank(message = "工程名不能为空")
    private String projectName;

    /**
     * 导出类型
     */
    @NotEmpty(message = "导出类型不能为空")
    private List<String> patchType;

    /**
     * 部署到 TODO 代码级还是工程级
     */
    @NotEmpty
    private List<String> profileWhere;

    /**
     * 投放清单
     */
    @NotEmpty
    private List<SvnPathListRequest> fileList;


    /**
     * 拼接导出类型
     *
     * @return
     */
    public String getPatchType(){
        String patchType = "";
        for (String str:this.patchType){
            if(patchType != ""){
                patchType = patchType + "," + str;
            }else{
                patchType = str;
            }
        }
        return patchType;
    }

    /**
     * 拼接部署类型
     *
     * @return
     */
    public String getProfileWhere(){
        String profileType = "";
        for (String str:this.profileWhere){
            if(profileType != ""){
                profileType = profileType + "," + str;
            }else{
                profileType = str;
            }
        }
        return profileType;
    }

}
