package com.hansonwang99.web;

import com.hansonwang99.domain.Category;
import com.hansonwang99.domain.result.ExceptionMsg;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController{

	@Autowired
	private CategoryRepository categoryRepository;

	@Resource
	private CategoryService categoryService;

	@RequestMapping(value="/add",method= RequestMethod.POST)
	public Response addCategory(String name){
		if(StringUtils.isNotBlank(name)){
			Category category = categoryRepository.findByUserIdAndName(getUserId(), name);
			if(null != category){
				logger.info("该分类已经被创建");
				return result(ExceptionMsg.FavoritesNameUsed);
			}else{
				try {
					categoryService.saveCategory(getUserId(), 0l, name);
				} catch (Exception e) {
					logger.error("异常：",e);
					return result(ExceptionMsg.FAILED);
				}
			}
		}else{
			logger.info("收藏夹名称为空");
			return result(ExceptionMsg.FavoritesNameIsNull);
		}
		return result();
	}

	/**
	 * 获取文章分类
	 * @return
	 */
	@RequestMapping(value = "/getCategory/{userId}", method = RequestMethod.POST)
	public List<Category> getFavorites(@PathVariable("userId") Long userId) {
		List<Category> categories = null;
		try {
			Long id = getUserId();
			if(null != userId && 0 != userId){
				id = userId;
			}
			categories = categoryRepository.findByUserIdOrderByIdDesc(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getCategory failed, ", e);
		}
		logger.info("getCategory end ==" );
		return categories;
	}

}