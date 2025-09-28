package com.yq.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.wechat.pay.jsapi.GanZhuJsPayLogic;
import com.yq.entity.Address;
import com.yq.entity.Area;
import com.yq.entity.Cart;
import com.yq.entity.Coupons;
import com.yq.entity.ExpressCost;
import com.yq.entity.Freight;
import com.yq.entity.Goods;
import com.yq.entity.GoodsJson;
import com.yq.entity.GoodsOther;
import com.yq.entity.Order;
import com.yq.entity.User;
import com.yq.service.AddressService;
import com.yq.service.AreaService;
import com.yq.service.CartService;
import com.yq.service.CouponsService;
import com.yq.service.ExpressCostService;
import com.yq.service.FreightService;
import com.yq.service.GoodsService;
import com.yq.service.OrderService;
import com.yq.service.UserService;
import com.yq.service.WXPayResultService;
import com.yq.util.KdniaoTrackQueryAPI;
import com.yq.util.PageUtil;
import com.yq.util.StringUtil;
import com.yq.vo.CalDataVo;
import com.yq.vo.KdniaoVo;
import com.yq.vo.OrderVO;
import com.yq.vo.RMOrderDetailVo;

import net.sf.json.JSONArray;

@Controller
@RequestMapping
public class OrderCtrl extends StringUtil {
	@Autowired
	private OrderService orderService;
	private Order order = new Order();
	@Autowired
	private CartService cartService;
	private Cart cart = new Cart();
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
	private AreaService areaService;
	private Area area = new Area();
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private WXPayResultService wxPayResultService;
	@Autowired
	private ExpressCostService expressCostService;

	private Goods goods = new Goods();
	Gson gson = new Gson();
	Map<String, Object> map = new HashMap<String, Object>();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger log = Logger.getLogger(this.getClass());

	@ResponseBody
	@RequestMapping(value = "/page/orderInsert.html")
	public String insert(String goods_id, String goods_name, String goods_img, String goods_spe, String goods_price,
			String goods_num, Float goods_total, Integer goods_total_num, Integer cps_id, String cps_name,
			@RequestParam(defaultValue = "0") Float cps_price, String addr_name, String receive, String oppen_id,
			Integer status, String note, String receiveType, String addr_tel, String addr_id,String addr_user,String salesOne, HttpSession session)
			throws UnsupportedEncodingException {
		String add_time = sdf.format(new Date());
		oppen_id = getOppen_id(session);
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Order order = new Order();
		String order_id = sd.format(new Date());
		if (StringUtils.isNotEmpty(goods_name)) {
			goods_name = java.net.URLDecoder.decode(goods_name, "utf-8");
		}
		if (StringUtils.isNotEmpty(goods_spe)) {
			goods_spe = java.net.URLDecoder.decode(goods_spe, "utf-8");
		}
		if (StringUtils.isNotEmpty(cps_name)) {
			cps_name = java.net.URLDecoder.decode(cps_name, "utf-8");
		}
		if (StringUtils.isNotEmpty(addr_name)) {
			addr_name = java.net.URLDecoder.decode(addr_name, "utf-8");
		}
		if (StringUtils.isNotEmpty(receive)) {
			receive = java.net.URLDecoder.decode(receive, "utf-8");
		}
		if (StringUtils.isNotEmpty(note)) {
			note = java.net.URLDecoder.decode(note, "utf-8");
		}
		if (StringUtils.isNotEmpty(addr_user)) {
			addr_user = java.net.URLDecoder.decode(addr_user, "utf-8");
		}

		order.setOrder_id(order_id);
		order.setGoods_id(goods_id);
		order.setGoods_name(goods_name);
		order.setGoods_img(goods_img);
		order.setGoods_spe(goods_spe);
		order.setGoods_price(goods_price);
		order.setGoods_num(goods_num);
		order.setGoods_total(goods_total);
		order.setGoods_total_num(goods_total_num);
		order.setCps_id(cps_id);
		order.setCps_name(cps_name);
		order.setAddr_name(addr_name);
		order.setCps_price(cps_price);
		order.setReceive(receive);
		order.setOppen_id(oppen_id);
		order.setAdd_time(add_time);
		order.setStatus(11);// 2020.4.5 微信订单 下单后，进入待付款（允许未审批单据可以付款），单据状态： 11【未付款待发货】
		order.setNote(note);
		order.setPhone(addr_tel);// 订单收件人电话
		order.setRecipient(addr_user);// 订单收件人
		order.setReceiveType(receiveType);
		order.setAddrId(addr_id);
		Object custId = session.getAttribute("custId");
		boolean salesOneFlag = false;
		try {
			if("TRUE".equals(salesOne)) {//首单规则
				User user = userService.getUserByOpenId(order.getOppen_id());
				if (!com.weixin.util.StringUtil.isRealEmpty(user.getCustid())//1、客服核实并转内部客户
						&&!com.weixin.util.StringUtil.isRealEmpty(user.getLongitude())// 2、精准的定位
						&& !com.weixin.util.StringUtil.isRealEmpty(user.getLatitude())//
						&& user.getCustStatus()!=0&&user.getSalesPriceTimes()<2) {//3、新开以及重新激活;享受的优惠次数，最多只能一次
					userService.updateSalesPrice1(order.getOppen_id());
					orderService.updateRMSalesPrice1(order.getOppen_id());
					salesOneFlag = true;
				}
			}
			if (orderService.insertWithRM(order, custId == null ? false : true,salesOneFlag) == 1) {
				if (goods_id.contains(",-=")) {
					String[] gids = goods_id.split(",-=");
					for (int i = 0; i < gids.length; i++) {
						map.put("goods_id", gids[i]);
						cartService.delete(map);
					}
				} else {
					map.put("goods_id", goods_id);
					session.setAttribute("cart_num", 0);
					cartService.delete(map);
				}
				if (cps_id != null && cps_id != 0) {
					map.put("status", 0);
					map.put("cps_id", cps_id);
					couponsService.upstatus(map);
				}
				return order_id;
			} else {
				return "0";
			}
		} catch (Exception e) {
			log.error(e);
			return "0";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "page/orderSucc.html")
	public ModelAndView bindingDetail(String orderNum,String returnURL,HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		ml.addObject("returnURL", returnURL);
		ml.addObject("orderNum", orderNum);
		ml.setViewName("page/orderSucc");
		return ml;
	}

	@ResponseBody
	@RequestMapping(value = "/page/orderUpdate.html")
	public Object update(String order_id, @RequestParam(defaultValue = "1") Integer status, HttpSession session) {

		// setOppen_id("111", session);//测试代码，模仿登录
		// map.put("oppen_id", getOppen_id(session));
		map.put("order_id", order_id);
		map.put("status", status);
		return orderService.upstatus(map) + "";
	}

	@ResponseBody
	@RequestMapping(value = "/page/orderSign.html")
	public Object orderSign(String orderId, HttpSession session) {
		String openId = getOppen_id(session);
		return orderService.updateSignStatus(orderId, openId) + "";
	}

	@ResponseBody
	@RequestMapping(value = "/main/orderprice.html")
	public Object orderprice(String order_id, String goods_total, HttpSession session) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String reorder_id = sf.format(new Date());
		map.put("status", 0);
		// map.put("order_id", order_id);
		// map.put("reorder_id", reorder_id);
		// orderService.upstatus(map);
		map.put("order_id", order_id);
		map.put("reorder_id", reorder_id);
		map.put("goods_total", goods_total);
		return orderService.upprice(map) + "";
	}

