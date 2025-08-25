package com.wetube.wetube_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
  name = "video_tags",
  uniqueConstraints = @UniqueConstraint(name="uk_video_tag", columnNames = {"video_id","tag_id"})
)
public class VideoTag {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "CHAR(16)")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "video_id", nullable = false)
  private Video video;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tag_id", nullable = false)
  private Tag tag;
}
