package com.polymeric.service.admin.impl;
import com.alibaba.fastjson.JSON;
import com.polymeric.aop.SysLogAnnotation;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.constants.UserConstants;
import com.polymeric.dao.system.SysMenuDao;
import com.polymeric.dao.system.SysRoleMenuDao;
import com.polymeric.dao.system.SysUserDao;
import com.polymeric.entity.system.MenuTreeEntity;
import com.polymeric.entity.system.MetaVo;
import com.polymeric.entity.system.RouterVo;
import com.polymeric.entity.system.SysMenuEntity;
import com.polymeric.entity.system.SysRoleMenuEntity;
import com.polymeric.entity.system.SysUserRoleEntity;
import com.polymeric.enums.RoleTypeEnums;
import com.polymeric.service.admin.MenuService;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.StringUtils;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@Transactional
@CrossOrigin
public class MenuServiceImpl extends BaseApiService implements MenuService {
	
	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysUserDao sysUserDao;
	

	@Override
	@SysLogAnnotation(module = "菜单管理", type = "POST", remark = "获取用户菜单")
	public ResponseBase findByUser(Integer userId) {
		List<SysUserRoleEntity> roleList = sysUserDao.findUserRoleJoin(userId);
		boolean temp = false;
		if(roleList != null && roleList.size() > 0) {
			for (int i = 0; i < roleList.size(); i++) {
				if(roleList.get(i).getRoleId().equals(RoleTypeEnums.ADMIN.getIndex())) {//管理员
					temp = true;
				}
			}
		}else {
			return setResultError(Constants.ERROR);
		}
		if(temp) {
			List<SysMenuEntity> list = sysMenuDao.findByParentId(Constants.ZERO_INT);
		    if (list != null && !list.isEmpty()) {
		    	addChildren(list);
		    }
		    return setResultSuccess(buildMenus(list));
		}else {
			List<SysMenuEntity> list = sysMenuDao.findUserParentMenu(userId, Constants.ZERO_INT);
		    if (list != null && !list.isEmpty()) {
		    	addUserChildren(list,userId);
		    }
		    return setResultSuccess(buildMenus(list),I18nUtil.getMessage("base_success"));
		}
	}

	public void addChildren(List<SysMenuEntity> list) {
	    for (SysMenuEntity menu : list) {
	        List<SysMenuEntity> children = sysMenuDao.findByParentId(menu.getMenuId());
	        if (children != null && !children.isEmpty()) {
	            menu.setChildren(children);
	            addChildren(children); // 递归调用，获取子菜单的子菜单
	        }
	    }
	}
	
	public void addUserChildren(List<SysMenuEntity> list,Integer userId) {
	    for (SysMenuEntity menu : list) {
	        List<SysMenuEntity> children = sysMenuDao.findUserParentMenu(userId,menu.getMenuId());
	        if (children != null && !children.isEmpty()) {
	            menu.setChildren(children);
	            addUserChildren(children,userId); // 递归调用，获取子菜单的子菜单
	        }
	    }
	}
	
	 public List<RouterVo> buildMenus(List<SysMenuEntity> menus)
	    {
	        List<RouterVo> routers = new LinkedList<RouterVo>();
	        for (SysMenuEntity menu : menus)
	        {
	            RouterVo router = new RouterVo();
	            router.setHidden("1".equals(menu.getVisible()));
	            router.setName(getRouteName(menu));
	            router.setPath(getRouterPath(menu));
	            router.setComponent(getComponent(menu));
	            router.setQuery(menu.getQuery());
	            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
	            List<SysMenuEntity> cMenus = menu.getChildren();
	            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
	            {
	                router.setAlwaysShow(true);
	                router.setRedirect("noRedirect");
	                router.setChildren(buildMenus(cMenus));
	            }
	            else if (isMenuFrame(menu))
	            {
	                router.setMeta(null);
	                List<RouterVo> childrenList = new ArrayList<RouterVo>();
	                RouterVo children = new RouterVo();
	                children.setPath(menu.getPath());
	                children.setComponent(menu.getComponent());
	                children.setName(StringUtils.capitalize(menu.getPath()));
	                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
	                children.setQuery(menu.getQuery());
	                childrenList.add(children);
	                router.setChildren(childrenList);
	            }
	            else if (menu.getParentId().intValue() == 0 && isInnerLink(menu))
	            {
	                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
	                router.setPath("/");
	                List<RouterVo> childrenList = new ArrayList<RouterVo>();
	                RouterVo children = new RouterVo();
	                String routerPath = innerLinkReplaceEach(menu.getPath());
	                children.setPath(routerPath);
	                children.setComponent(UserConstants.INNER_LINK);
	                children.setName(StringUtils.capitalize(routerPath));
	                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
	                childrenList.add(children);
	                router.setChildren(childrenList);
	            }
	            routers.add(router);
	        }
	        return routers;
	    }
	 
