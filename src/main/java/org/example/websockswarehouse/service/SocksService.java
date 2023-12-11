package org.example.websockswarehouse.service;

import org.example.websockswarehouse.dto.SockDTO;
import org.example.websockswarehouse.model.SocksOperation;

public interface SocksService {
    void arrivalSocks(SockDTO sockDTO);

    void dispatchSocks(SockDTO sockDTO);

    int findSocks(String color, int cottonPart, SocksOperation operation);
}
