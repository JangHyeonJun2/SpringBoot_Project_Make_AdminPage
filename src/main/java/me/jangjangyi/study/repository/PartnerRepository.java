package me.jangjangyi.study.repository;

import me.jangjangyi.study.model.entity.Category;
import me.jangjangyi.study.model.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner,Long> {
     List<Partner> findByCategory(Category category);
}
