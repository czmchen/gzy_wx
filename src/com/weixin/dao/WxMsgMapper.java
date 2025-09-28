package com.weixin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weixin.entity.WxMsg;
import com.yq.vo.WXMsgListVo;

public interface WxMsgMapper {

	int insert(WxMsg wxMsg);
	
	int insertNewMsg(@Param(value = "openId")String openId);
	
	public List<WxMsg> getAllMsg(@Param(value = "openId")String openId,@Param(value = "isMsgNew")Integer isMsgNew);
	
	public List<WXMsgListVo> getNewMsgList(@Param(value = "wxName")String wxName,@Param(value = "recordAmount")int recordAmount);

	public void updateNewMsg2Read(@Param(value = "openId")String openId);

	public void updateNewMsg2ReadByIds(@Param(value = "openId")String openId,@Param(value = "ids")List<Long> ids);
	
	public List<WxMsg> getNeed2Download();
	
	int updateDownloadResult(WxMsg objWxMsg);
	
	public List<WxMsg> search(@Param(value = "mediaId")String mediaId);
	
	public int updatePicUrlByMediaId(@Param(value = "mediaId")String mediaId,@Param(value = "picUrl")String picUrl);
	
}