	@ResponseBody
	@RequestMapping(value = "/main/orderUpstatus.html")
	public Object upstatus(Integer status, String order_id, String express_dm, String express_hm, String express_name)
			throws UnsupportedEncodingException {
		if (StringUtils.isNotEmpty(express_name)) {
			express_name = java.net.URLDecoder.decode(express_name, "utf-8");
		}
		map.put("order_id", order_id);
		map.put("status", status);
		map.put("express_dm", express_dm);
		map.put("express_hm", express_hm);
		map.put("express_name", express_name);
		int rs = orderService.upstatus(map);
		// if (rs == 1) {
		// order.setOrder_id(order_id);
		// List<Order> list = orderService.listById(order);
		// if (status == -6) {
		// map.put("result", "商家已同意退款");
		// } else {
		// map.put("result", "商家已发货");
		// }
		//
		// map.put("body", list.get(0).getGoods_name().replace("-=", ""));
		// map.put("price", list.get(0).getGoods_total() + "");
		// map.put("oppen_id", list.get(0).getOppen_id());
		// // wechatPushMassage.pushMessage(map);
		// }
		return rs + "";
	}

	@ResponseBody
	@RequestMapping(value = "/main/orderDel.html")
	public Object delete(String order_id) {
		map.put("order_id", order_id);
		return orderService.delete(map) + "";
	}

	@ResponseBody
	@RequestMapping(value = "/page/orderCancel.html")
	public Object orderCancel(String order_id, HttpSession session) {
		int status = orderService.getOrderStatus(order_id);
		if(status==11||status==16) {//只能为11的状态才允许取消订单
			try {
				return orderService.cancelOrder(order_id, getOppen_id(session)) + "";
			} catch (Exception e) {
				log.error(e);
				return 0;
			}
		}
		return 0;
	}

	@RequestMapping(value = "/main/go2UnionPay.html")
	public ModelAndView go2UnionPay(String payAmount, int isYLZF,String orderId, HttpSession session) {
		String openId = getOppen_id(session);
		try {
			orderService.acceptOrder(orderId, openId);
		} catch (Exception e) {
			log.error(e);
		}
		ModelAndView ml = new ModelAndView();
		ml.addObject("payAmount", payAmount);
		ml.addObject("isYLZF", isYLZF);
		ml.setViewName("page/payImg");
		return ml;
	}

