package clofi.codeython.common.domain.dto;

import lombok.Getter;

@Getter
public class ExceptionResult {
	private String message;

	public ExceptionResult(String message) {
		this.message = message;
	}
}
