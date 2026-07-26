package foodapp.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification,Integer> {
    Optional<OtpVerification> findByPhoneNumber(String phoneNumber);

}