	@RequestMapping(value = "/page/orderList.html")
	public ModelAndView list(@RequestParam(defaultValue = "-2") Integer status, String oppen_id,String go2Tab, HttpSession session) {
		String openId = getOppen_id(session);
		List<OrderVO> rmOrders = orderService.listOrder(openId);// 订单头
		List<RMOrderDetailVo> rmOrderDetails = orderService.listOrderDetail(openId, null);// 订单明细
		Map<String, List<RMOrderDetailVo>> lstRMOrderDetailVoData = new HashMap<String, List<RMOrderDetailVo>>();// 微信订单系统使用

		for (RMOrderDetailVo objRMOrderDetailVo : rmOrderDetails) {
			List<RMOrderDetailVo> lstRMOrderDetailVo = lstRMOrderDetailVoData.get(objRMOrderDetailVo.getFid());
			if (lstRMOrderDetailVo == null) {
				lstRMOrderDetailVo = new ArrayList<RMOrderDetailVo>();
			}
			lstRMOrderDetailVo.add(objRMOrderDetailVo);
			lstRMOrderDetailVoData.put(objRMOrderDetailVo.getFid(), lstRMOrderDetailVo);
		}

		List<OrderVO> prePayOrders = new ArrayList<OrderVO>();// 待付款订单
//		List<OrderVO> payedOrders = new ArrayList<OrderVO>();// 已付款待发货订单
//		List<OrderVO> havedSendOrders = new ArrayList<OrderVO>();// 已发货订单
		List<OrderVO> preparedSignOrders = new ArrayList<OrderVO>();// 待签收

		for (OrderVO objOrderVO : rmOrders) {
			objOrderVO.setObjRMOrderDetailVo(lstRMOrderDetailVoData.get(objOrderVO.getFid()));

			if (objOrderVO.getStatus() > 10 && objOrderVO.getStatus() < 20) {// 待支付：11、12、13、14、15
				prePayOrders.add(objOrderVO);
			}
//			if (objOrderVO.getStatus() == 21) {//待发货：21
//				payedOrders.add(objOrderVO);
//			}
//			if (objOrderVO.getStatus() == 14||objOrderVO.getStatus() == 15||objOrderVO.getStatus() == 24) {//已发货：14、15、24
//				havedSendOrders.add(objOrderVO);
//			}
			if (objOrderVO.getSignFlag()==1) {//			if (objOrderVO.getStatus() == 14 || objOrderVO.getStatus() == 24) {// 已发货：14、24
				preparedSignOrders.add(objOrderVO);
			}
		}

		Collections.sort(prePayOrders, new Comparator<Order>() {
			@Override
			public int compare(Order u1, Order u2) {
				return u2.getAdd_time().compareTo(u1.getAdd_time());
			}
		});
//		Collections.sort(payedOrders, new Comparator<Order>() {
//			@Override
//			public int compare(Order u1, Order u2) {
//				return u2.getAdd_time().compareTo(u1.getAdd_time());
//			}
//		});
//		Collections.sort(havedSendOrders, new Comparator<Order>() {
//			@Override
//			public int compare(Order u1, Order u2) {
//				return u2.getAdd_time().compareTo(u1.getAdd_time());
//			}
//		});
		Collections.sort(preparedSignOrders, new Comparator<Order>() {
			@Override
			public int compare(Order u1, Order u2) {
				return u2.getAdd_time().compareTo(u1.getAdd_time());
			}
		});
		map.put("list", rmOrders);
		map.put("list0", prePayOrders);
//		map.put("list1", payedOrders);
//		map.put("list2", havedSendOrders);
		map.put("list3", preparedSignOrders);
		map.put("go2Tab", go2Tab);
		ModelAndView ml = new ModelAndView();
		ml.addObject("map", map);
		ml.setViewName("page/order-list");
		return ml;
	}

