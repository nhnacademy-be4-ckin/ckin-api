package store.ckin.api.address.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import store.ckin.api.address.entity.Address;

/**
 * Address 에 관한 쿼리를 관리하는 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 17.
 */
public interface AddressRepository extends JpaRepository<Address, Long>, AddressRepositoryCustom {
    @Query(value = "SELECT a.isDefault FROM Address AS a WHERE a.id = :addressId")
    Boolean isDefaultAddress(Long addressId);

    boolean existsByMemberIdAndBaseAndDetail(Long memberId, String base, String detail);

    @Query(value = "SELECT a FROM Address AS a WHERE a.member.id = :memberId AND a.isDefault = true")
    Optional<Address> findDefaultAddressByMemberId(Long memberId);
}
