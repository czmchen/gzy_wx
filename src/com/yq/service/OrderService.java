package com.yq.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weixin.constants.CommonConstants;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.DateUtils;
import com.weixin.util.SpringUtil;
import com.weixin.util.StringUtil;
import com.yq.dao.OrderDao;
import com.yq.entity.Address;
import com.yq.entity.Coupons;
import com.yq.entity.Distribution;
import com.yq.entity.ExpressCost;
import com.yq.entity.Goods;
import com.yq.entity.Order;
import com.yq.vo.OrderVO;
import com.yq.vo.RMOrderDetailVo;

@Service
public class OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private CouponsService couponsService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ExpressCostService expressCostService;
	@Autowired
	DistributionService distributionService;
	@Autowired
	private UserService userService;

	private final static Logger logger = Logger.getLogger(OrderService.class);

	public int insert(Order order) {
		return orderDao.insert(order);
	}
	
	
	public Order freeMile(String openIdString) throws Exception {
		String freeGoodCodeString = "-1000";
		String[] gids = {freeGoodCodeString};
		String[] gnums = {"1"};
		Order order = new Order();
		order.setOrder_id(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		order.setOppen_id(openIdString);
		order.setAdd_time(DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
		order.setStatus(11);// 2020.4.5 微信订单 下单后，进入待付款（允许未审批单据可以付款），单据状态： 11【未付款待发货】
		float goods_total = 0.00f;// 重置总价为0.00；
		Float nboxTotal = 0.00f;
		Goods goodSearchs = new Goods();
		goodSearchs.setGoods_id(-1000);
				
		List<Goods> list = goodsService.listById(goodSearchs); // 获取订单信息
		Map<Integer, Goods> mapGoods = new HashMap<Integer, Goods>();
		for (Goods goods : list) {
			goods.setDef3(1);// def3作为灌数
			mapGoods.put(goods.getGoods_id(), goods);
		}

		Address queryAddress = new Address();
		queryAddress.setOppen_id(openIdString);
		queryAddress.setIsCustomerRegist(1);
		Address objAddress = addressService.getAddressById(queryAddress);
		ExpressCost expressCost = new ExpressCost();
		
		expressCost.setProvinceCode(objAddress.getProvince());
		List<ExpressCost> lstExpressCost = expressCostService.list(expressCost);
		if (lstExpressCost.size() > 0) {// 通过地址计算运费
			ExpressCost objExpressCost = lstExpressCost.get(0);// 从寄送的地址检索
			Float baseWeight = objExpressCost.getBaseWeight();// 基础运费

			Double expressCostAmout = baseWeight.doubleValue();
			Float expressCostAmoutFloat = expressCostAmout.floatValue();
			goods_total = (goods_total + expressCostAmoutFloat);// 加上运费的钱
			order.setExpressCost(expressCostAmoutFloat);
		}
		order.setAddrId(String.valueOf(objAddress.getAddr_id()));

		order.setGoods_total(goods_total);
		order.setOrgAmount(goods_total);

//		order.setGoods_total(0.0f);
		order.setPreference1(0.0f);

		order.setNboxTotal(nboxTotal);
		order.setGoods_price("0");
		order.setGoods_code(freeGoodCodeString);
		order.setGoods_total_num(0);
		order.setNbox(String.valueOf(mapGoods.get(Integer.valueOf(freeGoodCodeString)).getNbox()));
		order.setWholesalePrice(String.valueOf(mapGoods.get(Integer.valueOf(freeGoodCodeString)).getWholesalePrice()));
		String fid = UUID.randomUUID().toString().replace("-", "");
		this.insertIN2Order(C3P0DBManager.getConnection(),order, mapGoods, gids, gnums, fid);//插入到远程数据库主表
		this.insertIN2OrderDetail(C3P0DBManager.getConnection(),order, mapGoods, gids, gnums, fid, false);//插入到远程数据库明细表
		this.insertIN2Order(((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection(),order, mapGoods, gids, gnums, fid);//插入到本地数据库主表
		this.insertIN2OrderDetail(((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection(),order, mapGoods, gids, gnums, fid, false);//插入到本地数据库明细表
		orderDao.insert(order);
		return order;
	}

	public int updateRMSalesPrice1(String openId) throws RuntimeException {
		int succss = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			StringBuffer sql = new StringBuffer("update WxUserCust set custStatus=0,salesPriceTimes = (salesPriceTimes+1) where openid = ?");
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, openId);
			ps.executeUpdate();
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succss;
	}
	
	public int insertWithRM(Order order, boolean isNumberFlag,boolean salesOneFlag) throws Exception {
		String[] gids = order.getGoods_id().split(",-=");
		String[] gnums = order.getGoods_num().split(",-=");
		for (String nums : gnums) {
			if (Integer.valueOf(nums) < 0) {
				return -1;
			}
		}
		
		float goods_total = 0.00f;// 重置总价为0.00；
		Float goods_total_amount = 0.00f;
		Float nboxTotal = 0.00f;
		Float cps_price = 0.00f;// 重置总价为0.00；
		StringBuffer sbfGoodsPrice = new StringBuffer();
		StringBuffer sbfGoodsCode = new StringBuffer();
		StringBuffer sbfGoodsNbox = new StringBuffer();
		StringBuffer sbfwholesalePrice = new StringBuffer();

		List<Goods> list = goodsService.listByIds(gids); // 获取订单信息
		if(salesOneFlag) {//首单规则
			for(Goods good : list) {
				good.setWholesalePrice(good.getSalesPrice1());
			}
		}
		
		Map<Integer, Goods> mapGoods = new HashMap<Integer, Goods>();
		for (Goods goods : list) {
			mapGoods.put(goods.getGoods_id(), goods);
		}

		List<Goods> lstPreference1GoodBox = new ArrayList<Goods>();
		int preference1MinNboxNum = 0;
		Float allGoodsWeight = 0.0f;// 总重量
		for (int i = 0; i < gids.length; i++) {
			Goods goods = mapGoods.get(Integer.valueOf(gids[i]));
			Float gum = Float.valueOf(gnums[i]);
			goods.setDef3(gum);// def3作为灌数
			Float goodsWeight = goods.getGoods_weight();// 重量
			allGoodsWeight += goodsWeight * gum;
			goods_total += gum * (isNumberFlag == true ? goods.getWholesalePrice() : goods.getGoods_price());// 订单内的商品与数量的总价
			goods_total_amount += gum;
			if (i != 0) {
				sbfGoodsPrice.append(",-=");
				sbfGoodsCode.append(",-=");
				sbfGoodsNbox.append(",-=");
				sbfwholesalePrice.append(",-=");
			}
			sbfGoodsPrice.append(goods.getGoods_price());
			sbfGoodsCode.append(goods.getGoods_code());
			sbfGoodsNbox.append(goods.getNbox());
			sbfwholesalePrice.append(goods.getWholesalePrice());
			nboxTotal += (float) gum / goods.getNbox();
			if (isNumberFlag == true && goods.getPreference1() != null && goods.getPreference1() == 1) {// 会员享受组合优惠
				lstPreference1GoodBox.add(goods);
			}
			if (isNumberFlag == true && goods.getPreference1() != null && goods.getPreference1() == 2) {// 会员享受组合优惠
				preference1MinNboxNum = preference1MinNboxNum
						+ (Integer.valueOf(gnums[i]) / Integer.valueOf(goods.getNbox()));// 统计有多少件小的
			}
		}

		Integer cps_id = order.getCps_id();
		if (cps_id != null) {
			if (order.getCps_id() != 0) {
				Coupons coupons = new Coupons();
				coupons.setCps_id(cps_id);
				List<Coupons> cps = couponsService.listById(coupons);// 优惠券
				if (cps.size() > 0) {
					cps_price = cps.get(0).getCps_price(); // 如果优惠券大于0，统计出此优惠券价格
				}
			}
		}
		goods_total = (goods_total * 100 - cps_price * 100) / 100; // 使用优惠券的总价

		if ("0".equals(order.getReceiveType())||"1".equals(order.getReceiveType())) {// 该选项为选择为快递或者联系客服
			List<Address> addr = new ArrayList<Address>();
			Address address = new Address();
			String addrId = order.getAddrId();
			if ("0".equals(order.getAddrId())) {// 默认地址
				address.setOppen_id(order.getOppen_id());
				addr = addressService.list(address);
			} else {// 重选的地址
				address.setAddr_id(Integer.parseInt(addrId));
				addr = addressService.listById(address);
			}

			Address objAddress = addr.get(0);// 地址
			ExpressCost expressCost = new ExpressCost();
			expressCost.setProvinceCode(objAddress.getProvince());
			List<ExpressCost> lstExpressCost = expressCostService.list(expressCost);
			if (lstExpressCost.size() > 0) {// 通过地址计算运费
				ExpressCost objExpressCost = lstExpressCost.get(0);// 从寄送的地址检索
				Double allGoodsWeightDouble = Math.ceil(allGoodsWeight.doubleValue());// 有小数向前进一位，总的总重量

				Float baseWeight = objExpressCost.getBaseWeight();// 基础运费
				Float continuationWeight = objExpressCost.getContinuationWeight();// 叠加的运费

				Double expressCostAmout = baseWeight.doubleValue();
				if (allGoodsWeightDouble > 1) {
					expressCostAmout += (allGoodsWeightDouble - 1) * continuationWeight;
				}
				Float expressCostAmoutFloat = expressCostAmout.floatValue();
				goods_total = goods_total + expressCostAmoutFloat;// 加上运费的钱
				order.setExpressCost(expressCostAmoutFloat);
			}
			order.setAddrId(String.valueOf(objAddress.getAddr_id()));
		}

//		List<Freight> fgt = freightService.list(new Freight());
//		if (fgt.size() > 0) {
//			if (goods_total < fgt.get(0).getFree_price()) {
//				goods_total = goods_total + fgt.get(0).getFgt_price(); // 如果总价小于免邮价，则加上运费
//				order.setReceiveCost(fgt.get(0).getFgt_price());
//			}
//		}

		order.setGoods_total(goods_total);
		order.setOrgAmount(goods_total);

		if (isNumberFlag == true && preference1MinNboxNum > 0 && lstPreference1GoodBox.size() > 0) {
			Collections.sort(lstPreference1GoodBox, new Comparator<Goods>() {// 执行组合套优惠开始
				@Override
				public int compare(Goods u1, Goods u2) {
					int diff = u2.getNbox() - u1.getNbox();
					if (diff > 0) {
						return 1;
					} else if (diff < 0) {
						return -1;
					}
					return 0; // 相等为0
				}
			}); // 按年龄排序

			int couponNbox_ = 0;
			int couponNbox = 0;
			int maxCoupon = 2;
			float preference1TotalAmount = 0.0F;
			float maxCouponPrice = 30.0F;
			float maxCouponPrice_ = 0.0F;
			float oneCouponPrice = 2.5F;
			for (int k = 0; k < lstPreference1GoodBox.size(); k++) {
				if(k==maxCoupon) {
					break;
				}
				Goods good = lstPreference1GoodBox.get(k);
				float willMinAmount = 0.0F;
				float orgGoodAmount = good.getWholesalePrice() * good.getDef3();
				int nboxNum = (int) (good.getDef3() / good.getNbox().intValue());
				if (nboxNum >= maxCoupon) {
					willMinAmount = (maxCoupon * good.getNbox().intValue()) * oneCouponPrice;
					if (willMinAmount > maxCouponPrice)
						willMinAmount = maxCouponPrice;
					preference1TotalAmount = willMinAmount;
					good.setDef1((orgGoodAmount - willMinAmount) / good.getDef3());
					good.setDef2(willMinAmount);
					break;
				}
				couponNbox = maxCoupon - couponNbox_;
				maxCouponPrice_ = maxCouponPrice - preference1TotalAmount;
				if (nboxNum < couponNbox) {
					couponNbox_ += nboxNum;
					willMinAmount = (nboxNum * good.getNbox().intValue()) * oneCouponPrice;
					if (willMinAmount >= maxCouponPrice_) {
						willMinAmount = maxCouponPrice_;
						preference1TotalAmount += willMinAmount;
						good.setDef1((orgGoodAmount - willMinAmount) / good.getDef3());
						good.setDef2(willMinAmount);
						break;
					}else {
						preference1TotalAmount += willMinAmount;
						good.setDef1((orgGoodAmount - willMinAmount) / good.getDef3());
						good.setDef2(willMinAmount);
					}
				}
				if (nboxNum >= couponNbox) {
					willMinAmount = (couponNbox * good.getNbox().intValue()) * oneCouponPrice;
					if (willMinAmount >= maxCouponPrice_) {
						willMinAmount = maxCouponPrice_;
						preference1TotalAmount += willMinAmount;
						good.setDef1((orgGoodAmount - willMinAmount) / good.getDef3());
						good.setDef2(willMinAmount);
						break;
					}
					preference1TotalAmount += willMinAmount;
					good.setDef1((orgGoodAmount - willMinAmount) / good.getDef3());
					good.setDef2(willMinAmount);
				}
			}
			order.setGoods_total(goods_total - preference1TotalAmount);
			order.setPreference1(preference1TotalAmount);
		}

		order.setNboxTotal(nboxTotal);
		order.setGoods_price(sbfGoodsPrice.toString());
		order.setGoods_code(sbfGoodsCode.toString());
		order.setGoods_total_num(goods_total_amount.intValue());
		order.setNbox(sbfGoodsNbox.toString());
		order.setWholesalePrice(sbfwholesalePrice.toString());
		String fid = UUID.randomUUID().toString().replace("-", "");
		this.insertIN2Order(C3P0DBManager.getConnection(),order, mapGoods, gids, gnums, fid);//插入到远程数据库主表
		this.insertIN2OrderDetail(C3P0DBManager.getConnection(),order, mapGoods, gids, gnums, fid, isNumberFlag);//插入到远程数据库明细表
		this.insertIN2Order(((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection(),order, mapGoods, gids, gnums, fid);//插入到本地数据库主表
		this.insertIN2OrderDetail(((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection(),order, mapGoods, gids, gnums, fid, isNumberFlag);//插入到本地数据库明细表
		return orderDao.insert(order);
	}
	
	private int insertIN2Order(Connection conn,Order order, Map<Integer, Goods> mapGoods, String[] gids, String[] gnums, String fid)
			throws RuntimeException {
		int succss = 0;
		PreparedStatement ps = null;
		try {
			Map<String, String> userData = this.getRMUserBy(order.getOppen_id());
			ps = conn.prepareStatement(
					"insert into WxOrder (wxOrderNO,CustWXnum,creater,creatdate,amount,IsVerified,ordAddress,Note,receive,receiveType,fid,orderStatus,qty,receiveCost,CustID,CustMC,nqty,issend,orgAmount,preference1,ordphone,ordRecipient,addrId,expressCost,isSync,signFlag) values (?,?,?,?,?,?,?,?,?,?,?,11,?,?,'"
							+ userData.get("Custid") + "','" + userData.get("CustName") + "',?,1,?,?,?,?,?,?,0,1)");
			ps.setObject(1, order.getOrder_id());
			ps.setObject(2, order.getOppen_id());
			ps.setObject(3, "微信订单");
			ps.setObject(4, order.getAdd_time());
			ps.setFloat(5, order.getGoods_total());
			ps.setInt(6, 0);
			ps.setObject(7, order.getAddr_name());
			ps.setObject(8, order.getNote());
			ps.setObject(9, order.getReceive());
			ps.setObject(10, order.getReceiveType());
			ps.setObject(11, fid);
			ps.setObject(12, order.getGoods_total_num());
			ps.setObject(13, order.getReceiveCost());
			ps.setObject(14, order.getNboxTotal());
			ps.setFloat(15, order.getOrgAmount());
			ps.setFloat(16, order.getPreference1() == null ? 0.00f : order.getPreference1());
			ps.setObject(17, order.getPhone());
			ps.setObject(18, order.getRecipient());
			ps.setObject(19, order.getAddrId());
			ps.setFloat(20, order.getExpressCost() == null ? 0.00f : order.getExpressCost());
			ps.executeUpdate();
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succss;
	}

	private int insertIN2OrderDetail(Connection conn,Order order, Map<Integer, Goods> mapGoods, String[] gids, String[] gnums, String fid,
			boolean isNumberFlag) throws RuntimeException {
		int succss = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into WxOrderDetail (fid,InvID,InvName,Qty,NQty,Price,Amount,goodsQty,orgPrice,orgAmount,preference1,isSync) values (?,?,?,?,?,?,?,?,?,?,?,0)");
			for (int i = 0; i < gids.length; i++) {
				Goods goods = mapGoods.get(Integer.valueOf(gids[i]));
				ps.setObject(1, fid);
				ps.setObject(2, goods.getGoods_code());
				ps.setObject(3, goods.getGoods_name());
				ps.setObject(4, Float.valueOf(gnums[i]));
				ps.setObject(5, Float.valueOf(gnums[i]) / Float.valueOf(goods.getNbox()));
				ps.setObject(6,isNumberFlag == true ? (goods.getDef1() != 0.0f ? goods.getDef1() : goods.getWholesalePrice()): goods.getGoods_price());
				ps.setFloat(7,isNumberFlag == true? Float.valueOf(gnums[i])* (goods.getDef1() != 0.0f ? goods.getDef1() : goods.getWholesalePrice()): Float.valueOf(gnums[i]) * goods.getGoods_price());
				ps.setObject(8, goods.getNbox());
				ps.setFloat(9, isNumberFlag == true ? goods.getWholesalePrice() : goods.getGoods_price());
				ps.setFloat(10, isNumberFlag == true ? Float.valueOf(gnums[i]) * goods.getWholesalePrice(): Float.valueOf(gnums[i]) * goods.getGoods_price());
				ps.setFloat(11, goods.getDef2());
				ps.addBatch();
			}
			ps.executeBatch();
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succss;
	}

	private int updateRmOrderPay(String orderId, String openId, String zfTransactionId, String zfamount)
			throws RuntimeException {
		int succss = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			List<String> need2UpdateOpenId = getOrderPayList(orderId);
			StringBuffer sql = new StringBuffer("update WxOrder set Iszf=1,zfdate=GETDATE(),zfamount = ?,");
			sql.append("orderStatus=(CASE orderStatus WHEN 11 THEN 21 WHEN 12 THEN 22 WHEN 13 THEN 23 WHEN 14 THEN 24  WHEN 15 THEN 10 ELSE orderStatus END),zfTransactionId=? ");
			sql.append("where (wxOrderNO = ? or OrderNO = ?) and CustWXnum = ?");
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			for(String updatepenId : need2UpdateOpenId) {
				ps.setObject(1, zfamount);
				ps.setObject(2, zfTransactionId);
				ps.setObject(3, orderId);
				ps.setObject(4, orderId);
				ps.setObject(5, updatepenId);
				ps.addBatch();
			}
			ps.executeBatch();
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succss;
	}
	
	
	private int updateLocalOrderPay(String orderId, String openId, String zfTransactionId, String zfamount)
			throws RuntimeException {
		int succss = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			List<String> need2UpdateOpenId = getOrderPayList(orderId);
			StringBuffer sql = new StringBuffer("update WxOrder set Iszf=1,zfdate=now(),zfamount = ?,");
			sql.append("orderStatus=(CASE orderStatus WHEN 11 THEN 21 WHEN 12 THEN 22 WHEN 13 THEN 23 WHEN 14 THEN 24  WHEN 15 THEN 10 ELSE orderStatus END),zfTransactionId=? ");
			sql.append("where (wxOrderNO = ? or OrderNO = ?) and CustWXnum = ?");
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(sql.toString());
			for(String updatepenId : need2UpdateOpenId) {
				ps.setObject(1, zfamount);
				ps.setObject(2, zfTransactionId);
				ps.setObject(3, orderId);
				ps.setObject(4, orderId);
				ps.setObject(5, updatepenId);
				ps.addBatch();
			}
			ps.executeBatch();
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succss;
	}
	
	
	public List<String> getOrderPayList(String orderId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> lstData = new ArrayList<String>();
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(
					"select t.CustWXnum from WxOrder t where (t.wxOrderNO = ? or t.OrderNO = ?)");
			ps.setString(1, orderId);
			ps.setString(2, orderId);
			rs = ps.executeQuery();
			while (rs.next()) {
				lstData.add(rs.getString(1));
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return lstData;
	}

	private int updateRmOrderSign(String orderId, String openId) throws RuntimeException {
		int succss = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			StringBuffer sql = new StringBuffer("update WxOrder set ");
			sql.append("signFlag=2,signDatetime=GETDATE(),signSendMsg= (ISNULL(signSendMsg,'')+'").append("状态更改为2（已签收），签收操作人openId：").append(openId).append("发送时间:").append(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)).append("/r/n')");
			sql.append("where (wxOrderNO = ? or OrderNO = ?)");
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setObject(1, orderId);
			ps.setObject(2, orderId);
			ps.executeUpdate();
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succss;
	}
	
	private int updateLocalOrderSign(String orderId, String openId) throws RuntimeException {
		int succss = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			StringBuffer sql = new StringBuffer("update WxOrder set ");
			sql.append("signFlag=2,signDatetime=now(),signSendMsg= CONCAT(ifnull(signSendMsg,''),'").append("状态更改为2（已签收），签收操作人openId：").append(openId).append("发送时间:").append(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)).append("/r/n')");
			sql.append("where (wxOrderNO = ? or OrderNO = ?)");
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setObject(1, orderId);
			ps.setObject(2, orderId);
			ps.executeUpdate();
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succss;
	}
	
	
	private Map<String,String> getOrderNumFull(String orderId, String openId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> orderDataMap = new HashMap<String, String>();
		try {
			StringBuffer sql = new StringBuffer("select t.wxOrderNO,t.OrderNO from WxOrder t where (t.wxOrderNO = ? or t.OrderNO = ?) and t.CustWXnum = ?");
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setObject(1, orderId);
			ps.setObject(2, orderId);
			ps.setObject(3, openId);
			rs = ps.executeQuery();
			while (rs.next()) {
				orderDataMap.put("wxOrderNO", rs.getString("wxOrderNO"));
				orderDataMap.put("OrderNO", rs.getString("OrderNO"));
			}
			return orderDataMap;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
	}

	private int cancleOrder(Connection conn,String orderId, String openId,String orderStatus) throws RuntimeException {
		int succss = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update WxOrder set orderStatus="+orderStatus+" where (wxOrderNO = ? or OrderNO = ?) and CustWXnum = ?");
			ps.setObject(1, orderId);
			ps.setObject(2, orderId);
			ps.setObject(3, openId);
			ps.executeUpdate();
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succss;
	}

	public int cancelOrder(String orderId, String openId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderStatus = "-10";
		map.put("order_id", orderId);
		map.put("oppen_id", openId);
		map.put("status", orderStatus);
		cancleOrder(C3P0DBManager.getConnection(),orderId, openId,orderStatus);// 更新状态为取消
		cancleOrder(((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection(),orderId, openId,orderStatus);// 更新状态为取消
		return orderDao.upstatusByMap(map);// 更新本地的状态为取消
	}
	
	public boolean getAcceptStatusSend(String orderId, String openId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement("select acceptStatusSend,orderStatus from WxOrder t where (t.wxOrderNO = ? or t.OrderNO = ?) and t.CustWXnum = ?");
			ps.setString(1, orderId);
			ps.setString(2, orderId);
			ps.setString(3, openId);
			rs = ps.executeQuery();
			while (rs.next()) {
				if(rs.getInt("acceptStatusSend")>0||rs.getInt("orderStatus")!=11) {//状态改变不能大于1次，超过1次则不允许再取消；状态不是11未付款未受理不允许改变状态
					return false;
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return true;
	}
	
	private void updateAcceptStatusSend(String orderId, String openId) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement("update WxOrder set acceptStatusSend=(acceptStatusSend+1) where (wxOrderNO = ? or OrderNO = ?) and CustWXnum = ?");
			ps.setString(1, orderId);
			ps.setString(2, orderId);
			ps.setString(3, openId);
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
	}
	
	public int acceptOrder(String orderId, String openId) throws Exception {
		if(getAcceptStatusSend(orderId, openId)==false) {//状态改变只能改变1次，超过1次则不允许再取消；状态不是11未付款未受理不允许改变状态
			return 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String orderStatus = "16";
		map.put("order_id", orderId);
		map.put("oppen_id", openId);
		map.put("status", orderStatus);
		cancleOrder(C3P0DBManager.getConnection(),orderId, openId,orderStatus);// 更新状态为取消
		cancleOrder(((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection(),orderId, openId,orderStatus);// 更新状态为取消
		updateAcceptStatusSend(orderId, openId);
		return orderDao.upstatusByMap(map);// 更新本地的状态
	}

	public void updateZFStatus(String orderId, String openId, String zfTransactionId, String zfamount) {
		updateRmOrderPay(orderId, openId, zfTransactionId, zfamount);// updateRMWxSendSKInfo(orderId, openId);
		updateLocalOrderPay(orderId, openId, zfTransactionId, zfamount);// updateRMWxSendSKInfo(orderId, openId);

		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("order_id", orderId);
		parMap.put("oppen_id", openId);
		parMap.put("status", 1);
		upstatusByMap(parMap);
	}

	public int updateSignStatus(String orderId, String openId) {
		int succStatus = 0;
		try {
			updateRmOrderSign(orderId, openId);
			updateLocalOrderSign(orderId, openId);
			Map<String, Object> parMap = new HashMap<String, Object>();
			parMap.put("order_id", orderId);
			parMap.put("oppen_id", openId);
			updateSign(parMap);
			succStatus = 1;
			Map<String,String> orderNumDataMap = getOrderNumFull(orderId, openId);
			if(orderNumDataMap.size()!=0) {
				Distribution objDistribution = new Distribution();
				objDistribution.setActionLogs(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+":客户执行签收状态：更改为1已签收");
				if(orderNumDataMap.get("OrderNO")!=null) {
					objDistribution.setOrderNum(orderNumDataMap.get("OrderNO").toString());
					int updateSucc = distributionService.orderSign(objDistribution);
					
					if(updateSucc==0&&orderNumDataMap.get("wxOrderNO")!=null) {
						objDistribution.setOrderNum(orderNumDataMap.get("wxOrderNO").toString());
						distributionService.orderSign(objDistribution);
					}
				}else {
					if(orderNumDataMap.get("wxOrderNO")!=null) {
						objDistribution.setOrderNum(orderNumDataMap.get("wxOrderNO").toString());
						distributionService.orderSign(objDistribution);
					}
				}
			}
		} catch (Exception e) {
			succStatus = 0;
			logger.error(e);
		}
		return succStatus;
	}

	public int upstatus(Map<String, Object> map) {
		return orderDao.upstatus(map);
	}

	public int upstatusByMap(Map<String, Object> map) {
		return orderDao.upstatusByMap(map);
	}
	

	public int updateSign(Map<String, Object> map) {
		return orderDao.updateSign(map);
	}

	public int upprice(Map<String, Object> map) {
		return orderDao.upprice(map);
	}

	public int delete(Map<String, Object> map) {
		return orderDao.delete(map);
	}

	public List<Order> list(Order order) {
		return orderDao.list(order);
	}

	public List<Order> listById(Order order) {
		return orderDao.listById(order);
	}

	public int count(Order order) {
		return orderDao.count(order);
	}

	public List<Order> listJson(Order order) {
		return orderDao.listJson(order);
	}

	public int listJsonCount(Order order) {
		return orderDao.listJsonCount(order);
	}

	public Map<String, String> getRMUserBy(String openId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> data = new HashMap<String, String>();
		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select t.Custid,t.CustName from WxUserCust t where t.openid = ?");
			ps.setString(1, openId);
			rs = ps.executeQuery();
			while (rs.next()) {
				data.put("CustName", rs.getString("CustName"));
				data.put("Custid", rs.getString("Custid"));
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return data;
	}

	public int getOrderStatus(String orderId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement("select t.orderStatus from WxOrder t where IFNULL(t.wxOrderNO,t.OrderNO) = ?");
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return -10000;
	}

	public List<OrderVO> listOrder(String openId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderVO> lstOrder = new ArrayList<OrderVO>();
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(
					"select t.wxOrderNO,t.OrderNO orderNO,IFNULL(t.wxOrderNO,t.OrderNO) order_id,t.Amount,t.orderStatus,t.creatdate,t.fid,t.OrderWLDH,t.qty,t.ispd,t.signFlag from WxOrder t where t.CustWXnum = ? order by t.id desc");
			ps.setString(1, openId);
			rs = ps.executeQuery();
			while (rs.next()) {
				OrderVO order = new OrderVO();
				int orderStatus = rs.getInt("orderStatus");
				order.setRealname(StringUtil.isRealEmpty(rs.getString("wxOrderNO")) ? "电销" : "微信");
				order.setOrder_id(rs.getString("order_id"));
				order.setWxOrderNO(rs.getString("wxOrderNO"));
				order.setOrderNO(rs.getString("orderNO"));
				order.setAdd_time(rs.getString("creatdate"));
				order.setGoods_total(rs.getFloat("Amount"));
				order.setStatus(orderStatus);
				order.setFid(rs.getString("fid"));
				order.setExpress_hm(rs.getString("OrderWLDH"));
				order.setGoods_total_num(rs.getInt("qty"));
				order.setStatusVal(CommonConstants.ORDER_STATUS_DATA.get(orderStatus));
				order.setIspd(rs.getInt("ispd"));
				order.setSignFlag(rs.getInt("signFlag"));
				lstOrder.add(order);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return lstOrder;
	}

	public OrderVO getOrderByOrderId(String openId, String orderId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OrderVO order = new OrderVO();
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(
					"select t.wxOrderNO,IFNULL(t.wxOrderNO,t.OrderNO) OrderNO,t.receiveType,t.ordAddress,t.Amount,t.orderStatus,t.creatdate,t.fid,t.OrderWLDH,t.qty,t.amount,t.receiveCost,t.ispd,t.isYLZF,t.expressCost from WxOrder t where t.CustWXnum = ? and (t.wxOrderNO = ? or t.OrderNO = ?)");
			ps.setString(1, openId);
			ps.setString(2, orderId);
			ps.setString(3, orderId);
			rs = ps.executeQuery();
			while (rs.next()) {
				int orderStatus = rs.getInt("orderStatus");
				order.setRealname(StringUtil.isRealEmpty(rs.getString("wxOrderNO")) ? "电销" : "微信");
				order.setOrder_id(rs.getString("OrderNo"));
				order.setAdd_time(rs.getString("creatdate"));
				order.setStatus(orderStatus);
				order.setFid(rs.getString("fid"));
				order.setExpress_hm(rs.getString("OrderWLDH"));
				order.setAddr_name(rs.getString("ordAddress"));
				order.setReceiveType(rs.getString("receiveType"));
				order.setGoods_total_num(rs.getInt("qty"));
				order.setGoods_total(rs.getFloat("amount"));
				order.setReceiveCost(rs.getFloat("receiveCost"));
				order.setIspd(rs.getInt("ispd"));
				order.setStatusVal(CommonConstants.ORDER_STATUS_DATA.get(orderStatus));
				order.setIsYLZF(rs.getInt("isYLZF"));
				order.setExpressCost(rs.getFloat("expressCost"));
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return order;
	}
	
	public OrderVO getOrderByOrderId(String orderId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OrderVO order = new OrderVO();
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(
					"select t.wxOrderNO,t.OrderNO orgOrderNO,IFNULL(t.wxOrderNO,t.OrderNO) OrderNO,t.receiveType,t.ordAddress,t.Amount,t.orderStatus,t.creatdate,t.fid,t.OrderWLDH,t.qty,t.amount,t.receiveCost,t.ispd,t.isYLZF,t.expressCost from WxOrder t where (t.wxOrderNO = ? or t.OrderNO = ?)");
			ps.setString(1, orderId);
			ps.setString(2, orderId);
			rs = ps.executeQuery();
			while (rs.next()) {
				int orderStatus = rs.getInt("orderStatus");
				order.setRealname(StringUtil.isRealEmpty(rs.getString("wxOrderNO")) ? "电销" : "微信");
				order.setOrder_id(rs.getString("OrderNo"));
				order.setOrderNO(rs.getString("orgOrderNO"));;
				order.setAdd_time(rs.getString("creatdate"));
				order.setStatus(orderStatus);
				order.setFid(rs.getString("fid"));
				order.setExpress_hm(rs.getString("OrderWLDH"));
				order.setAddr_name(rs.getString("ordAddress"));
				order.setReceiveType(rs.getString("receiveType"));
				order.setGoods_total_num(rs.getInt("qty"));
				order.setGoods_total(rs.getFloat("amount"));
				order.setReceiveCost(rs.getFloat("receiveCost"));
				order.setIspd(rs.getInt("ispd"));
				order.setStatusVal(CommonConstants.ORDER_STATUS_DATA.get(orderStatus));
				order.setIsYLZF(rs.getInt("isYLZF"));
				order.setExpressCost(rs.getFloat("expressCost"));
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return order;
	}

	public Float getCurrentDatePayAmount(String openId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Float resultFloat = 0.0f;
		try {
			StringBuffer sbd = new StringBuffer("select t.zfamount from WxOrder t where t.CustWXnum = '").append(openId)
					.append("' and DATE_FORMAT(zfdate,'%Y-%m-%d') = '").append(DateUtils.nowDate(null)).append("'");
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(sbd.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				resultFloat = rs.getFloat("zfamount");
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return resultFloat;
	}

	public List<RMOrderDetailVo> listOrderDetail(String openId, String orderId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RMOrderDetailVo> lstRMOrderDetailVo = new ArrayList<RMOrderDetailVo>();
		try {
			StringBuffer sbd = new StringBuffer(
					"select tf.fid,tf.invID,tf.invName,tf.Price,tf.Qty,tf.nQty,tf.Amount,tf.goodsQty from WxOrder t,WxOrderDetail tf where t.fid = tf.fid and t.CustWXnum =? ");
			if (!StringUtil.isRealEmpty(orderId)) {
				sbd.append("and (t.wxOrderNO = ? or t.OrderNO = ?)");
			}
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(sbd.toString());
			ps.setString(1, openId);
			if (!StringUtil.isRealEmpty(orderId)) {
				ps.setString(2, orderId);
				ps.setString(3, orderId);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				RMOrderDetailVo objRMOrderDetailVo = new RMOrderDetailVo();
				objRMOrderDetailVo.setFid(rs.getString("fid"));
				objRMOrderDetailVo.setInvID(rs.getString("invID"));
				objRMOrderDetailVo.setInvName(rs.getString("invName"));
				objRMOrderDetailVo.setPrice(rs.getString("Price"));
				objRMOrderDetailVo.setQty(rs.getString("Qty"));
				objRMOrderDetailVo.setnQty(rs.getString("nQty"));
				objRMOrderDetailVo.setAmount(rs.getString("Amount"));
				objRMOrderDetailVo.setGoodsQty(rs.getString("goodsQty"));
				lstRMOrderDetailVo.add(objRMOrderDetailVo);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return lstRMOrderDetailVo;
	}
	
	public List<RMOrderDetailVo> listOrderDetail(String orderId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RMOrderDetailVo> lstRMOrderDetailVo = new ArrayList<RMOrderDetailVo>();
		try {
			StringBuffer sbd = new StringBuffer(
					"select tf.fid,tf.invID,tf.invName,tf.Price,tf.Qty,tf.nQty,tf.Amount,tf.goodsQty from WxOrder t,WxOrderDetail tf where t.fid = tf.fid ");
			if (!StringUtil.isRealEmpty(orderId)) {
				sbd.append("and (t.wxOrderNO = ? or t.OrderNO = ?)");
			}
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement(sbd.toString());
			if (!StringUtil.isRealEmpty(orderId)) {
				ps.setString(1, orderId);
				ps.setString(2, orderId);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				RMOrderDetailVo objRMOrderDetailVo = new RMOrderDetailVo();
				objRMOrderDetailVo.setFid(rs.getString("fid"));
				objRMOrderDetailVo.setInvID(rs.getString("invID"));
				objRMOrderDetailVo.setInvName(rs.getString("invName"));
				objRMOrderDetailVo.setPrice(rs.getString("Price"));
				objRMOrderDetailVo.setQty(rs.getString("Qty"));
				objRMOrderDetailVo.setnQty(rs.getString("nQty"));
				objRMOrderDetailVo.setAmount(rs.getString("Amount"));
				objRMOrderDetailVo.setGoodsQty(rs.getString("goodsQty"));
				lstRMOrderDetailVo.add(objRMOrderDetailVo);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return lstRMOrderDetailVo;
	}


}
