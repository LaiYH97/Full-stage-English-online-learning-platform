package com.online.college.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.AuthUserDao;
import com.online.college.domain.AuthUser;
import com.online.college.page.TailPage;
import com.online.college.service.IAuthUserService;
import com.online.college.storage.QiniuStorage;


@Service
public class AuthUserServiceImpl implements IAuthUserService{

	@Autowired
	private AuthUserDao entityDao;

	@Override
	public void createSelectivity(AuthUser entity){
		entityDao.createSelectivity(entity);
	}
	
	/**
	*根据username获取
	**/
	@Override
	public AuthUser getByUsername(String username){
		return entityDao.getByUsername(username);
	}
	
	
	
	@Override
	public AuthUser getById(Long id){
		return entityDao.getById(id);
	}
	
	/**
	*根据username和password获取
	**/
	@Override
	public AuthUser getByUsernameAndPassword(AuthUser authUser){
		return entityDao.getByUsernameAndPassword(authUser);
	}

	/**
	*获取首页推荐5个讲师
	**/
	@Override
	public List<AuthUser> queryRecomd(){
		List<AuthUser> recomdList = entityDao.queryRecomd();
		if(CollectionUtils.isNotEmpty(recomdList)){
			for(AuthUser item : recomdList){
				if(StringUtils.isNotEmpty(item.getHeader())){
					item.setHeader(QiniuStorage.getUrl(item.getHeader()));
				}
			}
		}
		return recomdList;
	}

	@Override
	public TailPage<AuthUser> queryPage(AuthUser queryEntity ,TailPage<AuthUser> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<AuthUser> items = entityDao.queryPage(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	

	@Override
	public void update(AuthUser entity){
		entityDao.update(entity);
	}

	@Override
	public void updateSelectivity(AuthUser entity){
		entityDao.updateSelectivity(entity);
	}

	@Override
	public void delete(AuthUser entity){
		entityDao.delete(entity);
	}

	@Override
	public void deleteLogic(AuthUser entity){
		entityDao.deleteLogic(entity);
	}



}

