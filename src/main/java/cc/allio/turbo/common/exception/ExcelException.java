/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package cc.allio.turbo.common.exception;

/**
 * Excel异常处理类
 *
 * @author h.x
 * @date 2023/12/29 17:52
 * @since 0.1.0
 */
public class ExcelException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExcelException(String message) {
		super(message);
	}
}
