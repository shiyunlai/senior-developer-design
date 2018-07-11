package org.tis.senior.module.developer.controller.request;


import lombok.Data;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SWorkitem;

@Data
public class WorkitemBranchDetailRequest {

    private SWorkitem workitem;

    private SBranch branch;
}
