INSERT IGNORE INTO roles (id, code) VALUES (1, 'ROLE_USER');
INSERT IGNORE INTO roles (id, code) VALUES (2, 'ROLE_ADMIN');

INSERT INTO channels (id, picture, background_img_url, country_code, created_at, description, name,
                      revenue, status, total_subscribers, total_videos, total_views, updated_at)
VALUES ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'https://example.com/channel-avatar.png',
        'https://example.com/channel-bg.png',
        'VI', NOW(), 'Kênh demo tổng hợp video', 'Demo Channel', 0.0, 'ACTIVE', 0, 0, 0, NOW()),
       ('550e8400-e29b-41d4-a716-446655440000', 'https://example.com/channel-avatar.png',
        'https://example.com/channel-bg.png',
        'VI', NOW(), 'Kênh demo tổng hợp video 2', 'Demo Channel', 0.0, 'ACTIVE', 0, 0, 0, NOW());


INSERT INTO users (id, email, password, name, picture, created_at, updated_at, channel_id)
VALUES ('ca636eda-e9a2-4396-b3f3-a214b1386abb', 'huynhkyson0310@gmail.com', 'hashed_password_1', 'Kyson',
        'https://example.com/avatar1.jpg', '2025-09-03 15:00:00', '2025-09-03 15:00:00',
        '550e8400-e29b-41d4-a716-446655440000'),
       ('b7f6c8b2-4c1d-4e3b-9c2a-1f2a3b4c5d6e', 'testuser2@gmail.com', 'hashed_password_2', 'Test User 2',
        'https://example.com/avatar2.jpg', '2025-09-03 15:00:00', '2025-09-03 15:00:00',
        '6ba7b810-9dad-11d1-80b4-00c04fd430c8');


INSERT INTO videos (id, title, video_url, thumbnail_url, duration, created_at, updated_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Video 1', 'https://example.com/video1.mp4', 'https://example.com/thumb1.png', 120, NOW(), NOW()),
    ('22222222-2222-2222-2222-222222222222', 'Video 2', 'https://example.com/video2.mp4', 'https://example.com/thumb2.png',95, NOW(), NOW()),
    ('33333333-3333-3333-3333-333333333333', 'Video 3', 'https://example.com/video3.mp4', 'https://example.com/thumb3.png', 180, NOW(), NOW()),
    ('44444444-4444-4444-4444-444444444444', 'Video 4', 'https://example.com/video4.mp4', 'https://example.com/thumb4.png', 210, NOW(), NOW()),
    ('55555555-5555-5555-5555-555555555555', 'Video 5', 'https://example.com/video5.mp4', 'https://example.com/thumb5.png', 150, NOW(), NOW());
