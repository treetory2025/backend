package site.treetory.domain.tree.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.treetory.domain.tree.enums.Font;
import site.treetory.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = " UPDATE PlacedOrnament SET is_deleted = true WHERE placed_ornament_id = ? ")
public class PlacedOrnament extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placed_ornament_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tree_id", nullable = false)
    private Tree tree;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ornament_id", nullable = false)
    private Ornament ornament;

    @NotNull
    @Column(name = "position_x")
    private Integer positionX;

    @NotNull
    @Column(name = "position_y")
    private Integer positionY;

    @NotBlank
    @Column(length = 300)
    private String message;

    @NotBlank
    @Column(length = 6)
    private String writerNickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Font font;
    
}
