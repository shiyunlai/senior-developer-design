package org.tis.senior.module.developer.controller;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.controller.request.DeliveryProcessRequest;
import org.tis.senior.module.developer.entity.SCheck;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.entity.enums.PackTime;
import org.tis.senior.module.developer.entity.vo.CheckResultDetail;
import org.tis.senior.module.developer.service.ISCheckService;
import org.tis.senior.module.developer.service.ISDeliveryListService;
import org.tis.senior.module.developer.util.DeveloperUtils;
import org.tmatesoft.svn.core.SVNException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.dsig.XMLSignature;
import java.io.*;
import java.util.List;

/**
 * sCheck的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/27
 */
@RestController
@RequestMapping("/checks")
public class SCheckController extends BaseController<SCheck>  {

    @Autowired
    private ISCheckService sCheckService;

    @Autowired
    private ISDeliveryListService deliveryListService;

    /**
     * 核对清单
     * @param profileId
     * @param packTiming
     * @return
     */
    @RequiresRoles(value = "rct")
    @PostMapping("/profiles/{profileId}/packTiming/{packTiming}")
    public ResultVO add(@PathVariable @NotBlank(message = "环境ID不能为空") String profileId,
                        @PathVariable @NotNull(message = "打包窗口不能为空") String packTiming) throws SVNException {
        CheckResultDetail detail = sCheckService.check(profileId, PackTime.what(packTiming), getUser().getUserId());
        return ResultVO.success(detail);
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SCheck sCheck) {
        sCheckService.updateById(sCheck);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sCheckService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        CheckResultDetail detail = sCheckService.detail(id);
        return ResultVO.success("查询成功", detail);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SCheck> page) {
        return ResultVO.success("查询成功", sCheckService.selectPage(getPage(page), getCondition(page)));
    }

    /**
     * 投放申请结果处理
     * @param request
     * @return
     */
    @PostMapping("/delivery")
    @RequiresRoles("rct")
    public ResultVO process(@RequestBody @Validated DeliveryProcessRequest request) {
        sCheckService.process(request.getDeliveryGuid(), request.getResult(), request.getDesc(), getUser().getUserId());
        return ResultVO.success("处理成功");
    }

    /**
     * Excel导出清单
     *
     * @return
     */
    @PostMapping("excel")
    public ResultVO deliveryExportExcel(HttpServletResponse response,
                                        HttpServletRequest request,
                                        @PathVariable @NotBlank(message = "工作项guid不能为空") String guidWorkitem,
                                        @PathVariable @NotBlank(message = "运行环境guid不能为空") String guidProfiles) {

        SSvnAccount user = getUser();
        List<SDeliveryList> sDeliveryLists = deliveryListService.selectDeliveryListOutPutExcel(guidWorkitem,guidProfiles);

        OutputStream os = null;
        InputStream is = null;
        try {
            System.out.println();
            is = new FileInputStream("src\\main\\resources\\template\\excel.xls");

            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            for (int i=0;i < sDeliveryLists.size();i++){
                SDeliveryList sdl = sDeliveryLists.get(i);
                HSSFRow row = hssfSheet.createRow(i+2);
                row.createCell(0).setCellValue(sdl.getPartOfProject());
                row.createCell(1).setCellValue(sdl.getPatchType());
                row.createCell(2).setCellValue("*." + sdl.getPatchType());
                row.createCell(3).setCellValue(sdl.getDeployWhere());
                if("ecd".equals(sdl.getPatchType())){
                    row.createCell(4).setCellValue(sdl.getFullPath());
                }else {
                    row.createCell(4).setCellValue(DeveloperUtils.getFilePath(sdl.getFullPath()));
                }
                row.createCell(5).setCellValue("all");
                row.createCell(6).setCellValue("all");
                row.createCell(6).setCellValue(user.getUserId());
            }
            String fileName = "南京同城";
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
            os = response.getOutputStream();
            hssfWorkbook.write(os);
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(os != null){
                    os.close();
                }
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ResultVO.success("导出成功");
    }

    
}

