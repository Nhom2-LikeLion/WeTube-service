package com.wetube.wetube_service.repository.channel;

import com.wetube.wetube_service.entity.channel.Channel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ChannelRepository extends CrudRepository<Channel, UUID> {
}
