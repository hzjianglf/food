package com.sniper.springmvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * 自定义错误信息显示
 * @author sniper
 *
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "出错了")
public class ErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ErrorException() {
	}

}
