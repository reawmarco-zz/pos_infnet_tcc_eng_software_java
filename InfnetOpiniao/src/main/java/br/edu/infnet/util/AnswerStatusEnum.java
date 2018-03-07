package br.edu.infnet.util;

public enum AnswerStatusEnum {
	
	ERROR("error"),
	OK("ok"),
	ANSWERED("answered"),
	SUCCESS("success"),
	EXPIRED("expired");
	
	private final String desc;
	
	private AnswerStatusEnum(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
