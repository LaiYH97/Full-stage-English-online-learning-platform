package com.online.college.ocSpringBootCms;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.online.college.domain.ConstsSiteCarousel;
import com.online.college.page.TailPage;
import com.online.college.service.IConstsSiteCarouselService;
import com.online.college.storage.QiniuStorage;
import com.online.college.storage.ThumbModel;
import com.online.college.web.JsonView;

/**
 * 轮播配置
 */
@Controller
@RequestMapping("/carousel")
public class SiteCarouselController{
	
	@Autowired
	private IConstsSiteCarouselService entityService;

	//实现分页
	@RequestMapping(value = "/queryPage")
	public String queryPage(ConstsSiteCarousel queryEntity , TailPage<ConstsSiteCarousel> page , Map<String,Object> model , HttpServletRequest request){
		model.put("curNav", "carousel");
		page.setPageSize(5);//每页5条数据
		page = entityService.queryPage(queryEntity,page);
		model.put("page",page);
		model.put("queryEntity",queryEntity);
		return "cms/carousel/pagelist";
	}

	//添加轮播配置，获得根据id获取，id不为空则进行修改或添加
	//图片不为空时为修改，否则为添加
	@RequestMapping(value = "/toMerge")
	public String toMerge(ConstsSiteCarousel entity , Map<String,Object> model , HttpServletRequest request){
		model.put("curNav", "carousel");
		
		if(entity.getId() != null){
			entity = entityService.getById(entity.getId());
			if(null != entity && StringUtils.isNotEmpty(entity.getPicture())){
				String pictureUrl = QiniuStorage.getUrl(entity.getPicture(),ThumbModel.THUMB_128);
				entity.setPicture(pictureUrl);
			}
		}
		model.put("entity",entity);
		return "cms/carousel/merge";
	}

	//提交图片和信息的添加或修改，参数为对象（数据）和图片
	@RequestMapping(value = "/doMerge")
	public String doMerge(ConstsSiteCarousel entity,@RequestParam MultipartFile pictureImg , Map<String,Object> model , HttpServletRequest request){
		String key = null;
		try {
			//如果有图片内容，则加载出图片的key保存至数据库
			if (null != pictureImg && pictureImg.getBytes().length > 0) {
				key = QiniuStorage.uploadImage(pictureImg.getBytes());
				entity.setPicture(key);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(entity.getId() == null){
			entityService.createSelectivity(entity);
		}else{
			entityService.updateSelectivity(entity);
		}
		return "redirect:/carousel/queryPage.html";
	}

	//删除操作
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(ConstsSiteCarousel entity){
		entityService.delete(entity);
		return new JsonView().toString();
	}
	
}

