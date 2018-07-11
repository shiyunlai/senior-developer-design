package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SProfiles;

@Data
public class ProfileBranchDetailRequest {

    private SProfiles profiles;

    private SBranch branch;
}
