package store.ckin.api.address.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.address.entity.Address;

/**
 * Address 에 관한 쿼리를 관리하는 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 17.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
}
