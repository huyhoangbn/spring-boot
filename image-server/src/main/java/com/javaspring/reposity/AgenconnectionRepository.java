package com.javaspring.reposity;

import com.javaspring.entity.Agentconnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenconnectionRepository extends JpaRepository<Agentconnection,Long> {
    Agentconnection findOneByToken(String token);
}
