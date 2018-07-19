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
import org.tis.senior.module.developer.entity.enums.CheckStatus;
import org.tis.senior.module.developer.entity.enums.PackTime;
import org.tis.senior.module.developer.entity.vo.CheckMergeDetail;
import org.tis.senior.module.developer.entity.vo.CheckResultDetail;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.ISCheckService;
import org.tis.senior.module.developer.service.ISDeliveryListService;
import org.tis.senior.module.developer.util.DeveloperUtils;
import org.tmatesoft.svn.core.SVNException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
    public ResultVO check(@PathVariable @NotBlank(message = "环境ID不能为空") String profileId,
                        @PathVariable @NotNull(message = "打包窗口不能为空") String packTiming) throws SVNException {
        CheckResultDetail detail = sCheckService.check(profileId, PackTime.what(packTiming), getUser().getUserId());
        return ResultVO.success(detail);
    }

    /**
     * 查看核对详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @RequiresRoles(value = "rct")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        CheckResultDetail detail = sCheckService.detail(id);
        return ResultVO.success("查询成功", detail);
    }

    /**
     * 去合并
     * @return
     */
    @GetMapping("/profiles/{profileId}/packTiming/{packTiming}")
    @RequiresRoles(value = "rct")
    public ResultVO merge(@PathVariable @NotBlank(message = "环境ID不能为空") String profileId,
                          @PathVariable @NotNull(message = "打包窗口不能为空") String packTiming) {
        List<CheckMergeDetail> detail = sCheckService.getMergeList(profileId, PackTime.what(packTiming));
        return ResultVO.success("查询成功", detail);
    }

    /**
     * 查看核对列表
     * @param page
     * @return
     */
    @PostMapping("/list")
    @RequiresRoles(value = "rct")
    public ResultVO list(@RequestBody @Validated SmartPage<SCheck> page) {
        return ResultVO.success("查询成功", sCheckService.selectPage(getPage(page), getCondition(page)));
    }

    /**
     * 投放申请结果处理
     * @param request
     * @return
     */
    @PutMapping("/delivery/{id}/result")
    @RequiresRoles("rct")
    public ResultVO process(@PathVariable @NotBlank(message = "投放申请ID不能为空！") String id,
                            @RequestBody @Validated DeliveryProcessRequest request) throws SVNException {
        sCheckService.process(id, request.getResult(), request.getDesc(), getUser().getUserId());
        return ResultVO.success("处理成功");
    }

    /**
     * 完成核对
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/{id}/status/{status}")
    @RequiresRoles("rct")
    public ResultVO changeCheckStatus(@PathVariable @NotBlank(message = "核对ID不能为空！") String id,
                                      @PathVariable @NotBlank(message = "核对状态不能为空！") String status) throws SVNException {
        sCheckService.completeCheck(id, CheckStatus.what(status));
        return ResultVO.success("操作成功！");
    }

    /**
     * Excel导出清单
     *
     * @return
     */
    @RequiresRoles(value = "rct")
    @GetMapping("/delivery/{guidDelivery}/excel")
    public ResultVO deliveryExportExcel(HttpServletResponse response,
                                        @PathVariable @NotBlank(message = "工作项guid不能为空") Integer guidDelivery) throws FileNotFoundException {

        SSvnAccount user = getUser();
        List<SDeliveryList> sDeliveryLists = deliveryListService.selectDeliveryListExcel(guidDelivery);

        OutputStream os = null;
        InputStream is = null;
        try {
            is = this.getClass().getResourceAsStream("/template/excel.xls");
//            is = new FileInputStream(ResourceUtils.getFile("classpath:template/excel.xls"));
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
            String fileName = "清单"+ new SimpleDateFormat("");
            response.setContentType("application/vnd.ms-excel;");
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
            os = response.getOutputStream();
            hssfWorkbook.write(os);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DeveloperException("导出清单异常！");
        } finally {
            try {
                if(os != null){
                    os.close();
                }
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                throw new DeveloperException("输出流或输入流为空！");
            }
        }

        return ResultVO.success("导出成功");
    }

}

