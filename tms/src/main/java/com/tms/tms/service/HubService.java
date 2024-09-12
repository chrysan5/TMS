package com.tms.tms.service;

import com.tms.tms.dto.HubRequestDto;
import com.tms.tms.dto.HubResponseDto;
import com.tms.tms.exception.ErrorCode;
import com.tms.tms.exception.TmsCustomException;
import com.tms.tms.model.Hub;
import com.tms.tms.repository.HubRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HubService {
    private final HubRepository hubRepository;

    @Transactional
    public HubResponseDto createHub(@Valid HubRequestDto hubRequestDto) {
        Hub hub = hubRepository.save(new Hub(hubRequestDto));
        return new HubResponseDto(hub);
    }

    @Transactional
    public HubResponseDto updateHub(Long hubId, @Valid HubRequestDto hubRequestDto) {
        Hub hub = findByIdOrElseThrow(hubId);
        hub.updateHub(hubRequestDto);
        return new HubResponseDto(hub);
    }

    @Transactional
    public void deleteHub(Long hubId, String username) {
        Hub hub = findByIdOrElseThrow(hubId);

        hub.setDelete(true);
        hub.delete(username);
    }

    public HubResponseDto getHub(Long hubId) {
        Hub hub = findByIdOrElseThrow(hubId);
        return new HubResponseDto(hub);
    }

    public Page<HubResponseDto> getHubs(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Hub> hubList = hubRepository.findAll(pageable);
        return hubList.map(HubResponseDto::new);
    }

    public List<HubResponseDto> getHubSearch(String keyword) {
        List<Hub> hubList = hubRepository.findAllByHubRegionContaining(keyword);
        return hubList.stream()
                .map(HubResponseDto::new)
                .collect(Collectors.toList());
    }

    public Hub findByIdOrElseThrow(Long hubId){
        return hubRepository.findById(hubId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_HUB)
        );
    }
}
