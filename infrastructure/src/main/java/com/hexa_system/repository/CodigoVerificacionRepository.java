package com.hexa_system.repository;

import com.hexa_system.entity.CodigoVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CodigoVerificacionRepository extends JpaRepository<CodigoVerificacion,Long> {
    Optional<CodigoVerificacion> findByEmailAndCodigoAndUsadoFalse(String email, String codigo);
    @Transactional
    @Modifying
    void deleteByEmail(String email);
}
