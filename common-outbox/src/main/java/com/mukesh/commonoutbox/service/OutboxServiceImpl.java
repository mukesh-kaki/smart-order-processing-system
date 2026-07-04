package com.mukesh.commonoutbox.service;

import com.mukesh.commonoutbox.entity.EventStatus;
import com.mukesh.commonoutbox.entity.OutboxEntity;
import com.mukesh.commonoutbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxServiceImpl
        implements OutboxService {

    private final OutboxRepository repository;

    @Override
    public OutboxEntity save(
            OutboxEntity event
    ) {

        return repository.save(event);

    }

    @Override
    public List<OutboxEntity> findByStatus(
            EventStatus status
    ) {

        return repository.findByStatusOrderByCreatedAtAsc(
                status,
                PageRequest.of(0, 100)
        );

    }

    @Override
    public OutboxEntity update(
            OutboxEntity event
    ) {

        return repository.save(event);

    }

}