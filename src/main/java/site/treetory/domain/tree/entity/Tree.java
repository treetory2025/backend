package site.treetory.domain.tree.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.enums.Background;
import site.treetory.domain.tree.enums.Theme;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static site.treetory.domain.tree.enums.Theme.SNOWY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tree {

    @Id
    @Column(name = "member_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "member_id", columnDefinition = "INT UNSIGNED")
    private Member member;

    @NotNull
    private Integer size;

    @NotNull
    @Enumerated(STRING)
    private Theme theme;

    @NotNull
    @Enumerated(STRING)
    private Background background;
    
    public void resize() {
        this.size++;
    }

    public static Tree createBasicTree(Member member) {

        return Tree.builder()
                .member(member)
                .size(1)
                .theme(SNOWY)
                .background(Background.SILENT_NIGHT)
                .build();
    }

    public void changeTheme(Theme theme) {
        this.theme = theme;
    }

    public void changeBackground(Background background) {
        this.background = background;
    }
}
