package com.gcb.learning.mybatisplus.pms.controller;


import com.gcb.learning.mybatisplus.pms.common.CommonResult;
import com.gcb.learning.mybatisplus.pms.vo.AdminLoginParam;
import com.gcb.learning.mybatisplus.ums.entity.Admin;
import com.gcb.learning.mybatisplus.ums.entity.Permission;
import com.gcb.learning.mybatisplus.ums.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author test
 * @since 2019-12-16
 */
@Slf4j
@CrossOrigin
@Api(tags = "AdminController", description = "用户管理后台")
@RestController
@RequestMapping("/ums/admin")
public class AdminController {

//    @ApiOperation(value = "用户注册接口")
//    @PostMapping(value = "register")
//    public Object register(@Valid @RequestBody AdminParam adminParam, BindingResult result){
//        Admin admin = null;
//        return new CommonResult().success(admin);
//

//        int errorCount = result.getErrorCount();
//        if(errorCount>0){
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            fieldErrors.forEach((fieldError)->{
//                String field = fieldError.getField();
//                log.debug("属性：{}，传来的值是：{}，校验出错。出错的提示消息：{}",
//                        field,fieldError.getRejectedValue(),fieldError.getDefaultMessage());
//            });
//            return new CommonResult().validateFailed(result);
//        }else {
        //return new CommonResult().success(admin);
//        }

//    }


    @Autowired
    AdminService adminService;

    // @Value("${jwt.tokenHeader}")
    private String tokenHeader = "Authorization";

    // @Value("${jwt.tokenHead}")
    private String tokenHead = "Bearer";

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult register(@RequestBody Admin umsAdminParam, BindingResult result) {
        Admin umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin == null) {
            new CommonResult().failed();
        }
        return new CommonResult().success(umsAdmin);
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@RequestBody AdminLoginParam adminLoginParam, BindingResult result) {
        String token = adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword());
        if (token == null) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        //添加 start
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        //添加 end
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new CommonResult().success(tokenMap);
    }

    @PostMapping("haha")
    public CommonResult haha(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();
        return new CommonResult().success(authentication);
    }

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @RequestMapping(value = "/permission/{adminId}", method = RequestMethod.GET)
    public CommonResult getPermissionList(@PathVariable Long adminId) {
        List<Permission> permissionList = adminService.getPermissionList(adminId);
        return new CommonResult().success(permissionList);
    }


}
