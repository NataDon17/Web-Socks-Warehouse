package org.example.websockswarehouse.controller;

import lombok.RequiredArgsConstructor;
import org.example.websockswarehouse.dto.SockDTO;
import org.example.websockswarehouse.model.SocksOperation;
import org.example.websockswarehouse.service.SocksService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class WarehouseController {
    private final SocksService socksService;

    @PostMapping("/income")
    public void arrivalSocks(@RequestBody SockDTO sockDTO) {
        socksService.arrivalSocks(sockDTO);
    }

    @PostMapping("/outcome")
    public void dispatchSocks(@RequestBody SockDTO sockDTO) {
        socksService.dispatchSocks(sockDTO);
    }

    @GetMapping("/")
    public int findSocks(@RequestParam("color") String color,
                         @RequestParam("cottonPart") int cottonPart,
                         @ModelAttribute("operation") SocksOperation operation) {
        return socksService.findSocks(color, cottonPart, operation);
    }
}
