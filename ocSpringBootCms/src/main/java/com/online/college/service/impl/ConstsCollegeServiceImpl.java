package com.online.college.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.dao.ConstsCollegeDao;
import com.online.college.domain.ConstsCollege;
import com.online.college.page.TailPage;
import com.online.college.service.IConstsCollegeService;


@Service
public class ConstsCollegeServiceImpl implements IConstsCollegeService{

	@Autowired
	private ConstsCollegeDao entityDao;

	/**
	 * 根据id获取
	 */
	@Override
	public ConstsCollege getById(Long id){
		return entityDao.getById(id);
	}
	
	/**
	 * 根据code获取
	 */
	@Override
	public ConstsCollege getByCode(String code){
		return entityDao.getByCode(code);
	}

	@Override
	public List<ConstsCollege> queryAll(ConstsCollege queryEntity){
		return entityDao.queryAll(queryEntity);
	}

	@Override
	public TailPage<ConstsCollege> queryPage(ConstsCollege queryEntity ,TailPage<ConstsCollege> page){
		Integer itemsTotalCount = entityDao.getTotalItemsCount(queryEntity);
		List<ConstsCollege> items = entityDao.queryPage(queryEntity,page);
		page.setItemsTotalCount(itemsTotalCount);
		page.setItems(items);
		return page;
	}

	@Override
	public void create(ConstsCollege entity){
		entityDao.create(entity);
	}
	
	/**
	 * 创建网校
	 */
	@Override
	public void createSelectivity(ConstsCollege entity){
		this.entityDao.createSelectivity(entity);
	}

	@Override
	public void update(ConstsCollege entity){
		entityDao.update(entity);
	}

	@Override
	public void updateSelectivity(ConstsCollege entity){
		entityDao.updateSelectivity(entity);
	}

	@Override
	public void delete(ConstsCollege entity){
		entityDao.delete(entity);
	}

	@Override
	public void deleteLogic(ConstsCollege entity){
		entityDao.deleteLogic(entity);
	}



}

