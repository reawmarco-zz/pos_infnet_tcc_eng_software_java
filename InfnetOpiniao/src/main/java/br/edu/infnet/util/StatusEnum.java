package br.edu.infnet.util;

public enum StatusEnum {
	
	NEW("NE", "Aguardando Inicio"),
	INITIALIZED("IN", "Inicializada"),
	FINISHED("FI", "Finalizada");
	
	private final String desc;
	private final String view;
	
	private StatusEnum(String desc, String view){
		this.desc = desc;
		this.view = view;
	}

	public String getDesc() {
		return desc;
	}

	public String getView() {
		return view;
	}

}
