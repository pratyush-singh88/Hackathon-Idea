/**
 * 
 */
package com.bangalorewest.bagtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bangalorewest.bagtracker.model.Agent;

/**
 * @author sudhanshu.singh
 *
 */
public interface BagAgentRepository extends JpaRepository<Agent, Integer> {

	@Query(value = "select a.user_name, a.role, a.agent_location from Agent a where a.password =:pswrd and a.user_name = :uName and a.role = :role", nativeQuery = true)
	List<Object> checkLogin(@Param("pswrd") String pwd, @Param("uName") String userName, @Param("role") String role);

	@Query(value = "select a.user_name, a.role, a.agent_location from Agent a where a.password =:pswrd and a.user_name = :uName and a.role = :role and a.agent_location=:agentLocation", nativeQuery = true)
	List<Object> checkLoginForLoadUnload(@Param("pswrd") String pwd, @Param("uName") String userName,
			@Param("role") String role, @Param("agentLocation") String agentLocation);
	
	@Query(value = "select a.user_name, a.role, a.agent_location from Agent a where a.password =:pswrd and a.user_name = :uName", nativeQuery = true)
	List<Object> checkLoginWithoutRole(@Param("pswrd") String pwd, @Param("uName") String userName);

}
