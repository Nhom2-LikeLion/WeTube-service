INSERT INTO users (id, avatar_url, created_at, email, name, password, updated_at, channel_id) 
VALUES (
    '123e4567-e89b-12d3-a456-426614174000','https://example.com/avatar.png',NOW(),
    'demo@example.com','Demo User','$2a$10$7Q/9bE8Z0x3.vbBkRz6xeO9ZsQZDlZ/JYpJ5UOml8ZCuzv1UOv1xO', -- password hash "123456"
    NOW(),
    NULL
);
INSERT INTO channels (
    id, avatar_url, background_img_url, country, created_at, description, name,
    revenue, status, total_subscribers, total_videos, total_views, updated_at
) VALUES (
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','https://example.com/channel-avatar.png','https://example.com/channel-bg.png',
    'VIETNAM',NOW(),'Kênh demo tổng hợp video','Demo Channel',0.0,'ACTIVE',0,0,0,NOW()
);
INSERT INTO videos (id, title, video_url, thumbnail_url, duration, created_at, updated_at)
VALUES
('11111111-1111-1111-1111-111111111111', 'Video 1', 'https://example.com/video1.mp4', 'https://example.com/thumb1.png', 120, NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'Video 2', 'https://example.com/video2.mp4', 'https://example.com/thumb2.png',95, NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'Video 3', 'https://example.com/video3.mp4', 'https://example.com/thumb3.png', 180, NOW(), NOW()),
('44444444-4444-4444-4444-444444444444', 'Video 4', 'https://example.com/video4.mp4', 'https://example.com/thumb4.png', 210, NOW(), NOW()),
('55555555-5555-5555-5555-555555555555', 'Video 5', 'https://example.com/video5.mp4', 'https://example.com/thumb5.png', 150, NOW(), NOW());

