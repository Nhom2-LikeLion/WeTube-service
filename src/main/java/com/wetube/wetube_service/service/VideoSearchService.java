package com.wetube.wetube_service.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.wetube.wetube_service.search.VideoDocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

import static co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders.terms;
import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoSearchService {
        private final ElasticsearchOperations elasticsearchOperations;
        private static final IndexCoordinates INDEX = IndexCoordinates.of("videos");

        public Page<VideoDocument> fullText(String text, int page, int size) {
                Instant start = Instant.now();
                log.info("Starting full-text search - query: '{}', page: {}, size: {}", text, page, size);

                Pageable pageable = PageRequest.of(page, size);
                NativeQuery query = new NativeQueryBuilder()
                                .withQuery(multiMatch(m -> m.query(text).fields("title^2", "description", "tags")
                                .operator(Operator.And)))
                                .withPageable(pageable)
                                .build();

                log.debug("Full-text query: {}", query.getQuery());
                Page<VideoDocument> results = search(query, pageable);

                Duration duration = Duration.between(start, Instant.now());
                log.info("Full-text search completed - found {} results in {}ms",
                                results.getTotalElements(), duration.toMillis());

                return results;
        }

        public Page<VideoDocument> fuzzy(String text, int page, int size) {
                Instant start = Instant.now();
                log.info("Starting fuzzy search - query: '{}', page: {}, size: {}", text, page, size);

                Pageable pageable = PageRequest.of(page, size);
                NativeQuery query = new NativeQueryBuilder()
                                .withQuery(match(m -> m.field("title").query(text).fuzziness("AUTO")))
                                .withPageable(pageable)
                                .build();

                log.debug("Fuzzy query: {}", query.getQuery());
                Page<VideoDocument> result = search(query, pageable);

                Duration duration = Duration.between(start, Instant.now());
                log.info("Fuzzy search completed - found {} results in {}ms",
                                result.getTotalElements(), duration.toMillis());

                return result;
        }

        public List<String> suggestNames(String prefix, int size) {
                Instant start = Instant.now();
                log.info("Starting suggestion search - prefix: '{}', size: {}", prefix, size);

                NativeQuery query = new NativeQueryBuilder()
                                .withQuery(prefix(m -> m.field("title").value(prefix)))
                                .withSourceFilter(
                                                new FetchSourceFilter(false, new String[] { "title" }, new String[] {}))
                                .withMaxResults(size)
                                .build();

                log.debug("Suggestion query: {}", query.getQuery());
                SearchHits<VideoDocument> hits = elasticsearchOperations.search(query, VideoDocument.class, INDEX);
                List<String> suggestions = hits.getSearchHits().stream()
                                .map(h -> h.getContent().getTitle())
                                .distinct()
                                .toList();

                Duration duration = Duration.between(start, Instant.now());
                log.info("Suggestion search completed - found {} suggestions in {}ms",
                                suggestions.size(), duration.toMillis());

                return suggestions;
        }

        public Page<VideoDocument> sortAndPaginate(String text, String sortField, boolean asc, int page, int size) {
                Instant start = Instant.now();
                log.info("Starting sort and paginate search - query: '{}', sortField: '{}', asc: {}, page: {}, size: {}",
                                text, sortField, asc, page, size);

                Pageable pageable = PageRequest.of(page, size);
                NativeQueryBuilder builder = new NativeQueryBuilder()
                                .withQuery(multiMatch(m -> m.query(text).fields("title^2", "description")))
                                .withPageable(pageable);
                if (asc)
                        builder.withSort(
                                        s -> s.field(f -> f.field(sortField)
                                                        .order(co.elastic.clients.elasticsearch._types.SortOrder.Asc)));
                else
                        builder.withSort(s -> s
                                        .field(f -> f.field(sortField).order(
                                                        co.elastic.clients.elasticsearch._types.SortOrder.Desc)));

                NativeQuery query = builder.build();
                log.debug("Sort and paginate query: {}", query.getQuery());
                Page<VideoDocument> result = search(query, pageable);

                Duration duration = Duration.between(start, Instant.now());
                log.info("Sort and paginate search completed - found {} results in {}ms",
                                result.getTotalElements(), duration.toMillis());

                return result;
        }

        public SearchHits<VideoDocument> aggregateByCategory(String text) {
                Instant start = Instant.now();
                log.info("Starting aggregation search - query: '{}'", text);

                NativeQuery query = new NativeQueryBuilder()
                                .withQuery(multiMatch(m -> m.query(text).fields("title", "description")))
                                .withAggregation("by_category", terms(t -> t.field("categories")))
                                .build();

                log.debug("Aggregation query: {}", query.getQuery());
                SearchHits<VideoDocument> hits = elasticsearchOperations.search(query, VideoDocument.class, INDEX);

                Duration duration = Duration.between(start, Instant.now());
                log.info("Aggregation search completed - found {} results in {}ms",
                                hits.getTotalHits(), duration.toMillis());

                return hits;
        }

        public Page<VideoDocument> multiFieldSearch(String tag, String title, String description, String category,
                        int page,
                        int size) {
                Instant start = Instant.now();
                log.info("Starting multi-field search - title: '{}', description: '{}', category: '{}', page: {}, size: {}",
                                tag, title, description, category, page, size);

                Pageable pageable = PageRequest.of(page, size);
                NativeQuery query = new NativeQueryBuilder()
                                .withQuery(bool(b -> b
                                                .must(tag == null || tag.isBlank()
                                                                ? matchAll().build()._toQuery()
                                                                : term(t -> t.field("tags").value(tag.toLowerCase())))
                                                .must(title == null || title.isBlank()
                                                                ? matchAll().build()._toQuery()
                                                                : match(m -> m.field("title").query(title)))
                                                .must(description == null || description.isBlank()
                                                                ? matchAll().build()._toQuery()
                                                                : match(m -> m.field("description").query(description)))
                                                .filter(category == null || category.isBlank()
                                                                ? matchAll().build()._toQuery()
                                                                : term(t -> t.field("categories").value(category)))))
                                .withPageable(pageable)
                                .build();

                log.debug("Multi-field query: {}", query.getQuery());
                Page<VideoDocument> result = search(query, pageable);

                Duration duration = Duration.between(start, Instant.now());
                log.info("Multi-field search completed - found {} results in {}ms",
                                result.getTotalElements(), duration.toMillis());

                return result;
        }

        private Page<VideoDocument> search(NativeQuery query, Pageable pageable) {
                try {
                        SearchHits<VideoDocument> hits = elasticsearchOperations.search(query, VideoDocument.class,
                                        INDEX);
                        List<VideoDocument> content = hits.getSearchHits().stream().map(SearchHit::getContent).toList();
                        log.debug("Elasticsearch search executed - totalHits: {}, returnedHits: {}",
                                        hits.getTotalHits(), hits.getSearchHits().size());

                        return new PageImpl<>(content, pageable, hits.getTotalHits());
                } catch (Exception e) {
                        log.error("Elasticsearch search failed - query: {}, error: {}", query.getQuery(),
                                        e.getMessage(), e);
                        throw e;
                }
        }

        // Search helpers for tag/category specific use cases
        public Page<VideoDocument> byTag(String tag, int page, int size) {
                Pageable pageable = PageRequest.of(page, size);
                NativeQuery query = new NativeQueryBuilder()
                                .withQuery(term(t -> t.field("tags").value(tag.toLowerCase())))
                                .withPageable(pageable)
                                .build();
                return search(query, pageable);
        }

        public Page<VideoDocument> byCategory(String category, int page, int size) {
                Pageable pageable = PageRequest.of(page, size);
                NativeQuery query = new NativeQueryBuilder()
                                .withQuery(term(t -> t.field("categories").value(category)))
                                .withPageable(pageable)
                                .build();
                return search(query, pageable);
        }

        public Page<VideoDocument> byTagAndCategory(String tag, String category, int page, int size) {
                Pageable pageable = PageRequest.of(page, size);
                NativeQuery query = new NativeQueryBuilder()
                                .withQuery(bool(b -> b
                                        .must(term(t -> t.field("tags").value(tag.toLowerCase())))
                                        .filter(term(t -> t.field("categories").value(category)))))
                                .withPageable(pageable)
                                .build();
                return search(query, pageable);
        }
}
