package com.wetube.wetube_service.Repository;

import com.wetube.wetube_service.entity.Channel.Channel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ChannelRepository extends CrudRepository<Channel, UUID> {
}