	 /**
	     * 获取路由名称
	     * 
	     * @param menu 菜单信息
	     * @return 路由名称
	     */
	    public String getRouteName(SysMenuEntity menu)
	    {
	        String routerName = StringUtils.capitalize(menu.getPath());
	        // 非外链并且是一级目录（类型为目录）
	        if (isMenuFrame(menu))
	        {
	            routerName = StringUtils.EMPTY;
	        }
	        return routerName;
	    }

	    /**
	     * 获取路由地址
	     * 
	     * @param menu 菜单信息
	     * @return 路由地址
	     */
	    public String getRouterPath(SysMenuEntity menu)
	    {
	        String routerPath = menu.getPath();
	        // 内链打开外网方式
	        if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
	        {
	            routerPath = innerLinkReplaceEach(routerPath);
	        }
	        // 非外链并且是一级目录（类型为目录）
	        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
	                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
	        {
	            routerPath = "/" + menu.getPath();
	        }
	        // 非外链并且是一级目录（类型为菜单）
	        else if (isMenuFrame(menu))
	        {
	            routerPath = "/";
	        }
	        return routerPath;
	    }
	
	    /**
	     * 获取组件信息
	     * 
	     * @param menu 菜单信息
	     * @return 组件信息
	     */
	    public String getComponent(SysMenuEntity menu)
	    {
	        String component = UserConstants.LAYOUT;
	        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
	        {
	            component = menu.getComponent();
	        }
	        else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu))
	        {
	            component = UserConstants.INNER_LINK;
	        }
	        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
	        {
	            component = UserConstants.PARENT_VIEW;
	        }
	        return component;
	    }
	
	    /**
	     * 是否为菜单内部跳转
	     * 
	     * @param menu 菜单信息
	     * @return 结果
	     */
	    public boolean isMenuFrame(SysMenuEntity menu)
	    {
	        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
	                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
	    }
	    
	    /**
	     * 内链域名特殊字符替换
	     * 
	     * @return 替换后的内链域名
	     */
	    public String innerLinkReplaceEach(String path)
	    {
	        return StringUtils.replaceEach(path, new String[] { Constants.HTTP, Constants.HTTPS, Constants.WWW, "." },
	                new String[] { "", "", "", "/" });
	    }

	    /**
	     * 是否为内链组件
	     * 
	     * @param menu 菜单信息
	     * @return 结果
	     */
	    public boolean isInnerLink(SysMenuEntity menu)
	    {
	        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
	    }
	    
	    /**
	     * 是否为parent_view组件
	     * 
	     * @param menu 菜单信息
	     * @return 结果
	     */
	    public boolean isParentView(SysMenuEntity menu)
	    {
	        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
	    }


		@Override
		@SysLogAnnotation(module = "菜单管理", type = "POST", remark = "获取所有菜单列表")
		public ResponseBase findAll(SysMenuEntity entity) {
	        List<SysMenuEntity> list = sysMenuDao.findAll(entity);
	        return setResultSuccess(list,I18nUtil.getMessage("base_success"));
		}


		@Override
		@SysLogAnnotation(module = "菜单管理", type = "POST", remark = "获取树结构菜单")
		public ResponseBase treeselect(SysMenuEntity entity) {
			entity.setParentId(Constants.ZERO_INT);
			List<SysMenuEntity> list = sysMenuDao.findTreeselect(entity);
			 if (list != null && !list.isEmpty()) {
			        addChildren(list);
			 }
			return setResultSuccess(buildMenuTree(list),I18nUtil.getMessage("base_success"));
		}
		
		/**
		 * @category 格式化菜单构建id lable格式
		 * @param list
		 * @return
		 */
		public List<MenuTreeEntity> buildMenuTree(List<SysMenuEntity> list) {
		    if (list != null && !list.isEmpty()) {
		        List<MenuTreeEntity> menuTreeList = new ArrayList<>();
		        for (SysMenuEntity menu : list) {
		            MenuTreeEntity entity = new MenuTreeEntity();
		            entity.setId(menu.getMenuId());
		            entity.setLabel(menu.getMenuName());
		            List<SysMenuEntity> clientList = menu.getChildren();
		            if (clientList != null && !clientList.isEmpty()) {
		                List<MenuTreeEntity> clientTreeList = buildMenuTree(clientList); // 递归调用构建子菜单树
		                entity.setChildren(clientTreeList);
		            }

		            menuTreeList.add(entity);
		        }
		        return menuTreeList;
		    } else {
		        return null;
		    }
		}


		@SuppressWarnings("unchecked")
		@Override
		@SysLogAnnotation(module = "菜单管理", type = "POST", remark = "获取对应角色树结构菜单")
		public ResponseBase roleMenuTreeselect(Integer roleId) {
			List<SysMenuEntity> list = sysMenuDao.findByParentId(Constants.ZERO_INT);
			 if (list != null && !list.isEmpty()) {
			        addChildren(list);
			    }
			List<MenuTreeEntity>  menuTreeList = buildMenuTree(list);
			List<Integer> checkedKeys = sysMenuDao.findRoleMenu(roleId);
			Map<String, Object> map = new HashedMap();
			map.put("checkedKeys", checkedKeys);
			map.put("menus", menuTreeList);
			return setResultSuccess(map,I18nUtil.getMessage("base_success"));
		}


		/**
		 *
		 */
		@Override
		@SysLogAnnotation(module = "菜单管理", type = "POST", remark = "新增菜单")
		public ResponseBase add(@RequestBody SysMenuEntity entity) {
			try {
				entity.setCreateTime(new Date());
				int temp = sysMenuDao.insert(entity);
				if(temp > 0) {
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}else {
					return setResultError(I18nUtil.getMessage("base_error"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		@Override
		@SysLogAnnotation(module = "菜单管理", type = "POST", remark = "获取菜单详情")
		public ResponseBase findById(Integer menuId) {
			try {
				SysMenuEntity entity = sysMenuDao.selectById(menuId);
				if(entity != null) {
					return setResultSuccess(JSON.toJSON(entity),I18nUtil.getMessage("base_success"));
				}else {
					return setResultError(I18nUtil.getMessage("base_data_null"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		@Override
		@SysLogAnnotation(module = "菜单管理", type = "POST", remark = "获取菜单详情")
		public ResponseBase update(@RequestBody SysMenuEntity entity) {
			try {
				SysMenuEntity menuEntity = sysMenuDao.selectById(entity.getMenuId());
				if(menuEntity != null) {
					int temp = sysMenuDao.updateById(entity);
					if(temp > 0) {
						return setResultSuccess(I18nUtil.getMessage("base_success"));
					}else {
						return setResultError(I18nUtil.getMessage("base_error"));
					}
				}else {
					return setResultError(I18nUtil.getMessage("base_data_null"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		@Override
		@SysLogAnnotation(module = "菜单管理", type = "POST", remark = "获取菜单详情")
		public ResponseBase delete(Integer menuId) {
			try {
				SysMenuEntity entity = sysMenuDao.selectById(menuId);
				if(entity != null) {
					//判断是否有子菜单
					List<SysMenuEntity> clientList = sysMenuDao.findByParentId(menuId);
					if(clientList != null && clientList.size() > 0) {
						return setResultError(I18nUtil.getMessage("menu_son_yes"));
					}
					//判断菜单是否已分配
					List<SysRoleMenuEntity> roleMenuList = sysRoleMenuDao.findMenuId(menuId);
					if(roleMenuList != null && clientList.size() > 0) {
						return setResultError(I18nUtil.getMessage("menu_allot_yes"));
					}
					
					int temp = sysMenuDao.deleteById(menuId);
					if(temp > 0) {
						return setResultSuccess(I18nUtil.getMessage("base_success"));
					}else {
						return setResultError(I18nUtil.getMessage("base_error"));
					}
				}else {
					return setResultError(I18nUtil.getMessage("base_data_null"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	    
}
