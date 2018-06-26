package org.tis.senior.module.developer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.dao.SDeliveryListMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.entity.enums.PatchType;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.entity.vo.SvnCommit;
import org.tis.senior.module.developer.entity.vo.SvnPath;
import org.tis.senior.module.developer.service.ISDeliveryListService;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.service.ISProjectService;
import org.tis.senior.module.developer.service.ISSvnKitService;

import java.util.ArrayList;
import java.util.List;

/**
 * sDeliveryList的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SDeliveryListServiceImpl extends ServiceImpl<SDeliveryListMapper, SDeliveryList> implements ISDeliveryListService {


    @Autowired
    private ISSvnKitService svnKitService;

    @Autowired
    private ISProjectService projectService;

    @Override
    public List<DeliveryProjectDetail> assembleDelivery() {

        //查询所有的工程
        List<SProject> spList = projectService.selectProjectAll();

        List<SvnCommit> svnCommits = svnKitService.loadSvnHistory("",0);
        List<SDeliveryList> sdList = new ArrayList<>();
        for (SvnCommit sc:svnCommits){
            for (SvnPath sp:sc.getChangePaths()){
                SDeliveryList sdl = new SDeliveryList();
                sdl.setAuthor(sc.getAuthor());
                sdl.setCommitDate(sc.getCommitDate());
                sdl.setDeliveryVersion(sc.getRevision());
                sdl.setCommitType(sp.getType());
                sdl.setFullPath(sp.getPath());
                String[] pathSplit = sp.getPath().split("/");
                sdl.setProgramName(pathSplit[pathSplit.length-1]);
                for (SProject sProject:spList){
                    if(pathSplit[3].equals("Feature") || pathSplit[3].equals("Hotfix")){
                        if(pathSplit[5].equals(sProject.getProjectName())){
                            sdl.setPartOfProject(sProject.getProjectName());
                        }
                    }else{
                        if(pathSplit[4].equals(sProject.getProjectName())){
                            sdl.setPartOfProject(sProject.getProjectName());
                        }
                    }

                }
                sdList.add(sdl);
            }
        }
        List<DeliveryProjectDetail> dpdLst = DeliveryProjectDetail.getDeliveryDetail(sdList);
        return dpdLst;
    }

    @Override
    public void addDeliveryList() {

    }


}

