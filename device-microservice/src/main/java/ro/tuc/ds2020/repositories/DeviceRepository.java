package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.entities.Device;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Device d WHERE d.userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}
