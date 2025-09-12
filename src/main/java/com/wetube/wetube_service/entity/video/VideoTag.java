package com.wetube.wetube_service.entity.video;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "video_tags", uniqueConstraints = @UniqueConstraint(name = "uk_video_tag", columnNames = { "video_id",
    "tag_id" }))
public class VideoTag {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "VARCHAR(36)")
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "video_id", nullable = false, columnDefinition = "VARCHAR(36)")
  private Video video;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tag_id", nullable = false, columnDefinition = "VARCHAR(36)")
  private Tag tag;
}