	@ResponseBody
	@RequestMapping(value = "/main/order.html")
	public void orderListJs(@RequestParam(value = "c", defaultValue = "1") Integer currentPage,
			@RequestParam(value = "p", defaultValue = "0") Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		int total = orderService.listJsonCount(order);
		PageUtil.pager(currentPage, pageSize, total, request);
		order.setPageSize(pageSize);
		order.setCurrentNum(PageUtil.currentNum(currentPage, pageSize));
		List<Order> list = orderService.listJson(order);
		List<GoodsOther> goList = new ArrayList<GoodsOther>();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List<GoodsJson> goodsList = new ArrayList<GoodsJson>();
				String[] gName = list.get(i).getGoods_name().split(",-=");
				String[] gNum = list.get(i).getGoods_num().split(",-=");
				String[] gPrice = list.get(i).getGoods_price().split(",-=");
				for (int m = 0; m < gName.length; m++) {
					GoodsJson gj = new GoodsJson();
					gj.setGoods_name(gName[m]);
					gj.setGoods_num(gNum[m]);
					gj.setGoods_price(gPrice[m]);
					goodsList.add(gj);
				}
				GoodsOther go = new GoodsOther();
				go.setAddr_name(list.get(i).getAddr_name());
				go.setNote(list.get(i).getNote());
				go.setAdd_time(list.get(i).getAdd_time());
				go.setGoodsList(goodsList);
				go.setTotal(total);
				goList.add(go);
			}
		}
		JSONArray json = JSONArray.fromObject(goList);
		// return gson.toJson(map);
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
	}

	@RequestMapping(value = "/main/orderList.html")
	public ModelAndView orderList(@RequestParam(defaultValue = "1") Integer currentPage,
			@RequestParam(defaultValue = "-2") Integer status, @RequestParam(defaultValue = "") String start_time,
			@RequestParam(defaultValue = "") String end_time, @RequestParam(defaultValue = "") String ctg_name,
			@RequestParam(defaultValue = "") String goods_name, @RequestParam(defaultValue = "") String addr_name,
			HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
		start_time = java.net.URLDecoder.decode(start_time, "utf-8");
		end_time = java.net.URLDecoder.decode(end_time, "utf-8");
		ctg_name = java.net.URLDecoder.decode(ctg_name, "utf-8");
		goods_name = java.net.URLDecoder.decode(goods_name, "utf-8");
		addr_name = java.net.URLDecoder.decode(addr_name, "utf-8");
		order.setOppen_id("");
		order.setStatus(status);
		order.setStart_time(start_time);
		order.setEnd_time(end_time);
		order.setCtg_name(ctg_name);
		order.setGoods_name(goods_name);
		order.setAddr_name(addr_name);
		int total = orderService.count(order);
		PageUtil.pager(currentPage, pagesize_1, total, request);
		order.setPageSize(pagesize_1);
		order.setCurrentNum(PageUtil.currentNum(currentPage, pagesize_1));
		List<Order> list = orderService.list(order); // 全部订单
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List<Order> ordList = new ArrayList<Order>();
				String[] gId = list.get(i).getGoods_id().split(",-=");
				String[] gName = list.get(i).getGoods_name().split(",-=");
				String[] gImg = list.get(i).getGoods_img().split(",-=");
				String[] gPrice = list.get(i).getGoods_price().split(",-=");
				String[] gNum = list.get(i).getGoods_num().split(",-=");

				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				map.put("ord" + i, ordList);
			}

		}
		map.put("list", list);
		ModelAndView ml = new ModelAndView();
		ml.addObject("map", map);
		ml.addObject("status", status);
		ml.addObject("start_time", start_time);
		ml.addObject("end_time", end_time);
		ml.addObject("ctg_name", ctg_name);
		ml.addObject("goods_name", goods_name);
		ml.addObject("addr_name", addr_name);
		ml.setViewName("main/order/list");
		return ml;
	}

	/**
	 * 确认付款-根据id查询订单
	 * 
	 * @param order_id
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/page/orderDetail.html")
	public ModelAndView orderDetail(final String order_id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		ModelAndView ml = new ModelAndView();
		try {
			String openId = getOppen_id(session);

			OrderVO objOrderVO = orderService.getOrderByOrderId(order_id);// 订单头
			List<RMOrderDetailVo> rmOrderDetails = orderService.listOrderDetail(order_id);// 订单明细
			objOrderVO.setObjRMOrderDetailVo(rmOrderDetails);
			Float currentPayAmount = orderService.getCurrentDatePayAmount(openId);
			currentPayAmount = (currentPayAmount == null ? 0.0f : currentPayAmount);

			if (!com.weixin.util.StringUtil.isRealEmpty(objOrderVO.getExpress_hm())) {
				KdniaoVo objKdniaoVo = KdniaoTrackQueryAPI.getZTOExpress(objOrderVO.getExpress_hm());
				objOrderVO.setObjKdniaoVo(objKdniaoVo);
			}

			boolean currentDayPayQuota = false;
			if (currentPayAmount.floatValue() + objOrderVO.getGoods_total().floatValue() <= 1000.00f) {// 当天支付不能超过一千块
				currentDayPayQuota = true;
			}

			ml.addObject("data", objOrderVO);
			ml.addObject("currentDayPayQuota", currentDayPayQuota);
			if (objOrderVO.getIspd() != 1
					&& (objOrderVO.getStatus() > 10 && objOrderVO.getStatus() < 20 && currentDayPayQuota == true)) {
				log.error("OrderCtrl orderDetail:openId："+openId+"||传入的order_id:"+order_id+"||objOrderVO.getOrder_id："+objOrderVO.getOrder_id()+"||objOrderVO.getOrderNO()："+objOrderVO.getOrderNO()+"||objOrderVO.getWxOrderNO()："+objOrderVO.getWxOrderNO());
//				new GanZhuYouPay().getPayPackage(objOrderVO, request, response, session);
				try {
					GanZhuJsPayLogic objGanZhuJsPayLogic = new GanZhuJsPayLogic();
					objGanZhuJsPayLogic.prepay(openId, objOrderVO, ml);
				} catch (Exception e) {
					log.error(e);
					throw e;
				}
			}
			ml.setViewName("page/orderDetail");
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return ml;
	}
	
	/**
	 * 确认付款-根据id查询订单
	 * 
	 * @param order_id
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/page/orderDetail2.html")
	public ModelAndView orderDetail2(final String order_id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		ModelAndView ml = new ModelAndView();
		try {
			String openId = getOppen_id(session);

			OrderVO objOrderVO = orderService.getOrderByOrderId(order_id);// 订单头
			List<RMOrderDetailVo> rmOrderDetails = orderService.listOrderDetail(order_id);// 订单明细
			objOrderVO.setObjRMOrderDetailVo(rmOrderDetails);
			Float currentPayAmount = orderService.getCurrentDatePayAmount(openId);
			currentPayAmount = (currentPayAmount == null ? 0.0f : currentPayAmount);

			if (!com.weixin.util.StringUtil.isRealEmpty(objOrderVO.getExpress_hm())) {
				KdniaoVo objKdniaoVo = KdniaoTrackQueryAPI.getZTOExpress(objOrderVO.getExpress_hm());
				objOrderVO.setObjKdniaoVo(objKdniaoVo);
			}

			boolean currentDayPayQuota = false;
			if (currentPayAmount.floatValue() + objOrderVO.getGoods_total().floatValue() <= 1000.00f) {// 当天支付不能超过一千块
				currentDayPayQuota = true;
			}

			ml.addObject("data", objOrderVO);
			ml.addObject("currentDayPayQuota", currentDayPayQuota);
			if (objOrderVO.getIspd() != 1
					&& (objOrderVO.getStatus() > 10 && objOrderVO.getStatus() < 20 && currentDayPayQuota == true)) {
				log.error("OrderCtrl orderDetail:openId："+openId+"||传入的order_id:"+order_id+"||objOrderVO.getOrder_id："+objOrderVO.getOrder_id()+"||objOrderVO.getOrderNO()："+objOrderVO.getOrderNO()+"||objOrderVO.getWxOrderNO()："+objOrderVO.getWxOrderNO());
//				new GanZhuYouPay().getPayPackage(objOrderVO, request, response, session);
				try {
					GanZhuJsPayLogic objGanZhuJsPayLogic = new GanZhuJsPayLogic();
					objGanZhuJsPayLogic.prepay(openId, objOrderVO, ml);
				} catch (Exception e) {
					log.error(e);
					throw e;
				}
			}
			ml.setViewName("page/orderDetail");
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return ml;
	}

	/**
	 * 确认付款-根据id查询订单
	 * 
	 * @param order_id
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/page/payOrder.html")
	public ModelAndView payOrder(final String order_id, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		ModelAndView ml = new ModelAndView();
		String openId = getOppen_id(session);
		OrderVO objOrderVO = new OrderVO();
		order.setOrder_id(order_id);
		order.setOppen_id(openId);
		List<Order> list = orderService.listById(order);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List<Order> ordList = new ArrayList<Order>();
				String[] gId = list.get(i).getGoods_id().split(",-=");
				String[] gName = list.get(i).getGoods_name().split(",-=");
				String[] gImg = list.get(i).getGoods_img().split(",-=");
				String[] gPrice = list.get(i).getGoods_price().split(",-=");
				String[] gNum = list.get(i).getGoods_num().split(",-=");

				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg!=null&&gImg.length>0?gImg[m]:"");
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				objOrderVO.setOrder_id(list.get(i).getOrder_id());
				objOrderVO.setAdd_time(list.get(i).getAdd_time());
				objOrderVO.setGoods_total(list.get(i).getGoods_total());
				objOrderVO.setExpress_hm(list.get(i).getExpress_hm());
				objOrderVO.setNote(list.get(i).getNote());
				objOrderVO.setGoods_total_num(list.get(i).getGoods_total_num());
				objOrderVO.setGoods_total(list.get(i).getGoods_total());
				objOrderVO.setAddr_name(list.get(i).getAddr_name());
				objOrderVO.setReceive(list.get(i).getReceive());
				objOrderVO.setRealname("用户微信自助下单");

				if (!com.weixin.util.StringUtil.isRealEmpty(objOrderVO.getExpress_hm())) {
					KdniaoVo objKdniaoVo = KdniaoTrackQueryAPI.getZTOExpress(objOrderVO.getExpress_hm());
					objOrderVO.setObjKdniaoVo(objKdniaoVo);
				}
				ml.addObject("data", objOrderVO);
			}
			if (list.get(0).getStatus() > 10 && list.get(0).getStatus() < 20) {
				log.error("OrderCtrl payOrder:openId："+openId+"||传入的order_id:"+order_id+"||objOrderVO.getOrder_id："+objOrderVO.getOrder_id()+"||objOrderVO.getOrderNO()："+objOrderVO.getOrderNO()+"||objOrderVO.getWxOrderNO()："+objOrderVO.getWxOrderNO());
				try {//new GanZhuYouPay().getPayPackage(objOrderVO, request, response, session);
					GanZhuJsPayLogic objGanZhuJsPayLogic = new GanZhuJsPayLogic();
					objGanZhuJsPayLogic.prepay(openId, objOrderVO, ml);
				} catch (Exception e) {
					log.error(e);
					throw e;
				}
				ml.setViewName("page/pay-order");
			}
//				else {
//					map.put("status", 1);
//					map.put("order_id", order_id);
//					String url = orderService.upstatus(map) == 1 ? "redirect:orderList.html" : "error";
//					ml.setViewName(url);
//				}
		} else {
			ml.addObject("error", "payOrder无待支付订单！");
			ml.setViewName("page/error");
		}
		return ml;
	}
	
	@RequestMapping(value = "/page/payNotify.html", method = RequestMethod.POST)
	public void payNotify(@RequestHeader("Wechatpay-Serial") String wechatPaySerial,
			@RequestHeader("Wechatpay-Signature") String wechatSignature,
			@RequestHeader("Wechatpay-Timestamp") String wechatTimestamp,
			@RequestHeader("Wechatpay-Nonce") String wechatpayNonce, @RequestBody String requestBody,HttpServletResponse response) throws Exception {
		new GanZhuJsPayLogic().callBack(wechatPaySerial, wechatSignature, wechatTimestamp, wechatpayNonce, requestBody,response);
	}

	@RequestMapping(value = "/page/payCallback.html", method = RequestMethod.POST)
	public void payCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("进入微信支付异步通知");
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		String resXml = "";
		BufferedReader reader = null;
		InputStream is = null;
		try {
			is = request.getInputStream();
			// 将InputStream转换成String
			reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				log.error(e);
			}
			resXml = sb.toString();
			log.info("微信支付异步通知请求包: {}" + resXml);
			resXml = this.payBack(resXml);
		} catch (Exception e) {
			log.error("微信支付回调通知失败", e);
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		} finally {
			if (!com.weixin.util.StringUtil.isRealEmpty(resXml)) {
				try {
					pw.println(resXml);
					pw.flush();
				} finally {
					pw.close();
				}
			}
			if (reader != null) {
				reader.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}

	public String payBack(String notifyData) {
		log.info("payBack() start, notifyData={}" + notifyData);
		String xmlBack = "";
		Map<String, String> notifyMap = null;
		try {
			WXPay wxpay = new WXPay(MyConfig.getInstance());

			notifyMap = WXPayUtil.xmlToMap(notifyData); // 转换成map

			wxPayResultService.insert(notifyMap);// 数据结果插入记录表

			if (wxpay.isPayResultNotifySignatureValid(notifyMap)) { // 签名正确，进行处理。
																	// 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
				String out_trade_no = notifyMap.get("out_trade_no");// 订单号

				if (out_trade_no == null) {
					log.info("微信支付回调失败订单号: {}" + notifyMap);
					xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
					return xmlBack;
				}

				String orderId = notifyMap.get("out_trade_no");
				orderService.updateZFStatus(orderId.split("\\|\\_\\|")[0], notifyMap.get("openid"),
						notifyMap.get("transaction_id"),
						new DecimalFormat("0.00").format(Double.valueOf(notifyMap.get("total_fee")) / 100));// 更改订单状态

				// 业务逻辑处理 ****************************
				log.info("微信支付回调成功订单号: {}" + notifyMap);
				xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[SUCCESS]]></return_msg>" + "</xml> ";
				return xmlBack;
			} else {
				log.error("微信支付回调通知签名错误");
				xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				return xmlBack;
			}
		} catch (Exception e) {
			log.error("微信支付回调通知失败", e);
			xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		return xmlBack;
	}

	/**
	 * 从订单列表获取订单
	 * 
	 * @param oppen_id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/page/prepareOrder.html")
	public ModelAndView prepareOrder(CalDataVo calDataVo, @RequestParam(defaultValue = "0") Integer cps_id,
			@RequestParam(defaultValue = "0") Integer addr_id, String cps_name,
			@RequestParam(defaultValue = "0") Float cps_price, HttpSession session) {
		if (calDataVo.getId() == null) {
			calDataVo = (CalDataVo) session.getAttribute("calDataVo");
		}
		ModelAndView ml = new ModelAndView();
		String oppen_id = getOppen_id(session);// String oppen_id = "olSMfty-Izl7sqsQM493M4YjmEsI";//此处为测试用，要去掉
		Float tprice = calDataVo.getCalTotalAmount().floatValue();// 总价
		List<Address> addr = new ArrayList<Address>();
		if (addr_id == 0) {
			address.setOppen_id(oppen_id);
			addr = addressService.list(address);
		} else {
			address.setAddr_id(addr_id);
			addr = addressService.listById(address);
		}

		if (cps_id != null) {
			System.out.println(cps_id);
			if (cps_id != 0) {
				coupons.setCps_id(cps_id);
				List<Coupons> cps = couponsService.listById(coupons);// 优惠券
				if (cps.size() > 0) {
					cps_price = cps.get(0).getCps_price(); // 如果优惠券大于0，统计出此优惠券价格
				}
				ml.addObject("cps", cps);
			}
		}
		tprice = (tprice * 100 - cps_price * 100) / 100; // 使用优惠券的总价

		if (addr.size() > 0) {//地址
			Address objAddress = addr.get(0);
			ExpressCost expressCost = new ExpressCost();
			expressCost.setProvinceCode(objAddress.getProvince());
			List<ExpressCost> lstExpressCost = expressCostService.list(expressCost);
			if (lstExpressCost.size() > 0) {// 通过地址计算运费
				ExpressCost objExpressCost = lstExpressCost.get(0);//从寄送的地址检索
				Double[] goodsWeights = calDataVo.getGoodsWeight();// 单一罐的重量
				Double[] goodsNums = calDataVo.getGoodsNum();// 一个商品的灌数量
				Double allGoodsWeight = 0.0;
				for(int i = 0;i<goodsWeights.length;i++) {
					Double goodsWeight = goodsWeights[i];
					Double goodsNum = goodsNums[i];
					allGoodsWeight += goodsWeight*goodsNum;//一罐的KG*灌数
				}
				allGoodsWeight = Math.ceil(allGoodsWeight);//有小数向前进一位，总的总重量
				
				Float baseWeight = objExpressCost.getBaseWeight();//基础运费
				Float continuationWeight = objExpressCost.getContinuationWeight();//叠加的运费
				
				Double expressCostAmout = baseWeight.doubleValue();
				if(allGoodsWeight>1) {
					expressCostAmout += (allGoodsWeight-1)*continuationWeight;
				}
				tprice = tprice + expressCostAmout.floatValue();
				ml.addObject("fgt_price", expressCostAmout);// 运费总价钱
			}
		}else {
			ml.addObject("fgt_price", 0);// 免运费
		}

//		List<Freight> fgt = freightService.list(freight);
//		if (fgt.size() > 0) {
//			if (tprice < fgt.get(0).getFree_price()) {
//				tprice = tprice + fgt.get(0).getFgt_price(); // 如果总价小于免邮价，则加上运费
//				ml.addObject("fgt_price", fgt.get(0).getFgt_price());
//			} else {
//				ml.addObject("fgt_price", 0);// 免运费
//			}
//		}
		 
		
		if (calDataVo.getPreference1TotalAmount() != null && calDataVo.getPreference1TotalAmount() != 0) {
			tprice = tprice - calDataVo.getPreference1TotalAmount().floatValue();
		}

		String add_time = sf.format(new Date());
		coupons.setOppen_id(oppen_id);
		coupons.setCps_level(-1);
		coupons.setCps_time(add_time);
		coupons.setStatus(1);
		List<Coupons> cps = couponsService.list(coupons); // 获取用户优惠券

		area.setStatus(1);
		area.setLevel(0);
		List<Area> areaList = areaService.list(area);

		session.setAttribute("calDataVo", calDataVo);
		ml.addObject("calDataVo", calDataVo);
		ml.addObject("tprice", tprice);
		ml.addObject("addr", addr);
		ml.addObject("tnum", calDataVo.getGoodsAmount());
		ml.addObject("cpsCount", cps.size());
		ml.addObject("areaList", areaList);
		ml.addObject("cps_id", cps_id);
		ml.addObject("addr_id", addr_id);
		ml.addObject("salesOne", calDataVo.getSalesOne());
		ml.setViewName("page/preparedOrder");
		return ml;
	}

	/**
	 * 从购物车获取订单
	 * 
	 * @param oppen_id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/page/cartOrderList.html")
	public ModelAndView cartList(@RequestParam(defaultValue = "0") Integer cps_id,
			@RequestParam(defaultValue = "0") Integer addr_id, String cps_name,
			@RequestParam(defaultValue = "0") Float cps_price, String oppen_id, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		// setOppen_id("111", session);// 测试代码，模仿登录
		oppen_id = getOppen_id(session);
		cart.setOppen_id(oppen_id);
		List<Cart> list = cartService.list(cart); // 获取订单信息
		Float tprice = cartService.goodstotalprice(cart);// 总价
		ml.addObject("price", tprice); //
		int tnum = cartService.goodstotalnum(cart);// 总数量
		if (cps_id != null) {
			System.out.println(cps_id);
			if (cps_id != 0) {
				coupons.setCps_id(cps_id);
				List<Coupons> cps = couponsService.listById(coupons);// 优惠券
				if (cps.size() > 0) {
					cps_price = cps.get(0).getCps_price(); // 如果优惠券大于0，统计出此优惠券价格
				}
				ml.addObject("cps", cps);
			}
		}
		List<Address> addr = new ArrayList<Address>();
		if (addr_id == 0) {
			address.setOppen_id(oppen_id);
			addr = addressService.list(address);
		} else {
			address.setAddr_id(addr_id);
			addr = addressService.listById(address);
		}
		tprice = (tprice * 100 - cps_price * 100) / 100; // 使用优惠券的总价
		// if(tprice<0){
		// tprice = 0F;
		// }
		List<Freight> fgt = freightService.list(freight);
		if (fgt.size() > 0) {
			if (tprice < fgt.get(0).getFree_price()) {
				tprice = tprice + fgt.get(0).getFgt_price(); // 如果总价小于免邮价，则加上运费
				ml.addObject("fgt_price", fgt.get(0).getFgt_price());
			} else {
				ml.addObject("fgt_price", 0);// 免运费
			}
		}
		String add_time = sf.format(new Date());

		coupons.setOppen_id(oppen_id);
		coupons.setCps_level(-1);
		coupons.setCps_time(add_time);
		coupons.setStatus(1);
		List<Coupons> cps = couponsService.list(coupons); // 获取用户优惠券
		// user.setOppen_id(oppen_id);
		// List<User> userList = userService.listById(user);

		area.setStatus(1);
		area.setLevel(0);
		List<Area> areaList = areaService.list(area);

		ml.addObject("goods", list);
		ml.addObject("tprice", tprice);
		System.err.println("tprice" + tprice);
		ml.addObject("addr", addr);
		ml.addObject("tnum", tnum);
		ml.addObject("cpsCount", cps.size());
		ml.addObject("cps_id", cps_id);
		ml.addObject("addr_id", addr_id);
		// ml.addObject("userList", userList);
		ml.addObject("areaList", areaList);
		ml.setViewName("page/cart-order");
		return ml;
	}

	/**
	 * 商品直接下订单
	 * 
	 * @param oppen_id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/page/goodsOrderSure.html")
	public ModelAndView goodsOrder(Integer goods_id, Integer goods_num,
			@RequestParam(defaultValue = "0") Integer cps_id, @RequestParam(defaultValue = "0") Integer addr_id,
			String cps_name, @RequestParam(defaultValue = "0") Float cps_price, String oppen_id, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		oppen_id = getOppen_id(session);
		cart.setOppen_id(oppen_id);
		goods.setGoods_id(goods_id);
		List<Goods> list = goodsService.listById(goods); // 获取订单信息
		Float goods_total = goods_num * list.get(0).getGoods_price();// 总价
		Float tprice = goods_num * list.get(0).getGoods_price();// 总价
		ml.addObject("price", tprice); //
		int tnum = cartService.goodstotalnum(cart);// 总数量

		// int tnum = goods_num;// 总数量
		if (cps_id != null) {
			System.out.println(cps_id);
			if (cps_id != 0) {
				coupons.setCps_id(cps_id);
				List<Coupons> cps = couponsService.listById(coupons);// 优惠券

				if (cps.size() > 0) {
					cps_price = cps.get(0).getCps_price(); // 如果优惠券大于0，统计出此优惠券价格
				}
				ml.addObject("cps", cps);
			}
		}
		List<Address> addr = new ArrayList<Address>();
		if (addr_id == 0) {
			address.setOppen_id(oppen_id);
			addr = addressService.list(address);
		} else {
			address.setAddr_id(addr_id);
			addr = addressService.listById(address);
		}
		tprice = (tprice * 100 - cps_price * 100) / 100; // 使用优惠券的总价
		List<Freight> fgt = freightService.list(freight);
		if (fgt.size() > 0) {
			if (tprice < fgt.get(0).getFree_price()) {
				tprice = tprice + fgt.get(0).getFgt_price(); // 如果总价小于免邮价，则加上运费
				ml.addObject("fgt_price", fgt.get(0).getFgt_price());
			} else {
				ml.addObject("fgt_price", 0);// 免运费
			}
		}
		String add_time = sf.format(new Date());
		coupons.setOppen_id(oppen_id);
		coupons.setCps_level(-1);
		coupons.setCps_time(add_time);
		coupons.setStatus(1);
		List<Coupons> cps = couponsService.list(coupons); // 获取用户优惠券
		user.setOppen_id(oppen_id);
		List<User> userList = userService.listById(user);
		area.setStatus(1);
		area.setLevel(0);
		List<Area> areaList = areaService.list(area);
		ml.addObject("goods", list);
		ml.addObject("tprice", tprice);
		ml.addObject("addr", addr);
		ml.addObject("tnum", goods_num);
		ml.addObject("cps_id", cps_id);
		ml.addObject("addr_id", addr_id);
		ml.addObject("userList", userList);
		ml.addObject("goods_id", goods_id);
		ml.addObject("goods_num", goods_num);
		ml.addObject("goods_total", goods_total);

		ml.addObject("tnum", tnum);
		ml.addObject("cpsCount", cps.size());
		// ml.addObject("userList", userList);
		ml.addObject("areaList", areaList);
		ml.setViewName("page/goods-order-sure");
		return ml;
	}

	@RequestMapping(value = "/page/order.html")
	public ModelAndView goodsOrder(String order_id) {
		ModelAndView ml = new ModelAndView();
		order.setOrder_id(order_id);
		ml.addObject("order", orderService.listById(order));
		ml.setViewName("page/express");
		return ml;
	}

	// @RequestMapping(value = "orderPay.html")
	// public void orderPay() {
	//
	// }

}
