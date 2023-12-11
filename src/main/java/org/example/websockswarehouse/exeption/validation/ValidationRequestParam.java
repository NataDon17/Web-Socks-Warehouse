package org.example.websockswarehouse.exeption.validation;

import org.example.websockswarehouse.dto.SockDTO;
import org.example.websockswarehouse.exeption.SockInvalidParameterException;

public class ValidationRequestParam {
    public static void checkParams(SockDTO sockDTO) {
        if(sockDTO.getQuantity()==null || sockDTO.getCottonPart()==null || sockDTO.getColor()==null){
            throw new SockInvalidParameterException();
        }
        if (sockDTO.getQuantity() < 0) {
            throw new SockInvalidParameterException();   //"Quantity should be greater than 0");
        }
        if (sockDTO.getCottonPart() < 0 || sockDTO.getCottonPart() > 100) {
            throw new SockInvalidParameterException();   //"Cotton part should be between 0 and 100");
        }
        if (sockDTO.getColor().isEmpty()) {
            throw new SockInvalidParameterException();   //"Color cannot be empty");
        }
    }
}
