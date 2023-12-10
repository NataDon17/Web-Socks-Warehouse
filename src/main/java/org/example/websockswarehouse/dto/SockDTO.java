package org.example.websockswarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websockswarehouse.model.Sock;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SockDTO {
    private String color;
    private Integer cottonPart;
    private Integer quantity;

    public Sock toSock() {
        Sock sock = new Sock();
        sock.setColor(this.getColor());
        sock.setCottonPart(this.getCottonPart());
        sock.setQuantity(this.getQuantity());
        return sock;
    }
    public static SockDTO fromSocks(Sock sock) {
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor(sock.getColor());
        sockDTO.setCottonPart(sock.getCottonPart());
        sockDTO.setQuantity(sock.getQuantity());
        return sockDTO;
    }
}
