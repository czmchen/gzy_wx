package com.weixin.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.weixin.jobs.InactiveCustomerJob;
import com.weixin.util.QuartzManager;

public class ServerStartInitData extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final static Logger LOGGER = Logger.getLogger(ServerStartInitData.class);

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			LOGGER.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " ServerStartInitData任务进行中。。。");
			addQuartzJob();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	
	private void addQuartzJob() {
		QuartzManager.removeJob("GZY_InactiveCustomerJobJob", "GZY_InactiveCustomerJob_TRIGGER");
		QuartzManager.addJob("GZY_InactiveCustomerJobJob", "GZY_InactiveCustomerJob_TRIGGER", InactiveCustomerJob.class, "0 0 0 * * ?", null, null);

//		QuartzManager.removeJob("GZY_SyncWxUserCustJob", "GZY_SyncWxUserCustJob_TRIGGER");
//		QuartzManager.addJob("GZY_SyncWxUserCustJob", "GZY_SyncWxUserCustJob_TRIGGER", SyncWxUserCustJob.class, "*/30 * * * * ?", null, null);
		
//		QuartzManager.removeJob("GZY_SyncWxOrderJob", "GZY_SyncWxOrderJob_TRIGGER");
//		QuartzManager.addJob("GZY_SyncWxOrderJob", "GZY_SyncWxOrderJob_TRIGGER", SyncWxOrderJob.class, "*/7 * * * * ?", null, null);
//
//		QuartzManager.removeJob("GZY_SyncDistributionJob", "GZY_SyncDistributionJob_TRIGGER");
//		QuartzManager.addJob("GZY_SyncDistributionJob", "GZY_SyncDistributionJob_TRIGGER", SyncDistributionJob.class, "*/30 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_RMWXMsgNoteJob", "GZY_RMWXMsgNoteJob_TRIGGER");
//		QuartzManager.addJob("GZY_RMWXMsgNoteJob", "GZY_RMWXMsgNoteJob_TRIGGER", RMWXMsgNoteJob.class, "*/5 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_RMWxSendInfoJob", "GZY_RMWxSendInfoJob_TRIGGER");
//		QuartzManager.addJob("GZY_RMWxSendInfoJob", "GZY_RMWxSendInfoJob_TRIGGER", RMWxSendInfoJob.class, "*/10 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_RMWxSendSKInfoJob", "GZY_RMWxSendSKInfoJob_TRIGGER");
//		QuartzManager.addJob("GZY_RMWxSendSKInfoJob", "GZY_RMWxSendSKInfoJob_TRIGGER", RMWxSendSKInfoJob.class, "*/10 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_RMWxUserCustJob", "GZY_RMWxUserCustJob_TRIGGER");
//		QuartzManager.addJob("GZY_RMWxUserCustJob", "GZY_RMWxUserCustJob_TRIGGER", RMWxUserCustJob.class, "*/10 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_RMWxOrderJob", "GZY_RMWxOrderJob_TRIGGER");
//		QuartzManager.addJob("GZY_RMWxOrderJob", "GZY_RMWxOrderJob_TRIGGER", RMWxOrderJob.class, "*/10 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_RMWXSendMsgJob", "GZY_RMWXSendMsgJob_TRIGGER");
//		QuartzManager.addJob("GZY_RMWXSendMsgJob", "GZY_RMWXSendMsgJob_TRIGGER", RMWXSendMsgJob.class, "*/5 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_RMWxOrderAutoCancleJob", "GZY_RMWxOrderAutoCancleJob_TRIGGER");
//		QuartzManager.addJob("GZY_RMWxOrderAutoCancleJob", "GZY_RMWxOrderAutoCancleJob_TRIGGER", RMWxOrderAutoCancleJob.class, "*/10 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_RMWxOrderSignJob", "GZY_RMWxOrderSignJob_TRIGGER");
//		QuartzManager.addJob("GZY_RMWxOrderSignJob", "GZY_RMWxOrderSignJob_TRIGGER", RMWxOrderSignJob.class, "*/10 * * * * ?", null, null);
//		
//		QuartzManager.removeJob("GZY_WXCustromerMsgJob", "GZY_WXCustromerMsgJob_TRIGGER");
//		QuartzManager.addJob("GZY_WXCustromerMsgJob", "GZY_WXCustromerMsgJob_TRIGGER", WXCustromerMsgJob.class, "0 */1 * * * ?", null, null);
	}

}
