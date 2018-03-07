package br.edu.infnet.util;

public enum Likert {
	
	CONCORDO_TOTALMENTE("CT", "Concordo totalmente"),
	CONCORDO("CO", "Concordo"),
	NÃO_CONCORDO_NEM_DISCORDO("CD", "Não concordo nem discordo"),
	DISCORDO("DI", "Discordo"),
	DISCORDO_TOTALMENTE("DT", "Discordo totalmente"),
	NÃO_SEI_AVALIAR("NA", "Não sei avaliar");
	
	private final String code;
	private final String description;
	
	private Likert(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static Likert getValue(String code) {
		for(Likert likert : Likert.values()) {
			if(likert.getCode().equals(code)) {
				return likert;
			}
		}
		return null;
	}

}
