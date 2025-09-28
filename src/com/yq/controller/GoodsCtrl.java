package com.yq.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.weixin.util.SpringUtil;
import com.yq.entity.Address;
import com.yq.entity.Category;
import com.yq.entity.Coupons;
import com.yq.entity.Freight;
import com.yq.entity.Goods;
import com.yq.entity.User;
import com.yq.service.AddressService;
import com.yq.service.CategoryService;
import com.yq.service.CouponsService;
import com.yq.service.FreightService;
import com.yq.service.GoodsService;
import com.yq.service.OrderService;
import com.yq.service.UserService;
import com.yq.util.PageUtil;
import com.yq.util.StringUtil;

@Controller
@RequestMapping("/")
public class GoodsCtrl extends StringUtil {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private CategoryService categoryService;
	private Goods goods = new Goods();
	private Category category = new Category();
	@Autowired
	private CouponsService couponsService;
	private Coupons coupons = new Coupons();
	@Autowired
	private AddressService addressService;
	private Address address = new Address();
	@Autowired
	private FreightService freightService;
	private Freight freight = new Freight();
	@Autowired
	private UserService userService;
	private User user = new User();
	@Autowired
	private OrderService orderService;
	
	Map<String, Object> map = new HashMap<String, Object>();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@RequestMapping(value = "/main/goodsAddjsp.html")
	public ModelAndView addjsp() {
		ModelAndView ml = new ModelAndView();
		category.setStatus(1);
		List<Category> list = categoryService.list(category);
		ml.addObject("category", list);
		ml.setViewName("main/goods/add");
		return ml;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/page/go2Order.html")
	public ModelAndView go2Order(HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView ml = new ModelAndView();
		String openIdString = (String)session.getAttribute("oppen_id");
		goods.setStatus(1);
		goods.setType(1);
		List<Goods> list = null;
		String custId = null;
		User user = userService.getUserByOpenId(openIdString);//userService.getCustidByOpenId(openIdString);
		custId = user.getCustid();//"olSMfty-Izl7sqsQM493M4YjmEsI";此处为测试用，要去掉
		session.setAttribute("custId", custId);

		if(!com.weixin.util.StringUtil.isRealEmpty(custId)){//检测是否是内部系统客户
			list = goodsService.getBuildInMemberGoodsCar(custId);//内部系统走该路线
			if (!com.weixin.util.StringUtil.isRealEmpty(user.getLongitude())// 2、精准的定位
					&& !com.weixin.util.StringUtil.isRealEmpty(user.getLatitude())//
					&& user.getCustStatus()!=0&&user.getSalesPriceTimes()<2) {//3、新开以及重新激活;享受的优惠次数，最多只能一次
				for(Goods good : list) {
					good.setGoods_price(good.getSalesPrice1());
				}
				ml.addObject("salesOne", "TRUE");
			}
			for(Goods good : list) {
				good.setSalesPrice1(0);
			}
			ml.setViewName("page/member-goods-car");
		}else{
			list = goodsService.getGoodsCar();//散户显示散户的价格
			ml.setViewName("page/goods-car");
		}
		
		String keyString = "WX_NEW_USER:"+openIdString;//标识为新关注用户
		RedisTemplate redisTemplate =(RedisTemplate)SpringUtil.getBean("redisTemplate");
		if(redisTemplate.opsForHash().get(keyString,openIdString)!=null) {
			ml.addObject("isNewWXUser", "TRUE");
		}
		
		ml.addObject("goods", list);
		return ml;
	}
	
	@RequestMapping(value = "/page/go2NewCust.html")
	public ModelAndView go2NewCust(HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView ml = new ModelAndView();
		ml.setViewName("page/newCust");
		return ml;
	}
	
	@RequestMapping(value = "/main/goodsInsertData.html")
	@ResponseBody
	public String insertData(Goods goods) throws UnsupportedEncodingException {
		String add_time =sf.format(new Date());
//		try {
//		goods_name = new String(goods_name.getBytes("iso8859-1"),"utf-8");

		map.put("goods_name", goods.getGoods_name());
		map.put("goods_code", goods.getGoods_code());
		map.put("goods_img", goods.getGoods_img());
		map.put("goods_spe", goods.getGoods_spe());
		map.put("goods_price", goods.getGoods_price());
		map.put("goods_detail", goods.getGoods_detail());
		map.put("add_time", add_time);
		map.put("ctg_id", goods.getCtg_id());
		map.put("status", 1);
		map.put("type", 1);
		map.put("nbox", goods.getNbox());
		return goodsService.insert(map) + "";
	}

	@RequestMapping(value = "/main/goodsUpdateData.html")
	@ResponseBody
	public String updateData(Goods goods) throws UnsupportedEncodingException {
		map.put("goods_name", goods.getGoods_name());
		map.put("goods_code", goods.getGoods_code());
		map.put("goods_img", goods.getGoods_img());		
		map.put("goods_spe", goods.getGoods_spe());
		map.put("goods_price", goods.getGoods_price());
		map.put("goods_detail", goods.getGoods_detail());
		map.put("add_time", goods.getAdd_time());
		map.put("ctg_id", goods.getCtg_id());
		map.put("goods_id", goods.getGoods_id());
		map.put("type", 1);
		map.put("nbox", goods.getNbox());
		return goodsService.update(map) + "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/main/goodsUpstatus.html")
	public Object upstatus(Integer goods_id, Integer status) {
		map.put("status", status);
		map.put("goods_id", goods_id);
		return goodsService.upstatus(map) + "";
	}

	@ResponseBody
	@RequestMapping(value = "/main/goodsUpisrec.html")
	public Object upisrec(Integer goods_id, Integer is_recommend) {
		map.put("is_recommend", is_recommend);
		map.put("goods_id", goods_id);
		return goodsService.upisrec(map) + "";
	}

	@RequestMapping(value = "/main/goodsList.html")
	public ModelAndView list(Integer status,@RequestParam(defaultValue = "") String goods_name,
			@RequestParam(defaultValue = "0") Integer ctg_id,
			@RequestParam(defaultValue = "1") Integer currentPage,
			HttpServletRequest request) throws UnsupportedEncodingException {
		goods_name = java.net.URLDecoder.decode(goods_name,"utf-8") ;
		goods.setStatus(status);
		goods.setGoods_name(goods_name);
		goods.setCtg_id(ctg_id);
		goods.setType(1);
		goods.setIs_recommend(0);
		System.out.println(request.getParameter("goods_name"));
		int total = goodsService.count(goods);
		PageUtil.pager(currentPage, pagesize_1, total, request);
		goods.setPageSize(pagesize_1);
		goods.setCurrentNum(PageUtil.currentNum(currentPage, pagesize_1));
		List<Goods> list = goodsService.list(goods);
		ModelAndView ml = new ModelAndView();
		ml.addObject("goods", list);
		ml.addObject("goods_name", goods_name);
		ml.setViewName("main/goods/list");
		return ml;
	}

	@RequestMapping(value = "/main/goodsListById.html")
	public ModelAndView listById(Integer goods_id) {
		// addjsp();
		goods.setGoods_id(goods_id);
		List<Goods> list = goodsService.listById(goods);
		ModelAndView ml = new ModelAndView();
		category.setStatus(1);
		List<Category> ctg = categoryService.list(category);
		ml.addObject("category", ctg);
		ml.addObject("list", list);
		ml.setViewName("main/goods/info");
		return ml;
	}

	/**
	 * 根据商品id查询商品详情
	 * 
	 * @param goods_id
	 * @return
	 */
	@RequestMapping(value = "/page/goodsListById.html")
	public ModelAndView goodsListById(Integer goods_id) {
		goods.setGoods_id(goods_id);
		List<Goods> list = goodsService.listById(goods);
		ModelAndView ml = new ModelAndView();
		ml.addObject("list", list);
		ml.addObject("goods_id", goods_id);
		ml.setViewName("page/goods-info");
		return ml;
	}

	/**
	 * 根据商品id查询商品详情
	 * 
	 * @param goods_id
	 * @return
	 */
	@RequestMapping(value = "/page/goodsOrder.html")
	public ModelAndView goodsOrder(Integer goods_id) {
		goods.setGoods_id(goods_id);
		List<Goods> list = goodsService.listById(goods);
		list.get(0).setGoods_num(1);
		ModelAndView ml = new ModelAndView();
		ml.addObject("goods", list);
		ml.addObject("goods_id", goods_id);
		ml.setViewName("page/goods-order");
		return ml;
	}
	
	@RequestMapping(value = "/page/secGoodsList.html")
	public ModelAndView secGoodsList(String goods_name,
			@RequestParam(defaultValue = "0") Integer is_recommend,
			@RequestParam(defaultValue = "1") Integer status,
			@RequestParam(defaultValue = "0") Integer ctg_id,
			@RequestParam(defaultValue = "1") Integer currentPage,
			HttpServletRequest request) {
		try {
			goods.setType(1);
			goods.setStatus(status);
			goods.setCtg_id(ctg_id);
			goods.setGoods_name(goods_name);
			goods.setIs_recommend(is_recommend);
			List<Goods> list = goodsService.list(goods);
			ModelAndView ml = new ModelAndView();
			ml.addObject("goods", list);
			ml.setViewName("page/goods-list");
			return ml;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	


}
