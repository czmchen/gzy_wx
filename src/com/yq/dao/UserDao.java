package com.yq.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yq.entity.User;

public interface UserDao {

	public int insert(Map<String, Object> map);
	
	public int insertNotRep(Map<String, Object> map);

	public int updateBatch(List<User> updateUsers);
	
	public int update(Map<String, Object> map);
	
	public int updateIsWXFocus(Map<String, Object> map);
	
	public int upRMCustInfo(Map<String, Object> map);

	public int uparea(Map<String, Object> map);

	public int upstatus(Map<String, Object> map);

	public int upmbertime(Map<String, Object> map);

	public int upmphone(Map<String, Object> map);

	public List<User> list(User user);

	public int count(User user);

	public List<User> listById(User user);
	
	public String getCustidByOpenId(@Param(value = "oppen_id")String oppen_id);
	
	public User getUserByOpenId(@Param(value = "oppen_id")String oppen_id);
	
	public int updateSalesPrice1(@Param(value = "oppen_id")String oppen_id);

	public int isMember(User user);
	
	public List<User> allUser(@Param(value = "wxName")String wxName);

}
