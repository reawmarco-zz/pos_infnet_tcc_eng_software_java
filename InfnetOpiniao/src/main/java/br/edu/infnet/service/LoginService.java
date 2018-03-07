package br.edu.infnet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.edu.infnet.model.Employee;
import br.edu.infnet.model.EmployeeDetails;
import br.edu.infnet.repository.EmployeeRepository;

public class LoginService implements UserDetailsService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeRepository.findEmployeeByUsername(username);
		if (employee == null) {
            throw new UsernameNotFoundException(username);
        }
        return new EmployeeDetails(employee);
	}
	


}
