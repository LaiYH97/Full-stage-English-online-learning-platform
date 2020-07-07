package com.online.college.ocSpringBoot;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.online.college.storage.QiniuStorage;
import com.online.college.storage.ThumbModel;
import com.online.college.util.CommonUtil;

@SpringBootTest
public class QiNiuTest extends TestCase {
	Logger log = Logger.getLogger(QiNiuTest.class);
	
	@Test
	public void testImages() {
		//测试上传图片
		byte[] buff = CommonUtil.getFileBytes(new File("D://images//1.jpg"));
		String key = QiniuStorage.uploadImage(buff);
		System.out.println("key = " + key);
		
		/*String key = "/default/all/0/bc5008d7bf244baea7aa2b98465481c4.jpeg";*/
		//测试下载图片
		String url = QiniuStorage.getUrl(key);
		System.out.println("url = " + url);
		
		//测试下载不同大小的图片
		url = QiniuStorage.getUrl(key,ThumbModel.THUMB_256);
		System.out.println("url = " + url);  
		
	}
}
