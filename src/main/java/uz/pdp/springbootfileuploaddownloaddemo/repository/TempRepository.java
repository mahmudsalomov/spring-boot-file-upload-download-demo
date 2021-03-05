package uz.pdp.springbootfileuploaddownloaddemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springbootfileuploaddownloaddemo.entity.Temp;

@Repository
public interface TempRepository extends JpaRepository<Temp,Long> {
    boolean existsByEmail(String email);
    Temp findByEmail(String email);
}
