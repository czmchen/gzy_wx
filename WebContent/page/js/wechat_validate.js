(function($){
	/**
	 * 包含加载中提示的ajax
	 */
	$.wxajax = function( option ){
		$.showLoading();
		var tmp = option.complete;
		var complete = function(jqXHR, textStatus ){
			$.hideLoading();
			(tmp)?tmp( jqXHR, textStatus ):'';
		}
		option.complete = complete;
		$.ajax(option);
	};
	
	/**
	 * 各种校验格式
	 */
	$.wxregex = {
			//emailReg: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*(\-)?[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
			//emailReg: /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/,
			emailReg:/^[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
			emailTest: function(val){
				return this.emailReg.test(val);
			},
			pwdReg: /^\S{6,20}$/i,
			pwdTest: function(val){
				return this.pwdReg.test(val);
			},
			mobileReg: /^1[3|4|5|6|7|8|9|][0-9]{9}/,
			mobileTest: function(val){
				return this.mobileReg.test(val);
			},
			logNameReg: /^[a-zA-Z][a-zA-Z0-9_]{1,19}$/,
			logNameTest: function(val){
				return this.logNameReg.test(val);
			},
			zipCodeReg: /^\d{6}$/,
			zipCodeTest: function(val){
				return this.zipCodeReg.test(val);
			},
			nameReg: /^[\w\u4E00-\u9FA5\uF900-\uFA2D]*$/,
			nameTest: function(val){
				return this.nameReg.test(val);
			},
			telReg: /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/,
			telTest: function(val){
				return this.telReg.test(val);
			},
			idCardReg: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
			idCardTest: function(val){
				return this.idCardReg.test(val);
			},
			integerReg: /(^0$)|(^([1-9])\d*$)/,
			integerTest: function(val){
				return this.integerReg.test(val);
			},
			decimalReg: /(^0$)|(^-?([1-9])\d*$)|(^-?0\.\d+$)|(^-?([1-9])\d*\.\d+$)/,
			decimalTest: function(val){
				return this.decimalReg.test(val);
			},
			letNumReg: /^[a-zA-Z0-9]*$/,
			letNumTest: function(val){
				return this.letNumReg.test(val);
			},
			webSiteReg: /^[a-zA-Z0-9\:\/\.\@\%\?\&\=]+$/,
			webSiteTest: function(val){
				return this.webSiteReg.test(val);
			},
			cnReg: /^[\u4E00-\u9FA5]*$/,
			cnTest: function(val){
				return this.cnReg.test(val);
			},
			fullCnReg: /^[\uFF00-\uFFFF]+$/,
			fullCnTest: function(val){
				return this.fullCnReg.test(val);
			},
			netReg: /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?/,
			netTest: function(val){
				return this.netReg.test(val);
			},
			
			bankNoReg:  /^(\d{16}|\d{19})$/,
			bankNoTest: function(val){
				return this.bankNoReg.test(val);
			},
			numberReg:  /^[0-9]*$/ ,
			numberTest: function(val){
				return this.numberReg.test(val);
			}	
	};
	
}(jQuery));

