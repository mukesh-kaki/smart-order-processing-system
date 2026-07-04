package com.mukesh.commonoutbox.service;

import com.mukesh.commonoutbox.entity.EventStatus;
import com.mukesh.commonoutbox.entity.OutboxEntity;

import java.util.List;
import java.util.UUID;

public interface OutboxService {

    OutboxEntity save(OutboxEntity event);

    List<OutboxEntity> findByStatus(EventStatus status);

    OutboxEntity update(OutboxEntity event);

}