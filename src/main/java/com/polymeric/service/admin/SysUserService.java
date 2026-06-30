package com.polymeric.service.admin;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.system.SysUserEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/user")
@Api(value="管理用户管理",tags = "管理用户管理")
public interface SysUserService {

	@PostMapping("/findList")
	@ApiOperation(value="用户列表",notes="用户列表分页查询未注销用户",response=ResponseBase.class)
	ResponseBase findList(SysUserEntity entity);

	@PostMapping("/signUp")
	@ApiOperation(value="用户注册",notes="用户注册",response=ResponseBase.class)
	ResponseBase signUp(String user,@RequestPart(required = false) MultipartFile faceFile);

	@GetMapping("/findToken")
	@ApiOperation(value = "通过token获取用户信息",notes="通过token获取用户信息",response=ResponseBase.class)
	ResponseBase findToken(HttpServletRequest request);

    @DeleteMapping("/delUser/{userId}")
	@ApiOperation(value = "删除用户信息",notes="删除用户信息",response=ResponseBase.class)
	ResponseBase delUser(@PathVariable Integer userId);

    @PostMapping("/updateState")
	@ApiOperation(value = "改变用户账号状态",notes="改变用户账号状态",response=ResponseBase.class)
    ResponseBase updateUserState( Integer userId, Integer newState);

    @PostMapping("/updateUser")
	@ApiOperation(value = "编辑用户信息",notes="编辑用户信息",response=ResponseBase.class)
	ResponseBase updateUser( String user,@RequestPart(required = false) MultipartFile file);

	@PostMapping("/resetPwd")
	@ApiOperation(value = "修改密码",notes="修改密码",response=ResponseBase.class)
	ResponseBase resetPwd(Integer userId,String password);

	@PostMapping("/resetUserPwd")
	@ApiOperation(value = "重置密码",notes="重置密码",response=ResponseBase.class)
	ResponseBase resetUserPwd(Integer userId,String password);

	@PostMapping("/verifyPwd")
	@ApiOperation(value = "校验账号密码",notes="校验账号密码",response=ResponseBase.class)
	ResponseBase verifyPwd(Integer userId,String password);
	
	@PostMapping("/updateDeviceModel")
	@ApiOperation(value = "更新用户设备信息",notes="更新用户设备信息",response=ResponseBase.class)
    ResponseBase updateDeviceModel(SysUserEntity entity);
	
	@GetMapping("/upGoogleSecretkey")
	@ApiOperation(value = "修改谷歌验证密钥",notes="修改谷歌验证密钥",response=ResponseBase.class)
    ResponseBase upGoogleSecretkey(Integer userId,String googleSecretkey);
	
	@GetMapping("/IssueGoogleSecretkey")
	@ApiOperation(value = "签发谷歌验证密钥",notes="签发谷歌验证密钥",response=ResponseBase.class)
    ResponseBase IssueGoogleSecretkey(String userName);

	@GetMapping("/getPasswordOrKey")
	@ApiOperation(value = "签发谷歌验证密钥、默认密码",notes="签发谷歌验证密钥、默认密码",response=ResponseBase.class)
    ResponseBase getPasswordOrKey();

}
