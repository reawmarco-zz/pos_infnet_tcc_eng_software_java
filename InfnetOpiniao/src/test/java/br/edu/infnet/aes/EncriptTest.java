package br.edu.infnet.aes;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.edu.infnet.exception.AdvancedEncryptionStandardException;
import br.edu.infnet.util.AdvancedEncryptionStandard;
import br.edu.infnet.util.VariablesUtil;

public class EncriptTest {
	
	@Test
	public void encriptKey() throws AdvancedEncryptionStandardException, UnsupportedEncodingException {
		String dados = "1;11";
		String encriptedKey = AdvancedEncryptionStandard.encrypt(dados, VariablesUtil.KEY);
		System.out.println(encriptedKey);
	}
	
	@Test
	public void decriptKey() throws AdvancedEncryptionStandardException, UnsupportedEncodingException {
		String dados = "gmIWpdlmB%2FcbzVOi7zlqWg%3D%3D";
		String encriptedKey = AdvancedEncryptionStandard.decrypt(dados, VariablesUtil.KEY);
		System.out.println(encriptedKey);
	}
	
	@Test
	public void studentList() throws AdvancedEncryptionStandardException {
		Long oidEvaluation = new Long(2);
		List<Long> list = new ArrayList<Long>();
		list.add(1L);
		list.add(2L);
		list.add(3L);
		list.add(4L);
		list.add(5L);
		list.add(6L);
		list.add(7L);
		list.add(8L);
		list.add(9L);
		list.add(10L);
		for(Long oid : list) {
			StringBuilder sb = new StringBuilder();
			sb.append(oidEvaluation).append(";").append(oid);
			String encriptedKey = AdvancedEncryptionStandard.encrypt(sb.toString(), VariablesUtil.KEY);
			System.out.println("Dados: [" + sb.toString() + "] Hash: [" + encriptedKey + "]");
		}
	}
	
	@Test
	public void encriptPassword() throws AdvancedEncryptionStandardException, UnsupportedEncodingException {
		String dados = "tricolor";
		String encriptedKey = AdvancedEncryptionStandard.encrypt(dados, VariablesUtil.KEY);
		System.out.println(encriptedKey);
	}
	
	@Test
	public void decriptPassword() throws AdvancedEncryptionStandardException, UnsupportedEncodingException {
		String dados = "DirvMKivxh2V8U1WF3RHiA%3D%3D";
		String encriptedKey = AdvancedEncryptionStandard.decrypt(dados, VariablesUtil.KEY);
		System.out.println(encriptedKey);
	}

}
