package br.com.hbparking.vagaInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVagaInfoRepository extends JpaRepository<VagaInfo, Long> {
}
