package ssodam.ssodam.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "post")
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

// 수정 필요 (연관관계 없는 걸로)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // 우선 다대일로 설정, 후에 논의 필요
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Scrap> scrappedBy = new HashSet<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob // 검색해보기
    private String contents;

    @ColumnDefault("0")
    private int likes;
    @ColumnDefault("0")
    private int visit;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public Post(Member member, Category category, String title, String contents){
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.member = member;
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    public Post() {

    }
}
