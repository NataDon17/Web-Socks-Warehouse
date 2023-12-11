package org.example.websockswarehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.websockswarehouse.dto.SockDTO;
import org.example.websockswarehouse.exeption.SockNotFoundException;
import org.example.websockswarehouse.model.Sock;
import org.example.websockswarehouse.model.SocksOperation;
import org.example.websockswarehouse.repository.SocksRepository;
import org.example.websockswarehouse.service.SocksService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.websockswarehouse.exeption.validation.ValidationRequestParam.checkParams;

@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {
    private final SocksRepository socksRepository;

    public int getQuantitySocks(SockDTO sockDTO) {
        checkParams(sockDTO);
        List<Sock> sockList = socksRepository.findAll();
        return sockList.stream()
                .map(SockDTO::fromSocks)
                .filter(sock -> sock.getColor().equals(sockDTO.getColor()) &&
                        sock.getCottonPart().equals(sockDTO.getCottonPart()))
                .mapToInt(SockDTO::getQuantity)
                .sum();
    }

    @Override
    public void arrivalSocks(SockDTO sockDTO) {
        if (getQuantitySocks(sockDTO) == 0) {
            socksRepository.save(sockDTO.toSock());
        } else {
            socksRepository.updateQuantityWhenSocksArrive(
                    sockDTO.getColor(),
                    sockDTO.getCottonPart(),
                    sockDTO.getQuantity()
            );
        }
    }

    @Override
    public void dispatchSocks(SockDTO sockDTO) {
        if (getQuantitySocks(sockDTO) != 0) {
            socksRepository.updateQuantityWhenSocksDisposal(
                    sockDTO.getColor(),
                    sockDTO.getCottonPart(),
                    sockDTO.getQuantity()
            );
        } else {
            throw new SockNotFoundException();
        }
    }

    @Override
    public int findSocks(String color, int cottonPart, SocksOperation operation) {
        List<Sock> sockList = socksRepository.findAll();
        if (sockList.isEmpty()) {
            throw new SockNotFoundException();
        } else {
            return sockList.stream()
                    .map(SockDTO::fromSocks)
                    .filter(sock -> sock.getColor().equals(color) && compare(cottonPart, sock.getCottonPart(), operation))
                    .mapToInt(SockDTO::getQuantity)
                    .sum();
        }
    }

    private boolean compare(int value1, int value2, SocksOperation operation) {
        return switch (operation) {
            case moreThan -> value1 < value2;
            case lessThan -> value1 > value2;
            case equal -> value1 == value2;
        };
    }
}
