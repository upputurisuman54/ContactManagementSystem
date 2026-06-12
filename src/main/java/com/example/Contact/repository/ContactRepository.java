package com.example.Contact.repository;

import com.example.Contact.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Page<Contact> findByUserId(Long userId, Pageable pageable);

    Optional<Contact> findByIdAndUserId(Long id, Long userId);

    List<Contact> findByUserIdAndFavouriteTrue(Long userId);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(c.company) LIKE LOWER(CONCAT('%', :kw, '%')))")
    List<Contact> searchContacts(@Param("userId") Long userId, @Param("kw") String keyword);

    @Query("SELECT c FROM Contact c JOIN c.tags t WHERE c.user.id = :userId AND t.id = :tagId")
    Page<Contact> findByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findAllByUserId(@Param("userId") Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndFavouriteTrue(Long userId);

    @Query("SELECT c.company, COUNT(c) as cnt FROM Contact c WHERE c.user.id = :userId AND c.company IS NOT NULL AND c.company != '' GROUP BY c.company ORDER BY cnt DESC")
    List<Object[]> topCompaniesByUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t.name, COUNT(c) as cnt FROM Contact c JOIN c.tags t WHERE c.user.id = :userId GROUP BY t.name ORDER BY cnt DESC")
    List<Object[]> contactsPerTagByUser(@Param("userId") Long userId);

    boolean existsByEmailAndUserId(String email, Long userId);
}