package site.treetory.domain.tree.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.enums.Category;
import site.treetory.global.entity.BaseEntity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = " UPDATE ornament SET is_deleted = true WHERE ornament_id = ? ")
public class Ornament extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ornament_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 10, unique = true)
    private String name;

    @NotNull
    @Enumerated(STRING)
    private Category category;

    @NotNull
    private String imgUrl;
}


