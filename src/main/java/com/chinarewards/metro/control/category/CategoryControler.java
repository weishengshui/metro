package com.chinarewards.metro.control.category;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.TreeNode;
import com.chinarewards.metro.domain.category.Category;
import com.chinarewards.metro.model.common.AjaxResponseCommonVo;
import com.chinarewards.metro.service.category.ICategoryService;
import com.chinarewards.metro.validator.category.CreateCategoryValidator;

@Controller
@RequestMapping("/category")
public class CategoryControler {

	@Autowired
	private ICategoryService categoryService;

	@RequestMapping(value = "/create")
	@ResponseBody
	public void createOrUpdateCategory(HttpServletResponse response,
			@ModelAttribute Category category, BindingResult result, Model model) throws Exception{
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		System.out.println("createOrUpdateCategory parent is " + category.getParent().getId());
		System.out.println("createOrUpdateCategory id is " + category.getId());
		
		new CreateCategoryValidator(categoryService).validate(category, result);
		if(result.hasErrors()){
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo(result.getAllErrors().get(0).getDefaultMessage())));
			out.flush();
			return;
		}
		
		if(null == category.getId() || category.getId().isEmpty()){ // insert
			categoryService.addCategory(category, category.getParent().getId());
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("添加成功")));
			out.flush();
			return;
		}else{ // update
			categoryService.modifyCategory(category, category.getParent().getId());
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("修改成功")));
			out.flush();
			return;

		}
	}

	@RequestMapping("/add")
	public String add() {
		return "category/create";
	}

	@RequestMapping("/delete")
	@ResponseBody
	public void deleteCategory(HttpServletResponse response, String id, Model model) throws Exception{
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		Long childs = categoryService.countChildByParentId(id);
		System.out.println("childs is " + childs);
		if (childs > 0) {
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("只能删除叶子节点")));
			out.flush();
			return;
		}
		if(categoryService.hasMerchandiseCatagoryById(id)){//如果商品类别下关联这商品，也不能删除
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("该商品类别下有商品，不能删除")));
			out.flush();
			return;
		}
		categoryService.deleteCategoryById(id);
		out.println(CommonUtil.toJson(new AjaxResponseCommonVo("删除成功")));
		out.flush();
	}

	@RequestMapping("/maintain")
	public String maintain() {
		return "category/show";
	}

	/**
	 * 获取商品类别下商品数
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/get_category_mercCount")
	public AjaxResponseCommonVo get_category_merc(String id, Model model) {

		return new AjaxResponseCommonVo(String.valueOf(1));
	}

	/**
	 * 获得页面商品树，全部关闭
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/get_tree_nodes")
	@ResponseBody
	public List<TreeNode> getTreeNodes(String id) {

		List<TreeNode> nodes = new ArrayList<TreeNode>();
		// getAllNodes(nodes, null);
		List<Category> categories = categoryService.getChildsByParentId(id);

		if (null != categories && categories.size() > 0) {
			System.out.println("categories.size() is " + categories.size());
			for (Category catagory : categories) {
				TreeNode tn = new TreeNode(catagory.getId(),
						catagory.getName(),
						// categoryService.countChildByParentId(catagory.getId())
						// > 0 ? "closed"
						// : "open");
						"closed", "{\'displaySort\':\'"
								+ catagory.getDisplaySort() + "\'}");
				nodes.add(tn);
			}
		}
		return nodes;
	}

	/**
	 * 获得页面商品树, 叶子展开
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/get_tree_nodes2")
	@ResponseBody
	public List<TreeNode> getTreeNodes2(String id) {

		List<TreeNode> nodes = new ArrayList<TreeNode>();
		// getAllNodes(nodes, null);
		List<Category> categories = categoryService.getChildsByParentId(id);

		if (null != categories && categories.size() > 0) {
			System.out.println("categories.size() is " + categories.size());
			for (Category catagory : categories) {
				TreeNode tn = new TreeNode(
						catagory.getId(),
						catagory.getName(),
						categoryService.countChildByParentId(catagory.getId()) > 0 ? "closed"
								: "open", "{\'displaySort\':\'"
								+ catagory.getDisplaySort() + "\'}");
				nodes.add(tn);
			}
		}
		return nodes;
	}

	@RequestMapping("/get_parents")
	@ResponseBody
	public List<TreeNode> getAllParents(String id, Model model) {
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		List<Category> categories = categoryService.getAllParents(id);
		for (Category catagory : categories) {
			TreeNode tn = new TreeNode(
					catagory.getId(),
					catagory.getName(),
					categoryService.countChildByParentId(catagory.getId()) > 0 ? "closed"
							: "open", "{\'displaySort\':\'"
							+ catagory.getDisplaySort() + "\'}");
			nodes.add(tn);
		}
		return nodes;
	}

}
