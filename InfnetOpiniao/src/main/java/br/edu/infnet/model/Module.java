package br.edu.infnet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "module")
public class Module {

	@Id
	@Column(name = "oid_module")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long oid;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_date")
	private String endDate;

	@Column(name = "description")
	private String description;

	@Column(name = "oid_block")
	private Long block;

	// private List<Evaluation> evaluationList;

	public Module() {
		super();
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getBlock() {
		return block;
	}

	public void setBlock(Long block) {
		this.block = block;
	}

	/*
	 * public List<Evaluation> getEvaluationList() { return evaluationList; }
	 * 
	 * public void setEvaluationList(List<Evaluation> evaluationList) {
	 * this.evaluationList = evaluationList; }
	 */
}
